package pippin;

/**
 * Rotation Instruction
 * @author Samuel David Bravo, Daniel Romero, Jack Fischer
 *
 */
public class ROT extends Instruction { 
 
	public ROT(Machine machine, MemoryInterface memoryInterface) { 
		super(machine, memoryInterface); 
	} 
	
	/**
	 * ROT shifts the information of a group of memory locations down by a given distance, with the end elements shifted back to the top.
	 * For example, if the set of memory values to be shifted contained {1,2,3,4} and was to be shifted by a distance of 2 rotations,
	 * the same memory locations would contain {3,4,1,2}. Please note that uses the same memory locations and only rotates the values between them.
	 * 
	 * 
	 * ROT uses three parameters to perform this rotation: 
	 * - start: the first memory address of the group to be rotated
	 * - numElems: how many memory addresses will be rotated (this defines the length of the group).
	 * - numMoves: how many rotations will be performed. If positive,
	 * @param arg a memory address pointing to where the 3 parameters used in the ROT instruction are stored.
	 * If
	 * 
	 * @throws IllegalArgumentException 
	 * 
	 * 
	 *
	 * 
	 */
	@Override 
	public int execute(int arg, boolean indirect) throws DataAccessException { 
		int retVal = getMachine().getAccumulator(); 
		if(indirect) {
			arg = getMachine().getMemory().getData(arg);
		}

		int start = getMachine().getMemory().getData(arg); 
		int numElems = getMachine().getMemory().getData(arg+1); 
		int numMoves = getMachine().getMemory().getData(arg+2); 

		if(arg >= (start-2) && arg <= (start+numElems-1)){
			throw new IllegalArgumentException("The range of values to rotate contain the parameter locations.");
		}
		else if (numElems<0){
			throw new IllegalArgumentException("Number of elements to rotate cannot be negative");
		}
		else if (numElems>1 && numMoves!=0){
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