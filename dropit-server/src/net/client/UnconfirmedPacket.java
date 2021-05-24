package net.client;

import packet.Packet;

/**
 * {@link Packet} that needs to be confirmed. It is used by
 * the {@link UnconfirmedPacketManager}. It stores when the Packet was sent
 * to know when it has to be resent.
 */
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
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((packet == null) ? 0 : packet.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof UnconfirmedPacket))
			return false;
		UnconfirmedPacket other = (UnconfirmedPacket) obj;
		if (packet == null) {
			if (other.packet != null)
				return false;
		} else if (!packet.equals(other.packet))
			return false;
		return true;
	}

	
}
