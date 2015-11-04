/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jassemble;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
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
   * Test of assemble method, of class Assembler. (add instruction)
   */
  @Test
  public void testAssembleAdd() throws Exception {
    System.out.println("Assemble add instruction");
    Assembler instance = new Assembler("main: add $0, $0, $1\nadd $0, $1, $2");
    int[] expResult = {0x2100, 0x2600};
    int[] result = instance.assemble();
    assertArrayEquals(expResult, result);
  }
  
  /**
   * Test of assemble method, of class Assembler. (addi instruction)
   */
  @Test
  public void testAssembleAddi() throws Exception {
    System.out.println("Assemble addi instruction");
    Assembler instance = new Assembler("main: addi $1, $2, 5\naddi $3, $1, -6");
    int[] expResult = {0x3605, 0x3DFA};
    int[] result = instance.assemble();
    assertArrayEquals(expResult, result);
  }
  
  /**
   * Test of assemble method, of class Assembler. (add instruction)
   */
  @Test
  public void testAssembleAnd() throws Exception {
    System.out.println("Assemble and instruction");
    Assembler instance = new Assembler("main: and $0, $0, $1\nand $0, $1, $2");
    int[] expResult = {0x5100, 0x5600};
    int[] result = instance.assemble();
    assertArrayEquals(expResult, result);
  }
  
  /**
   * Test of assemble method, of class Assembler. (andi instruction)
   */
  @Test
  public void testAssembleAndi() throws Exception {
    System.out.println("Assemble andi instruction");
    Assembler instance = new Assembler("main: andi $1, $2, 5\nandi $3, $1, -6");
    int[] expResult = {0x6605, 0x6DFA};
    int[] result = instance.assemble();
    assertArrayEquals(expResult, result);
  }
  
  /**
   * Test of assemble method, of class Assembler. (add instruction)
   */
  @Test
  public void testAssembleOr() throws Exception {
    System.out.println("Assemble or instruction");
    Assembler instance = new Assembler("main: or $0, $0, $1\nor $0, $1, $2");
    int[] expResult = {0x7100, 0x7600};
    int[] result = instance.assemble();
    assertArrayEquals(expResult, result);
  }
  
  /**
   * Test of assemble method, of class Assembler. (andi instruction)
   */
  @Test
  public void testAssembleOri() throws Exception {
    System.out.println("Assemble ori instruction");
    Assembler instance = new Assembler("main: ori $1, $2, 5\nori $3, $1, -6");
    int[] expResult = {0x8605, 0x8DFA};
    int[] result = instance.assemble();
    assertArrayEquals(expResult, result);
  }
}
