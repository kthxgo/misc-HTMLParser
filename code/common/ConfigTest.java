package common;

import static org.junit.Assert.*;
import org.junit.Test;


/**
 * 
 * @author S. Berndt
 *
 */

public class ConfigTest {

	@Test
	public void values(){
		assertTrue(Config.Keys_Defs.IntTypes.JUnit_TestValue.getKey() == 42);		
	}
	
	@Test
	public void rewrite(){
		int oldKey = Config.Keys_Defs.IntTypes.JUnit_TestValue.getKey();
		int newKey = oldKey * 2;
		Config.Keys_Defs.IntTypes.JUnit_TestValue.setKey(newKey);
		assertTrue(Config.Keys_Defs.IntTypes.JUnit_TestValue.getKey() == 2*oldKey);	
		System.out.println(Config.Keys_Defs.IntTypes.JUnit_TestValue.getKey());
	}
	
	
	
	

}
