package ui.downloads;

import api.API;
import ui.App;
import ui.Window;
import ui.downloads.table.DownloadsTable;

public class DownloadsWindow extends Window {
	public static final String DOWNLOADS_WINDOW_TITLE = "Descargas";

	public DownloadsWindow(API api, App app) {
		super(DOWNLOADS_WINDOW_TITLE, api, app);
		// Create and start downloads table
		DownloadsTable downloadsTable = new DownloadsTable(this.getApi());
		this.addToWindow(downloadsTable);
	}
}
