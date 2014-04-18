package pippin;

public class NOP extends Instruction{
	public NOP(Machine machine, Memory memory){
		super(machine, memory);
	}
	
	@Override
	public int execute(int arg, boolean indirect) throws DataAccessException{
		getMachine().incrementCounter();
		return getMachine().getAccumulator();
	}
}
