package ui;

import java.awt.Color;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetAdapter;
import java.awt.dnd.DropTargetDropEvent;
import java.io.File;
import java.io.IOException;
import java.awt.dnd.DnDConstants;
import java.util.List;
import java.util.TooManyListenersException;

import javax.swing.JPanel;

public class DropZone extends JPanel {
	private DropTarget dropTarget;
	public DropZone() {
		this.setDropTarget();
		
		// Set JPanel properties
		this.setSize(DropIt.MIN_WINDOW_X, 300);
		this.setBackground(new Color(240, 240, 240));
	}

	private void setDropTarget() {
		this.dropTarget = new DropTarget();
		this.setDropTarget(this.dropTarget);

		try {
			this.dropTarget.addDropTargetListener(new DropTargetAdapter() {
				@Override
				public void dragEnter(DropTargetDragEvent event) {
					System.out.println("Dragging something");
					DropZone.this.setBackground(Color.BLACK);
				}

				@Override
				public void drop(DropTargetDropEvent event) {
					System.out.println("Dropped something");
					DropZone.this.setBackground(Color.GREEN);

					event.acceptDrop(DnDConstants.ACTION_COPY_OR_MOVE);
					try {
						List<File> files = (List<File>) event.getTransferable().getTransferData(DataFlavor.javaFileListFlavor);
						for (File file : files)
							System.out.println(file.getName());
					} catch (UnsupportedFlavorException ufe) {
						ufe.printStackTrace();
					} catch (IOException ioe) {
						ioe.printStackTrace();
					}

				}
			});
		} catch (TooManyListenersException e) {
			e.printStackTrace();
		}
	}
}
