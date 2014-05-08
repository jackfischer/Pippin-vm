package pippin;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Observable;

import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.Timer;

import pippin.CodeAccessException;
import pippin.DataAccessException;
import pippin.DivideByZeroException;
import pippin.Instruction;
import pippin.CodeViewPanel;
import pippin.DataViewPanel;
import pippin.States;
import pippin.Machine.ExitAdapter;
import pippin.Machine.TimerListener;
import pippin.AssemblerAdapter;
import pippin.DirectoryManager;
import pippin.LoaderAdapter;


public class Machine extends Observable {
	class CPU {
		private int accumulator;
		private int programCounter;
	}

	private CPU cpu = new CPU();
	private Instruction[] iSet;
	private MemoryInterface memory;
	private States state;
	private boolean halted = false;
	private boolean autoStepping = false;
	private JFrame frame;
	private DirectoryManager directoryManager = new DirectoryManager(this);
	private AssemblerAdapter assembler = new AssemblerAdapter(this);
	private LoaderAdapter loader = new LoaderAdapter(this);
	private final int INITIAL_PERIOD = 500;
	private Timer timer;

	public Machine() {
		memory = new Memory();
		iSet = new Instruction[32];
		setGUIMemory();
		createAndShowGUI();
		callForUpdates(States.NOTHING_LOADED);
	}
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

	public MemoryInterface getMemory() {
		return memory;
	}

	public Instruction[] getiSet() {
		return iSet;
	}
	public States getState() {
		return state;
	}

	public void setMemory(Memory memory) {
		this.memory = memory;
	}

	public boolean isAutoStepping(){
		return autoStepping;
	}

	public void setAutoStepping(boolean val){
		autoStepping = val;
	}

	public DirectoryManager getDirectoryManager(){
		return directoryManager;
	}
	public void halt(){
		halted = true;
		callForUpdates(States.PROGRAM_HALTED);
		state = States.PROGRAM_HALTED;
	}

	public void callForUpdates() { 
		setChanged(); 
		notifyObservers(); 
	} 

	public void callForUpdates(States newState) { 
		state = newState; 
		state.enter(); 
		setChanged(); 
		notifyObservers(); 
	}

	public void exit() { // method executed when user exits the program 
		int decision = JOptionPane.showConfirmDialog( 
				frame, 
				"Do you really wish to exit?", 
				"Confirmation", 
				JOptionPane.YES_NO_OPTION); 
		if (decision == JOptionPane.YES_OPTION) { 
			directoryManager.closePropertiesFile(); //requires pdf 3 to be done
			System.exit(0); 
		} 
	}

	public class ExitAdapter extends WindowAdapter { 
		@Override 
		public void windowClosing(WindowEvent arg0) { 
			exit(); 
		} 
	} 

	public class TimerListener implements ActionListener { 
		@Override 
		public void actionPerformed(ActionEvent e) { 
			if(autoStepping && !halted) { 
				step(); 
			} 
		} 
	}

	public void assembleFile() { 
		assembler.assemble(); 
	} 

	public void loadFile() { 
		loader.load(); 
		this.callForUpdates(States.PROGRAM_LOADED_NOT_AUTOSTEPPING); 
	} 

	public void execute() { 
		this.setAutoStepping(false); 
		while(!halted) { 
			step1(); 
		} 
		this.callForUpdates(States.PROGRAM_HALTED); 
	}
	
	// HERE ARE METHODS REQUIRED BY OTHER CLASSES THAT HAVE NOT YET BEEN COMPLETED
	
	public JFrame getFrame() {
		return frame;
		//That's a guess -jack
	}

	public void clearAll() {
		setAutoStepping(false);
		memory.clearCode();
		memory.clearData();
		cpu.accumulator = 0;
		cpu.programCounter = 0;
		callForUpdates(States.NOTHING_LOADED);
	}

	public void reload() {
		this.clearAll();
		halted = false;
		loader.finalStep();
	}
	
	public void setPeriod(int period) {
		timer.setDelay(period);
	}


