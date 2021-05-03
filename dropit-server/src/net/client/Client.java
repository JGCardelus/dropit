package net.client;

import java.io.IOException;
import java.net.Socket;
import java.util.LinkedList;
import java.util.List;

import net.client.threads.ReadThread;
import net.client.threads.SendThread;
import net.server.Server;
import packet.Packet;

public class Client extends Thread {
	public static final int MID_PRIORITY_CLEAR_RATE = 4;
	
	private Server server;

	private Socket socket;
	private boolean isLive;

	private List<Packet> packets;

	// Internal threads
	private ReadThread readThread;
	private SendThread sendThread;

	public Client(Server server, Socket socket) {
		this.setPackets(new LinkedList<Packet>());
		this.setServer(server);
		this.setSocket(socket);
	}

	@Override
	public void run() {
		this.setLive(true);
		// Start reader
		this.readThread = new ReadThread(this);
		this.readThread.start();
		// Start sender
		this.sendThread = new SendThread(this);
		this.sendThread.start();
	}

	public Server getServer() {
		return server;
	}

	public void setServer(Server server) {
		this.server = server;
	}

	public synchronized List<Packet> getPackets() {
		return packets;
	}

	public synchronized void setPackets(List<Packet> packets) {
		this.packets = packets;
	}

	public synchronized List<Packet> clearPackets() {
		List<Packet> packets = this.getPackets();
		this.packets = new LinkedList<Packet>();
		return packets;
	}

	public boolean isLive() {
		return isLive;
	}

	public void setLive(boolean isLive) {
		this.isLive = isLive;
	}

	public synchronized Socket getSocket() {
		return socket;
	}

	private synchronized void setSocket(Socket socket) {
		this.socket = socket;
	}

	public void handlePacket(Packet packet) {
		System.out.println(packet.getId());
	}

	public void close() {
		this.server.close(this);

		try {
			this.socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public synchronized void send(Packet packet) {
		this.packets.add(packet);
	}

	public synchronized void send(List<Packet> packets) {
		for (Packet packet : packets)
			this.packets.add(packet);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((socket == null) ? 0 : socket.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Client other = (Client) obj;
		if (socket == null) {
			if (other.socket != null)
				return false;
		} else if (!socket.equals(other.socket))
			return false;
		return true;
	}
}
