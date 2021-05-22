package ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

import api.API;

public class Window extends JPanel {
	private API api;
	private App app;

	private boolean isOpen;

	// Graphic elements
	private String title;
	private JLabel lblTitle;

	public Window(String title, API api, App app) {
		this.setOpen(false);
		this.setTitle(title);
		this.api = api;
		this.app = app;

		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

		// Add window title
		JPanel panelTitle = new JPanel();
		panelTitle.setLayout(new FlowLayout(FlowLayout.LEFT));
		panelTitle.setBorder(BorderFactory.createEmptyBorder(
			App.BIG_MARGIN_SIZE, 
			App.MARGIN_SIZE, 
			App.BIG_MARGIN_SIZE, 
			App.MARGIN_SIZE
		));

		this.lblTitle = new JLabel(this.getTitle());
		this.lblTitle.setFont(App.TITLE_1_FONT);
		
		panelTitle.setMaximumSize(new Dimension(
			App.MIN_WINDOW_X,
			50 + 2*App.BIG_MARGIN_SIZE
		));
		panelTitle.add(this.lblTitle);

		this.add(panelTitle);
	}

	public boolean isOpen() {
		return isOpen;
	}

	public void setOpen(boolean isOpen) {
		this.isOpen = isOpen;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
		if (this.isOpen()) {
			this.updateTitle();
		}
	}

	public void updateTitle() {
		this.app.setTitle(this.getTitle());
	}

	public void open() {
		this.updateTitle();
	}

	public API getApi() {
		return this.api;
	}

	public App getApp() {
		return this.app;
	}
}
