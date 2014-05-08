package pippin;

public class HALT extends Instruction{
	public HALT(Machine machine, MemoryInterface memory){
		super(machine, memory);
	}

	@Override
	public int execute(int arg, boolean indirect) throws DataAccessException{
//	model.running = false;
		getMachine().halt();
		return getMachine().getAccumulator();
	}
}
