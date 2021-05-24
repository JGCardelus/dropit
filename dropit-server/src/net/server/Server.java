package net.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.LinkedList;
import java.util.List;

import basic.IdGenerator;
import net.client.Client;
import net.server.events.ServerAdapter;
import net.server.events.ServerNewClientEvent;
import net.server.threads.AcceptThread;
import packet.Packet;

/**
 * The server class is in charge of handling clients. It can progagate
 * messages to the network as well as sending messages to clients.
 */
public class Server extends Thread implements IdGenerator {
	public static final int PORT = 8008;
	
	private ServerSocket socket;
	private boolean isLive;
	private List<Client> clients;

	// Internal threads
	private AcceptThread acceptThread;

	// Events
	private List<ServerAdapter> serverAdapters = new LinkedList<ServerAdapter>();
	
	public Server() {
		this.setClients(new LinkedList<Client>());
	}

	@Override
	public void run() {
		try {
			this.socket = new ServerSocket(PORT);
			this.setLive(true);
			
			// Accepting new clients should be non-blocking
			acceptThread = new AcceptThread(this);
			acceptThread.start();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
	}

	public boolean isLive() {
		return isLive;
	}

	public void setLive(boolean isLive) {
		this.isLive = isLive;
	}

	public ServerSocket getSocket() {
		return socket;
	}

	public synchronized void addClient(Client client) {
		this.clients.add(client);
	}

	public synchronized List<Client> getClients() {
		return clients;
	}

	public synchronized void setClients(List<Client> clients) {
		this.clients = clients;
	}

	/**
	 * Terminates a client connection.
	 * @param client
	 */
	public void close(Client client) {
		try {
			client.getSocket().close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		this.clients.remove(client);
	}

	/**
	 * Sends a {@link Packet} to the specified {@link Client}.
	 * @param client
	 * @param packets
	 */
	public void send(Client client, List<Packet> packets) {
		client.send(packets);
	}

	/**
	 * Sends a list of {@link Packet} to a list of {@link Client}.
	 * @param clients
	 * @param packets
	 */
	public void send(List<Client> clients, List<Packet> packets) {
		for (Client client : clients)
			client.send(packets);
	}

	/**
	 * Sends a list of {@link Packet} to all the clients connected
	 * to the network.
	 * @param packets
	 */
	public void propagate(List<Packet> packets) {
		for (Client client : this.getClients())
			client.send(packets);
	}

	/**
	 * Sends a {@link Packet} to all the clients int the network.
	 * @param packet
	 */
	public void propagate(Packet packet) {
		for (Client client : this.getClients())
			client.send(packet);
	}

	public synchronized List<ServerAdapter> getServerAdapters() {
		return this.serverAdapters;
	}

	/**
	 * Adds a {@link ServerAdapter} to the listeners.
	 * @param adapter
	 */
	public synchronized void addServerListener(ServerAdapter adapter) {
		this.serverAdapters.add(adapter);
	}

	public void triggerOnServerNewClient(ServerNewClientEvent event) {
		for (ServerAdapter serverAdapter : this.getServerAdapters()) {
			serverAdapter.onServerNewClient(event);
		}
	}

	private int id = 0; // Starts always at zero
	/**
	 * Generates an unique identifier.
	 */
	@Override
	public synchronized int nextId() {
		this.id += 1;
		if (Integer.MAX_VALUE == this.id)
			this.id = 0;
		return id;
	}
}