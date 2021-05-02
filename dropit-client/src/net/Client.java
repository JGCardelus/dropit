package net;


import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import packet.Packet;

public class Client extends Thread {
	public static final int PORT = 8008;

	private Socket socket;

	public Client() {
		// This is empty becuase I have to fill it
	}

	@Override
	public void run() {
		try {
			this.socket = new Socket("2.2.2.51", PORT);

			ObjectOutputStream oos = new ObjectOutputStream(this.socket.getOutputStream());
			for (int i = 0; i < 10; i++) {
				oos.writeObject(new Packet(i));
				oos.flush();
			}

			ObjectInputStream ois = new ObjectInputStream(this.socket.getInputStream());
			
			while (true) {
				Object obj = ois.readObject();
				if (obj instanceof Packet) {
					Packet packet = (Packet) obj;
					System.out.println(packet);
				}
			}
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException ioe) {
			// The socket has closed
			ioe.printStackTrace();
		} catch (ClassNotFoundException cnfe) {
			cnfe.printStackTrace();
		}
	}
}