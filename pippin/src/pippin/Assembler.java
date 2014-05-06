package pippin;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

public class Assembler{

    static Set<String> allowsIndirect = new HashSet<String>(); 
	static Set<String> noArgument = new HashSet<String>(); 
	static Map<String, Integer> opcode = new HashMap<String, Integer>(); 

	static { 
		allowsIndirect.add("LOD"); 
		allowsIndirect.add("STO"); 
		allowsIndirect.add("JUMP");
		allowsIndirect.add("JMPZ");
		allowsIndirect.add("ADD");
		allowsIndirect.add("SUB");
		allowsIndirect.add("MUL");
		allowsIndirect.add("DIV");

		// [DONE] complete with all the other instructions that allow indirect addressing 

		noArgument.add("HALT"); 
		noArgument.add("NOP");
		noArgument.add("NOT");

		// [DONE]complete with all the other instructions that have no argument

		opcode.put("NOP", 0);
		opcode.put("LOD", 1); 
		opcode.put("LODI", 2); 
		opcode.put("STO", 3); 
		opcode.put("ADD", 4);
		opcode.put("SUB", 5);
		opcode.put("MUL", 6);
		opcode.put("DIV", 7);
		opcode.put("ADDI", 8);
		opcode.put("SUBI", 9);
		opcode.put("AND", 0x10);
		opcode.put("ANDI", 0x11);
		opcode.put("NOT", 0x12);
		opcode.put("CMPZ", 0x13);
		opcode.put("CMPL", 0x14);
		opcode.put("MULI", 0xA);
		opcode.put("DIVI", 0xB);
		opcode.put("JUMP", 0x1A);
		opcode.put("JMPZ", 0x1B);
		opcode.put("HALT", 0x1F);

		// [DONE] complete with all the other instructions and their opcodes 
	} 

	public String assemble(File input, File output) { 
		// note that input and output are the parameters to this method 
		boolean goodProgram = false; // will be used often and at the end of method 
		String message = "goodProgram"; 
		try { 
			goodProgram = true;
			Scanner inp = new Scanner(input);
			PrintWriter outp = new PrintWriter(output);
			boolean blankLineHit = false;
			boolean inCode = true;
			int lineCounter = 0;
			while(inp.hasNextLine() && goodProgram){ // changed to !goodProgram instead of goodProgram and IllegalStateException no longer thrown.
				lineCounter += 1;
				String rawInput = inp.nextLine();
				String line = rawInput.trim();
				System.out.println(line);
				if(blankLineHit && line.length() > 0) { //used to be line.isEmpty()
					goodProgram = false;
					message = "There is a blank line in the " 
							+ "program on line " + (lineCounter - 1);
				}
				else if(line.length() == 0){ //used to be line.isEmpty()
					blankLineHit = true;
				}
				else if(Character.isWhitespace(rawInput.charAt(0))){
					goodProgram = false;
					message = "Illegal line starts with White Space " 
							+ "on line " + (lineCounter - 1);
				} else {
					if (!line.startsWith("DATA")) { 
						if(!inCode) { 
							goodProgram = false; 
							message = "There is more code after data began " 
									+ "on line " + (lineCounter);
						}
						else{
							String[] parts = line.trim().split("\\s+");
							if(parts.length == 1){
								if(noArgument.contains(parts[0])){
									long temp = 2*opcode.get(parts[0]); 
									temp = temp << 32;  
									outp.println(temp);
									System.out.println(temp);
								}
								else{
									goodProgram = false;
									message = "Illegal mnemonic or missing argument on line " + lineCounter;
								}
							} else if(parts.length == 2){
								boolean indirect = false;
								int arg = 0;
								String mnemonic = parts[0];
								if(mnemonic.endsWith("#")){
									indirect = true;
									mnemonic = mnemonic.substring(0, mnemonic.length() - 1);
								}
								if(!opcode.containsKey(mnemonic)){
									goodProgram = false;
									message = "Illegal mnemonic on line " + lineCounter;
								}
								if(indirect && !allowsIndirect.contains(mnemonic)){
									goodProgram = false;
									message = "Instruction cannot be indirect on line " + lineCounter;
								}
								if(goodProgram){
									try {
										arg = Integer.parseInt(parts[1], 16);
									}
									catch (NumberFormatException e){
										goodProgram = false;
										message = "Argument not a hexadecimal number on line " + lineCounter;
									}
								}
								if(goodProgram){
									int opc = 2*opcode.get(mnemonic); 
									if(indirect) { 
										opc++; 
									} 
									long otemp = opc; 
									long atemp = arg; 
									otemp = otemp << 32; 
									atemp = atemp & 0x00000000FFFFFFFFL; 
									otemp = otemp | atemp; 
									outp.println(otemp);
									System.out.println(otemp);
								}
							}
							else if(parts.length > 2){
								goodProgram = false;
								message = "Too many arguments or other error on line " + lineCounter;
							}
						}
					}
					else{ //line starts with DATA
						if(goodProgram){
							if(inCode){
								outp.println(-1L); // output a separator
								inCode = false; 
							}
								String[] parts2 = line.trim().split("\\s+");
								if(parts2.length == 1){
									goodProgram = false;
									message = "No data on line " + lineCounter;
								}
								else if (parts2.length == 2){
									goodProgram = false;
									message = "No data value on line " + lineCounter; 
								}
								else if (parts2.length > 3){
									goodProgram = false;
									message = "Too many data values on line " + lineCounter;
								}
								else{
									int addr = 0; 
									int val = 0; 
									try { 
										addr = Integer.parseInt(parts2[1], 16); 
										val = Integer.parseInt(parts2[2], 16); 
									} catch(NumberFormatException e) { 
										goodProgram = false; 
										message = "Address or value not a hexadecimal number on line " + lineCounter; 
									} 
									if(goodProgram) { 
										long atemp = addr; 
										//System.out.println(atemp); 
										atemp = atemp << 32; 
										//System.out.println(atemp); 
										long vtemp = val; 
										vtemp = vtemp & 0x00000000FFFFFFFFL; 
										//System.out.println(vtemp); 
										atemp = atemp | vtemp; 
										//System.out.println(atemp);
										outp.println(atemp);
										System.out.println(atemp);
									} 
								} 
							} 
						} 
					}
				} 
			inp.close(); 
			outp.close(); 

		} catch (IOException e){ 
			message = "Unable to open the necessary files"; 
		} 

		if(!goodProgram && output != null && output.exists()) { 
			output.delete(); 
		} 
		return message; 
	}
	// Here is a very simple tester. You can change the name of the test file. 

	public static void main(String[] args) { 
		Assembler ass = new Assembler();
		String fileName = "factorial8"; // CHANGE AS NEEDED FOR TESTING
		System.out.println(fileName);
		System.out.println(ass.assemble(new File(fileName + ".pasm"), 
				new File(fileName + ".pexe"))); 
	}
}
