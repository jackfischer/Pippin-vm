package pippin;
/**
 * HALT instruction: opcode 1F
 * @author Sam
 *
 */
public class HALT extends Instruction{
	public HALT(Machine machine, Memory memory){
		super(machine, memory);
	}
	/**
	 * halt program execution; don't do anything more
	 * later a "running" boolean in the model will be set to false (when we have a model)
	 * do not change the program counter
	 */
	@Override
	public int execute(int arg, boolean indirect) throws DataAccessException{
		if (getMachine() != null) {
		      getMachine().halt();
		}
		return getMachine().getAccumulator();
	}
}
