package manager;

public class Record {
	int[] values;
	byte isLastRecord;
	byte isValid;

	public Record(int[] values, int isLastRecord, int isValid) {
		this.values = values;
		this.isLastRecord = (byte) isLastRecord;
		this.isValid = (byte) isValid;
	}

	@Override
	public String toString() {
		String str = isLastRecord + "," + isValid;
		for (int i = 0; i < values.length; i++) {
			str = str + "," + padWithHashtag(values[i]);
		}
		for (int i = values.length; i < 9; i++) {
			str = str + ",########";
		}
		str = str + "\n";
		return str;
	}

	private String padWithHashtag(int i) {
		String s = i + "";
		while (s.length() < 8) {
			s = s + "#";
		}
		return s;
	}
}
