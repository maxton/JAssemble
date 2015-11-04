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
public class InvalidInstructionException extends Exception {
  private String op;
  public InvalidInstructionException(String op){
    super("Invalid instruction encountered: "+op);
    this.op = op;
  }
  
  public String getOp(){
    return this.op;
  }
}
