package ui.drop;

import api.API;
import ui.App;
import ui.Window;

public class DropWindow extends Window {
	public static final String DROP_WINDOW_TITLE = "DropZone";
	public DropWindow(API api, App app) {
		super(DROP_WINDOW_TITLE, api, app);

		// Create and start DropZone
		DropZone dropZone = new DropZone(this.getApi());
		dropZone.setAlignmentX(this.CENTER_ALIGNMENT);
		this.add(dropZone);
	}
}
