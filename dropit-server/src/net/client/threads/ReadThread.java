package net.client.threads;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

import net.client.Client;
import packet.Packet;

public class ReadThread extends Thread {
	private Client client;

	public ReadThread(Client client) {
		this.setClient(client);
	}

	@Override
	public void run() {
		this.read();
	}

	public Client getClient() {
		return client;
	}

	public void setClient(Client client) {
		this.client = client;
	}

	private void read() {
		try {
			Socket socket = this.client.getSocket();
			ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
			
			while (true) {
				Object obj = ois.readObject();
				if (obj instanceof Packet) {
					Packet packet = (Packet) obj;
					this.client.handlePacket(packet);
				}
			}
		} catch (IOException ioe) {
			// The socket has closed
			this.client.close();
		} catch (ClassNotFoundException cnfe) {
			cnfe.printStackTrace();
		}
	}
}
