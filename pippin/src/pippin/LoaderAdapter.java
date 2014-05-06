package pippin;

import java.io.File;
import java.io.IOException;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;

public class LoaderAdapter {
	private Machine machine;
	private File currentlyExecutingFile;
	private Loader loader = new Loader();
	
	public LoaderAdapter(Machine m){
		machine = m;
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
			JOptionPane.showMessageDialog(machine.getFrame(), "No file was selected or the file does not exist.\nCannot load the program");
		}
	}

	public void finalStep() {
		try {
			loader.load(machine.getMemory(), currentlyExecutingFile); 
            //machine.setRunnable();
			machine.setAutoStepping(false);
			machine.setAccumulator(0);
			machine.setProgramCounter(0);
			//machine.callForUpdates();
            machine.callForUpdates(States.PROGRAM_LOADED_NOT_AUTOSTEPPING);
			} catch(IOException e) {
				JOptionPane.showMessageDialog(machine.getFrame(), "The selected has problems.\nCannot load the program");
			}
	}
	
	public void reload(){
		if(currentlyExecutingFile!=null && currentlyExecutingFile.exists()){
			finalStep();
		}
		else{
			JOptionPane.showMessageDialog(machine.getFrame(), "For some reason the currently executing file is lost.\nCannot load the program");
		}
	}
}