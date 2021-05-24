package ui.generic;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.MouseEvent;
import java.util.LinkedList;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.event.MouseInputAdapter;

import api.API;
import ui.App;

public class Menu extends JPanel {
	public static final Color DEFAULT_COLOR = Color.WHITE;
	public static final Color ACTIVE_COLOR = new Color(255, 184, 28);

	private App app;
	private API api;

	private List<JButton> menuButtons = new LinkedList<JButton>();

	public Menu(API api, App app) {
		this.api = api;
		this.app = app;

		this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
	}

	public void open(int window) {
		if (window > -1 && window < this.menuButtons.size()) {
			menuButtons.get(window).setBackground(ACTIVE_COLOR);

			for (int i = 0; i < this.menuButtons.size(); i++) {
				if (i != window) {
					menuButtons.get(i).setBackground(DEFAULT_COLOR);
				}
			}
		}
	}

	public int addButton(String text) {
		JButton button = new JButton(text);
		menuButtons.add(button);
		int index = menuButtons.size() - 1;
		button.addMouseListener(new MouseInputAdapter() {
			@Override
			public void mouseClicked(MouseEvent event) {
				Menu.this.app.open(index);
			}
		});
		button.setMaximumSize(new Dimension(App.INFINITE_SIZE, 50));
		button.setPreferredSize(new Dimension(App.INFINITE_SIZE, 50));
		button.setForeground(Color.BLACK);
		button.setBackground(DEFAULT_COLOR);
		this.add(button);
		return index;
	}

	public void updateButton(int buttonIndex, String text) {
		if (buttonIndex > -1 && buttonIndex < this.menuButtons.size()) {
			this.menuButtons.get(buttonIndex).setText(text);
		}
	}
}
