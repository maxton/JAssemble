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
 * Represents a label in the assembly code.
 * @author Max
 */
class Label {
  /** 
   * The line in source code where this Label was found.
   */
  private int fileLine;
  
  /**
   * The position of the instruction word to which this Label points.
   */
  private int instructionNum;
  
  /**
   * The name of this label.
   */
  private String name;
  
  /**
   * Instantiate a label at the given source line # and instruction number.
   * @param fileLine
   * @param instructionNum 
   */
  public Label(int fileLine, int instructionNum, String name) {
    this.fileLine = fileLine;
    this.instructionNum = instructionNum;
    this.name = name;
  }
  
  public int getLine(){
    return this.fileLine;
  }
  
  public int getInstructionNum(){
    return this.instructionNum;
  }
  
  public String getName(){
    return this.name;
  }
  
  public String toString(){
    return this.getName();
  }
  
  public void setLine(int line){
    this.fileLine = line;
  }
  
  public void setInstruction(int num){
    this.instructionNum = num;
  }
  
  public void setName(String name){
    this.name = name;
  }
}
