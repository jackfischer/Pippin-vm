package pippin;
/**
 * ADDITION instruction: opcode 04
 * @author Lander
 */
public class ADD extends Instruction {

    public ADD(Machine machine, MemoryInterface memory) {
        super(machine, memory);
    }

    /**
     * @param arg type X: memory address
     * (indirect false) return accumulator plus the data value stored at data memory location X, increment the program counter.
     * (indirect true) use the value stored at data memory location X as the location of the data value X1 and return the accumulator plus X1, increment the program counter.
     */
    @Override
    public int execute(int arg, boolean indirect)
            throws DataAccessException {
        int retVal = getMachine().getAccumulator();
        if (indirect) {
            retVal += getMemoryInterface().getData(getMemoryInterface().getData(arg));            
        }
        else {
            retVal += getMemoryInterface().getData(arg);         
        }
        getMachine().incrementCounter();
        return retVal;      
    }
}