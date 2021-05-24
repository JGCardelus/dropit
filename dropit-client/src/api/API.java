package api;

import java.io.File;
import java.io.IOException;
import java.net.Inet4Address;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.LinkedList;
import java.util.List;

import api.events.ApiAdapter;
import app.users.User;
import io.IOManager;
import io.events.FileReadAdapter;
import io.events.FileReadEvent;
import io.threads.files.FileReadThread;
import io.threads.files.FileWriteThread;
import net.buffer.events.BufferManagerAdapter;
import net.buffer.events.FileCompleteEvent;
import net.buffer.events.NewFileEvent;
import net.client.Client;
import net.client.events.ClientAdapter;
import net.client.events.UpdateUserEvent;
import packet.types.UserPacket;

/**
 * Wrapper around IOManager and Server for easy interaction between backend
 * and frontend.
 */
public class API {
	public static final int PORT = 8008;

	private Client client;
	private IOManager io;
	private User user;

	private String ip;
	private int roomCode;

	private String filePath = System.getProperty("user.home") + "\\Downloads\\test\\";

	private List<ApiAdapter> apiAdapters = new LinkedList<ApiAdapter>();

	public API(Socket socket) {
		this.client = new Client(socket);
		this.io = new IOManager(this.client);
	}

	public static Socket createSocketFromRoomCode(int roomCode) throws IOException {
		String ip;
		try {
			ip = Inet4Address.getLocalHost().getHostAddress();
		} catch (UnknownHostException e) {
			ip = "127.0.0.1"; // Enter loopback mode
		}

		StringBuilder serverIp = new StringBuilder();
		String[] ipChunks = ip.split("\\.");
		for (int i = 0; i<3; i++) {
			serverIp.append(ipChunks[i]).append(".");
		}
		serverIp.append(roomCode);
		return new Socket(serverIp.toString(), PORT);
	}

	public void start() {
		this.client.start();
		this.handleNewClient();
		this.setIp();

		if (this.user != null) {
			this.setUser(this.user);
		}
	}

	public void setIp() {
		try {
			this.ip = Inet4Address.getLocalHost().getHostAddress();
		} catch (UnknownHostException e) {
			this.ip = "127.0.0.1"; // Enter loopback mode
		}
	}

	public String getIp() {
		return this.ip;
	}

	public void setRoomCode(int roomCode) {
		this.roomCode = roomCode;
	}

	public int getRoomCode() {
		return this.roomCode;
	}

	public void send(File file) {
		FileReadThread frt = this.io.readFile(file);
		frt.addFileReadListener(new FileReadAdapter() {
			@Override
			public void onFileRead(FileReadEvent event) {
				API.this.client.send(event.getPackets());
			}
		});
		frt.start();
	}

	public void setUser(User user) {
		this.user = user;
		
		UserPacket userPacket = new UserPacket(this.client.nextId());
		userPacket.setUser(user);
		this.client.send(userPacket);
	}

	public User getUser() {
		return this.user;
	}

	private void handleFileComplete(FileCompleteEvent event) {
		File directory = new File(System.getProperty("user.dir"));
		if (this.filePath != null) {
			File test = new File(this.filePath);
			if (test.isDirectory())
				directory = test;
		}
		String fileName = event.getHeader().getFileName() + "." + event.getHeader().getFileExtension();
		File file = new File(directory.toString(), fileName);

		FileWriteThread fwt = this.io.writeFile(file, event.getHeader(), event.getFilePackets());
		fwt.start();

		this.triggerOnFileComplete(event);
	}

	public void addApiListener(ApiAdapter apiAdapter) {
		this.apiAdapters.add(apiAdapter);
	}

	public void triggerOnNewFile(NewFileEvent event) {
		for (ApiAdapter apiAdapter : this.apiAdapters) {
			apiAdapter.onNewFile(event);
		}
	}

	public void triggerOnFileComplete(FileCompleteEvent event) {
		for (ApiAdapter apiAdapter : this.apiAdapters) {
			apiAdapter.onFileComplete(event);
		}
	}

	public void triggerUpdateUser(UpdateUserEvent event) {
		for (ApiAdapter apiAdapter : this.apiAdapters) {
			apiAdapter.onUpdateUser(event);
		}
	}

	private void handleNewFile(NewFileEvent event) {
		this.triggerOnNewFile(event);
	}

	private void handleNewClient() {
		Client client = this.client;
		client.getBufferManager().addBufferManagerListener(new BufferManagerAdapter() {
			@Override
			public void onFileComplete(FileCompleteEvent event) {
				API.this.handleFileComplete(event);
			}

			@Override
			public void onNewFile(NewFileEvent event) {
				API.this.handleNewFile(event);
			}
		});
		client.addClientListener(new ClientAdapter() {
			@Override
			public void onUpdateUser(UpdateUserEvent event) {
				API.this.triggerUpdateUser(event);
			}
		});
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
}
