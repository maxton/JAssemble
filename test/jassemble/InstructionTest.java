/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jassemble;

import jassemble.Instruction.Opcode;
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
public class InstructionTest {
  
  public InstructionTest() {
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
   * Test of toHexCode method, of class JumpInstruction.
   */
  @Test
  public void testJumpInstructionToHexCode() {
    System.out.println("JumpInstruction toHexCode (2 tests)");
    JumpInstruction instance = new JumpInstruction(Instruction.Opcode.BEQ, 0, 0, new Label(0, 0), 3);
    String expResult = "b0fd";
    String result = instance.toHexCode();
    assertEquals(expResult, result);
    
    instance = new JumpInstruction(Instruction.Opcode.BNE, 0, 1, new Label(0,6), 0);
    expResult = "c106";
    result = instance.toHexCode();
    assertEquals(expResult, result);
  }
  
  /**
   * Test of toHexCode method, of class RegisterInstruction.
   */
  @Test
  public void testRegisterInstructionToHexCode() {
    System.out.println("RegisterInstruction toHexCode (3 tests)");
    RegisterInstruction instance = new RegisterInstruction(Opcode.ADD, 1, 2, 3);
    String expResult = "2b40";
    String result = instance.toHexCode();
    assertEquals(expResult, result);
    
    instance = new RegisterInstruction(Opcode.AND, 3, 3, 3);
    expResult = "5fc0";
    result = instance.toHexCode();
    assertEquals(expResult, result);
    
    instance = new RegisterInstruction(Opcode.OR, 0, 0, 0);
    expResult = "7000";
    result = instance.toHexCode();
    assertEquals(expResult, result);
  }
  
  /**
   * Test of toHexCode method, of class ImmediateInstruction.
   */
  @Test
  public void testImmediateInstructionToHexCode() {
    System.out.println("ImmediateInstruction toHexCode (2 tests)");
    ImmediateInstruction instance = new ImmediateInstruction(Opcode.ADDI, 1, 2, -75);
    String expResult = "36b5";
    String result = instance.toHexCode();
    assertEquals(expResult, result);
    
    instance = new ImmediateInstruction(Opcode.ANDI, 3, 3, 3);
    expResult = "6f03";
    result = instance.toHexCode();
    assertEquals(expResult, result);
  }
  
}
