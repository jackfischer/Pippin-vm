package pippin;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class ControlPanel implements Observer {
	
	private Machine machine;
	private JButton stepButton = new JButton("Step");
	private JButton clearButton = new JButton("Clear");
	private JButton runButton = new JButton("Run/Pause");
	private JButton reloadButton = new JButton("Reload");
	
	public ControlPanel(Machine machine) {
		this.machine=machine;
		machine.addObserver(this);
	}
	
	public void checkEnabledButtons() { 
		 runButton.setEnabled(machine.getState().getRunSuspendActive()); 
		 stepButton.setEnabled(machine.getState().getStepActive()); 
		 clearButton.setEnabled(machine.getState().getClearActive()); 
		 reloadButton.setEnabled(machine.getState().getReloadActive()); 
	} 
		 
	@Override 
	public void update(Observable arg0, Object arg1) { 
		checkEnabledButtons(); 
	} 
	
	private class RunPauseListener implements ActionListener { 
		 @Override 
		 public void actionPerformed(ActionEvent arg0) { 
		 if (machine.isAutoStepping()) { 
		 machine.setAutoStepping(false); 
		 } else { 
		 machine.setAutoStepping(true); 
		 } }
	} 
	
	private class ClearListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent arg0) {
			machine.clearAll();
		}
	}
	
	private class ReloadListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent arg0) {
			machine.reload();
		}
	}
	
	private class StepListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent arg0) {
			machine.step();
		}
	}

	public JComponent createControlDisplay() {
		JPanel retVal = new JPanel(new GridLayout(1, 0));
		
		final JSlider slider = new JSlider(5,1000);
		retVal.add(slider);
		slider.addChangeListener((new ChangeListener(){
			public void stateChanged(ChangeEvent arg0) { 
				machine.setPeriod(slider.getValue()); 
			} 
		})); 

		stepButton.setBackground(Color.WHITE);
		retVal.add(stepButton);
		stepButton.addActionListener(new StepListener());
		
		clearButton.setBackground(Color.WHITE);
		retVal.add(clearButton);
		clearButton.addActionListener(new ClearListener());
		
		runButton.setBackground(Color.WHITE);
		retVal.add(runButton);
		runButton.addActionListener(new RunPauseListener());
		
		reloadButton.setBackground(Color.WHITE);
		retVal.add(reloadButton);
		reloadButton.addActionListener(new ReloadListener());

		return retVal;
		
		
	}	
}