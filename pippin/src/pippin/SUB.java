package pippin;
/**
 * SUBTRACTION instruction: opcode 05
 * @author Jack
 *
 */
public class SUB extends Instruction {

    public SUB(Machine machine, Memory memory) {
        super(machine, memory);
    }
    
    /**
     * @param arg type X: memory address
     * indirect false) return accumulator minus the data value stored at data memory location X, increment the program counter.
     * (indirect true) use the value stored at data memory location X as the location of the data value X1 and return the accumulator minus X1, increment the program counter.
     */
    @Override
    public int execute(int arg, boolean indirect)
            throws DataAccessException {
        int retVal = getMachine().getAccumulator();
        if (indirect) {
            retVal -= getMemory().getData(getMemory().getData(arg));            
        }
        else {
            retVal -= getMemory().getData(arg);         
        }
        getMachine().incrementCounter();
        return retVal;
    }
}