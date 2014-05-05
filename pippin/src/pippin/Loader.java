package pippin;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Loader implements LoaderInterface{ 

    public String load(MemoryInterface memory, File file) throws FileNotFoundException{ 
		String message = "goodLoad"; 
		Scanner input = new Scanner(file);
		boolean inCode = true; 
		try { 
			int ip = 0;
			while(input.hasNextLong()){ // added this condition because without it InputMismatchException was being thrown.
				long inp = input.nextLong();
				if(inp == -1L){
					inCode = false;
				}
				else if(inCode){
					//System.out.println("ip is: " + ip + " inp is: " + inp);
					memory.setCode(ip++, inp);
				}
				else{
					int addr = (int)(inp >> 32); 
					long temp = ((long)addr) << 32; 
					int val = (int)(inp ^ temp); 
					memory.setData(addr, val); 
				}
			} 
		}
		catch (CodeAccessException e) { 
			message = "Failure loading code"; 
		} 
		catch (DataAccessException e) { 
			message = "Failure loading data"; 
		}
		input.close(); 
		return message; 
	} 


	// test driver 
	public static void main(String[] args) throws FileNotFoundException, InputMismatchException { 
		MemoryInterface m = new Memory(); 
		LoaderInterface ld = new Loader(); 
		//Loader ld = new Loader();
		for(int i = 0; i < MemoryInterface.CODE_SIZE; i++) { 
			try { 
				m.setCode(i, -1); // an invalid code 
			} catch (CodeAccessException e) { 
				e.printStackTrace(); 
			} 
		} 

		String fileName = "factorial8.pexe"; // CHANGE AS NEEDED 
		System.out.println(ld.load(m, new File(fileName))); 

		// here we are testing what was put in Memory so it is not bad style 
		// to need casts to get at the special methods of Memory
		System.out.println(Arrays.toString(((Memory)m).getData()));
		
		for(long lng : ((Memory)m).getCode()) { 
			if (lng >= 0) { 
				long op = lng >> 32; 
					int arg = (int)(lng ^ (op << 32)); 
					boolean indirect = false; 
					if (op % 2 == 1) { 
						indirect = true; 
					} 
					int opcode = (int)op/2; 
					System.out.println(opcode + " " + indirect + " " + arg); 
			} 
		} 
	} 
} 
