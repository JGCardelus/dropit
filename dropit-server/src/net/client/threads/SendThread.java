package net.client.threads;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.List;

import net.client.Client;
import packet.Packet;

public class SendThread extends Thread {
	private Client client;
	private Packet packet;
	private SendThread previousThread;

	public SendThread(Client client, Packet packet) {
		this.setClient(client);
		this.setPacket(packet);
		this.setPreviousThread();
	}

	public void setPreviousThread() {
		List<SendThread> clientSendThreads = this.client.getSendThreads();
		if (!clientSendThreads.isEmpty()) {
			// There are more sendThreads than myself, preivous sendThread is the last one
			this.previousThread = clientSendThreads.get(clientSendThreads.size() - 1);
		}
	}

	public Packet getPacket() {
		return packet;
	}

	public void setPacket(Packet packet) {
		this.packet = packet;
	}

	@Override
	public void run() {
		// First wait until previous sendThread has finished
		if (this.previousThread != null) {
			try {
				this.previousThread.join(); // Wait until last has finished
			} catch (InterruptedException e) {
				this.previousThread.interrupt(); // Halt execution of last one
				e.printStackTrace();
			}
		}
		// When last thread finishes, send packet
		this.send();
		// Remove self from queue
		this.client.removeSendThread(this);
	}

	public void send() {
		// Send packet
		try {
			ObjectOutputStream oos = new ObjectOutputStream(this.client.getSocket().getOutputStream());
			oos.writeObject(this.packet);
			// Don't close socket in case someone else needs it
			oos.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public Client getClient() {
		return client;
	}

	public void setClient(Client client) {
		this.client = client;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((client == null) ? 0 : client.hashCode());
		result = prime * result + ((packet == null) ? 0 : packet.hashCode());
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
		SendThread other = (SendThread) obj;
		if (client == null) {
			if (other.client != null)
				return false;
		} else if (!client.equals(other.client))
			return false;
		if (packet == null) {
			if (other.packet != null)
				return false;
		} else if (!packet.equals(other.packet))
			return false;
		return true;
	}

	
}