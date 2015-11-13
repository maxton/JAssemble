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

import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.AbstractAction;
import javax.swing.JFileChooser;
import javax.swing.KeyStroke;
import javax.swing.event.UndoableEditEvent;
import javax.swing.event.UndoableEditListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.text.Document;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import javax.swing.undo.UndoManager;

/**
 *
 * @author Max
 */
public class MainFrame extends javax.swing.JFrame {
  private SimulatorPanel simulator;
  private File assemblyFile, machineCodeFile;
  final UndoManager undo = new UndoManager();
  Document doc;
  /**
   * Creates new form MainFrame
   */
  public MainFrame() {
    initComponents();
    this.simulator = new SimulatorPanel();
    this.sim.add(simulator).setVisible(true);
  }
  
  /**
   * Shows the file open dialog for an assembly source file and loads it.
   */
  private void openNewFile() {
    fileChooser.setCurrentDirectory(assemblyFile);
    fileChooser.setFileFilter(new FileNameExtensionFilter("Assembly source (*.s)","s"));
    if(fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION){
      this.assemblyFile = fileChooser.getSelectedFile();
      try {
        this.assemblyTextArea.setText(Util.readFile(assemblyFile.getPath(), StandardCharsets.UTF_8));
        this.saveAssemblyAsMenuItem.setEnabled(true);
        this.saveAssemblyMenuItem.setEnabled(true);
        this.undo.discardAllEdits();
        this.setTitle(String.format("%s - JAssemble", assemblyFile));
      } catch (IOException ex) {
        Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
      }
    }
  }
  
  /**
   * Shows the file selection window and saves the current source code as a new file.
   */
  private void saveAs() {
    fileChooser.setSelectedFile(assemblyFile);
    fileChooser.setFileFilter(new FileNameExtensionFilter("Assembly source (*.s)","s"));
    if(fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
      this.assemblyFile = fileChooser.getSelectedFile();
      this.saveFile();
    }
  }
  
  /**
   * Shows the file selection window and saves the current machine code as a new file.
   */
  private void export() {
    if(machineCodeFile == null){
      if(assemblyFile != null){
        machineCodeFile = new File(assemblyFile.getAbsolutePath().replaceAll("\\.s", "") + ".coe");
      }
    }
    fileChooser.setSelectedFile(machineCodeFile);
    fileChooser.setFileFilter(new FileNameExtensionFilter("Coefficients File (*.coe)","coe"));
    if(fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
      machineCodeFile = fileChooser.getSelectedFile();
      try {
        Util.saveFile(machineCodeFile.getPath(), machineCode.getText());
      } catch (IOException ex) {
        Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
      }
    }
  }
  
  /**
   * Saves the currently open assembly file.
   */
  private void saveFile() {
    if(this.assemblyFile == null) 
      saveAs();
    else try {
      Util.saveFile(assemblyFile.getPath(), assemblyTextArea.getText());
      this.setTitle(String.format("%s - JAssemble", assemblyFile));
    } catch (IOException ex) {
      Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
    }
  }
  
  /**
   * Updates the title bar to tell the user that the file has been modified but not saved.
   */
  private void modified() {
    if(assemblyFile != null)
      this.setTitle(String.format("*%s - JAssemble", assemblyFile));
    else
      this.setTitle("*(new file) - JAssemble");
}
  
  /**
   * Implementation of MessagePasser to get warnings and errors on assembly.
   */
  private class MPImpl implements MessagePasser {
    @Override
    public void sendMessage(String msg) {
      outputTabPane.setSelectedIndex(1);
      errorTextArea.append(msg);
    }
  }
  
  /**
   * Attempts to assemble the code in the assembly text area.
   */
  private void assemble() {
    errorTextArea.setText("");
    Assembler as = new Assembler(assemblyTextArea.getText());
    try {
      as.assemble(new MPImpl());
      short[] assembledWords = as.getInstructionWords();
      int i = 0;
      machineCode.setText("memory_initialization_radix=16;\nmemory_initialization_vector=");
      for(short word : assembledWords) {
        machineCode.append(String.format("%1$04x", word));
        if(++i < assembledWords.length){
          machineCode.append(",");
        } else {
          machineCode.append(";\n");
        }
      }
      outputTabPane.setSelectedIndex(0);
    } catch (Exception ex) {
      outputTabPane.setSelectedIndex(1);
      errorTextArea.append("Couldn't assemble code:\n " + ex.getMessage() + "\n");
    }
  }
  
