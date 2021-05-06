package packet.types;

import packet.Packet;

public class FilePacket extends Packet {
	public static final int MAX_BYTES = 1024;
	private byte[] bytes;

	public FilePacket(int id) {
		this(
			id, 
			Packet.DEFAULT_CODE, 
			Packet.DEFAULT_QOS,
			Packet.DEFAULT_PRIORITY
		);
	}
	
	public FilePacket(int id, int code, int qos) {
		this(
			id,
			code,
			qos,
			Packet.DEFAULT_PRIORITY
		);
	}

	public FilePacket(int id, int code, int qos, int priority) {
		super(id, code, qos, priority);
	}

	public byte[] getBytes() {
		return bytes;
	}

	public void setBytes(byte[] bytes) {
		this.bytes = bytes;
	}
}
