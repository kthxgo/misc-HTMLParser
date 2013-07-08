package common;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

import crawler.CrawlingRequest;

import dataStructures.Website;

/**Class for methods that save and load queues
 * 
 * @author J. Dietrich
 *
 */
public class QueueHandler {
	
	private Logger log;
	
	private String				CRAWLERINPUTQUEUE_LOCATION = Config.Keys_Defs.StringTypes.Queues_SaveLocation.getKey() + File.separator + "CrawlerInputQueue.bin";
	private String				CRAWLEROUTPUTQUEUE_LOCATION = Config.Keys_Defs.StringTypes.Queues_SaveLocation.getKey() + File.separator + "CrawlerOutputQueue.bin";
	private String				INGESTIONQUEUE_LOCATION = Config.Keys_Defs.StringTypes.Queues_SaveLocation.getKey() + File.separator + "IngestionInputQueue.bin";
	
	private BlockingQueue<CrawlingRequest> crawlerInput;
	private BlockingQueue<Website> crawlerOutput;
	private BlockingQueue<Website> ingestionInput;
	
	/**
	 * just sets up log
	 */
	public QueueHandler() {
		log = Logger.getLogger(this.getClass().getName());
		log.setLevel(Level.INFO);
	}

	
	/** Method to save the CrawlerOutputQueue
	 * 
	 * @param crawlerInputQueue the queue to save
	 */
	public void saveCrawlerInputQueue(BlockingQueue<CrawlingRequest> crawlerInputQueue) {
		if (crawlerInputQueue.size()>0) {
			File file = new File(CRAWLERINPUTQUEUE_LOCATION);
			File folder = new File(Config.Keys_Defs.StringTypes.Queues_SaveLocation.getKey());
			folder.getAbsoluteFile().mkdirs();
			crawlerInput = new LinkedBlockingQueue<CrawlingRequest>();
			while(crawlerInputQueue.size()>0) {
				try {
					crawlerInput.put(crawlerInputQueue.take());
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			OutputStream out;
			try {
				out = new FileOutputStream(file);
				ObjectOutputStream obj_out = new ObjectOutputStream(out);
				obj_out.writeObject(crawlerInput);
				obj_out.close();
				log.info("CrawlerInputQueue saved to file");
			} catch (FileNotFoundException e) {
				log.warning("File/Directory was not found");
			} catch (IOException e) {
				log.warning("Failed to write to file");
			}
		}
	}
	
	/**Method to load the CrawlerOutputQueue
	 * 
	 * @return the loaded CrawlerOutputQueue or a new ObservedBlockingQueue if there was nothing saved
	 */
	public BlockingQueue<CrawlingRequest> loadCrawlerInputQueue() {
		BlockingQueue<CrawlingRequest> crawlerInputQueue = new LinkedBlockingQueue<CrawlingRequest>(Config.Keys_Defs.IntTypes.CrawlerSupervisor_CrawlingRequestQueueSize.getKey());;
		try {
			File file = new File(CRAWLERINPUTQUEUE_LOCATION);
			InputStream in = new FileInputStream(file);
			ObjectInputStream obj_in = new ObjectInputStream(in);
			crawlerInput = (BlockingQueue<CrawlingRequest>) obj_in.readObject();
			in.close();
			if(file.exists()) {
				file.delete();
			}
			log.info("CrawlerInputQueue loaded from file");
		} catch (Exception e) {
			crawlerInput = new LinkedBlockingQueue<CrawlingRequest>(Config.Keys_Defs.IntTypes.CrawlerSupervisor_CrawlingRequestQueueSize.getKey());
			log.info("CrawlerOutputQueue wasn't loaded from file");
		}
		putIt put = new putIt(crawlerInput, crawlerInputQueue);
		put.run();
		return crawlerInputQueue;
	}
	
	/** Method to save the CrawlerOutputQueue
	 * 
	 * @param crawlerOutputQueue the queue to save
	 */
	public void saveCrawlerOutputQueue(BlockingQueue<Website> crawlerOutputQueue) {
		if (crawlerOutputQueue.size()>0) {
			File file = new File(CRAWLEROUTPUTQUEUE_LOCATION);
			File folder = new File(Config.Keys_Defs.StringTypes.Queues_SaveLocation.getKey());
			folder.getAbsoluteFile().mkdirs();
			crawlerOutput = new LinkedBlockingQueue<Website>();
			while(crawlerOutputQueue.size()>0) {
				try {
					crawlerOutput.put(crawlerOutputQueue.take());
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			OutputStream out;
			try {
				out = new FileOutputStream(file);
				ObjectOutputStream obj_out = new ObjectOutputStream(out);
				obj_out.writeObject(crawlerOutput);
				obj_out.close();
				log.info("CrawlerOutputQueue saved to file");
			} catch (FileNotFoundException e) {
				log.warning("File/Directory was not found");
			} catch (IOException e) {
				log.warning("Failed to write to file");
			}
		}
	}
	
	/**Method to load the CrawlerOutputQueue
	 * 
	 * @return the loaded CrawlerOutputQueue or a new ObservedBlockingQueue if there was nothing saved
	 */
	public BlockingQueue<Website> loadCrawlerOutputQueue() {
		BlockingQueue<Website> crawlerOutputQueue = new LinkedBlockingQueue<Website>(Config.Keys_Defs.IntTypes.CrawlerOutputQueue_MaxSize.getKey());
		try {
			File file = new File(CRAWLEROUTPUTQUEUE_LOCATION);
			InputStream in = new FileInputStream(file);
			ObjectInputStream obj_in = new ObjectInputStream(in);
			crawlerOutput = (LinkedBlockingQueue<Website>) obj_in.readObject();
			in.close();
			if(file.exists()) {
				file.delete();
			}
			log.info("CrawlerOutputQueue loaded from file");
		} catch (Exception e) {
			crawlerOutput = new LinkedBlockingQueue<Website>(Config.Keys_Defs.IntTypes.CrawlerOutputQueue_MaxSize.getKey());
			log.info("CrawlerOutputQueue wasn't loaded from file");
		}
		putIt put = new putIt(crawlerOutput, crawlerOutputQueue);
		put.run();
		return crawlerOutputQueue;
	}
	
	/** Method to save the IngestionInputQueue
	 * 
	 * @param ingestionInputQueue the queue to save
	 */
	public void saveIngestionInputQueue(BlockingQueue<Website> ingestionInputQueue) {
		if (ingestionInputQueue.size()>0) {
			File file = new File(INGESTIONQUEUE_LOCATION);
			File folder = new File(Config.Keys_Defs.StringTypes.Queues_SaveLocation.getKey());
			folder.getAbsoluteFile().mkdirs();
			ingestionInput = new LinkedBlockingQueue<Website>();
			while(ingestionInputQueue.size()>0) {
				try {
					ingestionInput.put(ingestionInputQueue.take());
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			OutputStream out;
			try {
				out = new FileOutputStream(file);
				ObjectOutputStream obj_out = new ObjectOutputStream(out);
				obj_out.writeObject(ingestionInput);
				obj_out.close();
				log.info("IngestionInputQueue saved to file");
			} catch (FileNotFoundException e) {
				log.warning("File/Directory was not found");
			} catch (IOException e) {
				log.warning("Failed to write to file");
			}
		}
	}
	
	/**Method to load the IngestionInputQueue
	 * 
	 * @return the loaded IngestionInputQueue or a new PriorityBlockingQueue if there was nothing saved
	 */
	public BlockingQueue<Website> loadIngestionInputQueue() {
		BlockingQueue<Website> ingestionInputQueue = new LinkedBlockingQueue<Website>(Config.Keys_Defs.IntTypes.IngestionInputQueue_MaxSize.getKey());
		try {
			File file = new File(INGESTIONQUEUE_LOCATION);
			InputStream in = new FileInputStream(file);
			ObjectInputStream obj_in = new ObjectInputStream(in);
			ingestionInputQueue = (LinkedBlockingQueue<Website>) obj_in.readObject();
			in.close();
			if(file.exists()) {
				file.delete();
			}
			log.info("IngestionInputQueue loaded from file");
		} catch (Exception e) {
			ingestionInputQueue = new LinkedBlockingQueue<Website>(Config.Keys_Defs.IntTypes.IngestionInputQueue_MaxSize.getKey());
			log.info("IngestionInputQueue wasn't loaded from file");
		}
		putIt put = new putIt(ingestionInput, ingestionInputQueue);
		put.run();
		return ingestionInputQueue;
	}
	
	class putIt implements Runnable {

		private BlockingQueue localQueue;
		private BlockingQueue programQueue;
		
		public putIt(BlockingQueue localQueue, BlockingQueue programQueue) {
			this.localQueue = localQueue;
			this.programQueue = programQueue;
		}
		
		@Override
		public void run() {
			while(localQueue.size()>0) {
				try {
					programQueue.put(localQueue.take());
				} catch (InterruptedException e) {
					log.warning("Can't put object to queue");
				}
			}
		}
	}
}
