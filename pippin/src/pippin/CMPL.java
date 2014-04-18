//Warning
package pippin;

public class CMPL extends Instruction{
	public CMPL (Machine machine, Memory memory){
		super(machine, memory);
	}
	
	@Override
	public int execute(int arg, boolean indirect) throws DataAccessException {
		int retVal;
		if (getMemory().getData(arg) < 0)
			retVal = 1;
		else
			retVal = 0;
		getMachine().incrementCounter();

		return retVal;
	}
}
