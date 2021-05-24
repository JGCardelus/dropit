package ui.landing;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.Socket;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import api.API;
import app.users.User;
import ui.App;

public class LandingDialog extends JDialog {
	private App app;

	private JPanel frame;
	private JTextField usernameText;
	private JTextField roomCodeText;

	public LandingDialog(App app) {
		super(app);

		this.app = app;

		this.setTitle("¡Hola!");
		
		this.frame = new JPanel();
		frame.setLayout(new BoxLayout(frame, BoxLayout.Y_AXIS));
		frame.setAlignmentX(LEFT_ALIGNMENT);
		frame.setBorder(App.createMargin(App.MARGIN_SIZE));

		JLabel welcomeLabel = new JLabel("DropIt");
		welcomeLabel.setFont(App.TITLE_1_FONT);
		frame.add(welcomeLabel);

		JLabel instructionsLabel = new JLabel("Pon tu nombre y el room code");
		instructionsLabel.setFont(App.P_FONT);
		instructionsLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, App.BIG_MARGIN_SIZE, 0));
		frame.add(instructionsLabel);
		
		this.usernameText = this.addForm("Nombre");
		this.roomCodeText = this.addForm("Room Code");

		JButton okButton = new JButton("Vamos allá");
		okButton.setBorder(App.createMargin(App.MARGIN_SIZE));
		this.frame.add(okButton);

		okButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent event) {
				LandingDialog.this.close();
			}
		});

		this.add(frame);
		this.setSize(500,250);
		this.setLocationRelativeTo(null);
	}

	private void close() {
		String username = this.usernameText.getText();
		try {
			int roomCode = Integer.parseInt(this.roomCodeText.getText());
			if (!(username.isEmpty() || username.isBlank())) {
				if (roomCode > 1) {
					this.startApi(username, roomCode);
				}
			}
		} catch (NumberFormatException e) {
			this.invalidData();
		}
	}

	private void startApi(String username, int roomCode) {
		try {
			Socket socket = API.createSocketFromRoomCode(roomCode);
			this.app.setApi(new API(socket));
			this.app.getApi().setUser(new User(username));
			this.app.start();
			this.setVisible(false);
		} catch (IOException e) {
			this.tryAgain();
		}
	}

	private void tryAgain() {
		JOptionPane.showMessageDialog(
			this, 
			"Parece que no te puedes conectar ahora mismo. Vuelve a intentarlo en un rato.", 
			"Ups!", 
			JOptionPane.OK_OPTION);
	}

	private void invalidData() {
		JOptionPane.showMessageDialog(
			this, 
			"Los datos introducidos no son válidos", 
			"Ups!", 
			JOptionPane.OK_OPTION);

		this.usernameText.setText("");
		this.roomCodeText.setText("");
	}

	public JTextField addForm(String question) {
		JPanel panel = new JPanel(new GridLayout(0,2));
		JLabel label = new JLabel(question);
		label.setAlignmentX(RIGHT_ALIGNMENT);
		label.setFont(App.P_FONT);
		panel.add(label);

		JTextField txtField = new JTextField();
		panel.add(txtField);
		panel.setMaximumSize(new Dimension(App.INFINITE_SIZE, 30));
		panel.setBorder(App.createMargin(0, App.MARGIN_SIZE));
		this.frame.add(panel);
		return txtField;
	}
}
