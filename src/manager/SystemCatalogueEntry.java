package manager;

import java.io.IOException;
import java.io.RandomAccessFile;

public class SystemCatalogueEntry {
	
	public static final String SYSTEM_CATALOGUE_PATH = "./data/system_catalogue.txt";
	public static final int SYS_CAT_ENTRY_SIZE = 191;
	
	String typeName, dataFileName;
	int numberOfFields;
	String[] fields;
	byte isValid;

	public SystemCatalogueEntry(String typeName, String dataFileName, int numberOfFields, String[] fields) {
		this.typeName = typeName;
		this.dataFileName = dataFileName;
		this.numberOfFields = numberOfFields;
		this.fields = fields;
		this.isValid = 1;
	}

	public static int getNumberOfFieldsOfDataType(String typeName) throws IOException {
		RandomAccessFile raf = new RandomAccessFile(SYSTEM_CATALOGUE_PATH, "r");
		long startingByteOfType = findStartingByteOfDataType(raf, typeName);
		raf.seek(startingByteOfType + 34); // Read number of fields in data type
		int numberOfFields = raf.read() - '0'; // convert it to a int
		raf.close(); // Close sys_cat
		return numberOfFields;
	}

	public static long findStartingByteOfDataType(RandomAccessFile raf, String typeName) throws IOException {
		long size = (long) raf.length();
		long byteIndex = 0;
		while (byteIndex < size) {
			raf.seek(byteIndex);
			String name = "";
			char c = 'a';
			while (c != '#') {
				c = (char) raf.read();
				name = name + c;
			}
			name = name.substring(0, name.length() - 1);
			if (name.equalsIgnoreCase(typeName)) {
				return byteIndex;
			}
			byteIndex += SYS_CAT_ENTRY_SIZE;
		}
		return -1;
	}
	
	@Override
	public String toString() {
		String res = Utils.padWithHashtag(typeName,16) + "," + Utils.padWithHashtag(dataFileName,16) + "," + numberOfFields + ","
				+ isValid;
		for (int i = 0; i < fields.length; i++) {
			res = res + "," + Utils.padWithHashtag(fields[i],16);
		}
		for(int i = fields.length; i < 9; i++){
			res = res + ","+"################";
		}
		res = res + "\n";
		return res;

	}
}
