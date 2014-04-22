package pippin;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

public class Assembler implements AssemblerInterface {
	static Set<String> allowsIndirect = new HashSet<String>();
	static Set<String> noArgument = new HashSet<String>();
	static Map<String, Integer> opcode = new HashMap<String, Integer>();
	static {
		allowsIndirect.add("ADD");
		allowsIndirect.add("DIV");
		allowsIndirect.add("JMPZ");
		allowsIndirect.add("JUMP");
		allowsIndirect.add("LOD");
		allowsIndirect.add("MUL");
		allowsIndirect.add("STO");
		allowsIndirect.add("SUB");

		noArgument.add("HALT");
		noArgument.add("NOP");
		noArgument.add("NOT");

		opcode.put("NOP", 	0x00);
		opcode.put("LOD", 	0x01);
		opcode.put("LODI",	0x02);
		opcode.put("STO",	0x03);
		opcode.put("ADD",	0x04);
		opcode.put("SUB",	0x05);
		opcode.put("MUL",	0x06);
		opcode.put("DIV",	0x07);
		opcode.put("ADDI",	0x08);
		opcode.put("SUBI",	0x09);
		opcode.put("MULI",	0x0A);
		opcode.put("DIVI",	0x0B);
		opcode.put("AND",	0x10);
		opcode.put("ANDI",	0x11);
		opcode.put("NOT",	0x12);
		opcode.put("CMPZ",	0x13);
		opcode.put("CMPL",	0x14);
		opcode.put("JUMP",	0x1A);
		opcode.put("JMPZ",	0x1B);
		opcode.put("HALT",	0x1F);
	}

	public String assemble(File input, File output){
		boolean goodProgram = false;			
		String message = new String();

		try {
			goodProgram = true;
			Scanner inp = new Scanner(input);
			PrintWriter outp = new PrintWriter(output);	
			boolean blankLineHit = false;
			boolean inCode = true;
			int lineCounter = 0;
			while (inp.hasNextLine() && goodProgram){
				lineCounter++;
				String rawInput = inp.nextLine();
				String line = rawInput.trim();

				//check for a blank line in the code
				if (blankLineHit && line.length()>0){
					goodProgram = false;
					message = "There is a blank line in the program on line " + (lineCounter - 1);
				}
				//check if the current line is blank
				else if (line.length()==0){
					blankLineHit = true;
				}
				//check if the first character on the line is a white-space
				else if (Character.isWhitespace(rawInput.charAt(0))){
					goodProgram = false;
					message = "Illegal line starts with white space on line " + lineCounter;
				}
				//else do the rest of the processing
				else{

					//CODE handler
					if (!line.startsWith("DATA")) {
						//generate an error if we find code after some DATA
						if(!inCode) {
							goodProgram = false;
							message = "There is more code after data began on line " + (lineCounter);
						}
						//process the instruction line
						else {	
							// break the line into pieces
							String[] parts = line.trim().split("\\s+");

							//NoArgument instructions
							if (parts.length==1){
								//check the mnemonic 	
								if (noArgument.contains(parts[0])){
									long temp = 2*opcode.get(parts[0]); temp = temp << 32;
								}
								//bad instruction
								else {
									goodProgram = false;
									message = "Illegal mnemonic or missing argument on line " + lineCounter;
								}
							}

							//Argument instructions
							else if (parts.length==2){
								//check for mnemonic
								if (!opcode.containsKey(parts[0])){	/**!!! THIS IS WRONG What is the right way to check for mnemonic?*/
									Boolean indirect = false;
									int arg = 0;
									String mnemonic = parts[0];

									//test for an indirect instruction
									if (mnemonic.endsWith("#")){
										indirect = true;
										mnemonic = mnemonic.substring(0, mnemonic.length()-1);
									}

									//some error checks:
									if (!opcode.containsKey(mnemonic)){
										goodProgram = false;
										message = "Illegal mnemonic on line " + lineCounter;
									}
									else if (indirect && !allowsIndirect.contains(mnemonic)){
										goodProgram = false;
										message = "Instruction cannot be indirect on line " + lineCounter;
									}

									//extract the integer
									if (goodProgram){
										try{
											Integer.parseInt(parts[1], 16);
										}
										catch(NumberFormatException e){
											goodProgram = false;
											message = "Argument not a hexadecimal number on line " + lineCounter;
										}
									}

									//create the output
									if (goodProgram){
										int opc = 2*opcode.get(mnemonic);
										if(indirect){
											opc++;
										}
										long otemp = opc;
										long atemp = arg;
										otemp = otemp << 32;
										atemp = atemp & 0x00000000FFFFFFFFL; otemp = otemp | atemp;
										outp.println(otemp);
									}				
								}
								else {
									/**!!! There's no directions concerning this else.*/
									goodProgram = false;
									message = "Illegal mnemonic or missing argument on line " + lineCounter;
								}
							}
							// invalidly long instruction
							else if (parts.length > 2){
								goodProgram = false;
								message = "Too many arguments or other error on line " + lineCounter;
							}
						}

					}

					//DATA handler
					else{
						if(goodProgram){
							if(inCode) {
								outp.println(-1L); // output a separator
								inCode = false;
							}
							String[] parts = line.trim().split("\\s+");

							// invalid forms of DATA
							if (parts.length ==1){
								goodProgram = false;
								message = "No data on line " + lineCounter;
							}
							else if (parts.length == 2){
								goodProgram = false;
								message = "No data value on line " + lineCounter;
							}
							else if (parts.length > 3){
								goodProgram = false;
								message =  "Too many data values on line " + lineCounter;
							}

							//valid form of DATA
							else {
								int addr = 0;
								int val = 0;
								try {
									addr = Integer.parseInt(parts[1], 16);
									val = Integer.parseInt(parts[2], 16);
								}
								catch(NumberFormatException e){
									goodProgram = false;
									message = "Address or value not a hexadecimal number";
								}

								if (goodProgram){
									long atemp = addr;	//System.out.println(atemp);
									atemp = atemp << 32;	//System.out.println(atemp);
									long vtemp = val;
									vtemp = vtemp & 0x00000000FFFFFFFFL; //System.out.println(vtemp);
									atemp = atemp | vtemp; //System.out.println(atemp); outp.println(atemp);
								}
							}
						}
					}
				}
			}
			inp.close();
			outp.close();
		}
		catch (IOException e){
			message = "Unable to open the necessary files";
		}
		if(!goodProgram && output != null && output.exists()){
			output.delete();
		}

		return message;
	}

	public static void main(String[] args) {
		AssemblerInterface as = new Assembler();
		String fileName = "factorial8"; // CHANGE AS NEEDED FOR TESTING
		System.out.println(as.assemble(new File(fileName + ".pasm"), new File(fileName + ".pexe")));
	}

}