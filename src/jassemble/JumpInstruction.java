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
 * Represents a J-type instruction.
 * @author Max
 */
public class JumpInstruction extends Instruction {
  private final Label target;
  private final int instructionNum;
  
  /**
   * Instantiate a j-type instruction. Note the order of the arguments follows
   * the order found in assembly code.
   * @param op The opcode. Should be one of BNE, BEQ
   * @param rs The first register to compare.
   * @param rt The second register to compare.
   * @param target The label to which this instruction should jump.
   * @param instructionNum The number of this instruction.
   */
  public JumpInstruction(Opcode op, int rs, int rt, Label target, int instructionNum) {
    this.op = op;
    this.rs = (byte)rs;
    this.rt = (byte)rt;
    this.target = target;
    this.instructionNum = instructionNum;
  }
  
  public byte getTarget(){
    return (byte)(this.target.getInstructionNum() - this.instructionNum);
  }
  
  @Override
  public String toString() {
    String t = Instruction.decompToHex 
                  ? String.format("0x%02X", getTarget())
                  : Integer.toString(getTarget());
    return op.toString().toLowerCase() + " $" + rs + ", $"+rt+", "+t;
  }
  
  @Override
  public int toWord() {
    return (this.op.ordinal() << 12)
                    | (this.rs << 10)
                    | (this.rt << 8)
                    | ((this.target.getInstructionNum() - this.instructionNum) & 0xFF);
  }
  
  @Override
  public Type getType() {
    return Type.J;
  }
}
