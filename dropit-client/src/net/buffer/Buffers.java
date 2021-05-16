package net.buffer;

import java.io.File;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import io.IOManager;
import io.threads.files.FileWriteThread;
import net.buffer.events.BufferAdapter;
import net.buffer.events.BufferCompleteEvent;
import net.client.Client;
import packet.Packet;
import packet.types.BufferHeaderPacket;
import packet.types.FilePacket;

public class Buffers {
	private Client client;

	private List<Buffer> buffers = new LinkedList<>();

	public Buffers(Client client) {
		this.setClient(client);
	}

	public void handleBufferHeaderPacket(BufferHeaderPacket header) {
		Buffer buffer = new Buffer(header, this.client);
		this.buffers.add(buffer);
		buffer.addBufferListener(new BufferAdapter() {
			@Override
			public void onBufferComplete(BufferCompleteEvent event) {
					System.out.println("Buffer complete");
				}
			});
		
	}

	public Client getClient() {
		return client;
	}

	public void setClient(Client client) {
		this.client = client;
	}
}
