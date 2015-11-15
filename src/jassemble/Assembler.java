/*
 * Copyright (C) 2015 Maxton Connor
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package jassemble;

import jassemble.Instruction.Opcode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Represents an object that takes some form of assembly code and turns it into
 * some form of machine code.
 * @author Max
 */
public class Assembler {
  private ArrayList<Instruction> instructions;
  private Map<String,Label> labels;
  private final String[] sourceLines;
  private int currentInstruction;
  private short[] instructionWords;
  
  /**
   * Instantiate an assembler with the given source code.
   * @param source All the source code in a string.
   */
  public Assembler(String source) {
    source = source.replaceAll("\r", "").replaceAll("\t"," ");
    sourceLines = source.split("\n");
  }
  
  /**
   * Gets an instance of a label, given its name. Updates its line / instruction numbers if
   * update is true.
   * @param name The label's name.
   * @param lineNum Which line of code has the label.
   * @param instructionNum Where the label sits in the sequence of instructions.
   * @param update Should update line and instruction numbers?
   * @return 
   */
  private Label getLabel(String name, int lineNum, int instructionNum, boolean update){
    if(!labels.containsKey(name)){
      labels.put(name, new Label(lineNum,instructionNum, name));
    }
    Label ret = labels.get(name);
    if(update){
      ret.fileLine = lineNum;
      ret.instructionNum = instructionNum;
    }
    return ret;
  }
  
  
  private void addNewLabel(String name, int lineNum, int instructionNum) throws Exception {
    if(!labels.containsKey(name)){
      labels.put(name, new Label(lineNum,instructionNum, name));
    } else {
      throw new Exception(String.format("Duplicate label \"%s\" encountered", name));
    }
  }
  
  /**
   * Find the label with the given name, or create a blank one with the given name.
   * @param name
   * @return 
   */
  private Label getLabel(String name){
    return getLabel(name, 0, 0, false);
  }
  
  /**
   * Strip the $ from a register, turn it into an int.
   * @param reg String like "$1"
   * @return 
   */
  private int regToInt(String reg) throws Exception{
    if(reg == null)
      throw new Exception("Expected register, found none!");
    if(reg.charAt(0) != '$')
      throw new Exception("Expected register, found "+reg);
    int intVal = Integer.decode(reg.substring(1));
    if(intVal > 3 || intVal < 0)
      throw new Exception("Register "+reg+" is invalid.");
    return intVal;
  }
  
