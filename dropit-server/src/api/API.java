package api;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

import api.events.ApiAdapter;
import io.IOManager;
import io.events.FileReadAdapter;
import io.events.FileReadEvent;
import io.threads.files.FileReadThread;
import io.threads.files.FileWriteThread;
import net.buffer.events.BufferManagerAdapter;
import net.buffer.events.FileCompleteEvent;
import net.buffer.events.NewFileEvent;
import net.client.Client;
import net.server.Server;
import net.server.events.ServerAdapter;
import net.server.events.ServerNewClientEvent;

/**
 * Wrapper around IOManager and Server for easy interaction between backend
 * and frontend.
 */
public class API {
	private Server server;
	private IOManager io;

	private String filePath;

	private List<ApiAdapter> apiAdapters = new LinkedList<ApiAdapter>();

	public API() {
		this.server = new Server();
		this.io = new IOManager(this.server);

		// Connect to clients events and clients' buffers events
		this.server.addServerListener(new ServerAdapter() {
			@Override
			public void onServerNewClient(ServerNewClientEvent event) {
				API.this.handleNewClient(event.getClient());
			}
		});
	}

	public void start() {
		this.server.start();
	}

	public void send(File file) {
		FileReadThread frt = this.io.readFile(file);
		frt.addFileReadListener(new FileReadAdapter() {
			@Override
			public void onFileRead(FileReadEvent event) {
				API.this.server.propagate(event.getPackets());
				// File sent
				System.out.println("File sent");
			}
		});
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

	private void handleNewFile(NewFileEvent event) {
		this.triggerOnNewFile(event);
	}

	private void handleNewClient(Client client) {
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
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
}
