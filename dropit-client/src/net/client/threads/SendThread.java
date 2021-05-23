package net.client.threads;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.LinkedList;
import java.util.List;

import net.client.Client;
import packet.Packet;

public class SendThread extends Thread {
	private Client client;
	private List<Packet> packets;

	public static final int MAX_CHECK_COUNT = 50;

	public SendThread(Client client) {
		this.setClient(client);
		this.setPackets(new LinkedList<Packet>());
	}

	public List<Packet> getPackets() {
		return packets;
	}

	public void setPackets(List<Packet> packets) {
		this.packets = packets;
	}

	private int getCheckSize() {
		int size = this.getPackets().size();
		if (size > MAX_CHECK_COUNT) {
			size = MAX_CHECK_COUNT;
		}
		return size;
	}

	public List<Packet> clearPacketsOfPriority(int n) {
		List<Packet> packets = new LinkedList<Packet>();

		// List<Integer> packetsToRemove = new LinkedList<Integer>();
		for (int i = 0; i < this.getCheckSize(); i++) {
			Packet packet = this.getPackets().get(i);
			if (packet.getPriority() == n) {
				packets.add(packet);
				this.packets.remove(i);
			}
		}

		// this.removePackets(packetsToRemove);
		return packets;
	}

	public void removePackets(List<Integer> ids) {
		for (int i = 0; i < ids.size(); i++) {
			int k = (int) ids.get(i);
			this.packets.remove(k);

			if (i + 1 < ids.size()) {
				for (int j = i+1; j < ids.size(); j++) {
					int m = (int) ids.get(j);
					if (m > k) {
						ids.set(j, m-1);
					}
				}
			}
		}
	}

	public List<Packet> clearPacketsOfPriority(int n, int size) {
		List<Packet> packets = new LinkedList<Packet>();

		// List<Integer> packetsToRemove = new LinkedList<Integer>();
		for (int i = 0; i < this.getCheckSize(); i++) {
			Packet packet = this.getPackets().get(i);
			if (packet.getPriority() == n) {
				packets.add(packet);
				// packetsToRemove.add(i);
				this.packets.remove(i);
			}
			if (packets.size() == size)
				break;
		}

		// this.removePackets(packetsToRemove);
		return packets;
	}


	public void clearPackets() {
		for (Packet packet : this.client.clearPackets()) {
			if (packet.getPriority() == Packet.MAX_PRIORITY) {
				this.packets.add(0, packet);
			} else if (packet.getPriority() == Packet.MID_PRIORITY) {
				int index = 1;
				if (this.packets.size() == 0) 
					index = 0;
				this.packets.add(index, packet);
			} else {
				this.packets.add(packet);
			}
		}
	}

	@Override
	public void run() {
		try {
			ObjectOutputStream oos = new ObjectOutputStream(this.client.getSocket().getOutputStream());
			while (true) {
				// Clear parent's queue
				this.clearPackets();
				// Get and clear all packets of max priority
				for (Packet packet : this.clearPacketsOfPriority(Packet.MAX_PRIORITY))
					this.send(oos, packet);
				// Get and clear MID_PRIORITY_CLEAR_RATE packets of mid priority
				for (Packet packet : this.clearPacketsOfPriority(Packet.MID_PRIORITY, Client.MID_PRIORITY_CLEAR_RATE))
					this.send(oos, packet);
				// Get and clear 1 packet of no_priority
				for (Packet packet : this.clearPacketsOfPriority(Packet.NO_PRIORITY, 1))
					this.send(oos, packet);
			}
		} catch (IOException ioe) {
			// The socket has closed
			this.client.close();
		}
	}

	public Client getClient() {
		return client;
	}

	public void setClient(Client client) {
		this.client = client;
	}

	private void send(ObjectOutputStream oos, Packet packet) {
		try {
			if (packet.getQos() == Packet.QOS_LEVEL_1) {
				this.client.getUnconfirmedPacketManager().add(packet);
			}
			oos.writeObject(packet);
			oos.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
