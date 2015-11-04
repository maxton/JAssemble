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
public class JumpInstruction extends Instruction {
  private final Label target;
  private final int instructionNum;
  
  /**
   * 
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
  
  @Override
  public int toWord() {
    return (this.op.ordinal() << 12)
                    | (this.rs << 10)
                    | (this.rt << 8)
                    | ((this.target.instructionNum - this.instructionNum) & 0xFF);
  }
  
  @Override
  public Type getType() {
    return Type.J;
  }
}
