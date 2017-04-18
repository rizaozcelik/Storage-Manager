package manager;

public class SysCatPage {
	
	
	@Override
	public String toString(){
		String ret = "";
		String[] fields = new String[]{"#"};
		SystemCatalogueEntry e = new SystemCatalogueEntry("", "", 0, fields);
		for(int i = 0; i < 5; i++){
			ret += e.toString();
		}
		ret = Utils.padWithHashtag(ret, 1024);
		return ret + "\n";
	}
}
