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
	private Server server;

	private Socket socket; // Socket binded to Client
	private boolean isLive;

	// Internal threads
	private ReadThread readThread;
	private List<SendThread> sendThreads;

	public Client(Server server, Socket socket) {
		this.setSendThreads(new LinkedList<SendThread>());
		this.setServer(server);
		this.setSocket(socket);
	}

	@Override
	public void run() {
		this.setLive(true);
		// Start reader
		this.readThread = new ReadThread(this);
		this.readThread.start();

		for (int i = 0; i < 10; i++) {
			this.send(new Packet(i));
		}
	}

	public void setSendThreads(List<SendThread> sendThreads) {
		this.sendThreads = sendThreads;
	}

	public List<SendThread> getSendThreads() {
		return this.sendThreads;
	}

	public synchronized void removeSendThread(SendThread sendThread) {
		this.sendThreads.remove(sendThread);
	}

	public Server getServer() {
		return server;
	}

	public void setServer(Server server) {
		this.server = server;
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

	public void send(Packet packet) {
		SendThread newSendThread = new SendThread(this, packet);
		this.sendThreads.add(newSendThread);
		newSendThread.start();
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
