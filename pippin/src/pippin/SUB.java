package pippin;

public class SUB extends Instruction {

    public SUB(Machine machine, Memory memory) {
        super(machine, memory);
    }

    @Override
    public int execute(int arg, boolean indirect)
            throws DataAccessException {
        int retVal = getMachine().getAccumulator();
        if (indirect) {
            int arg1 = getMemory().getData(arg);
            retVal -= getMemory().getData(arg1);            
        } else {
            retVal -= getMemory().getData(arg);         
        }
        getMachine().incrementCounter();
        return retVal;
    }
}