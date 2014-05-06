package pippin;

import java.awt.Component;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.Properties;

import javax.swing.JOptionPane;

public class DirectoryManager {
	private Machine machine;
	private String pasm; // A string for the pasm directory name
	private String pexe; // A string for the pexe directory name
	private String edir; // A string for the name of the default Eclipse directory
	private Properties properties;

	public DirectoryManager(Machine m){
		machine = m;
		File temp = new File("propertyfile.txt");
		String eclipseFile = "";
		if(!temp.exists()) {
			PrintWriter out;
			try {
				//make a temporary file wherever Eclipse places files by default:
				out = new PrintWriter(temp);
				out.close();
				//capture its full path name 
				eclipseFile = temp.getAbsolutePath(); //remove it
				temp.delete();
			} 
			catch (FileNotFoundException e) {
				//this exception should not happen 
				e.printStackTrace();
			} 
		}
		else {
			//if the file exists it must have been in the default directory 
			eclipseFile = temp.getAbsolutePath();
			edir = extractDir(eclipseFile);
			// System.out.println(eclipseDir);--for debugging
			try {
				// load properties file "propertyfile.txt", if it exists 
				properties = new Properties();
				File inFile = new File("propertyfile.txt");
				if(inFile.exists()) {
					FileInputStream in = new FileInputStream(inFile);
					properties.load(in);
					pasm = properties.getProperty("SourceDirectory");
					pexe = properties.getProperty("ExecutableDirectory"); in.close();
				}
				// CLEAN UP ANY ERRORS IN WHAT IS STORED:
				if (pasm == null || pasm.isEmpty() || !new File(pasm).exists()){
					pasm = edir;
				}
				if (pexe == null || pexe.isEmpty() || !new File(pexe).exists()){
					pexe = edir;
				}
			}
			catch (Exception e){
				// PROPERTIES FILE DID NOT EXIST
				pasm = edir;
				pexe = edir;
			}
		}
	}

	public void closePropertiesFile() {
		try {
			FileOutputStream out = new FileOutputStream("propertyfile.txt");
			properties.store(out, "File locations");
			out.close();
		}
		catch (Exception e){
			JOptionPane.showMessageDialog((Component) machine.getFrame(),
					"Problems when storing Source Directory in Property File", "Warning",
					JOptionPane.WARNING_MESSAGE);
		}
	}
	public String getPasm() {
		return pasm;
	}

	public String getPexe() {
		return pexe;
	}

	public String getDf() {
		return edir;
	}

	private String extractDir(String fileName){
		fileName.replace('\\', '/');
		int i = fileName.lastIndexOf('/');
		fileName = fileName.substring(0,i+1);
		return fileName;
	}

	public void recordSourceDir(String sourceFile) {
		pasm = extractDir(sourceFile);
		properties.setProperty("SourceDirectory", pasm);
	}

	public void recordExecutableDir(String executableFile) {
		pexe = extractDir(executableFile);
		properties.setProperty("ExecutableDirectory", pexe);
	}	
}
