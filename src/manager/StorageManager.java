package manager;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.RandomAccessFile;
import java.util.Scanner;

public class StorageManager {

	public static final String SYSTEM_CATALOGUE_PATH = "./data/system_catalogue.txt";
	public static final int SYS_CAT_ENTRY_SIZE = 191;
	public static final int RECORD_SIZE = 87;

	public static FileWriter fw;
	public static BufferedWriter bw;
	public static PrintWriter pw;
	public static boolean isWriterClosed;

	public StorageManager() {
		try {
			fw = new FileWriter(SYSTEM_CATALOGUE_PATH, true);
			bw = new BufferedWriter(fw);
			pw = new PrintWriter(bw);
			isWriterClosed = false;
		} catch (IOException e) {
			System.out.println("Error while opening writer");
			isWriterClosed = true;
		}
	}

	public static void createType(Scanner scan) throws IOException {
		if (isWriterClosed) {
			pw = new PrintWriter(bw);
		}
		welcome("type name");
		String typeName = scan.next();
		String dataFileName = (typeName + ".txt").toLowerCase();
		File f = new File("./data/dataFiles/" + dataFileName);
		f.createNewFile();
		welcome("number of fields");
		int numberOfFields = scan.nextInt();
		String[] fields = new String[numberOfFields];
		welcome("field names");
		for (int i = 0; i < numberOfFields; i++) {
			String fieldName = scan.next();
			fields[i] = fieldName;
		}
		SystemCatalogueEntry e = new SystemCatalogueEntry(typeName, dataFileName, numberOfFields, fields);
		pw.print(e);
		pw.close();
		isWriterClosed = true;
	}

	public static void deleteType(Scanner scan) throws IOException {
		if (!isWriterClosed) {
			pw.close();
			isWriterClosed = true;
		}
		RandomAccessFile raf = new RandomAccessFile(SYSTEM_CATALOGUE_PATH, "rw");
		welcome("data type to be deleted.");
		String typeName = scan.next();
		long startByteOfTheType = findStartingByteOfDataType(raf, typeName);
		raf.seek(startByteOfTheType + 36);
		raf.write((int) '0');
		raf.close();

		String fileToDelete = "./data/dataFiles/" + typeName.toLowerCase() + ".txt";
		File f = new File(fileToDelete);
		if (f.exists()) {
			f.delete();
		}
	}

	public static void listTypes() throws FileNotFoundException {
		if (!isWriterClosed) {
			pw.close();
			isWriterClosed = true;
		}
		Scanner file = new Scanner(new File(SYSTEM_CATALOGUE_PATH));
		while (file.hasNextLine()) {
			String entry = file.nextLine();
			String[] values = entry.split(",");
			// Otherwise it means it is deleted since values[3] is isValid flag.
			if (values[3].equals("1")) {
				for (int i = 0; i < values.length; i++) {
					int index = values[i].indexOf('#');
					if (index == -1) {
						index = values[i].length();
					}
					if (index != 0) {
						System.out.print(values[i].substring(0, index) + " ");
					}
				}
				System.out.println();
			}
		}
		file.close();
	}

	public static void createRecord(Scanner scan) throws IOException {
		welcome("record type");
		String typeName = scan.next();
		RandomAccessFile raf = new RandomAccessFile(SYSTEM_CATALOGUE_PATH, "r");
		long startingByteOfType = findStartingByteOfDataType(raf, typeName);
		raf.seek(startingByteOfType + 34);
		int numberOfFields = raf.read() - '0';
		int[] values = new int[numberOfFields];
		welcome("enter " + numberOfFields + " fields");
		for (int i = 0; i < numberOfFields; i++) {
			values[i] = scan.nextInt();
		}
		Record r = new Record(values, 1, 1);

		String dataFileName = "./data/dataFiles/" + typeName.toLowerCase() + ".txt";
		raf.close();
		raf = new RandomAccessFile(dataFileName, "rw");
		if (raf.length() == 0) {
			Page p = new Page(1);
			StringBuilder s = new StringBuilder(p.toString());
			s.replace(5, 6, "1");
			s.replace(15, 100, r.toString());
			raf.writeBytes(s.toString());
		} else {
			boolean inserted = false;
			long pageBaseIndex = 0;
			while (!inserted) {
				raf.seek(pageBaseIndex + 10);
				int ispageEmptyFlag = raf.read() - '0';
				if (ispageEmptyFlag == 1) {
					// Page has deallocated space
					boolean recordsAreFull = true;
					long recordBaseIndex = 17;
					while (recordsAreFull) {
						raf.seek(recordBaseIndex);
						int isRecordEmptyFlag = raf.read() - '0'; 
						System.out.println(isRecordEmptyFlag);
						if (isRecordEmptyFlag == 1) {
							recordBaseIndex += RECORD_SIZE + 1;
						} else {
							System.out.println(isRecordEmptyFlag);
							inserted = true;
							recordsAreFull = false;
						}

					}
				}
			}
		}
		raf.close();
	}

	public static void updateRecord(Scanner scan) {
		// TODO Auto-generated method stub

	}

	public static void searchRecord(Scanner scan) {
		// TODO Auto-generated method stub

	}

	public static void listRecords(Scanner scan) {
		// TODO Auto-generated method stub

	}

	public static void deleteRecord(Scanner scan) {
		// TODO Auto-generated method stub

	}

	private static void welcome(String word) {
		System.out.println("Please enter the " + word);
	}

	public void exit() throws IOException {
		pw.close();
	}

	private static long findStartingByteOfDataType(RandomAccessFile raf, String typeName) throws IOException {
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

}
