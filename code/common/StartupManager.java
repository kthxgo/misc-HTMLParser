package common;

//import ingestion.IngestionSupervisor;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

//import analyzer.AnalyzerSupervisor;
import crawler.CrawlerSupervisor;
import crawler.CrawlingRequest;
import database.DatabasePool;

/**
 * Startup Class<br>
 * provides main-method
 * 
 * @author S. Berndt
 * 
 */
public class StartupManager {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		new StartupManager();
	}

	private Logger	log;

	public StartupManager() {

		loadLogProperties();
		log = Logger.getLogger(StartupManager.class.getName());
		log.setLevel(Level.FINEST);
		log.info("WebArchive - Startup");
		log.info("loading Configuration...");
		Config config = Config.getInstance();
		config.loadXML();
		config.saveXML();
		log.info("Configuration loaded and rewritten");
		
		CrawlerSupervisor crawler = new CrawlerSupervisor();
		DatabasePool dbpool = new DatabasePool();
		crawler.linkDatabase(dbpool);
//		IngestionSupervisor ingestion = new IngestionSupervisor();
//		AnalyzerSupervisor analyzer = new AnalyzerSupervisor(crawler, ingestion.getMyIPManager());
		
//		analyzer.startSupervisor();
//		ingestion.startSupervisor();
		
		
		URL url;
		try {
			url = new URL("http://german-bash.org/");
			CrawlingRequest req = new CrawlingRequest(url, 5, System.currentTimeMillis());
			crawler.scheduleCrawling(req);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}

	private void loadLogProperties() {
		System.setProperty("java.util.logging.config.file", Config.LOG_CONFIGFILE_LOCATION.getPath());
		try {
			LogManager.getLogManager().readConfiguration();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
