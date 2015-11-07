/*
 * Copyright (C) 2015 Max
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

/**
 * Represent the RISC CPU.
 * @author Max
 */
public class CPU {
  private short PC;
  private byte[] registers;
  private short[] instrMem;
  private byte[] dataMem;
  
  public CPU(short[] instrMem, byte[] dataMem) {
    this.registers = new byte[4];
    this.PC = 0;
    this.instrMem = instrMem;
    this.dataMem = dataMem;
  }
  
  /**
   * Resets the registers, PC, and data memory to all zeroes.
   */
  public void reset(){
    this.PC = 0;
    this.registers = new byte[]{0,0,0,0};
    for(int i=0; i < dataMem.length; i++){
      dataMem[i] = 0;
    }
  }
  public void setInstruction(int idx, short instruction){
    this.instrMem[idx] = instruction;
  }
  public short getInstruction(int idx){
    return this.instrMem[idx];
  }
  public void setMem(int idx, byte data) {
    this.dataMem[idx] = data;
  }
  public byte getMem(int idx){
    return this.dataMem[idx];
  }
  public void setRegister(int idx, byte val) {
    this.registers[idx] = val;
  }
  public byte getRegister(int idx) {
    return this.registers[idx];
  }
  public short getPC(){
    return this.PC;
  }
  public void setPC(short pc){
    this.PC = pc;
  }
  public class EndOfCodeException extends Exception{
    public EndOfCodeException(){
      super("Reached end of code.");
    }
  }
  public void step() throws InvalidInstructionException, EndOfCodeException {
    if(PC == instrMem.length)
      throw new EndOfCodeException();
    Instruction inst = Instruction.fromWord(instrMem[PC], PC);
    switch(inst.getOpcode()){
      case LW:
        PC++;
        registers[inst.getRt()] = 
                dataMem[registers[inst.getRs()] + ((ImmediateInstruction)inst).getImm()];
        break;
      case SW:
        PC++;
        dataMem[registers[inst.getRs()] + ((ImmediateInstruction)inst).getImm()] = 
                registers[inst.getRt()];
        break;
      case ADD:
        PC++;
        registers[((RegisterInstruction)inst).getRd()] = 
                (byte) (registers[inst.getRs()] + registers[inst.getRt()]);
        break;
      case ADDI:
        PC++;
        registers[inst.getRt()] = 
                (byte) (registers[inst.getRs()] + ((ImmediateInstruction)inst).getImm());
        break;
      case INV:
        PC++;
        registers[((RegisterInstruction)inst).getRd()] = 
                (byte) (~registers[inst.getRs()]);
        break;
      case AND:
        PC++;
        registers[((RegisterInstruction)inst).getRd()] = 
                (byte) (registers[inst.getRs()] & registers[inst.getRt()]);
        break;
      case ANDI:
        PC++;
        registers[inst.getRt()] = 
                (byte) (registers[inst.getRs()] & ((ImmediateInstruction)inst).getImm());
        break;
      case OR:
        PC++;
        registers[((RegisterInstruction)inst).getRd()] = 
                (byte) (registers[inst.getRs()] | registers[inst.getRt()]);
        break;
      case ORI:
        PC++;
        registers[inst.getRt()] = 
                (byte) (registers[inst.getRs()] | ((ImmediateInstruction)inst).getImm());
        break;
      case SRA:
        PC++;
        registers[inst.getRt()] = 
                (byte) (registers[inst.getRs()] >> ((ImmediateInstruction)inst).getImm());
        break;
      case SLL:
        PC++;
        registers[inst.getRt()] = 
                (byte) (registers[inst.getRs()] << ((ImmediateInstruction)inst).getImm());
        break;
      case BEQ:
        if(registers[inst.getRt()] == registers[inst.getRs()])
          PC += ((JumpInstruction)inst).getTarget();
        else
          PC++;
        break;
      case BNE:
        if(registers[inst.getRt()] != registers[inst.getRs()])
          PC += ((JumpInstruction)inst).getTarget();
        else
          PC++;
        break;
      case CLR:
        PC++;
        registers[((RegisterInstruction)inst).getRd()] = 0;
        break;
      default:
        throw new InvalidInstructionException("Invalid instruction encountered ,PC: "+PC);
    }
  }
  
  /**
   * Step the CPU (up to) num times.
   * @param num How many steps to execute.
   */
  public void step(int num) {
    try {
      for(int i = 0; i < num; i++){
        step();
      }
    }
    catch(Exception e) {
      
    }
  }
}
