package manager;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Scanner;

public class StorageManager {

	public static final String SYSTEM_CATALOGUE_PATH = SystemCatalogueEntry.SYSTEM_CATALOGUE_PATH;
	public static final int RECORD_SIZE = Record.RECORD_SIZE;
	public static final int PAGE_SIZE = Page.PAGE_SIZE;

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
		long startByteOfTheType = SystemCatalogueEntry.findStartingByteOfDataType(raf, typeName);
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
		int numberOfFields = SystemCatalogueEntry.getNumberOfFieldsOfDataType(typeName);

		int[] values = new int[numberOfFields];
		welcome("enter " + numberOfFields + " fields");
		for (int i = 0; i < numberOfFields; i++) {
			values[i] = scan.nextInt();
		}
		Record r = new Record(values, 1, 1);

		RandomAccessFile raf = Utils.getStreamOfDataType(typeName);
		int pageIdCounter = 0;
		if (raf.length() == 0) {
			// This is the first record that will be created in the data type
			Page p = new Page(1);
			StringBuilder s = new StringBuilder(p.toString());
			s.replace(5, 6, "1");
			s.replace(15, 100, r.toString());
			raf.writeBytes(s.toString());
		} else {
			// If there is already a page
			boolean inserted = false;
			long pageBaseIndex = 0;
			while (!inserted) {
				pageIdCounter++; // Will be used for new page creation if
									// necessary
				if (Page.getHasSpace(raf, pageBaseIndex) == 1) {
					// Page has deallocated space
					int recordCounter = 0;
					boolean recordsAreFull = true;
					// Index of isValid field is 17
					long recordBaseIndex = pageBaseIndex + 17;
					while (recordsAreFull) {
						recordCounter++;
						if (recordCounter == 11) {
							// It means that page is not actually empty.
							// hence make adjustments and append a new page
							Page.setHasSpace(raf, pageBaseIndex, "0");
							Page.setIsLastPage(raf, pageBaseIndex, "0");
							long l = raf.length();
							raf.seek(l);
							raf.writeBytes((new Page(pageIdCounter + 1)).toString());
							pageBaseIndex += PAGE_SIZE;
							recordBaseIndex = pageBaseIndex + 17;
						}
						raf.seek(recordBaseIndex);
						int isRecordValidFlag = raf.read() - '0';
						if (isRecordValidFlag == 1) {
							if (recordCounter != 11) {
								// update isLast field of record. It is 2 index
								// before isValid
								raf.seek(recordBaseIndex - 2);
								raf.writeBytes("0");
							}
							recordBaseIndex += RECORD_SIZE + 1;
						} else if (isRecordValidFlag == 0) {
							// It could also be #
							if (Page.isThereRecordBelow(raf, recordBaseIndex)) {
								r.isLastRecord = 0;
							}
							long startingIndexOfRecordInPage = recordBaseIndex - 2;
							raf.seek(startingIndexOfRecordInPage);
							raf.writeBytes(r.toString());
							inserted = true;
							recordsAreFull = false;
						}

					}
				} else {
					pageBaseIndex += PAGE_SIZE;
				}
			}
		}
		raf.close();
	}

	public static void updateRecord(Scanner scan) throws IOException {
		welcome("record type");
		String typeName = scan.next();
		int numberOfFields = SystemCatalogueEntry.getNumberOfFieldsOfDataType(typeName);

		int[] values = new int[numberOfFields];
		welcome("enter " + numberOfFields + " fields");
		for (int i = 0; i < numberOfFields; i++) {
			values[i] = scan.nextInt();
		}
		Record newRecord = new Record(values, 1, 1);

		welcome("index of the field to search on");
		int searchIndex = scan.nextInt();
		welcome("value of the field to search on");
		long searchValue = scan.nextInt();

		ArrayList<Long> recordsToUpdate = Record.getIsValidIndexOfARecord(scan, typeName, searchIndex, searchValue);
		RandomAccessFile raf = Utils.getStreamOfDataType(typeName);
		for (Long r : recordsToUpdate) {
			raf.seek(r - 2);
			int isLastFlag = raf.read() - '0';
			if (isLastFlag == 0) {
				newRecord.isLastRecord = 0;
			} else if (isLastFlag == 1) {
				newRecord.isLastRecord = 1;
			} else {
				System.out.println("ERROR IN UPDATE");
				System.exit(1);
			}
			raf.seek(r - 2);
			raf.writeBytes(newRecord.toString());
		}
		raf.close();
	}

	public static void searchRecord(Scanner scan) throws IOException {
		welcome("type of the record");
		String typeName = scan.next().toLowerCase();
		welcome("index of the field to search on");
		int searchIndex = scan.nextInt();
		welcome("value of the field to search on");
		long searchValue = scan.nextInt();

		ArrayList<Long> res = Record.getIsValidIndexOfARecord(scan, typeName, searchIndex, searchValue);
		int numberOfFields = SystemCatalogueEntry.getNumberOfFieldsOfDataType(typeName);
		RandomAccessFile raf = Utils.getStreamOfDataType(typeName);

		for (Long l : res) {
			for (int i = 0; i < numberOfFields; i++) {
				long value = Record.getValueOfTheField(raf, l + 2 + 9 * i);
				System.out.print(value + " ");
			}
			System.out.println();
		}

	}

	public static void listRecords(Scanner scan) throws IOException {
		welcome("record type");
		String typeName = scan.next();
		int numberOfFields = SystemCatalogueEntry.getNumberOfFieldsOfDataType(typeName);

		RandomAccessFile raf = Utils.getStreamOfDataType(typeName);
		long pageBaseIndex = 0;
		int recordCounter = 0;
		int wasLastPage = 0;
		while (wasLastPage == 0) {
			long recordBaseIndex = pageBaseIndex + 17;
			for (int i = 0; i < 11; i++) {
				raf.seek(recordBaseIndex);
				int isRecordValidFlag = raf.read() - '0';
				if (isRecordValidFlag == 1) {
					recordCounter++;
					System.out.print("Record " + recordCounter + ": ");
					for (int j = 0; j < numberOfFields; j++) {
						// 8 is field size
						long valIndex = recordBaseIndex + j * 8 + j + 2;
						System.out.print(Record.getValueOfTheField(raf, valIndex));
						if (j != numberOfFields - 1) {
							System.out.print(", ");
						}
					}
					System.out.println();
				}
				recordBaseIndex += RECORD_SIZE + 1;
			}
			wasLastPage = Page.getIsLastPage(raf, pageBaseIndex);

			pageBaseIndex += 1024;
		}
		raf.close();
	}

	public static void deleteRecord(Scanner scan) throws IOException {
		welcome("type of the record");
		String typeName = scan.next().toLowerCase();
		welcome("index of the field to search on");
		int searchIndex = scan.nextInt();
		welcome("value of the field to search on");
		long searchValue = scan.nextInt();

		ArrayList<Long> records = Record.getIsValidIndexOfARecord(scan, typeName, searchIndex, searchValue);
		RandomAccessFile raf = Utils.getStreamOfDataType(typeName);

		for (Long r : records) {
			raf.seek(r);
			raf.writeBytes("0");
			int pageNumber = (int) (r / PAGE_SIZE);
			long pageBaseIndex = pageNumber * PAGE_SIZE;
			Page.setHasSpace(raf, pageBaseIndex, "1");
			raf.seek(r - 2);
			int isLastFlag = raf.read() - '0';
			long recordBaseIndex = r - RECORD_SIZE - 1;
			if (isLastFlag == 1) {
				// Last record in the page is deleted. Hence find the new last
				// record.
				boolean newLastFound = false;
				while (!newLastFound) {
					raf.seek(recordBaseIndex);
					int isValidFlag = raf.read() - '0';
					if (isValidFlag == 1) {
						raf.seek(recordBaseIndex - 2);
						raf.writeBytes("1");
						newLastFound = true;
					} else if(isValidFlag == -13){
						// hashtag is found. Hence no other record in page. Break the loop.
						newLastFound = true;
					}
					recordBaseIndex = recordBaseIndex - RECORD_SIZE - 1;
				}
			}
		}
		raf.close();
	}

	private static void welcome(String word) {
		System.out.println("Please enter the " + word);
	}

	public void exit() throws IOException {
		pw.close();
	}
}
