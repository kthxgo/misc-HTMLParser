package test;

import crawler.HTMLNormalizer;
import crawler.HTMLParser2;

public class Test2 {

	public static void main(String[] args) {
		HTMLParser2 parser = new HTMLParser2();
		HTMLNormalizer norma = new HTMLNormalizer();
		
		String code = "< html >< head><title>test is naemlich > als des andere halt</title und so shit halt ></head>< / html nub>";
		
		System.out.println(norma.normalize(code));
	}
	
}
