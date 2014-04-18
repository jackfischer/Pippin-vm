//Warning
package pippin;

public class CMPZ extends Instruction{
	public CMPZ (Machine machine, Memory memory){
		super(machine, memory);
	}
	
	@Override
	public int execute(int arg, boolean indirect) throws DataAccessException {
		int retVal;
		if (getMemory().getData(arg) == 0)
			retVal = 1;
		else
			retVal = 0;
		getMachine().incrementCounter();

		return retVal;
	}
}
