package manager;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

public class StorageManager {

	public static final String SYSTEM_CATALOGUE_PATH = "./data/system_catalogue.txt";

	public static FileWriter fw;
	public static BufferedWriter bw;
	public static PrintWriter pw;

	public StorageManager() {
		try{
			fw = new FileWriter(SYSTEM_CATALOGUE_PATH, true);
			bw = new BufferedWriter(fw);
			pw = new PrintWriter(bw);
		} catch (IOException e) {
			System.out.println("gluglu");
		}
	}

	public static void createType(Scanner scan) throws IOException {
		welcome("type name");
		String typeName = scan.next();
		String dataFileName = typeName + ".txt";
		File f = new File(dataFileName);
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
	}

	public static void deleteType(Scanner scan) {
		// TODO Auto-generated method stub

	}

	public static void listTypes() {
		// TODO Auto-generated method stub

	}

	public static void createRecord(Scanner scan) {
		// TODO Auto-generated method stub

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
	
	public void exit() throws IOException{
		pw.close();
	}

}
