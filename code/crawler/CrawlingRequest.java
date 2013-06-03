package crawler;

import java.net.URL;

import common.TimeTaggedURL;

public class CrawlingRequest extends TimeTaggedURL{

	private final int		recursion;

	public CrawlingRequest(URL url, int recursion, long timestamp) {
		super(url, timestamp);
		this.recursion = recursion;
	}
	
	public int getRecursion() {
		return recursion;
	}

}
