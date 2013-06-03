package plugins;

import java.net.URL;

import dataStructures.Website;

public class DomainPlugin extends Plugin {

	String Plugin_Domain = common.Config.Keys_Defs.StringTypes.Plugin_Domain
			.getKey();
	private Website website;
	private String[] Domain = Plugin_Domain.split("; ");
	URL url = website.getUrl();

	public DomainPlugin(Website website) {
		this.website = website;
	}

	public boolean domainCheck(String[] Domain, URL url) {
		for (String i : Domain) {
			if (i.equals("DE")) {
				DomainDE de = new DomainDE(url);
				return de.domainCheck();
			}
		}

		return false;

	}

	@Override
	public Boolean call() {
		// TODO Auto-generated method stub
		return null;
	}

}
