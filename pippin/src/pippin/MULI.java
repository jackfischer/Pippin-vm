package pippin;
/**
 * MULTIPLICATION INDIRECT instruction: opcode 0A
 * @author Jack
 *
 */
public class MULI extends Instruction{
	
	public MULI (Machine machine, MemoryInterface memory) {
		super(machine, memory);
	}
	
	/**
	 * @param arg type Z: data value (an integer)
	 * return the accumulator times Z (ignore indirect), increment the program counter.
	 */
	@Override
	public int execute(int arg, boolean indirect) throws DataAccessException{
			int retVal = arg*(getMachine().getAccumulator());
			getMachine().incrementCounter();
			return retVal;
		}
		
	}