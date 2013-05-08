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
		
		
		//Suche html-Tag und eliminiere alles vorherige
		first = html.indexOf("<html>");
		html = html.substring(first);
		last = html.indexOf("</html>");
		html = html.substring(0, last);
		Element rootElement = new Element("html");
		Attribute rootAttribute = new Attribute("html", "<html>");
		rootElement.setAttribute(rootAttribute);
		domStructure.setRootElement(rootElement);
		
		root = domStructure.getRootElement();
		
		//Suche head-Tag
		first = html.indexOf("<head>");
		last = html.indexOf("</head>");
		temp = html.substring(first, last);
		Element headElement = new Element("head");
		root.addContent(headElement);
		
		//Suche head-Tag
		first = html.indexOf("<title>");
		last = html.indexOf("</title>");
		temp = html.substring(first, last);
		Element titleElement = new Element("title");
		root.getChild("head", root.getNamespace()).addContent(titleElement);
		
		
		
		
		//Suche body-Tag
		first = html.indexOf("<body>");
		last = html.indexOf("</body>");
		temp = html.substring(first, last);
		Element bodyElement = new Element("body");
		root.addContent(bodyElement);
		body = root.getChild("body", root.getNamespace());
		current = body;
		//starte Rekursion
		body.addContent(parseNext(current, temp));
	}
	
	
	
	private Element parseNext(Element current, String temp) {
		int first, next;
		String newTemp;
		
		first = html.indexOf("<div>");
		next = html.indexOf("</div>");
		newTemp = html.substring(first, next);
		Element element = new Element("div");
//		Attribute attribute = new Attribute("div", newTemp);
		current.addContent(element);
//		current.setAttribute(attribute);
		current.addContent(parseNext(current, newTemp));
		
		
		current.addContent(element);
		
		return current;
	}
	
	
	
	
	private void test(Website website) {
		/*
		Document domStructure;
		domStructure = new Document();
		
		Element rootElement = new Element("root");
		Attribute rootAttribute = new Attribute("root", "<html>");
		rootElement.setAttribute(rootAttribute);
		domStructure.setRootElement(rootElement);
		
		root = domStructure.getRootElement();
		
		
		Element headElement = new Element("head");
		Attribute headAttribute = new Attribute("head", "<head>");
		headElement.setAttribute(headAttribute);
		
		root.addContent(headElement);
		
		
		Element bodyElement = new Element("body");
		Attribute bodyAttribute = new Attribute("body", "<body>");
		headElement.setAttribute(bodyAttribute);
		
		root.addContent(bodyElement);
		body = root.getChild("body", root.getNamespace());
		
		
		Element titleElement = new Element("title");
		Attribute titleAttribute = new Attribute("title", "<title>");
		titleElement.setAttribute(titleAttribute);
		
		
		
		
		Element tableElement = new Element("table");
		Attribute tableAttribute = new Attribute("table", "<table>");
		tableElement.setAttribute(tableAttribute);
		
		Element tdElement = new Element("td");
		Attribute tdAttribute = new Attribute("td", "<td>");
		tdElement.setAttribute(tdAttribute);
		
		
		root.addContent(headElement);
		root.addContent(bodyElement);
		
//		root.getChild("head", root.getNamespace()).addContent(titleElement);
		body.addContent(titleElement);
		
		root.getChild("body", root.getNamespace()).addContent(tableElement);
		
		
		website.setDomStructure(domStructure);
		*/
	}
	
}
