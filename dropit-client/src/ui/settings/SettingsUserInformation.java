package ui.settings;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

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
import ui.generic.SectionTitle;

public class SettingsUserInformation extends JPanel {
	private API api;
	private App app;

	private JTextField txtUsername;
	private JTextField txtName;
	private JTextField txtSurname;

	private JButton btnUpdate;

	public SettingsUserInformation(API api, App app) {
		this.api = api;
		this.app = app;

		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

		this.setBorder(App.createMargin(App.MARGIN_SIZE, 0));
		this.add(new SectionTitle("Mi información"));
		
		this.initializeForm();
		
		this.btnUpdate = new JButton("Actualizar");
		this.add(btnUpdate); 

		this.btnUpdate.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent event) {
				SettingsUserInformation.this.updateUser();
			}
		});
	}

	private void initializeForm() {
		this.txtUsername = this.addForm("Usuario");
		this.txtName = this.addForm("Nombre");
		this.txtSurname = this.addForm("Apellidos");

		if (this.api.getUser() != null) {
			this.txtName.setText(this.api.getUser().getName());
			this.txtUsername.setText(this.api.getUser().getUsername());
			this.txtSurname.setText(this.api.getUser().getSurname());
		}
	}

	public void updateUser() {
		String username = this.txtUsername.getText();
		String name = this.txtName.getText();
		String surname = this.txtSurname.getText();

		if (username.isBlank() || username.isEmpty()) {
			JOptionPane.showMessageDialog(
				this.app, 
				"El usuario no puede estar en blanco.", 
				"Error al actualizar el usuario", 
				JOptionPane.OK_OPTION);
		} else {
			User user = new User(username);
			user.setName(name);
			user.setSurname(surname);
			this.api.setUser(user);
		}
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
		this.add(panel);
		return txtField;
	}
}
