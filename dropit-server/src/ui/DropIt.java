package ui;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.JFrame;

import io.IOManager;
import net.server.Server;

public class DropIt extends JFrame {
	public static final int MIN_WINDOW_Y = 600;
	public static final int MIN_WINDOW_X = 800;

	public static final String MAIN_TITLE = "DropIt";

	private Server server;
	private IOManager io;

	public static void main(String args[]) {
		new DropIt();
	}

	public DropIt() {
		// Start server and IOManager
		this.server = new Server();
		this.server.start();
		this.io = new IOManager(this.server);

		// Define JFrame
		this.setLayout(new BorderLayout());
		this.add(new DropWindow(), BorderLayout.CENTER);
		
		this.setTitle(MAIN_TITLE);
		this.setSize(MIN_WINDOW_X, MIN_WINDOW_Y);
		this.setBackground(Color.WHITE);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setLocationRelativeTo(null);
		this.setVisible(true);
	}
}
