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
 * Indicates that an instruction (opcode) in the (dis)assembly was not valid.
 * @author Max
 */
public class InvalidInstructionException extends Exception {
  private String op;
  
  /**
   * Construct an invalid instruction exception.
   * @param op The instruction that was encountered.
   */
  public InvalidInstructionException(String op){
    super("Invalid instruction encountered: "+op);
    this.op = op;
  }
  
  /**
   * Get back the encountered instruction (opcode).
   * @return The instruction/opcode.
   */
  public String getOp(){
    return this.op;
  }
}
