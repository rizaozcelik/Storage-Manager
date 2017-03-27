package manager;

public class SystemCatalogueEntry {
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

	private static String padWithHashtag(String word) {
		while (word.length() < 16) {
			word = word + "#";
		}
		return word;
	}

	@Override
	public String toString() {
		String res = padWithHashtag(typeName) + "," + padWithHashtag(dataFileName) + "," + numberOfFields + ","
				+ isValid;
		for (int i = 0; i < fields.length; i++) {
			res = res + "," + padWithHashtag(fields[i]);
		}
		for(int i = fields.length; i < 9; i++){
			res = res + ","+"################";
		}
		res = res + "\n";
		return res;

	}
}
