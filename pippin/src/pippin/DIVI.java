package pippin;
/**
 * DIVISION INDIRECT instruction: opcode 0B
 * @author Lander
 *
 */
public class DIVI extends Instruction {

    public DIVI(Machine machine, MemoryInterface memory) {
        super(machine, memory);
    }
    
    /**
     * @param arg type Z: data value (an integer)
     * return the accumulator divided by Z (ignore indirect), increment the program counter. Division by 0 will throw DivideByZeroException
     */
    @Override
    public int execute(int arg, boolean indirect) throws DataAccessException {
        int retVal = getMachine().getAccumulator();
        if(arg == 0) {
            throw new DivideByZeroException("attempt to divide by zero");
        }
        retVal /= arg;          
        getMachine().incrementCounter();
        return retVal;      
    }
}