/*
 * Copyright (C) 2015 Max
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

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableModel;

/**
 * GUI for simulating the CPU.
 * @author Max
 */
public class SimulatorPanel extends javax.swing.JPanel {
  private Instruction[] instructions;
  private short[] instructionWords;
  private byte[] data;
  private CPU cpu;
  private String fmt = Instruction.decompToHex ? "%02X" : "%d";
  Thread cpuThread;
  CPURunner cpuRunner;
  
  /**
   * Creates new form SimulatorPanel
   * @param as The assembler which has prepared the instructions for this simulator.
   */
  public SimulatorPanel() {
    initComponents();
    this.instructionTable.getColumnModel().getColumn(0).setMaxWidth(40);
    this.instructions = new Instruction[0];
    this.instructionWords = new short[0];
    this.data = new byte[0];
    this.cpu = new CPU(instructionWords, data);
  }
  
  public void setAssembler(Assembler as) {
    this.instructions = as.getInstructions();
    this.instructionWords = as.getInstructionWords();
    this.data = new byte[256];
    this.cpu = new CPU(instructionWords, data);
    this.instructionTable.getSelectionModel().setSelectionInterval(0, 0);
    this.refreshGui();
  }
  
  /**
   * Worker thread for stepping the CPU automatically.
   */
  private class CPURunner implements Runnable {
    
    /**
     * Steps the CPU until it can't.
     */
    @Override
    public void run() {
      try {
        while(step()){
          Thread.sleep(1000 - runSpeedSlider.getValue());
        }
      } catch(InterruptedException e) {
        
      }
      runButton.setText("Run");
      runButton.setSelected(false);
    }
  }
  
  /**
   * Automatically steps through all instructions.
   */
  private void run() {
    if(cpuThread == null || !cpuThread.isAlive()){
      cpuRunner = new CPURunner();
      cpuThread = new Thread(cpuRunner);
      cpuThread.start();
      runButton.setText("Pause");
      runButton.setSelected(true);
    }
  }
  
  private void stop() {
    cpuThread.interrupt();
  }
  
  /**
   * Step through a single CPU instruction.
   * @return Did the instruction execute correctly?
   */
  private boolean step() {
    try {
      cpu.step();
    } catch (InvalidInstructionException ex) {
      return false;
    } catch (CPU.EndOfCodeException ex) {
      return false;
    }
    refreshGui();
    return true;
  }
  
  /**
   * Reset the CPU and simulator GUI.
   */
  private void reset() {
    cpu.reset();
    refreshGui();
  }
  
  /**
   * Update all GUI elements with current CPU state.
   */
  private void refreshGui(){
    fmt = Instruction.decompToHex ? "%02X" : "%d";
    instructionTable.getSelectionModel().setSelectionInterval(cpu.getPC(), cpu.getPC());
    pcTextField.setText(String.format(fmt, cpu.getPC()));
    r0TextField.setText(String.format(fmt, cpu.getRegister(0)));
    r1TextField.setText(String.format(fmt, cpu.getRegister(1)));
    r2TextField.setText(String.format(fmt, cpu.getRegister(2)));
    r3TextField.setText(String.format(fmt, cpu.getRegister(3)));
    dataTable.invalidate();
    instructionTable.invalidate();
    this.revalidate();
    this.repaint();
  }
  
  private class DataTableModel
          extends AbstractTableModel {
    public DataTableModel() {
    }

    @Override
    public String getColumnName(int column){
      return column==1 ? "Val" : "Addr";
    }
    
    @Override
    public int getRowCount() {
      return data.length;
    }

    @Override
    public int getColumnCount() {
      return 2;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
      if(columnIndex == 0){
        return rowIndex;
      }
      else {
        return String.format(fmt, data[rowIndex]);
      }
    }
  }
  
  private class InstructionTableModel 
          extends AbstractTableModel {
    public InstructionTableModel() {
    }

    @Override
    public String getColumnName(int column){
      return column==1 ? "Instruction" : "Addr";
    }
    
    @Override
    public int getRowCount() {
      return instructions.length;
    }

    @Override
    public int getColumnCount() {
      return 2;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
      if(columnIndex == 0){
        return rowIndex;
      }
      else {
        return instructions[rowIndex].toString();
      }
    }
  }
  
