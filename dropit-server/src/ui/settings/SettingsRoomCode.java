package ui.settings;

import javax.swing.BoxLayout;
import javax.swing.JPanel;

import api.API;
import ui.App;
import ui.generic.RoomCodeLabel;
import ui.generic.SectionTitle;

public class SettingsRoomCode extends JPanel {
	private API api;
	private App app;
	
	public SettingsRoomCode(API api, App app) {
		this.api = api;
		this.app = app;

		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		this.setBorder(App.createMargin(App.MARGIN_SIZE, 0));

		this.add(new SectionTitle("Room Code"));

		RoomCodeLabel roomCodeLabel = new RoomCodeLabel(api, app);
		this.add(roomCodeLabel);
	}
}
