package manager;

import java.util.Scanner;

public class Runner {

	public static void main(String[] args) {
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
					createType(scan);
				} else if (choice == 2) {
					deleteType(scan);
				} else if (choice == 3) {
					listTypes();
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
					createRecord(scan);
				} else if (choice == 2) {
					updateRecord(scan);
				} else if (choice == 3) {
					searchRecord(scan);
				} else if (choice == 4) {
					listRecords(scan);
				} else if (choice == 5) {
					deleteRecord(scan);
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
		scan.close();
	}

	private static void createType(Scanner scan) {
		// TODO Auto-generated method stub

	}

	private static void deleteType(Scanner scan) {
		// TODO Auto-generated method stub

	}

	private static void listTypes() {
		// TODO Auto-generated method stub

	}

	private static void createRecord(Scanner scan) {
		// TODO Auto-generated method stub

	}

	private static void updateRecord(Scanner scan) {
		// TODO Auto-generated method stub

	}

	private static void searchRecord(Scanner scan) {
		// TODO Auto-generated method stub

	}

	private static void listRecords(Scanner scan) {
		// TODO Auto-generated method stub

	}

	private static void deleteRecord(Scanner scan) {
		// TODO Auto-generated method stub

	}

}
