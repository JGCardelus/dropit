package ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.BorderLayout;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

import api.API;

public class Window extends JPanel {
	private API api;
	private App app;

	private int windowPosition = -1;
	private boolean isOpen;

	// Graphic elements
	private JPanel frame;

	private String title;
	private JLabel lblTitle;

	public Window(String title, API api, App app) {
		// Initialize
		this.setOpen(false);
		this.setTitle(title);
		this.api = api;
		this.app = app;
		this.windowPosition = this.app.addWindow(this);

		// Define window graphic logic
		this.setLayout(new BorderLayout());
		this.frame = new JPanel();
		this.frame.setLayout(new BoxLayout(this.frame, BoxLayout.Y_AXIS));

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
			App.INFINITE_SIZE,
			50 + 2*App.BIG_MARGIN_SIZE
		));
		panelTitle.add(this.lblTitle, BorderLayout.NORTH);
		this.addToWindow(panelTitle);

		this.add(this.frame);
	}

	public Component addToWindow(Component comp) {
		this.frame.add(comp);
		return comp;
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
		this.app.getMenu().updateButton(this.windowPosition, this.getTitle());
	}

	public void open() {
		this.updateTitle();
		this.setVisible(true);
		this.setOpen(true);
	}
	
	public void close() {
		this.setVisible(false);
		this.setOpen(false);
	}

	public API getApi() {
		return this.api;
	}

	public App getApp() {
		return this.app;
	}

	public int getWindowPosition() {
		return this.windowPosition;
	}
}
