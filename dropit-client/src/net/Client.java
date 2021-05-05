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
			this.socket = new Socket("172.24.154.30", PORT);

			ObjectOutputStream oos = new ObjectOutputStream(this.socket.getOutputStream());
			// for (int i = 0; i < 10; i++) {
			// 	oos.writeObject(new Packet(i));
			// 	oos.flush();
			// }

			ObjectInputStream ois = new ObjectInputStream(this.socket.getInputStream());
			
			for (int i = 0; i < 3; i++) {
				Object obj = ois.readObject();
				if (obj instanceof Packet) {
					Packet packet = (Packet) obj;
					System.out.println(packet);

					if (packet.getQos() == Packet.QOS_LEVEL_1) {
						oos.writeObject(new Packet(packet.getId(), Packet.CODE_CONFIRMATION, Packet.QOS_LEVEL_0));
						oos.flush();
					}
				}
			}

			for (int i = 0; i < 3; i++) {
				Object obj = ois.readObject();
				if (obj instanceof Packet) {
					Packet packet = (Packet) obj;
					System.out.println(packet);

					if (packet.getQos() == Packet.QOS_LEVEL_1) {
						oos.writeObject(new Packet(packet.getId(), Packet.CODE_RESEND, Packet.QOS_LEVEL_0));
						oos.flush();
					}
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