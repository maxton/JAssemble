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


/**
 * Represents a single 16-bit instruction.
 * @author Max
 */
public abstract class Instruction {
  public enum Type {
    R, I, J
  }
  public enum Opcode {
    LW, SW, 
    ADD, ADDI, 
    INV, 
    AND, ANDI, 
    OR, ORI, 
    SRA, SLL, 
    BEQ, BNE, 
    CLR, 
    INVALID1, INVALID2
  }
  Type t;
  Opcode op;
  byte rs, rt;
  
  public abstract int toWord();
  public abstract Type getType();
  
  public String toHexCode() {
    return String.format("%1$04x", this.toWord());    
  }
  
  public Opcode getOpcode() {
    return this.op;
  }
  
  public byte getOpcodeAsByte() {
    return (byte)this.op.ordinal();
  }
  
  public byte getRs() {
    return this.rs;
  }

  public byte getRt() {
    return this.rt;
  }
  
  /**
   * Decompiles an instruction given machine code. You will end up with duplicate labels. Actual
   * decompiler code should deal with that.
   * @param word The machine code.
   * @param instructionNum The offset of the instruction from the beginning of the file.
   * @return The decompiled instruction.
   * @throws InvalidInstructionException 
   */
  public static Instruction fromWord(int word, int instructionNum) 
          throws InvalidInstructionException {
    word &= 0xFFFF;
    int op = (word & 0xF000) >> 12;
    int rs = (word & 0x0C00) >> 10;
    int rt = (word & 0x0400) >> 8;
    int rd = (word & 0x00C0) >> 6;
    byte imm = (byte)(word & 0x00FF);
    switch(op){
      case 0:
      case 1:
      case 3:
      case 6:
      case 8:
      case 9:
      case 10:
        return new ImmediateInstruction(Opcode.values()[op], rs, rt, imm);
      case 2:
      case 4:
      case 5:
      case 7:
      case 13:
        return new RegisterInstruction(Opcode.values()[op],rd, rs, rt);
      case 11:
      case 12:
        return new JumpInstruction(Opcode.values()[op], rs, rt, new Label(0, imm+instructionNum), instructionNum);
      default:
        throw new InvalidInstructionException(""+op);
    }
  }
}
