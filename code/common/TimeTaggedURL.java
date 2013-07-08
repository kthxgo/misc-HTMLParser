package common;

import java.io.Serializable;
import java.net.URL;


/**
 * a Wrapper Class used to store a <code>long</code>-Timestamp to a given URL. Since {@link URL} is final there is now way to extend a new Class from it, wrapping is the only Solution.
 * @author S. Berndt
 *
 */
public class TimeTaggedURL implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final long	timeStamp;
	private final URL	url;
	
	
	/**
	 * Creates the specified Value-Set
	 * @param url the URL
	 * @param timeStamp the Timestamp as a <code>long</code>
	 */
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