  /**
   * Add the instruction described by op, arg1, arg2, arg3 to the array of instructions.
   * Will properly handle pseudo-instructions.
   * @param op
   * @param arg1
   * @param arg2
   * @param arg3
   * @throws InvalidInstructionException 
   */
  private void addInstruction(
          String op, String arg1, String arg2, String arg3) throws InvalidInstructionException, Exception{
    op = op.toLowerCase();
    Instruction ret;
    // Never forget: immediate instructions have rs first, register instructions have rd first
    switch (op) {
      case "lw":
        ret = new ImmediateInstruction(Opcode.LW, regToInt(arg1), regToInt(arg2), Integer.decode(arg3));
        break;
      case "sw":
        ret = new ImmediateInstruction(Opcode.SW, regToInt(arg1), regToInt(arg2), Integer.decode(arg3));
        break;
      case "add":
        ret = new RegisterInstruction(Opcode.ADD, regToInt(arg1), regToInt(arg2), regToInt(arg3));
        break;
      case "addi":
        ret = new ImmediateInstruction(Opcode.ADDI, regToInt(arg2), regToInt(arg1), Integer.decode(arg3));
        break;
      case "inv":
        ret = new RegisterInstruction(Opcode.INV, regToInt(arg1), 0, regToInt(arg2));
        break;
      case "and":
        ret = new RegisterInstruction(Opcode.AND, regToInt(arg1), regToInt(arg2), regToInt(arg3));
        break;
      case "andi":
        ret = new ImmediateInstruction(Opcode.ANDI, regToInt(arg2), regToInt(arg1), Integer.decode(arg3));
        break;
      case "or":
        ret = new RegisterInstruction(Opcode.OR, regToInt(arg1), regToInt(arg2), regToInt(arg3));
        break;
      case "ori":
        ret = new ImmediateInstruction(Opcode.ORI, regToInt(arg2), regToInt(arg1), Integer.decode(arg3));
        break;
      case "sra":
        ret = new ImmediateInstruction(Opcode.SRA, regToInt(arg2), regToInt(arg1), Integer.decode(arg3));
        break;
      case "sll":
        ret = new ImmediateInstruction(Opcode.SLL, regToInt(arg2), regToInt(arg1), Integer.decode(arg3));
        break;
      case "beq":
        ret = new JumpInstruction(Opcode.BEQ, regToInt(arg1), regToInt(arg2), getLabel(arg3), currentInstruction);
        break;
      case "bne":
        ret = new JumpInstruction(Opcode.BNE, regToInt(arg1), regToInt(arg2), getLabel(arg3), currentInstruction);
        break;
        // the spec is not very clear about what to do for clr. it says
        // that for "clr $rt" that rd = rt and rs = 0
      case "clr": 
        ret = new RegisterInstruction(Opcode.CLR, regToInt(arg1), 0, regToInt(arg1));
        break;
      
        // Pseudoinstructions:
      /*
        j LABEL
        PC = PC + offset(LABEL)
      */
      case "j":
        ret = new JumpInstruction(Opcode.BEQ, 0,0, getLabel(arg1), currentInstruction);
        break;
      /*
        sub $rd, $rs, $rt
        R[rd] = rs - rt
        
        implemented as:
        inv $rd, $rt
        addi $rs, $rd, 1
      */
      case "sub":
        this.instructions.add(new RegisterInstruction(Opcode.INV, regToInt(arg1), regToInt(arg3), 0));
        this.currentInstruction++;
        ret = new ImmediateInstruction(Opcode.ADDI, regToInt(arg2), regToInt(arg1), 1);
        break;
      /*
        subi $rd, $rs, IMM
        R[rd] = rs - IMM
        
        implemented as:
        addi $rd, $rs, (-IMM)
        
        (the assembler makes the immediate negative)
      */
      case "subi":
        ret = new ImmediateInstruction(Opcode.ADDI, regToInt(arg2), regToInt(arg1), -Integer.decode(arg3));
        break;
      /*
        move $rd, $rs
        R[rd] = R[rs]
        
        implemented as:
        or $rd, $rs, $rs
      */
      case "move":
        ret = new RegisterInstruction(Opcode.OR, regToInt(arg1), regToInt(arg2), regToInt(arg2));
        break;
      /*
        li $rd, IMM
        R[rd] = IMM
        
        implemented as:
        clr $rd (clr $rd, 0, $rd)
        ori $rd, $rd, IMM
      */
      case "li":
        this.instructions.add(new RegisterInstruction(Opcode.CLR, regToInt(arg1), 0, regToInt(arg1)));
        this.currentInstruction++;
        ret = new ImmediateInstruction(Opcode.ORI, regToInt(arg1), regToInt(arg1), Integer.decode(arg2));
        break;
      default:
        throw new InvalidInstructionException(op);
    }
    this.instructions.add(ret);
    this.currentInstruction++;
  }
  
  /**
   * Assemble the internal assembly code to machine instruction words.
   * @throws InvalidInstructionException
   * @throws Exception 
   */
  public void assemble() throws InvalidInstructionException, Exception {
    assemble(null);
  }
  
