package pippin;

public class SUBI extends Instruction {

    public SUBI(Machine machine, Memory memory) {
        super(machine, memory);
    }

    @Override
    public int execute(int arg, boolean indirect)
            throws DataAccessException {
        int retVal = getMachine().getAccumulator() - arg;           
        getMachine().incrementCounter();
        return retVal; 
    }
}