  /**
   * Attempts to assemble the code and initialize the simulator with the assembled instructions.
   */
  private void simulate() {
    errorTextArea.setText("");
    Assembler as = new Assembler(assemblyTextArea.getText());
    try {
      as.assemble(new MPImpl());
    } catch (Exception ex) {
      mainTabPane.setSelectedIndex(0);
      outputTabPane.setSelectedIndex(1);
      errorTextArea.append("Couldn't assemble code:\n " + ex.getMessage() + "\n");
      return;
    }
    this.simulator.setAssembler(as);
    mainTabPane.setSelectedIndex(1);
    this.sim.revalidate();
  }

  
  class PopupListener extends MouseAdapter {
    @Override
    public void mousePressed(MouseEvent e) {
        maybeShowPopup(e);
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        maybeShowPopup(e);
    }

    private void maybeShowPopup(MouseEvent e) {
        if (e.isPopupTrigger()) {
            codePopup.show(e.getComponent(),
                       e.getX(), e.getY());
        }
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
    codePopup = new javax.swing.JPopupMenu();
    ctxCutMenuItem = new javax.swing.JMenuItem();
    ctxCopyMenuItem = new javax.swing.JMenuItem();
    ctxPasteMenuItem = new javax.swing.JMenuItem();
    mainTabPane = new javax.swing.JTabbedPane();
    jSplitPane1 = new javax.swing.JSplitPane();
    editorPanel = new javax.swing.JPanel();
    assemblyCodeLabel = new javax.swing.JLabel();
    jScrollPane3 = new javax.swing.JScrollPane();
    assemblyTextArea = new javax.swing.JTextArea();
    assembleButton = new javax.swing.JButton();
    simulateButton = new javax.swing.JButton();
    outputTabPane = new javax.swing.JTabbedPane();
    jPanel3 = new javax.swing.JPanel();
    jScrollPane2 = new javax.swing.JScrollPane();
    machineCode = new javax.swing.JTextArea();
    jPanel4 = new javax.swing.JPanel();
    jScrollPane1 = new javax.swing.JScrollPane();
    errorTextArea = new javax.swing.JTextArea();
    sim = new javax.swing.JPanel();
    jMenuBar1 = new javax.swing.JMenuBar();
    fileMenu = new javax.swing.JMenu();
    openFileMenuItem = new javax.swing.JMenuItem();
    saveAssemblyMenuItem = new javax.swing.JMenuItem();
    saveAssemblyAsMenuItem = new javax.swing.JMenuItem();
    jSeparator2 = new javax.swing.JPopupMenu.Separator();
    exportMCMenuItem = new javax.swing.JMenuItem();
    editMenu = new javax.swing.JMenu();
    undoMenuItem = new javax.swing.JMenuItem();
    redoMenuItem = new javax.swing.JMenuItem();
    jSeparator1 = new javax.swing.JPopupMenu.Separator();
    cutMenuItem = new javax.swing.JMenuItem();
    copyMenuItem = new javax.swing.JMenuItem();
    pasteMenuItem = new javax.swing.JMenuItem();
    jMenu1 = new javax.swing.JMenu();
    jMenuItem1 = new javax.swing.JMenuItem();
    jMenuItem2 = new javax.swing.JMenuItem();
    helpMenu = new javax.swing.JMenu();
    aboutMenuItem = new javax.swing.JMenuItem();

    AboutPopup.setTitle("About JAssemble");
    AboutPopup.setLocationByPlatform(true);
    AboutPopup.setMinimumSize(new java.awt.Dimension(300, 150));
    AboutPopup.setModal(true);
    AboutPopup.setResizable(false);

    jLabel1.setFont(new java.awt.Font("Tahoma", 0, 36)); // NOI18N
    jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
    jLabel1.setText("JAssemble");

    jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
    jLabel2.setText("©2015 Maxton Connor");

    jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
    jLabel5.setText("Version 1.0.1");

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
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addComponent(jLabel5)
        .addGap(0, 40, Short.MAX_VALUE))
    );

