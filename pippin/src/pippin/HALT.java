package pippin;

public class HALT extends Instruction{
	public HALT(Machine machine, Memory memory){
		super(machine, memory);
	}
	
	@Override
	public int execute(int arg, boolean indirect) throws DataAccessException{
//	model.running = false;
		return 0;
	}
}
