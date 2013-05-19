package crawler;

import java.util.List;
import java.util.ArrayList;

import org.jdom2.Attribute;
import org.jdom2.Document;
import org.jdom2.Element;

public class HTMLParser3 {

	private List<String> tags = new ArrayList<String>();
	private int position;
	
	private		Document	domStructure;
	private		Element		rootElement;
	private		Element		bodyElement;
	
	private		String		htmlText;
	
	
	public Document parse(String htmlText) {
		this.htmlText = htmlText;
		domStructure = new Document();
		filterTags();
		
		System.err.println("START PARSING ...");
		
		
		int first, last;
		String temp;
		
		
		//search for html-tag and eliminate everything around it
		first = htmlText.indexOf("<html>");
		last = htmlText.indexOf("</html>");

		htmlText = htmlText.substring(first, last);
		if(first>=0 && last>0) {
			System.out.println("found <html> and </html>");
		}
		rootElement = new Element("html");
		domStructure.setRootElement(rootElement);
		
		rootElement = domStructure.getRootElement();
		
		//search for head-tag
		first = htmlText.indexOf("<head>");
		last = htmlText.indexOf("</head>");
		temp = htmlText.substring(first, last);
		if(first>0 && last>0) {
			System.out.println("found <head> and </head>");
		}
		Element headElement = new Element("head");
		rootElement.addContent(headElement);
		
		//search for title-tag
		first = temp.indexOf("<title>") + 7;
		last = temp.indexOf("</title>");
		temp = temp.substring(first, last);
		Element titleElement = new Element("title");
		Attribute titleAttribute = new Attribute("value", temp);
		titleElement.setAttribute(titleAttribute);
		rootElement.getChild("head", rootElement.getNamespace()).addContent(titleElement);
		
		
		
		
		//search for body-tag
		first = htmlText.indexOf("<body>") + 6;
		last = htmlText.indexOf("</body>");
		temp = htmlText.substring(first, last);
		if(first>0 && last>0) {
			System.out.println("found <body> and </body>");
		}
		bodyElement = new Element("body");
		rootElement.addContent(bodyElement);
		bodyElement = rootElement.getChild("body", rootElement.getNamespace());
		//start recursion
		System.out.println("start recursive parsing ...");
		parseNext(bodyElement, temp);
		
		
		
		System.err.println("PARSING FINISHED!");
		
		return domStructure;
		
	}
	
	
	//returns the position in the whole html-Text to continue parsing on same level
	private int parseNext(Element currentElement, String tempText) {
		if(position<tags.size()) {
			String tag = tags.get(position);
			
			
			switch(tag) {
			case "<div>":
				return parseFor(currentElement, tag, "div", tempText);
				
			case "<table>":
				return parseFor(currentElement, tag, "table", tempText);
				
			//continue with other tags
				
			}
		}
		
		return -1;
	}
	
	
	//tagName.length()+2 to skip an opening tag
	//tagName.length()+3 to skip an closing tag
	private int parseFor(Element currentElement, String tag, String tagName, String tempText) {
		int		openerLocation;
		int		closerLocation;
		
		String	newTempText;
		
		openerLocation = tempText.indexOf("<" + tagName + ">");
		if(openerLocation>0) {
			String front = tempText.substring(0, openerLocation);
			Element element = new Element("text");
			Attribute att = new Attribute("value", front);
			element.setAttribute(att);
			currentElement.addContent(element);
		}
		
		Element divElement = new Element(tagName);
		currentElement.addContent(divElement);
		
		position++;
		
		tag = tags.get(position);
		if(tag.equals("</" + tagName + ">")) { 
			closerLocation = tempText.indexOf("</" + tagName + ">");
			Element textElement = new Element("text");
			Attribute textAttribute = new Attribute("value", tempText.substring(openerLocation + tagName.length()+2, closerLocation));
			textElement.setAttribute(textAttribute);
			divElement.addContent(textElement);
			
			position++;
			
			newTempText = tempText.substring(closerLocation + tagName.length()+3); 
			parseNext(currentElement, newTempText);

			return htmlText.length()-newTempText.length()-8; //-8 because of surrounding html-tag
		} else {
			newTempText = tempText.substring(openerLocation + tagName.length()+2);
			int pos = parseNext(divElement, newTempText);
			
			position++;
			
			parseNext(currentElement, htmlText.substring(pos));
			
			return pos + tagName.length()+3;
		}
	}
	
	
		
	private void filterTags() {
		int		posOpener;
		int		currentPosition = -1;
		
		while (htmlText.indexOf("<", currentPosition + 1)>=0) {
			//search for a tag
			//+1 -> not the same tag again
			posOpener = htmlText.indexOf("<", currentPosition + 1);
			if (htmlText.indexOf("<div>", posOpener) == posOpener) {
				tags.add("<div>");
			} else if (htmlText.indexOf("</div>", posOpener) == posOpener) {
				tags.add("</div>");
			} else if (htmlText.indexOf("<table>", posOpener) == posOpener) {
				tags.add("<table>");
			} else if (htmlText.indexOf("</table>", posOpener) == posOpener) {
				tags.add("</table>");
			}
			//continue with other tags
			
			currentPosition = posOpener;
		}
	}
	
}
