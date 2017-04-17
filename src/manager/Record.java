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
			str = str + "," + Utils.padWithHashtag(values[i]+"",8);
		}
		for (int i = values.length; i < 9; i++) {
			str = str + ",########";
		}
		str = str + "\n";
		return str;
	}

	
}
