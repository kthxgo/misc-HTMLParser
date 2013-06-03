package plugins;

import dataStructures.Website;

public class PluginFactory {

	public static enum Filters {
		LANGUAGE, DOMAIN, LENGTH;
	}
	
	public Plugin createPlugin(Filters type, Website website){
		switch (type) {
			case DOMAIN:
				return new DomainPlugin(website);
			case LANGUAGE:
				return new LanguagePlugin(website);
			case LENGTH:
				return new LengthPlugin(website);
			}
		return null;
		}
	
	
	
	
}
