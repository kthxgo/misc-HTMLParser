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
		tempTextHelper = htmlText;
		String firstTag = getNextTag();
		parseNext(bodyElement, temp, firstTag);
		
		
		
		System.err.println("PARSING FINISHED!");
		
		return domStructure;
		
	}
	
	
	

	private Element parseNext(Element currentElement, String tempText, String tag) {
		int		firstLocation;
		int		nextLocation;
		int		posOpener;
		
		String	newTempText;
		String	tempTextHelper;
		
		int tagPosition = currentPosition;
		String nextTag = getNextTag();
		
		switch(tag) {
		case "<div>": 
			firstLocation = tempText.indexOf("<div>");
			if(firstLocation>0) {
				String front = tempText.substring(0, firstLocation);
				Element element = new Element("text");
				Attribute att = new Attribute("value", front);
				element.setAttribute(att);
				currentElement.addContent(element);
			}
			
//			Element divElement = new Element("div");
//			
//			if(!nextTag.equals("</div>")) {
//				Element newCurrent = divElement;
//			}
			
			
			newTempText = tempText.substring(firstLocation + 5, tempText.indexOf("</div>"));
			Element newCurrent = new Element("div");
			currentElement.addContent(parseNext(newCurrent, newTempText, nextTag));
			
			tempText = tempText.substring(tempText.indexOf("</div>") + 6);
			
			break;
		case "table": break;
		case "": break;
		
		}
		return currentElement;
	}
	
	
	private String tempTextHelper;
	private int currentPosition = -1;
	
	private String getNextTag() {
		int		posOpener;
		String	result = "";
		
		
		//search for a tag
		//+1 -> not the same tag again
		posOpener = tempTextHelper.indexOf("<", currentPosition+1);
		
		if(tempTextHelper.indexOf("<div>", posOpener)==posOpener) {
			result = "<div>";
		} else if(tempTextHelper.indexOf("</div>", posOpener)==posOpener) {
			result = "</div>";
		} else if(tempTextHelper.indexOf("<table>", posOpener)==posOpener) {
			result = "<table>";
		} else if(tempTextHelper.indexOf("</table>", posOpener)==posOpener) {
			result = "</table>";
		} 

		currentPosition = posOpener;
		System.out.println(result);
		return result;
	}
	
}
