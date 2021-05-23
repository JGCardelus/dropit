package ui.generic;

import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.JLabel;

import api.API;
import ui.App;

public class RoomCodeLabel extends JLabel {
	private API api;
	private App app;

	public RoomCodeLabel(API api, App app) {
		super(api.getRoomCode());
		this.api = api;
		this.app = app;

		this.setFont(App.TITLE_2_FONT);
		this.setAlignmentX(CENTER_ALIGNMENT);
		this.setOpaque(true);
		this.setBackground(Color.WHITE);
		this.setBorder(BorderFactory.createLineBorder(Color.BLACK));

		RoomCodeDialog roomCodeDialog = new RoomCodeDialog(api, app);
		this.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent event) {
				roomCodeDialog.setVisible(true);
			}
		});
	}
}
