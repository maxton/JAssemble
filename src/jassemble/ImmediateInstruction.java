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
