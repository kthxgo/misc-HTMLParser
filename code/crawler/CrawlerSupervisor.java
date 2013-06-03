package crawler;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

import plugins.PluginFactory;

import common.Config;
import common.FastShutdown;

import dataStructures.Website;
import database.Database;
import database.DatabasePool;

/**
 * Supervisor Class for Crawler Instances
 * 
 * @author S. Berndt
 * 
 */
public class CrawlerSupervisor implements FastShutdown {

	private Config						config;

	private BlockingQueue<Runnable>		crawlerRunnables;
	private List<PluginFactory.Filters>	activePlugins;
	private BlockingQueue<Website>		crawledWebsites;
	private ThreadFactory				threadFactory;

	private DatabasePool				databasePool;

	private ThreadPoolExecutor			executor;
	
	private Logger log;

	/**
	 * inserts a Crawling Request into the requestQueue
	 * 
	 * @param request
	 */
	public void scheduleCrawling(CrawlingRequest request) {
		log.info("Crawling Request recieved");
		if (databasePool != null) {
			Database db = databasePool.getDatabaseConnection();
			long lastVisit = db.getLastVisit(request.getUrl());
			if (lastVisit > System.currentTimeMillis() - Config.Keys_Defs.IntTypes.CrawlerSupervisor_MinTimeDiff.getKey() || lastVisit < 0) {
				log.fine("Website is kinda new, crawling will be scheduled");
				Crawler crawlerRunnable = new Crawler(request, crawledWebsites, this);
				executor.execute(crawlerRunnable);
			} else {
				log.fine("Website has been crawled not long ago");
			}
		}
	}

	/**
	 * loads remaining download jobs and the list of already visited websites
	 * from the given Database
	 * 
	 * @param database
	 *            connected Database
	 */
	public void restoreWorkspace(Database database) {
	}

	/**
	 * 
	 * @return a List of the selected FilterPlugins
	 */
	public List<PluginFactory.Filters> getActivePlugins() {
		return activePlugins;
	}

	/**
	 * 
	 * @return a Website Object<br>
	 *         blocks of no Website Object is avialable
	 */
	public Website getCrawledWebsite() {
		Website website = null;
		try {
			website = crawledWebsites.take();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return website;
	}

	/**
	 * 
	 * @param plugin
	 *            A Plugin wich should be added to the active Plugins
	 */
	public void insertPlugin(PluginFactory.Filters plugin) {
		activePlugins.add(plugin);
		log.info("Plugin " + plugin.name() + " has been inserted");
	}

	public CrawlerSupervisor() {
		config = Config.getInstance();
		log = Logger.getLogger(this.getClass().getName());
		log.setLevel(Level.FINE);
		crawlerRunnables = new LinkedBlockingQueue<Runnable>();
		activePlugins = new LinkedList<PluginFactory.Filters>();
		crawledWebsites = new LinkedBlockingQueue<Website>();
		threadFactory = new CrawlerThreadFactory();
		executor = new ThreadPoolExecutor(Config.Keys_Defs.IntTypes.CrawlerSupervisor_ThreadPoolCore.getKey(), Config.Keys_Defs.IntTypes.CrawlerSupervisor_ThreadPoolMax.getKey(), 30,
				TimeUnit.SECONDS, crawlerRunnables, threadFactory);
		Config.Keys_Defs.StringTypes.JUnit_DemoValue.getKey();
		databasePool = null;
		log.info("Crawler Supervisor initialized");
	}
	
	public void linkDatabase(DatabasePool database){
		this.databasePool = database;
		log.info("Database linked");
	}

	@Override
	public Thread shutdown() {
		Thread shutdown = new Thread() {
			@Override
			public void run() {
				// TODO
				// shutdown related stuff
				executor.shutdown();
			}
		};
		shutdown.start();
		return shutdown;
	}
}
