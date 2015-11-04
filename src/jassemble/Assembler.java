/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jassemble;

import jassemble.Instruction.Opcode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author Max
 */
public class Assembler {
  private ArrayList<Instruction> instructions;
  private Map<String,Label> labels;
  private final String[] sourceLines;
  private int currentInstruction;
  
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
      labels.put(name, new Label(lineNum,instructionNum));
    } 
    Label ret = labels.get(name);
    if(update){
      ret.fileLine = lineNum;
      ret.instructionNum = instructionNum;
    }
    return ret;
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
   * Strip the $ from a register, turn it into a string.
   * @param reg String like "$1"
   * @return 
   */
  private int regToInt(String reg){
    return Integer.decode(reg.substring(1));
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
          String op, String arg1, String arg2, String arg3) throws InvalidInstructionException{
    op = op.toLowerCase();
    Instruction ret;
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
        ret = new ImmediateInstruction(Opcode.ADDI, regToInt(arg1), regToInt(arg2), Integer.decode(arg3));
        break;
      case "inv":
        ret = new RegisterInstruction(Opcode.INV, regToInt(arg1), regToInt(arg2), 0);
        break;
      case "and":
        ret = new RegisterInstruction(Opcode.AND, regToInt(arg1), regToInt(arg2), regToInt(arg3));
        break;
      case "andi":
        ret = new ImmediateInstruction(Opcode.ANDI, regToInt(arg1), regToInt(arg2), Integer.decode(arg3));
        break;
      case "or":
        ret = new RegisterInstruction(Opcode.OR, regToInt(arg1), regToInt(arg2), regToInt(arg3));
        break;
      case "ori":
        ret = new ImmediateInstruction(Opcode.ORI, regToInt(arg1), regToInt(arg2), Integer.decode(arg3));
        break;
      case "sra":
        ret = new ImmediateInstruction(Opcode.SRA, regToInt(arg1), regToInt(arg2), Integer.decode(arg3));
        break;
      case "sll":
        ret = new ImmediateInstruction(Opcode.SLL, regToInt(arg1), regToInt(arg2), Integer.decode(arg3));
        break;
      case "beq":
        ret = new JumpInstruction(Opcode.BEQ, regToInt(arg1), regToInt(arg2), getLabel(arg3), currentInstruction);
        break;
      case "bne":
        ret = new JumpInstruction(Opcode.BNE, regToInt(arg1), regToInt(arg2), getLabel(arg3), currentInstruction);
        break;
      case "clr":
        ret = new RegisterInstruction(Opcode.CLR, 0, regToInt(arg1), regToInt(arg1));
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
        addi $rd, $rs, 1
      */
      case "sub":
        this.instructions.add(new RegisterInstruction(Opcode.INV, regToInt(arg1), regToInt(arg3), 0));
        this.currentInstruction++;
        ret = new ImmediateInstruction(Opcode.ADDI, regToInt(arg1), regToInt(arg2), 1);
        break;
      /*
        subi $rs, $rd, IMM
        R[rd] = rs - IMM
        
        implemented as:
        addi $rd, $rs, (-IMM)
        
        (the assembler makes the immediate negative)
      */
      case "subi":
        ret = new ImmediateInstruction(Opcode.ADDI, regToInt(arg1), regToInt(arg2), -Integer.decode(arg3));
        break;
      /*
        move $rs, $rd
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
        clr $rd
        ori $rd, $rd, IMM
      */
      case "li":
        this.instructions.add(new RegisterInstruction(Opcode.CLR, 0, regToInt(arg1), regToInt(arg1)));
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
   * @return Array of instruction words.
   * @throws InvalidInstructionException 
   */
  public int[] assemble() throws InvalidInstructionException{
    int[] ret;
    this.labels = new HashMap<>();
    this.instructions = new ArrayList<>();
    this.currentInstruction = 0;

    // Match an instruction, or a label followed by an instruction
    Pattern labelAndOrInstruction = Pattern.compile(
                      "^\\s*(([A-Za-z0-9_]+):\\s*)?"
                    + "([a-z]{1,4})\\s+(\\$[0-3]|[A-Za-z0-9_-]+)\\s*"
                    + "(,\\s*(\\$[0-3]|[A-Za-z0-9_-]+))?\\s*(,\\s*(\\$[0-3]|[A-Za-z0-9_-]+))?$");
    // Match just a label.
    Pattern labelOnly = Pattern.compile("^(([A-Za-z0-9_-]+):\\s*)");
    Matcher m1, m2;
    
    for(int i = 0; i < sourceLines.length; i++){
      m1 = labelAndOrInstruction.matcher(sourceLines[i]);
      m2 = labelOnly.matcher(sourceLines[i]);
      if(sourceLines[i].equals("") || sourceLines[i].matches("\\s+")) {
        continue;
      } else if(m1.matches()) {
        if(m1.group(2) != null) { // if contains label
          getLabel(m1.group(2), i+1, currentInstruction, true);
        }
        this.addInstruction(m1.group(3), m1.group(4), m1.group(6), m1.group(8));
      } else if(m2.matches()) {
        getLabel(m2.group(2), i+1, currentInstruction, true);
      } else {
        continue;
      }
    }
    ret = new int[currentInstruction];
    int i = 0;
    for(Instruction ins : instructions){
      ret[i++] = ins.toWord();
    }
    return ret;
  }
  
  public static void main(String[] args){
    Assembler a = new Assembler("main: add $0, $0, $1\nadd $0, $1, $2\nj main\nlabel:");
    try {
      for(int word : a.assemble())
        System.out.println(String.format("%1$04x", (short)word));
    }
    catch(InvalidInstructionException e){
      System.out.println("Error: "+e.getMessage());
    }
  }
}
