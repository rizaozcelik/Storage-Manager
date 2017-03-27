package manager;

public class Page {
	int ID;
	int numberOfRecords;
	byte isLastPage;
	byte hasSpace;

	public Page(int id) {
		ID = id;
		this.numberOfRecords = 0;
		this.isLastPage = 1;
		this.hasSpace = 1;
	}

	@Override
	public String toString() {
		String header = padWithHashtag(ID,4) + "|" + padWithHashtag(numberOfRecords,2) + "|" + isLastPage + "|" + hasSpace
				+ "\n";
		String page = header;
		for(int i = 0; i < 11; i++){
			Record r = new Record(new int[]{},(byte)0,(byte)0);
			page = page + padWithHashtag(i, 2) + "," + r.toString();
		}
		int l = page.length();
		for(int i = 0; i < 1023-l; i++){
			page = page +"#";
		}
		page = page + "\n";
		return page;
	}

	private String padWithHashtag(int i, int upLimit) {
		String str = i + "";
		while (str.length() < upLimit) {
			str = str + "#";
		}
		return str;
	}
}
