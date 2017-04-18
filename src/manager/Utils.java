package manager;

import java.io.IOException;
import java.io.RandomAccessFile;

public class Utils {
	
	public static String padWithHashtag(String word, int upLimit) {
		String str = word + "";
		while (str.length() < upLimit) {
			str = str + "#";
		}
		return str;
	}
	
	public static RandomAccessFile getStreamOfDataType(String typeName) throws IOException {
		String dataFileName = "./data/dataFiles/" + typeName.toLowerCase() + ".txt";
		RandomAccessFile raf = new RandomAccessFile(dataFileName, "rw"); // open
																			// data
																			// file
		return raf;
	}
}
