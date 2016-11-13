/*
 * SecurityManager.java
 *
 * Created on July 26, 2007, 11:49 AM
 */
package com.datavirtue.nevitiumpro.Views;

import com.datavirtue.nevitiumpro.Views.ExceptionErrorDialog;
import com.datavirtue.nevitiumpro.Legacy.RuntimeManagement.AppSettings;
import com.datavirtue.nevitiumpro.Legacy.datavirtue.DV;
import com.datavirtue.nevitiumpro.Legacy.datavirtue.PBE;
import com.datavirtue.nevitiumpro.entities.UserAccounts;
import com.datavirtue.nevitiumpro.hibernate.BeanTableModel;
import com.datavirtue.nevitiumpro.hibernate.HibernateUtil;
import com.datavirtue.nevitiumpro.hibernate.TableUtil;
import com.datavirtue.nevitiumpro.Model.SecurityDAO;
import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.awt.Toolkit;
import java.util.ArrayList;
import javax.swing.JLabel;

/**
 *
 * @author Data Virtue
 */
public class SecurityManager extends javax.swing.JDialog {

    private AppSettings application;
    private UserAccounts localBean;
    private SecurityDAO dao;
    
    /**
     * Creates new form SecurityManager
     */
    public SecurityManager(java.awt.Frame parent, boolean modal, AppSettings application) {
        super(parent, modal);
        initComponents();

        Toolkit tools = Toolkit.getDefaultToolkit();
        Image winIcon = tools.getImage(getClass().getResource("/com/datavirtue/nevitiumpro/res/Orange.png"));
        this.setIconImage(winIcon);

        java.awt.Dimension dim = DV.computeCenter((java.awt.Window) this);
        this.setLocation(dim.width, dim.height);

        this.application = application;
        dao = new SecurityDAO((HibernateUtil) application.getJPA());
        init();

    }

    private void init() {

        refreshTable();
        setStatus();

    }

    private void setStatus() {
        UserAccounts userAcct;
        BeanTableModel btm = (BeanTableModel)userTable.getModel();
        for (int r = 0; r < btm.getRowCount(); r++){
            userAcct = (UserAccounts)btm.getBeanAt(r);            
            if (userAcct.getUsername().equalsIgnoreCase(application.getLangText().getString("master"))){
                if (userAcct.getPassword().isEmpty()){
                    statBox.setBackground(Color.RED);
                    statBox.setText(application.getLangText().getString("disabled").toUpperCase(application.getLangText().getLocale()));
                    return;
                }else{
                    statBox.setBackground(Color.GREEN);
                    statBox.setText(application.getLangText().getString("enabled").toUpperCase(application.getLangText().getLocale()));
                    return;
                }
            }
            
        }                
    }

    private void refreshTable() {
        
        BeanTableModel btm = dao.getTableModelAll(0);
        userTable.setModel(btm);
        
        ArrayList<String> list = new ArrayList<String>();
        list.add("Username");
        list.add("Master");
        ArrayList<Integer> widths = new ArrayList<Integer>();
        widths.add(60);
        widths.add(39);
        
        try {
            TableUtil.removeAllCols(userTable, list);
            TableUtil.orderColumns(userTable, list);
            TableUtil.sizeColumns(userTable, widths);
        } catch (Exception ex) {
            ExceptionErrorDialog errDialog = new ExceptionErrorDialog(ex);
            errDialog.pack();
            errDialog.setVisible(true);
        }

        
    }
    
    private void resetPassword() {
        if (localBean == null) {
            return;
        }
        String password1, password2;
        password1 = new String(passField1.getPassword());
        password2 = new String(passField2.getPassword());

        if (password1.equals("") && password2.equals("")) {

            /* Prevent master from being assigned as a regular user */
            if (localBean.getUsername().equalsIgnoreCase(application.getLangText().getString("master"))) {
                masterRadio.setSelected(true);
            }
            localBean.setPassword("");
            dao.transactionSave(localBean, application.getTimeout());
            passField1.setText("");
            passField2.setText("");
            javax.swing.JOptionPane.showMessageDialog(null, "Password for " + localBean.getUsername() + " reset.");
            resetButton.setEnabled(false);
            return;
        }
        if (password1.equals(password2)) {
            try {
                // Try to encrypt and the ndecrypt the password to verify integrity 
                String cipher = PBE.encrypt(passField1.getPassword(), password1);
                String uncipher = PBE.decrypt(passField1.getPassword(), cipher);
                if (uncipher.equals(password1)) {

                    /* Prevent master from being assigned as a regular user */
                    if (localBean.getUsername().equalsIgnoreCase(application.getLangText().getString("master"))) {
                        masterRadio.setSelected(true);
                    }
                    localBean.setPassword(cipher);
                    dao.transactionSave(localBean, application.getTimeout());

                    passField1.setText("");
                    passField2.setText("");

                    javax.swing.JOptionPane.showMessageDialog(null, "Password for " + localBean.getUsername() + " reset.");
                    resetButton.setEnabled(false);
                    return;

                } else {
                    javax.swing.JOptionPane.showMessageDialog(null, "The cipher did not compute!");
                }
            } catch (Exception ex) {
                ExceptionErrorDialog errDialog = new ExceptionErrorDialog(ex);
                errDialog.pack();
                errDialog.setVisible(true);
            }
        } else {
            javax.swing.JOptionPane.showMessageDialog(null, "The two passwords did not match.  Try Again.");
        }
    }

