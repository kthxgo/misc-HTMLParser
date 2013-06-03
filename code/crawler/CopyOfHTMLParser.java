package crawler;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;

import org.jdom2.Attribute;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.output.XMLOutputter;

public class CopyOfHTMLParser {

	private List<String> tags = new ArrayList<String>();
	private int position;
	
	private		Document	domStructure;
	private		Element		rootElement;
	private		Element		bodyElement;
	
	private		String		htmlText;
	
	
	public Document parse(String text) {
		
		HTMLNormalizer norma = new HTMLNormalizer();
		htmlText = norma.normalize(text);
		
		System.out.println(htmlText);
		
		domStructure = new Document();
		filterTags();
		
		System.err.println("START PARSING ...");
		
		
		int first, last;
		String temp;
		
		
		//search for html-tag and eliminate everything around it
		first = htmlText.indexOf("<html>");
		last = htmlText.indexOf("</html>");

		
		if(first>=0 && last>0) {
			htmlText = htmlText.substring(first, last);
			System.out.println("found <html> and </html>");
		}
		rootElement = new Element("html");
		domStructure.setRootElement(rootElement);
		
		rootElement = domStructure.getRootElement();
		
		//search for head-tag
		first = htmlText.indexOf("<head>");
		last = htmlText.indexOf("</head>");
		
		if(first>0 && last>0) {
			temp = htmlText.substring(first, last);
			System.out.println("found <head> and </head>");
			Element headElement = new Element("head");
			rootElement.addContent(headElement);
			
			//search for title-tag
			first = temp.indexOf("<title>") + 7;
			last = temp.indexOf("</title>");
			if(first>=0 && last>0) {
				System.out.println("found <title> and </title>");
				temp = temp.substring(first, last);
				Element titleElement = new Element("title");
				Attribute titleAttribute = new Attribute("value", temp);
				titleElement.setAttribute(titleAttribute);
				rootElement.getChild("head", rootElement.getNamespace()).addContent(titleElement);
			}
			
		}
		
		
		
		
		
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
		
		XMLOutputter outputter = new XMLOutputter();
		File file = new File("test.xml");
		FileOutputStream output;
		try {
			output = new FileOutputStream(file);
			outputter.output(domStructure, output);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		return domStructure;
		
	}
	
	
	//returns the position in the whole html-Text to continue parsing on same level
	private int parseNext(Element currentElement, String tempText) {
		if(position<tags.size()) {
			String tag = tags.get(position);
			
			
			switch(tag) {
			case "<div>":
				return parseFor(currentElement, tag, "div", tempText);
				
			case "</div>":
				position++;
				break;
//				int closerLocation = tempText.indexOf("</div>");
//				position++;
//				parseNext(currentElement, tempText.substring(closerLocation));
//				break;
				
			case "<table>":
				return parseFor(currentElement, tag, "table", tempText);
				
			case "<h1>":
				return parseFor(currentElement, tag, "h1", tempText);	
				
			case "<h2>":
				return parseFor(currentElement, tag, "h2", tempText);
				
			case "<h3>":
				return parseFor(currentElement, tag, "h3", tempText);
				
			case "<h4>":
				return parseFor(currentElement, tag, "h4", tempText);	
				
			case "<h5>":
				return parseFor(currentElement, tag, "h5", tempText);	
				
			case "<h6>":
				return parseFor(currentElement, tag, "h6", tempText);	

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
			String front = "";
			int closer = tempText.lastIndexOf("</" + tagName + ">", openerLocation);
			if(closer<openerLocation && closer != -1) { 			//when there is a closing div in front of the actual opening one
				front = tempText.substring(closer+tagName.length()+3, openerLocation);
			} else {
				front = tempText.substring(0, openerLocation);
			}
			
				//TODO noch leere wegmachen
				Element element = new Element("text");
				Attribute att = new Attribute("value", front);
				element.setAttribute(att);
				currentElement.addContent(element);
			
		}
		
		Element newElement = new Element(tagName);
		currentElement.addContent(newElement);
		
		position++;
		
		tag = tags.get(position);
		if(tag.equals("</" + tagName + ">")) { 
			closerLocation = tempText.indexOf("</" + tagName + ">", openerLocation);
			
			String text = tempText.substring(openerLocation + tagName.length()+2, closerLocation);
			
				//TODO noch leere wegmachen
//				System.out.println("make text with: " + text);
				Element textElement = new Element("text");
				Attribute textAttribute = new Attribute("value", text);
				textElement.setAttribute(textAttribute);
				newElement.addContent(textElement);
			
			
			
			position++;
			
			newTempText = tempText.substring(closerLocation + tagName.length()+3);
			int pos = parseNext(currentElement, newTempText);
//			return htmlText.length()-newTempText.length()-8+; //-8 because of surrounding html-tag
			return pos + tagName.length()+3;
		} else {
			newTempText = tempText.substring(openerLocation + tagName.length()+2);
			int pos = parseNext(newElement, newTempText);
			
			position++;
			
			// pos edited
			newTempText = htmlText.substring(pos);
			pos = parseNext(currentElement, newTempText);
			
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
			if (htmlText.indexOf("<meta>", posOpener) == posOpener) {
				
			} else if (htmlText.indexOf("<link>", posOpener) == posOpener) {
				
			} else if (htmlText.indexOf("<script>", posOpener) == posOpener) {
				
			}
			
			else if (htmlText.indexOf("<div>", posOpener) == posOpener) {
				tags.add("<div>");
			} else if (htmlText.indexOf("</div>", posOpener) == posOpener) {
				tags.add("</div>");
			} 
			
			else if (htmlText.indexOf("<h1>", posOpener) == posOpener) {
				tags.add("<h1>");
			} else if (htmlText.indexOf("</h1>", posOpener) == posOpener) {
				tags.add("</h1>");
			} else if (htmlText.indexOf("<h2>", posOpener) == posOpener) {
				tags.add("<h2>");
			} else if (htmlText.indexOf("</h2>", posOpener) == posOpener) {
				tags.add("</h2>");
			} else if (htmlText.indexOf("<h3>", posOpener) == posOpener) {
				tags.add("<h3>");
			} else if (htmlText.indexOf("</h3>", posOpener) == posOpener) {
				tags.add("</h3>");
			} else if (htmlText.indexOf("<h4>", posOpener) == posOpener) {
				tags.add("<h4>");
			} else if (htmlText.indexOf("</h4>", posOpener) == posOpener) {
				tags.add("</h4>");
			} else if (htmlText.indexOf("<h5>", posOpener) == posOpener) {
				tags.add("<h5>");
			} else if (htmlText.indexOf("</h5>", posOpener) == posOpener) {
				tags.add("</h5>");
			} else if (htmlText.indexOf("<h6>", posOpener) == posOpener) {
				tags.add("<h6>");
			} else if (htmlText.indexOf("</h6>", posOpener) == posOpener) {
				tags.add("</h6>");
			}
				
				
			else if (htmlText.indexOf("<table>", posOpener) == posOpener) {
				tags.add("<table>");
			} else if (htmlText.indexOf("</table>", posOpener) == posOpener) {
				tags.add("</table>");
			} 
			//continue with other tags
			
			currentPosition = posOpener;
		}
	}
	
}
