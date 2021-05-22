package ui.drop;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetAdapter;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.TooManyListenersException;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

import api.API;
import ui.App;

public class DropZone extends JPanel {
	private API api;
	private DropTarget dropTarget;

	public static final String DEFAULT_MESSAGE = "Arrastra archivos aquí";
	public static final String ON_DRAG_ENTER_MESSAGE = "Suelta para enviar";
	public static final String ON_DROP_MESSAGE = "Enviando archivos";

	public static final Color DEFAULT_COLOR = App.BACKGROUND_COLOR;
	public static final Color ON_DRAG_COLOR = new Color(200, 200, 200);

	public static final int HEIGHT = 200;

	private JLabel lblDropInformation;

	public DropZone(API api) {
		this.api = api;
		this.setDropTarget();
		
		// Set JPanel properties
		this.lblDropInformation = new JLabel(DEFAULT_MESSAGE);
		this.lblDropInformation.setFont(App.TITLE_2_FONT);
		this.lblDropInformation.setBorder(BorderFactory.createDashedBorder(
			Color.BLACK,
			2f,
			5f,
			2f,
			true));
		this.lblDropInformation.setMaximumSize(new Dimension(App.MIN_WINDOW_X - 2*App.MARGIN_SIZE, HEIGHT));
		this.lblDropInformation.setHorizontalAlignment(JLabel.CENTER);		

		this.setBorder(BorderFactory.createEmptyBorder(App.MARGIN_SIZE, App.MARGIN_SIZE, App.MARGIN_SIZE, App.MARGIN_SIZE));
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		this.add(this.lblDropInformation);
		this.setBackground(DEFAULT_COLOR);
	}

	private void setDropTarget() {
		this.dropTarget = new DropTarget();
		this.setDropTarget(this.dropTarget);

		try {
			this.dropTarget.addDropTargetListener(new DropTargetAdapter() {
				@Override
				public void dragEnter(DropTargetDragEvent event) {
					DropZone.this.setBackground(DropZone.ON_DRAG_COLOR);
					DropZone.this.lblDropInformation.setText(DropZone.ON_DRAG_ENTER_MESSAGE);
				}

				@Override
				public void dragExit(DropTargetEvent event) {
					DropZone.this.lblDropInformation.setText(DropZone.DEFAULT_MESSAGE);
					DropZone.this.setBackground(DropZone.DEFAULT_COLOR);
				}

				@Override
				public void drop(DropTargetDropEvent event) {
					DropZone.this.lblDropInformation.setText(DropZone.ON_DROP_MESSAGE);

					event.acceptDrop(DnDConstants.ACTION_COPY_OR_MOVE);
					try {
						List<File> files = (List<File>) event.getTransferable().getTransferData(DataFlavor.javaFileListFlavor);
						for (File file : files)
							DropZone.this.api.send(file);
					} catch (UnsupportedFlavorException ufe) {
						ufe.printStackTrace();
					} catch (IOException ioe) {
						ioe.printStackTrace();
					}
					DropZone.this.lblDropInformation.setText(DropZone.DEFAULT_MESSAGE);
					DropZone.this.setBackground(DropZone.DEFAULT_COLOR);
				}
			});
		} catch (TooManyListenersException e) {
			e.printStackTrace();
		}
	}
}
