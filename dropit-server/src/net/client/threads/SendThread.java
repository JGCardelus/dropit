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

	public List<Packet> clearPacketsOfPriority(int n) {
		List<Packet> packets = new LinkedList<Packet>();
		for (Packet packet : this.getPackets())
			if (packet.getPriority() == n)
				packets.add(packet);

		this.packets.removeAll(packets);
		return packets;
	}

	public List<Packet> clearPacketsOfPriority(int n, int size) {
		List<Packet> packets = new LinkedList<Packet>();
		for (Packet packet : this.getPackets()) {
			if (packet.getPriority() == n)
				packets.add(packet);
			if (packets.size() == size)
				break;
		}

		this.packets.removeAll(packets);
		return packets;
	}

	@Override
	public void run() {
		try {
			ObjectOutputStream oos = new ObjectOutputStream(this.client.getSocket().getOutputStream());
			
			while (true) {
				// Clear parent's queue
				this.packets.addAll(this.client.clearPackets());
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
			oos.writeObject(packet);
			oos.flush();
			System.out.println(packet);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
