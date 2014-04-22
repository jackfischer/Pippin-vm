package pippin;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Scanner;

public class Loader implements LoaderInterface{

	public String load(Memory mem, File file) throws FileNotFoundException {
		String message = "goodLoad";
		Scanner input = new Scanner(file);
		boolean inCode = true;
		try {
			int ip = 0;
			while (input.hasNextLong()){
				long inp = input.nextLong();
				if (inp == -1L){
					inCode = false;
				}
				if (inCode){
					mem.setCode(ip, inp);
					ip++;
				}
				else{
					int addr = (int)(inp >> 32);
					long temp = ((long)addr) << 32;
					int val = (int)(inp ^ temp);
					mem.setData(addr, val);
				}
			}
		} catch (CodeAccessException e) {
			message = "Failure loading code";
		} catch (DataAccessException e) {
			message = "Failure loading data";
		}
		input.close();
		return message;
	}

	public static void main(String[] args) throws FileNotFoundException {
		MemoryInterface m = new Memory();
		LoaderInterface ld = new Loader();
		for(int i = 0; i < MemoryInterface.CODE_SIZE; i++){
			try {
				m.setCode(i, -1); // an invalid code
			}
			catch (CodeAccessException e) {
				e.printStackTrace();
			}
		}
		String fileName = "test.pexe"; // CHANGE AS NEEDED
		System.out.println(ld.load((Memory) m, new File(fileName)));
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