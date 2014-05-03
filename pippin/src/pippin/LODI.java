package pippin;
/**
 * LOAD INDIRECT instruction, opcode 02
 * @author Sam
 */
public class LODI extends Instruction{
	public LODI(Machine machine, MemoryInterface memory){
		super(machine, memory);
	}
	
	/**
	 * @param arg type Z: data value (an integer)
	 * return Z (ignore indirect), increment the program counter.
	 */
	@Override
	public int execute(int arg, boolean indirect) throws DataAccessException{
		getMachine().incrementCounter();
		return getMachine().getAccumulator();
	}
}