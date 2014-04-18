package pippin;

public class MUL extends Instruction{
	public MUL(Machine machine, Memory memory) {
        super(machine, memory);
    }

    @Override
    public int execute(int arg, boolean indirect)
            throws DataAccessException {
        int retVal = getMachine().getAccumulator();
        if (indirect) {
            int arg1 = getMemory().getData(arg);
            retVal = retVal*(getMemory().getData(arg1)); 
        } else {
            retVal = retVal*(getMemory().getData(arg)); 
        }
        getMachine().incrementCounter();
        return retVal;
    }
}

