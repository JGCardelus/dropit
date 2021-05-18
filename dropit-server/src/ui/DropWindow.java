package ui;

import javax.swing.BoxLayout;
import javax.swing.JPanel;

public class DropWindow extends JPanel {

	public DropWindow() {
		
		// Set JPanel properties
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		this.add(new DropZone());
	}
}
