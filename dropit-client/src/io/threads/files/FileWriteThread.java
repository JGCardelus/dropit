package io.threads.files;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import packet.types.BufferHeaderPacket;
import packet.types.FilePacket;

public class FileWriteThread extends Thread {
	private BufferHeaderPacket bufferHeaderPacket;
	private List<FilePacket> packets;
	private File file;

	public FileWriteThread(File file, BufferHeaderPacket bufferHeaderPacket, List<FilePacket> packets) {
		this.setPackets(packets);
		this.setBufferHeaderPacket(bufferHeaderPacket);
		this.setFile(file);
	}

	@Override
	public void run() {
		try(FileOutputStream fos = new FileOutputStream(this.getFile())) {
			for (FilePacket packet : this.getPackets())
				fos.write(packet.getBytes());
		} catch (FileNotFoundException fnofe) {
			fnofe.printStackTrace();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
	}

	public BufferHeaderPacket getBufferHeaderPacket() {
		return bufferHeaderPacket;
	}

	public void setBufferHeaderPacket(BufferHeaderPacket bufferHeaderPacket) {
		this.bufferHeaderPacket = bufferHeaderPacket;
	}

	public File getFile() {
		return file;
	}

	public void setFile(File file) {
		this.file = file;
	}

	public List<FilePacket> getPackets() {
		return packets;
	}

	public void setPackets(List<FilePacket> packets) {
		this.packets = packets;
	}
}
