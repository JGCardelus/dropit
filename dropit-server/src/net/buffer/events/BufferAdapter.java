package net.buffer.events;

public abstract class BufferAdapter {
	public void onBufferComplete(BufferCompleteEvent event) {}
	public void onBufferNewPacket(BufferNewPacketEvent event) {}

	public void onFileComplete(FileCompleteEvent event) {}
}
