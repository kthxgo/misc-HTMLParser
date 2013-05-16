package crawler;

public class HTMLNormalizer {

	
	public String normalize(String text) {
		String newText = "";
		String temp;
		int posOpener;
		int posNextOpener;
		int posCloser;
		
		while(text.indexOf("<")>=0) {
			posOpener = text.indexOf("<");
			posNextOpener = text.indexOf("<", posOpener+1);
			posCloser = text.indexOf(">");
			
			if (posOpener > posCloser) {
				newText += text.substring(0, posOpener);
				text = text.substring(posOpener);
			} else if(posNextOpener>posCloser || posNextOpener == -1) {
				if(posOpener != 0) {
					newText += text.substring(0, posOpener);
				}
				temp = text.substring(posOpener, posCloser + 1);
				temp = optimizeTag(temp);
				newText += temp;
				text = text.substring(posCloser+1);
			} else {
				newText += text.substring(0, posNextOpener);
				text = text.substring(posNextOpener);
			}
		}
		
		
		return newText;
	}
	
	
	
	private String optimizeTag(String tag) {
		String	temp;
		int tagStartPosition = 1;
		while(tag.charAt(tagStartPosition)==' ' || tag.charAt(tagStartPosition)=='/') {
			tagStartPosition++;
		}
		temp = tag.substring(tagStartPosition);
		if(temp.indexOf(" ")>=0) {
			temp = temp.substring(0, temp.indexOf(" "));
		} else {
			temp = temp.substring(0, temp.indexOf(">"));
		}
		
		if(tag.substring(0, tagStartPosition).indexOf("/")>=0) {
			temp = "</" + temp + ">";
		} else {
			temp = "<" + temp + ">";
		}
		
		return temp;
	}
	
}
