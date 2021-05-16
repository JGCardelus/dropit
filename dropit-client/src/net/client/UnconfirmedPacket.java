package net.client;

import packet.Packet;

public class UnconfirmedPacket {
	private Packet packet;
	private int start;

	public UnconfirmedPacket(Packet packet, int start) {
		this.setPacket(packet);
		this.setStart(start);
	}
	public Packet getPacket() {
		return packet;
	}
	public void setPacket(Packet packet) {
		this.packet = packet;
	}
	public int getStart() {
		return start;
	}
	public void setStart(int start) {
		this.start = start;
	}

	@Override
	public boolean equals(Object object) {
		return packet.equals(object);
	}
}
