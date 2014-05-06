package pippin;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.util.Observable;
import java.util.Observer;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

public class ProcessorViewPanel implements Observer{

	private JTextField accumulator;
	private JTextField programCounter;
	
	public ProcessorViewPanel(Machine machine) {
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
		accumulator.setText("" + machine.getAcculumator());
	}

}
