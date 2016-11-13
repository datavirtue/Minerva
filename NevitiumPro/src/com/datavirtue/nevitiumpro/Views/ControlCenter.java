/*
 * ControlCenter.java
 *
 * Created on June 22, 2006, 9:47 AM
 ** Copyright (c) Data Virtue 2006
 */
package com.datavirtue.nevitiumpro.Views;

import com.datavirtue.nevitiumpro.Views.SettingsDialog;
import com.datavirtue.nevitiumpro.Views.MyConnectionsApp;
import com.datavirtue.nevitiumpro.Views.MyInventoryApp;
import com.datavirtue.nevitiumpro.Views.InvoiceDialog;
import com.datavirtue.nevitiumpro.Views.InvoiceManager;
import com.datavirtue.nevitiumpro.Legacy.RuntimeManagement.AppSettings;
import com.datavirtue.nevitiumpro.Legacy.datavirtue.DV;
import com.datavirtue.nevitiumpro.Legacy.datavirtue.DbEngine;
import com.datavirtue.nevitiumpro.ReturnMessageThread;
import com.datavirtue.nevitiumpro.entities.UserAccounts;
import com.datavirtue.nevitiumpro.hibernate.GUIUtil;
import com.datavirtue.nevitiumpro.hibernate.HibernateUtil;
import java.awt.FlowLayout;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.Window;
import java.io.IOException;
import java.util.logging.Logger;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Sean K Anderson - Data Virtue
 * @rights Copyright Data Virtue 2006, 2007 All Rights Reserved.
 */
public class ControlCenter extends javax.swing.JFrame {

    private boolean debug = false;
    private boolean log = false;
    private UserAccounts user;
    private Toolkit tools = Toolkit.getDefaultToolkit();
    private Image winIcon;
    private AppSettings application;
    private DbEngine dbsys;
    private HibernateUtil jpa;
    private String nl = System.getProperty("line.separator");
    private DefaultTableModel tm;
    private static final Logger logger = Logger.getLogger(ControlCenter.class.getName());
    private AccessDialog accessDialogReference;

