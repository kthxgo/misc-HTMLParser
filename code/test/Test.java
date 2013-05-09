package test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import org.jdom2.output.XMLOutputter;

import crawler.HTMLParser;

import dataStructures.Website;

public class Test {

	public static void main(String[] args) throws IOException {
		HTMLParser parser = new HTMLParser();
		
		Website website = new Website();
		
//		</br><div>Test-Div</div><div>noch einer</div>
		String text = "<html><head><title>Test-Seite</title></head><body>Test<table>T4b3ll3</table><div>Test-Div</div>secnod<div>noch einer</div></body></html>";
		website.setSiteText(text);
		
		parser.parse(website);
		
		XMLOutputter outputter = new XMLOutputter();
		File file = new File("test.xml");
		FileOutputStream output = new FileOutputStream(file);
		outputter.output(website.getDomStructure(), output);
	}
	
}
