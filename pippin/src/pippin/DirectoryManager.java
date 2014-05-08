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
	private String sourceDir; // A string for the sourceDir directory name
	private String executableDir; // A string for the executableDir directory name
	private String eclipseDir; // A string for the name of the default Eclipse directory
	private Properties properties;

	public DirectoryManager(Machine m){
		this.machine = m;
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
		}
		eclipseDir = extractDir(eclipseFile);
		// System.out.println(eclipseDir);--for debugging
		try {
			// load properties file "propertyfile.txt", if it exists 
			properties = new Properties();
			File inFile = new File("propertyfile.txt");
			if(inFile.exists()) {
				FileInputStream in = new FileInputStream(inFile);
				properties.load(in);
				sourceDir = properties.getProperty("SourceDirectory");
				executableDir = properties.getProperty("ExecutableDirectory"); 
				in.close();
			}
			// CLEAN UP ANY ERRORS IN WHAT IS STORED:
			if (sourceDir == null || sourceDir.length() == 0 
					|| !new File(sourceDir).exists()){
				sourceDir = eclipseDir;
			}
			if (executableDir == null || executableDir.length() == 0 
					|| !new File(executableDir).exists()){
				executableDir = eclipseDir;
			}
		}
		catch (Exception e){
			// PROPERTIES FILE DID NOT EXIST
			sourceDir = eclipseDir;
			executableDir = eclipseDir;
		}
	}

	public String getSourceDir() {
		return sourceDir;
	}

	public String getExecutableDir() {
		return executableDir;
	}

	public String getEclipseDir() {
		return eclipseDir;
	}

	private String extractDir(String fileName){
		fileName = fileName.replace('\\', '/');
		int lastSlash = fileName.lastIndexOf('/');
		//fileName = fileName.substring(0,i+1);
		return fileName.substring(0, lastSlash + 1);
	}

	public void recordSourceDir(String sourceFile) {
		this.sourceDir = extractDir(sourceFile);
		properties.setProperty("SourceDirectory", sourceDir);
	}

	public void recordExecutableDir(String executableFile) {
		this.executableDir = extractDir(executableFile);
		properties.setProperty("ExecutableDirectory", executableDir);
	}	
	
	public void closePropertiesFile() {
		try {
			FileOutputStream out = new FileOutputStream("propertyfile.txt");
			properties.store(out, "File locations");
			out.close();
		}
		catch (Exception e){
			JOptionPane.showMessageDialog(machine.getFrame(),
					"Problems when storing Source Directory in Property File", 
					"Warning",
					JOptionPane.WARNING_MESSAGE);
		}
	}
}