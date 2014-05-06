package pippin;

import java.util.Properties;

public class DirectoryManager {
	private Machine machine;
	private String pasm; // A string for the pasm directory name
	private String pexe; // A string for the pexe directory name
	private String df; // A string for the name of the default Eclipse directory
	private Properties properties;
	
	public DirectoryManager(Machine m){
		machine = m;
	}
	
	public String getPasm() {
		return pasm;
	}
	
	public String getPexe() {
		return pexe;
	}

	public String getDf() {
		return df;
	}
	
	private String extractDir(String fileName){
		fileName.replace('\\', '/');
		int i = fileName.lastIndexOf('/');
		fileName = fileName.substring(0,i+1);
		return fileName;
	}
	
}
