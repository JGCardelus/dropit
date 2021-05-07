package io.events;

import java.io.File;
import java.util.List;

import packet.Packet;

public class FileReadEvent {
	private File file;
	private List<Packet> packets;

	public FileReadEvent(File file, List<Packet> packets) {
		this.setFile(file);
		this.setPackets(packets);
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
}
