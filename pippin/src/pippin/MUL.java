package pippin;
/**
 * MULTIPLICATION instruction: opcode 06
 * @author Jack
 *
 */
public class MUL extends Instruction{
	public MUL(Machine machine, Memory memory) {
        super(machine, memory);
    }
	
	/**
	 * @param arg type X: memory address
	 * (indirect false) return accumulator times the data value stored at data memory location X, increment the program counter.
	 * (indirect true) use the value stored at data memory location X as the location of the data value X1 and return the accumulator times X1, increment the program counter.
	 */
    @Override
    public int execute(int arg, boolean indirect)
            throws DataAccessException {
        int retVal = getMachine().getAccumulator();
        if (indirect) {
            retVal *= (getMemory().getData(getMemory().getData(arg))); 
        }
        else {
            retVal *= (getMemory().getData(arg)); 
        }
        getMachine().incrementCounter();
        return retVal;
    }
}

