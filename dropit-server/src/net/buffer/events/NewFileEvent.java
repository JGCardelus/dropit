package net.buffer.events;

import net.buffer.Buffer;

public class NewFileEvent {
	private Buffer buffer;

	public NewFileEvent(Buffer buffer) {
		this.setBuffer(buffer);
	}

	public Buffer getBuffer() {
		return buffer;
	}

	public void setBuffer(Buffer buffer) {
		this.buffer = buffer;
	}
}
