package pippin;
/**
 * AND INDIRECT instruction: opcode 11
 * @author Jack
 *
 */
public class ANDI extends Instruction{
    public ANDI (Machine machine, MemoryInterface memory){
		super(machine, memory);
	}
	
	/**
	 * @param arg type Z: data value (an integer)
	 * if the contents of the accumulator and Z are both non-zero, return 1 into the accumulator,
	 * else return 0 (ignore indirect),
	 * increment the program counter.
	 */
	@Override
	public int execute(int arg, boolean indirect) throws DataAccessException {
		int retVal;
		if (getMachine().getAccumulator() != 0 && arg !=0){
			retVal = 1;
		}
		else{
			retVal = 0;
		}
		getMachine().incrementCounter();
		return retVal;
	}
}
