package ui.settings;

import java.awt.Dimension;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import api.API;
import api.events.ApiAdapter;
import net.client.Client;
import net.client.events.UpdateUserEvent;
import net.server.events.ServerNewClientEvent;
import ui.App;
import ui.generic.SectionTitle;

public class SettingsConnectedUsers extends JPanel {
	private API api;
	private App app;

	private DefaultListModel<String> connectedClientsList = new DefaultListModel<>();
	private Map<Client, Integer> clientsToList = new HashMap();

	public SettingsConnectedUsers(API api, App app) {
		this.api = api;
		this.app = app;

		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		this.setBorder(App.createMargin(App.MARGIN_SIZE, 0));

		this.add(new SectionTitle("Usuarios conectados"));

		// Subscribe to Api newClient and updateUserEvent
		this.api.addApiListener(new ApiAdapter() {
			@Override
			public void onNewClient(ServerNewClientEvent event) {
				SettingsConnectedUsers.this.addClientUser(event.getClient());
			}

			@Override
			public void onUpdateUser(UpdateUserEvent event) {
				SettingsConnectedUsers.this.updateClientUser(event.getClient());
			}
		});

		JList<String> connectedClientsJList = new JList<>(this.connectedClientsList);
		connectedClientsJList.setFont(App.P_FONT);
		JScrollPane listWrapper = new JScrollPane(connectedClientsJList);
		listWrapper.setMaximumSize(new Dimension(App.INFINITE_SIZE, 200));
		this.add(listWrapper);
	}

	public void addClientUser(Client client) {
		String clientUserName;
		if (client.getUser() == null) {
			clientUserName = client.getSocket().getLocalAddress().toString();
		} else {
			clientUserName = client.getUser().getUsername();
		}
		this.connectedClientsList.addElement(clientUserName);
		int index = this.connectedClientsList.getSize() - 1;
		this.clientsToList.put(client, index);
	}

	public void updateClientUser(Client client) {
		if (this.clientsToList.containsKey(client)) {
			int index = this.clientsToList.get(client);
			String clientUserName = client.getUser().getUsername();
			this.connectedClientsList.set(index, clientUserName);
		} else {
			this.addClientUser(client);
		}
	}
}
