package sehati.util.file;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;


public class ReverseLineInputStream extends InputStream {

	private RandomAccessFile in;
	private long currentLineStart = -1;
	private long currentLineEnd = -1;
	private long currentPos = -1;
	private long lastPosInFile = -1;

	public ReverseLineInputStream(File file) throws FileNotFoundException {
		in = new RandomAccessFile(file, "r");
		currentLineStart = file.length();
		currentLineEnd = file.length();
		lastPosInFile = file.length() - 1;
		currentPos = currentLineEnd;
	}


	public void findPrevLine() throws IOException {
		currentLineEnd = currentLineStart;

		if (currentLineEnd == 0) {
			currentLineEnd = -1;
			currentLineStart = -1;
			currentPos = -1;
		}

		long filePointer = currentLineStart -1;
		while(true) {
			filePointer --;
			if (filePointer < 0) {
				break;
			}
			in.seek(filePointer);
			int readByte = in.readByte();

			if (readByte == 0xA && filePointer != lastPosInFile) {
				break;
			}
		}
		currentLineStart = filePointer + 1;
		currentPos = currentLineStart;
	}

	@Override
	public int read() throws IOException {
		if (currentPos < currentLineEnd) {
			in.seek(currentPos++);
			int readByte = in.readByte();
			return readByte;
		}
		else if (currentPos < 0) {
			return -1;
		}
		else {
			findPrevLine();
			return read();
		}
	}

}
