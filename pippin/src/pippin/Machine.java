package pippin;

import java.util.Observable;

import javax.swing.JFrame;

public class Machine extends Observable {
	private States state;
	private boolean halted = false;
	private boolean autoStepping = false;
	private JFrame frame;
	
    class CPU {
        private int accumulator;
        private int programCounter;
    }

    private CPU cpu = new CPU();

    public int getAccumulator() {
        return cpu.accumulator;
    }

    public void setAccumulator(int i) {
        cpu.accumulator = i;
    }

    public int getProgramCounter() {
        return cpu.programCounter;
    }

    public void setProgramCounter(int i) {
        cpu.programCounter = i;
    }

    public void incrementCounter() {
        cpu.programCounter++;
    }

	public GUIMemoryDecorator getMemory() {
		// TODO Auto-generated method stub
		return null;
	}

	public States getState() {
		// TODO Auto-generated method stub
		return null;
	}

	public Instruction[] getiSet() {
		// TODO Auto-generated method stub
		return null;
	}

}