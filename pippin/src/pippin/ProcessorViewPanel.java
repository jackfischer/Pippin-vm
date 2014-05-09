package pippin;

import java.awt.GridLayout;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class ProcessorViewPanel implements Observer{
	private Machine machine;
	private JTextField accumulator;
	private JTextField programCounter;
	
	public ProcessorViewPanel(Machine machine) {
		this.machine = machine;
		machine.addObserver(this);
	}
	
	public JComponent createProcessorDisplay(){
		JPanel returnPanel = new JPanel();
		returnPanel.setLayout(new GridLayout(1,0));
		JLabel acl = new JLabel("Accumulator: ", JLabel.RIGHT);
		accumulator = new JTextField();
		JLabel prc = new JLabel("Program Counter: ", JLabel.RIGHT);
		programCounter = new JTextField();
		returnPanel.add(acl);
		returnPanel.add(accumulator);
		returnPanel.add(prc);
		returnPanel.add(programCounter);
		return returnPanel;
	}
	@Override
	public void update(Observable arg0, Object arg1) {
		accumulator.setText("" + machine.getAccumulator());
		programCounter.setText("" +machine.getProgramCounter());
	}

}