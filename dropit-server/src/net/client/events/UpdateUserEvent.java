package net.client.events;

import app.users.User;
import net.client.Client;

public class UpdateUserEvent {
	private Client client;
	private User user;

	public UpdateUserEvent(Client client) {
		this.setClient(client);
	}

	public void setClient(Client client) {
		this.client = client;
		this.user = client.getUser();
	}

	public Client getClient() {
		return this.client;
	}

	public User getUser() {
		return this.user;
	}
}
