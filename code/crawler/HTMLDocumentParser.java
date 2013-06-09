package crawler;

import java.util.Enumeration;

import javax.swing.text.AttributeSet;
import javax.swing.text.html.HTML;
import javax.swing.text.html.HTMLDocument;

import org.jdom2.Attribute;
import org.jdom2.Document;
import org.jdom2.Element;

public class HTMLDocumentParser {
	
	public static void main(String[] args) {
		
	}

	public Document parse(HTMLDocument html) {
		Document domStructure = new Document();
		
		javax.swing.text.Element root = html.getDefaultRootElement();
		
		Element rootElement = new Element(root.getName());
		domStructure.setRootElement(rootElement);
		
		rootElement = domStructure.getRootElement();
		
		//start recursive parsing
		parseNext(root, rootElement);
		
		
		
		return domStructure;
	}
	
	private void parseNext(javax.swing.text.Element element, Element jdomElement) {
		int elements = element.getElementCount();
		for(int i = 0; i<elements; i++) {
			Element elem = new Element(element.getElement(i).getName());
			
			
			
			AttributeSet attributes = element.getElement(i).getAttributes();
			int length = attributes.getAttributeCount();
//			while(attributeNames.hasMoreElements()) {
//				
//				Attribute att = new Attribute(attributeName.toString());
//			}
			
			
			for(int j = 0; j<length; j++) {
				attributes.getAttribute(HTML.Attribute.NAME);
			}
			
		}
		
		
		
		
		
		
	}
	
}
