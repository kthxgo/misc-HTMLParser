package dataStructures;

import java.awt.image.BufferedImage;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

import org.jdom2.Document;

/**
 * Website Object<br>
 * Stores Information about the Website beside the Website text itself
 * 
 * @author S. Berndt
 * 
 */
public class Website implements Delayed{

	private static final long			TIME_UNDEFINIED	= -1;

	// meta informations - such as language, author and description
	private HashSet<MetaInformation<?>>	metaInformations;
	// keywords - such as golf, schoolsystem, doughnut
	private HashSet<String>				keyWords;
	// url refeering to this Website
	private URL					url;
	// The Website in raw String Format
	private String						raw;
	// Timestamp of the Website readout
	private long						timeStamp;
	// Dom representation of this site
	private Document					domStructure;
	//XML boolean for Priority Ordering
	private boolean						XML;

	private List<BufferedImage>	imgs;
	
	public Website() {
		setXml(false);
	}
	
	public Website(URL url) {
		this.url = url;
		metaInformations = new HashSet<MetaInformation<?>>();
		keyWords = new HashSet<String>();
		imgs = new ArrayList<BufferedImage>();
		raw = null;
		domStructure = null;
		timeStamp = TIME_UNDEFINIED;
	}

	public void setTimeStamp() {
		this.timeStamp = System.currentTimeMillis();
	}

	public URL getUrl() {
		return url;
	}

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
	
	public List<BufferedImage> getImgs() {
		return imgs;
	}

	public boolean isXML() {
		return XML;
	}

	public void setXml(boolean xml) {
		this.XML = xml;
	}

	//Ingestion - DelayedQueue necessary Methods for "implements Delayed"
	@Override
	public int compareTo(Delayed A) {
		//Cast wirklich notwendig?
		//Evtl. return Werte falsch
		if(((Website) A).isXML()==this.isXML()){
			return 0;
		}
		else if(((Website) A).isXML()&&this.isXML()==false){
			return 1;
		}
		else{
			return -1;
		}
		
	}

	@Override
	public long getDelay(TimeUnit arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

}
