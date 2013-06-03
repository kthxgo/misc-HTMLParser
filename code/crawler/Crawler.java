package crawler;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.ImageIO;
import javax.naming.LimitExceededException;
import javax.naming.SizeLimitExceededException;

import org.jdom2.Document;
import org.jdom2.Element;

import common.Config;

import plugins.Plugin;
import plugins.PluginFactory;

import dataStructures.Website;

/** Basic Crawler Class
 * 
 * @author S. Berndt */
public class Crawler implements Runnable {

	private BlockingQueue<Website>	output;
	private CrawlingRequest			crawlingRequest;
	private CrawlerSupervisor		supervisor;
	private Website					wipWebsite;
	private Logger					log;

	/** Creates a Crawler Instance for a specified Website
	 * 
	 * @param crawlingRequest
	 *            A Request for crawling the refeering Website
	 * @param outputQueue
	 *            A {@link BlockingQueue<Website>} in wich finished Website
	 *            Objects are put into
	 * @param supervisor
	 *            A Supervisor */
	public Crawler(CrawlingRequest crawlingRequest, BlockingQueue<Website> outputQueue, CrawlerSupervisor supervisor) {
		log = Logger.getLogger(this.getClass().getName());
		log.setLevel(Level.FINEST);
		this.crawlingRequest = crawlingRequest;
		this.output = outputQueue;
		this.supervisor = supervisor;
	}

	/** Checks with all in the {@link CrawlerSupervisor} set Filterplugins if the
	 * Website should be archived or not
	 * 
	 * @return <code>true</code> if all Plugins apreachiate the Website,
	 *         <code>false</code> if not */
	private boolean checkWebsite() {
		log.info("gonna check website");
		PluginFactory pFac = new PluginFactory();
		List<Plugin> filters = new LinkedList<Plugin>();
		List<Future<Boolean>> results = null;
		log.fine("creating Plugins...");
		for (PluginFactory.Filters plugin : supervisor.getActivePlugins()) {
			filters.add(pFac.createPlugin(plugin, wipWebsite));
		}
		log.fine("executing Plugins...");
		if (filters.size() > 0) {
			ExecutorService pluginExecutor = Executors.newFixedThreadPool(filters.size());
			try {
				results = pluginExecutor.invokeAll(filters);
				for (Future<Boolean> result : results) {
					if (result.get().booleanValue() == false) {
						log.info("a Plugin disaproved the website");
						return false;
					}
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (ExecutionException e) {
				e.printStackTrace();
			}
		}
		log.info("website is valid - at least the plugins say so");
		return true;
	}

	/** downloads the Website<br>
	 * nothing more, nothing less */
	private boolean download(){
		log.info("downloading the Website");
		StringBuilder site = new StringBuilder();
		try {
			SizeLimitedInputStream inLimiter = new SizeLimitedInputStream(crawlingRequest.getUrl().openStream(), Config.Keys_Defs.IntTypes.Crawler_MaxSiteSize.getKey());
			BufferedReader in = new BufferedReader(new InputStreamReader(inLimiter));
			String line = null;
			while ((line = in.readLine()) != null) {
				site.append(line + "\n");
			}
			in.close();
			if (inLimiter.bytesLeft()) {
				wipWebsite.setSiteText(site.toString());
				log.info("downloading finished");
				return true;
			} else {
				wipWebsite.setSiteText(null);
				log.info("could not download Website - Input Stream never ended - Blackhole?");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}

	/** analyzes the website and create Crawling Requests for the found
	 * Hyperlinks, wich will send to the {@link CrawlerSupervisor} */
	private void analyseLinks() {
		log.info("search for links within the Dom-Structure");
		Element htmlRoot = wipWebsite.getDomStructure().getRootElement();
		checkElementForHref(htmlRoot);
	}

	private void checkElementForHref(Element element) {
		if (element.getName().equalsIgnoreCase("a")) {
			try {
				CrawlingRequest cr = new CrawlingRequest(new URL(element.getAttributeValue("href")), crawlingRequest.getRecursion() + 1, System.currentTimeMillis());
				supervisor.scheduleCrawling(cr);
			} catch (MalformedURLException e) {
				e.printStackTrace();
			}
			for (Element child : element.getChildren()) {
				checkElementForHref(child);
			}
		}
	}

	public int downloadImages(URL url) {
		log.fine("downloading an Image");
		try {
			BufferedImage img = ImageIO.read(url);
			wipWebsite.getImgs().add(img);
			return wipWebsite.getImgs().indexOf(img);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return -1;
	}

	/** trys to refactor the Website to a {@link Document}<br>
	 * because HTML is no 100% valid XML-Format results may be inaccurate <br>
	 * if parsing breaks on a high Level the {@link Document} will be
	 * <code>null</code> <br>
	 * !!! unreliable !!! */
	private void refactorToXML() {
		wipWebsite.setDomStructure(new HTMLParser().parse(wipWebsite.getSiteText()));
	}

	/** stores the website into the Queue given by the Constructor */
	private void outputWebsite() {
		try {
			output.put(wipWebsite);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		log.info("crawling finished - putting website into outputqueue");
	}

	@Override
	public void run() {

		wipWebsite = new Website(crawlingRequest.getUrl());
		if (download()) {
			refactorToXML();
			if (checkWebsite()) {
				analyseLinks();
				outputWebsite();
			}	
		}
		
	}

}
