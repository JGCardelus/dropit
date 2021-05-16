package packet.types;

public class FileHeaderPacket extends BufferHeaderPacket {
	public final static int BUFFER_TYPE = 1;
	
	private String fileName;
	private String fileExtension;

	public FileHeaderPacket(int id) {
		super(id);
		this.setBufferType(BUFFER_TYPE);
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getFileExtension() {
		return fileExtension;
	}

	public void setFileExtension(String fileExtension) {
		this.fileExtension = fileExtension;
	}
}