  /**
   * This method is called from within the constructor to initialize the form.
   * WARNING: Do NOT modify this code. The content of this method is always
   * regenerated by the Form Editor.
   */
  @SuppressWarnings("unchecked")
  // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
  private void initComponents() {
    java.awt.GridBagConstraints gridBagConstraints;

    buttonGroup1 = new javax.swing.ButtonGroup();
    jScrollPane2 = new javax.swing.JScrollPane();
    instructionTable = new javax.swing.JTable();
    jScrollPane3 = new javax.swing.JScrollPane();
    dataTable = new javax.swing.JTable();
    stepButton = new javax.swing.JButton();
    resetButton = new javax.swing.JButton();
    jLabel1 = new javax.swing.JLabel();
    jLabel2 = new javax.swing.JLabel();
    jLabel3 = new javax.swing.JLabel();
    jLabel4 = new javax.swing.JLabel();
    jLabel5 = new javax.swing.JLabel();
    pcTextField = new javax.swing.JTextField();
    r0TextField = new javax.swing.JTextField();
    r1TextField = new javax.swing.JTextField();
    r2TextField = new javax.swing.JTextField();
    r3TextField = new javax.swing.JTextField();
    runSpeedSlider = new javax.swing.JSlider();
    jLabel6 = new javax.swing.JLabel();
    runButton = new javax.swing.JToggleButton();
    jPanel1 = new javax.swing.JPanel();
    jLabel7 = new javax.swing.JLabel();
    hexRadioButton = new javax.swing.JRadioButton();
    decRadioButton = new javax.swing.JRadioButton();

    setLayout(new java.awt.GridBagLayout());

    jScrollPane2.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
    jScrollPane2.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

    instructionTable.setFont(new java.awt.Font("Courier New", 0, 12)); // NOI18N
    instructionTable.setModel(new InstructionTableModel());
    instructionTable.setEnabled(false);
    instructionTable.setShowHorizontalLines(false);
    jScrollPane2.setViewportView(instructionTable);

    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 0;
    gridBagConstraints.gridheight = java.awt.GridBagConstraints.REMAINDER;
    gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
    gridBagConstraints.weightx = 0.6;
    gridBagConstraints.weighty = 1.0;
    add(jScrollPane2, gridBagConstraints);

    jScrollPane3.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
    jScrollPane3.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

    dataTable.setModel(new DataTableModel());
    dataTable.setEnabled(false);
    jScrollPane3.setViewportView(dataTable);

    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 1;
    gridBagConstraints.gridy = 0;
    gridBagConstraints.gridheight = java.awt.GridBagConstraints.REMAINDER;
    gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
    gridBagConstraints.weightx = 0.25;
    gridBagConstraints.weighty = 1.0;
    add(jScrollPane3, gridBagConstraints);

    stepButton.setText("Step");
    stepButton.setFocusable(false);
    stepButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
    stepButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
    stepButton.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        stepButtonActionPerformed(evt);
      }
    });
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 2;
    gridBagConstraints.gridy = 8;
    gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
    gridBagConstraints.weightx = 0.2;
    add(stepButton, gridBagConstraints);

    resetButton.setText("Reset");
    resetButton.setFocusable(false);
    resetButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
    resetButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
    resetButton.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        resetButtonActionPerformed(evt);
      }
    });
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 2;
    gridBagConstraints.gridy = 9;
    gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
    gridBagConstraints.weightx = 0.2;
    add(resetButton, gridBagConstraints);

    jLabel1.setText("PC");
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 2;
    gridBagConstraints.gridy = 0;
    gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 0);
    add(jLabel1, gridBagConstraints);

    jLabel2.setText("$0");
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 2;
    gridBagConstraints.gridy = 1;
    gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 0);
    add(jLabel2, gridBagConstraints);

    jLabel3.setText("$1");
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 2;
    gridBagConstraints.gridy = 2;
    gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 0);
    add(jLabel3, gridBagConstraints);

    jLabel4.setText("$2");
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 2;
    gridBagConstraints.gridy = 3;
    gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 0);
    add(jLabel4, gridBagConstraints);

    jLabel5.setText("$3");
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 2;
    gridBagConstraints.gridy = 4;
    gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 0);
    add(jLabel5, gridBagConstraints);

    pcTextField.setText("0");
    pcTextField.setToolTipText("");
    pcTextField.setEnabled(false);
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 3;
    gridBagConstraints.gridy = 0;
    gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
    add(pcTextField, gridBagConstraints);

    r0TextField.setText("0");
    r0TextField.setEnabled(false);
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 3;
    gridBagConstraints.gridy = 1;
    gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
    add(r0TextField, gridBagConstraints);

    r1TextField.setText("0");
    r1TextField.setEnabled(false);
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 3;
    gridBagConstraints.gridy = 2;
    gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
    add(r1TextField, gridBagConstraints);

    r2TextField.setText("0");
    r2TextField.setEnabled(false);
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 3;
    gridBagConstraints.gridy = 3;
    gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
    add(r2TextField, gridBagConstraints);

    r3TextField.setText("0");
    r3TextField.setEnabled(false);
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 3;
    gridBagConstraints.gridy = 4;
    gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
    add(r3TextField, gridBagConstraints);

    runSpeedSlider.setMaximum(950);
    runSpeedSlider.setValue(475);
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 2;
    gridBagConstraints.gridy = 6;
    gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
    gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
    add(runSpeedSlider, gridBagConstraints);

    jLabel6.setText("Run Speed (1Hz - 20Hz)");
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 2;
    gridBagConstraints.gridy = 5;
    gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
    add(jLabel6, gridBagConstraints);

    runButton.setText("Run");
    runButton.setFocusable(false);
    runButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
    runButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
    runButton.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        runButtonActionPerformed(evt);
      }
    });
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 2;
    gridBagConstraints.gridy = 7;
    gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
    gridBagConstraints.weightx = 0.2;
    add(runButton, gridBagConstraints);

    jLabel7.setText("Immediates:");
    jPanel1.add(jLabel7);

    buttonGroup1.add(hexRadioButton);
    hexRadioButton.setText("Hex");
    hexRadioButton.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        radioButtonChanged(evt);
      }
    });
    jPanel1.add(hexRadioButton);

    buttonGroup1.add(decRadioButton);
    decRadioButton.setSelected(true);
    decRadioButton.setText("Dec");
    decRadioButton.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        radioButtonChanged(evt);
      }
    });
    jPanel1.add(decRadioButton);

    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 2;
    gridBagConstraints.gridy = 10;
    gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
    gridBagConstraints.gridheight = java.awt.GridBagConstraints.REMAINDER;
    gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
    add(jPanel1, gridBagConstraints);
  }// </editor-fold>//GEN-END:initComponents

  private void resetButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_resetButtonActionPerformed
    this.reset();
  }//GEN-LAST:event_resetButtonActionPerformed

  private void stepButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_stepButtonActionPerformed
    this.step();
  }//GEN-LAST:event_stepButtonActionPerformed

  private void runButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_runButtonActionPerformed
    if(runButton.isSelected())
      this.run();
    else
      this.stop();
  }//GEN-LAST:event_runButtonActionPerformed

  private void radioButtonChanged(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_radioButtonChanged
    if(hexRadioButton.isSelected()){
      Instruction.decompToHex = true;
    } else {
      Instruction.decompToHex = false;
    }
    this.refreshGui();
  }//GEN-LAST:event_radioButtonChanged


  // Variables declaration - do not modify//GEN-BEGIN:variables
  private javax.swing.ButtonGroup buttonGroup1;
  private javax.swing.JTable dataTable;
  private javax.swing.JRadioButton decRadioButton;
  private javax.swing.JRadioButton hexRadioButton;
  private javax.swing.JTable instructionTable;
  private javax.swing.JLabel jLabel1;
  private javax.swing.JLabel jLabel2;
  private javax.swing.JLabel jLabel3;
  private javax.swing.JLabel jLabel4;
  private javax.swing.JLabel jLabel5;
  private javax.swing.JLabel jLabel6;
  private javax.swing.JLabel jLabel7;
  private javax.swing.JPanel jPanel1;
  private javax.swing.JScrollPane jScrollPane2;
  private javax.swing.JScrollPane jScrollPane3;
  private javax.swing.JTextField pcTextField;
  private javax.swing.JTextField r0TextField;
  private javax.swing.JTextField r1TextField;
  private javax.swing.JTextField r2TextField;
  private javax.swing.JTextField r3TextField;
  private javax.swing.JButton resetButton;
  private javax.swing.JToggleButton runButton;
  private javax.swing.JSlider runSpeedSlider;
  private javax.swing.JButton stepButton;
  // End of variables declaration//GEN-END:variables
}
