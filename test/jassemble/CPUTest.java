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
public class CPUTest {
  
  private Assembler as;
  private byte[] data;
  
  public CPUTest() {
  }
  
  @Test
  public void TestSequence1() throws Exception {
    this.data = new byte[256];
    this.as = new Assembler("li $0, 3\n"
            + "addi $0, $0, 4\n"
            + "move $0, $1\n"
            + "move $1, $2\n"
            + "move $2, $3");
    as.assemble();
    CPU instance = new CPU(as.getInstructionWords(), data);
    instance.step(8);
    assertEquals(7, instance.getRegister(0));
    assertEquals(7, instance.getRegister(1));
    assertEquals(7, instance.getRegister(2));
    assertEquals(7, instance.getRegister(3));
  }

  
}
