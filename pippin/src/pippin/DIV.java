package pippin;
/**
 * DIVISION instruction: opcode 07
 * @author Jack
 *
 */
public class DIV extends Instruction {
	public DIV (Machine machine, MemoryInterface memory){
		super(machine, memory);
	}
	
	/**
	 * @param arg type X: memory address
	 * (indirect false) return accumulator divided by the value stored at data memory location X, increment the program counter.
	 * (indirect true) use the value stored at data memory location X as the location of the data value X1 and return the accumulator divided by X1, increment the program counter.
	 * Division by 0 will throw DivideByZeroException
	 */
	@Override
	public int execute(int arg, boolean indirect) throws DataAccessException, DivideByZeroException{
		int retVal = getMachine().getAccumulator();
		
		if(indirect) {
			int arg1 = getMemoryInterface().getData(arg);
			if (getMemory().getData(arg1) == 0){
				throw new DivideByZeroException("attempt to divide by zero");
			}
			else{
				retVal /= getMemoryInterface().getData(arg1);
			}
		}
		else {
			if (getMemoryInterface().getData(arg) ==0){
				throw new DivideByZeroException("attempt to divide by zero");
			}
			else{
				retVal /= getMemoryInterface().getData(arg);
			}
		}	
		getMachine().incrementCounter();
		return retVal;
	}
}