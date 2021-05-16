package packet.types;

import packet.Packet;

public class ConfirmationPacket extends Packet {
	private int packetToConfirmId;

	public ConfirmationPacket(int id) {
		super(
			id,
			Packet.CODE_CONFIRMATION
		);
	}

	public int getPacketToConfirmId() {
		return packetToConfirmId;
	}

	public void setPacketToConfirmId(int packetToConfirmId) {
		this.packetToConfirmId = packetToConfirmId;
	}
}