    /**
     * Creates new form ControlCenter
     */
    public ControlCenter(final AppSettings g, AccessDialog ad) {

        //this.getLookAndFeel();
        this.accessDialogReference = ad;
        this.application = g;
        this.addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent e) {
                application.shutdown(false);
            }
        });

        winIcon = tools.getImage(getClass().getResource("/com/datavirtue/nevitiumpro/res/Orange.png"));

        initComponents();

        user = (UserAccounts) application.getUserAccount();

        mainToolbar.setLayout(new FlowLayout());
        statusToolbar.setLayout(new FlowLayout());

        //dbsys = new DbEngine("data/main.dsf", application, unicode);

        /* Check to see if workingPath.conn.db can read / write */
        this.application.setDb(this.dbsys);
        mainToolbar.setVisible(application.getAppUserSettings().getShowToolbar());

        GUIUtil.setWindowPosition(application, this);

        updateMessage();
        setBG();

    }

    private void setBG() {

        //String screenPic = props.getProp("SCREEN");
        byte[] imageData = application.getAppGlobalSettings().getCompanyLogo();
        if (imageData != null) {
            picLabel.setIcon(new javax.swing.ImageIcon(imageData));
        }

    }

    private void shutdown() {
        shutdown(false);
    }

    private void shutdown(boolean compactDatabase) {
        GUIUtil.recordWindowPosition(application, this);
        if (compactDatabase) {
            application.shutdown(compactDatabase);
        } else {
            application.shutdown(false);
        }
        //System.exit(9);
    }

    private void updateMessage() {

        boolean getRemoteMessage;
        getRemoteMessage = application.getAppUserSettings().getRetrieveRemoteMessage();
        if (!getRemoteMessage) {
            internetStatus.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/datavirtue/nevitiumpro/res/Aha-16/enabled/Disconnect.png")));
        } else {
            internetStatus.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/datavirtue/nevitiumpro/res/Aha-16/enabled/Connect.png")));
        }
        if (getRemoteMessage) {
            /* Thread Example */
            ReturnMessageThread rm = new ReturnMessageThread("http://www.datavirtue.com/nevitium/update/nevstat.txt",
                    remoteMessageBox, internetStatus);
            rm.start();


        } else {

            remoteMessageBox.setText(" Please visit datavirtue.com for updates & support.");

        }
    }

    /* Displays the role of the current user and changes the win title to reflect the company name. */
    private void showRole() {

        userButton.setText(user.getUsername());

        if (userButton.getText().equals("No Security")) {
            userButton.setIcon(new javax.swing.ImageIcon(
                    getClass().getResource("/com/datavirtue/nevitiumpro/res/Aha-48/Unlock.png")));
        }

        String role = application.getLangText().getString("master");

        /* Show the role and change the color based on the role, red for master users and green for regular users */
        //roleButton.setForeground(new java.awt.Color(153,0,0));
        //change icon
        roleButton.setIcon(new javax.swing.ImageIcon(
                getClass().getResource("/com/datavirtue/nevitiumpro/res/Aha-48/Boss.png")));

        if (!user.getMaster()) {

            roleButton.setIcon(new javax.swing.ImageIcon(
                    getClass().getResource("/com/datavirtue/nevitiumpro/res/Aha-48/User.png")));
            role = "User";
        }
        roleButton.setText(role);

        this.setTitle("Nevitium  " + '(' + application.getAppGlobalSettings().getCompanyName() + ')');

    }

    /**
     * This method is called from within the constructor to initialize the form. WARNING: Do NOT modify this code. The
     * content of this method is always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        picLabel = new javax.swing.JLabel();
        remoteMessageBox = new javax.swing.JTextField();
        mainToolbar = new javax.swing.JToolBar();
        connectionsButton = new javax.swing.JButton();
        inventoryButton = new javax.swing.JButton();
        activityButton = new javax.swing.JButton();
        invoiceButton = new javax.swing.JButton();
        settingsButton = new javax.swing.JButton();
        exitButton = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        statusToolbar = new javax.swing.JToolBar();
        userButton = new javax.swing.JButton();
        roleButton = new javax.swing.JButton();
        internetStatus = new javax.swing.JLabel();
        jMenuBar2 = new javax.swing.JMenuBar();
        fileMenu = new javax.swing.JMenu();
        logoutMenuItem = new javax.swing.JMenuItem();
        dataTransferMenu = new javax.swing.JMenuItem();
        settingsMenuItem = new javax.swing.JMenuItem();
        jSeparator11 = new javax.swing.JPopupMenu.Separator();
        exitMenuItem = new javax.swing.JMenuItem();
        exitCompactMenuItem = new javax.swing.JMenuItem();
        toolsMenu = new javax.swing.JMenu();
        peopleMenuItem = new javax.swing.JMenuItem();
        inventoryMenuItem = new javax.swing.JMenuItem();
        invoiceManagerMenuItem = new javax.swing.JMenuItem();
        jSeparator3 = new javax.swing.JPopupMenu.Separator();
        invoiceMenuItem = new javax.swing.JMenuItem();
        checkMenuItem = new javax.swing.JMenuItem();
        jSeparator1 = new javax.swing.JPopupMenu.Separator();
        expensesMenuItem = new javax.swing.JMenuItem();
        bankAccountsMenuItem = new javax.swing.JMenuItem();
        jSeparator2 = new javax.swing.JPopupMenu.Separator();
        paymentMenuItem = new javax.swing.JMenuItem();
        prepaidMenuItem = new javax.swing.JMenuItem();
        reportsMenu = new javax.swing.JMenu();
        helpMenu = new javax.swing.JMenu();
        supportMenuItem = new javax.swing.JMenuItem();
        videosMenuItem = new javax.swing.JMenuItem();
        infoMenuItem = new javax.swing.JMenuItem();
        creditsMenuItem = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setTitle("Nevitium Invoice Manager");
        setIconImage(winIcon);

        picLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jScrollPane1.setViewportView(picLabel);

        remoteMessageBox.setEditable(false);

        mainToolbar.setFloatable(false);
        mainToolbar.setRollover(true);
        mainToolbar.setBorderPainted(false);

        connectionsButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/datavirtue/nevitiumpro/res/Aha-48/Conference.png"))); // NOI18N
        connectionsButton.setText("Connections");
        connectionsButton.setFocusable(false);
        connectionsButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        connectionsButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        connectionsButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                connectionsButtonActionPerformed(evt);
            }
        });
        mainToolbar.add(connectionsButton);

        inventoryButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/datavirtue/nevitiumpro/res/Aha-48/Tables.png"))); // NOI18N
        inventoryButton.setText("Inventory");
        inventoryButton.setFocusable(false);
        inventoryButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        inventoryButton.setMinimumSize(new java.awt.Dimension(91, 81));
        inventoryButton.setPreferredSize(new java.awt.Dimension(91, 81));
        inventoryButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        inventoryButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                inventoryButtonActionPerformed(evt);
            }
        });
        mainToolbar.add(inventoryButton);

        activityButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/datavirtue/nevitiumpro/res/Aha-48/Money.png"))); // NOI18N
        activityButton.setText("Invoices");
        activityButton.setFocusable(false);
        activityButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        activityButton.setPreferredSize(new java.awt.Dimension(91, 81));
        activityButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        activityButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                activityButtonActionPerformed(evt);
            }
        });
        mainToolbar.add(activityButton);

        invoiceButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/datavirtue/nevitiumpro/res/Aha-48/Barcode scanner1.png"))); // NOI18N
        invoiceButton.setText("SALE");
        invoiceButton.setFocusable(false);
        invoiceButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        invoiceButton.setMinimumSize(new java.awt.Dimension(91, 81));
        invoiceButton.setPreferredSize(new java.awt.Dimension(91, 81));
        invoiceButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        invoiceButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                invoiceButtonActionPerformed(evt);
            }
        });
        mainToolbar.add(invoiceButton);

        settingsButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/datavirtue/nevitiumpro/res/Aha-48/Configuration.png"))); // NOI18N
        settingsButton.setText("Setup");
        settingsButton.setFocusable(false);
        settingsButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        settingsButton.setPreferredSize(new java.awt.Dimension(91, 81));
        settingsButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        settingsButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                settingsButtonActionPerformed(evt);
            }
        });
        mainToolbar.add(settingsButton);

        exitButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/datavirtue/nevitiumpro/res/Aha-48/Close.png"))); // NOI18N
        exitButton.setText("Exit");
        exitButton.setFocusable(false);
        exitButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        exitButton.setPreferredSize(new java.awt.Dimension(91, 81));
        exitButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        exitButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exitButtonActionPerformed(evt);
            }
        });
        mainToolbar.add(exitButton);

        jPanel2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        statusToolbar.setFloatable(false);
        statusToolbar.setRollover(true);

        userButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/datavirtue/nevitiumpro/res/Aha-48/Lock.png"))); // NOI18N
        userButton.setText("Security");
        userButton.setToolTipText("Security Status - Change User");
        userButton.setFocusable(false);
        userButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        userButton.setPreferredSize(new java.awt.Dimension(98, 81));
        userButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        userButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                userButtonActionPerformed(evt);
            }
        });
        statusToolbar.add(userButton);

        roleButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/datavirtue/nevitiumpro/res/Aha-48/Boss.png"))); // NOI18N
        roleButton.setText("Master");
        roleButton.setToolTipText("Current Security Role - Manage Security");
        roleButton.setFocusable(false);
        roleButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        roleButton.setPreferredSize(new java.awt.Dimension(98, 81));
        roleButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        roleButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                roleButtonActionPerformed(evt);
            }
        });
        statusToolbar.add(roleButton);

        org.jdesktop.layout.GroupLayout jPanel2Layout = new org.jdesktop.layout.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .add(statusToolbar, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .add(statusToolbar, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 87, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        internetStatus.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        internetStatus.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/datavirtue/nevitiumpro/res/Aha-16/enabled/Connect.png"))); // NOI18N
        internetStatus.setToolTipText("Nevitium Internet Status");

        fileMenu.setText("File");

        logoutMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F2, 0));
        logoutMenuItem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/datavirtue/nevitiumpro/res/Aha-16/enabled/Access key.png"))); // NOI18N
        logoutMenuItem.setText("Log Out / Log In");
        logoutMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                logoutMenuItemActionPerformed(evt);
            }
        });
        fileMenu.add(logoutMenuItem);

        dataTransferMenu.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/datavirtue/nevitiumpro/res/Aha-16/enabled/Export table.png"))); // NOI18N
        dataTransferMenu.setText("Data Transfer (Import/Export)");
        fileMenu.add(dataTransferMenu);

        settingsMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F3, 0));
        settingsMenuItem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/datavirtue/nevitiumpro/res/Aha-16/enabled/Configuration.png"))); // NOI18N
        settingsMenuItem.setText("Settings");
        fileMenu.add(settingsMenuItem);
        fileMenu.add(jSeparator11);

        exitMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_ESCAPE, 0));
        exitMenuItem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/datavirtue/nevitiumpro/res/Aha-16/enabled/Close.png"))); // NOI18N
        exitMenuItem.setText("Exit");
        exitMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exitMenuItemActionPerformed(evt);
            }
        });
        fileMenu.add(exitMenuItem);

        exitCompactMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_ESCAPE, java.awt.event.InputEvent.ALT_MASK | java.awt.event.InputEvent.SHIFT_MASK | java.awt.event.InputEvent.CTRL_MASK));
        exitCompactMenuItem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/datavirtue/nevitiumpro/res/Aha-16/enabled/Data.png"))); // NOI18N
        exitCompactMenuItem.setText("Exit and clean database");
        exitCompactMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exitCompactMenuItemActionPerformed(evt);
            }
        });
        fileMenu.add(exitCompactMenuItem);

        jMenuBar2.add(fileMenu);

        toolsMenu.setText("Tools");

        peopleMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F5, 0));
        peopleMenuItem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/datavirtue/nevitiumpro/res/Aha-16/enabled/Conference.png"))); // NOI18N
        peopleMenuItem.setText("People");
        peopleMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                peopleMenuItemActionPerformed(evt);
            }
        });
        toolsMenu.add(peopleMenuItem);

        inventoryMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F6, 0));
        inventoryMenuItem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/datavirtue/nevitiumpro/res/Aha-16/enabled/Tables.png"))); // NOI18N
        inventoryMenuItem.setText("Inventory");
        inventoryMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                inventoryMenuItemActionPerformed(evt);
            }
        });
        toolsMenu.add(inventoryMenuItem);

        invoiceManagerMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F7, 0));
        invoiceManagerMenuItem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/datavirtue/nevitiumpro/res/Aha-16/enabled/Money.png"))); // NOI18N
        invoiceManagerMenuItem.setText("Invoice Activity");
        toolsMenu.add(invoiceManagerMenuItem);
        toolsMenu.add(jSeparator3);

        invoiceMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F8, 0));
        invoiceMenuItem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/datavirtue/nevitiumpro/res/Aha-16/enabled/Barcode scanner1.png"))); // NOI18N
        invoiceMenuItem.setText("Quick Invoice/Quote");
        toolsMenu.add(invoiceMenuItem);

        checkMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F9, 0));
        checkMenuItem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/datavirtue/nevitiumpro/res/Aha-16/enabled/Notebook.png"))); // NOI18N
        checkMenuItem.setText("Write Check");
        toolsMenu.add(checkMenuItem);
        toolsMenu.add(jSeparator1);

        expensesMenuItem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/datavirtue/nevitiumpro/res/Aha-16/enabled/Equipment.png"))); // NOI18N
        expensesMenuItem.setText("Equipment & Expenses");
        expensesMenuItem.setDisabledIcon(new javax.swing.ImageIcon(getClass().getResource("/com/datavirtue/nevitiumpro/res/Aha-16/enabled/Equipment.png"))); // NOI18N
        expensesMenuItem.setEnabled(false);
        toolsMenu.add(expensesMenuItem);

        bankAccountsMenuItem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/datavirtue/nevitiumpro/res/Aha-16/enabled/Money bag.png"))); // NOI18N
        bankAccountsMenuItem.setText("Banking (Cash, Credit, Loans)");
        bankAccountsMenuItem.setDisabledIcon(new javax.swing.ImageIcon(getClass().getResource("/com/datavirtue/nevitiumpro/res/Aha-16/enabled/Money bag.png"))); // NOI18N
        bankAccountsMenuItem.setEnabled(false);
        toolsMenu.add(bankAccountsMenuItem);
        toolsMenu.add(jSeparator2);

        paymentMenuItem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/datavirtue/nevitiumpro/res/Aha-16/enabled/Card terminal.png"))); // NOI18N
        paymentMenuItem.setText("Credit Card & Check Processing");
        toolsMenu.add(paymentMenuItem);

        prepaidMenuItem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/datavirtue/nevitiumpro/res/Aha-16/enabled/Credit cards.png"))); // NOI18N
        prepaidMenuItem.setText("Gift Card & Prepaid Accounts");
        toolsMenu.add(prepaidMenuItem);

        jMenuBar2.add(toolsMenu);

        reportsMenu.setText("Reports");
        jMenuBar2.add(reportsMenu);

        helpMenu.setText("Help");

        supportMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F1, java.awt.event.InputEvent.CTRL_MASK));
        supportMenuItem.setText("Support (datavirtue.com)");
        helpMenu.add(supportMenuItem);

        videosMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F1, 0));
        videosMenuItem.setText("Videos (YouTube.com)");
        helpMenu.add(videosMenuItem);

        infoMenuItem.setText("Info");
        helpMenu.add(infoMenuItem);

        creditsMenuItem.setText("Credits");
        helpMenu.add(creditsMenuItem);

        jMenuBar2.add(helpMenu);

        setJMenuBar(jMenuBar2);

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, jScrollPane1)
                    .add(mainToolbar, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 608, Short.MAX_VALUE)
                    .add(jPanel2, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
                        .add(remoteMessageBox)
                        .add(1, 1, 1)
                        .add(internetStatus, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 27, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
                .add(mainToolbar, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jScrollPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 48, Short.MAX_VALUE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(remoteMessageBox, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 29, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(internetStatus, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 29, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void custInvoiceHistory() {

        MyConnectionsApp cd = new MyConnectionsApp(this, true, application, true, true, false);
        cd.setVisible(true);

        //int k = cd.getReturnValue();  //real value
        int k = 0;

        if (k == -1) {
            return;
        }

        cd.dispose(); //dont call dispose before finsihing with method
        cd = null;

        if (k > 0) {
            //ReportFactory.generateCustomerStatement(application, k);
        }
    }

    private void upgradeExport() {
    }

    private void settingsButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_settingsButtonActionPerformed
        if (application.checkPermissions(user, user.getSettings(), 500)) {
            new SettingsDialog(this, true, application, 0).setVisible(true);
            updateMessage();
            setBG();
            mainToolbar.setVisible(application.getAppUserSettings().getShowToolbar());
        }
    }//GEN-LAST:event_settingsButtonActionPerformed

    private void connectionsButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_connectionsButtonActionPerformed
        //Tools.playSound(getClass().getResource("/com/datavirtue/nevitiumpro/res/slip.wav"));
        MyConnectionsApp cd = new MyConnectionsApp(this, true, application, false, true, true);
        //cd.setVisible(true);


    }//GEN-LAST:event_connectionsButtonActionPerformed

    private void invoiceButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_invoiceButtonActionPerformed
        if (!application.checkPermissions(user, user.getInvoices(), 300)) {
            return;
        }
        InvoiceDialog id = new InvoiceDialog(this, true, 0, application); //no select            id.setVisible(true);
        id.setVisible(true);
        id.dispose();
        id = null;

    }//GEN-LAST:event_invoiceButtonActionPerformed

    private void activityButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_activityButtonActionPerformed

        if (!application.checkPermissions(user, user.getInvoiceManager(), 300)) {
            return;
        }
        //InvoiceModel temp = (InvoiceModel) DV.DeSerial("data/hold/I.10010.inv");
        //  InvoiceModel temp = null;             
        //invDialog id = new invDialog (this, true, dbsys, cso, temp); //no select
        InvoiceManager i = new InvoiceManager(this, true, application);
        i.dispose();

    }//GEN-LAST:event_activityButtonActionPerformed

    private void inventoryButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_inventoryButtonActionPerformed

        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                MyInventoryApp id = new MyInventoryApp(null, true, application, false);

            }
        });


    }//GEN-LAST:event_inventoryButtonActionPerformed

    private void exitButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exitButtonActionPerformed
        shutdown();
    }//GEN-LAST:event_exitButtonActionPerformed

    private void userButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_userButtonActionPerformed
    }//GEN-LAST:event_userButtonActionPerformed

    private void roleButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_roleButtonActionPerformed

        if (application.checkPermissions(user, user.getSettings(), 500)) {
            new SettingsDialog(this, true, application, 3).setVisible(true);
            updateMessage();
            setBG();
            mainToolbar.setVisible(application.getAppUserSettings().getShowToolbar());
        }

    }//GEN-LAST:event_roleButtonActionPerformed

    private void logoutMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_logoutMenuItemActionPerformed
        application.getJPA().shutdown();
        this.setVisible(false);
        this.accessDialogReference.setVisible(true);

    }//GEN-LAST:event_logoutMenuItemActionPerformed

    private void peopleMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_peopleMenuItemActionPerformed
        MyConnectionsApp cd = new MyConnectionsApp(this, true, application, false, true, true);
    }//GEN-LAST:event_peopleMenuItemActionPerformed

    private void inventoryMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_inventoryMenuItemActionPerformed
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                MyInventoryApp id = new MyInventoryApp(null, true, application, false);

            }
        });
    }//GEN-LAST:event_inventoryMenuItemActionPerformed

    private void exitMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exitMenuItemActionPerformed
        shutdown();
    }//GEN-LAST:event_exitMenuItemActionPerformed

    private void exitCompactMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exitCompactMenuItemActionPerformed
        shutdown(true);
    }//GEN-LAST:event_exitCompactMenuItemActionPerformed

    private void goSettings() {

        if (application.checkPermissions(user, user.getSettings(), 600)) {
            new SettingsDialog(this, true, application, 8).setVisible(true);
            updateMessage();
            setBG();
            mainToolbar.setVisible(application.getAppUserSettings().getShowToolbar());
        }

    }
    private String file_sep = System.getProperty("file.separator");

    private void launchPaymentSystem() {

        boolean usePaymentSystem = false;

        String paymentURL = application.getAppGlobalSettings().getPaymentSysUrl();

        if (paymentURL.length() > 0) {
            usePaymentSystem = true;
        } else {
            usePaymentSystem = false;
        }


        if (usePaymentSystem == false) {

            javax.swing.JOptionPane.showMessageDialog(null,
                    "It appears that you have not configured an external payment system." + nl
                    + "Go to File-->Settings-->Output to configure a payment system.");

            return;
        }

        if (usePaymentSystem) {

            boolean webPayment = application.getAppGlobalSettings().getWebBasedPaymentSys();
            if (webPayment) {
                String url = paymentURL;
                if (!url.contains("http://") && !url.contains("HTTP://")
                        && !url.contains("https://") && !url.contains("HTTPS://")) {

                    javax.swing.JOptionPane.showMessageDialog(null,
                            "You must spcifiy a protocol in the web address" + nl
                            + "Example: http://www.paypal.com instead of just www.paypal.com");
                    return;
                }
                int a = DV.launchURL(paymentURL);
                if (a < 1) {
                    javax.swing.JOptionPane.showMessageDialog(null,
                            "There was a problem trying to launch your web browser."
                            + nl + "This may not be supported by your Operating System.");
                }

            } else {

                String nl = System.getProperty("line.separator");

                String osName = System.getProperty("os.name");

                try {

                    if (osName.contains("Windows")) {
                        Runtime.getRuntime().exec('"' + paymentURL + '"');
                    } //FOR WINDOWS NT/XP/2000 USE CMD.EXE
                    else {

                        //System.out.println(acro + " " + file);
                        Runtime.getRuntime().exec(paymentURL);

                    }
                } catch (IOException ex) {

                    javax.swing.JOptionPane.showMessageDialog(null,
                            "error: There was a problem launching the payment system!" + nl
                            + "<<" + paymentURL + ">>");
                    //ex.printStackTrace();
                }

            }

        }


    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton activityButton;
    private javax.swing.JMenuItem bankAccountsMenuItem;
    private javax.swing.JMenuItem checkMenuItem;
    private javax.swing.JButton connectionsButton;
    private javax.swing.JMenuItem creditsMenuItem;
    private javax.swing.JMenuItem dataTransferMenu;
    private javax.swing.JButton exitButton;
    private javax.swing.JMenuItem exitCompactMenuItem;
    private javax.swing.JMenuItem exitMenuItem;
    private javax.swing.JMenuItem expensesMenuItem;
    private javax.swing.JMenu fileMenu;
    private javax.swing.JMenu helpMenu;
    private javax.swing.JMenuItem infoMenuItem;
    private javax.swing.JLabel internetStatus;
    private javax.swing.JButton inventoryButton;
    private javax.swing.JMenuItem inventoryMenuItem;
    private javax.swing.JButton invoiceButton;
    private javax.swing.JMenuItem invoiceManagerMenuItem;
    private javax.swing.JMenuItem invoiceMenuItem;
    private javax.swing.JMenuBar jMenuBar2;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JPopupMenu.Separator jSeparator1;
    private javax.swing.JPopupMenu.Separator jSeparator11;
    private javax.swing.JPopupMenu.Separator jSeparator2;
    private javax.swing.JPopupMenu.Separator jSeparator3;
    private javax.swing.JMenuItem logoutMenuItem;
    private javax.swing.JToolBar mainToolbar;
    private javax.swing.JMenuItem paymentMenuItem;
    private javax.swing.JMenuItem peopleMenuItem;
    private javax.swing.JLabel picLabel;
    private javax.swing.JMenuItem prepaidMenuItem;
    private javax.swing.JTextField remoteMessageBox;
    private javax.swing.JMenu reportsMenu;
    private javax.swing.JButton roleButton;
    private javax.swing.JButton settingsButton;
    private javax.swing.JMenuItem settingsMenuItem;
    private javax.swing.JToolBar statusToolbar;
    private javax.swing.JMenuItem supportMenuItem;
    private javax.swing.JMenu toolsMenu;
    private javax.swing.JButton userButton;
    private javax.swing.JMenuItem videosMenuItem;
    // End of variables declaration//GEN-END:variables
}
