package crawler;

import java.io.IOException;
import java.io.InputStream;

public class SizeLimitedInputStream extends InputStream {

	private long		bytesLeft;
	private InputStream	capsulatedInputStream;

	public SizeLimitedInputStream(InputStream inputStream, long maxSize) {
		this.bytesLeft = maxSize;
		this.capsulatedInputStream = inputStream;
	}

	@Override
	public int read() throws IOException {
		int read = -1;
		if (bytesLeft()) {
			read = capsulatedInputStream.read();
			bytesLeft--;
		}
		return read;
	}
	
	public boolean bytesLeft() {
		return (bytesLeft > 0);
	}
	
	public long bytesRemaining() {
		return bytesLeft;
	}

}
