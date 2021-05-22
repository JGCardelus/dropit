package ui.downloads.table;

import java.awt.GridLayout;
import java.awt.FlowLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;

import net.buffer.Buffer;
import net.buffer.events.BufferAdapter;
import net.buffer.events.BufferNewPacketEvent;
import packet.types.FileHeaderPacket;
import ui.App;

public class DownloadsTableRow extends JPanel {
	private FileHeaderPacket fileHeader;
	private Buffer fileBuffer;

	private JProgressBar pbrFile = new JProgressBar(0, 100);
	private JLabel lblFile = new JLabel();

	public DownloadsTableRow(FileHeaderPacket fileHeader, Buffer fileBuffer) {
		this.setFileHeader(fileHeader);
		this.setFileBuffer(fileBuffer);

		this.setLayout(new GridLayout(0,2));
		
		JPanel lblFileWrapper = new JPanel();
		lblFileWrapper.setBorder(App.createMargin(App.MARGIN_SIZE, 0));
		lblFileWrapper.setLayout(new FlowLayout(FlowLayout.CENTER));
		lblFileWrapper.add(this.lblFile);

		this.add(lblFileWrapper);
		this.add(this.pbrFile);
	}

	public FileHeaderPacket getFileHeader() {
		return fileHeader;
	}

	public void setFileHeader(FileHeaderPacket fileHeader) {
		this.fileHeader = fileHeader;
		this.lblFile.setText(fileHeader.getFileName());
	}

	public Buffer getFileBuffer() {
		return fileBuffer;
	}

	public void setFileBuffer(Buffer fileBuffer) {
		this.fileBuffer = fileBuffer;
		this.fileBuffer.addBufferListener(new BufferAdapter(){
			@Override
			public void onBufferNewPacket(BufferNewPacketEvent event) {
				DownloadsTableRow.this.setPercentage(DownloadsTableRow.this.fileBuffer.getPercentage());
			}
		});
	}

	public void setPercentage(int percentage) {
		this.pbrFile.setValue(percentage);
	}
}