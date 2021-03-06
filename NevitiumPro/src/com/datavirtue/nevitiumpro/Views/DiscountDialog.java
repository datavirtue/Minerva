/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * DiscountDialog.java
 *
 * Created on Nov 30, 2011, 11:28:46 PM
 */
package com.datavirtue.nevitiumpro.Views;

import com.datavirtue.nevitiumpro.Legacy.RuntimeManagement.AppSettings;
import com.datavirtue.nevitiumpro.Util.JTextFieldFilter;
import com.datavirtue.nevitiumpro.Util.LimitedDocument;
import com.datavirtue.nevitiumpro.Legacy.datavirtue.AutoCompleteDocument;
import com.datavirtue.nevitiumpro.Legacy.datavirtue.DV;
import com.datavirtue.nevitiumpro.Legacy.datavirtue.DbEngine;



import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import javax.swing.table.TableModel;

/**
 *
 * @author sean
 */
public class DiscountDialog extends javax.swing.JDialog {
    Image winIcon;
    /** Creates new form DiscountDialog */
    public DiscountDialog(java.awt.Frame parent, boolean modal, AppSettings application, String desc, float total, boolean tx1, boolean tx2) {
        super(parent, modal);
        
        Toolkit tools = Toolkit.getDefaultToolkit();
        winIcon = tools.getImage(getClass().getResource("/com/datavirtue/nevitiumpro/res/Aha-16/enabled/Percent.png"));

        initComponents();
        discItem = new Object [16];
        db = application.getDb();
        priceTotal = total;
        tax1 = tx1;
        tax2 = tx2;
        //Setup textbox fields with data restrictions
        this.discDescField.setDocument(new LimitedDocument(50));
        percentageField.setDocument(new JTextFieldFilter(JTextFieldFilter.FLOAT));
        
        this.populateItemList();
        
        discDescField.setText(desc);
        percentageField.requestFocus();
        
        java.awt.Dimension dim = DV.computeCenter((java.awt.Window) this);
        this.setLocation(dim.width, dim.height);
       
        
        
        this.setVisible(true);
        
    }

    
    boolean stat = false;
    public Object [] getDisc(){
        return discItem;
        
    }
    
