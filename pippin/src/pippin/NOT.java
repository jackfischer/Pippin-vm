package pippin;
/**
 * NOT instruction: opcode 12
 * @author Jack
 */
public class NOT extends Instruction{
	public NOT (Machine machine, Memory memory){
		super(machine, memory);
	}
	
	/**
	 * if the accumulator contains 0, return 1, else return 0 (ignore indirect), increment the program counter.
	 */
	@Override
	public int execute(int arg, boolean indirect) throws DataAccessException{
		int retVal = 0;
		if (getMachine().getAccumulator() == 0){
			retVal = 1;
		}
		getMachine().incrementCounter();
		return retVal;
	}
}
