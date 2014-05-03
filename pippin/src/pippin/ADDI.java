package pippin;
/**
 * ADDITION INDIRECT instruction: opcode 08
 * @author Lander
 *
 */
public class ADDI extends Instruction {

    public ADDI(Machine machine, MemoryInterface memory) {
        super(machine, memory);
    }
    
    /**
     * @param arg type Z: data value (an integer)
     * return the accumulator plus Z (ignore indirect), increment the program counter.
     */
    @Override
    public int execute(int arg, boolean indirect)
            throws DataAccessException {
        int retVal = getMachine().getAccumulator() + arg;           
        getMachine().incrementCounter();
        return retVal;      
    }
}