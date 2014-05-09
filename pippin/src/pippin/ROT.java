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
	 * @param indirect a boolean which, if true, means that arg is a memory address to another address that points to the 3 parameters.
	 * @return retVal as with other instructions, ROT returns the accumulator.
	 * 
	 * There are various conditions that the method deals with:
	 * @throws IllegalArgumentException If the number of elements to rotate is negative, or if set to be rotated includes the parameters.
	 * If numElems is 0 or 1, or if the number of moves is 0, then these are trivial conditions, and the process is skipped.
	 * If numMoves is greater than numElems, then we can increase efficiency by making numMoves = numMoves % numElems.
	 * 
	 * The method has an inner and outer loop.
	 * The inner loop shifts all the elements in the set up/down once, and the outer loop repeats this process numMoves times.
	 * However, the inner loop requires that one of the elements be copied in a temporary memory location, and then placed in its correct position.
	 * Here's an example:
	 * 		assume we are rotating {1,2,3,4} -1 times. The inner loop takes (start+j+1) and stores it in (start+j).
	 * 		We move {2,2,3,4}, {2,3,3,4}, {2,3,4,4}. However, {1} has been lost. So {1} is stored at another memory location temporarily.
	 * 		So, for negative rotations, the first element is stored temporarily and lastly placed at the end of the set.
	 * This process is also necessary for positive rotations:
	 * 		assume we are rotating {1,2,3,4} +1 times. The inner loop starts from the end of the set, else we would end up with this error: {1,1,1,1}
	 * 		It takes (start+numElems-1)-j-1 and stores it in (start+numElems-1)-j.
	 * 		We move {1,2,3,3}, {1,2,2,3}, {1,1,2,3}. Since {4} has been lost, it has to be stored in the temporary memory location.
	 * 		For positive rotations, the last element is stored temporarily and lastly placed at the start of the set.
	 * 
	 * ROT calls the method findAnEmptySpace() before starting any rotation process.
	 * FindAnEmptySpace() will return the first NO-OP location in memory.
	 * This memory location is used as the temp that was necessary above throughout the operation.
	 * When the rotation process is finished, this memory location is changed back into a NO-OP.
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