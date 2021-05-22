package net.server.events;

import net.client.Client;

public class ServerNewClientEvent {
	private Client client;

	public ServerNewClientEvent(Client client) {
		this.setClient(client);
	}

	public Client getClient() {
		return client;
	}

	public void setClient(Client client) {
		this.client = client;
	}
}
