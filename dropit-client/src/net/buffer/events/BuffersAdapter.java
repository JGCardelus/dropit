package net.buffer.events;

public abstract class BuffersAdapter {
	public void onNewBuffer(NewBufferEvent event) {}
	public void onBufferComplete(BufferCompleteEvent event) {}

	// Wrappers for new buffer events
	public void onNewFile(NewFileEvent event) {}
	public void onFileComplete(FileCompleteEvent event) {}
}
