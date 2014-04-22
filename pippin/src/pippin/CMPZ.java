package pippin;
/**
 * COMPARE ZERO instruction: opcode 13
 * @author Jack
 *
 */
public class CMPZ extends Instruction{
	public CMPZ (Machine machine, Memory memory){
		super(machine, memory);
	}
	
	/**
	 * @param arg type X: memory address
	 * if the data value stored at data memory location X is 0, return 1, 
	 * else return 0 (ignore indirect), 
	 * increment the program counter.
	 */
	@Override
	public int execute(int arg, boolean indirect) throws DataAccessException {
		int retVal = 0;
		if (getMemory().getData(arg) == 0){
			retVal = 1;
		}
		getMachine().incrementCounter();
		return retVal;
	}
}
