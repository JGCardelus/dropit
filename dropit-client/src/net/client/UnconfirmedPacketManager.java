package net.client;

import java.util.LinkedList;
import java.util.List;

import packet.Packet;

public class UnconfirmedPacketManager {
	public static final int WAIT_TIME = 10;
	public List<UnconfirmedPacket> unconfirmedPackets = new LinkedList<UnconfirmedPacket>();

	public boolean contains(Object object) {
		return unconfirmedPackets.contains(object);
	}

	public void add(Packet packet) {
		if (!this.contains(packet)) {
			UnconfirmedPacket newUnconfirmedPacket = new UnconfirmedPacket(
				packet, 
				(int) System.currentTimeMillis() / 1000
			);
			this.unconfirmedPackets.add(newUnconfirmedPacket);
		}
	}

	public void remove(Object object) {
		this.unconfirmedPackets.remove(object);
	}

	public synchronized List<Packet> getResendPackets() {
		List<Packet> packets = new LinkedList<Packet>();
		int currentTime = (int) System.currentTimeMillis() / 1000;
		for (UnconfirmedPacket unconfirmedPacket : this.unconfirmedPackets) {
			if (currentTime - unconfirmedPacket.getStart() >= WAIT_TIME) {
				unconfirmedPacket.setStart(currentTime);
				packets.add(unconfirmedPacket.getPacket());
			}
		}

		if (!packets.isEmpty()) {
			System.out.println("Resending:");
			System.out.println(packets);
		}

		return packets;
	}
}
