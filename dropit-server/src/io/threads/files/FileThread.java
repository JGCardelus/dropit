package io.threads.files;

import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import packet.types.FilePacket;

public class FileThread extends Thread {
	private String path;
	private File file;

	private List<FilePacket> filePackets;

	public FileThread(String path) {
		this.setPath(path);
	}

	@Override
	public void run() {
		if (this.file.exists()) {
			System.out.println("File exists!!");
			this.filePackets = new LinkedList<FilePacket>();
			try (FileInputStream fr = new FileInputStream(this.file)) {
				while (fr.available() > 0) {
					FilePacket newFilePacket = new FilePacket(1);
					if (fr.available() >= FilePacket.MAX_BYTES) {
						newFilePacket.setBytes(fr.readNBytes(FilePacket.MAX_BYTES));
					} else {
						newFilePacket.setBytes(fr.readNBytes(fr.available()));
					}
					this.filePackets.add(newFilePacket);
				}
			}
			catch (EOFException e) {
				System.out.println("I've reached the end of the file");
			} 
			catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			catch (IOException e) {
				e.printStackTrace();
			}
		}

		try (FileOutputStream fw = new FileOutputStream(new File("test.txt"))) {
			for (FilePacket filePacket : this.filePackets) {
					fw.write(filePacket.getBytes());
			}
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
				
	}

	public File getFile() {
		return file;
	}

	public void setFile(File file) {
		this.file = file;
	}

	public List<FilePacket> getFilePackets() {
		return filePackets;
	}

	public void setFilePackets(List<FilePacket> filePackets) {
		this.filePackets = filePackets;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
		if (this.path != null) {
			this.setFile(new File(path));
		}
	}


}
