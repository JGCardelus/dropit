package io.threads.files;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import io.IOManager;
import io.events.FileReadAdapter;
import io.events.FileReadEvent;
import net.server.Server;
import packet.Packet;
import packet.types.BufferHeaderPacket;
import packet.types.FilePacket;

public class FileReadThread extends Thread {
	private File file;
	private List<Packet> packets;

	// To generate ids
	private IOManager ioManager;
	private Server server;

	// Events
	private List<FileReadAdapter> fileReadAdapters = new LinkedList<>();

	public FileReadThread(File file, Server server, IOManager ioManager) {
		this.setFile(file);
		this.setIoManager(ioManager);
	}

	@Override
	public void run() {
		if (this.file.exists()) {
			this.packets = new LinkedList<Packet>();

			try (FileInputStream fr = new FileInputStream(this.file)) {
				BufferHeaderPacket fileHeaderPacket = this.getBufferHeader();
				this.packets.add(fileHeaderPacket);

				while (fr.available() > 0) {
					int newFilePacketId = this.server.nextId();
					fileHeaderPacket.addPacket(newFilePacketId);
					FilePacket newFilePacket = new FilePacket(newFilePacketId);

					if (fr.available() >= FilePacket.MAX_BYTES) {
						newFilePacket.setBytes(fr.readNBytes(FilePacket.MAX_BYTES));
					} else {
						newFilePacket.setBytes(fr.readNBytes(fr.available()));
					}

					this.packets.add(newFilePacket);
				}
				this.triggerFileReadEvent();
			}
			catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public void triggerFileReadEvent() {
		FileReadEvent fre = new FileReadEvent(
			this.file, 
			this.packets
		);
		for (FileReadAdapter fileReadAdapter : this.fileReadAdapters)
			fileReadAdapter.onFileRead(fre);
	}

	public void addFileReadListener(FileReadAdapter adapter) {
		this.fileReadAdapters.add(adapter);
	}

	public BufferHeaderPacket getBufferHeader() {
		BufferHeaderPacket bufferHeaderPacket = new BufferHeaderPacket(this.server.nextId());
		bufferHeaderPacket.setBufferId(this.ioManager.nextId());

		return bufferHeaderPacket;
	}

	public List<FileReadAdapter> getFileReadAdapters() {
		return fileReadAdapters;
	}

	public void setFileReadAdapters(List<FileReadAdapter> fileReadAdapters) {
		this.fileReadAdapters = fileReadAdapters;
	}

	public File getFile() {
		return file;
	}

	public void setFile(File file) {
		this.file = file;
	}

	public List<Packet> getPackets() {
		return packets;
	}

	public void setPackets(List<Packet> packets) {
		this.packets = packets;
	}

	public Server getServer() {
		return server;
	}

	public void setServer(Server server) {
		this.server = server;
	}

	public IOManager getIoManager() {
		return ioManager;
	}

	public void setIoManager(IOManager ioManager) {
		this.ioManager = ioManager;
	}
	
}
