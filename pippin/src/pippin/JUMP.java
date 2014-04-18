package pippin;

public class JUMP extends Instruction{
	public JUMP(Machine machine, Memory memory){
		super(machine, memory);
	}
	
	@Override
	public int execute(int arg, boolean indirect) throws DataAccessException{
		int retVal = getMachine().getAccumulator();
		if(indirect){
			getMachine().setProgramCounter(getMemory().getData(arg));
		}
		else{
			getMachine().setProgramCounter(arg);
		}
		getMachine().incrementCounter();
		return retVal;
	}
}
