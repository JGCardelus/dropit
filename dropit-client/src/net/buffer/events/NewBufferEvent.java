package net.buffer.events;

import net.buffer.Buffer;

public class NewBufferEvent {
	private Buffer buffer;

	public NewBufferEvent(Buffer buffer) {
		this.setBuffer(buffer);
	}

	public Buffer getBuffer() {
		return buffer;
	}

	public void setBuffer(Buffer buffer) {
		this.buffer = buffer;
	}
}
