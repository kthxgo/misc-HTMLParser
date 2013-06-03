package crawler;

public class HTMLNormalizer {

	
	public String normalize(String text) {
		String newText = "";
		String temp;
		int posOpener;
		int posNextOpener;
		int posCloser;
		int imageCounter = 0;
		
		//normal
		text = text.replace("&uuml;", "ue");
		text = text.replace("&Uuml;", "Ue");
		text = text.replace("&auml;", "ae");
		text = text.replace("&Auml;", "Ae");
		text = text.replace("&ouml;", "oe");
		text = text.replace("&Ouml;", "Oe");
		text = text.replace("&szlig;", "ss");
		
		//advanced
		text = text.replace("ü", "ue");
		text = text.replace("ä", "ae");
		text = text.replace("ö", "oe");
		text = text.replace("ß", "ss");
		
		
		
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
				
				if(temp.equals("<meta>") || temp.equals("<link>") || temp.equals("<a>") || temp.equals("</a>") || temp.equals("<p>") || temp.equals("</p>")
						|| temp.equals("<span>") || temp.equals("</span>") || temp.equals("<form>") || temp.equals("</form>") || temp.equals("<input>")
						|| temp.equals("<b>") || temp.equals("</b>") || temp.equals("<u>") || temp.equals("</u>") || temp.equals("<i>") || temp.equals("</i>")
						|| temp.equals("<iframe>") || temp.equals("</iframe>")) {
					
				} else if (temp.equals("<img>")) {
					newText += "[img_" + imageCounter + "]";
					imageCounter++;
				} else if (temp.equals("<br>") || temp.equals("</br>")) {
					
				} else if (temp.equals("<script>")) {
					posCloser = text.indexOf("</script>")+8;
				}
				
				
				else {
					newText += temp;
				}
				
				
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
