package ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.Border;

import api.API;
import ui.downloads.DownloadsWindow;

public class App extends JFrame {
	public static final int MIN_WINDOW_Y = 600;
	public static final int MIN_WINDOW_X = 800;

	public static final int SMALL_MARGIN_SIZE = 5;
	public static final int MARGIN_SIZE = 10;
	public static final int BIG_MARGIN_SIZE = 20;

	public static final Color BACKGROUND_COLOR = Color.WHITE;

	public static final String MAIN_TITLE = "DropIt";

	public static final String FONT_FAMILY = "Roboto";
	public static final Font TITLE_1_FONT = new Font(FONT_FAMILY, Font.BOLD, 36);
	public static final Font TITLE_2_FONT = new Font(FONT_FAMILY, Font.PLAIN, 24);
	public static final Font TITLE_3_FONT = new Font(FONT_FAMILY, Font.BOLD, 18);

	private API api;

	public static void main(String args[]) {
		new App();
	}

	public App() {
		// Start server and IOManager
		this.api = new API();
		this.api.start();

		// Define JFrame
		JPanel frame = new JPanel();
		frame.setLayout(new BoxLayout(frame, BoxLayout.Y_AXIS));
		frame.setMinimumSize(new Dimension(MIN_WINDOW_X, MIN_WINDOW_Y));
		frame.setPreferredSize(new Dimension(MIN_WINDOW_X, MIN_WINDOW_Y));

		// DropWindow dropWindow = new DropWindow(this.api, this);
		// frame.add(dropWindow, BorderLayout.CENTER);

		// TEST
		frame.add(new DownloadsWindow(this.api, this), BorderLayout.CENTER);
		// Define row logic
		
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
}
