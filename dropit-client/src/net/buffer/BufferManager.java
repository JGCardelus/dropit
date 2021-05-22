package net.buffer;

import java.util.LinkedList;
import java.util.List;

import net.buffer.events.BufferAdapter;
import net.buffer.events.BufferCompleteEvent;
import net.buffer.events.FileCompleteEvent;
import net.buffer.events.BufferManagerAdapter;
import net.buffer.events.FileCompleteEvent;
import net.buffer.events.NewBufferEvent;
import net.buffer.events.NewFileEvent;
import net.client.Client;
import packet.types.BufferHeaderPacket;
import packet.types.FileHeaderPacket;

public class BufferManager {
	private Client client;

	private List<Buffer> buffers = new LinkedList<>();
	private List<BufferManagerAdapter> bufferManagerAdapters = new LinkedList<>();

	public BufferManager(Client client) {
		this.setClient(client);
	}

	public void handleBufferHeaderPacket(BufferHeaderPacket header) {
		Buffer buffer = new Buffer(header, this.client);
		this.buffers.add(buffer);
		buffer.addBufferListener(new BufferAdapter() {
			@Override
			public void onBufferComplete(BufferCompleteEvent event) {
				System.out.println("buffer complete");
				BufferManager.this.triggerOnBufferComplete(event);
			}
			
			@Override
			public void onFileComplete(FileCompleteEvent event) {
				System.out.println("file complete");
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

	public void addBufferManagerListener(BufferManagerAdapter bufferManagerAdapter) {
		this.bufferManagerAdapters.add(bufferManagerAdapter);
	}

	private void triggerNewFileEvent(NewFileEvent event) {
		for (BufferManagerAdapter bufferManagerAdapter : this.bufferManagerAdapters)
			bufferManagerAdapter.onNewFile(event);
	}

	private void triggerOnFileComplete(FileCompleteEvent event) {
		for (BufferManagerAdapter bufferManagerAdapter : this.bufferManagerAdapters)
			bufferManagerAdapter.onFileComplete(event);
	}

	private void triggerOnNewBuffer(NewBufferEvent event) {
		for (BufferManagerAdapter bufferManagerAdapter : this.bufferManagerAdapters)
			bufferManagerAdapter.onNewBuffer(event);
	}

	private void triggerOnBufferComplete(BufferCompleteEvent event) {
		for (BufferManagerAdapter bufferManagerAdapter : this.bufferManagerAdapters)
			bufferManagerAdapter.onBufferComplete(event);
	}

	public Client getClient() {
		return client;
	}

	public void setClient(Client client) {
		this.client = client;
	}
}
