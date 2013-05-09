package dataStructures;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashSet;

import org.jdom2.Document;

/**
 * Website Object<br>
 * Stores Information about the Website beside the Website text itself
 * 
 * @author S. Berndt
 * 
 */
public class Website {

	private static final long			TIME_UNDEFINIED	= -1;

	// meta informations - such as language, autor and description
	private HashSet<MetaInformation<?>>	metaInformations;
	// keywords - such as golf, schoolsystem, dounut
	private HashSet<String>				keyWords;
	// url refeering to this Website
//	private final URL					url;
	// The Website in raw String Format
	private String						raw;
	// Timestamp of the Website readout
	private long						timeStamp;
	// Dom representation of this site
	private Document					domStructure;

	public Website() throws MalformedURLException {
//		url = new URL("");
		metaInformations = new HashSet<MetaInformation<?>>();
		keyWords = new HashSet<String>();
		raw = null;
		domStructure = null;
		timeStamp = TIME_UNDEFINIED;
	}

	public void setTimeStamp() {
		this.timeStamp = System.currentTimeMillis();
	}

//	public URL getUrl() {
//		return url;
//	}

	public long getTimeStamp() {
		return timeStamp;
	}

	public void addMetaInformation(MetaInformation<?> mInformation) {
		metaInformations.add(mInformation);
	}

	public HashSet<MetaInformation<?>> getMetaInformations() {
		return metaInformations;
	}

	public String getSiteText() {
		return raw;
	}

	public void setSiteText(String siteText) {
		this.raw = siteText;
	}

	public HashSet<String> getKeyWords() {
		return keyWords;
	}

	public void addKeyWord(String keyWord) {
		keyWords.add(keyWord);
	}
	
	public Document getDomStructure() {
		return domStructure;
	}
	
	public void setDomStructure(Document domStructure) {
		this.domStructure = domStructure;
	}

}
