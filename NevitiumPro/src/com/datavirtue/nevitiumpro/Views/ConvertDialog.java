/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * ConvertDialog.java
 *
 * Created on Jan 16, 2010, 11:05:31 PM
 */

package com.datavirtue.nevitiumpro.Views;

/**
 *
 * @author Data Virtue
 */
public class ConvertDialog extends javax.swing.JDialog {

    /** Creates new form ConvertDialog */
    public ConvertDialog(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();

        //browse to path of Nevitium 1.4 data folder
        //open db14 for 1.4 data
        //change paths of db14
        //convert  inventory and conn records over to 1.5

    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 300, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
    * @param args the command line arguments
    */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                ConvertDialog dialog = new ConvertDialog(new javax.swing.JFrame(), true);
                dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                    public void windowClosing(java.awt.event.WindowEvent e) {
                        System.exit(0);
                    }
                });
                dialog.setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables

}