  /**
   * Assemble the internal assembly code to machine instruction words.
   * 
   * TODO: Clean up this function, stop using regular expressions,
   *       add better comment support (don't give warnings)
   * 
   * @param mp Where to send warning messages.
   * @throws InvalidInstructionException 
   * @throws Exception
   */
  public void assemble(MessagePasser mp) throws InvalidInstructionException, Exception{
    this.labels = new HashMap<>();
    this.instructions = new ArrayList<>();
    this.currentInstruction = 0;
    
    // Match an instruction, or a label followed by an instruction
    Pattern labelAndOrInstruction = Pattern.compile(
                      "^\\s*(([A-Za-z0-9_]+):\\s*)?"
                    + "([a-z]{1,4})\\s+(\\$[0-9]|[A-Za-z0-9_-]+)\\s*"
                    + "(,\\s*(\\$[0-9]|[A-Za-z0-9_-]+))?\\s*(,\\s*(\\$[0-9]|[A-Za-z0-9_-]+))?.*");
    Pattern labelAndOrLoadStore = Pattern.compile(
            "^\\s*(([A-Za-z0-9_]+):\\s*)?" //label
                    + "([a-z]{1,4})\\s+" // instruction
                    + "(\\$[0-9]),\\s*" // rd
                    + "([-x0-9]+)\\s*\\((\\$[0-9])\\).*"); //offset(rs)
    // Match just a label.
    Pattern labelOnly = Pattern.compile("^\\s*(([A-Za-z0-9_-]+):\\s*)");
    Matcher m1, m2, m3;
    boolean error = false;
    for(int i = 0; i < sourceLines.length; i++){
      m1 = labelAndOrInstruction.matcher(sourceLines[i]);
      m2 = labelOnly.matcher(sourceLines[i]);
      m3 = labelAndOrLoadStore.matcher(sourceLines[i]);
      if(sourceLines[i].equals("") || sourceLines[i].matches("\\s+")) {
        continue;
      } else if(m3.matches()) {
        if(m3.group(2) != null) { // if contains label
          try {
            addNewLabel(m3.group(2), i+1, currentInstruction);
          } catch(Exception e) {
            if(mp != null){
              mp.sendMessage("Error: at line "+(i+1)+",\n "+e.getMessage()+"\n");
              error = true;
            }
          }
        }
        try {
          //                            instr, base, data, offset
          this.addInstruction(m3.group(3), m3.group(6), m3.group(4), m3.group(5));
        } catch(Exception e) {
          if(mp != null){
            mp.sendMessage("Error: at line "+(i+1)+",\n "+e.getMessage()+"\n");
            error = true;
          }
        }
      } else if(m2.matches()) {
        try {
            addNewLabel(m2.group(2), i+1, currentInstruction);
          } catch(Exception e) {
            if(mp != null){
              mp.sendMessage("Error: at line "+(i+1)+",\n "+e.getMessage()+"\n");
              error = true;
            }
          }
      } else if(m1.matches()) {
        if(m1.group(2) != null) { // if contains label
          try {
            addNewLabel(m1.group(2), i+1, currentInstruction);
          } catch(Exception e) {
            if(mp != null){
              mp.sendMessage("Error: at line "+(i+1)+",\n "+e.getMessage()+"\n");
              error = true;
            }
          }
        }
        try {
          this.addInstruction(m1.group(3), m1.group(4), m1.group(6), m1.group(8));
        } catch(Exception e) {
          if(mp != null){
            mp.sendMessage("Error: at line "+(i+1)+",\n "+e.getMessage()+"\n");
            error = true;
          }
        }
      } else {
        if(mp!=null){
          mp.sendMessage("Warning: Can't understand line "+(i+1)+", ignoring entire line...\n");
        }
        continue;
      }
    }
    for(String key : labels.keySet()){
      if(labels.get(key).fileLine == 0){
        error = true;
        mp.sendMessage("Error: jump to nonexistent label '"+key+"'\n");
      }
    }
    
    if(error){
      // We only kept going so that all syntax errors would be revealed.
      // So, we still need to error-out and quit the assembly process.
      throw new Exception("One or more instructions were malformed.");
    }
    
    instructionWords = new short[currentInstruction];
    int i = 0;
    for(Instruction ins : instructions){
      instructionWords[i++] = (short)ins.toWord();
    }
  }
  
  public Instruction[] getInstructions(){
    return this.instructions.toArray(new Instruction[instructions.size()]);
  }
  
  public short[] getInstructionWords(){
    return this.instructionWords.clone();
  }
  
  /**
   * For testing...
   * @param args 
   */
  public static void main(String[] args){
    Assembler a = new Assembler("main: lw $1, 0($2)\nlw $3, 2($1)\nj main\nlabel:");
    try {
      a.assemble();
      for(short word : a.getInstructionWords())
        System.out.println(String.format("%1$04x", word));
    }
    catch(Exception e){
      System.out.println("Error: "+e.getMessage());
    }
  }
}