    private void viewLog() {
    }

    private void updateRole() {

        /* Prevent master from being assigned as a regular user */

        if (localBean.getUsername().equalsIgnoreCase("master")) {
            masterRadio.setSelected(true);
        }

        localBean.setInventory(inventorySlider.getValue());
        localBean.setConnections(connectionsSlider.getValue());
        localBean.setInvoiceManager(managerSlider.getValue());
        localBean.setQuotes(quotesSlider.getValue());
        localBean.setInvoices(invoicesSlider.getValue());
        localBean.setReports(reportsSlider.getValue());
        localBean.setChecks(checksSlider.getValue());
        localBean.setExports(exportsSlider.getValue());
        refreshTable();

    }

    private void newAccount() {
        localBean = new UserAccounts();
        UserAccounts masterBean = new UserAccounts();

        String iValue = javax.swing.JOptionPane.showInputDialog("Type a user name.");
        /* Use the saved access restrictions from the master user */

        BeanTableModel btm = dao.getBeanTableModelByHQL("from UserAccounts where userName = "+application.getLangText().getString("master"));


        if (btm.getRowCount() == 1) {
            masterBean = (UserAccounts) btm.getBeanAt(0);
        } else {
            javax.swing.JOptionPane.showMessageDialog(null,
                    "You cannot add users without a Master account being present.");
            return;
        }

        if (iValue != null) {
            //check for user
            btm = dao.getBeanTableModelByHQL("from UserAccounts where userName=" + iValue);
            if (btm.getRowCount() > 0) {
                javax.swing.JOptionPane.showMessageDialog(null,
                        "This user (" + iValue + ") already exsists.");
                return;
            }

            localBean.setUsername(iValue);
            localBean.setInventory(masterBean.getInventory());
            localBean.setQuotes(masterBean.getQuotes());
            localBean.setInvoices(masterBean.getInvoices());
            localBean.setConnections(masterBean.getConnections());
            localBean.setInvoiceManager(masterBean.getInvoiceManager());
            localBean.setReports(masterBean.getReports());
            localBean.setChecks(masterBean.getChecks());
            localBean.setExports(masterBean.getExports());
            localBean.setMaster(false);
            dao.transactionSave(localBean, application.getTimeout());
            refreshTable();

        }

    }

    private void removeAccount() {
        if (localBean == null) {
            return;
        }


        /* Prevent master from being assigned as a regular user */
        String u = localBean.getUsername();

        if (u.equalsIgnoreCase(application.getLangText().getString("master"))) {
            javax.swing.JOptionPane.showMessageDialog(null, "You cannot delete the master account.");
        } else {
            dao.transactionDelete(localBean, application.getTimeout());
        }

    }

    private void populateUser() {

        int r = userTable.getSelectedRow();

        if (r > -1) {

            BeanTableModel btm = (BeanTableModel) userTable.getModel();
            localBean = (UserAccounts) btm.getBeanAt(r);

            boolean master = localBean.getMaster();

            if (master) {
                masterRadio.setSelected(true);
            } else {
                userRadio.setSelected(true);
            }

            if (localBean.getUsername().equalsIgnoreCase(application.getLangText().getString("master"))) {

                helpBox.setText(""
                        + "The Master account security settings do not apply to "
                        + "the Master account.  They are used as a template for "
                        + "each new user account that you create.  This allows you "
                        + "to easily create user accounts with a set of standard "
                        + "access restrictions." + nl + nl
                        + "To enable security you must set a password for the 'Master' "
                        + "user account.  To disable security: set a blank "
                        + "password for the 'Master' user.");
            }

            inventorySlider.setValue(localBean.getInventory());
            connectionsSlider.setValue(localBean.getConnections());
            invoicesSlider.setValue(localBean.getInvoices());
            quotesSlider.setValue(localBean.getQuotes());
            managerSlider.setValue(localBean.getInvoiceManager());
            reportsSlider.setValue(localBean.getReports());
            checksSlider.setValue(localBean.getChecks());
            exportsSlider.setValue(localBean.getExports());

            saveButton.setEnabled(true);
            resetButton.setEnabled(true);

        }



    }

