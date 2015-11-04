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
 * Represents an I-type instruction.
 * @author Max
 */
public class ImmediateInstruction extends Instruction {
  private final byte imm;
  
  /**
   * Instantiate an I-type instruction. Note the order of the arguments follows
   * the order in the assembly code.
   * @param op The opcode. Should be one of (ADDI, ANDI, ORI, SRA, SLL, LW, SW)
   * @param rs The source register.
   * @param rt The destination register.
   * @param imm The immediate value.
   */
  public ImmediateInstruction(Opcode op, int rs, int rt, int imm){
    this.op = op;
    this.rs = (byte)rs;
    this.rt = (byte)rt;
    this.imm = (byte)imm;
  }
  
  @Override
  public Type getType() {
    return Type.I;
  }

  @Override
  public int toWord() {
    return (this.op.ordinal() << 12)
                    | (this.rs << 10)
                    | (this.rt << 8)
                    | (this.imm & 0xFF);
  }
}
