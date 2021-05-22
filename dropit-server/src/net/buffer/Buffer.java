package net.buffer;

import java.util.LinkedList;
import java.util.List;

import net.buffer.events.BufferAdapter;
import net.buffer.events.BufferCompleteEvent;
import net.buffer.events.BufferNewPacketEvent;
import net.buffer.events.FileCompleteEvent;
import net.client.Client;
import net.client.events.ClientAdapter;
import net.client.events.PacketArrivedEvent;
import packet.Packet;
import packet.types.BufferHeaderPacket;
import packet.types.FileHeaderPacket;

public class Buffer {
	private Client client;
	private BufferHeaderPacket header;
	private ClientAdapter adapter;
	private List<Packet> packets = new LinkedList<>();
	private List<Integer> packetIds = new LinkedList<>();

	private boolean isComplete;

	// Events
	private List<BufferAdapter> bufferAdapters = new LinkedList<>();

	public Buffer(BufferHeaderPacket bufferHeaderPacket, Client client) {
		this.setPacketIds(bufferHeaderPacket.getPacketIds());
		this.setHeader(bufferHeaderPacket);
		this.setClient(client);
		this.initPacketArrivedListener();
	}
	
	public List<Packet> getPackets() {
		return packets;
	}
	public void setPackets(List<Packet> packets) {
		this.packets = packets;
	}
	public List<Integer> getPacketIds() {
		return packetIds;
	}
	public void setPacketIds(List<Integer> packetIds) {
		this.packetIds = packetIds;
	}

	public boolean isComplete() {
		return isComplete;
	}

	public void setComplete(boolean isComplete) {
		this.isComplete = isComplete;
	}

	public Client getClient() {
		return client;
	}

	public void setClient(Client client) {
		this.client = client;
	}

	public BufferHeaderPacket getHeader() {
		return header;
	}

	public void setHeader(BufferHeaderPacket header) {
		this.header = header;
	}

	public synchronized ClientAdapter getAdapter() {
		return this.adapter;
	}

	private void checkBufferState() {
		int packetsCount = this.getPacketsCount();

		if (packetsCount ==this.packetIds.size()) {
			this.setComplete(true);
			this.client.removeClientListener(this.getAdapter());
			this.triggerOnBufferComplete();
		}
	}

	private int getPacketsCount() {
		int packetsCount = 0;
		for (Packet packet : this.packets) {
			if (packet != null) {
				packetsCount += 1;
			}
		}
		return packetsCount;
	}
	
	private void handlePacket(Packet packet) {
		if (this.packetIds.contains(packet.getId())) {
			int index = this.packetIds.indexOf(packet.getId());
			this.packets.add(index, packet);
			this.triggerOnBufferNewPacket(packet);
			this.checkBufferState();
		}
	}

	public void initPacketArrivedListener() {
		this.adapter = this.client.addClientListener(new ClientAdapter(){
			@Override
			public void onPacketArrived(PacketArrivedEvent event) {
				Buffer.this.handlePacket(event.getPacket());
			}
		});
	}

	private void triggerOnBufferNewPacket(Packet packet) {
		BufferNewPacketEvent event = new BufferNewPacketEvent(this, packet);
		for (BufferAdapter adapter : this.bufferAdapters) {
			adapter.onBufferNewPacket(event);
		}
	}

	private void triggerOnBufferComplete() {
		BufferCompleteEvent event = new BufferCompleteEvent(this.getHeader(), this.getPackets());
		for (BufferAdapter adapter : this.bufferAdapters) {
			adapter.onBufferComplete(event);
		}

		// An if-else statement would better for this block
		// but since it might grow, keeping as switch
		switch (this.getHeader().getBufferType()) {
			case FileHeaderPacket.BUFFER_TYPE:
				this.triggerOnFileComplete();
			break;
		}
	}

	private void triggerOnFileComplete() {
		if (this.getHeader() instanceof FileHeaderPacket) {
			FileCompleteEvent event = new FileCompleteEvent((FileHeaderPacket) this.getHeader(), this.getPackets());
			for (BufferAdapter adapter : this.bufferAdapters)
				adapter.onFileComplete(event);
		}
	}

	public void addBufferListener(BufferAdapter adapter) {
		this.bufferAdapters.add(adapter);
	}

	public int getPercentage() {
		float a = (float) this.getPacketsCount();
		float b = (float) this.packetIds.size();
		return (int) (a/b*100f);
	}
}
