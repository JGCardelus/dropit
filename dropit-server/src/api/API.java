package api;

import java.io.File;
import java.net.Inet4Address;
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
import net.server.Server;
import net.server.events.ServerAdapter;
import net.server.events.ServerNewClientEvent;
import packet.types.UserPacket;

/**
 * Wrapper around IOManager and Server for easy interaction between backend
 * and frontend.
 */
public class API {
	private Server server;
	private IOManager io;
	private User user;

	private String ip;
	private String roomCode;

	private String filePath = System.getProperty("user.dir");

	private List<ApiAdapter> apiAdapters = new LinkedList<ApiAdapter>();

	public API() {
		this.server = new Server();
		this.io = new IOManager(this.server);

		// Connect to clients events and clients' buffers events
		this.server.addServerListener(new ServerAdapter() {
			@Override
			public void onServerNewClient(ServerNewClientEvent event) {
				API.this.handleNewClient(event);
			}
		});
	}

	public void start() {
		this.server.start();
		this.setIp();
	}

	public void setIp() {
		try {
			this.ip = Inet4Address.getLocalHost().getHostAddress();
		} catch (UnknownHostException e) {
			this.ip = "127.0.0.1"; // Enter loopback mode
		}
		this.setRoomCode();
	}

	public String getIp() {
		return this.ip;
	}

	public void setRoomCode() {
		this.roomCode = this.ip.split("\\.")[3];
	}

	public String getRoomCode() {
		return this.roomCode;
	}

	public void send(File file) {
		FileReadThread frt = this.io.readFile(file);
		frt.addFileReadListener(new FileReadAdapter() {
			@Override
			public void onFileRead(FileReadEvent event) {
				API.this.server.propagate(event.getPackets());
			}
		});
		frt.start();
	}

	public void setUser(User user) {
		this.user = user;

		UserPacket userPacket = new UserPacket(this.server.nextId());
		userPacket.setUser(user);
		this.server.propagate(userPacket);
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

	public void triggerOnNewClient(ServerNewClientEvent event) {
		for (ApiAdapter apiAdapter : this.apiAdapters) {
			apiAdapter.onNewClient(event);
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

	private void handleNewClient(ServerNewClientEvent event) {
		Client client = event.getClient();
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

		this.triggerOnNewClient(event);
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
}
