package net.client;

import java.util.LinkedList;
import java.util.List;

import packet.Packet;

/**
 * {@link Packet} with QOS_LEVEL_1 need a confirmation from the client, acknowledging
 * the {@link Packet}. It after a certain time a {@link Packet} hasn't been confirmed it is
 * sent again. {@link UnconfirmedPacketManager} is in charge of handling said {@link Packet}.
 */
public class UnconfirmedPacketManager {
	public static final int WAIT_TIME = 15;
	public List<UnconfirmedPacket> unconfirmedPackets = new LinkedList<UnconfirmedPacket>();

	public synchronized boolean contains(Packet packet) {
		for (UnconfirmedPacket unconfirmedPacket : this.unconfirmedPackets) {
			if (packet.equals(unconfirmedPacket.getPacket())) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Adds a Packet to the manager's watch list.
	 * @param packet
	 */
	public synchronized void add(Packet packet) {
		if (!this.contains(packet)) {
			UnconfirmedPacket newUnconfirmedPacket = new UnconfirmedPacket(
				packet, 
				(int) System.currentTimeMillis() / 1000
			);
			this.unconfirmedPackets.add(newUnconfirmedPacket);
		}
	}

	/**
	 * Removes a {@link Packet} from the manager because it has been confirmed.
	 * @param packet
	 */
	public synchronized void remove(Packet packet) {
		UnconfirmedPacket placeholderPacket = new UnconfirmedPacket(packet, 0);
		this.unconfirmedPackets.remove(placeholderPacket);
	}

	/**
	 * Gets the Packets that need to be resent since their waiting time has expired.
	 * @return
	 */
	public synchronized List<Packet> getResendPackets() {
		List<Packet> packets = new LinkedList<Packet>();
		int currentTime = (int) System.currentTimeMillis() / 1000;
		for (UnconfirmedPacket unconfirmedPacket : this.unconfirmedPackets) {
			if (currentTime - unconfirmedPacket.getStart() >= WAIT_TIME) {
				unconfirmedPacket.setStart(currentTime);
				packets.add(unconfirmedPacket.getPacket());
			}
		}

		return packets;
	}
}
