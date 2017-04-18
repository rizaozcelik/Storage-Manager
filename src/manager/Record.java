package manager;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Scanner;

public class Record {
	
	public static final int RECORD_SIZE = 87;
	
	int[] values;
	byte isLastRecord;
	byte isValid;

	public Record(int[] values, int isLastRecord, int isValid) {
		this.values = values;
		this.isLastRecord = (byte) isLastRecord;
		this.isValid = (byte) isValid;
	}

	public static ArrayList<Long> getIsValidIndexOfARecord(Scanner scan, String typeName, int searchIndex, long searchValue) throws IOException {

		ArrayList<Long> indices = new ArrayList<Long>();

		RandomAccessFile raf = Utils.getStreamOfDataType(typeName);
		long pageBaseIndex = 0;

		int wasLastPage = 0;
		while (wasLastPage == 0) {
			long recordsFirstFieldIndex = pageBaseIndex + 19;
			boolean wasLastRecord = false;
			while (!wasLastRecord) {
				long recordsDesiredFieldsIndex = recordsFirstFieldIndex + searchIndex * 9;
				long val = Page.getValueOfTheField(raf, recordsDesiredFieldsIndex);
				if (searchValue == val) {
					indices.add(recordsFirstFieldIndex - 2);
				}
				raf.seek(recordsFirstFieldIndex - 4);
				int flag = raf.read() - '0';
				wasLastRecord = (flag == 1);

				recordsFirstFieldIndex += RECORD_SIZE + 1;

			}
			wasLastPage = Page.getIsLastPage(raf, pageBaseIndex);
			pageBaseIndex += Page.PAGE_SIZE;
		}

		return indices;
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
