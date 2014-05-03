package pippin;
/**
 * LOAD instruction, opcode 01
 * @author Sam
 */
public class LOD extends Instruction{
	public LOD(Machine machine, MemoryInterface memory){
		super(machine, memory);
	}
	
	/**
	 * @param arg type X: memory address
	 * (indirect false) return the data value stored at data memory location X.
	 * (indirect true) use the data value stored at data memory location X as the location of the data and return that data value.
	 * increment the program counter.
	 */
	@Override
	public int execute(int arg, boolean indirect) throws DataAccessException{
		int retVal;
		if(indirect){
			retVal = getMemoryInterface().getData(getMemoryInterface().getData(arg));
		}
		else{
			retVal = getMemoryInterface().getData(arg);
		}
		getMachine().incrementCounter();
		return retVal;
	}
}