package common;

import java.net.URL;

public class TimeTaggedURL {

	private final long	timeStamp;
	private final URL	url;
	
	public TimeTaggedURL(URL url, long timeStamp) {
		this.url = url;
		this.timeStamp = timeStamp;
	}
	
	public long getTimeStamp() {
		return timeStamp;
	}
	
	public URL getUrl() {
		return url;
	}
	

}
