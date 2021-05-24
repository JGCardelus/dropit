package ui.settings;

import javax.swing.JLabel;
import javax.swing.JList;

import api.API;
import ui.App;
import ui.Window;

public class SettingsWindow extends Window {
	public static final String SETTINGS_WINDOW_TITLE = "Ajustes";
	
	public SettingsWindow(API api, App app) {
		super(SETTINGS_WINDOW_TITLE, api, app);

		this.addToWindow(new SettingsUserInformation(this.getApi(), this.getApp()));
		this.addToWindow(new SettingsFilePath(this.getApi(), this.getApp()));
	}
}
