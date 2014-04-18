//Warning

package pippin;

public class DIV extends Instruction {
	public DIV (Machine machine, Memory memory){
		super(machine, memory);
	}
	
	@Override
	public int execute(int arg, boolean indirect) throws DataAccessException, DivideByZeroException{
		int retVal = getMachine().getAccumulator();

		if(indirect) {
			int arg1 = getMemory().getData(arg);
			if (getMemory().getData(arg1) == 0)
				throw new DivideByZeroException();
			else
			retVal = retVal / getMemory().getData(arg1);
		}
			else {
				if (getMemory().getData(arg) ==0)
					throw new DivideByZeroException();
				else
				retVal = retVal / getMemory().getData(arg);
			}	
		getMachine().incrementCounter();
		return retVal;
	}
}
