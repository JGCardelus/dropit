package ui.downloads.table;

import java.awt.FlowLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;

import ui.App;

public class DownloadsTableHeader extends JPanel {

	public DownloadsTableHeader(String title) {
		this.setLayout(new FlowLayout(FlowLayout.CENTER));
		
		JLabel lblTitle = new JLabel(title);
		lblTitle.setFont(App.TITLE_3_FONT);
		this.add(lblTitle);

		this.setBorder(App.createMargin(App.MARGIN_SIZE, 0));
		this.setBackground(DownloadsTable.HEADER_BACKGROUND_COLOR);
	}
}
