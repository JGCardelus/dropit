package ui.generic;

import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;

import api.API;
import ui.App;

public class RoomCodeDialog extends JDialog {
	private API api;
	private App app;

	public RoomCodeDialog(API api, App app) {
		super(app);

		this.api = api;
		this.app = app;

		this.setTitle("Room Code");
		
		JPanel panel = new JPanel();
		panel.setLayout(new FlowLayout());
		panel.setBorder(App.createMargin(App.BIG_MARGIN_SIZE * 4));

		JLabel lblRoomCode = new JLabel(Integer.valueOf(this.api.getRoomCode()).toString());
		lblRoomCode.setFont(new Font(App.FONT_FAMILY, Font.BOLD, 96));
		panel.add(lblRoomCode);
		
		this.add(panel);

		this.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent event) {
				RoomCodeDialog.this.setVisible(false);
			}
		});

		this.pack();
		this.setLocationRelativeTo(null);
	}
}
