package ui.generic;

import javax.swing.JLabel;

import ui.App;

public class SectionTitle extends JLabel {
	public SectionTitle(String text) {
		super(text);
		this.setAlignmentX(CENTER_ALIGNMENT);
		this.setFont(App.TITLE_2_FONT);
		this.setBorder(App.createMargin(App.MARGIN_SIZE, 0));
	}
}
