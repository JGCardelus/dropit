package packet.types;

import packet.Packet;

public class FilePacket extends Packet {
	public static final int MAX_BYTES = 1024;
	private byte[] bytes;

	public FilePacket(int id) {
		super(
			id,
			Packet.CODE_FILEPACKET,
			Packet.QOS_LEVEL_1,
			Packet.NO_PRIORITY
		);
	}

	public byte[] getBytes() {
		return bytes;
	}

	public void setBytes(byte[] bytes) {
		this.bytes = bytes;
	}
}
