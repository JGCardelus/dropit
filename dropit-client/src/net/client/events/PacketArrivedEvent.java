package net.client.events;

import packet.Packet;

public class PacketArrivedEvent {
	private Packet packet;

	public PacketArrivedEvent(Packet packet) {
		this.setPacket(packet);
	}

	public Packet getPacket() {
		return packet;
	}

	public void setPacket(Packet packet) {
		this.packet = packet;
	}

	
}
