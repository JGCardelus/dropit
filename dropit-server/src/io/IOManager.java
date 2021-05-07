package io;

import java.io.File;
import java.util.List;

import basic.IdGenerator;
import io.threads.files.FileReadThread;
import io.threads.files.FileWriteThread;
import net.server.Server;
import packet.Packet;
import packet.types.FileHeaderPacket;
import packet.types.FilePacket;

public class IOManager implements IdGenerator{
	private Server server;
	
	public IOManager(Server server) {
		this.setServer(server);
	}

	public FileReadThread readFile(File file) {
		return new FileReadThread
		(
			file,
			this.server, 
			this
		);
	}

	public FileWriteThread writeFile(File file, FileHeaderPacket fileHeaderPacket, List<FilePacket> packets) {
		return new FileWriteThread(file fileHeaderPacket, packets);
	}

	public Server getServer() {
		return server;
	}

	public void setServer(Server server) {
		this.server = server;
	}

	private int id = 0; // Starts always at zero
	@Override
	public synchronized int nextId() {
		this.id += 1;
		if (Integer.MAX_VALUE == this.id)
			this.id = 0;
		return id;
	}
}
