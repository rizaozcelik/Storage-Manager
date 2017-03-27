package manager;

public class Page {
	int ID;
	int numberOfRecords;
	int isLastPage;
	int isEmpty;
	public Page(int iD, int numberOfRecords, int isLastPage, int isEmpty) {
		ID = iD;
		this.numberOfRecords = numberOfRecords;
		this.isLastPage = isLastPage;
		this.isEmpty = isEmpty;
	}
	
	
}
