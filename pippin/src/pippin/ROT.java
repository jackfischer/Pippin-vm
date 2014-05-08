package pippin;

public class ROT extends Instruction { 
 
	public ROT(Machine machine, MemoryInterface memoryInterface) { 
		super(machine, memoryInterface); 
	} 
 
	@Override 
	public int execute(int arg, boolean indirect) throws DataAccessException { 
		int retVal = getMachine().getAccumulator(); 
		if(indirect) {
			arg = getMachine().getMemory().getData(arg);
		}

		int start = getMachine().getMemory().getData(arg); 
		int numElems = getMachine().getMemory().getData(arg+1); 
		int numMoves = getMachine().getMemory().getData(arg+2); 

		if (numElems>1 && numMoves!=0){
			if (Math.abs(numMoves)>numElems){
				numMoves %= numElems;
			}
			int temp = findAnEmptySpace();
			for (int i = 0; i < Math.abs(numMoves); i++){
				if (numMoves>0) {
					getMachine().getMemory().setData(temp, getMachine().getMemory().getData(start + numElems -1));
					for (int j = 0; j < numElems; j++){
						getMachine().getMemory().setData((start+numElems-1)-j, getMachine().getMemory().getData((start+numElems)-j-1));

					}
					getMachine().getMemory().setData(start, getMachine().getMemory().getData(temp));
				}
				else{
					getMachine().getMemory().setData(temp, getMachine().getMemory().getData(start));
					for (int j = 0; j < numElems; j++){
						getMachine().getMemory().setData(start+j, getMachine().getMemory().getData(start+j+1));
					}
					getMachine().getMemory().setData(start+numElems-1,getMachine().getMemory().getData(temp));
				}
			}
			getMachine().getMemory().setData(temp, 0);
		}
		else{
			throw new IllegalArgumentException("Number of elements to rotate cannot be negative");
		}
		
		getMachine().incrementCounter();
		return retVal;
	}

	private int findAnEmptySpace() throws DataAccessException {
		int location = 0;
		boolean found = false;
		while(!found){
			if (getMachine().getMemory().getData(location) == 0){
				found = true;
			}
			else{
				location ++;
			}
		}
		return location;
	}

} 