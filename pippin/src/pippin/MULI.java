//Warning

package pippin;

public class MULI extends Instruction{
	
	public MULI (Machine machine, Memory memory) {
		super(machine, memory);
	}
	
	@Override
	public int execute(int arg, boolean indirect) throws DataAccessException{
			int retVal = arg*(getMachine().getAccumulator());
			getMachine().incrementCounter();
			return retVal;
		}
		
	}


