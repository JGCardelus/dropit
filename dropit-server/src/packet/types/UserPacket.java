package packet.types;

import app.users.User;
import packet.Packet;

public class UserPacket extends Packet {
	private User user;

	public UserPacket(int id) {
		super(
			id,
			Packet.CODE_USERPACKET,
			Packet.QOS_LEVEL_0,
			Packet.MID_PRIORITY
		);
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
}
