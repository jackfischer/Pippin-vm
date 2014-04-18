package pippin;

public class LOD extends Instruction{
	public LOD(Machine machine, Memory memory){
		super(machine, memory);
	}
	
	@Override
	public int execute(int arg, boolean indirect) throws DataAccessException{
		int retVal;
		if(indirect){
			retVal = getMemory().getData(getMemory().getData(arg));
		}
		else{
			retVal = getMemory().getData(arg);
		}
		getMachine().incrementCounter();
		return retVal;
	}
}
