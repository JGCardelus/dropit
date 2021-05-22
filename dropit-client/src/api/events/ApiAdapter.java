package api.events;

import net.buffer.events.FileCompleteEvent;
import net.buffer.events.NewFileEvent;

public abstract class ApiAdapter {
	public void onNewFile(NewFileEvent event) {}
	public void onFileComplete(FileCompleteEvent event) {}
}
