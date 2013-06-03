package crawler;

import java.util.Random;
import java.util.concurrent.ThreadFactory;

/** Crawler Factory especially for Crawler Threads used by the CrawlerSupervisor<br>
 * creates Thread-Objects with a very personal Name and an lower priority than normal
 * @author S. Berndt
 */

public class CrawlerThreadFactory implements ThreadFactory {
	
	
	// Only for personal amusement while debugging
	private String[] threadNames = new String[] {"Horst", "Rupert", "Stanley", "Richard", "Hinz", "Kunz", "Wolfgang"};
	private String getRandomName(){
		return threadNames[new Random().nextInt(threadNames.length)];
	}
	
	public Thread newThread(Runnable runnable) {
		Thread thread = new Thread(runnable);
		thread.setName(getRandomName());
		thread.setPriority(Thread.NORM_PRIORITY -2);
		return thread;
	}

}
