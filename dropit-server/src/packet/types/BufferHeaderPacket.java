package packet.types;

import java.util.LinkedList;
import java.util.List;

import packet.Packet;

public class BufferHeaderPacket extends Packet {
	public static final int NO_BUFFER_ID = -1;
	public static final int NO_BUFFER_HASH = -1;
	
	public static final int DEFAULT_BUFFER_TYPE = 0;

	private int bufferId = NO_BUFFER_ID;
	private List<Integer> packetIds = new LinkedList<Integer>();
	private int bufferHash = NO_BUFFER_HASH;
	private int bufferType = DEFAULT_BUFFER_TYPE;

	public BufferHeaderPacket(int id) {
		super(
			id, 
			Packet.CODE_FILEHEADER,
			Packet.QOS_LEVEL_1,
			Packet.MAX_PRIORITY);
	}

	public int getBufferType() {
		return bufferType;
	}
	public void setBufferType(int bufferType) {
		this.bufferType = bufferType;
	}
	public int getBufferId() {
		return bufferId;
	}
	public void setBufferId(int fileId) {
		this.bufferId = fileId;
	}
	public List<Integer> getPacketIds() {
		return packetIds;
	}
	public void setPacketIds(List<Integer> packetIds) {
		this.packetIds = packetIds;
	}
	public int getBufferHash() {
		return bufferHash;
	}
	public void setBufferHash(int fileHash) {
		this.bufferHash = fileHash;
	}
	public void addPacket(int filePacketId) {
		this.packetIds.add(filePacketId);
	}
}
