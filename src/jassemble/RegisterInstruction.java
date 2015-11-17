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
 * Represents an R-type instruction.
 * @author Max
 */
public class RegisterInstruction extends Instruction {
  private byte rd;
  
  /**
   * Instantiate an R-type instruction. Note the order of the constructor: it
   * follows the assembly order, not the machine code order. Also note that for 
   * INV and CLR, the format is different.
   * 
   * @param op The Opcode. Should be AND, INV, OR, or CLR.
   * @param rd The destination register.
   * @param rs The first source register.
   * @param rt The second source register. (only register, for clr)
   */
  public RegisterInstruction(Opcode op, int rd, int rs, int rt) {
    this.op = op;
    this.rs = (byte)rs;
    this.rt = (byte)rt;
    this.rd = (byte)rd;
  }
  
  /**
   * Get the destination register.
   * @return the destination register.
   */
  public byte getRd(){
    return this.rd;
  }
  
  @Override
  public String toString() {
    switch(op){
      case INV:
        return op.toString().toLowerCase() + " $" + rd + ", $"+rt;
      case CLR:
        return op.toString().toLowerCase() + " $" + rd;
      default:
        return op.toString().toLowerCase() + " $" + rd + ", $"+rs+ ", $"+rt;
    }
  }
  
  @Override
  public Type getType() {
    return Type.R;
  }

  @Override
  public int toWord() {
    return (this.op.ordinal() << 12)
                    | (this.rs << 10)
                    | (this.rt << 8)
                    | (this.rd << 6);
  }
}
