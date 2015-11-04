/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jassemble;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;

/**
 *
 * @author Max
 */
public class MainFrame extends javax.swing.JFrame {

  
  private File currentFile;
  /**
   * Creates new form MainFrame
   */
  public MainFrame() {
    initComponents();
    
  }
  
  private void openNewFile() {
    if(fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION){
      this.currentFile = fileChooser.getSelectedFile();
    }
    try {
      this.assemblyTextArea.setText(Util.readFile(currentFile.getPath(), StandardCharsets.UTF_8));
      this.saveAssemblyAsMenuItem.setEnabled(true);
      this.saveAssemblyMenuItem.setEnabled(true);
    } catch (IOException ex) {
      Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
    }
  }
  
  private void saveFile() {
    if(this.currentFile == null) return;
    try {
      Util.saveFile(currentFile.getPath(), assemblyTextArea.getText());
    } catch (IOException ex) {
      Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
    }
  }
  private class MPImpl implements MessagePasser {
    @Override
    public void sendMessage(String msg) {
      tabPane.setSelectedIndex(1);
      errorTextArea.append(msg);
    }
  }
  private void assemble() {
    errorTextArea.setText("");
    Assembler as = new Assembler(assemblyTextArea.getText());
    try {
      int[] assembledWords = as.assemble(new MPImpl());
      int i = 0;
      machineCode.setText("memory_initialization_radix=16;\nmemory_initialization_vector=");
      for(int word : assembledWords) {
        machineCode.append(String.format("%1$04x", (short)word));
        if(++i < assembledWords.length){
          machineCode.append(",");
        } else {
          machineCode.append(";\n");
        }
      }
    } catch (Exception ex) {
      tabPane.setSelectedIndex(1);
      errorTextArea.append("Couldn't assemble code:\n " + ex.getMessage() + "\n");
    }
  }

  /**
   * This method is called from within the constructor to initialize the form. WARNING: Do NOT
   * modify this code. The content of this method is always regenerated by the Form Editor.
   */
  @SuppressWarnings("unchecked")
  // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
  private void initComponents() {
    java.awt.GridBagConstraints gridBagConstraints;

    AboutPopup = new javax.swing.JDialog();
    jLabel1 = new javax.swing.JLabel();
    jLabel2 = new javax.swing.JLabel();
    jLabel5 = new javax.swing.JLabel();
    fileChooser = new javax.swing.JFileChooser();
    jFrame1 = new javax.swing.JFrame();
    jPanel2 = new javax.swing.JPanel();
    assemblyCodeLabel = new javax.swing.JLabel();
    jScrollPane3 = new javax.swing.JScrollPane();
    assemblyTextArea = new javax.swing.JTextArea();
    assembleButton = new javax.swing.JButton();
    tabPane = new javax.swing.JTabbedPane();
    jPanel3 = new javax.swing.JPanel();
    jScrollPane2 = new javax.swing.JScrollPane();
    machineCode = new javax.swing.JTextArea();
    jPanel4 = new javax.swing.JPanel();
    jScrollPane1 = new javax.swing.JScrollPane();
    errorTextArea = new javax.swing.JTextArea();
    jMenuBar1 = new javax.swing.JMenuBar();
    jMenu1 = new javax.swing.JMenu();
    openFileMenuItem = new javax.swing.JMenuItem();
    saveAssemblyMenuItem = new javax.swing.JMenuItem();
    saveAssemblyAsMenuItem = new javax.swing.JMenuItem();
    jMenu2 = new javax.swing.JMenu();

    AboutPopup.setTitle("About JAssemble");
    AboutPopup.setLocationByPlatform(true);
    AboutPopup.setMaximumSize(new java.awt.Dimension(300, 150));
    AboutPopup.setMinimumSize(new java.awt.Dimension(300, 150));
    AboutPopup.setModal(true);
    AboutPopup.setPreferredSize(new java.awt.Dimension(300, 150));
    AboutPopup.setResizable(false);

    jLabel1.setFont(new java.awt.Font("Tahoma", 0, 36)); // NOI18N
    jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
    jLabel1.setText("JAssemble");

    jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
    jLabel2.setText("©2015 Maxton Connor");

    jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
    jLabel5.setText("Version 0.2");

    javax.swing.GroupLayout AboutPopupLayout = new javax.swing.GroupLayout(AboutPopup.getContentPane());
    AboutPopup.getContentPane().setLayout(AboutPopupLayout);
    AboutPopupLayout.setHorizontalGroup(
      AboutPopupLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 300, Short.MAX_VALUE)
      .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
      .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
    );
    AboutPopupLayout.setVerticalGroup(
      AboutPopupLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(AboutPopupLayout.createSequentialGroup()
        .addComponent(jLabel1)
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addComponent(jLabel2)
        .addGap(18, 18, 18)
        .addComponent(jLabel5)
        .addGap(0, 54, Short.MAX_VALUE))
    );

