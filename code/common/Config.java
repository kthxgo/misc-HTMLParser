package common;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

import common.Config.Keys_Defs.BooleanTypes;
import common.Config.Keys_Defs.IntTypes;
import common.Config.Keys_Defs.StringTypes;

/** Class used to store Runtime relavant Values which should be editable via an
 * external XML-File
 * 
 * @author S. Berndt */
public class Config {

	private static final String	INT_TYPES				= "intTypes";
	private static final String	BOOL_TYPES				= "boolTypes";
	private static final String	STRING_TYPES			= "stringTypes";

	private static final File	CONFIGFILE_LOCATION		= new File("." + File.separator + "config" + File.separator + "config.xml");
	public static final File	LOG_CONFIGFILE_LOCATION	= new File("." + File.separator + "config" + File.separator + "logging.properties");

	private static final Config	singleton				= new Config();

	private Logger				log;

	private Config() {
		log = Logger.getLogger(this.getClass().getName());
		log.setLevel(Level.ALL);
	}

	public static Config getInstance() {
		return singleton;
	}

	/** trys to load the Values from the XML-File. If there are Problems, e.g. a
	 * Value-Set is missing or the Value is unparsable the specified default Key
	 * is keept. */
	public void loadXML() {
		if (CONFIGFILE_LOCATION.exists()) {
			log.fine("Config File existing, try to load values...");
			try {
				Element root = new SAXBuilder().build(CONFIGFILE_LOCATION).getRootElement();
				Element ints = root.getChild(INT_TYPES, root.getNamespace());
				Element doubles = root.getChild(BOOL_TYPES, root.getNamespace());
				Element strings = root.getChild(STRING_TYPES, root.getNamespace());
				for (IntTypes key : Keys_Defs.IntTypes.values()) {
					try {
						Element valueSet = ints.getChild(key.name(), ints.getNamespace());
						int value = Integer.parseInt(valueSet.getText());
						key.setKey(value);
						log.finest("loaded " + key.name() + " = " + value);
					} catch (NullPointerException e) {
						log.finest(key.name() + " missing, will use default Value");
					} catch (NumberFormatException e) {
						e.printStackTrace();
					}
				}
				for (BooleanTypes key : Keys_Defs.BooleanTypes.values()) {
					try {
						Element valueSet = doubles.getChild(key.name(), doubles.getNamespace());
						boolean value = Boolean.parseBoolean(valueSet.getText());
						key.setKey(value);
						log.finest("loaded " + key.name() + " = " + value);
					} catch (NullPointerException e) {
						log.finest(key.name() + " missing, will use default Value");
					} catch (NumberFormatException e) {
						e.printStackTrace();
					}
				}
				for (StringTypes key : Keys_Defs.StringTypes.values()) {
					try {
						Element valueSet = strings.getChild(key.name(), strings.getNamespace());
						String value = valueSet.getText();
						key.setKey(value);
						log.finest("loaded " + key.name() + " = " + value);
					} catch (NullPointerException e) {
						log.finest(key.name() + " missing, will use default Value");
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

	/** trys to store all current Value-Sets into the XML-File. The File will be
	 * rewritten no matter what was in it. Unused Entries will be deleted within
	 * the XML-File. The Path to the XML-File is absolute specified within this
	 * Class as <code>CONFIGFILE_LOCATION</code>. If the Directory pointing to
	 * the XML-File does not exist the needed Structure will be generated */
	public void saveXML() {
		log.fine("Save Config to File");
		Element root = new Element("config");
		Element ints = new Element(INT_TYPES);
		Element bools = new Element(BOOL_TYPES);
		Element strings = new Element(STRING_TYPES);
		for (IntTypes key : Keys_Defs.IntTypes.values()) {
			ints.addContent(new Element(key.name()).setText(key.getKey() + ""));
			log.finest("saved " + key.name() + "(" + key.getKey() + ")");
		}
		for (BooleanTypes key : Keys_Defs.BooleanTypes.values()) {
			bools.addContent(new Element(key.name()).setText(key.getKey() + ""));
			log.finest("saved " + key.name() + "(" + key.getKey() + ")");
		}
		for (StringTypes key : Keys_Defs.StringTypes.values()) {
			strings.addContent(new Element(key.name()).setText(key.getKey() + ""));
			log.finest("saved " + key.name() + "(" + key.getKey() + ")");
		}
		root.addContent(ints);
		root.addContent(bools);
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

			DataBase_Port(3306),

			Analyzer_Shutdown_Timeout(2000),

			CrawlerSupervisor_ThreadPoolMax(8),
			CrawlerSupervisor_MaximumCrawlingDepth(4),
			Crawler_MaxSiteSize(25_000_000),

			CrawlerSupervisor_MinTimeDiff(1000 * 60 * 60 * 14),
			CrawlerSupervisor_ShutdownTimeout(5),

			CrawlerSupervisor_CrawlingRequestQueueSize(50),
			CrawlerOutputQueue_MaxSize(50),
			IngestionInputQueue_MaxSize(50),

			Plugin_Length(5000),

			Ingestion_msPerDatabaseInput(0),
			Ingestion_maxThreads(5),

			Analyzer_maxThreads(4);

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

		public static enum BooleanTypes {
			JUnit_TestValue(true),
			Ingestion_readXML(true),
			Ingestion_compress(true);

			private boolean	key;

			BooleanTypes(boolean key) {
				this.key = key;
			}

			public void setKey(boolean key) {
				this.key = key;
			}

			public boolean getKey() {
				return key;
			}

		}

		public static enum StringTypes {

			Webarchiv_websiteFolder("websites"),

			Queues_SaveLocation("queues"),

			DataBase_RootFolderLocation("." + File.separator + "db"),
			DataBase_Address("78.46.189.61"),
			DataBase_Database("webarchiv"),
			DataBase_Username("webarchiv"),
			DataBase_Password("t8TPuSZwwDQGtNuG"),
			JUnit_TestValue("DO_NOT_CHANGE"),

			Plugin_Language("DE; ENG"),
			Plugin_Domain("DE; COM; ORG"),

			Compression_StandartFilename("compressedFile"),

			Ingestion_rootDirectory("XML"),
			Ingestion_crawlerDate("date"),
			Ingestion_dateformat("dd-MM-yyyy HH:mm:ss"),
			Ingestion_url("url"),
			Ingestion_title("title"),
			Ingestion_prefix(""),
			Ingestion_delimiter("/ -"),
			Ingestion_ratedKeyWordTags("rKWT"),
			Ingestion_headMetaTags("tag1;tag2;tag3");

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