    ctxCutMenuItem.setText("Cut");
    ctxCutMenuItem.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        ctxCutMenuItemActionPerformed(evt);
      }
    });
    codePopup.add(ctxCutMenuItem);

    ctxCopyMenuItem.setText("Copy");
    ctxCopyMenuItem.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        ctxCopyMenuItemActionPerformed(evt);
      }
    });
    codePopup.add(ctxCopyMenuItem);

    ctxPasteMenuItem.setText("Paste");
    ctxPasteMenuItem.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        ctxPasteMenuItemActionPerformed(evt);
      }
    });
    codePopup.add(ctxPasteMenuItem);

    setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
    setTitle("JAssemble");
    setLocationByPlatform(true);
    setMinimumSize(new java.awt.Dimension(520, 540));
    setPreferredSize(new java.awt.Dimension(520, 540));
    getContentPane().setLayout(new javax.swing.BoxLayout(getContentPane(), javax.swing.BoxLayout.LINE_AXIS));

    jSplitPane1.setDividerLocation(300);
    jSplitPane1.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);
    jSplitPane1.setResizeWeight(0.5);
    jSplitPane1.setDoubleBuffered(true);

    editorPanel.setLayout(new java.awt.GridBagLayout());

    assemblyCodeLabel.setText("Assembly Code:");
    assemblyCodeLabel.setAlignmentY(0.0F);
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 0;
    gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
    gridBagConstraints.insets = new java.awt.Insets(0, 13, 0, 3);
    editorPanel.add(assemblyCodeLabel, gridBagConstraints);

    assemblyTextArea.setColumns(20);
    assemblyTextArea.setRows(5);
    jScrollPane3.setViewportView(assemblyTextArea);
    tips4java.TextLineNumber lineNumbers = new tips4java.TextLineNumber(assemblyTextArea);
    lineNumbers.setMinimumDisplayDigits(2);
    jScrollPane3.setRowHeaderView(lineNumbers);

    MouseListener popupListener = new PopupListener();
    assemblyTextArea.addMouseListener(popupListener);

    doc = assemblyTextArea.getDocument();
    doc.addUndoableEditListener(new UndoableEditListener() {
      public void undoableEditHappened(UndoableEditEvent evt) {
        undo.addEdit(evt.getEdit());
        modified();
      }
    });

    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 1;
    gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
    gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.PAGE_START;
    gridBagConstraints.weightx = 1.0;
    gridBagConstraints.weighty = 1.0;
    gridBagConstraints.insets = new java.awt.Insets(0, 12, 0, 12);
    editorPanel.add(jScrollPane3, gridBagConstraints);

    assembleButton.setText("Assemble!");
    assembleButton.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        assembleButtonActionPerformed(evt);
      }
    });
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 2;
    gridBagConstraints.weightx = 0.5;
    gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
    editorPanel.add(assembleButton, gridBagConstraints);

    simulateButton.setText("Simulate!");
    simulateButton.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        simulateButtonActionPerformed(evt);
      }
    });
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 1;
    gridBagConstraints.gridy = 2;
    gridBagConstraints.weightx = 0.5;
    gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
    editorPanel.add(simulateButton, gridBagConstraints);

    jSplitPane1.setLeftComponent(editorPanel);

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
        .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 488, Short.MAX_VALUE)
        .addContainerGap())
    );
    jPanel3Layout.setVerticalGroup(
      jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
        .addContainerGap()
        .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 145, Short.MAX_VALUE)
        .addContainerGap())
    );

    outputTabPane.addTab("Machine Code", jPanel3);

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
        .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 488, Short.MAX_VALUE)
        .addContainerGap())
    );
    jPanel4Layout.setVerticalGroup(
      jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(jPanel4Layout.createSequentialGroup()
        .addContainerGap()
        .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 145, Short.MAX_VALUE)
        .addContainerGap())
    );

    outputTabPane.addTab("Warnings/Errors", jPanel4);

    jSplitPane1.setRightComponent(outputTabPane);

    mainTabPane.addTab("Edit", jSplitPane1);

    sim.setLayout(new javax.swing.BoxLayout(sim, javax.swing.BoxLayout.LINE_AXIS));
    mainTabPane.addTab("Simulate", sim);

    getContentPane().add(mainTabPane);

    fileMenu.setMnemonic('f');
    fileMenu.setText("File");

    openFileMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_O, java.awt.event.InputEvent.CTRL_MASK));
    openFileMenuItem.setMnemonic('o');
    openFileMenuItem.setText("Open...");
    openFileMenuItem.setName("openFileMenuItem"); // NOI18N
    openFileMenuItem.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        openFileMenuItemActionPerformed(evt);
      }
    });
    fileMenu.add(openFileMenuItem);

    saveAssemblyMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, java.awt.event.InputEvent.CTRL_MASK));
    saveAssemblyMenuItem.setMnemonic('s');
    saveAssemblyMenuItem.setText("Save assembly");
    saveAssemblyMenuItem.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        saveAssemblyMenuItemActionPerformed(evt);
      }
    });
    fileMenu.add(saveAssemblyMenuItem);

    saveAssemblyAsMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, java.awt.event.InputEvent.SHIFT_MASK | java.awt.event.InputEvent.CTRL_MASK));
    saveAssemblyAsMenuItem.setMnemonic('a');
    saveAssemblyAsMenuItem.setText("Save assembly as...");
    saveAssemblyAsMenuItem.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        saveAssemblyAsMenuItemActionPerformed(evt);
      }
    });
    fileMenu.add(saveAssemblyAsMenuItem);
    fileMenu.add(jSeparator2);

    exportMCMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_E, java.awt.event.InputEvent.CTRL_MASK));
    exportMCMenuItem.setMnemonic('e');
    exportMCMenuItem.setText("Export machine code...");
    exportMCMenuItem.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        exportMCMenuItemActionPerformed(evt);
      }
    });
    fileMenu.add(exportMCMenuItem);

    jMenuBar1.add(fileMenu);

    editMenu.setMnemonic('e');
    editMenu.setText("Edit");

    undoMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_Z, java.awt.event.InputEvent.CTRL_MASK));
    undoMenuItem.setMnemonic('u');
    undoMenuItem.setText("Undo");
    undoMenuItem.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        undoMenuItemActionPerformed(evt);
      }
    });
    editMenu.add(undoMenuItem);

    redoMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_Y, java.awt.event.InputEvent.CTRL_MASK));
    redoMenuItem.setMnemonic('r');
    redoMenuItem.setText("Redo");
    redoMenuItem.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        redoMenuItemActionPerformed(evt);
      }
    });
    editMenu.add(redoMenuItem);
    editMenu.add(jSeparator1);

    cutMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_X, java.awt.event.InputEvent.CTRL_MASK));
    cutMenuItem.setMnemonic('t');
    cutMenuItem.setText("Cut");
    cutMenuItem.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        cutMenuItemActionPerformed(evt);
      }
    });
    editMenu.add(cutMenuItem);

    copyMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_C, java.awt.event.InputEvent.CTRL_MASK));
    copyMenuItem.setMnemonic('y');
    copyMenuItem.setText("Copy");
    copyMenuItem.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        copyMenuItemActionPerformed(evt);
      }
    });
    editMenu.add(copyMenuItem);

    pasteMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_V, java.awt.event.InputEvent.CTRL_MASK));
    pasteMenuItem.setMnemonic('p');
    pasteMenuItem.setText("Paste");
    pasteMenuItem.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        pasteMenuItemActionPerformed(evt);
      }
    });
    editMenu.add(pasteMenuItem);

    jMenuBar1.add(editMenu);

    jMenu1.setMnemonic('r');
    jMenu1.setText("Run");
    jMenu1.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        simulateButtonActionPerformed(evt);
      }
    });

    jMenuItem1.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F3, 0));
    jMenuItem1.setMnemonic('a');
    jMenuItem1.setText("Assemble");
    jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        assembleButtonActionPerformed(evt);
      }
    });
    jMenu1.add(jMenuItem1);

    jMenuItem2.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F5, 0));
    jMenuItem2.setMnemonic('s');
    jMenuItem2.setText("Simulate");
    jMenuItem2.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        simulateButtonActionPerformed(evt);
      }
    });
    jMenu1.add(jMenuItem2);

    jMenuBar1.add(jMenu1);

    helpMenu.setMnemonic('h');
    helpMenu.setText("Help");

    aboutMenuItem.setMnemonic('a');
    aboutMenuItem.setText("About...");
    aboutMenuItem.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        aboutMenuItemActionPerformed(evt);
      }
    });
    helpMenu.add(aboutMenuItem);

    jMenuBar1.add(helpMenu);

    setJMenuBar(jMenuBar1);

    pack();
  }// </editor-fold>//GEN-END:initComponents

  private void openFileMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_openFileMenuItemActionPerformed
    this.openNewFile();
  }//GEN-LAST:event_openFileMenuItemActionPerformed

  private void saveAssemblyAsMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveAssemblyAsMenuItemActionPerformed
    saveAs();
  }//GEN-LAST:event_saveAssemblyAsMenuItemActionPerformed

  private void assembleButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_assembleButtonActionPerformed
    this.assemble();
  }//GEN-LAST:event_assembleButtonActionPerformed

  private void saveAssemblyMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveAssemblyMenuItemActionPerformed
    this.saveFile();
  }//GEN-LAST:event_saveAssemblyMenuItemActionPerformed

  private void simulateButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_simulateButtonActionPerformed
    this.simulate();
  }//GEN-LAST:event_simulateButtonActionPerformed

  private void redoMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_redoMenuItemActionPerformed
   redoCode();
  }//GEN-LAST:event_redoMenuItemActionPerformed

  private void undoMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_undoMenuItemActionPerformed
    undoCode();
  }//GEN-LAST:event_undoMenuItemActionPerformed

  private void cutMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cutMenuItemActionPerformed
    assemblyTextArea.cut();
  }//GEN-LAST:event_cutMenuItemActionPerformed

  private void copyMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_copyMenuItemActionPerformed
    assemblyTextArea.copy();
  }//GEN-LAST:event_copyMenuItemActionPerformed

  private void pasteMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_pasteMenuItemActionPerformed
    assemblyTextArea.paste();
  }//GEN-LAST:event_pasteMenuItemActionPerformed

  private void ctxCopyMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ctxCopyMenuItemActionPerformed
    assemblyTextArea.copy();
  }//GEN-LAST:event_ctxCopyMenuItemActionPerformed

  private void ctxPasteMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ctxPasteMenuItemActionPerformed
    assemblyTextArea.paste();
  }//GEN-LAST:event_ctxPasteMenuItemActionPerformed

  private void ctxCutMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ctxCutMenuItemActionPerformed
    assemblyTextArea.cut();
  }//GEN-LAST:event_ctxCutMenuItemActionPerformed

  private void aboutMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_aboutMenuItemActionPerformed
    this.AboutPopup.setVisible(true);
  }//GEN-LAST:event_aboutMenuItemActionPerformed

  private void exportMCMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exportMCMenuItemActionPerformed
    export();
  }//GEN-LAST:event_exportMCMenuItemActionPerformed

  private void undoCode(){
    try {
      if (undo.canUndo()) {
        undo.undo();
      }
    } catch (CannotUndoException e) {
    }
  }
  private void redoCode(){
    try {
      if (undo.canRedo()) {
        undo.redo();
      }
    } catch (CannotRedoException e) {
    }
  }
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
  private javax.swing.JMenuItem aboutMenuItem;
  private javax.swing.JButton assembleButton;
  private javax.swing.JLabel assemblyCodeLabel;
  private javax.swing.JTextArea assemblyTextArea;
  private javax.swing.JPopupMenu codePopup;
  private javax.swing.JMenuItem copyMenuItem;
  private javax.swing.JMenuItem ctxCopyMenuItem;
  private javax.swing.JMenuItem ctxCutMenuItem;
  private javax.swing.JMenuItem ctxPasteMenuItem;
  private javax.swing.JMenuItem cutMenuItem;
  private javax.swing.JMenu editMenu;
  private javax.swing.JPanel editorPanel;
  private javax.swing.JTextArea errorTextArea;
  private javax.swing.JMenuItem exportMCMenuItem;
  private javax.swing.JFileChooser fileChooser;
  private javax.swing.JMenu fileMenu;
  private javax.swing.JMenu helpMenu;
  private javax.swing.JLabel jLabel1;
  private javax.swing.JLabel jLabel2;
  private javax.swing.JLabel jLabel5;
  private javax.swing.JMenu jMenu1;
  private javax.swing.JMenuBar jMenuBar1;
  private javax.swing.JMenuItem jMenuItem1;
  private javax.swing.JMenuItem jMenuItem2;
  private javax.swing.JPanel jPanel3;
  private javax.swing.JPanel jPanel4;
  private javax.swing.JScrollPane jScrollPane1;
  private javax.swing.JScrollPane jScrollPane2;
  private javax.swing.JScrollPane jScrollPane3;
  private javax.swing.JPopupMenu.Separator jSeparator1;
  private javax.swing.JPopupMenu.Separator jSeparator2;
  private javax.swing.JSplitPane jSplitPane1;
  private javax.swing.JTextArea machineCode;
  private javax.swing.JTabbedPane mainTabPane;
  private javax.swing.JMenuItem openFileMenuItem;
  private javax.swing.JTabbedPane outputTabPane;
  private javax.swing.JMenuItem pasteMenuItem;
  private javax.swing.JMenuItem redoMenuItem;
  private javax.swing.JMenuItem saveAssemblyAsMenuItem;
  private javax.swing.JMenuItem saveAssemblyMenuItem;
  private javax.swing.JPanel sim;
  private javax.swing.JButton simulateButton;
  private javax.swing.JMenuItem undoMenuItem;
  // End of variables declaration//GEN-END:variables
}
