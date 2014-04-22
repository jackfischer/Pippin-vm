package pippin;
/**
 * JUMP instruction: opcode 1A
 * @author Sam
 * 
 */
public class JUMP extends Instruction{
	public JUMP(Machine machine, Memory memory){
		super(machine, memory);
	}
	/**
	 * @param arg type X: memory address
	 * (indirect false) put X in the program counter, causing the instruction at location X to be the next instruction executed
	 * (indirect true) use the data value stored at data memory location X as the location to be copied to the program counter
	 */
	@Override
	public int execute(int arg, boolean indirect) throws DataAccessException{
		int retVal = getMachine().getAccumulator();
		if(indirect){
			getMachine().setProgramCounter(getMemory().getData(arg));
		}
		else{
			getMachine().setProgramCounter(arg);
		}
		return retVal;
	}
}