    public boolean getStat(){
        return stat;
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        discDescField = new javax.swing.JTextField();
        percentageField = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        applyButton = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        amountField = new javax.swing.JTextField();
        autoInsertBox = new javax.swing.JCheckBox();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Item Discount");
        setResizable(false);

        jLabel1.setText("Discount Description");

        discDescField.setFont(new java.awt.Font("Dialog", 0, 18)); // NOI18N
        discDescField.setNextFocusableComponent(percentageField);
        discDescField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                discDescFieldKeyPressed(evt);
            }
        });

        percentageField.setFont(new java.awt.Font("Dialog", 0, 18)); // NOI18N
        percentageField.setNextFocusableComponent(discDescField);
        percentageField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                percentageFieldKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                percentageFieldKeyReleased(evt);
            }
        });

        jLabel2.setText("Percentage");

        applyButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/businessmanager/res/Aha-16/enabled/Apply.png"))); // NOI18N
        applyButton.setText("Apply");
        applyButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                applyButtonActionPerformed(evt);
            }
        });

        jLabel3.setText("Amount");

        amountField.setEditable(false);
        amountField.setFont(new java.awt.Font("Dialog", 0, 18)); // NOI18N

        autoInsertBox.setSelected(true);
        autoInsertBox.setText("Auto Insert %");
        autoInsertBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                autoInsertBoxActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1)
                            .addComponent(discDescField, javax.swing.GroupLayout.PREFERRED_SIZE, 364, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(percentageField)
                            .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel3)
                            .addComponent(amountField, javax.swing.GroupLayout.DEFAULT_SIZE, 100, Short.MAX_VALUE)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(autoInsertBox)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 378, Short.MAX_VALUE)
                        .addComponent(applyButton)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jLabel2)
                    .addComponent(jLabel3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(discDescField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(percentageField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(amountField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(applyButton)
                    .addComponent(autoInsertBox))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void applyButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_applyButtonActionPerformed
        
        applyDiscount();
        
    }//GEN-LAST:event_applyButtonActionPerformed
    String kptemp;
    int percentIndex = -1;
    private void percentageFieldKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_percentageFieldKeyReleased
        float calc = ((DV.parseFloat(percentageField.getText()) * .01f) * priceTotal);
        amountField.setText(DV.money(calc * -1));
        kptemp = discDescField.getText();
        
        percentIndex = kptemp.indexOf("%");
        if (!autoInsertBox.isSelected()) return;
        if (percentageField.getText().length() == 0){
            percentIndex += 2;
            kptemp = kptemp.substring(percentIndex, kptemp.length());
            discDescField.setText(kptemp);
            return;
        }else if (percentIndex > -1) {
            if (percentIndex > 0) percentIndex+=2;
            kptemp = kptemp.substring(percentIndex, kptemp.length());
        } 
               
        discDescField.setText(percentageField.getText() + "% " + kptemp);
    }//GEN-LAST:event_percentageFieldKeyReleased

    private void percentageFieldKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_percentageFieldKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) applyDiscount();
        if (evt.getKeyCode() == KeyEvent.VK_ESCAPE) {
            stat = false;
            this.setVisible(false);
        }
        
    }//GEN-LAST:event_percentageFieldKeyPressed

    private void autoInsertBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_autoInsertBoxActionPerformed
        
        kptemp = discDescField.getText();
        percentIndex = kptemp.indexOf("%");
        if (percentIndex < 0) return;
        percentIndex += 2;
        kptemp = kptemp.substring(percentIndex, kptemp.length());
        discDescField.setText(kptemp);
        percentageField.requestFocus();
        
    }//GEN-LAST:event_autoInsertBoxActionPerformed

    private void discDescFieldKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_discDescFieldKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) percentageField.requestFocus();
        if (evt.getKeyCode() == KeyEvent.VK_ESCAPE) {
            stat = false;
            this.setVisible(false);
        }
    }//GEN-LAST:event_discDescFieldKeyPressed

    private Object [] discItem;
    private java.util.ArrayList itemList;
    private DbEngine db;
    private float priceTotal;
    private boolean tax1, tax2;
    
    private void applyDiscount(){
        //build item to insert in invoicedialog
        //discItem = new Object[16];
        float unit  = 0.00f;
        float costf = 0.00f;
        unit = DV.parseFloat(amountField.getText());
        
        discItem [2] = "DISC";  //code
        
        String note = discDescField.getText();
                
        discItem [3] = note;  //desc
        discItem [7] = new Float (costf);  //cost
        discItem [8] = new Float (unit);  //unit
        discItem [13] = tax1;  //t1
        discItem [14] = tax2;  //t2
                
        this.normalizeItemList(discDescField.getText());
        stat = true;
        this.setVisible(false);    
    }
    
    private void populateItemList () {
        
         itemList = new java.util.ArrayList();
        itemList.trimToSize();
        
        TableModel cat_tm = db.createTableModel("miscitems");
        
        if (cat_tm != null && cat_tm.getRowCount() > 0){
            
            for (int r = 0; r < cat_tm.getRowCount(); r++){
            
                itemList.add((String) cat_tm.getValueAt(r, 1));
                
            }
        
         
        }else {
            
            itemList.add("N/A");
        }
        
        discDescField.setDocument(new AutoCompleteDocument( discDescField, itemList ));
        
        
    }
    
    private void normalizeItemList(String s) {
        
        
        String txm;
        
        java.util.ArrayList al;
        
        al = db.search("miscitems", 1, s, false);
        
        if (al == null){
            
            db.saveRecord("miscitems",new Object [] {new Integer(0), s} ,false);
           
        }
        
        
    }
    
    
 
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField amountField;
    private javax.swing.JButton applyButton;
    private javax.swing.JCheckBox autoInsertBox;
    private javax.swing.JTextField discDescField;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JTextField percentageField;
    // End of variables declaration//GEN-END:variables
}
