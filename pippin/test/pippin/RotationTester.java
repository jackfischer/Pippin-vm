package pippin;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import org.junit.Before;
import org.junit.Test;

public class RotationTester {
	Machine machine = new Machine();
	Memory memory = new Memory();
	int[] dataCopy = new int[Memory.DATA_SIZE];
	int accInit;
	int ipInit;

	@Before
	public void setup() {
		for (int i = 0; i < Memory.DATA_SIZE; i++) {
			dataCopy[i] = -5*Memory.DATA_SIZE + 10*i;
			try {
				memory.setData(i, -5*Memory.DATA_SIZE + 10*i);
			} catch (DataAccessException e) {
				e.printStackTrace();
			}
			// Initially the memory will contain a known spread
			// of different numbers: 
			// -2560, -2550, -2540, ..., 0, 10, 20, ..., 2550 
			// This allows us to check that the instructions do 
			// not corrupt memory unexpectedly.
			// 0 is at index 256
		}
		accInit = 0;
		ipInit = 0;
	}
	
	@Test
	public void testROTBasics() throws DataAccessException {
		Instruction instr = new ROT(machine, memory);
		//Test intrinsic properties
		assertEquals("Name is ROT", "ROT", instr.toString());
		//test original memory is right
		assertEquals("Memory is rotated correctly", memory, instr.getMemoryInterface());
		
		//params within data
		
		
	}
	
	@Test
	public void testBasicRotation() throws DataAccessException {
		//Test basic rotation
		memory.setData(1, 5); //start is at 5
		memory.setData(2, 3); //numElems is 3
		memory.setData(3, 2); //numMoves is 2
		System.out.println(memory.getData(5));
		System.out.println(memory.getData(6));
		System.out.println(memory.getData(7));
		Instruction instr = new ROT(machine, memory);
		memory.setData(5, -2500);
		memory.setData(6, -2490);
		memory.setData(7, -2510);
		assertEquals("Basic rotation", memory, instr.getMemoryInterface());
	}
	
	@Test
	public void testNegativeRotation() throws DataAccessException {
		//Test basic rotation
		memory.setData(1, 5); //start is at 5
		memory.setData(2, 3); //numElems is 3
		memory.setData(3, 2); //numMoves is 2
		System.out.println(memory.getData(5));
		System.out.println(memory.getData(6));
		System.out.println(memory.getData(7));
		Instruction instr = new ROT(machine, memory);
		memory.setData(5, -2490);
		memory.setData(6, -2510);
		memory.setData(7, -2500);
		assertEquals("Basic rotation", memory, instr.getMemoryInterface());
	}
	
	@Test
	public void testNegativeArgError() throws DataAccessException {
		//Test detecting problems
		//test numElems is negative
		memory.setData(1, 5); //start is at 5
		memory.setData(2, -3); //numElems is -3
		memory.setData(3, 2); //numMoves is 2
		System.out.println(memory.getData(5));
		System.out.println(memory.getData(6));
		System.out.println(memory.getData(7));
		Instruction instr = new ROT(machine, memory);
		memory.setData(5, -2500);
		memory.setData(6, -2490);
		memory.setData(7, -2510);
		try {
			instr.execute(1, false);
			assert false;
		}
		catch(IllegalArgumentException e) {
			assert true;
		}
	}
	
	@Test
	public void testCannabalizationError() throws DataAccessException {
		//Test detecting problems
		//test numElems is negative
		memory.setData(1, 1); //start is at 5
		memory.setData(2, 3); //numElems is -3
		memory.setData(3, 2); //numMoves is 2
		System.out.println(memory.getData(5));
		System.out.println(memory.getData(6));
		System.out.println(memory.getData(7));
		Instruction instr = new ROT(machine, memory);
		memory.setData(5, -2500);
		memory.setData(6, -2490);
		memory.setData(7, -2510);
		try {
			instr.execute(1, false);
			assert false;
		}
		catch(DataAccessException e) {
			assert true;
		}
	}

}





