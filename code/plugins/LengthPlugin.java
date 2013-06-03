package plugins;

import dataStructures.Website;
import common.Config;

public class LengthPlugin extends Plugin {

	int minLenght = common.Config.Keys_Defs.IntTypes.Plugin_Length.getKey();
	// Document dataStructures.Website.getDomStructure();

	String WebsiteText = "Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet. Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet.";
	int SiteLength = WebsiteText.length();

	private Website website;

	public LengthPlugin(Website website) {
		this.website = website;

	}

	private int lenghtCheck() {
		int count;

		for (int i = 0; i < SiteLength; i++) {
			
		}
		return 0;
	}

	@Override
	public Boolean call() {
		// TODO Auto-generated method stub
		return false;
	}

}
