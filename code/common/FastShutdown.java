package common;

/** An Interface the main Parts of the Program should implement
 * 
 * @author S. Berndt
 *
 */
public interface FastShutdown {
	
	
	/** When called a Thread is created. Within its run() Methode shutdown related Stuff should be handled.<br>
	 * The Thread must be started befor returning it!
	 * 
	 * @return an started Thread, handling shutdown related stuff
	 * @see 
	 */
	public Thread shutdown();

}