    javax.swing.GroupLayout jFrame1Layout = new javax.swing.GroupLayout(jFrame1.getContentPane());
    jFrame1.getContentPane().setLayout(jFrame1Layout);
    jFrame1Layout.setHorizontalGroup(
      jFrame1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGap(0, 400, Short.MAX_VALUE)
    );
    jFrame1Layout.setVerticalGroup(
      jFrame1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGap(0, 300, Short.MAX_VALUE)
    );

    setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
    setTitle("JAssemble");
    setLocationByPlatform(true);
    setMinimumSize(new java.awt.Dimension(500, 500));
    setPreferredSize(new java.awt.Dimension(520, 540));
    getContentPane().setLayout(new java.awt.GridLayout(2, 1));

    jPanel2.setLayout(new java.awt.GridBagLayout());

    assemblyCodeLabel.setText("Assembly Code:");
    assemblyCodeLabel.setAlignmentY(0.0F);
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 0;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
    gridBagConstraints.insets = new java.awt.Insets(0, 13, 0, 3);
    jPanel2.add(assemblyCodeLabel, gridBagConstraints);

    assemblyTextArea.setColumns(20);
    assemblyTextArea.setRows(5);
    jScrollPane3.setViewportView(assemblyTextArea);

    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 1;
    gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.PAGE_START;
    gridBagConstraints.weightx = 1.0;
    gridBagConstraints.weighty = 1.0;
    gridBagConstraints.insets = new java.awt.Insets(0, 12, 0, 12);
    jPanel2.add(jScrollPane3, gridBagConstraints);

