package pippin;

/**
 * SUBTRACTION INDIRECT instruction: opcode 09
 * @author Jack
 *
 */
public class SUBI extends Instruction {

    public SUBI(Machine machine, MemoryInterface memory) {
        super(machine, memory);
    }
    
    /**
     * @param arg type Z: data type (an integer)
     * return the accumulator minus Z (ignore indirect), increment the program counter.
     */
    @Override
    public int execute(int arg, boolean indirect)
            throws DataAccessException {
        int retVal = getMachine().getAccumulator() - arg;           
        getMachine().incrementCounter();
        return retVal; 
    }
}

