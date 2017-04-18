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

	public static ArrayList<Long> getIsValidIndexOfARecord(Scanner scan, String typeName, int searchIndex,
			long searchValue) throws IOException {

		ArrayList<Long> indices = new ArrayList<Long>();

		RandomAccessFile raf = Utils.getStreamOfDataType(typeName);
		long pageBaseIndex = 0;

		int wasLastPage = 0;
		while (wasLastPage == 0) {
			long recordsFirstFieldIndex = pageBaseIndex + 19;
			boolean wasLastRecord = false;
			while (!wasLastRecord) {

				long recordsDesiredFieldsIndex = recordsFirstFieldIndex + searchIndex * 9;
				raf.seek(recordsFirstFieldIndex - 2);
				if (raf.read() - '0' == 1) {
					// The record is valid
					long val = getValueOfTheField(raf, recordsDesiredFieldsIndex);
					if (searchValue == val) {
						indices.add(recordsFirstFieldIndex - 2);
					}
				}
				raf.seek(recordsFirstFieldIndex - 4);
				int flag = raf.read() - '0';
				wasLastRecord = (flag == 1);
				recordsFirstFieldIndex += RECORD_SIZE + 1;

			}
			wasLastPage = Page.getIsLastPage(raf, pageBaseIndex);
			pageBaseIndex += Page.PAGE_SIZE;
		}
		if(indices.size() == 0){
			System.out.println("No record found matching your criterion");
		}
		return indices;
	}

	public static long getValueOfTheField(RandomAccessFile raf, long valIndex) throws IOException {
		String res = "";
		raf.seek(valIndex);
		int val = raf.read() - '0';
		while (val <= 9 && val >= 0) {
			valIndex++;
			res = res + val;
			raf.seek(valIndex);
			val = raf.read() - '0';
		}
		return Long.parseLong(res);
	}

	@Override
	public String toString() {
		String str = isLastRecord + "," + isValid;
		for (int i = 0; i < values.length; i++) {
			str = str + "," + Utils.padWithHashtag(values[i] + "", 8);
		}
		for (int i = values.length; i < 9; i++) {
			str = str + ",########";
		}
		str = str + "\n";
		return str;
	}

}
