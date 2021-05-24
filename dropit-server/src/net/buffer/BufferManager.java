package net.buffer;

import java.util.LinkedList;
import java.util.List;

import net.buffer.events.BufferAdapter;
import net.buffer.events.BufferCompleteEvent;
import net.buffer.events.BufferManagerAdapter;
import net.buffer.events.FileCompleteEvent;
import net.buffer.events.NewBufferEvent;
import net.buffer.events.NewFileEvent;
import net.client.Client;
import packet.types.BufferHeaderPacket;
import packet.types.FileHeaderPacket;

/**
 * The BufferManager captures {@link BufferHeaderPacket} and starts Buffers
 * to handle the rest of the message. In order for the Client to receive multi-packet
 * messages a {@link BufferHeaderPacket} must be sent, containing which packets
 * belong to the message.
 */
public class BufferManager {
	private Client client;

	private List<Buffer> buffers = new LinkedList<>();
	private List<BufferManagerAdapter> bufferManagerAdapters = new LinkedList<>();

	public BufferManager(Client client) {
		this.setClient(client);
	}

	/**
	 * Handles {@link BufferHeaderPacket} by creating a {@link Buffer} that
	 * will complete the rest of the message and notify the {@link BufferManager} once
	 * it's done. 
	 * @param header
	 */
	public void handleBufferHeaderPacket(BufferHeaderPacket header) {
		Buffer buffer = new Buffer(header, this.client);
		this.buffers.add(buffer);
		buffer.addBufferListener(new BufferAdapter() {
			@Override
			public void onBufferComplete(BufferCompleteEvent event) {
				BufferManager.this.triggerOnBufferComplete(event);
			}
			
			@Override
			public void onFileComplete(FileCompleteEvent event) {
				BufferManager.this.triggerOnFileComplete(event);
			}
		});

		this.triggerOnNewBuffer(new NewBufferEvent(buffer));

		switch (header.getBufferType()) {
			case FileHeaderPacket.BUFFER_TYPE:
				this.triggerNewFileEvent(new NewFileEvent(buffer));
			break;
		}
	}

	public synchronized void addBufferManagerListener(BufferManagerAdapter bufferManagerAdapter) {
		this.bufferManagerAdapters.add(bufferManagerAdapter);
	}

	private synchronized List<BufferManagerAdapter> getBufferManagerAdapters() {
		return this.bufferManagerAdapters;
	}

	private synchronized void triggerNewFileEvent(NewFileEvent event) {
		for (BufferManagerAdapter bufferManagerAdapter : this.getBufferManagerAdapters())
			bufferManagerAdapter.onNewFile(event);
	}

	private synchronized void triggerOnFileComplete(FileCompleteEvent event) {
		for (BufferManagerAdapter bufferManagerAdapter : this.getBufferManagerAdapters())
			bufferManagerAdapter.onFileComplete(event);
	}

	private synchronized void triggerOnNewBuffer(NewBufferEvent event) {
		for (BufferManagerAdapter bufferManagerAdapter : this.getBufferManagerAdapters())
			bufferManagerAdapter.onNewBuffer(event);
	}

	private synchronized void triggerOnBufferComplete(BufferCompleteEvent event) {
		for (BufferManagerAdapter bufferManagerAdapter : this.getBufferManagerAdapters())
			bufferManagerAdapter.onBufferComplete(event);
	}

	public Client getClient() {
		return client;
	}

	public void setClient(Client client) {
		this.client = client;
	}
}
