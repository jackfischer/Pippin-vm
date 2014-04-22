package pippin;
/**
 * NO-OPERATION instruction: opcode 00
 * @author Sam
 */
public class NOP extends Instruction{
	public NOP(Machine machine, Memory memory){
		super(machine, memory);
	}
	
	/**
	 * no operation, do nothing but increment the program counter
	 */
	@Override
	public int execute(int arg, boolean indirect) throws DataAccessException{
		getMachine().incrementCounter();
		return getMachine().getAccumulator();
	}
}
