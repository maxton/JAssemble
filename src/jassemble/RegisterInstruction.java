/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jassemble;

/**
 *
 * @author Max
 */
public class RegisterInstruction extends Instruction {
  private byte rd;
  
  /**
   * Instantiate an R-type instruction. Note the order of the constructor. 
   * It follows the assembly order, not the machine code order.
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
