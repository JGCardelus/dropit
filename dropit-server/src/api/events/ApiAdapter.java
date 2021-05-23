package api.events;

import net.buffer.events.FileCompleteEvent;
import net.buffer.events.NewFileEvent;
import net.client.events.UpdateUserEvent;
import net.server.events.ServerNewClientEvent;

public abstract class ApiAdapter {
	public void onNewFile(NewFileEvent event) {}
	public void onFileComplete(FileCompleteEvent event) {}
	public void onNewClient(ServerNewClientEvent event) {}
	public void onUpdateUser(UpdateUserEvent event) {}
}
