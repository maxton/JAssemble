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
   * Test of assemble method, of class Assembler. (lw instruction)
   */
  @Test
  public void testAssembleLw() throws Exception {
    System.out.println("Assemble lw instruction");
    Assembler instance = new Assembler("main: lw $1, $2, 5\nlw $3, $1, -6");
    int[] expResult = {0x0605, 0x0DFA};
    int[] result = instance.assemble();
    assertArrayEquals(expResult, result);
  }
  /**
   * Test of assemble method, of class Assembler. (sw instruction)
   */
  @Test
  public void testAssembleSw() throws Exception {
    System.out.println("Assemble sw instruction");
    Assembler instance = new Assembler("main: sw $1, $2, 5\nsw $3, $1, -6");
    int[] expResult = {0x1605, 0x1DFA};
    int[] result = instance.assemble();
    assertArrayEquals(expResult, result);
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
   * Test of assemble method, of class Assembler. (or instruction)
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
   * Test of assemble method, of class Assembler. (ori instruction)
   */
  @Test
  public void testAssembleOri() throws Exception {
    System.out.println("Assemble ori instruction");
    Assembler instance = new Assembler("main: ori $1, $2, 5\nori $3, $1, -6");
    int[] expResult = {0x8605, 0x8DFA};
    int[] result = instance.assemble();
    assertArrayEquals(expResult, result);
  }
  
  /**
   * Test of assemble method, of class Assembler. (sra instruction)
   */
  @Test
  public void testAssembleSra() throws Exception {
    System.out.println("Assemble sra instruction");
    // are negative shifts valid? I don't think so...
    Assembler instance = new Assembler("main: sra $1, $2, 5\nsra $3, $1, 7");
    int[] expResult = {0x9605, 0x9D07};
    int[] result = instance.assemble();
    assertArrayEquals(expResult, result);
  }
  
  /**
   * Test of assemble method, of class Assembler. (sll instruction)
   */
  @Test
  public void testAssembleSll() throws Exception {
    System.out.println("Assemble sll instruction");
    // are negative shifts valid? I don't think so...
    Assembler instance = new Assembler("main: sll $1, $2, 5\nsll $3, $1, 7");
    int[] expResult = {0xA605, 0xAD07};
    int[] result = instance.assemble();
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
    int[] expResult = {0xB602, 0xBDFF};
    int[] result = instance.assemble();
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
    int[] expResult = {0xC602, 0xCDFF};
    int[] result = instance.assemble();
    assertArrayEquals(expResult, result);
  }
  
  /**
   * Test of assemble method, of class Assembler. (or instruction)
   */
  @Test
  public void testAssembleClr() throws Exception {
    System.out.println("Assemble clr instruction");
    Assembler instance = new Assembler("main: clr $0\nclr $1");
    int[] expResult = {0xD000, 0xD140};
    int[] result = instance.assemble();
    assertArrayEquals(expResult, result);
  }
}
