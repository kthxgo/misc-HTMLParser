package crawler;

import org.jdom2.Attribute;
import org.jdom2.Document;
import org.jdom2.Element;

import dataStructures.Website;

public class HTMLParser2 {

	private		Document	domStructure;
	private		Element		rootElement;
	private		Element		bodyElement;
	private		Element		currentElement;
	
	private		String		htmlText;
	private		String		tempText;
	
	
	
	public Document parse(String htmlText) {
		domStructure = new Document();
		
		System.err.println("START PARSING ...");
		
		
		int first, last;
		String temp;
		
		
		//Suche html-Tag und eliminiere alles auﬂenrum
		first = htmlText.indexOf("<html>");
		last = htmlText.indexOf("</html>");

		htmlText = htmlText.substring(first, last);
		if(first>0 && last>0) {
			System.out.println("found <html> and </html>");
		}
		rootElement = new Element("html");
//		Attribute rootAttribute = new Attribute("html", "<html>");
//		rootElement.setAttribute(rootAttribute);
		domStructure.setRootElement(rootElement);
		
		rootElement = domStructure.getRootElement();
		
		//Suche head-Tag
		first = htmlText.indexOf("<head>");
		last = htmlText.indexOf("</head>");
		temp = htmlText.substring(first, last);
		if(first>0 && last>0) {
			System.out.println("found <head> and </head>");
		}
		Element headElement = new Element("head");
		rootElement.addContent(headElement);
		
		//Suche title-Tag
		first = temp.indexOf("<title>") + 7;
		last = temp.indexOf("</title>");
		temp = temp.substring(first, last);
		Element titleElement = new Element("title");
		Attribute titleAttribute = new Attribute("value", temp);
		titleElement.setAttribute(titleAttribute);
		rootElement.getChild("head", rootElement.getNamespace()).addContent(titleElement);
		
		
		
		
		//Suche body-Tag
		first = htmlText.indexOf("<body>") + 6;
		last = htmlText.indexOf("</body>");
		temp = htmlText.substring(first, last);
		if(first>0 && last>0) {
			System.out.println("found <body> and </body>");
		}
		bodyElement = new Element("body");
		rootElement.addContent(bodyElement);
		bodyElement = rootElement.getChild("body", rootElement.getNamespace());
//		current = body;
		//starte Rekursion
		parseNext(bodyElement, temp);
		
		
		
		System.err.println("PARSING FINISHED!");
		
		return domStructure;
		
	}

	private void parseNext(Element currentElement, String tempText) {
		int		firstLocation;
		int		nextLocation;
		int		helper1;
		int		helper2;
		
		String	newTempText;
		String	tempTextHelper;
		
		
		while(!tempText.equals("")) {
			//search for a tag
			tempTextHelper = tempText;
			helper1 = tempTextHelper.indexOf("<");
			helper2 = tempTextHelper.indexOf(">");
			
			
			
			
		}
		
		
		
	}
	
	private String optimizeTag(String tag) {
		int		help;
		String	temp;
		
		int tagStartPosition = 1;
		while(tag.charAt(tagStartPosition)==' ' || tag.charAt(tagStartPosition)=='/') {
			tagStartPosition++;
		}
		temp = tag.substring(tagStartPosition);
		temp = temp.substring(0, temp.indexOf(" "));
		if(tag.substring(0, tagStartPosition).indexOf("/")>=0) {
			temp = "</" + temp + ">";
		} else {
			temp = "<" + temp + ">";
		}
		
		
		return temp;
	}
	
	
	
	
	
	
	
	
	
	
	
	
}
