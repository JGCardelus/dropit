package net.buffer.events;

import net.buffer.Buffer;
import packet.types.FileHeaderPacket;

public class NewFileEvent {
	private FileHeaderPacket header;
	private Buffer buffer;

	/**
	 * Triggered when a new file is received.
	 * @param buffer
	 */
	public NewFileEvent(Buffer buffer) {
		this.setBuffer(buffer);
	}

	public Buffer getBuffer() {
		return buffer;
	}

	public void setBuffer(Buffer buffer) {
		this.buffer = buffer;
		this.setHeader();
	}

	public void setHeader() {
		if (this.buffer.getHeader() instanceof FileHeaderPacket) {
			this.header = (FileHeaderPacket) this.buffer.getHeader();
		}
	}

	public FileHeaderPacket getHeader() {
		return this.header;
	}
	
}
