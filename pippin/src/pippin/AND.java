package pippin;
public class AND extends Instruction {

    public AND(Machine machine, Memory memory) {
        super(machine, memory);
    }

    @Override
    public int execute(int arg, boolean indirect)
            throws DataAccessException {
        int retVal = getMachine().getAccumulator();
        int operand = getMemory().getData(arg);
        if(retVal != 0 && operand != 0) {
            retVal = 1;
        } else {
            retVal = 0;
        }
        getMachine().incrementCounter();
        return retVal;      
    }
}