package ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.io.IOException;
import java.net.Socket;
import java.util.LinkedList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.Border;

import api.API;
import ui.downloads.DownloadsWindow;
import ui.drop.DropWindow;
import ui.generic.Menu;
import ui.settings.SettingsWindow;

public class App extends JFrame {
	public static final int MIN_WINDOW_Y = 900;
	public static final int MIN_WINDOW_X = 800;
	public static final int INFINITE_SIZE = 1000000;

	public static final int SMALL_MARGIN_SIZE = 5;
	public static final int MARGIN_SIZE = 10;
	public static final int BIG_MARGIN_SIZE = 20;

	public static final Color BACKGROUND_COLOR = Color.WHITE;

	public static final String MAIN_TITLE = "DropIt";

	public static final String FONT_FAMILY = "Verdana";
	public static final Font TITLE_1_FONT = new Font(FONT_FAMILY, Font.BOLD, 36);
	public static final Font TITLE_2_FONT = new Font(FONT_FAMILY, Font.PLAIN, 24);
	public static final Font TITLE_3_FONT = new Font(FONT_FAMILY, Font.BOLD, 18);

	public static final Font P_FONT = new Font(FONT_FAMILY, Font.PLAIN, 14);

	private API api;

	private Menu menu;
	private JPanel frame;

	// Windows
	private List<Window> windows = new LinkedList<Window>();
	private DownloadsWindow downloadsWindow;
	// private DropWindow dropWindow;
	private SettingsWindow settingsWindow;

	public static void main(String args[]) {
		new App();
	}

	public App() {
		// Start server and IOManager
		Socket socket;
		try {
			socket = API.createSocketFromRoomCode(51);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			return;
		}

		this.api = new API(socket);
		this.api.start();

		this.menu = new Menu(this.api, this);

		// Define JFrame
		this.frame = new JPanel();
		this.frame.setLayout(new BoxLayout(frame, BoxLayout.Y_AXIS));
		this.frame.setMinimumSize(new Dimension(MIN_WINDOW_X, MIN_WINDOW_Y));
		this.frame.setPreferredSize(new Dimension(MIN_WINDOW_X, MIN_WINDOW_Y));

		this.downloadsWindow = new DownloadsWindow(this.api, this);
		// this.dropWindow = new DropWindow(this.api, this);
		this.settingsWindow = new SettingsWindow(this.api, this);

		// Initial state
		this.open(this.downloadsWindow.getWindowPosition());
		
		this.frame.add(this.menu, BorderLayout.SOUTH);

		this.add(frame);
		this.setResizable(true);
		this.setTitle(MAIN_TITLE);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.pack();
		this.setLocationRelativeTo(null);
		this.setVisible(true);
	}

	public API getApi() {
		return this.api;
	}

	public static Border createMargin(int marginSize) {
		return BorderFactory.createEmptyBorder(
			marginSize, 
			marginSize,
			marginSize,
			marginSize);
	}

	public static Border createMargin(int topBottomMarginSize, int leftRightMarginSize) {
		return BorderFactory.createEmptyBorder(
			topBottomMarginSize, 
			leftRightMarginSize, 
			topBottomMarginSize, 
			leftRightMarginSize);
	}

	public void open(int window) {
		this.menu.open(window);
		
		if (window < this.windows.size()) {
			for (int i = 0; i < this.windows.size(); i++) {
				if (i != window) {
					this.windows.get(i).close();
				}
			}

			this.windows.get(window).open();
		}
	}

	public Menu getMenu() {
		return this.menu;
	}

	public int addWindow(Window window) {
		this.windows.add(window);
		this.frame.add(window, BorderLayout.CENTER);
		window.close();
		return this.menu.addButton(window.getTitle());
	}
}
