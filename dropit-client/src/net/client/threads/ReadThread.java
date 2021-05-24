package net.client.threads;

import java.io.IOException;
import java.io.ObjectInputStream;

import net.client.Client;
import packet.Packet;

public class ReadThread extends Thread {
	private Client client;

	public ReadThread(Client client) {
		this.setClient(client);
	}

	@Override
	public void run() {
		try {
			ObjectInputStream ois = new ObjectInputStream(this.client.getSocket().getInputStream());
			
			while (!this.isInterrupted()) {
				Object obj = ois.readObject();
				this.read(obj);
			}
		} catch (IOException ioe) {
			// The socket has closed
			this.client.close();
		} catch (ClassNotFoundException cnfe) {
			cnfe.printStackTrace();
		}
	}

	public Client getClient() {
		return client;
	}

	public void setClient(Client client) {
		this.client = client;
	}

	private void read(Object obj) {
		if (obj instanceof Packet) {
			Packet packet = (Packet) obj;
			this.client.handlePacket(packet);
		}
	}
}
