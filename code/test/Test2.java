package test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.jdom2.Document;
import org.jdom2.output.XMLOutputter;

import crawler.HTMLNormalizer;
import crawler.HTMLParser2;

public class Test2 {

	public static void main(String[] args) throws IOException {
		HTMLParser2 parser = new HTMLParser2();
		HTMLNormalizer norma = new HTMLNormalizer();
		
		String code = "< html >< head><title>test is naemlich > als des andere halt</title und so shit halt ></head>< / html nub>";
		
		System.out.println(norma.normalize(code));
		
		String text = "<html><head><title>Test-Seite</title></head><body>Test<div>Test-Div in a </div>secnod<div><b>noch einer</b></div></body></html>";
		
		Document domStructure = parser.parse(text);
		
		XMLOutputter outputter = new XMLOutputter();
		File file = new File("test.xml");
		FileOutputStream output = new FileOutputStream(file);
		outputter.output(domStructure, output);
	}
	
}
