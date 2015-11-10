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
  public void TestAddiMove() throws Exception {
    this.data = new byte[256];
    this.as = new Assembler("li $0, 3\n"
            + "addi $0, $0, 4\n"
            + "move $1, $0\n"
            + "move $2, $1\n"
            + "move $3, $2");
    as.assemble();
    CPU instance = new CPU(as.getInstructionWords(), data);
    instance.step(8);
    assertEquals(7, instance.getRegister(0));
    assertEquals(7, instance.getRegister(1));
    assertEquals(7, instance.getRegister(2));
    assertEquals(7, instance.getRegister(3));
  }

  @Test
  public void TestSll() throws Exception {
    this.data = new byte[256];
    this.as = new Assembler("li  $0, 3\n"
                          + "sll $0, $0, 4");
    as.assemble();
    CPU instance = new CPU(as.getInstructionWords(), data);
    instance.step(3);
    assertEquals(48, instance.getRegister(0));
  }
  
  @Test
  public void TestSra() throws Exception {
    this.data = new byte[256];
    this.as = new Assembler("li  $0, 48\n"
                          + "sra $0, $0, 4");
    as.assemble();
    CPU instance = new CPU(as.getInstructionWords(), data);
    instance.step(3);
    assertEquals(3, instance.getRegister(0));
  }
  
  @Test
  public void TestClr() throws Exception {
    this.data = new byte[256];
    this.as = new Assembler("li  $0, 48\n"
                          + "clr $0");
    as.assemble();
    CPU instance = new CPU(as.getInstructionWords(), data);
    instance.step(3);
    assertEquals(0, instance.getRegister(0));
  }
  
  @Test
  public void TestMemoryInstructions() throws Exception {
    this.data = new byte[256];
    this.as = new Assembler("li  $0, 48\n"
                          + "sw $0, 2($1)\n"
                          + "lw $2, 2($1)");
    as.assemble();
    CPU instance = new CPU(as.getInstructionWords(), data);
    instance.step(4);
    assertEquals(48, data[2]);
    assertEquals(0, instance.getRegister(1));
    assertEquals(48, instance.getRegister(2));
  }
  
  @Test
  public void TestAndAndi() throws Exception {
    this.data = new byte[256];
    this.as = new Assembler("li  $0, 8\n"
                          + "and $1, $0, $1\n"
                          + "andi $2, $0, 15");
    as.assemble();
    CPU instance = new CPU(as.getInstructionWords(), data);
    instance.step(4);
    assertEquals(8, instance.getRegister(0));
    assertEquals(0, instance.getRegister(1));
    assertEquals(8, instance.getRegister(2));
  }
  
  @Test
  public void TestOrOri() throws Exception {
    this.data = new byte[256];
    this.as = new Assembler("li  $0, 8\n"
                          + "or $1, $0, $1\n"
                          + "ori $2, $0, 15");
    as.assemble();
    CPU instance = new CPU(as.getInstructionWords(), data);
    instance.step(4);
    assertEquals(8, instance.getRegister(0));
    assertEquals(8, instance.getRegister(1));
    assertEquals(15, instance.getRegister(2));
  }
  
  @Test
  public void TestInv() throws Exception {
    this.data = new byte[256];
    this.as = new Assembler("li  $0, 8\n"
                          + "inv $1, $0\n"
                          + "inv $2, $1");
    as.assemble();
    CPU instance = new CPU(as.getInstructionWords(), data);
    instance.step(4);
    assertEquals(8, instance.getRegister(0));
    assertEquals(-9, instance.getRegister(1));
    assertEquals(8, instance.getRegister(2));
  }
  
  @Test
  public void TestBranch() throws Exception {
    this.data = new byte[256];
    this.as = new Assembler("li  $0, 8\n"
                          + "bne $1, $0, label\n"
                          + "inv $2, $1\n"
                          + "label: clr $0\n"
                          + "beq $1, $0, label2\n"
                          + "inv $2, $1\n"
                          + "label2:");
    as.assemble();
    CPU instance = new CPU(as.getInstructionWords(), data);
    instance.step(8);
    assertEquals(0, instance.getRegister(0));
    assertEquals(0, instance.getRegister(1));
    assertEquals(0, instance.getRegister(2));
  }
}
