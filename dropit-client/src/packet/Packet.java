package packet;

public class Packet implements java.io.Serializable {
	public static final int DEFAULT_CODE = 0;
	
	private int id;
	private int code;

	public Packet(int id) {
		this(id, DEFAULT_CODE);
	}

	public Packet(int id, int code) {
		this.setId(id);
		this.setCode(code);
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + code;
		result = prime * result + id;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Packet other = (Packet) obj;
		if (code != other.code)
			return false;
		if (id != other.id)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Packet [code=" + code + ", id=" + id + "]";
	}
}
