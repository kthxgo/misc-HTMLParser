package dataStructures;

import java.io.File;
import java.io.Serializable;
import java.net.URL;
import java.util.HashMap;
import java.util.HashSet;
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

import org.jdom2.Document;

import common.Config;
import common.JDOMReader;

/** THE Website-Object
 * Stores Information about the Website beside the Website text itself. Used to
 * carry all needed Informations to Process the Website through the Programm.
 * 
 * @author S. Berndt */
public class Website implements Delayed, Serializable {
	private static final long		serialVersionUID	= 1L;

	private static final long		TIME_UNDEFINIED		= -1;

	// meta informations - such as language, author and description
	private HashMap<String, String>	headMetaInformations;
	// keywords - such as golf, schoolsystem, doughnut
	private HashSet<RatedKeyWord>	contentMetaInformations;
	// url refeering to this Website
	private URL						url;

	// title of the Website
	private String					title;

	// The Website in raw String Format
	private String					raw;
	// Timestamp of the Website readout
	private long					timeStamp;
	// Dom representation of this site
	private Document				domStructure;
	// XML boolean for Priority Ordering
	private boolean					XML;
	private boolean					correctXML;
	private File					currentLocation;

	private int						delayTime;
	
	

	public Website(URL url) {
		this.url = url;
		headMetaInformations = new HashMap<String, String>();
		contentMetaInformations = new HashSet<RatedKeyWord>();
		raw = null;
		domStructure = null;
		XML = false;
		setCorrectXML(false);
		setDelayTime(Config.Keys_Defs.IntTypes.Ingestion_msPerDatabaseInput.getKey());
		timeStamp = TIME_UNDEFINIED;
	}

	public void setTimeStamp() {
		this.timeStamp = System.currentTimeMillis();
	}

	public void setTimeStamp(long timestamp) {
		this.timeStamp = timestamp;
	}

	public URL getUrl() {
		return url;
	}

	public long getTimeStamp() {
		return timeStamp;
	}

	public void addMetaInformation(String Key, String Value) {
		headMetaInformations.put(Key, Value);
	}

	public HashMap<String, String> getMetaInformations() {
		return headMetaInformations;
	}

	public String getSiteText() {
		return raw;
	}

	public void setSiteText(String siteText) {
		this.raw = siteText;
	}

	public HashSet<RatedKeyWord> getRatedKeyWords() {
		return contentMetaInformations;
	}

	public void addRatedKeyWord(RatedKeyWord ratedKeyWord) {
		contentMetaInformations.add(ratedKeyWord);
	}

	public Document getDomStructure() {
		return domStructure;
	}

	public void setDomStructure(Document domStructure) {
		this.domStructure = domStructure;
	}


	public boolean isXML() {
		return XML;
	}

	public void setXml(boolean xml) {
		this.XML = xml;
	}

	public void setUrl(URL url) {
		this.url = url;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public File getCurrentLocation() {
		return currentLocation;
	}

	public void setCurrentLocation(File currentLocation) {
		this.currentLocation = currentLocation;
	}

	public boolean isCorrectXML() {
		return correctXML;
	}

	public void setCorrectXML(boolean correctXML) {
		this.correctXML = correctXML;
	}

	/** toString method to see what is inside the website-object
	 * 
	 * @param var
	 *            var == 1 => website-text (String) will be shown in return
	 *            var == 2 => JDOM-Document will be shown in return
	 * @return
	 *         a String in the way you wanted */
	public String toString(int var) {
		String result = "";

		result += "URL: " + url + "\n=========================\n";
		result += "Timestamp " + timeStamp + "\n=========================\n";
		result += "Meta-Informations: " + contentMetaInformations.toString() + "\n=========================\n";
		result += "Head-Informations: " + headMetaInformations.toString() + "\n=========================\n";
		result += "XML: " + XML + "\n=========================\n";
		result += "Location: " + currentLocation + "\n=========================\n";
		if (var == 1) {
			result += "Text: \n--------------------------------------------------------------------------\n" + raw
					+ "\n--------------------------------------------------------------------------\n=========================\n";
		}
		if (var == 2) {
			result += "JDOM: \n--------------------------------------------------------------------------\n";
			JDOMReader reader = new JDOMReader();
			result += reader.readJDOM(this, 2);
			result += "\n--------------------------------------------------------------------------\n=========================\n";
		}
		return result;
	}

	// Ingestion - DelayedQueue necessary Methods for "implements Delayed"
	@Override
	public int compareTo(Delayed A) {
		if(this.delayTime < ((Website) A).delayTime){
			return -1;
		}
		else if(this.delayTime > ((Website) A).delayTime){
			return 1;
		}
		return 0;
	}

	@Override
	public long getDelay(TimeUnit unit) {
		return unit.convert(Long.valueOf(delayTime) - System.currentTimeMillis(), TimeUnit.MILLISECONDS);
	}

	public int getDelayTime() {
		return delayTime;
	}

	public void setDelayTime(int delayTime) {
		this.delayTime = delayTime;
	}

}
