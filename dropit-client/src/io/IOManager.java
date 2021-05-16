package io;

import java.io.File;
import java.util.List;

import basic.IdGenerator;
import io.threads.files.FileReadThread;
import io.threads.files.FileWriteThread;
import net.client.Client;
import packet.types.BufferHeaderPacket;
import packet.types.FilePacket;

public class IOManager implements IdGenerator{
	private Client client;
	
	public IOManager(Client client) {
		this.setClient(client);
	}

	public FileReadThread readFile(File file) {
		return new FileReadThread
		(
			file,
			this.client, 
			this
		);
	}

	public FileWriteThread writeFile(File file, BufferHeaderPacket bufferHeaderPacket, List<FilePacket> packets) {
		return new FileWriteThread(file, bufferHeaderPacket, packets);
	}

	public Client getServer() {
		return client;
	}

	public void setClient(Client client) {
		this.client = client;
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
