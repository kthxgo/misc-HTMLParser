package crawler;

import org.jdom2.Attribute;
import org.jdom2.Document;
import org.jdom2.Element;

import dataStructures.Website;

public class HTMLParser {

	private Element root;
	private Element body;
	private Element current;
	private String html;
	private Document domStructure;
	
	public HTMLParser() {
		domStructure = new Document();
	}
	
	public void parse(Website website) {
		html = website.getSiteText();
		int first, last;
		String temp;
		
		
		//Suche html-Tag und eliminiere alles auﬂenrum
		first = html.indexOf("<html>");
		last = html.indexOf("</html>");

		html = html.substring(first, last);
		if(first>0 && last>0) {
			System.out.println("found <html> and </html>");
		}
		Element rootElement = new Element("html");
//		Attribute rootAttribute = new Attribute("html", "<html>");
//		rootElement.setAttribute(rootAttribute);
		domStructure.setRootElement(rootElement);
		
		root = domStructure.getRootElement();
		
		//Suche head-Tag
		first = html.indexOf("<head>");
		last = html.indexOf("</head>");
		temp = html.substring(first, last);
		if(first>0 && last>0) {
			System.out.println("found <head> and </head>");
		}
		Element headElement = new Element("head");
		root.addContent(headElement);
		
		//Suche title-Tag
		first = temp.indexOf("<title>") + 7;
		last = temp.indexOf("</title>");
		temp = temp.substring(first, last);
		Element titleElement = new Element("title");
		Attribute titleAttribute = new Attribute("value", temp);
		titleElement.setAttribute(titleAttribute);
		root.getChild("head", root.getNamespace()).addContent(titleElement);
		
		
		
		
		//Suche body-Tag
		first = html.indexOf("<body>") + 6;
		last = html.indexOf("</body>");
		temp = html.substring(first, last);
		Element bodyElement = new Element("body");
		root.addContent(bodyElement);
		body = root.getChild("body", root.getNamespace());
//		current = body;
		//starte Rekursion
		parseNext(body, temp);
		
		website.setDomStructure(domStructure);
		
	}
	
	
	private String temp;
	
	private Element parseNext(Element current, String temp) {
		int first, next;
		String newTemp, helper = temp;
		
		
		
		
		
		while (temp.length()>0) {
			
			int help1, help2;
			help1 = temp.indexOf("<");
			help2 = temp.indexOf(">");
			if(help1>=0 && help2>=0) {
				helper = temp.substring(temp.indexOf("<"), temp.indexOf(">") + 1);
			}
			
			
			if (helper.indexOf("<div") == 0) {
				System.out.println("found <div>");
				String arg = "div";
				first = temp.indexOf("<" + arg);
				next = temp.indexOf("</" + arg);

				String front = temp.substring(0, first);
				if(!front.isEmpty()) {
					Element element = new Element("text");
				Attribute att = new Attribute("value", front);
				element.setAttribute(att);
				current.addContent(element);
				}
				

				newTemp = temp.substring(first + 5, next);
				Element newCurrent = new Element(arg);
				current.addContent(parseNext(newCurrent, newTemp));

				temp = temp.substring(next + 6);
			} else if (helper.indexOf("<table")==0) {
				System.out.println("found <table>");
				String arg = "table";
				first = temp.indexOf("<" + arg);
				next = temp.indexOf("</" + arg);

				Element element = new Element("text");
				Attribute att = new Attribute("value", temp.substring(0, first));
				element.setAttribute(att);
				current.addContent(element);

				newTemp = temp.substring(first + 7, next);
				Element newCurrent = new Element(arg);
				current.addContent(parseNext(newCurrent, newTemp));

				temp = temp.substring(next + 8);
			} else {
				Element element = new Element("text");
				Attribute att = new Attribute("value", temp);
				element.setAttribute(att);
				current.addContent(element);
				
				temp = "";
				
			}
		}
		return current;
	}
	
	
	public void test(Website website) {
		
		Document domStructure;
		domStructure = new Document();
		
		Element rootElement = new Element("html");
//		Attribute rootAttribute = new Attribute("html", "<html>");
//		rootElement.setAttribute(rootAttribute);
		domStructure.setRootElement(rootElement);
		
		root = domStructure.getRootElement();
		
		
		Element headElement = new Element("head");
//		Attribute headAttribute = new Attribute("head", "<head>");
//		headElement.setAttribute(headAttribute);
		
		root.addContent(headElement);
		
		
		Element bodyElement = new Element("body");
//		Attribute bodyAttribute = new Attribute("body", "<body>");
//		headElement.setAttribute(bodyAttribute);
		
		root.addContent(bodyElement);
		body = root.getChild("body", root.getNamespace());
		
		
		Element titleElement = new Element("title");
//		Attribute titleAttribute = new Attribute("title", "<title>");
//		titleElement.setAttribute(titleAttribute);
		
		
		
		
		Element tableElement = new Element("table");
//		Attribute tableAttribute = new Attribute("table", "<table>");
//		tableElement.setAttribute(tableAttribute);
		
		Element tdElement = new Element("td");
		Attribute tdAttribute = new Attribute("td", "Das ist ein Tabellenfeld");
		tdElement.setAttribute(tdAttribute);
		
		Element tdElement2 = new Element("td");
		Attribute tdAttribute2 = new Attribute("td", "Das ist noch ein Tabellenfeld");
		tdElement2.setAttribute(tdAttribute2);
		
		
//		root.addContent(headElement);
//		root.addContent(bodyElement);
		
//		root.getChild("head", root.getNamespace()).addContent(titleElement);
		root.getChild("head", root.getNamespace()).addContent(titleElement);
		
		body.addContent(tableElement);
		
		body.getChild("table", body.getNamespace()).addContent(tdElement);
		body.getChild("table", body.getNamespace()).addContent(tdElement);
		
		website.setDomStructure(domStructure);
		
	}
	
}
