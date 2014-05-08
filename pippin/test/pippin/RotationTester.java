package pippin;

import static org.junit.Assert.*;

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
	public void testROTbasics(){
		Instruction instr = new ROT(machine, memory);
		//Test intrinsic properties
		assertEquals("Name is ROT", "ROT", instr.toString());
	}

}
