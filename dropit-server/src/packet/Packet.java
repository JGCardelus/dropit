package packet;

public class Packet implements java.io.Serializable {
	public static final int DEFAULT_CODE = 0;
	public static final int DEFAULT_PRIORITY = 0;
	
	
	public static final int MAX_PRIORITY = 3;
	public static final int MID_PRIORITY = 2;
	public static final int NO_PRIORITY = 1;
	
	private int id;
	private int code;
	private int priority;

	public Packet(int id) {
		this(id, DEFAULT_CODE, DEFAULT_PRIORITY);
	}
	
	public Packet(int id, int code) {
		this(id, code, DEFAULT_PRIORITY);
	}

	public Packet(int id, int code, int priority) {
		this.setId(id);
		this.setCode(code);
		this.setPriority(priority);
	}

	public int getPriority() {
		return priority;
	}

	public void setPriority(int priority) {
		this.priority = priority;
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
		result = prime * result + priority;
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
		if (priority != other.priority)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Packet [code=" + code + ", id=" + id + ", priority=" +  priority +"]";
	}
}
