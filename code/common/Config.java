package common;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

import common.Config.Keys_Defs.DoubleTypes;
import common.Config.Keys_Defs.IntTypes;
import common.Config.Keys_Defs.StringTypes;


/**
 * 
 * @author S. Berndt
 *
 */
public class Config {

	private static final String	INT_TYPES			= "intTypes";
	private static final String	DOUBLE_TYPES		= "doubleTypes";
	private static final String	STRING_TYPES		= "stringTypes";

	private static final File	CONFIGFILE_LOCATION	= new File("." + File.separator + "config" + File.separator +  "config.xml");
	public static final File	LOG_CONFIGFILE_LOCATION	= new File("." + File.separator + "config" + File.separator +  "logging.properties");
	
	private static final Config singleton = new Config();
	
	private Logger log;
	
	private Config(){
		log = Logger.getLogger(this.getClass().getName());
		log.setLevel(Level.FINEST);
	}
	
	
	public static Config getInstance(){
		return singleton;
	}

	public void loadXML() {
		if (CONFIGFILE_LOCATION.exists()) {
			log.fine("Config File existing, try to load values...");
			try {
				Element root = new SAXBuilder().build(CONFIGFILE_LOCATION).getRootElement();
				Element ints = root.getChild(INT_TYPES,root.getNamespace());
				Element doubles = root.getChild(DOUBLE_TYPES,root.getNamespace());
				Element strings = root.getChild(STRING_TYPES,root.getNamespace());
				for (IntTypes key : Keys_Defs.IntTypes.values()){
					try {
						Element valueSet = ints.getChild(key.name(), ints.getNamespace());
						int value = Integer.parseInt(valueSet.getText());
						key.setKey(value);
						log.finest("loaded " +  key.name() + " = " + value);
					} catch (NullPointerException e) {
//						e.printStackTrace();
					} catch (NumberFormatException e) {
						e.printStackTrace();
					} 
				}
				for (DoubleTypes key : Keys_Defs.DoubleTypes.values()){
					try {
						Element valueSet = doubles.getChild(key.name(), doubles.getNamespace());
						double value = Double.parseDouble(valueSet.getText());
						key.setKey(value);
						log.finest("loaded " +  key.name() + " = " + value);
					} catch (NullPointerException e) {
//						e.printStackTrace();
					} catch (NumberFormatException e) {
						e.printStackTrace();
					} 
				}
				for (StringTypes key : Keys_Defs.StringTypes.values()){
					try {
						Element valueSet = strings.getChild(key.name(), strings.getNamespace());
						String value = valueSet.getText();
						key.setKey(value);
						log.finest("loaded " +  key.name() + " = " + value);
					} catch (NullPointerException e) {
//						e.printStackTrace();
					}			
				}
			} catch (JDOMException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			log.fine("Config File does not exist");
		}
	}

	public void saveXML(){
		log.fine("Save Config to File");
		Element root = new Element("config");
		Element ints = new Element(INT_TYPES);
		Element doubles = new Element(DOUBLE_TYPES);
		Element strings = new Element(STRING_TYPES);
		for (IntTypes key : Keys_Defs.IntTypes.values()){
			ints.addContent(new Element(key.name()).setText(key.getKey() + ""));
			log.finest("saved " + key.name() + "(" + key.getKey() + ")");
		}
		for (DoubleTypes key : Keys_Defs.DoubleTypes.values()){
			doubles.addContent(new Element(key.name()).setText(key.getKey() + ""));
			log.finest("saved " + key.name() + "(" + key.getKey() + ")");			
		}
		for (StringTypes key : Keys_Defs.StringTypes.values()){
			strings.addContent(new Element(key.name()).setText(key.getKey() + ""));		
			log.finest("saved " + key.name() + "(" + key.getKey() + ")");	
		}
		root.addContent(ints);
		root.addContent(doubles);
		root.addContent(strings);
		try {
			if (!CONFIGFILE_LOCATION.getParentFile().exists()) {
				CONFIGFILE_LOCATION.getParentFile().mkdirs();
			}
			FileWriter fileOut = new FileWriter(CONFIGFILE_LOCATION);
			new XMLOutputter(Format.getPrettyFormat()).output(new Document(root), fileOut);
			fileOut.close();
			log.finer("saving done");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static class Keys_Defs {

		public static enum IntTypes {
			JUnit_TestValue(42),

			CrawlerSupervisor_ThreadPoolCore(2), 
			CrawlerSupervisor_ThreadPoolMax(8),
			CrawlerSupervisor_MaximumCrawlingDepth(32),
			Crawler_MaxSiteSize(25_000_000),
			
			CrawlerSupervisor_MinTimeDiff(1000*60*60*14),
			
			Plugin_Length(5000),
			
			Ingestion_msPerDatabaseInput(0),
			Ingestion_preferInput(1),
			Ingestion_maxThreads(5),
			Ingestion_compress(1);

			private int	key;

			IntTypes(int key) {
				this.key = key;
			}

			public void setKey(int key) {
				this.key = key;
			}

			public int getKey() {
				return key;
			}

		}

		public static enum DoubleTypes {
			PlaceHolder_Dunno(13.37);

			private double	key;

			DoubleTypes(double key) {
				this.key = key;
			}

			public void setKey(double key) {
				this.key = key;
			}

			public double getKey() {
				return key;
			}

		}

		public static enum StringTypes {
			
			DataBase_RootFolderLocation("." + File.separator + "db"),
			JUnit_DemoValue("whatsoever"),
			
			Compression_MainPath("." + File.separator + "compression" + File.separator),			
			Plugin_Language("DE; ENG"),
			Plugin_Domain("DE; COM"),
			
			
			Ingestion_rootDirectory("." + File.separator + "xmlData"),
			Ingestion_validator("." + File.separator + "Schema.xsd"),
			Ingestion_crawlerDate("2013-5-30"), //Later set to Today
			Ingestion_titel("myTitel"); 

			private String	key;

			StringTypes(String key) {
				this.key = key;
			}

			public void setKey(String key) {
				this.key = key;
			}

			public String getKey() {
				return key;
			}

		}
	}

}
