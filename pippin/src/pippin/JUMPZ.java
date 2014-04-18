package pippin;

public class JUMPZ extends Instruction{
	public JUMPZ(Machine machine, Memory memory){
		super(machine, memory);
	}
	
	@Override
	public int execute(int arg, boolean indirect) throws DataAccessException{
		int retVal = getMachine().getAccumulator();
		if(indirect){
			if(retVal==0){
				getMachine().setProgramCounter(arg);
			}
			else{
				getMachine().incrementCounter();
			}
		}
		else{
			if(retVal==0){
				getMachine().setProgramCounter(getMemory().getData(arg));
			}
			else{
				getMachine().incrementCounter();
			}
		}
		return retVal;
	}
}