    assembleButton.setText("Assemble!");
    assembleButton.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        assembleButtonActionPerformed(evt);
      }
    });
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 2;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
    gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
    jPanel2.add(assembleButton, gridBagConstraints);

    getContentPane().add(jPanel2);

    jScrollPane2.setMaximumSize(new java.awt.Dimension(32767, 180));
    jScrollPane2.setMinimumSize(new java.awt.Dimension(2, 2));
    jScrollPane2.setPreferredSize(new java.awt.Dimension(480, 180));

    machineCode.setColumns(20);
    machineCode.setLineWrap(true);
    machineCode.setRows(5);
    jScrollPane2.setViewportView(machineCode);

    javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
    jPanel3.setLayout(jPanel3Layout);
    jPanel3Layout.setHorizontalGroup(
      jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(jPanel3Layout.createSequentialGroup()
        .addContainerGap()
        .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 456, Short.MAX_VALUE)
        .addContainerGap())
    );
    jPanel3Layout.setVerticalGroup(
      jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
        .addContainerGap()
        .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 235, Short.MAX_VALUE)
        .addContainerGap())
    );

    tabPane.addTab("Machine Code", jPanel3);

    errorTextArea.setEditable(false);
    errorTextArea.setColumns(20);
    errorTextArea.setRows(5);
    jScrollPane1.setViewportView(errorTextArea);

    javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
    jPanel4.setLayout(jPanel4Layout);
    jPanel4Layout.setHorizontalGroup(
      jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(jPanel4Layout.createSequentialGroup()
        .addContainerGap()
        .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 456, Short.MAX_VALUE)
        .addContainerGap())
    );
    jPanel4Layout.setVerticalGroup(
      jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(jPanel4Layout.createSequentialGroup()
        .addContainerGap()
        .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 235, Short.MAX_VALUE)
        .addContainerGap())
    );

    tabPane.addTab("Warnings/Errors", jPanel4);

    getContentPane().add(tabPane);

    jMenu1.setText("File");

    openFileMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_O, java.awt.event.InputEvent.CTRL_MASK));
    openFileMenuItem.setText("Open...");
    openFileMenuItem.setName("openFileMenuItem"); // NOI18N
    openFileMenuItem.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        openFileMenuItemActionPerformed(evt);
      }
    });
    jMenu1.add(openFileMenuItem);

    saveAssemblyMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, java.awt.event.InputEvent.CTRL_MASK));
    saveAssemblyMenuItem.setText("Save assembly");
    saveAssemblyMenuItem.setEnabled(false);
    saveAssemblyMenuItem.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        saveAssemblyMenuItemActionPerformed(evt);
      }
    });
    jMenu1.add(saveAssemblyMenuItem);

    saveAssemblyAsMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, java.awt.event.InputEvent.SHIFT_MASK | java.awt.event.InputEvent.CTRL_MASK));
    saveAssemblyAsMenuItem.setText("Save assembly as...");
    saveAssemblyAsMenuItem.setEnabled(false);
    saveAssemblyAsMenuItem.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        saveAssemblyAsMenuItemActionPerformed(evt);
      }
    });
    jMenu1.add(saveAssemblyAsMenuItem);

    jMenuBar1.add(jMenu1);

    jMenu2.setText("About");
    jMenu2.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseClicked(java.awt.event.MouseEvent evt) {
        jMenu2MouseClicked(evt);
      }
    });
    jMenuBar1.add(jMenu2);

    setJMenuBar(jMenuBar1);

    pack();
  }// </editor-fold>//GEN-END:initComponents

  private void jMenu2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jMenu2MouseClicked
    this.AboutPopup.setVisible(true);
  }//GEN-LAST:event_jMenu2MouseClicked

  private void openFileMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_openFileMenuItemActionPerformed
    this.openNewFile();
  }//GEN-LAST:event_openFileMenuItemActionPerformed

  private void saveAssemblyAsMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveAssemblyAsMenuItemActionPerformed
    if(fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
      this.currentFile = fileChooser.getSelectedFile();
      this.saveFile();
    }
  }//GEN-LAST:event_saveAssemblyAsMenuItemActionPerformed

  private void assembleButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_assembleButtonActionPerformed
    this.assemble();
  }//GEN-LAST:event_assembleButtonActionPerformed

  private void saveAssemblyMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveAssemblyMenuItemActionPerformed
    this.saveFile();
  }//GEN-LAST:event_saveAssemblyMenuItemActionPerformed

  /**
   * @param args the command line arguments
   */
  public static void main(String args[]) {
    /* Set the Nimbus look and feel */
    //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
    /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
     */
    try {
      javax.swing.UIManager.setLookAndFeel(javax.swing.UIManager.getSystemLookAndFeelClassName());
    } catch (ClassNotFoundException ex) {
      java.util.logging.Logger.getLogger(MainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
    } catch (InstantiationException ex) {
      java.util.logging.Logger.getLogger(MainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
    } catch (IllegalAccessException ex) {
      java.util.logging.Logger.getLogger(MainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
    } catch (javax.swing.UnsupportedLookAndFeelException ex) {
      java.util.logging.Logger.getLogger(MainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
    }
    //</editor-fold>

    /* Create and display the form */
    java.awt.EventQueue.invokeLater(new Runnable() {
      public void run() {
        new MainFrame().setVisible(true);
      }
    });
  }

  // Variables declaration - do not modify//GEN-BEGIN:variables
  private javax.swing.JDialog AboutPopup;
  private javax.swing.JButton assembleButton;
  private javax.swing.JLabel assemblyCodeLabel;
  private javax.swing.JTextArea assemblyTextArea;
  private javax.swing.JTextArea errorTextArea;
  private javax.swing.JFileChooser fileChooser;
  private javax.swing.JFrame jFrame1;
  private javax.swing.JLabel jLabel1;
  private javax.swing.JLabel jLabel2;
  private javax.swing.JLabel jLabel5;
  private javax.swing.JMenu jMenu1;
  private javax.swing.JMenu jMenu2;
  private javax.swing.JMenuBar jMenuBar1;
  private javax.swing.JPanel jPanel2;
  private javax.swing.JPanel jPanel3;
  private javax.swing.JPanel jPanel4;
  private javax.swing.JScrollPane jScrollPane1;
  private javax.swing.JScrollPane jScrollPane2;
  private javax.swing.JScrollPane jScrollPane3;
  private javax.swing.JTextArea machineCode;
  private javax.swing.JMenuItem openFileMenuItem;
  private javax.swing.JMenuItem saveAssemblyAsMenuItem;
  private javax.swing.JMenuItem saveAssemblyMenuItem;
  private javax.swing.JTabbedPane tabPane;
  // End of variables declaration//GEN-END:variables
}