	private void createAndShowGUI(){
		frame = new JFrame("Pippin simulator");
		Container content = frame.getContentPane();
		content.setLayout(new BorderLayout(1,1));
		content.setBackground(Color.BLACK);
		frame.setSize(800,600);
		JPanel centerPanel = new JPanel();
		centerPanel.setLayout(new GridLayout(1,0));
		centerPanel.add(new CodeViewPanel(this).createCodeDisplay(), BorderLayout.WEST);
		centerPanel.add(new DataViewPanel(this).createDataDisplay(), BorderLayout.EAST);
		frame.add(centerPanel, BorderLayout.CENTER);
		JMenuBar bar = new JMenuBar();
		frame.setJMenuBar(bar);
		frame.add(new ProcessorViewPanel(this).createProcessorDisplay(), BorderLayout.PAGE_START);
		frame.add(new ControlPanel(this).createControlDisplay(), BorderLayout.PAGE_END);
		MenuBarBuilder menuBuilder = new MenuBarBuilder(this);
		menuBuilder = new MenuBarBuilder(this);
		bar.add(menuBuilder.createMenu1());
		bar.add(menuBuilder.createMenu2());
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frame.addWindowListener(new ExitAdapter());
		//new javax.swing.Timer(period,  new TimerListener().start());
		timer = new Timer(INITIAL_PERIOD, new TimerListener());
		timer.start();
		callForUpdates(States.NOTHING_LOADED);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}

	public void setGUIMemory() { 
		memory = new GUIMemoryDecorator(this, memory); 
		iSet[0] = new NOP(this, memory); 
		iSet[1] = new LOD(this, memory); 
		iSet[2] = new LODI(this, memory); 
		iSet[3] = new STO(this, memory); 
		iSet[4] = new ADD(this, memory); 
		iSet[5] = new SUB(this, memory); 
		iSet[6] = new MUL(this, memory); 
		iSet[7] = new DIV(this, memory); 
		iSet[8] = new ADDI(this, memory); 
		iSet[9] = new SUBI(this, memory); 
		iSet[0xA] = new MULI(this, memory); 
		iSet[0xB] = new DIVI(this, memory); 
		iSet[0x10] = new AND(this, memory); 
		iSet[0x11] = new ANDI(this, memory); 
		iSet[0x12] = new NOT(this, memory); 
		iSet[0x13] = new CMPZ(this, memory); 
		iSet[0x14] = new CMPL(this, memory); 
		iSet[0x1A] = new JUMP(this, memory); 
		iSet[0x1B] = new JMPZ(this, memory); 
		iSet[0x1F] = new HALT(this, memory); 
		iSet[0x1C] = new ROT(this, memory);
	}


	public void step() {
		if(!halted) { 
			step1(); 
			callForUpdates(); 
		} 
	} 

	private void step1() { //used to be public void step()
		if(!halted) { 
			long lng; 
			Instruction instr = iSet[0x1F]; 
			try { 
				lng = memory.getCode(getProgramCounter()); 
				if (lng >= 0) { 
					long op = lng >> 32;
					int arg = (int)(lng ^ (op << 32));
					boolean indirect = false;
					if (op % 2 == 1) {
						indirect = true;
					}
					int opcode = (int)op/2;
					instr = iSet[opcode];
					setAccumulator(instr.execute(arg, indirect));
				} 
			} catch (CodeAccessException e) { 
				JOptionPane.showMessageDialog(null, 
						"There was a code access exception executing " + instr, 
						"Error on code line " + this.getProgramCounter(), 
						JOptionPane.WARNING_MESSAGE); 
				halt(); 

			} catch (DataAccessException e){
				JOptionPane.showMessageDialog(null, "There was a data access exception executing " + instr,
						"Error on code line " + this.getProgramCounter(),
						JOptionPane.WARNING_MESSAGE);

			} catch (NullPointerException e){
				JOptionPane.showMessageDialog(null, "There was a null pointer exception executing " + instr,
						"Error on code line " + this.getProgramCounter(),
						JOptionPane.WARNING_MESSAGE);
			} catch (DivideByZeroException e){
				JOptionPane.showMessageDialog(null, "There was a divide by zero exception executing " + instr,
						"Error on code line " + this.getProgramCounter(),
						JOptionPane.WARNING_MESSAGE);
			}
		} 
	} 

