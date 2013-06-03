package plugins;

import dataStructures.Website;

public class LanguagePlugin extends Plugin {

	private Website website;
	
	String Language = common.Config.Keys_Defs.StringTypes.Plugin_Language.getKey();

	public LanguagePlugin(Website website) {
		this.website = website;
	}
	public static enum Languages {
		DE, ENG, FR, SP, IT;
	}
	
	public boolean langCeck(Languages lang, Website website){
		switch(lang){
		case DE:
			 new LangDE(website);
		case ENG:
			 new LangENG(website);
		case FR:
			 new LangFR(website);
		case SP:
			 new LangSP(website);
		case IT:
			 new LangIT(website);
		}
		return false;
	}
	
	@Override
	public Boolean call() {
		// TODO Auto-generated method stub
		return null;
	}

}
