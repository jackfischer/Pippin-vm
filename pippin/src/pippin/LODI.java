package pippin;

public class LODI extends Instruction{
	public LODI(Machine machine, Memory memory){
		super(machine, memory);
	}
	
	@Override
	public int execute(int arg, boolean indirect) throws DataAccessException{
		getMachine().incrementCounter();
		return arg;
	}
}
