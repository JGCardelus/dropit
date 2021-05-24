package net.client;

import java.net.Socket;
import java.util.LinkedList;
import java.util.List;

import app.users.User;
import net.buffer.BufferManager;
import net.client.events.ClientAdapter;
import net.client.events.PacketArrivedEvent;
import net.client.events.UpdateUserEvent;
import net.client.threads.ReadThread;
import net.client.threads.SendThread;
import net.server.Server;
import packet.Packet;
import packet.types.BufferHeaderPacket;
import packet.types.ConfirmationPacket;
import packet.types.UserPacket;

public class Client extends Thread {
	public static final int MID_PRIORITY_CLEAR_RATE = 4;
	
	private Server server;

	private BufferManager bufferManager;

	private Socket socket;
	private boolean isLive;

	private User user; // Each client is associated with one user

	private List<Packet> packets;
	private UnconfirmedPacketManager unconfirmedPacketManager = new UnconfirmedPacketManager(); // Packets with QoS 1

	// Internal threads
	private ReadThread readThread;
	private SendThread sendThread;

	// Events
	private List<ClientAdapter> clientAdapters = new LinkedList<>();

	public Client(Server server, Socket socket) {
		this.setPackets(new LinkedList<Packet>());
		this.setServer(server);
		this.setSocket(socket);
		this.setBufferManager(new BufferManager(this));
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

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
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
		// Add uncofirmed packets that need to be resent
		packets.addAll(this.unconfirmedPacketManager.getResendPackets());
		return packets;
	}

	// Check that package has been sent
	public void confirmPacket(Packet packet) {
		if (packet instanceof ConfirmationPacket) {
			// Create placholder packet
			ConfirmationPacket confirmationPacket = (ConfirmationPacket) packet;
			Packet placeholderPacket = new Packet(confirmationPacket.getPacketToConfirmId());
			this.unconfirmedPacketManager.remove(placeholderPacket);
		}
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
		if (packet.getQos() == Packet.QOS_LEVEL_1) {
			this.sendPacketConfirmation(packet);
		}

		switch(packet.getCode()) {
			case Packet.CODE_CONFIRMATION:
				this.confirmPacket(packet);
			break;
			case Packet.CODE_BUFFERHEADER:
				if (packet instanceof BufferHeaderPacket)
					this.bufferManager.handleBufferHeaderPacket((BufferHeaderPacket) packet);
			break;
			case Packet.CODE_USERPACKET:
				if (packet instanceof UserPacket)
					this.handleUserPacket((UserPacket) packet);
			break;
			default:
				this.triggerPacketArrived(packet);
			break;
		}
	}

	private synchronized void sendPacketConfirmation(Packet packet) {
		ConfirmationPacket confirmationPacket = new ConfirmationPacket(this.server.nextId());
		confirmationPacket.setPacketToConfirmId(packet.getId());
		this.send(confirmationPacket);
	}

	private synchronized void handleUserPacket(UserPacket packet) {
		this.setUser(packet.getUser());
		this.triggerUserUpdate();
	}

	public BufferManager getBufferManager() {
		return bufferManager;
	}

	public void setBufferManager(BufferManager bufferManager) {
		this.bufferManager = bufferManager;
	}

	public void close() {
		this.sendThread.interrupt();
		this.readThread.interrupt();
		this.server.close(this);
	}

	public synchronized void send(Packet packet) {
		this.packets.add(packet);
	}

	public synchronized void send(List<Packet> packets) {
		this.packets.addAll(packets);
	}

	public synchronized List<ClientAdapter> getClientAdapters() {
		return this.clientAdapters;
	}

	public synchronized void triggerPacketArrived(Packet packet) {
		PacketArrivedEvent event = new PacketArrivedEvent(packet);
		for (ClientAdapter adapter : this.getClientAdapters()) {
			adapter.onPacketArrived(event);
		}
		
	}

	private synchronized void triggerUserUpdate() {
		UpdateUserEvent event = new UpdateUserEvent(this);
		for (ClientAdapter adapter : this.getClientAdapters()) {
			adapter.onUpdateUser(event);
		}
	}


	public synchronized ClientAdapter addClientListener(ClientAdapter adapter) {
		this.getClientAdapters().add(adapter);
		return adapter;
	}

	public synchronized void removeClientListener(ClientAdapter adapter) {
		this.getClientAdapters().remove(adapter);
	}

	public synchronized UnconfirmedPacketManager getUnconfirmedPacketManager() {
		return this.unconfirmedPacketManager;
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
		if (!(obj instanceof Client))
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
