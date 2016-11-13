/*
 * AccessDialog.java
 *
 * Created on July 27, 2007, 10:40 PM
 */
package com.datavirtue.nevitiumpro.Views;

import com.datavirtue.nevitiumpro.Views.ExceptionErrorDialog;
import com.datavirtue.nevitiumpro.Legacy.RuntimeManagement.AppSettings;
import com.datavirtue.nevitiumpro.Legacy.datavirtue.DV;
import com.datavirtue.nevitiumpro.Legacy.datavirtue.PBE;
import com.datavirtue.nevitiumpro.entities.AppGlobalSettings;
import com.datavirtue.nevitiumpro.entities.AppUserSettings;
import com.datavirtue.nevitiumpro.entities.UserAccounts;
import com.datavirtue.nevitiumpro.hibernate.BeanTableModel;
import com.datavirtue.nevitiumpro.hibernate.HibernateUtil;
import com.datavirtue.nevitiumpro.Model.AppSettingsDAO;
import com.datavirtue.nevitiumpro.Model.UserAccountDAO;
import java.awt.Color;
import java.util.Locale;
import java.util.ResourceBundle;
import org.hibernate.HibernateException;

/**
 *
 * @author Data Virtue
 */
public class AccessDialog extends javax.swing.JDialog {

    /**
     * Creates new form AccessDialog
     */
    private HibernateUtil hibernate;
    private AppSettings application;
    private ControlCenter controlCenter;
    ResourceBundle langText;
    

    public AccessDialog(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        java.awt.Dimension dim = DV.computeCenter((java.awt.Window) this);
        this.setLocation(dim.width, dim.height);
                
        //set language in the registry and last server and login

        //Locale locale = new Locale("en","US");  //get lang from default locale
        langText = ResourceBundle.getBundle("com.datavirtue.nevitiumpro.i18n.Text", Locale.getDefault());

        // Apply captions and labels
        this.tryButton.setText(langText.getString("try"));
        this.cancelButton.setText(langText.getString("cancel"));
        this.serverLabel.setText(langText.getString("server"));
        this.companyFileLabel.setText(langText.getString("companyFile"));
        this.captionLabel.setText(langText.getString("AccessDialog.caption"));
        this.usernameLabel.setText(langText.getString("username"));
        this.passwordLabel.setText(langText.getString("password"));

        passwordField.requestFocus();
    }

    public void setControlCenterReference(ControlCenter c){
        controlCenter = c;
    }
    
    private void checkUser(String dataPath, String user) throws HibernateException {
        //Setup Data Objects
        hibernate = new HibernateUtil(dataPath);
        UserAccountDAO dao = new UserAccountDAO(hibernate);
        
        BeanTableModel tm=null;
        try {
             tm = dao.getTableModelAll(0); //pull all users
        } catch (Exception ex) {
            ExceptionErrorDialog errDialog = new ExceptionErrorDialog(ex);
            errDialog.pack();
            errDialog.setVisible(true);
        }
        
        
        
        AppSettingsDAO settings = new AppSettingsDAO(hibernate);
        UserAccounts theUser;
        application = new AppSettings();
        application.setResourceBundle(langText);
        application.setJPA(hibernate);
        
        //Test for no users in database and setup "master" with default settings
        if (tm == null || tm.getRowCount() < 1) {

            UserAccounts master = new UserAccounts();
            master.setUsername(langText.getString("master"));
            master.setPassword("");
            master.setMaster(true);

            master.setInventory(200);
            master.setConnections(200);
            master.setInvoices(200);
            master.setInvoiceManager(200);
            master.setQuotes(200);
            master.setReports(200);
            master.setExports(200);
            master.setChecks(200);
            master.setSettings(200);

            dao.transactionSave(master, 10);
            AppGlobalSettings globalSettings = settings.buildDefaultGlobalSettings(dao);
            AppUserSettings userSettings = settings.buildDefaultAppUserSettings(1, dao);
            dao.transactionSave(globalSettings, 10);
            dao.transactionSave(userSettings, 10);
            //Build the return value
            application.setAppUserSettings(userSettings);
            application.setAppGlobalSettings(globalSettings);
            application.setUserAccount(master);
            //No need to authenticate, just finish
            //this.setVisible(false);
            return;
        }
        //Now check for the username that was entered at the Access Panel
        tm = dao.getUserBean(user);  //pull the user bean 

        //Check for "user not found"
        if (tm == null) {
            usernameLabel.setForeground(Color.red);
            userField.requestFocus();
            return;
        }

        //Check password entered at Access Panel 
        theUser = (UserAccounts) tm.getBeanAt(0);
        String decrypted = null;

        // Hanlde user with blank password
        if (theUser.getPassword().equals("")) {
            String pw = new String(passwordField.getPassword());
            if (pw.equals("")) {
                prepareUser(theUser, settings, dao);
                return;
            } else {
                passwordLabel.setForeground(Color.red);
                passwordField.requestFocus();
                return;
            }
        }

        try {
            decrypted = PBE.decrypt(passwordField.getPassword(), theUser.getPassword());
        } catch (Exception ex) {
            ExceptionErrorDialog errDialog = new ExceptionErrorDialog(ex);
            errDialog.pack();
            errDialog.setVisible(true);
        }

        if (decrypted == null) {  //bad password
            passwordLabel.setForeground(Color.red);
            passwordField.requestFocus();
            return;
        }

        if (decrypted.equals(new String(passwordField.getPassword()))) {
            prepareUser(theUser, settings, dao);
        } else {
            passwordLabel.setForeground(Color.red);
            passwordField.requestFocus();
        }
    }

