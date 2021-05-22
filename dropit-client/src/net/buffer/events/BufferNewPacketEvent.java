package net.buffer.events;

import net.buffer.Buffer;
import packet.Packet;

public class BufferNewPacketEvent {
	private Packet packet;
	private Buffer buffer;
	
	public BufferNewPacketEvent(Buffer buffer, Packet packet) {
		this.setBuffer(buffer);
		this.setPacket(packet);
	}

	public Packet getPacket() {
		return packet;
	}
	public void setPacket(Packet packet) {
		this.packet = packet;
	}
	public Buffer getBuffer() {
		return buffer;
	}
	public void setBuffer(Buffer buffer) {
		this.buffer = buffer;
	}
}
