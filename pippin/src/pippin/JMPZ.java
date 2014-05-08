package pippin;
/**
 * JUMP-ZERO instruction: opcode 1B
 * @author Sam
 */
public class JMPZ extends Instruction{
	public JMPZ(Machine machine, MemoryInterface memory){
		super(machine, memory);
	}
	/**
	 * @param arg type X: memory address
	 * (indirect false) 
	 * 		if the accumulator contains 0, put X in the program counter, causing the instruction at location X to be the next instruction executed; 
	 * 		otherwise increment the program counter
	 * (indirect true) 
	 * 		if the accumulator contains 0, use the data value stored at data memory location X as the location to be copied to the program counter; 
	 * 		otherwise increment the program counter
	 */
	@Override
	public int execute(int arg, boolean indirect) throws DataAccessException{
		int retVal = getMachine().getAccumulator();
		if(indirect){
			if(retVal==0){
				getMachine().setProgramCounter(getMemoryInterface().getData(arg));
			}
			else{
				getMachine().incrementCounter();
			}
		}
		else{
			if(retVal==0){
				getMachine().setProgramCounter(arg);
			}
			else{
				getMachine().incrementCounter();
			}
		}
		return retVal;
	}
}