	public static void test1() throws DataAccessException, FileNotFoundException, CodeAccessException{
		Machine m = new Machine();
		MemoryInterface data = m.getMemory();
		Loader loader = new Loader();
		for(int i = 0; i < Memory.CODE_SIZE; i++) { // used to be interface
			data.setCode(i, -1); // an invalid code
		}   
		System.out.println(loader.load(data, new File("factorial8.pexe")));
		long lng = 0;
		Instruction instr = m.iSet[0x1F];
		do {
			lng = data.getCode(m.getProgramCounter());
			System.out.println(lng);
			if (lng >= 0) {
				long op = lng >> 32;
					int arg = (int)(lng ^ (op << 32));
					boolean indirect = false;
					if (op % 2 == 1) {
						indirect = true;
					}
					int opcode = (int)op/2;
					instr = m.iSet[opcode];
					m.setAccumulator(instr.execute(arg, indirect));
					System.out.println(instr.toString() + " " + arg);
					System.out.println("Memory: 0 => " + data.getData(0) 
							+ ", 32 => " + data.getData(32));
			}
		} while(!instr.toString().equals("HALT"));

	}

	public static void test2() throws DataAccessException, FileNotFoundException, 
	CodeAccessException { 
		Machine m = new Machine(); 
		m.frame = new JFrame(); 
		m.frame.setSize(300, 500); 
		DataViewPanel dvp = new DataViewPanel(m); 
		m.frame.add(dvp.createDataDisplay()); 
		m.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
		m.frame.setVisible(true); 

		Loader loader = new Loader();
		m.memory.clearCode(); 
		m.memory.clearData(); 
		System.out.println(loader.load(m.memory, new File("factorialIndirect7.pexe"))); 
		long lng = 0; 
		Instruction instr = m.iSet[0x1F]; 
		do { 
			try { 
				Thread.sleep(500); 
			} catch (InterruptedException e) {  
				e.printStackTrace(); 
			} 
			lng = m.memory.getCode(m.getProgramCounter()); 
			System.out.println(lng); 
			if (lng >= 0) { 
				long op = lng >> 32; 
					int arg = (int)(lng ^ (op << 32)); 
					boolean indirect = false; 
					if (op % 2 == 1) { 
						indirect = true; 
					} 
					int opcode = (int)op/2; 
					instr = m.iSet[opcode]; 
					m.setAccumulator(instr.execute(arg, indirect)); 
					System.out.println(instr.toString() + " " + arg); 
					System.out.println("Memory: 0 => " + m.memory.getData(0)
                            + ", 1 => " + m.memory.getData(1));
			} 
		} while(!m.halted); 
	}

	public static void test3() throws FileNotFoundException { 
		Machine m = new Machine(); 
		m.frame = new JFrame(); 
		m.frame.setSize(600, 500); 
		DataViewPanel dvp = new DataViewPanel(m); 
		m.frame.add(dvp.createDataDisplay(), BorderLayout.LINE_END); 
		CodeViewPanel cvp = new CodeViewPanel(m); 
		m.frame.add(cvp.createCodeDisplay(), BorderLayout.CENTER); 
		ProcessorViewPanel pvp = new ProcessorViewPanel(m);
		m.frame.add(pvp.createProcessorDisplay(), BorderLayout.PAGE_START);
		m.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
		m.frame.setVisible(true); 

		Loader loader = new Loader();
		m.memory.clearCode(); 
		m.memory.clearData();  
		System.out.println(loader.load(m.memory, new File("factorialIndirect7.pexe"))); 

		do { 
			try { 
				Thread.sleep(100); 
			} catch (InterruptedException e) { 
				e.printStackTrace(); 
			}
			m.step();
		} while(!m.halted); 
	}

	public static void main(String[] args) throws DataAccessException, FileNotFoundException, CodeAccessException {
		test1();
		//test2();
		//test3();
		/*javax.swing.SwingUtilities.invokeLater(new Runnable(){
			public void run() {
				new Machine();
			}
		});*/ 
	}
}