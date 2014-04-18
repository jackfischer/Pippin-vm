package pippin;

public class STO extends Instruction{
	public STO(Machine machine, Memory memory){
		super(machine, memory);
	}
	
	@Override
	public int execute(int arg, boolean indirect) throws DataAccessException{
		int retVal = getMachine().getAccumulator();
		if(indirect){
			getMemory().setData(retVal, getMemory().getData(arg));
		}
		else{
			getMemory().setData(retVal, arg);
		}
		return retVal;
	}
}
