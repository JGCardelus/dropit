package ui.settings;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;

import javax.swing.BoxLayout;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;

import api.API;
import ui.App;
import ui.generic.SectionTitle;

public class SettingsFilePath extends JPanel {
	private API api;
	private App app;

	private JLabel lblFilePath;
	
	public SettingsFilePath(API api, App app) {
		this.api = api;
		this.app = app;

		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		this.setBorder(App.createMargin(App.MARGIN_SIZE, 0));

		this.add(new SectionTitle("Dirección de descarga de archivos"));
		
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(0,2));
		panel.setMaximumSize(new Dimension(App.INFINITE_SIZE, 30));
		panel.setBorder(App.createMargin(0, App.MARGIN_SIZE));
		
		JLabel label = new JLabel("Dirección");
		label.setFont(App.P_FONT);
		panel.add(label);

		this.lblFilePath = new JLabel(this.api.getFilePath());
		this.lblFilePath.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent event) {
				JFileChooser fileChooser = new JFileChooser();
				fileChooser.setCurrentDirectory(new File(SettingsFilePath.this.api.getFilePath()));
				fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				
				int value = fileChooser.showOpenDialog(SettingsFilePath.this.app);
				if (value == JFileChooser.APPROVE_OPTION) {
					SettingsFilePath.this.setFilePath(fileChooser.getSelectedFile().getAbsolutePath());
				}
			}
		});
		this.lblFilePath.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		this.lblFilePath.setFont(App.P_FONT);
		this.lblFilePath.setForeground(Color.BLUE);
		panel.add(this.lblFilePath);
		this.add(panel);
	}

	public void setFilePath(String filePath) {
		this.api.setFilePath(filePath);
		this.lblFilePath.setText(filePath);
	}
	
}