    /**
     * This method is called from within the constructor to initialize the form. WARNING: Do NOT modify this code. The
     * content of this method is always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        userTable = new javax.swing.JTable();
        statBox = new javax.swing.JTextField();
        jToolBar1 = new javax.swing.JToolBar();
        newButton = new javax.swing.JButton();
        removeButton = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        passField1 = new javax.swing.JPasswordField();
        passField2 = new javax.swing.JPasswordField();
        resetButton = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        helpBox = new javax.swing.JTextPane();
        jLabel4 = new javax.swing.JLabel();
        masterRadio = new javax.swing.JRadioButton();
        userRadio = new javax.swing.JRadioButton();
        jToolBar2 = new javax.swing.JToolBar();
        saveButton = new javax.swing.JButton();
        connectionsSlider = new javax.swing.JSlider();
        inventorySlider = new javax.swing.JSlider();
        managerSlider = new javax.swing.JSlider();
        exportsSlider = new javax.swing.JSlider();
        invoicesSlider = new javax.swing.JSlider();
        reportsSlider = new javax.swing.JSlider();
        checksSlider = new javax.swing.JSlider();
        quotesSlider = new javax.swing.JSlider();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Nevitium Security Manager");

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("User Accounts"));

        userTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Boolean.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        userTable.setSelectionBackground(new java.awt.Color(204, 255, 255));
        userTable.setSelectionForeground(new java.awt.Color(0, 0, 0));
        userTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                userTableMouseClicked(evt);
            }
        });
        userTable.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                userTableKeyReleased(evt);
            }
        });
        jScrollPane1.setViewportView(userTable);

        statBox.setEditable(false);
        statBox.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        statBox.setText("Security Status");

        jToolBar1.setFloatable(false);
        jToolBar1.setRollover(true);

        newButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/datavirtue/nevitiumpro/res/Aha-16/enabled/Security.png"))); // NOI18N
        newButton.setText("New ");
        newButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                newButtonActionPerformed(evt);
            }
        });
        jToolBar1.add(newButton);

        removeButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/datavirtue/nevitiumpro/res/Aha-16/enabled/Delete.png"))); // NOI18N
        removeButton.setText("Remove");
        removeButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                removeButtonActionPerformed(evt);
            }
        });
        jToolBar1.add(removeButton);

        jPanel3.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel2.setFont(new java.awt.Font("Cantarell", 0, 14)); // NOI18N
        jLabel2.setText("User Password (Enter twice)");

        resetButton.setText("(Re)Set Password");
        resetButton.setEnabled(false);
        resetButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                resetButtonActionPerformed(evt);
            }
        });

        org.jdesktop.layout.GroupLayout jPanel3Layout = new org.jdesktop.layout.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(resetButton, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, passField2)
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, passField1)
                    .add(jLabel2, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel3Layout.createSequentialGroup()
                .add(6, 6, 6)
                .add(jLabel2)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                .add(passField1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                .add(passField2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(resetButton)
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        org.jdesktop.layout.GroupLayout jPanel1Layout = new org.jdesktop.layout.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                    .add(org.jdesktop.layout.GroupLayout.LEADING, jScrollPane1)
                    .add(org.jdesktop.layout.GroupLayout.LEADING, jToolBar1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(statBox)
                    .add(org.jdesktop.layout.GroupLayout.LEADING, jPanel3, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, jPanel1Layout.createSequentialGroup()
                .add(jScrollPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 552, Short.MAX_VALUE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel3, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                .add(statBox, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(18, 18, 18)
                .add(jToolBar1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("User Rights"));

        jScrollPane2.setViewportView(helpBox);

        jLabel4.setText("User Role: ");

        buttonGroup1.add(masterRadio);
        masterRadio.setText("Master");
        masterRadio.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        masterRadio.setNextFocusableComponent(userRadio);

        buttonGroup1.add(userRadio);
        userRadio.setSelected(true);
        userRadio.setText("Restricted");
        userRadio.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        userRadio.setNextFocusableComponent(inventorySlider);
        userRadio.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                userRadioActionPerformed(evt);
            }
        });

        jToolBar2.setFloatable(false);
        jToolBar2.setRollover(true);

        saveButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/datavirtue/nevitiumpro/res/Aha-16/enabled/Floppy.png"))); // NOI18N
        saveButton.setText("Save User Permissions");
        saveButton.setEnabled(false);
        saveButton.setNextFocusableComponent(masterRadio);
        saveButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveButtonActionPerformed(evt);
            }
        });
        jToolBar2.add(saveButton);

        connectionsSlider.setMajorTickSpacing(100);
        connectionsSlider.setMaximum(500);
        connectionsSlider.setMinimum(100);
        connectionsSlider.setMinorTickSpacing(100);
        connectionsSlider.setPaintLabels(true);
        connectionsSlider.setSnapToTicks(true);
        connectionsSlider.setToolTipText("Business Contact Management Permissions");
        connectionsSlider.setBorder(javax.swing.BorderFactory.createTitledBorder("My Connections"));
        connectionsSlider.setNextFocusableComponent(managerSlider);

        inventorySlider.setMajorTickSpacing(100);
        inventorySlider.setMaximum(500);
        inventorySlider.setMinimum(100);
        inventorySlider.setMinorTickSpacing(100);
        inventorySlider.setPaintLabels(true);
        inventorySlider.setSnapToTicks(true);
        inventorySlider.setToolTipText("Inventory Access Permissions");
        inventorySlider.setBorder(javax.swing.BorderFactory.createTitledBorder("Inventory"));
        inventorySlider.setNextFocusableComponent(connectionsSlider);

        managerSlider.setMajorTickSpacing(100);
        managerSlider.setMaximum(500);
        managerSlider.setMinimum(100);
        managerSlider.setMinorTickSpacing(100);
        managerSlider.setPaintLabels(true);
        managerSlider.setSnapToTicks(true);
        managerSlider.setToolTipText("Invoice Management Permissions");
        managerSlider.setBorder(javax.swing.BorderFactory.createTitledBorder("Invoice Manager"));
        managerSlider.setNextFocusableComponent(invoicesSlider);

        exportsSlider.setMajorTickSpacing(100);
        exportsSlider.setMaximum(500);
        exportsSlider.setMinimum(100);
        exportsSlider.setMinorTickSpacing(100);
        exportsSlider.setPaintLabels(true);
        exportsSlider.setSnapToTicks(true);
        exportsSlider.setToolTipText("Data Export Restrictions");
        exportsSlider.setBorder(javax.swing.BorderFactory.createTitledBorder("Exports"));
        exportsSlider.setNextFocusableComponent(saveButton);

        invoicesSlider.setMajorTickSpacing(100);
        invoicesSlider.setMaximum(500);
        invoicesSlider.setMinimum(100);
        invoicesSlider.setMinorTickSpacing(100);
        invoicesSlider.setPaintLabels(true);
        invoicesSlider.setSnapToTicks(true);
        invoicesSlider.setToolTipText("Invoice Permissions");
        invoicesSlider.setBorder(javax.swing.BorderFactory.createTitledBorder("Invoices"));
        invoicesSlider.setNextFocusableComponent(reportsSlider);

        reportsSlider.setMajorTickSpacing(100);
        reportsSlider.setMaximum(500);
        reportsSlider.setMinimum(100);
        reportsSlider.setMinorTickSpacing(100);
        reportsSlider.setPaintLabels(true);
        reportsSlider.setSnapToTicks(true);
        reportsSlider.setToolTipText("Report Printing Restrictions");
        reportsSlider.setBorder(javax.swing.BorderFactory.createTitledBorder("Reports"));
        reportsSlider.setNextFocusableComponent(checksSlider);

        checksSlider.setMajorTickSpacing(100);
        checksSlider.setMaximum(500);
        checksSlider.setMinimum(100);
        checksSlider.setMinorTickSpacing(100);
        checksSlider.setPaintLabels(true);
        checksSlider.setSnapToTicks(true);
        checksSlider.setToolTipText("Check Printing Permissions");
        checksSlider.setBorder(javax.swing.BorderFactory.createTitledBorder("Checks"));
        checksSlider.setNextFocusableComponent(exportsSlider);

        quotesSlider.setMajorTickSpacing(100);
        quotesSlider.setMaximum(500);
        quotesSlider.setMinimum(100);
        quotesSlider.setMinorTickSpacing(100);
        quotesSlider.setPaintLabels(true);
        quotesSlider.setSnapToTicks(true);
        quotesSlider.setToolTipText("Quote Permissions");
        quotesSlider.setBorder(javax.swing.BorderFactory.createTitledBorder("Quotes"));
        quotesSlider.setNextFocusableComponent(reportsSlider);

        org.jdesktop.layout.GroupLayout jPanel2Layout = new org.jdesktop.layout.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, jToolBar2, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(jPanel2Layout.createSequentialGroup()
                        .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING, false)
                            .add(org.jdesktop.layout.GroupLayout.LEADING, quotesSlider, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .add(org.jdesktop.layout.GroupLayout.LEADING, checksSlider, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .add(org.jdesktop.layout.GroupLayout.LEADING, invoicesSlider, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .add(org.jdesktop.layout.GroupLayout.LEADING, jPanel2Layout.createSequentialGroup()
                                .add(jLabel4)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(masterRadio)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(userRadio))
                            .add(org.jdesktop.layout.GroupLayout.LEADING, inventorySlider, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .add(org.jdesktop.layout.GroupLayout.LEADING, managerSlider, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .add(org.jdesktop.layout.GroupLayout.LEADING, exportsSlider, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .add(reportsSlider, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .add(org.jdesktop.layout.GroupLayout.LEADING, jScrollPane2)
                            .add(org.jdesktop.layout.GroupLayout.LEADING, connectionsSlider, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .add(0, 20, Short.MAX_VALUE))))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel4)
                    .add(masterRadio)
                    .add(userRadio))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                .add(inventorySlider, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(connectionsSlider, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(managerSlider, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(quotesSlider, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(invoicesSlider, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(reportsSlider, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(checksSlider, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(exportsSlider, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jScrollPane2)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jToolBar2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 25, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                .add(jPanel2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING, false)
                    .add(org.jdesktop.layout.GroupLayout.LEADING, jPanel1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(org.jdesktop.layout.GroupLayout.LEADING, jPanel2, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void removeButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_removeButtonActionPerformed

        removeAccount();

    }//GEN-LAST:event_removeButtonActionPerformed

    private void resetButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_resetButtonActionPerformed

        resetPassword();
        setStatus();

    }//GEN-LAST:event_resetButtonActionPerformed

    private void newButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_newButtonActionPerformed

        newAccount();
        setStatus();


    }//GEN-LAST:event_newButtonActionPerformed

    private void userTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_userTableMouseClicked
        int mouseButton = evt.getButton();
        if (mouseButton == evt.BUTTON2 || mouseButton == evt.BUTTON3) {
            return;
        }
        populateUser();
    }//GEN-LAST:event_userTableMouseClicked

    private void userTableKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_userTableKeyReleased
        populateUser();
    }//GEN-LAST:event_userTableKeyReleased

    private void saveButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveButtonActionPerformed
        updateRole();
        saveButton.setEnabled(false);
        setStatus();
    }//GEN-LAST:event_saveButtonActionPerformed

    private void userRadioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_userRadioActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_userRadioActionPerformed

    private void setCurrentFontBold(JLabel label) {
        label.setFont(label.getFont().deriveFont(Font.BOLD));
    }

    private void setCurrentFontPlain(JLabel label) {
        label.setFont(label.getFont().deriveFont(Font.PLAIN));
    }
    private String nl = System.getProperty("line.separator");
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JSlider checksSlider;
    private javax.swing.JSlider connectionsSlider;
    private javax.swing.JSlider exportsSlider;
    private javax.swing.JTextPane helpBox;
    private javax.swing.JSlider inventorySlider;
    private javax.swing.JSlider invoicesSlider;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JToolBar jToolBar1;
    private javax.swing.JToolBar jToolBar2;
    private javax.swing.JSlider managerSlider;
    private javax.swing.JRadioButton masterRadio;
    private javax.swing.JButton newButton;
    private javax.swing.JPasswordField passField1;
    private javax.swing.JPasswordField passField2;
    private javax.swing.JSlider quotesSlider;
    private javax.swing.JButton removeButton;
    private javax.swing.JSlider reportsSlider;
    private javax.swing.JButton resetButton;
    private javax.swing.JButton saveButton;
    private javax.swing.JTextField statBox;
    private javax.swing.JRadioButton userRadio;
    private javax.swing.JTable userTable;
    // End of variables declaration//GEN-END:variables
}
