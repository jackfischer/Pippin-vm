package pippin;

import java.util.Collections;

public class ROT extends Instruction { 
 
	public ROT(Machine machine, MemoryInterface memoryInterface) { 
		super(machine, memoryInterface); 
	} 
 
	@Override 
	public int execute(int arg, boolean indirect) throws DataAccessException { 
		int retVal = getMachine().getAccumulator(); 
		// IF YOU DID NOT CHANGE getMemory TO getMemoryInterface A COUPLE OF WEEKS AGO, JUST 
		// USE getMemory IN THE CODE BELOW 
		if(indirect) {
			arg = getMachine().getMemory().getData(arg); 
		} 

		int start = getMachine().getMemory().getData(arg); 
		int numElems = getMachine().getMemory().getData(arg+1); 
		int numMoves = getMachine().getMemory().getData(arg+2); 

		/*
		ArrayList<Integer> list = new ArrayList<Integer>(); 
		// throw IllegalArgumentException if numElems < 0 // with message that the number of elements to rotate cannot be negative 
		if (numElems < 0)
			throw new IllegalArgumentException("Number of elements to rotate cannot be negative");
		else {
			for(int i = 0; i < numElems; i++) { 
				list.add(getMachine().getMemory().getData(start+i)); 
			} 
			Collections.rotate(list, numMoves); 
			for(int i = 0; i < numElems; i++) { 
				getMachine().getMemory().setData(start+i,list.get(i)); 
			} 
			getMachine().incrementCounter(); return retVal; 
		}
		*/
	}

} 
