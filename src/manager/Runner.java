package manager;

import java.util.Scanner;

public class Runner {

	public static void main(String[] args) {
		Scanner scan = new Scanner(System.in);
		while (true) {
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
				break;
			case 2:
				System.out.println("Choose a DML operation");
				System.out.println("Press 1 to Create a record");
				System.out.println("Press 2 to Update a record");
				System.out.println("Press 3 to Search a");
				System.out.println("Press 4 to List all records of a type");
				System.out.println("Press 5 to Delete a record");
				break;
			case 3:
				scan.close();
				System.exit(0);
				System.out.println(3);
				break;
			default:
				break;
			}
		}
	}

}