    private void prepareUser(UserAccounts theUser, AppSettingsDAO settings, UserAccountDAO dao) {
        AppUserSettings userSettings = settings.getUserSettings(theUser.getId());
        if (userSettings == null) {
            userSettings = settings.buildDefaultAppUserSettings(theUser.getId(), dao);
        }
        AppGlobalSettings globalSettings = settings.getGlobalSettings();
        if (globalSettings == null) {
            globalSettings = settings.buildDefaultGlobalSettings(dao);
        }
        application.setAppUserSettings(userSettings);
        application.setAppGlobalSettings(globalSettings);
        application.setUserAccount(theUser);
        //this.setVisible(false);
    }

    private void processUserLogin(){
        checkUser(dataPathField.getText(), userField.getText().trim());
        if (application != null) {
            if (controlCenter != null){
                controlCenter.dispose();
                controlCenter = null;
            }
            this.setVisible(false);
            controlCenter = new ControlCenter(application, this);
            controlCenter.setVisible(true);
            //this.setVisible(true);
            // If ControlCenter is made invisible then we fall through this method and the user can log into another database/user
        } else {
            System.exit(1);
        }
    }
    
    /**
     * This method is called from within the constructor to initialize the form. WARNING: Do NOT modify this code. The
     * content of this method is always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        usernameLabel = new javax.swing.JLabel();
        passwordLabel = new javax.swing.JLabel();
        userField = new javax.swing.JTextField();
        passwordField = new javax.swing.JPasswordField();
        tryButton = new javax.swing.JButton();
        cancelButton = new javax.swing.JButton();
        captionLabel = new javax.swing.JLabel();
        dataPathField = new javax.swing.JTextField();
        companyFileLabel = new javax.swing.JLabel();
        jTextField2 = new javax.swing.JTextField();
        serverLabel = new javax.swing.JLabel();
        pathField = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Nevitium");
        setResizable(false);

        jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/datavirtue/nevitiumpro/res/onebit_25.gif"))); // NOI18N

        org.jdesktop.layout.GroupLayout jPanel1Layout = new org.jdesktop.layout.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .add(jLabel4, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .add(jLabel4, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel2.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        usernameLabel.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        usernameLabel.setText("Username");

        passwordLabel.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        passwordLabel.setText("Password");

        userField.setText("master");
        userField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                userFieldFocusGained(evt);
            }
        });

        passwordField.setNextFocusableComponent(tryButton);
        passwordField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                passwordFieldKeyPressed(evt);
            }
        });

        tryButton.setText("Try");
        tryButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tryButtonActionPerformed(evt);
            }
        });

        cancelButton.setText("Cancel");
        cancelButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelButtonActionPerformed(evt);
            }
        });

        captionLabel.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        captionLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        captionLabel.setText("Nevitium Access Panel");

        dataPathField.setText("c:/data/nevitium");
        dataPathField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                dataPathFieldActionPerformed(evt);
            }
        });

        companyFileLabel.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        companyFileLabel.setText("Company File");

        jTextField2.setEditable(false);
        jTextField2.setText("localhost");

        serverLabel.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        serverLabel.setText("Server");

        org.jdesktop.layout.GroupLayout jPanel2Layout = new org.jdesktop.layout.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(captionLabel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 278, Short.MAX_VALUE)
                    .add(jPanel2Layout.createSequentialGroup()
                        .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
                            .add(usernameLabel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .add(passwordLabel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .add(companyFileLabel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 66, Short.MAX_VALUE)
                            .add(serverLabel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(org.jdesktop.layout.GroupLayout.TRAILING, jPanel2Layout.createSequentialGroup()
                                .add(tryButton)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 94, Short.MAX_VALUE)
                                .add(cancelButton))
                            .add(org.jdesktop.layout.GroupLayout.TRAILING, passwordField, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 208, Short.MAX_VALUE)
                            .add(userField, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 208, Short.MAX_VALUE)
                            .add(dataPathField)
                            .add(jTextField2))))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .add(captionLabel)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jTextField2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(serverLabel))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(dataPathField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(companyFileLabel))
                .add(18, 18, 18)
                .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(usernameLabel)
                    .add(userField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(passwordLabel)
                    .add(passwordField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(cancelButton)
                    .add(tryButton))
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pathField.setEditable(false);

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(pathField, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 380, Short.MAX_VALUE)
                    .add(layout.createSequentialGroup()
                        .add(jPanel1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jPanel2, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(pathField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
                    .add(jPanel2, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(jPanel1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void userFieldFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_userFieldFocusGained

        userField.selectAll();


    }//GEN-LAST:event_userFieldFocusGained

    private void passwordFieldKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_passwordFieldKeyPressed

        if (evt.getKeyCode() == java.awt.event.KeyEvent.VK_ENTER) {
            processUserLogin();
        }

    }//GEN-LAST:event_passwordFieldKeyPressed

    private void cancelButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelButtonActionPerformed
        System.exit(2);
    }//GEN-LAST:event_cancelButtonActionPerformed

    private void tryButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tryButtonActionPerformed
        processUserLogin();
    }//GEN-LAST:event_tryButtonActionPerformed

    private void dataPathFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_dataPathFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_dataPathFieldActionPerformed
    private String nl = System.getProperty("line.separator");
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton cancelButton;
    private javax.swing.JLabel captionLabel;
    private javax.swing.JLabel companyFileLabel;
    private javax.swing.JTextField dataPathField;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JPasswordField passwordField;
    private javax.swing.JLabel passwordLabel;
    private javax.swing.JTextField pathField;
    private javax.swing.JLabel serverLabel;
    private javax.swing.JButton tryButton;
    private javax.swing.JTextField userField;
    private javax.swing.JLabel usernameLabel;
    // End of variables declaration//GEN-END:variables
}
