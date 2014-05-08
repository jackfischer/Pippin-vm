package pippin;

import java.io.File;
import java.io.IOException;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;

public class LoaderAdapter {
	private Machine machine;
	private File currentlyExecutingFile;
	private Loader loader;
	
	public LoaderAdapter(Machine m){
		this.machine = m;
		loader = new Loader();
	}
	
	public void load(){
		JFileChooser chooser = new JFileChooser(machine.getDirectoryManager().getExecutableDir());
		FileNameExtensionFilter filter = new FileNameExtensionFilter("Pippin Executable Files","pexe");
		chooser.setFileFilter(filter);
		int openOK = chooser.showOpenDialog(machine.getFrame());
		if (openOK == JFileChooser.APPROVE_OPTION){
			currentlyExecutingFile = chooser.getSelectedFile();
		}
		if (currentlyExecutingFile!=null && currentlyExecutingFile.exists()){
			String executableFile = currentlyExecutingFile .getAbsolutePath();
			machine.getDirectoryManager().recordExecutableDir(executableFile);
			finalStep();
		}
		else{
			JOptionPane.showMessageDialog(machine.getFrame(), "No file was selected or the file does not exist.\n" + 
					"Cannot load the program", 
					"Warning", JOptionPane.WARNING_MESSAGE);
		}
	}

	public void finalStep() {
		try {
			loader.load(machine.getMemory(), currentlyExecutingFile); 
			// machine.setRunnable(); removed in pdf8
			machine.setAutoStepping(false);
			machine.setAccumulator(0);
			machine.setProgramCounter(0);
			//machine.callForUpdates(); changed to below
			machine.callForUpdates(States.PROGRAM_LOADED_NOT_AUTOSTEPPING);
			} catch(IOException e) {
				JOptionPane.showMessageDialog(machine.getFrame(), "The selected has problems.\n" +
						"Cannot load the program",
						"Warning", JOptionPane.WARNING_MESSAGE);
			}
	}
	
	public void reload(){
		if(currentlyExecutingFile!=null && currentlyExecutingFile.exists()){
			finalStep();
		}
		else{
			JOptionPane.showMessageDialog(machine.getFrame(), "For some reason the currently executing file is lost.\n" + 
		 "Cannot load the program",
		 "Warning,", JOptionPane.WARNING_MESSAGE);
		}
	}
}