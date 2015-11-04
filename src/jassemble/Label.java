/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jassemble;

/**
 * Represents a label in the assembly code.
 * @author Max
 */
class Label {
  public int fileLine;
  public int instructionNum;
  
  /**
   * Instantiate a label at the given source line # and instruction number.
   * @param fileLine
   * @param instructionNum 
   */
  public Label(int fileLine, int instructionNum) {
    this.fileLine = fileLine;
    this.instructionNum = instructionNum;
  }
}
