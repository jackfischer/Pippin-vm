package pippin;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;

public class MenuBarBuilder implements Observer { // needs update method still..?
	private JMenuItem assemble = new JMenuItem("Assemble Source...");
	private JMenuItem load = new JMenuItem("Load Program...");
	private JMenuItem exit = new JMenuItem("Exit");
	private JMenuItem go = new JMenuItem("Go");
	private Machine machine;

	public MenuBarBuilder(Machine machine){
		this.machine = machine;
		machine.addObserver(this);
	}

	public JMenu createMenu1(){
		JMenu menu = new JMenu("File");
		menu.setMnemonic(KeyEvent.VK_F);
		assemble.setMnemonic(KeyEvent.VK_A);
		assemble.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A, ActionEvent.CTRL_MASK));
		assemble.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) { 
				machine.assembleFile(); 
			} 
		}); 
		menu.add(assemble);
		load.setMnemonic(KeyEvent.VK_L);
		load.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_L, ActionEvent.CTRL_MASK));
		load.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0){
				machine.loadFile();
			}
		});
		menu.add(load);
		menu.addSeparator();
		exit.setMnemonic(KeyEvent.VK_E);
		exit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_E, ActionEvent.CTRL_MASK));
		exit.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0){
				machine.exit();
			}
		});
		menu.add(exit);
		return menu;
	}
	

	public JMenu createMenu2(){
		JMenu menu = new JMenu("Execute");
		menu.setMnemonic(KeyEvent.VK_F);
		go.setMnemonic(KeyEvent.VK_X);
		go.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X, ActionEvent.CTRL_MASK));
		go.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0){
				machine.execute();
			}
		});
		menu.add(go);
		return menu;
	}

	public void checkEnabledMenus(){
		assemble.setEnabled(machine.getState().getAssembleFileActive());
		load.setEnabled(machine.getState().getAssembleFileActive());
		go.setEnabled(machine.getState().getAssembleFileActive());
	}

	@Override
	public void update(Observable arg0, Object arg1) {
		checkEnabledMenus();
	}

	


}
