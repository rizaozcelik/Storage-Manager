package manager;

import java.io.IOException;
import java.io.RandomAccessFile;

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
	
	public static int getHasSpace(RandomAccessFile raf, long pageBaseIndex) throws IOException{
		raf.seek(pageBaseIndex + 10);
		return raf.read() - '0';
	}
	
	public static boolean isThereRecordBelow(RandomAccessFile raf, long recordBaseIndex) throws IOException{
		raf.seek(recordBaseIndex);
		int isValidFlag = raf.read() - '0';
		while(isValidFlag == 0 || isValidFlag == 1){
			if (isValidFlag == 1){
				return true;
			}
			recordBaseIndex += StorageManager.RECORD_SIZE + 1;
			raf.seek(recordBaseIndex);
			isValidFlag = raf.read()-'0';
		}
		return false;
	}

	@Override
	public String toString() {
		String header = Utils.padWithHashtag(ID+"",4) + "|" + Utils.padWithHashtag(numberOfRecords+"",2) + "|" + isLastPage + "|" + hasSpace
				+ "\n";
		String page = header;
		for(int i = 0; i < 11; i++){
			Record r = new Record(new int[]{},(byte)0,(byte)0);
			page = page + Utils.padWithHashtag(i+"", 2) + "," + r.toString();
		}
		int l = page.length();
		for(int i = 0; i < 1023-l; i++){
			page = page +"#";
		}
		page = page + "\n";
		return page;
	}

}
