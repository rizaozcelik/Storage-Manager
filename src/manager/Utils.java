package manager;

public class Utils {
	
	public static String padWithHashtag(String word, int upLimit) {
		String str = word + "";
		while (str.length() < upLimit) {
			str = str + "#";
		}
		return str;
	}
}
