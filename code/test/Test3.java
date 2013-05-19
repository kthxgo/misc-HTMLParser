package test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.jdom2.Document;
import org.jdom2.output.XMLOutputter;

import crawler.HTMLParser3;

public class Test3 {

	public static void main(String[] args) throws IOException {
		HTMLParser3 parser = new HTMLParser3();
		
//		String text = "<html><head><title>Test-Seite</title></head><body>Test<div>Test-Div in a <div>another DIV<div>with a dvi</div></div></div>secnod<div><b>noch einer</b></div></body></html>";
		String text = "<html><head><title>Test-Seite</title></head><body>" +
				"<div>test</div>" +
				"<div>and another one<div>with <div><table>->inside</table></div></div></div>" +
				"<div>and one more<div>with inside</div></div></body></html>";
		
		Document domStructure = parser.parse(text);
		
		XMLOutputter outputter = new XMLOutputter();
		File file = new File("test.xml");
		FileOutputStream output = new FileOutputStream(file);
		outputter.output(domStructure, output);
		
		
	}
	
}
