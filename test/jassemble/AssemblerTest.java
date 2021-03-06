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

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test cases for the Assembler class.
 * @author Max
 */
public class AssemblerTest {
  
  public AssemblerTest() {
  }
  
  @BeforeClass
  public static void setUpClass() {
  }
  
  @AfterClass
  public static void tearDownClass() {
  }
  
  @Before
  public void setUp() {
  }
  
  @After
  public void tearDown() {
  }

  /**
   * Test of assemble method, of class Assembler. (lw instruction)
   */
  @Test
  public void testAssembleLw() throws Exception {
    System.out.println("Assemble lw instruction");
    Assembler instance = new Assembler("main: lw $1, $2, 5\nlw $3, $1, -6");
    short[] expResult = {0x0605, 0x0DFA};
    instance.assemble();
    short[] result = instance.getInstructionWords();
    assertArrayEquals(expResult, result);
            
    instance = new Assembler("main: lw $2, 5($1)\nlw $1, -6($3)");
    instance.assemble();
    result = instance.getInstructionWords();
    assertArrayEquals(expResult, result);
  }
  /**
   * Test of assemble method, of class Assembler. (sw instruction)
   */
  @Test
  public void testAssembleSw() throws Exception {
    System.out.println("Assemble sw instruction");
    Assembler instance = new Assembler("main: sw $1, $2, 5\nsw $3, $1, -6");
    short[] expResult = {0x1605, 0x1DFA};
    instance.assemble();
    short[] result = instance.getInstructionWords();
    assertArrayEquals(expResult, result);
    
    instance = new Assembler("main: sw $2, 5($1)\nsw $1, -6($3)");
    instance.assemble();
    result = instance.getInstructionWords();
    assertArrayEquals(expResult, result);
  }
  
  /**
   * Test of assemble method, of class Assembler. (add instruction)
   */
  @Test
  public void testAssembleAdd() throws Exception {
    System.out.println("Assemble add instruction");
    Assembler instance = new Assembler("main: add $0, $0, $1\nadd $0, $1, $2");
    short[] expResult = {0x2100, 0x2600};
    instance.assemble();
    short[] result = instance.getInstructionWords();
    assertArrayEquals(expResult, result);
  }
  
  /**
   * Test of assemble method, of class Assembler. (addi instruction)
   */
  @Test
  public void testAssembleAddi() throws Exception {
    System.out.println("Assemble addi instruction");
    Assembler instance = new Assembler("main: addi $2, $1, 5\naddi $1, $3, -6");
    short[] expResult = {0x3605, 0x3DFA};
    instance.assemble();
    short[] result = instance.getInstructionWords();
    assertArrayEquals(expResult, result);
  }
  
  /**
   * Test of assemble method, of class Assembler. (inv instruction)
   * inv rd, rt (rs = 0)
   * @throws Exception 
   */
  @Test
  public void testAssembleInv() throws Exception {
    System.out.println("Assemble inv instruction");
    Assembler instance = new Assembler("main: inv $1, $2\ninv $3, $1");
    short[] expResult = {0x4240, 0x41C0};
    instance.assemble();
    short[] result = instance.getInstructionWords();
    assertArrayEquals(expResult, result);
  }
  /**
   * Test of assemble method, of class Assembler. (add instruction)
   */
  @Test
  public void testAssembleAnd() throws Exception {
    System.out.println("Assemble and instruction");
    Assembler instance = new Assembler("main: and $0, $0, $1\nand $0, $1, $2");
    short[] expResult = {0x5100, 0x5600};
    instance.assemble();
    short[] result = instance.getInstructionWords();
    assertArrayEquals(expResult, result);
  }
  
  /**
   * Test of assemble method, of class Assembler. (andi instruction)
   */
  @Test
  public void testAssembleAndi() throws Exception {
    System.out.println("Assemble andi instruction");
    Assembler instance = new Assembler("main: andi $2, $1, 5\nandi $1, $3, -6");
    short[] expResult = {0x6605, 0x6DFA};
    instance.assemble();
    short[] result = instance.getInstructionWords();
    assertArrayEquals(expResult, result);
  }
  
  /**
   * Test of assemble method, of class Assembler. (or instruction)
   */
  @Test
  public void testAssembleOr() throws Exception {
    System.out.println("Assemble or instruction");
    Assembler instance = new Assembler("main: or $0, $0, $1\nor $0, $1, $2");
    short[] expResult = {0x7100, 0x7600};
    instance.assemble();
    short[] result = instance.getInstructionWords();
    assertArrayEquals(expResult, result);
  }
  
  /**
   * Test of assemble method, of class Assembler. (ori instruction)
   */
  @Test
  public void testAssembleOri() throws Exception {
    System.out.println("Assemble ori instruction");
    Assembler instance = new Assembler("main: ori $2, $1, 5\nori $1, $3, -6");
    short[] expResult = {(short)0x8605, (short)0x8DFA};
    instance.assemble();
    short[] result = instance.getInstructionWords();
    assertArrayEquals(expResult, result);
  }
  
  /**
   * Test of assemble method, of class Assembler. (sra instruction)
   */
  @Test
  public void testAssembleSra() throws Exception {
    System.out.println("Assemble sra instruction");
    // are negative shifts valid? I don't think so...
    Assembler instance = new Assembler("main: sra $2, $1, 5\nsra $1, $3, 7");
    short[] expResult = {(short)0x9605, (short)0x9D07};
    instance.assemble();
    short[] result = instance.getInstructionWords();
    assertArrayEquals(expResult, result);
  }
  
