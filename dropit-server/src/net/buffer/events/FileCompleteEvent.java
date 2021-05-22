package net.buffer.events;

import java.util.LinkedList;
import java.util.List;

import packet.Packet;
import packet.types.FileHeaderPacket;
import packet.types.FilePacket;

public class FileCompleteEvent {
	private FileHeaderPacket header;
	private List<Packet> packets;

	/**
	 * Triggered when buffer is complete and buffer contains a file.
	 * @param header
	 * @param packets
	 */
	public FileCompleteEvent(FileHeaderPacket header, List<Packet> packets) {
		this.setHeader(header);	
		this.setPackets(packets);
	}

	public FileHeaderPacket getHeader() {
		return header;
	}

	public void setHeader(FileHeaderPacket header) {
		this.header = header;
	}

	public List<Packet> getPackets() {
		return packets;
	}

	public List<FilePacket> getFilePackets() {
		List<FilePacket> filePackets = new LinkedList<FilePacket>();
		for (Packet packet : this.getPackets()) {
			if (packet instanceof FilePacket) {
				filePackets.add((FilePacket) packet);
			}
		}
		return filePackets;
	}

	public void setPackets(List<Packet> packets) {
		this.packets = packets;
	}
}
