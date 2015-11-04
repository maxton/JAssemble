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
  public int fileLine;
  
  /**
   * The position of the instruction word to which this Label points.
   */
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
