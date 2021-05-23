package ui.downloads.table;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;

import api.API;
import api.events.ApiAdapter;
import net.buffer.events.NewFileEvent;

public class DownloadsTable extends JPanel {
	public static final Color HEADER_BACKGROUND_COLOR = new Color(255, 184, 28);
	
	private JPanel rows;
	private API api;

	public DownloadsTable(API api) {
		this.api = api;
		this.setLayout(new BorderLayout());
		
		// Define header
		JPanel header = new JPanel();
		header.setLayout(new GridLayout(0,2));

		header.add(new DownloadsTableHeader("Archivo"));
		header.add(new DownloadsTableHeader("Progreso"));
		this.add(header, BorderLayout.NORTH);

		// Define rows
		this.rows = new JPanel();
		this.rows.setLayout(new BoxLayout(this.rows, BoxLayout.Y_AXIS));
		
		JScrollPane rowsWrapper = new JScrollPane(this.rows);
		this.add(rowsWrapper, BorderLayout.CENTER);

		// Define row logic
		this.api.addApiListener(new ApiAdapter(){
			@Override
			public void onNewFile(NewFileEvent event) {
				SwingUtilities.invokeLater(new Runnable() {
					@Override
					public void run() {
						DownloadsTable.this.addRow(new DownloadsTableRow(event.getHeader(), event.getBuffer()));
						DownloadsTable.this.rows.revalidate();
						DownloadsTable.this.rows.repaint();
					}
				});
			}
		});
	}
	
	public void addRow(DownloadsTableRow row) {
		row.setMaximumSize(new Dimension(this.getWidth(), 50));
		this.rows.add(row);
	}
}
