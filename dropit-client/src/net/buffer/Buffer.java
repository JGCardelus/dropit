package net.buffer;

import java.util.LinkedList;
import java.util.List;

import net.buffer.events.BufferAdapter;
import net.buffer.events.BufferCompleteEvent;
import net.client.Client;
import net.client.events.ClientAdapter;
import net.client.events.PacketArrivedEvent;
import packet.Packet;
import packet.types.BufferHeaderPacket;

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
		System.out.println(bufferHeaderPacket.getPacketIds());
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
		int packetsCount = 0;
		for (Packet packet : this.packets) {
			if (packet != null) {
				packetsCount += 1;
			}
		}
		if (packetsCount ==this.packetIds.size()) {
			this.setComplete(true);
			this.client.removeClientListener(this.getAdapter());
			this.triggerOnBufferComplete();
		}
	}
	
	private void handlePacket(Packet packet) {
		if (this.packetIds.contains(packet.getId())) {
			int index = this.packetIds.indexOf(packet.getId());
			this.packets.add(index, packet);
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

	public void triggerOnBufferComplete() {
		BufferCompleteEvent event = new BufferCompleteEvent(this.getHeader(), this.getPackets());
		for (BufferAdapter adapter : this.bufferAdapters) {
			adapter.onBufferComplete(event);
		}
	}

	public void addBufferListener(BufferAdapter adapter) {
		this.bufferAdapters.add(adapter);
	}
}
