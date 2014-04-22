package pippin;
/**
 * STORE instruction, opcode 03
 * @author Sam
 */
public class STO extends Instruction{
	public STO(Machine machine, Memory memory){
		super(machine, memory);
	}
	
	/**
	 * @param arg type X: memory address
	 * (indirect false) store the contents of the accumulator in data memory location X.
	 * (indirect true) use the data value stored at data memory location X as the location of where to store the contents of the accumulator
	 * increment the program counter
	 */
	@Override
	public int execute(int arg, boolean indirect) throws DataAccessException{
		int retVal = getMachine().getAccumulator();
		if(indirect){
			getMemory().setData(retVal, getMemory().getData(arg));
		}
		else{
			getMemory().setData(retVal, arg);
		}
		getMachine().incrementCounter();
		return retVal;
	}
}
