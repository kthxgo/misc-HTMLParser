package plugins;

import java.util.concurrent.Callable;

import dataStructures.Website;


public abstract class Plugin implements Callable<Boolean> {

		
	/** Use and Override this method to check a website
	 * 
	 * @return true if the website matches your assignments, false if not
	 */
	public abstract Boolean call();
	
	
}
