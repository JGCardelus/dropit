package net.buffer;

import java.io.File;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import io.IOManager;
import io.threads.files.FileWriteThread;
import net.buffer.events.BufferAdapter;
import net.buffer.events.BufferEvent;
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
			public void onBufferComplete(BufferEvent event) {
					List<FilePacket> filePackets = new LinkedList<FilePacket>();
					for (Packet packet : event.getPackets()) {
						if (packet instanceof FilePacket)
							filePackets.add((FilePacket) packet);
					}
					IOManager io = new IOManager(Buffers.this.client.getServer());
					int number = new Random().ints(0, 256).findFirst().getAsInt();
					FileWriteThread fileWrite = io.writeFile(
						new File("recv/" + number + ".jpg"), 
						event.getHeader(),
						filePackets);
					fileWrite.start();
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
