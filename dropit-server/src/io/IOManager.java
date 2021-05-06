package io;

import io.threads.files.FileThread;

public class IOManager {
	public IOManager() {
		this.start();
	}

	public void start() {
		FileThread ft = new FileThread("test1.txt");
		ft.start();
	}
}
