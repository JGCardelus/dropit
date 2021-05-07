package net.client;

import java.net.Socket;
import java.util.LinkedList;
import java.util.List;

import net.buffer.Buffers;
import net.client.events.ClientAdapter;
import net.client.events.PacketArrivedEvent;
import net.client.threads.ReadThread;
import net.client.threads.SendThread;
import net.server.Server;
import packet.Packet;
import packet.types.BufferHeaderPacket;

public class Client extends Thread {
	public static final int MID_PRIORITY_CLEAR_RATE = 4;
	
	private Server server;

	private Buffers buffers;

	private Socket socket;
	private boolean isLive;

	private List<Packet> packets;
	private List<Packet> unconfirmedPackets; // Packets with QoS 1

	// Internal threads
	private ReadThread readThread;
	private SendThread sendThread;

	// Events
	private List<ClientAdapter> clientAdapters = new LinkedList<>();

	public Client(Server server, Socket socket) {
		this.setPackets(new LinkedList<Packet>());
		this.setUnconfirmedPackets(new LinkedList<Packet>());
		this.setServer(server);
		this.setSocket(socket);
		this.setBuffers(new Buffers(this));
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

	public List<Packet> getUnconfirmedPackets() {
		return this.unconfirmedPackets;
	}

	public void setUnconfirmedPackets(List<Packet> unconfirmedPackets) {
		this.unconfirmedPackets = unconfirmedPackets;
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
		for (Packet packet : packets) {
			if (packet.getQos() == Packet.QOS_LEVEL_1) {
				if (!this.unconfirmedPackets.contains(packet))
					this.unconfirmedPackets.add(packet);
			}
		}
		this.packets = new LinkedList<Packet>();
		return packets;
	}

	// Packet might have arrived with errors, resend original one
	public void resendPacket(Packet packet) {
		Packet originalPacket = null;
		for (Packet testPacket : this.unconfirmedPackets)
			if (testPacket.equals(packet))
				originalPacket = testPacket;

		if (originalPacket != null) {
			this.unconfirmedPackets.remove(originalPacket);
			this.send(originalPacket);
		}
	}

	public void confirmPacket(Packet packet) {
		this.unconfirmedPackets.remove(packet);
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

	public synchronized void handlePacket(Packet packet) {
		switch(packet.getCode()) {
			case Packet.CODE_CONFIRMATION:
				this.confirmPacket(packet);
			break;
			case Packet.CODE_RESEND:
				this.resendPacket(packet);
			break;
			case Packet.CODE_FILEHEADER:
				if (packet instanceof BufferHeaderPacket)
					this.buffers.handleBufferHeaderPacket((BufferHeaderPacket) packet);
			break;
			default:
				this.triggerPacketArrived(packet);
			break;
		}
	}

	public Buffers getBuffers() {
		return buffers;
	}

	public void setBuffers(Buffers buffers) {
		this.buffers = buffers;
	}

	public void close() {
		this.server.close(this);
	}

	public synchronized void send(Packet packet) {
		this.packets.add(packet);
	}

	public synchronized void send(List<Packet> packets) {
		for (Packet packet : packets)
			this.packets.add(packet);
	}

	public synchronized List<ClientAdapter> getClientAdapters() {
		return this.clientAdapters;
	}

	public synchronized void triggerPacketArrived(Packet packet) {
		PacketArrivedEvent event = new PacketArrivedEvent(packet);
		List<ClientAdapter> adapters = this.getClientAdapters();
		for (int i = 0; i < adapters.size(); i++) {
			adapters.get(i).onPacketArrived(event);
		}
		
	}

	public ClientAdapter addClientListener(ClientAdapter adapter) {
		this.getClientAdapters().add(adapter);
		return adapter;
	}

	public void removeClientListener(ClientAdapter adapter) {
		this.getClientAdapters().remove(adapter);
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
