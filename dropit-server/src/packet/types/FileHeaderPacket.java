package packet.types;

import java.util.LinkedList;
import java.util.List;

import packet.Packet;

public class FileHeaderPacket extends Packet {
	public static final int NO_FILE_ID = -1;
	public static final int NO_FILE_HASH = -1;
	
	private int fileId = NO_FILE_ID;
	private List<Integer> packetIds = new LinkedList<Integer>();
	private int fileHash = NO_FILE_HASH;
	
	public FileHeaderPacket(int id) {
		super(
			id, 
			Packet.CODE_FILEHEADER,
			Packet.QOS_LEVEL_1,
			Packet.MAX_PRIORITY);
	}

	public int getFileId() {
		return fileId;
	}
	public void setFileId(int fileId) {
		this.fileId = fileId;
	}
	public List<Integer> getPacketIds() {
		return packetIds;
	}
	public void setPacketIds(List<Integer> packetIds) {
		this.packetIds = packetIds;
	}
	public int getFileHash() {
		return fileHash;
	}
	public void setFileHash(int fileHash) {
		this.fileHash = fileHash;
	}
	public void addPacket(int filePacketId) {
		this.packetIds.add(filePacketId);
	}
}
