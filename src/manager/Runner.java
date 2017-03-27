package manager;

import java.io.IOException;
import java.util.Scanner;

public class Runner {

	public static void main(String[] args) throws IOException {
		System.exit(1);
		StorageManager manager = new StorageManager();
		Scanner scan = new Scanner(System.in);
		boolean exited = false;
		while (!exited) {
			System.out.println("Press 1 for DDL operations");
			System.out.println("Press 2 for DML operations");
			System.out.println("Press 3 to exit");
			int choice = scan.nextInt();
			switch (choice) {
			case 1:
				System.out.println("Choose a DDL operation");
				System.out.println("Press 1 to Create a type");
				System.out.println("Press 2 to Delete a type");
				System.out.println("Press 3 to List all types");
				choice = scan.nextInt();
				if (choice == 1) {
					StorageManager.createType(scan);
				} else if (choice == 2) {
					StorageManager.deleteType(scan);
				} else if (choice == 3) {
					StorageManager.listTypes();
				}
				break;
			case 2:
				System.out.println("Choose a DML operation");
				System.out.println("Press 1 to Create a record");
				System.out.println("Press 2 to Update a record");
				System.out.println("Press 3 to Search a record");
				System.out.println("Press 4 to List all records of a type");
				System.out.println("Press 5 to Delete a record");
				choice = scan.nextInt();
				if (choice == 1) {
					StorageManager.createRecord(scan);
				} else if (choice == 2) {
					StorageManager.updateRecord(scan);
				} else if (choice == 3) {
					StorageManager.searchRecord(scan);
				} else if (choice == 4) {
					StorageManager.listRecords(scan);
				} else if (choice == 5) {
					StorageManager.deleteRecord(scan);
				}
				break;
			case 3:
				exited = true;
				System.out.println(3);
				break;
			default:
				System.out.println("Please choose a valid option\n\n");
				break;
			}
		}
		manager.exit();
		scan.close();
	}

	
}
