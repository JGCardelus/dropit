package net.buffer.events;

import java.util.List;

import packet.Packet;
import packet.types.BufferHeaderPacket;

public class BufferCompleteEvent {
	private List<Packet> packets;
	private BufferHeaderPacket header;
	
	public BufferCompleteEvent(BufferHeaderPacket header, List<Packet> packets) {
		this.setHeader(header);
		this.setPackets(packets);
	}

	public List<Packet> getPackets() {
		return packets;
	}

	public void setPackets(List<Packet> packets) {
		this.packets = packets;
	}

	public BufferHeaderPacket getHeader() {
		return header;
	}

	public void setHeader(BufferHeaderPacket header) {
		this.header = header;
	}
}