  /**
   * Test of assemble method, of class Assembler. (sll instruction)
   */
  @Test
  public void testAssembleSll() throws Exception {
    System.out.println("Assemble sll instruction");
    // are negative shifts valid? I don't think so...
    Assembler instance = new Assembler("main: sll $2, $1, 5\nsll $1, $3, 7");
    short[] expResult = {(short)0xA605, (short)0xAD07};
    instance.assemble();
    short[] result = instance.getInstructionWords();
    assertArrayEquals(expResult, result);
  }
  
  /**
   * Test of assemble method, of class Assembler. (beq instruction)
   */
  @Test
  public void testAssembleBeq() throws Exception {
    System.out.println("Assemble beq instruction");
    // are negative shifts valid? I don't think so...
    Assembler instance = new Assembler("main: beq $1, $2, end\nbeq $3, $1, main\nend:");
    short[] expResult = {(short)0xB602, (short)0xBDFF};
    instance.assemble();
    short[] result = instance.getInstructionWords();
    assertArrayEquals(expResult, result);
  }
  
  /**
   * Test of assemble method, of class Assembler. (bne instruction)
   */
  @Test
  public void testAssembleBne() throws Exception {
    System.out.println("Assemble bne instruction");
    // are negative shifts valid? I don't think so...
    Assembler instance = new Assembler("main: bne $1, $2, end\nbne $3, $1, main\nend:");
    short[] expResult = {(short)0xC602, (short)0xCDFF};
    instance.assemble();
    short[] result = instance.getInstructionWords();
    assertArrayEquals(expResult, result);
  }
  
  /**
   * Test of assemble method, of class Assembler. (or instruction)
   */
  @Test
  public void testAssembleClr() throws Exception {
    System.out.println("Assemble clr instruction");
    Assembler instance = new Assembler("main: clr $0\nclr $1");
    short[] expResult = {(short)0xD000, (short)0xD140};
    instance.assemble();
    short[] result = instance.getInstructionWords();
    assertArrayEquals(expResult, result);
  }
  
  /**
   * Test of assemble method, of class Assembler. (j pseudo-instruction)
   */
  @Test
  public void testAssembleJ() throws Exception {
    System.out.println("Assemble j pseudo-instruction");
    Assembler instance = new Assembler("main: j lbl\nlw $0, $0, 0\nlbl:j main");
    short[] expResult = {(short)0xB002, 0x0000, (short)0xB0FE};
    instance.assemble();
    short[] result = instance.getInstructionWords();
    assertArrayEquals(expResult, result);
  }
  
  /**
   * Test of assemble method, of class Assembler. (sub pseudo-instruction)
   */
  @Test
  public void testAssembleSub() throws Exception {
    System.out.println("Assemble sub pseudo-instruction");
    Assembler instance = new Assembler("main: sub $3, $2, $1\nsub $0, $1, $2");
    short[] expResult = {0x41C0, 0x3B01, 0x4200, 0x3401};
    instance.assemble();
    short[] result = instance.getInstructionWords();
    assertArrayEquals(expResult, result);
  }
  
  /**
   * Test of assemble method, of class Assembler. (subi instruction)
   * subi $rs, $rd, IMM --> R[rd] = R[rs] - IMM
   */
  @Test
  public void testAssembleSubi() throws Exception {
    System.out.println("Assemble subi pseudo-instruction");
    Assembler instance = new Assembler("main: subi $2, $1, 5\nsubi $1, $3, -6");
    short[] expResult = {0x36FB, 0x3D06};
    instance.assemble();
    short[] result = instance.getInstructionWords();
    assertArrayEquals(expResult, result);
  }
  
  /**
   * Test of assemble method, of class Assembler. (move instruction)
   * li $rd, IMM implemented as:
   * clr $rd (which is really like clr $rd, 0 $rd)
   * ori $rd, $rd, IMM
   */
  @Test
  public void testAssembleLi() throws Exception {
    System.out.println("Assemble move pseudo-instruction");
    Assembler instance = new Assembler("main: li $1, -1\nli $3, 4");
    short[] expResult = {(short)0xD140, (short)0x85FF, (short)0xD3C0, (short)0x8F04};
    instance.assemble();
    short[] result = instance.getInstructionWords();
    assertArrayEquals(expResult, result);
  }
  
  /**
   * Test of assemble method, of class Assembler. (move instruction)
   * This implementation does move $rd, $rs --> or $rd, $rs, $rs
   */
  @Test
  public void testAssembleMove() throws Exception {
    System.out.println("Assemble move pseudo-instruction");
    Assembler instance = new Assembler("main: move $1, $2\nmove $3, $0");
    short[] expResult = {0x7A40, 0x70C0};
    instance.assemble();
    short[] result = instance.getInstructionWords();
    assertArrayEquals(expResult, result);
  }
  
  /**
   * Test of assemble method, of class Assembler. (neg instruction)
   * This implementation does neg $rd, $rs --> inv $rd, $rs
   *                                           addi $rd, $rd, 1
   */
  @Test
  public void testAssembleNeg() throws Exception {
    System.out.println("Assemble neg pseudo-instruction");
    Assembler instance = new Assembler("main: neg $1, $2");
    short[] expResult = {0x4240, 0x3501};
    instance.assemble();
    short[] result = instance.getInstructionWords();
    assertArrayEquals(expResult, result);
  }
}
