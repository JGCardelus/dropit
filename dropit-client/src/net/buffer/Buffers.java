package net.buffer;

import java.util.LinkedList;
import java.util.List;

import net.buffer.events.BufferAdapter;
import net.buffer.events.BufferEvent;
import net.client.Client;
import packet.Packet;
import packet.types.BufferHeaderPacket;

public class Buffers {
	private Client client;

	private List<Buffer> buffers = new LinkedList<>();

	public void handleBufferHeaderPacket(BufferHeaderPacket header) {
		Buffer buffer = new Buffer(header, this.client);
		this.buffers.add(buffer);
		buffer.addBufferListener(new BufferAdapter() {
			@Override
			public void onBufferComplete(BufferEvent event) {
				for (Packet packet : event.getPackets()) {
					System.out.println(packet);
				}
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
