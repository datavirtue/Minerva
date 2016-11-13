/*
 * TextView.java
 *
 * Created on October 17, 2006, 9:17 AM
 */

/**
 *
 * @author  Sean K Anderson - Data Virtue 2006
 */
package com.datavirtue.nevitiumpro.Legacy.datavirtue;

import java.io.*;
//import javax.swing.text.PlainDocument;
import javax.swing.JEditorPane;
import javax.swing.text.*;

public class TextView extends javax.swing.JFrame {
    
    /** Creates new form TextView */
    public TextView(File file) {
        initComponents();
        textFile = file;
        
        
        jTextPane1.setContentType("text/plain");
        jTextPane1.setEditorKit(new javax.swing.text.StyledEditorKit());
        readFile(file);
        
        this.setTitle(file.getPath());
        java.awt.Dimension dim = DV.computeCenter((java.awt.Window) this);
        this.setLocation(dim.width, dim.height);
        
       
        this.setVisible(true);
        
        
        
    }
    
    public TextView (String text) {
        initComponents();
        
        //jTextPane1.setContentType("text/plain");
        jTextPane1.setEditorKit(new javax.swing.text.StyledEditorKit());
        
        SimpleAttributeSet as = new SimpleAttributeSet();
        
        StyleConstants.setFontFamily(as,"Monospaced");
        StyleConstants.setFontSize(as,10);
        
         DefaultStyledDocument dsd = new DefaultStyledDocument();
        
        try {
            dsd.insertString(0, text, as);
        } catch (BadLocationException ex) {
            ex.printStackTrace();
        }
                
        jTextPane1.setDocument(dsd);
        jTextPane1.validate();
        
        saveButton.setEnabled(false);
        java.awt.Dimension dim = DV.computeCenter((java.awt.Window) this);
        this.setLocation(dim.width, dim.height);
        this.setVisible(true);
        
    }
    
    
    private boolean readFile (File f) {
        
        StringBuilder sb = new StringBuilder ();
        String line = null;
        
        SimpleAttributeSet as = new SimpleAttributeSet();
        
        StyleConstants.setFontFamily(as,"Monospaced");
        StyleConstants.setFontSize(as,10);
        
        DefaultStyledDocument dsd = new DefaultStyledDocument();
       
        try {
            
            File data = f;
            BufferedReader in = new BufferedReader(
                                    new FileReader(data));
            do {
                
                line = in.readLine();
                if (line != null) sb.append( line + System.getProperty("line.separator") );
                  
            } while (line != null);
            
            
            // R E A D
            dsd.insertString(0, sb.toString(), as);
            jTextPane1.setDocument(dsd ); 
            jTextPane1.validate();
            

            // C L O S E
            in.close();




       } catch (Exception e) {e.printStackTrace();}
        
        return true;
    }
    
    
    private boolean writeFile (File f) {
        
        try {
                
            File data = f;  //??
            
            PrintWriter out = new PrintWriter(
                    new BufferedWriter( 
                     new FileWriter (data ) ) );
                //write text
            
                
            
           out.print(jTextPane1.getText());
            
            out.flush();
            out.close();
        } catch (Exception e) {e.printStackTrace();}

        return true;
        
        
    }
    
    private File textFile;
    private String text;
    
    
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        jPanel1 = new javax.swing.JPanel();
        cancelbutton = new javax.swing.JButton();
        saveButton = new javax.swing.JButton();
        printButton = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTextPane1 = new javax.swing.JTextPane();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));
        jPanel1.setFont(new java.awt.Font("OCR B MT", 0, 13));
        cancelbutton.setText("Close");
        cancelbutton.setToolTipText("Get Out");
        cancelbutton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelbuttonActionPerformed(evt);
            }
        });

        saveButton.setText("Save");
        saveButton.setToolTipText("Save Changes");
        saveButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveButtonActionPerformed(evt);
            }
        });

        printButton.setText("Print");
        printButton.setToolTipText("Convienience Utility");
        printButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                printButtonActionPerformed(evt);
            }
        });

        jScrollPane2.setViewportView(jTextPane1);

        org.jdesktop.layout.GroupLayout jPanel1Layout = new org.jdesktop.layout.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .add(jScrollPane2, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 655, Short.MAX_VALUE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 7, Short.MAX_VALUE)
                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING, false)
                    .add(printButton, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(saveButton, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(cancelbutton, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, jPanel1Layout.createSequentialGroup()
                        .add(printButton)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(saveButton)
                        .add(8, 8, 8)
                        .add(cancelbutton))
                    .add(jScrollPane2, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 475, Short.MAX_VALUE))
                .addContainerGap())
        );

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void printButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_printButtonActionPerformed
        
        /*
         *standard 8.5 x 11 inch
          page with 1 inch margins the rectangle X = 72, Y = 72, Width = 468,
          and Height = 648, the area 72, 72, 468, 648 will be painted regardless
          of which page is actually being printed.
         *
         */
        
        
        java.awt.print.Paper paper = new java.awt.print.Paper ();
        paper.setSize(540,720);
        paper.setImageableArea(36,36,540,684);  //half inch margins
        
        DocumentRenderer prn = new DocumentRenderer (paper);
        
        
        prn.print( jTextPane1 );
        
    }//GEN-LAST:event_printButtonActionPerformed

    private void cancelbuttonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelbuttonActionPerformed
        this.dispose();
    }//GEN-LAST:event_cancelbuttonActionPerformed

    private void saveButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveButtonActionPerformed
        
        writeFile(textFile);
        
    }//GEN-LAST:event_saveButtonActionPerformed
    
    /**
     * @param args the command line arguments
     */
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton cancelbutton;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTextPane jTextPane1;
    private javax.swing.JButton printButton;
    private javax.swing.JButton saveButton;
    // End of variables declaration//GEN-END:variables
    
}