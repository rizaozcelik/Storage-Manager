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
