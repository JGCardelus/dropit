package net.server;

import java.util.LinkedList;
import java.util.List;

import debug.DebugTools;
import net.client.Client;
import net.server.threads.AcceptThread;

import java.io.IOException;
import java.net.ServerSocket;

public class Server extends Thread {
	public static final int PORT = 8008;
	
	private ServerSocket socket;
	private boolean isLive;
	private List<Client> clients;

	// Internal threads
	private AcceptThread acceptThread;

	public Server() {
		this.setClients(new LinkedList<Client>());
	}

	@Override
	public void run() {
		try {
			DebugTools.print("Starting server");

			this.socket = new ServerSocket(PORT);
			this.setLive(true);
			
			// Accepting new clients should be non-blocking
			acceptThread = new AcceptThread(this);
			acceptThread.start();

			DebugTools.print("Server live and accepting clients");
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

	public void close(Client client) {
		this.clients.remove(client);
	}
}