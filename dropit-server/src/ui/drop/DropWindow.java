package ui.drop;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;

import api.API;
import ui.App;
import ui.Window;
import ui.generic.SectionTitle;

public class DropWindow extends Window {
	public static final String DROP_WINDOW_TITLE = "DropZone";

	private DefaultListModel<String> sentFilesList = new DefaultListModel<>();

	public DropWindow(API api, App app) {
		super(DROP_WINDOW_TITLE, api, app);

		// Create and start DropZone
		DropZone dropZone = new DropZone(this.getApi(), this);
		dropZone.setAlignmentX(this.CENTER_ALIGNMENT);
		this.addToWindow(dropZone);

		this.createSelectButton();
		this.createSentFilesList();
	}

	private void createSentFilesList() {
		this.addToWindow(new SectionTitle("Archivos enviados"));
		JList<String> sentFilesJList = new JList<>(this.sentFilesList);
		sentFilesJList.setFont(App.P_FONT);
		JScrollPane listWrapper = new JScrollPane(sentFilesJList);
		listWrapper.setMaximumSize(new Dimension(App.INFINITE_SIZE, 200));
		this.addToWindow(listWrapper);
	}

	public void createSelectButton() {
		JPanel wrapper = new JPanel();
		wrapper.setBorder(App.createMargin(App.MARGIN_SIZE, 0));
		wrapper.setMaximumSize(new Dimension(App.INFINITE_SIZE, 60));

		JButton btnFind = new JButton("O selecciónalos de la manera aburrida");
		btnFind.setFont(App.TITLE_3_FONT);
		btnFind.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent event) {
				JFileChooser fileChooser = new JFileChooser();
				fileChooser.setCurrentDirectory(new File(System.getProperty("user.dir")));
				fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
				fileChooser.setMultiSelectionEnabled(true);

				int val = fileChooser.showOpenDialog(DropWindow.this.getApp());
				if (val == JFileChooser.APPROVE_OPTION) {
					for (File file : fileChooser.getSelectedFiles()) {
						DropWindow.this.send(file);
					}
				}
			}
		});

		wrapper.add(btnFind);
		this.addToWindow(wrapper);
	}

	public void send(File file) {
		this.getApi().send(file);
		this.sentFilesList.addElement(file.getName());
	}
}
