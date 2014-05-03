package personal_pippin;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

public class Assembler implements AssemblerInterface{
    
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
		opcode.put("AND", 10);
		opcode.put("ANDI", 11);
		opcode.put("NOT", 12);
		opcode.put("CMPZ", 13);
		opcode.put("CMPL", 14);
		opcode.put("MULI", 0x0A);
		opcode.put("DIVI", 0x0B);
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
			while(inp.hasNextLine() && !goodProgram){ // changed to !goodProgram instead of goodProgram and IllegalStateException no longer thrown.
				lineCounter += 1;
				String rawInput = inp.nextLine();
				String line = rawInput.trim();
				if(line.isEmpty()){
					blankLineHit = true;
					goodProgram = false;
					message = "There is a blank line in the " 
							+ "program on line " + (lineCounter - 1);
				}
				else if(Character.isWhitespace(rawInput.charAt(0))){
					goodProgram = false;
					message = "Illegal line starts with White Space " 
							+ "on line " + (lineCounter - 1);
				}
				if (!line.startsWith("DATA")) { 
					if(!inCode) { 
						goodProgram = false; 
						message = "There is more code after data began " 
								+ "on line " + (lineCounter);
					}
					else{
						String[] parts = line.trim().split("\\s+");

						//\ change goodProgram to true, make a Scanner inp to read the file input, 
						//\make a PrintWriter outp to write to the file output 
						//\ make a boolean blankLineHit, initially false to keep track of the first time we hit a blank line 
						//\ make a boolean inCode initially true to keep track that we are in code, not data 
						//\ make an int lineCounter, initially 0. 
						//\ while(inp.hasNextLine() && goodProgram) 
						//\ add 1 to lineCounter, get the String rawInput using inp.nextLine() 
						//\ make the String line and set it equal to rawInput.trim(), which removes any white space at the front and end of rawInput 
						// check for a blank line in the code, else if the current line is blank, 
						// set blankLineHit to true, else check if the first character on the line is 
						// white-space, 
						// else do the rest of the processing 
						// NOTES: (blankLineHit && line.length() > 0) tells you that the previous 
						// line was blank. In this case do the following: 
						// goodProgram = false; 
						// message = "There is a blank line in the " 
						//		+ "program on line " + (lineCounter - 1); 
						// (line.length() == 0) tells you that a line is blank 
						// Character.isWhitespace(rawInput.charAt(0)) tells you that the first char 
						// is white-space. In this case do the following: 
						// goodProgram = false; 
						// message = "Illegal line starts with White Space " 
						//		+ "on line " + (lineCounter - 1); 
						// Now we are in the else part of the previous tests: 
						// To generate an error if we are back in code after seeing some DATA 
						// use 
						// if (!line.startsWith("DATA")) { 
						//	if(!inCode) { 
						//		goodProgram = false; 
						//		message = "There is more code after data began " 
						//				+ "on line " + (lineCounter); 
						// else we can really process an instruction line: 
						// break the line into pieces using 
						// String[] parts = line.trim().split("\\s+"); 
						// now there are cases: 
						if(parts.length == 1){
							if(noArgument.contains(parts[0])){
								long temp = 2*opcode.get(parts[0]); 
								temp = temp << 32;  
								outp.println(temp);
							}
						}
						else{
							goodProgram = false;
							message = "Illegal mnemonic or missing argument on line " + lineCounter;
						}
						if(parts.length == 2){
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
							}
						}
						if(parts.length > 2){
							goodProgram = false;
							message = "Too many arguments or other error on line " + lineCounter;
						}
						else{
							if(goodProgram){
								inCode = false; 
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
									} 
								} 
							} 
						} 
					} 
				}
				inp.close(); 
				outp.close(); 
			}
		} catch (IOException e){ 
			message = "Unable to open the necessary files"; 
		} 
		if(!goodProgram && output != null && output.exists()) { 
			output.delete(); 
		} 
		return message; 
	} 


	// if the parts array has length 1, check the mnemonic 
	// does not take an argument using noArgument.contains(parts[0]) 
	// if that is true, the following outputs the correct code: 
	// long temp = 2*opcode.get(parts[0]); 
	// temp = temp << 32; 
	// temp = -temp; 
	// outp.println(temp); 
	// else set goodProgram to false and set message to 
	// "Illegal mnemonic or missing argument on line " + lineCounter; 
	// if the parts array has length 2, check the mnemonic 
	// introduce a Boolean indirect set to false and an int arg set 
	// to 0. 
	// String mnemonic = parts[0]; 
	// Use mnemonic.endsWith("#") to test for an indirect instruction 
	// If this test is true, set indirect to true and use the 
	// substring method to remove the last char from mnemonic 
	// Now for some error checks: 
	// Use !opcode.containsKey(mnemonic) 
	// to set goodProgram to false, with the message 
	// "Illegal mnemonic on line " + lineCounter; 
	// Use indirect && !allowsIndirect.contains(mnemonic) to set 
	// goodProgram to false and message to 
	// "Instruction cannot be indirect on line " + lineCounter; 
	// if(goodProgram) use a try/catch for NumberFormatException 
	// to read arg using Integer.parseInt(parts[1], 16) 
	// if there is an exception, change goodProgram to false and 
	// message to 
	// "Argument not a hexadecimal number on line " + lineCounter; 
	// if(goodProgram) create the output using: 
	// int opc = 2*opcode.get(mnemonic); 
	// if(indirect) { 
	//	opc++; 
	// } 
	// long otemp = opc; 
	// long atemp = arg; 
	// otemp = otemp << 32; 
	// atemp = atemp & 0x00000000FFFFFFFFL; 
	// otemp = otemp | atemp; 
	// otemp = -otemp; 
	// outp.println(otemp); 
	//} 
	// if the parts array has length > 2, change goodProgram to false 
	// and message to 
	// "Too many arguments or other error on line " + lineCounter; 
	// ELSE – this is the else of (!line.startsWith("DATA")), so this is a 
	// DATA line 
	// if(goodProgram) 
	// inCode = false; 
	// String[] parts = line.trim().split("\\s+"); 
	// Now if parts has length 1 (it is only “DATA”) set goodProgram to 
	// false and message to 
	// "No data on line " + lineCounter; 
	// if parts has length 2, goodProgram is also false and the message is 
	// "No data value on line " + lineCounter; 
	// if parts has length > 3, , goodProgram is false and the message is 
	// "Too many data values on line " + lineCounter; 
	// else this is a correct line and we do the following 
	// int addr = 0; 
	//int val = 0; 
	//try { 
	//	addr = Integer.parseInt(parts[1], 16); 
	//	val = Integer.parseInt(parts[2], 16); 
	// } catch(NumberFormatException e) { 
	//	goodProgram = false; 
	//	message = "Address or value not a hexadecimal number on line " + lineCounter; 
	// } 
	// if(goodProgram) { 
	//	long atemp = addr; 
	//System.out.println(atemp); 
	//	atemp = atemp << 32; 
	//System.out.println(atemp); 
	//	long vtemp = val; 
	//	vtemp = vtemp & 0x00000000FFFFFFFFL; 
	//System.out.println(vtemp); 
	//	atemp = atemp | vtemp; 
	//System.out.println(atemp);
	//	outp.println(atemp); 
	// } 
	// } 
	// } 
	// } 
	// } 
	//} 
	// here is the rest of the code to close files, clean up and return the resulting message 
	// inp.close(); 
	// outp.close(); 
	// } catch (IOException e){ 
	//	message = "Unable to open the necessary files"; 
	// } 
	// if(!goodProgram && output != null && output.exists()) { 
	//	output.delete(); 
	// } 
	// return message; 

	//} 

	// Here is a very simple tester. You can change the name of the test file. 

	public static void main(String[] args) { 
		//AssemblerInterface ass = new Assembler(); 
		Assembler ass = new Assembler();
		String fileName = "factorial8"; // CHANGE AS NEEDED FOR TESTING 
		System.out.println(ass.assemble(new File(fileName + ".pasm"), 
				new File(fileName + ".pexe"))); 
	}
}
