package pippin;
/**
 * AND instruction: opcode 10
 * @author Lander
 *
 */
public class AND extends Instruction {

    public AND(Machine machine, MemoryInterface memory) {
        super(machine, memory);
    }
    
    /**
     * @param arg type X: memory address
     * if the contents of the accumulator and the data value stored at data memory location X are both non-zero, return 1,
     * else return 0 (ignore indirect),
     * increment the program counter.
     */
    @Override
    public int execute(int arg, boolean indirect)
            throws DataAccessException {
        int retVal = getMachine().getAccumulator();
        int operand = getMemoryInterface().getData(arg);
        if(retVal != 0 && operand != 0) {
            retVal = 1;
        }
        else {
            retVal = 0;
        }
        getMachine().incrementCounter();
        return retVal;      
    }
}