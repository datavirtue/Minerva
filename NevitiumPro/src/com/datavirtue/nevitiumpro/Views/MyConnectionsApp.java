/*
 * ConnectionsDialog.java
 *
 * Created on June 22, 2006, 10:08 AM
 ** Copyright (c) Data Virtue 2006, 2013
 */
package com.datavirtue.nevitiumpro.Views;

import com.datavirtue.nevitiumpro.Util.LimitedDocument;
import com.datavirtue.nevitiumpro.Util.TableView;
import com.datavirtue.nevitiumpro.Validation.TextFieldRegExVerifier;
import com.datavirtue.nevitiumpro.Validation.Verifier;
import com.datavirtue.nevitiumpro.Legacy.RuntimeManagement.AppSettings;
import com.datavirtue.nevitiumpro.Legacy.datavirtue.DV;
import com.datavirtue.nevitiumpro.Legacy.datavirtue.DbEngine;
import com.datavirtue.nevitiumpro.Legacy.datavirtue.Settings;
import com.datavirtue.nevitiumpro.Reports.PurchaseHistoryReport;
import com.datavirtue.nevitiumpro.Reports.ReportModel;
import com.datavirtue.nevitiumpro.Reports.ReportTableDialog;
import com.datavirtue.nevitiumpro.Plugins.ClassFactory;
import com.datavirtue.nevitiumpro.entities.Connections;
import com.datavirtue.nevitiumpro.entities.ConnectionsAddress;
import com.datavirtue.nevitiumpro.entities.ConnectionsPhones;
import com.datavirtue.nevitiumpro.entities.UserAccounts;
import com.datavirtue.nevitiumpro.hibernate.BeanTableModel;
import com.datavirtue.nevitiumpro.hibernate.GUIUtil;
import com.datavirtue.nevitiumpro.hibernate.HibernateUtil;
import com.datavirtue.nevitiumpro.hibernate.TableUtil;
import com.datavirtue.nevitiumpro.Model.MyConnectionsDAO;
import de.schlichtherle.io.ArchiveException;
import de.schlichtherle.io.File;
import java.awt.Desktop;
import java.awt.FlowLayout;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URI;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.table.*;

/**
 *
 * @author Sean K Anderson - Data Virtue
 * @rights Copyright Data Virtue 2006, 2007, 2008, 2009, 2010 All Rights Reserved.
 */
public class MyConnectionsApp extends javax.swing.JDialog {

    private boolean debug = false;
    private final AppSettings application;
    private MyConnectionsDAO dao;        
    private Connections connectionBean = null;
    private ConnectionsAddress addressBean = null;
    private ConnectionsPhones phoneBean = null;
    private BeanTableModel btm;
    private UserAccounts user;
   
    /**
     * Creates new form ConnectionsDialog
     */
    public MyConnectionsApp(java.awt.Frame parent, boolean modal, AppSettings g, boolean select, boolean customers, boolean suppliers) {
        super(parent, modal);
        
        /*
         * Window dressing and dialog-level key mappings 
         */
        Toolkit tools = Toolkit.getDefaultToolkit();
        winIcon = tools.getImage(getClass().getResource("/com/datavirtue/nevitiumpro/res/Orange.png"));
        initComponents();
        this.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent e) {
                GUIUtil.recordWindowPosition(application, (Window) e.getSource());
            }
        });

        /* Close dialog on escape */
        ActionMap am = getRootPane().getActionMap();
        InputMap im = getRootPane().getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
        Object windowCloseKey = new Object();
        KeyStroke windowCloseStroke = KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0);
        Action windowCloseAction = new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
                dispose();
            }
        };
        im.put(windowCloseStroke, windowCloseKey);
        am.put(windowCloseKey, windowCloseAction);
        /* End Close Dialog on Escape*/


        /*
         * Configure view with application/user configuration 
         */
        this.application = g;
        this.user = (UserAccounts)application.getUserAccount();
        
        dao = new MyConnectionsDAO((HibernateUtil) application.getJPA());

        selectMode = select;
        String tax1name = application.getAppGlobalSettings().getTax1Name();
        String tax2name = application.getAppGlobalSettings().getTax2Name();
        tax1CheckBox.setText(tax1name);
        tax2CheckBox.setText(tax2name);

        // Misc UI init
        jTabbedPane1.setSelectedIndex(0); //select a tab to view by default
        connTable.setSelectionForeground(new java.awt.Color(0, 0, 0));
        functionToolbar.setLayout(new FlowLayout());
        invoiceToolbar.setLayout(new FlowLayout());
        searchFieldCombo.setSelectedIndex(0);

        /*
         * Install a document model to limit field length and install input verifiers
         */
        //TODO: install input verifiers
        companyNameField.setDocument(new LimitedDocument(35));
        companyNameField.setInputVerifier(new TextFieldRegExVerifier(Verifier.ANYTHING, "Required", Verifier.REQUIRED, Verifier.YIELD_FOCUS));
        
        firstNameField.setDocument(new LimitedDocument(20));
        firstNameField.setInputVerifier(new TextFieldRegExVerifier(Verifier.ALPHA, "Required", Verifier.REQUIRED, Verifier.YIELD_FOCUS));
        
        sirNameField.setDocument(new LimitedDocument(20));
        addressField.setDocument(new LimitedDocument(40));
        address2Field.setDocument(new LimitedDocument(40));
        cityField.setDocument(new LimitedDocument(30));
        provenceField.setDocument(new LimitedDocument(20));
        pinCodeField.setDocument(new LimitedDocument(10));
        emailField.setDocument(new LimitedDocument(40));
        websiteField.setDocument(new LimitedDocument(50));
        notesTextArea.setDocument(new LimitedDocument(100));

              
        payrollButton.setVisible(false);

        /*
         * Get view ready based on constructor options
         */
        
        if (customers && !suppliers) {
            custRadio.setSelected(true);
        }
        if (suppliers && !customers) {
            suppRadio.setSelected(true);
        }
        if (customers && suppliers) {
            allRadio.setSelected(true);
        }
        refreshTable();

        if (select) {
            saveButton.setVisible(true);
            saveButton.setEnabled(false);
            selectButton.setVisible(true);
            cancelButton.setVisible(true);
            setFieldsEnabled(false);

        } else {
            saveButton.setVisible(true);
            saveButton.setEnabled(false);
            selectButton.setVisible(false);
            cancelButton.setVisible(false);
            setFieldsEnabled(false);
        }

        findField.requestFocus();

        GUIUtil.setWindowPosition(application, this);

        this.setVisible(true);
    }
    private String workingPath = "";

    
    /*
     * This is called for the "filter" radio button action events
     */
    private BeanTableModel filter() {
        try {
            if (allRadio.isSelected()) {
                return dao.getTableModelAll(0);                
            }
            if (custRadio.isSelected()) {
                return dao.getBeanTableModel("customer");                
            }
            if (suppRadio.isSelected()) {
                return dao.getBeanTableModel("supplier");
            }
            if (unpaidRadio.isSelected()) {
                return dao.getBeanTableModelUnpaid();
            }

            return dao.getTableModelAll(0);            
        } catch (Exception ex) {
            ExceptionErrorDialog errDialog = new ExceptionErrorDialog(ex);
            errDialog.pack();
            errDialog.setVisible(true);
        }
        return null;
    }

    /*
     * Return the selected user to a calling dialog (for selecting invoice customer)
     */
    public Connections getSelectedItem() {
        return connectionBean;
    }

    /*
     * Called to clear the data entry form
     */
    private void clearFields() {
        connectionBean = new Connections();

        String zone = application.getAppGlobalSettings().getCountryCode();
        companyNameField.setText("");
        firstNameField.setText("");
        sirNameField.setText("");
        addressField.setText("");
        address2Field.setText("");
        cityField.setText("");
        provenceField.setText("");
        pinCodeField.setText("");
        countryCombo.setSelectedItem(zone);
        emailField.setText("");
        websiteField.setText("http://");
        notesTextArea.setText("");
        customerTypeRadio.setSelected(false);
        supplierCheckBox.setSelected(false);
        tax1CheckBox.setSelected(false);
        tax2CheckBox.setSelected(false);

        fileList.setModel(new javax.swing.DefaultListModel());
        journalTextArea.setText("");
    }

    /*
     * Called to disable the data entry form (initial view/saving/clear)
     */
    private void setFieldsEnabled(boolean enabled) {

        employeeCustomerIdField.setEnabled(enabled);
        payrollButton.setEnabled(enabled);
        supplierIdField.setEnabled(enabled);

        tax1CheckBox.setEnabled(enabled);
        tax2CheckBox.setEnabled(enabled);

        inceptionDateField.setEnabled(enabled);

        companyNameField.setEnabled(enabled);
        firstNameField.setEnabled(enabled);
        sirNameField.setEnabled(enabled);
        loginNameField.setEnabled(enabled);
        passwordButton.setEnabled(enabled);
        emailField.setEnabled(enabled);
        websiteField.setEnabled(enabled);
        notesTextArea.setEnabled(enabled);

        employeeTypeRadio.setEnabled(enabled);
        customerTypeRadio.setEnabled(enabled);
        supplierCheckBox.setEnabled(enabled);


        saveButton.setEnabled(enabled);
        viewButton.setEnabled(enabled);
        fileList.setEnabled(enabled);
        journalTextArea.setEnabled(enabled);

        mapButton.setEnabled(enabled);
        defaultAddressButton.setEnabled(enabled);
        addressDialogButton.setEnabled(enabled);

        if (connectionBean != null && connectionBean.getId() != null && connectionBean.getId() > 0) {
            newButton.setEnabled(enabled);
        } else {
            newButton.setEnabled(false);
        }

        if (enabled) {
            messageField.setText("Remember to click 'Save' when you modify a record.");
        } else {

            messageField.setText("Click the Company Field to start a new record.");

        }

    }

    /*
     * Called when keyboard or mouse activity fires an event to show selected
     * row detail from the table view.
     */
    private void populateFields() {
        try {
            if (connTable.getSelectedRow() > -1) {
                btm = (BeanTableModel) connTable.getModel();
                connectionBean = (Connections) btm.getBeanAt(connTable.getSelectedRow());
                dao.getSess().update(connectionBean);

                btm = new BeanTableModel(ConnectionsAddress.class, connectionBean.getConnectionsAddressList());
                if (btm.getRowCount() > 0) {
                    for (int r = 0; r < btm.getRowCount(); r++) {
                        addressBean = (ConnectionsAddress) btm.getBeanAt(r);
                        if (addressBean.getDefaultAddress()) {
                            break;
                        }
                    }
                } else {
                    addressBean = null;
                }

                phoneTable.setModel(new BeanTableModel(ConnectionsPhones.class, connectionBean.getConnectionsPhonesList()));

                //phoneBean = new ArrayList<ConnectionsPhones>(connectionBean.getConnectionsPhonesList());

            } else {
                return;
            }

            if (connectionBean.getCustomer() == null || connectionBean.getCustomer()
                    && (connectionBean.getCustomerId() == null || connectionBean.getCustomerId().isEmpty())) {
                accountIdButton.setVisible(true);
            } else {
                accountIdButton.setVisible(false);
            }

            if (connectionBean.getEmployee() == null || connectionBean.getEmployee()
                    && (connectionBean.getEmployeeId() == null || connectionBean.getEmployeeId().isEmpty())) {
                accountIdButton.setVisible(true);
            } else {
                accountIdButton.setVisible(false);
            }

            if (connectionBean.getCustomer() != null) {
                customerTypeRadio.setSelected(connectionBean.getCustomer());
            }
            if (connectionBean.getEmployee() != null) {
                employeeTypeRadio.setSelected(connectionBean.getEmployee());
            }
            if (connectionBean.getSupplier() != null) {
                supplierCheckBox.setSelected(connectionBean.getSupplier());
            }
            companyNameField.setText(connectionBean.getCompanyName());
            firstNameField.setText(connectionBean.getFirstName());
            sirNameField.setText(connectionBean.getLastName());

            if (addressBean != null) {
                addressField.setText(addressBean.getAddress());
                address2Field.setText(addressBean.getAddress2());
                cityField.setText(addressBean.getCity());
                provenceField.setText(addressBean.getProvence());
                pinCodeField.setText(addressBean.getPostCode());
                countryCombo.setSelectedItem(addressBean.getCountryCode());
            }
            emailField.setText(connectionBean.getEmail());
            websiteField.setText(connectionBean.getWebsite());
            notesTextArea.setText(connectionBean.getNote());

            tax1CheckBox.setSelected(connectionBean.getTax1());
            tax2CheckBox.setSelected(connectionBean.getTax2());

        } catch (Exception ex) {
            ExceptionErrorDialog errDialog = new ExceptionErrorDialog(ex);
            errDialog.pack();
            errDialog.setVisible(true);
        }

        //Set active field based on supplier
        //activeCheckBox.setEnabled( supplierCheckBox.isSelected() );
        //populateInvoices(false);
        //populateJournals();

        //keyLabel.setText(Integer.toString(edit_key));  //show the user the key for the record
        this.setFieldsEnabled(true);

    }

    /* 
     * Called to display the desired columns in the table view and to set their widths
     */
    private void setTableView() {
        //Main Table
        ArrayList<String> list = new ArrayList<String>();
        list.add("Company Name");
        list.add("First Name");
        list.add("Last Name");
        ArrayList<Integer> widths = new ArrayList<Integer>();
        widths.add(25);
        widths.add(20);
        widths.add(30);

        try {
            TableUtil.removeAllCols(connTable, list);
            TableUtil.orderColumns(connTable, list);
            TableUtil.sizeColumns(connTable, widths);
        } catch (Exception ex) {
            ExceptionErrorDialog errDialog = new ExceptionErrorDialog(ex);
            errDialog.pack();
            errDialog.setVisible(true);
        }

    }

    /*
     * Called for search operations based on chosen field
     */
    private void find() {

        if (!findField.getText().equals("")) {

            searchColumn = searchFieldCombo.getSelectedIndex() + 1;

                            props.setProp("CONN COL", Integer.toString(searchColumn - 1));

           

        } else {
            refreshTable();
        }

    }

    /*
     * Called to generate a mailmerge data source for external programs
     */
    private void export(String filename) {

        //System.out.println(filename);
        ReportModel rm = new ReportModel(connTable.getModel());
        StringBuilder sb = new StringBuilder();
        int col_count = connTable.getModel().getColumnCount();

        /* Headers  */
        if (!new File(filename).exists()) {

            String[] headers = db.getFieldNames("conn");

            for (int i = 0; i < headers.length; i++) {

                sb.append(headers[i]);
                if (i < headers.length - 1) {
                    sb.append(',');
                }

            }

            sb.append(System.getProperty("line.separator"));

        }

        /* Data  */
        do {

            for (int c = 0; c < col_count; c++) {

                sb.append(rm.getValueAt(c).replace(',', ';'));
                if (c < connTable.getModel().getColumnCount() - 1) {
                    sb.append(',');
                }

            }

            sb.append(System.getProperty("line.separator"));

        } while (rm.next());

        DV.writeFile(filename, sb.toString(), true);

    }

    /*
     * Initiates a new journal in response to user actions
     */
    private void newJournal() {

        String date = DV.getShortDate().replace('/', '-');
        String tmp = "";
        int elements = lm.getSize();

        boolean match = false;

        for (int e = 0; e < elements; e++) {

            tmp = (String) lm.getElementAt(e);

            if (tmp.equals(date)) {
                match = true;
            }

        }

        if (!match && edit_key != 0) {

            File jFile = new File(workingPath + "jrnls/" + Integer.toString(edit_key) + "/");

            if (!jFile.exists()) {
                jFile.mkdirs();
            }

            DV.writeFile(jFile.toString() + "/" + date, DV.getFullDate(), false);
            lm.insertElementAt(date, 0);

        }

    }

    private void saveJournal() {

        int idx = fileList.getSelectedIndex();
        fileList.setEnabled(true);

        String text = journalTextArea.getText();

        if (!text.equals("") && idx > -1) {

            DV.writeFile(workingPath + "jrnls/" + Integer.toString(edit_key) + "/" + (String) lm.getElementAt(idx), text, false);


        } else {
            journalTextArea.setText("");
        }



    }

    private void getJournal() {


        int sel = fileList.getSelectedIndex();

        if (sel > -1) {

            String file = (String) lm.getElementAt(sel);

            journalTextArea.setText(DV.readFile(workingPath + "jrnls/" + Integer.toString(edit_key) + "/" + file));

        }

    }

    public static void launch(String com, String target) {


        String osName = System.getProperty("os.name");

        try {

            if (osName.contains("Windows")) {
                //Runtime.getRuntime().exec('"' + acro + '"' + " " + file.replace('/','\\'));

                String[] cm = {"cmd.exe", com + target};
                Runtime.getRuntime().exec(cm);

            } //FOR WINDOWS NT/XP/2000 USE CMD.EXE
            else {

                Runtime.getRuntime().exec(com + target);
                //System.out.println("cmd.exe start " + '"' + "c:\\Program Files\\Adobe\\Acrobat*\\Acrobat\\acrobat " + file.replace('/','\\') + '"');
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }



    }

    /*
     * Zip code retrieval for button action event
     */
    private void zipAction() {


        java.util.ArrayList al = null;

        if (pinCodeField.getText().length() > 4 && pinCodeField.getText().length() < 6 && DV.validIntString(pinCodeField.getText())) {

            al = zip.searchFast("zip", 1, pinCodeField.getText(), false);

        }

        if (al != null) {
            Object[] zipinfo = new Object[6];
            zipinfo = zip.getRecord("zip", (Long) al.get(0));
            cityField.setText((String) zipinfo[2]);
            provenceField.setText((String) zipinfo[3]);
        }

    }

    /*
     * This should fall out of use in light of hibernate integration
     */
    private void populateJournals() {

        lm = new javax.swing.DefaultListModel();


        journalTextArea.setText("");

        String path = workingPath + "jrnls/" + Integer.toString(edit_key) + "/";

        File dir = new File(path);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        String[] files = dir.list();

        for (int i = files.length - 1; i > -1; i--) {

            lm.addElement(files[i]);

        }
        if (files.length < 1) {
            journalTextArea.setEnabled(false);
        }

        fileList.setModel(lm);

    }

    /**
     * This method is called from within the constructor to initialize the form. WARNING: Do NOT modify this code. The
     * content of this method is always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        buttonGroup2 = new javax.swing.ButtonGroup();
        customerEmployeeGroup = new javax.swing.ButtonGroup();
        jScrollPane1 = new javax.swing.JScrollPane();
        connTable = new javax.swing.JTable();
        jPanel4 = new javax.swing.JPanel();
        jLabel15 = new javax.swing.JLabel();
        findField = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        searchFieldCombo = new javax.swing.JComboBox();
        functionToolbar = new javax.swing.JToolBar();
        toggleButton = new javax.swing.JButton();
        labelButton = new javax.swing.JButton();
        exportButton = new javax.swing.JButton();
        deleteButton = new javax.swing.JButton();
        filterToolbar = new javax.swing.JToolBar();
        allRadio = new javax.swing.JToggleButton();
        custRadio = new javax.swing.JToggleButton();
        suppRadio = new javax.swing.JToggleButton();
        employeeRadio = new javax.swing.JToggleButton();
        unpaidRadio = new javax.swing.JToggleButton();
        jSeparator1 = new javax.swing.JToolBar.Separator();
        selectButton = new javax.swing.JButton();
        cancelButton = new javax.swing.JButton();
        detailPanel = new javax.swing.JPanel();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        idPanel = new javax.swing.JPanel();
        jPanel8 = new javax.swing.JPanel();
        supplierCheckBox = new javax.swing.JCheckBox();
        customerTypeRadio = new javax.swing.JRadioButton();
        employeeTypeRadio = new javax.swing.JRadioButton();
        jPanel9 = new javax.swing.JPanel();
        tax1CheckBox = new javax.swing.JCheckBox();
        tax2CheckBox = new javax.swing.JCheckBox();
        jPanel10 = new javax.swing.JPanel();
        companyNameField = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        firstNameField = new javax.swing.JTextField();
        sirNameField = new javax.swing.JTextField();
        jLabel24 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        loginNameField = new javax.swing.JTextField();
        emailButton = new javax.swing.JButton();
        emailField = new javax.swing.JTextField();
        wwwButton = new javax.swing.JButton();
        websiteField = new javax.swing.JTextField();
        passwordButton = new javax.swing.JButton();
        jPanel11 = new javax.swing.JPanel();
        jLabel22 = new javax.swing.JLabel();
        employeeCustomerIdField = new javax.swing.JTextField();
        employeeCustomerLabel = new javax.swing.JLabel();
        supplierIdField = new javax.swing.JTextField();
        jToolBar3 = new javax.swing.JToolBar();
        accountIdButton = new javax.swing.JButton();
        payrollButton = new javax.swing.JButton();
        jToolBar5 = new javax.swing.JToolBar();
        supplierIDButton = new javax.swing.JButton();
        inceptionDateField = new com.michaelbaranov.microba.calendar.DatePicker();
        jLabel4 = new javax.swing.JLabel();
        addressPanel = new javax.swing.JPanel();
        jPanel6 = new javax.swing.JPanel();
        pinCodeField = new javax.swing.JTextField();
        provenceField = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        cityField = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        countryCombo = new javax.swing.JComboBox();
        jLabel6 = new javax.swing.JLabel();
        address2Field = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        addressField = new javax.swing.JTextField();
        jLabel13 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        latitudeField = new javax.swing.JTextField();
        jLabel20 = new javax.swing.JLabel();
        longitudeField = new javax.swing.JTextField();
        jToolBar1 = new javax.swing.JToolBar();
        mapButton = new javax.swing.JButton();
        defaultAddressButton = new javax.swing.JButton();
        addressDialogButton = new javax.swing.JButton();
        jPanel5 = new javax.swing.JPanel();
        jScrollPane5 = new javax.swing.JScrollPane();
        phoneTable = new javax.swing.JTable();
        jComboBox1 = new javax.swing.JComboBox();
        phoneField = new javax.swing.JTextField();
        jToolBar2 = new javax.swing.JToolBar();
        jButton6 = new javax.swing.JButton();
        jButton7 = new javax.swing.JButton();
        jButton8 = new javax.swing.JButton();
        contactPanel = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        invoiceTable = new javax.swing.JTable();
        invoiceToolbar = new javax.swing.JToolBar();
        viewButton = new javax.swing.JButton();
        invoiceReportButton = new javax.swing.JButton();
        purchaseHistoryButton = new javax.swing.JButton();
        invoiceLabel = new javax.swing.JLabel();
        invoiceToggleButton = new javax.swing.JButton();
        journalPanel = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        notesTextArea = new javax.swing.JTextField();
        jScrollPane3 = new javax.swing.JScrollPane();
        fileList = new javax.swing.JList();
        newButton = new javax.swing.JButton();
        jScrollPane4 = new javax.swing.JScrollPane();
        journalTextArea = new javax.swing.JTextPane();
        jLabel17 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        imagePanel = new javax.swing.JPanel();
        jPanel7 = new javax.swing.JPanel();
        viewImageButton = new javax.swing.JButton();
        jToolBar4 = new javax.swing.JToolBar();
        clearButton = new javax.swing.JButton();
        saveButton = new javax.swing.JButton();
        messageField = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("My Connections");
        setIconImage(winIcon);
        setModal(true);

        connTable.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        connTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        connTable.setSelectionBackground(new java.awt.Color(204, 255, 255));
        connTable.setSelectionForeground(new java.awt.Color(0, 51, 51));
        connTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                connTableMouseClicked(evt);
            }
        });
        connTable.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                connTableKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                connTableKeyReleased(evt);
            }
        });
        jScrollPane1.setViewportView(connTable);

        jPanel4.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));

        findField.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        findField.setToolTipText("Input Search Text Here and Hit ENTER to Search");
        findField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                findFieldFocusGained(evt);
            }
        });
        findField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                findFieldKeyPressed(evt);
            }
        });

        jLabel9.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/datavirtue/nevitiumpro/res/Aha-16/enabled/Zoom.png"))); // NOI18N
        jLabel9.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel9MouseClicked(evt);
            }
        });

        searchFieldCombo.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        searchFieldCombo.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Company", "First", "Last", "Address", "Addr #2", "City/Town", "State/Prov/Region", "Post Code", "Contact", "Phone", "Fax", "Email", "WWW", "Misc" }));

        functionToolbar.setFloatable(false);
        functionToolbar.setRollover(true);

        toggleButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/datavirtue/nevitiumpro/res/Aha-16/enabled/Down.png"))); // NOI18N
        toggleButton.setText("Less Detail");
        toggleButton.setToolTipText("Click this to Toggle the Form On or Off");
        toggleButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        toggleButton.setPreferredSize(new java.awt.Dimension(85, 49));
        toggleButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        toggleButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                toggleButtonActionPerformed(evt);
            }
        });
        functionToolbar.add(toggleButton);

        labelButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/datavirtue/nevitiumpro/res/Aha-16/enabled/Tag.png"))); // NOI18N
        labelButton.setText("Labels");
        labelButton.setToolTipText("Select Rows and Click this Button to Generate Labels");
        labelButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        labelButton.setPreferredSize(new java.awt.Dimension(78, 49));
        labelButton.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        labelButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        labelButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                labelButtonActionPerformed(evt);
            }
        });
        functionToolbar.add(labelButton);

        exportButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/datavirtue/nevitiumpro/res/Aha-16/enabled/Export text.png"))); // NOI18N
        exportButton.setText("Export");
        exportButton.setToolTipText("Export the Current Table to a (.csv text) File");
        exportButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        exportButton.setPreferredSize(new java.awt.Dimension(78, 49));
        exportButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        exportButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exportButtonActionPerformed(evt);
            }
        });
        functionToolbar.add(exportButton);

        deleteButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/datavirtue/nevitiumpro/res/Aha-16/enabled/Delete.png"))); // NOI18N
        deleteButton.setText("Delete");
        deleteButton.setToolTipText("Permenant Delete!");
        deleteButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        deleteButton.setMargin(new java.awt.Insets(2, 10, 2, 10));
        deleteButton.setPreferredSize(new java.awt.Dimension(78, 53));
        deleteButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        deleteButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteButtonActionPerformed(evt);
            }
        });
        functionToolbar.add(deleteButton);

        filterToolbar.setFloatable(false);
        filterToolbar.setRollover(true);
        filterToolbar.setBorderPainted(false);

        buttonGroup1.add(allRadio);
        allRadio.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/datavirtue/nevitiumpro/res/Aha-16/enabled/Navigator.png"))); // NOI18N
        allRadio.setText("All Records");
        allRadio.setFocusable(false);
        allRadio.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        allRadio.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        allRadio.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                allRadioActionPerformed(evt);
            }
        });
        filterToolbar.add(allRadio);

        buttonGroup1.add(custRadio);
        custRadio.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/datavirtue/nevitiumpro/res/Aha-16/enabled/Customers.png"))); // NOI18N
        custRadio.setText("Cutomers");
        custRadio.setFocusable(false);
        custRadio.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        custRadio.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        custRadio.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                custRadioActionPerformed(evt);
            }
        });
        filterToolbar.add(custRadio);

        buttonGroup1.add(suppRadio);
        suppRadio.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/datavirtue/nevitiumpro/res/Aha-16/enabled/Trailer.png"))); // NOI18N
        suppRadio.setText("Suppliers");
        suppRadio.setFocusable(false);
        suppRadio.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        suppRadio.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        suppRadio.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                suppRadioActionPerformed(evt);
            }
        });
        filterToolbar.add(suppRadio);

        buttonGroup1.add(employeeRadio);
        employeeRadio.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/datavirtue/nevitiumpro/res/Aha-16/enabled/Conference.png"))); // NOI18N
        employeeRadio.setText("Employees");
        employeeRadio.setFocusable(false);
        employeeRadio.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        employeeRadio.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        employeeRadio.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                employeeRadioActionPerformed(evt);
            }
        });
        filterToolbar.add(employeeRadio);

        buttonGroup1.add(unpaidRadio);
        unpaidRadio.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/datavirtue/nevitiumpro/res/Aha-16/enabled/Disaster.png"))); // NOI18N
        unpaidRadio.setText("Unpaid");
        unpaidRadio.setFocusable(false);
        unpaidRadio.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        unpaidRadio.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        unpaidRadio.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                unpaidRadioActionPerformed(evt);
            }
        });
        filterToolbar.add(unpaidRadio);
        filterToolbar.add(jSeparator1);

        selectButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/datavirtue/nevitiumpro/res/Aha-16/enabled/OK.png"))); // NOI18N
        selectButton.setText("Select");
        selectButton.setFocusable(false);
        selectButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        selectButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        selectButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                selectButtonActionPerformed(evt);
            }
        });
        filterToolbar.add(selectButton);

        cancelButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/datavirtue/nevitiumpro/res/Aha-16/enabled/No.png"))); // NOI18N
        cancelButton.setText("None");
        cancelButton.setFocusable(false);
        cancelButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        cancelButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        cancelButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelButtonActionPerformed(evt);
            }
        });
        filterToolbar.add(cancelButton);

        org.jdesktop.layout.GroupLayout jPanel4Layout = new org.jdesktop.layout.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel4Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jPanel4Layout.createSequentialGroup()
                        .add(searchFieldCombo, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .add(10, 10, 10)
                        .add(findField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 165, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jLabel9, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 34, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                    .add(jLabel15)
                    .add(filterToolbar, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(functionToolbar, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel4Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, functionToolbar, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 85, Short.MAX_VALUE)
                    .add(jPanel4Layout.createSequentialGroup()
                        .add(jPanel4Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(jLabel15)
                            .add(filterToolbar, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 44, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jPanel4Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                            .add(jLabel9, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 35, Short.MAX_VALUE)
                            .add(searchFieldCombo, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 35, Short.MAX_VALUE)
                            .add(findField))))
                .addContainerGap())
        );

        detailPanel.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jTabbedPane1.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N

        jPanel8.setBorder(javax.swing.BorderFactory.createTitledBorder("Type"));

        supplierCheckBox.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        supplierCheckBox.setText("Supplier  ");
        supplierCheckBox.setToolTipText("Marks This Contact as a Supplier");
        supplierCheckBox.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        supplierCheckBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                supplierCheckBoxActionPerformed(evt);
            }
        });

        customerEmployeeGroup.add(customerTypeRadio);
        customerTypeRadio.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        customerTypeRadio.setSelected(true);
        customerTypeRadio.setText("Customer");
        customerTypeRadio.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                customerTypeRadioActionPerformed(evt);
            }
        });

        customerEmployeeGroup.add(employeeTypeRadio);
        employeeTypeRadio.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        employeeTypeRadio.setText("Employee");
        employeeTypeRadio.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                employeeTypeRadioActionPerformed(evt);
            }
        });

        org.jdesktop.layout.GroupLayout jPanel8Layout = new org.jdesktop.layout.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .add(customerTypeRadio)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                .add(employeeTypeRadio, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 79, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                .add(supplierCheckBox)
                .addContainerGap(18, Short.MAX_VALUE))
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel8Layout.createSequentialGroup()
                .add(jPanel8Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(customerTypeRadio)
                    .add(employeeTypeRadio)
                    .add(supplierCheckBox))
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel9.setBorder(javax.swing.BorderFactory.createTitledBorder("Sales Taxes"));

        tax1CheckBox.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        tax1CheckBox.setText("Tax 1");
        tax1CheckBox.setToolTipText("Tax Status");
        tax1CheckBox.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));

        tax2CheckBox.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        tax2CheckBox.setText("Tax 2");
        tax2CheckBox.setToolTipText("Tax Status");
        tax2CheckBox.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));

        org.jdesktop.layout.GroupLayout jPanel9Layout = new org.jdesktop.layout.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .add(tax1CheckBox)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                .add(tax2CheckBox)
                .addContainerGap())
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel9Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(tax1CheckBox)
                    .add(tax2CheckBox))
                .addContainerGap())
        );

        companyNameField.setColumns(35);
        companyNameField.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        companyNameField.setToolTipText("Click here to Create a New Contact [35 Char] [Company or First Name REQUIRED]");
        companyNameField.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                companyNameFieldMouseClicked(evt);
            }
        });

        jLabel2.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel2.setText("Company Name");

        jLabel3.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel3.setText("First Name");

        firstNameField.setColumns(20);
        firstNameField.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        firstNameField.setToolTipText("[20 Char] Company or First Name REQUIRED");

        sirNameField.setColumns(20);
        sirNameField.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        sirNameField.setToolTipText("[20 Char]");

        jLabel24.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel24.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel24.setText("Sir Name");

        jLabel16.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel16.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel16.setText("Login Name");

        loginNameField.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N

        emailButton.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        emailButton.setText("Email");
        emailButton.setToolTipText("New Email");
        emailButton.setMargin(new java.awt.Insets(1, 2, 1, 2));
        emailButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                emailButtonActionPerformed(evt);
            }
        });

        emailField.setColumns(40);
        emailField.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        emailField.setToolTipText("[40 Char]");

        wwwButton.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        wwwButton.setText("Website");
        wwwButton.setToolTipText("Launch");
        wwwButton.setMargin(new java.awt.Insets(1, 2, 1, 2));
        wwwButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                wwwButtonActionPerformed(evt);
            }
        });

        websiteField.setColumns(50);
        websiteField.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        websiteField.setText("http://");
        websiteField.setToolTipText("[50 Char] Must Contain a Protocol Such as http:// or https://");
        websiteField.setNextFocusableComponent(notesTextArea);

        passwordButton.setText("Change Password");

        org.jdesktop.layout.GroupLayout jPanel10Layout = new org.jdesktop.layout.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel10Layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel10Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jPanel10Layout.createSequentialGroup()
                        .add(jPanel10Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(emailButton, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 93, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(wwwButton, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 93, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                        .add(4, 4, 4)
                        .add(jPanel10Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
                            .add(emailField, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 531, Short.MAX_VALUE)
                            .add(websiteField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)))
                    .add(jPanel10Layout.createSequentialGroup()
                        .add(jPanel10Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(jLabel2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 93, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(jPanel10Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
                                .add(jLabel24, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .add(jLabel3, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 93, Short.MAX_VALUE))
                            .add(jLabel16, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 93, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jPanel10Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(firstNameField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 178, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(companyNameField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 255, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(sirNameField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 178, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(jPanel10Layout.createSequentialGroup()
                                .add(loginNameField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 178, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(passwordButton)))))
                .addContainerGap())
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, jPanel10Layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel10Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(companyNameField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jLabel2))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel10Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel3)
                    .add(firstNameField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .add(6, 6, 6)
                .add(jPanel10Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(sirNameField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jLabel24))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel10Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel16)
                    .add(loginNameField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(passwordButton))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel10Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
                    .add(emailField)
                    .add(emailButton, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel10Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
                    .add(websiteField)
                    .add(wwwButton, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel11.setBorder(javax.swing.BorderFactory.createTitledBorder("Unique Codes"));

        jLabel22.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel22.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel22.setText("Supplier ID");

        employeeCustomerIdField.setEditable(false);

        employeeCustomerLabel.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        employeeCustomerLabel.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        employeeCustomerLabel.setText("Customer ID");

        supplierIdField.setEditable(false);

        jToolBar3.setFloatable(false);
        jToolBar3.setRollover(true);

        accountIdButton.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        accountIdButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/datavirtue/nevitiumpro/res/Aha-16/enabled/Counter.png"))); // NOI18N
        accountIdButton.setText("Get ID");
        accountIdButton.setFocusable(false);
        accountIdButton.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        accountIdButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        accountIdButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                accountIdButtonActionPerformed(evt);
            }
        });
        jToolBar3.add(accountIdButton);

        payrollButton.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        payrollButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/datavirtue/nevitiumpro/res/Aha-16/enabled/Client list.png"))); // NOI18N
        payrollButton.setText("Payroll");
        payrollButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                payrollButtonActionPerformed(evt);
            }
        });
        jToolBar3.add(payrollButton);

        jToolBar5.setFloatable(false);
        jToolBar5.setRollover(true);

        supplierIDButton.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        supplierIDButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/datavirtue/nevitiumpro/res/Aha-16/enabled/Counter.png"))); // NOI18N
        supplierIDButton.setText("Get ID");
        supplierIDButton.setFocusable(false);
        supplierIDButton.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        supplierIDButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToolBar5.add(supplierIDButton);

        org.jdesktop.layout.GroupLayout jPanel11Layout = new org.jdesktop.layout.GroupLayout(jPanel11);
        jPanel11.setLayout(jPanel11Layout);
        jPanel11Layout.setHorizontalGroup(
            jPanel11Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel11Layout.createSequentialGroup()
                .addContainerGap()
                .add(employeeCustomerLabel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 80, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(employeeCustomerIdField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 98, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jToolBar3, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 136, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                .add(jLabel22, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 80, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(supplierIdField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 88, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                .add(jToolBar5, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 100, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel11Layout.setVerticalGroup(
            jPanel11Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel11Layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel11Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jPanel11Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                        .add(jLabel22, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 14, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .add(supplierIdField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                    .add(jPanel11Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                        .add(employeeCustomerLabel)
                        .add(employeeCustomerIdField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                    .add(jToolBar3, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 25, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jToolBar5, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 25, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jLabel4.setText("Inception Date");

        org.jdesktop.layout.GroupLayout idPanelLayout = new org.jdesktop.layout.GroupLayout(idPanel);
        idPanel.setLayout(idPanelLayout);
        idPanelLayout.setHorizontalGroup(
            idPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(idPanelLayout.createSequentialGroup()
                .addContainerGap()
                .add(idPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(idPanelLayout.createSequentialGroup()
                        .add(jPanel8, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jPanel9, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(idPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
                            .add(inceptionDateField, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 158, Short.MAX_VALUE)
                            .add(jLabel4, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .add(0, 0, Short.MAX_VALUE))
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, idPanelLayout.createSequentialGroup()
                        .add(idPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                            .add(org.jdesktop.layout.GroupLayout.LEADING, jPanel11, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .add(jPanel10, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addContainerGap())))
        );
        idPanelLayout.setVerticalGroup(
            idPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(idPanelLayout.createSequentialGroup()
                .addContainerGap()
                .add(idPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
                    .add(idPanelLayout.createSequentialGroup()
                        .add(9, 9, 9)
                        .add(jLabel4)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(inceptionDateField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                    .add(jPanel9, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 57, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jPanel8, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel11, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 60, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel10, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 190, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Main", idPanel);

        addressPanel.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));

        jPanel6.setBorder(javax.swing.BorderFactory.createTitledBorder("Default Address"));

        pinCodeField.setEditable(false);
        pinCodeField.setColumns(10);
        pinCodeField.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        pinCodeField.setToolTipText("[10 Char Stored]");
        pinCodeField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                pinCodeFieldKeyPressed(evt);
            }
        });

        provenceField.setEditable(false);
        provenceField.setColumns(2);
        provenceField.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        provenceField.setToolTipText("[2 Char] Locality");

        jLabel8.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel8.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel8.setText("State/Prov/Region");

        cityField.setEditable(false);
        cityField.setColumns(30);
        cityField.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        cityField.setToolTipText("[30 Char] Dependent Locality");
        cityField.setNextFocusableComponent(provenceField);

        jLabel7.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel7.setText("City/Town");

        jLabel14.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel14.setText("Format");

        countryCombo.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        countryCombo.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "US", "CA", "AU", "UK", "ZA", "IN", "NZ", "PH" }));
        countryCombo.setToolTipText("Sets the Country Code for this Contact");
        countryCombo.setEnabled(false);

        jLabel6.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel6.setText("Addr #2");

        address2Field.setEditable(false);
        address2Field.setColumns(10);
        address2Field.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        address2Field.setToolTipText("[40 Char] 38 Characters is the Suggested Limit for Address Lines");

        jLabel5.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel5.setText("Address");

        addressField.setEditable(false);
        addressField.setColumns(40);
        addressField.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        addressField.setToolTipText("[40 Char] 38 Characters is the Suggested Limit for Address Lines");

        jLabel13.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel13.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel13.setText("Post Code");

        jLabel19.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel19.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel19.setText("Latitude");

        latitudeField.setEditable(false);
        latitudeField.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N

        jLabel20.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel20.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel20.setText("Longitude");

        longitudeField.setEditable(false);
        longitudeField.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        longitudeField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                longitudeFieldActionPerformed(evt);
            }
        });

        jToolBar1.setFloatable(false);
        jToolBar1.setRollover(true);

        mapButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/datavirtue/nevitiumpro/res/Aha-16/enabled/Earth.png"))); // NOI18N
        mapButton.setText("Get Map");
        mapButton.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        jToolBar1.add(mapButton);

        defaultAddressButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/datavirtue/nevitiumpro/res/Aha-16/enabled/Account card.png"))); // NOI18N
        defaultAddressButton.setText("Edit Default");
        defaultAddressButton.setFocusable(false);
        defaultAddressButton.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        defaultAddressButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        defaultAddressButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                defaultAddressButtonActionPerformed(evt);
            }
        });
        jToolBar1.add(defaultAddressButton);

        addressDialogButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/datavirtue/nevitiumpro/res/Aha-16/enabled/Account cards.png"))); // NOI18N
        addressDialogButton.setText("More Addresses");
        addressDialogButton.setFocusable(false);
        addressDialogButton.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        addressDialogButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        addressDialogButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addressDialogButtonActionPerformed(evt);
            }
        });
        jToolBar1.add(addressDialogButton);

        org.jdesktop.layout.GroupLayout jPanel6Layout = new org.jdesktop.layout.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel6Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jToolBar1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(jPanel6Layout.createSequentialGroup()
                        .add(jPanel6Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(jLabel6, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 101, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(jLabel5, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 101, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .add(jPanel6Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
                            .add(addressField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 1, Short.MAX_VALUE)
                            .add(address2Field, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 282, Short.MAX_VALUE)))
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, jPanel6Layout.createSequentialGroup()
                        .add(jPanel6Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(jPanel6Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                                .add(jLabel8)
                                .add(jLabel20, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 86, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                .add(jLabel7, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 101, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                            .add(jLabel19, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 101, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(jLabel13, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 101, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .add(jPanel6Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(jPanel6Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING, false)
                                .add(org.jdesktop.layout.GroupLayout.LEADING, provenceField)
                                .add(org.jdesktop.layout.GroupLayout.LEADING, jPanel6Layout.createSequentialGroup()
                                    .add(pinCodeField, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 88, Short.MAX_VALUE)
                                    .add(6, 6, 6)
                                    .add(jLabel14)
                                    .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                    .add(countryCombo, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 72, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                                .add(cityField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 1, Short.MAX_VALUE))
                            .add(jPanel6Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING, false)
                                .add(org.jdesktop.layout.GroupLayout.LEADING, latitudeField, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 88, Short.MAX_VALUE)
                                .add(org.jdesktop.layout.GroupLayout.LEADING, longitudeField)))
                        .add(74, 74, 74)))
                .addContainerGap())
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel6Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel5, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 16, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(addressField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel6Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel6, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 14, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(address2Field, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel6Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(cityField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jLabel7))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel6Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel8)
                    .add(provenceField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel6Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(pinCodeField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jLabel14)
                    .add(countryCombo, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jLabel13))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel6Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel19)
                    .add(latitudeField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel6Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel20)
                    .add(longitudeField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jToolBar1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 25, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel5.setBorder(javax.swing.BorderFactory.createTitledBorder("Phone Numbers"));

        phoneTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane5.setViewportView(phoneTable);

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Default", "Mobile", "Work", "Home", "Fax" }));

        jToolBar2.setFloatable(false);
        jToolBar2.setRollover(true);

        jButton6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/datavirtue/nevitiumpro/res/Aha-16/enabled/Corrupt text.png"))); // NOI18N
        jButton6.setToolTipText("Cancel/Clear modifications");
        jButton6.setFocusable(false);
        jButton6.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        jButton6.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToolBar2.add(jButton6);

        jButton7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/datavirtue/nevitiumpro/res/Aha-16/enabled/Floppy.png"))); // NOI18N
        jButton7.setToolTipText("Save selected phone number");
        jButton7.setFocusable(false);
        jButton7.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        jButton7.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToolBar2.add(jButton7);

        jButton8.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/datavirtue/nevitiumpro/res/Aha-16/enabled/Delete.png"))); // NOI18N
        jButton8.setToolTipText("Delete selected phone number");
        jButton8.setFocusable(false);
        jButton8.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        jButton8.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToolBar2.add(jButton8);

        org.jdesktop.layout.GroupLayout jPanel5Layout = new org.jdesktop.layout.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel5Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(phoneField)
                    .add(jToolBar2, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(jPanel5Layout.createSequentialGroup()
                        .add(jComboBox1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 100, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .add(0, 87, Short.MAX_VALUE)))
                .addContainerGap())
            .add(jScrollPane5, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel5Layout.createSequentialGroup()
                .add(jScrollPane5, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 175, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                .add(jComboBox1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(phoneField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(18, 18, 18)
                .add(jToolBar2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 25, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        org.jdesktop.layout.GroupLayout addressPanelLayout = new org.jdesktop.layout.GroupLayout(addressPanel);
        addressPanel.setLayout(addressPanelLayout);
        addressPanelLayout.setHorizontalGroup(
            addressPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(addressPanelLayout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel6, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel5, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        addressPanelLayout.setVerticalGroup(
            addressPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(addressPanelLayout.createSequentialGroup()
                .addContainerGap()
                .add(addressPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jPanel6, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(jPanel5, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        jTabbedPane1.addTab("Contact", addressPanel);

        contactPanel.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED, java.awt.Color.white, null));

        jPanel2.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        invoiceTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        invoiceTable.setToolTipText("Invoices found for this contact");
        invoiceTable.setSelectionBackground(new java.awt.Color(204, 255, 204));
        invoiceTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                invoiceTableMouseClicked(evt);
            }
        });
        invoiceTable.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                invoiceTableKeyPressed(evt);
            }
        });
        jScrollPane2.setViewportView(invoiceTable);

        invoiceToolbar.setFloatable(false);
        invoiceToolbar.setRollover(true);

        viewButton.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        viewButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/datavirtue/nevitiumpro/res/Aha-16/enabled/Eye.png"))); // NOI18N
        viewButton.setText("View");
        viewButton.setToolTipText("Open an Invoice or Quote");
        viewButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        viewButton.setMargin(new java.awt.Insets(1, 14, 1, 14));
        viewButton.setPreferredSize(new java.awt.Dimension(72, 49));
        viewButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        viewButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                viewButtonActionPerformed(evt);
            }
        });
        invoiceToolbar.add(viewButton);

        invoiceReportButton.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        invoiceReportButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/datavirtue/nevitiumpro/res/Aha-16/enabled/Paper.png"))); // NOI18N
        invoiceReportButton.setText("History");
        invoiceReportButton.setToolTipText("Customer's invoice history statement");
        invoiceReportButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        invoiceReportButton.setPreferredSize(new java.awt.Dimension(72, 47));
        invoiceReportButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        invoiceReportButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                invoiceReportButtonActionPerformed(evt);
            }
        });
        invoiceToolbar.add(invoiceReportButton);

        purchaseHistoryButton.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        purchaseHistoryButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/datavirtue/nevitiumpro/res/Aha-16/enabled/Credit.png"))); // NOI18N
        purchaseHistoryButton.setText("Purchases");
        purchaseHistoryButton.setToolTipText("Customer's product purchase history report");
        purchaseHistoryButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        purchaseHistoryButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        purchaseHistoryButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                purchaseHistoryButtonActionPerformed(evt);
            }
        });
        invoiceToolbar.add(purchaseHistoryButton);

        invoiceLabel.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        invoiceLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        invoiceLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/datavirtue/nevitiumpro/res/Aha-16/enabled/Rotate CW.png"))); // NOI18N
        invoiceLabel.setText("Invoices");
        invoiceLabel.setHorizontalTextPosition(javax.swing.SwingConstants.LEADING);

        invoiceToggleButton.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        invoiceToggleButton.setText("Show Quotes");
        invoiceToggleButton.setToolTipText("Toggles the Display of Quotes or Invoices");
        invoiceToggleButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                invoiceToggleButtonActionPerformed(evt);
            }
        });

        org.jdesktop.layout.GroupLayout jPanel2Layout = new org.jdesktop.layout.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, jScrollPane2, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 620, Short.MAX_VALUE)
                    .add(invoiceToolbar, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(invoiceToggleButton, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(invoiceLabel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .add(invoiceToggleButton)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(invoiceLabel)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jScrollPane2, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 181, Short.MAX_VALUE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(invoiceToolbar, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        org.jdesktop.layout.GroupLayout contactPanelLayout = new org.jdesktop.layout.GroupLayout(contactPanel);
        contactPanel.setLayout(contactPanelLayout);
        contactPanelLayout.setHorizontalGroup(
            contactPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, contactPanelLayout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel2, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        contactPanelLayout.setVerticalGroup(
            contactPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, contactPanelLayout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel2, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        jTabbedPane1.addTab("Invoices/Accounts", contactPanel);

        journalPanel.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED, java.awt.Color.white, null));

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel1.setText("Misc:");

        notesTextArea.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        notesTextArea.setToolTipText("[100 Character Limit]");
        notesTextArea.setNextFocusableComponent(saveButton);
        notesTextArea.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                notesTextAreaFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                notesTextAreaFocusLost(evt);
            }
        });

        fileList.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 255, 204)));
        fileList.setToolTipText("Daily Journals");
        fileList.setSelectionBackground(new java.awt.Color(204, 255, 255));
        fileList.setSelectionForeground(new java.awt.Color(0, 51, 51));
        fileList.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                fileListMouseClicked(evt);
            }
        });
        jScrollPane3.setViewportView(fileList);

        newButton.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        newButton.setText("New");
        newButton.setToolTipText("Create new journal for today");
        newButton.setMargin(new java.awt.Insets(1, 1, 1, 1));
        newButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                newButtonActionPerformed(evt);
            }
        });

        journalTextArea.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 255, 204)));
        journalTextArea.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        journalTextArea.setToolTipText("The selected journal's text");
        journalTextArea.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                journalTextAreaFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                journalTextAreaFocusLost(evt);
            }
        });
        journalTextArea.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                journalTextAreaMouseClicked(evt);
            }
        });
        jScrollPane4.setViewportView(journalTextArea);

        jLabel17.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel17.setText("Journal:");

        org.jdesktop.layout.GroupLayout journalPanelLayout = new org.jdesktop.layout.GroupLayout(journalPanel);
        journalPanel.setLayout(journalPanelLayout);
        journalPanelLayout.setHorizontalGroup(
            journalPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(journalPanelLayout.createSequentialGroup()
                .addContainerGap()
                .add(journalPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(journalPanelLayout.createSequentialGroup()
                        .add(jLabel1)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(notesTextArea))
                    .add(journalPanelLayout.createSequentialGroup()
                        .add(journalPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(journalPanelLayout.createSequentialGroup()
                                .add(newButton, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 55, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(jLabel17))
                            .add(jScrollPane3, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 111, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jScrollPane4, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 527, Short.MAX_VALUE)))
                .addContainerGap())
        );
        journalPanelLayout.setVerticalGroup(
            journalPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(journalPanelLayout.createSequentialGroup()
                .addContainerGap()
                .add(journalPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel1)
                    .add(notesTextArea, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(journalPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(journalPanelLayout.createSequentialGroup()
                        .add(journalPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                            .add(jLabel17)
                            .add(newButton))
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jScrollPane3, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 261, Short.MAX_VALUE))
                    .add(jScrollPane4))
                .addContainerGap())
        );

        jTabbedPane1.addTab("Notes", journalPanel);

        imagePanel.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        org.jdesktop.layout.GroupLayout imagePanelLayout = new org.jdesktop.layout.GroupLayout(imagePanel);
        imagePanel.setLayout(imagePanelLayout);
        imagePanelLayout.setHorizontalGroup(
            imagePanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 408, Short.MAX_VALUE)
        );
        imagePanelLayout.setVerticalGroup(
            imagePanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 0, Short.MAX_VALUE)
        );

        viewImageButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/datavirtue/nevitiumpro/res/Aha-16/enabled/Camera.png"))); // NOI18N
        viewImageButton.setText("View");
        viewImageButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                viewImageButtonActionPerformed(evt);
            }
        });

        org.jdesktop.layout.GroupLayout jPanel7Layout = new org.jdesktop.layout.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .add(viewImageButton, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 108, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(112, Short.MAX_VALUE))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, jPanel7Layout.createSequentialGroup()
                .addContainerGap(283, Short.MAX_VALUE)
                .add(viewImageButton)
                .addContainerGap())
        );

        org.jdesktop.layout.GroupLayout jPanel1Layout = new org.jdesktop.layout.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .add(imagePanel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel7, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                    .add(imagePanel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(jPanel7, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        jTabbedPane1.addTab("Picture", jPanel1);

        jToolBar4.setFloatable(false);
        jToolBar4.setRollover(true);

        clearButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/datavirtue/nevitiumpro/res/Aha-16/enabled/Corrupt text.png"))); // NOI18N
        clearButton.setText("Clear");
        clearButton.setToolTipText("Clears the Form");
        clearButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                clearButtonActionPerformed(evt);
            }
        });
        jToolBar4.add(clearButton);

        saveButton.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        saveButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/datavirtue/nevitiumpro/res/Aha-16/enabled/Floppy.png"))); // NOI18N
        saveButton.setText("Save");
        saveButton.setToolTipText("Save Modifications");
        saveButton.setMaximumSize(new java.awt.Dimension(79, 25));
        saveButton.setMinimumSize(new java.awt.Dimension(79, 25));
        saveButton.setNextFocusableComponent(companyNameField);
        saveButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveButtonActionPerformed(evt);
            }
        });
        jToolBar4.add(saveButton);

        messageField.setEditable(false);
        messageField.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jToolBar4.add(messageField);

        org.jdesktop.layout.GroupLayout detailPanelLayout = new org.jdesktop.layout.GroupLayout(detailPanel);
        detailPanel.setLayout(detailPanelLayout);
        detailPanelLayout.setHorizontalGroup(
            detailPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, detailPanelLayout.createSequentialGroup()
                .addContainerGap()
                .add(detailPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                    .add(org.jdesktop.layout.GroupLayout.LEADING, jTabbedPane1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .add(org.jdesktop.layout.GroupLayout.LEADING, jToolBar4, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        detailPanelLayout.setVerticalGroup(
            detailPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(detailPanelLayout.createSequentialGroup()
                .add(jToolBar4, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 25, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(18, 18, 18)
                .add(jTabbedPane1))
        );

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                    .add(org.jdesktop.layout.GroupLayout.LEADING, jScrollPane1)
                    .add(jPanel4, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(detailPanel, 0, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .add(jScrollPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 394, Short.MAX_VALUE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel4, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(18, 18, 18)
                .add(detailPanel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
        );

        getAccessibleContext().setAccessibleName("MyConnections");

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void invoiceTableKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_invoiceTableKeyPressed

        int kc = evt.getKeyCode();

        if (kc == evt.VK_ADD) {

            takePayment();
            return;

        }

        if (kc == evt.VK_ENTER) {

            viewInvoice();
            return;

        }

    }//GEN-LAST:event_invoiceTableKeyPressed

    private void toggleButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_toggleButtonActionPerformed

        if (detailPanel.isVisible()) {

            detailPanel.setVisible(false);
            toggleButton.setText("More Detail");
            toggleButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/datavirtue/nevitiumpro/res/Aha-16/enabled/Up.png")));

        } else {


            detailPanel.setVisible(true);
            toggleButton.setText("Less Detail");
            toggleButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/datavirtue/nevitiumpro/res/Aha-16/enabled/Down.png")));
        }

        findField.requestFocus();
    }//GEN-LAST:event_toggleButtonActionPerformed

    private void invoiceTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_invoiceTableMouseClicked



        if (evt.getClickCount() == 2) {

            if (application.checkPermissions(user, user.getInvoiceManager(), 300)) {
                return;
            }
            viewInvoice();
        }

    }//GEN-LAST:event_invoiceTableMouseClicked

    private void labelButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_labelButtonActionPerformed
        if (!application.checkPermissions(user, user.getConnections(), 300)) {
            return;
        }

        if (connTable.getSelectedRow() > -1) {


            new ConnLabelDialog(null, true, connTable.getModel(), connTable.getSelectedRows(), application);

        } else {

            javax.swing.JOptionPane.showMessageDialog(null, "Select rows from the Connections table to create labels.");

        }

    }//GEN-LAST:event_labelButtonActionPerformed

    private void wwwButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_wwwButtonActionPerformed
        /* Add protocol info if none is specified */
        if (!websiteField.getText().toUpperCase().contains("HTTP://") && !websiteField.getText().toUpperCase().contains("FTP://")) {
            websiteField.setText("http://" + websiteField.getText());
        }

        boolean desktop = DV.parseBool(props.getProp("DESKTOP SUPPORTED"), false);
        if (Desktop.isDesktopSupported() && desktop) {
            if (Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
                try {

                    Desktop.getDesktop().mail(new URI(websiteField.getText()));
                    return;
                } catch (Exception ex) {
                    //try the old manual method below
                }
            }
        }


        int a = DV.launchURL(websiteField.getText());
        if (a < 1) {
            javax.swing.JOptionPane.showMessageDialog(null, "There was a problem trying to launch a web browser." + nl + "This may not be supported by your Operating System.");
        }
        //process errors

    }//GEN-LAST:event_wwwButtonActionPerformed

    private void emailButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_emailButtonActionPerformed

        if (emailField.getText().equals("") || emailField.getText().length() < 8) {  /*REGEX*/

            javax.swing.JOptionPane.showMessageDialog(null, "You need to enter a good email address.");
            return;

        }
        boolean desktop = DV.parseBool(props.getProp("DESKTOP SUPPORTED"), false);
        if (Desktop.isDesktopSupported() && desktop) {
            if (Desktop.getDesktop().isSupported(Desktop.Action.MAIL)) {
                try {

                    Desktop.getDesktop().mail(new URI("mailto:" + emailField.getText()));
                    return;
                } catch (Exception ex) {
                    //try the old manual method below
                }
            }
        }
        int a = DV.launchURL("mailto:" + emailField.getText());
        if (a < 1) {
            javax.swing.JOptionPane.showMessageDialog(null, "There was a problem trying to launch an email application." + nl + "This may not be supported by your Operating System.");
        }



    }//GEN-LAST:event_emailButtonActionPerformed

    private void pinCodeFieldKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_pinCodeFieldKeyPressed

        if (evt.getKeyCode() == java.awt.event.KeyEvent.VK_ENTER) {

            zipAction();

        }


    }//GEN-LAST:event_pinCodeFieldKeyPressed

    private void takePayment() {

        int r = invoiceTable.getSelectedRow();
        TableModel tm = invoiceTable.getModel();

        if (r > -1) {

            if ((Boolean) tm.getValueAt(r, 2) == true) {

                javax.swing.JOptionPane.showMessageDialog(this, "Invoice is marked as paid.");
                invoiceTable.changeSelection(r, 0, false, false);
                invoiceTable.requestFocus();
                return;

            } else {

                int key = (Integer) tm.getValueAt(r, 0);

                PaymentDialog pd = new PaymentDialog(parentWin, true, key, application);
                pd.setVisible(true);
                //populateInvoices(false);
            }


            invoiceTable.changeSelection(r, 0, false, false);
            invoiceTable.requestFocus();

        }

    }

    private void findFieldFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_findFieldFocusGained

        findField.selectAll();

    }//GEN-LAST:event_findFieldFocusGained

    private void viewInvoice() {


        int row = invoiceTable.getSelectedRow();

        if (invoiceTable.getSelectedRow() > -1) {

            int key = (Integer) invoiceTable.getModel().getValueAt(invoiceTable.getSelectedRow(), 0);

            if (invoiceToggleButton.getText().endsWith("Quotes")) {

                if (key > 0) {
                    /* Opening quotes */
                    InvoiceDialog id = new InvoiceDialog(parentWin, true, application, key); //no select

                    id.setVisible(true);
                    id.dispose();

                }
            } else {
                if (key > 0) {

                    InvoiceDialog id = new InvoiceDialog(parentWin, true, key, application); //no select

                    id.setVisible(true);
                    id.dispose();

                }

            }


        }
        //this.populateInvoices(false);
        invoiceTable.changeSelection(row, 0, false, false);
        invoiceTable.requestFocus();

    }

    private void viewButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_viewButtonActionPerformed

        if (!application.checkPermissions(user, user.getInvoiceManager(), 300)) {
            return;
        }
        viewInvoice();

    }//GEN-LAST:event_viewButtonActionPerformed

    private void journalTextAreaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_journalTextAreaMouseClicked
    }//GEN-LAST:event_journalTextAreaMouseClicked

    private void journalTextAreaFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_journalTextAreaFocusLost



        if (!fileList.isEnabled()) {

            saveJournal();
            fileList.requestFocus();

        }


    }//GEN-LAST:event_journalTextAreaFocusLost

    private void journalTextAreaFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_journalTextAreaFocusGained
        fileList.setEnabled(false);
    }//GEN-LAST:event_journalTextAreaFocusGained

    private void connTableKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_connTableKeyReleased

        if (connTable.getSelectedRow() > -1) {
            populateFields();
            setFieldsEnabled(true);
            //saveButton.setEnabled(true);

        }

    }//GEN-LAST:event_connTableKeyReleased

    private void fileListMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_fileListMouseClicked

        if (!fileList.isEnabled() && companyNameField.isEnabled()) {
            saveJournal();
            fileList.requestFocus();

        }

        if (companyNameField.isEnabled()) {
            getJournal();
        }



    }//GEN-LAST:event_fileListMouseClicked

    private void newButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_newButtonActionPerformed

        if (edit_key > 0) {
            newJournal();
        }

    }//GEN-LAST:event_newButtonActionPerformed

    private void exportButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exportButtonActionPerformed

        if (connTable.getModel().getRowCount() > 0) {
            if (!application.checkPermissions(user, user.getExports(), 500)) {
                return;
            }
            String home = System.getProperty("user.home");
            if (System.getProperty("os.name").contains("Windows")) {
                home = home + '\\' + "My Documents";
            }

            com.datavirtue.nevitiumpro.Views.FileDialog fd = new com.datavirtue.nevitiumpro.Views.FileDialog(parentWin, true, home, "export.csv");

            fd.setVisible(true);

            if (!fd.getPath().equals("")) {
                export(fd.getPath());
            }
        }
    }//GEN-LAST:event_exportButtonActionPerformed

    private void findFieldKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_findFieldKeyPressed

        if (evt.getKeyCode() == java.awt.event.KeyEvent.VK_ENTER) {


            find();

        }
    }//GEN-LAST:event_findFieldKeyPressed

    private void connTableKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_connTableKeyPressed


        if (selectMode) {

            if (evt.getKeyCode() == java.awt.event.KeyEvent.VK_ENTER) {

                if (connTable.getSelectedRow() > -1 && selectMode) {
                    returnValue = (Integer) connTable.getModel().getValueAt(connTable.getSelectedRow(), 0);
                    this.setVisible(false);
                }

            }

        }


    }//GEN-LAST:event_connTableKeyPressed

    private void companyNameFieldMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_companyNameFieldMouseClicked

        if (!companyNameField.isEnabled()) {

            if (!application.checkPermissions(user, user.getConnections(), 200)) {
                return;
            }

            clearFields();
            setFieldsEnabled(true);
            this.connectionBean = new Connections();
            accountIdButton.setVisible(true);
            companyNameField.requestFocus();

        }


    }//GEN-LAST:event_companyNameFieldMouseClicked

    private void connTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_connTableMouseClicked
        int mouseButton = evt.getButton();
        if (mouseButton == evt.BUTTON2 || mouseButton == evt.BUTTON3) {
            return;
        }
        //on Double Click

        if (selectMode) {

            if (evt.getClickCount() == 2) {

                int row = connTable.rowAtPoint(new Point(evt.getX(), evt.getY()));

                if (connTable.getSelectedRow() > -1) {

                    returnValue = (Integer) connTable.getModel().getValueAt(row, 0);

                    this.setVisible(false);
                }

            }

        }

        populateFields();

    }//GEN-LAST:event_connTableMouseClicked

    private void deleteButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteButtonActionPerformed


        if (connTable.getSelectedRow() > -1) {

            if (!application.checkPermissions(user, user.getConnections(), 500)) {
                return;
            }

            int a = JOptionPane.showConfirmDialog(this, "Delete Selected Record?", "Delete", JOptionPane.YES_NO_OPTION);

            if (a == 0) {

                /*btm = (BeanTableModel) connTable.getModel();
                 localBean = (Connections) btm.getBeanAt(connTable.getSelectedRow());*/
                boolean deleted = false;
                
                try {
                    dao.transactionDelete(connectionBean);
                    deleted = true;
                } catch (Exception ex) {
                    ExceptionErrorDialog errDialog = new ExceptionErrorDialog(ex);
                    errDialog.pack();
                    errDialog.setVisible(true);
                    deleted = false;
                }

                if (deleted) {
                    clearFields();
                    setFieldsEnabled(false);
                    refreshTable();
                    JOptionPane.showMessageDialog(null, "The record was deleted.");
                } else {
                    JOptionPane.showMessageDialog(null, "The record was NOT deleted.");
                }

            }


        }
    }//GEN-LAST:event_deleteButtonActionPerformed

    private void refreshTable() {

        connTable.setModel(filter());
        setTableView();

    }

    private void clearButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_clearButtonActionPerformed
        clearFields();
        setFieldsEnabled(false);
    }//GEN-LAST:event_clearButtonActionPerformed

    private boolean checkDuplicates() {
        ArrayList al = new ArrayList();

        al = db.search("conn", 1, companyNameField.getText().trim(), false);
        if (al == null || DV.scanArrayList(al, edit_key) == edit_key); else {
            JOptionPane.showMessageDialog(this, "Company name is used in another record.", "Duplicate Data!", JOptionPane.OK_OPTION);
            return false;
        }
        al = db.search("conn", 12, emailField.getText().trim(), false);
        if (al == null || DV.scanArrayList(al, edit_key) == edit_key); else {
            JOptionPane.showMessageDialog(this, "Email address is used in another record.", "Duplicate Data!", JOptionPane.OK_OPTION);
            return false;
        }
        return true;
    }

    private void saveAction() {

        //if new record then check against database for possible dupes
        //move field data from each UI component into the bean and save
        
        
        if (!application.checkPermissions(user, user.getConnections(), 200)) {
            return;
        }

        if (customerTypeRadio.isSelected() == false && supplierCheckBox.isSelected() == false) {

            int a = JOptionPane.showConfirmDialog(this, "You did NOT select 'Customer' OR 'Supplier'.  Is this ok? ", "No Category", JOptionPane.YES_NO_OPTION);

            //System.out.println("OPTION " + a);

            if (a != 0) {
                return;
            }

        }

        if (companyNameField.getText().trim().equals("") && firstNameField.getText().trim().equals("")) {

            JOptionPane.showMessageDialog(this, "You have to provide some type of contact data; Company, First Name, or Contact.", "Form Problem!", JOptionPane.OK_OPTION);
            return;
        }

        /* if (!checkDuplicates()) {
         return;
         }*/

        connectionBean.setCompanyName(companyNameField.getText().trim());
        connectionBean.setFirstName(firstNameField.getText().trim());
        connectionBean.setLastName(sirNameField.getText().trim());

//        connectionBean.setAddress(addressField.getText().trim());
//        connectionBean.setAddress2(address2Field.getText().trim());
//        connectionBean.setCity(cityField.getText().trim());
//        connectionBean.setState(provenceField.getText().trim());
//        connectionBean.setPostCode(pinCodeField.getText().trim());

        connectionBean.setEmail(emailField.getText().trim());
        connectionBean.setWebsite(websiteField.getText().trim());
        connectionBean.setNote(notesTextArea.getText().trim());
        connectionBean.setCustomer(customerTypeRadio.isSelected());
        connectionBean.setSupplier(supplierCheckBox.isSelected());
        connectionBean.setTax1(tax1CheckBox.isSelected());
        connectionBean.setTax2(tax2CheckBox.isSelected());

        try {
            dao.transactionSave(connectionBean);
        } catch (Exception ex) {
            ExceptionErrorDialog errDialog = new ExceptionErrorDialog(ex);
            errDialog.pack();
            errDialog.setVisible(true);
        }
        
        clearFields();
        setFieldsEnabled(false);

        saveButton.setEnabled(false);

        allRadio.setSelected(true);

        refreshTable();

        /*  select  */

        /*int row = DV.searchTable(connTable.getModel(), 0, zx);

         if (row > connTable.getModel().getRowCount()); else {

         connTable.changeSelection(row, 0, false, false);

         }*/


    }

    private void saveButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveButtonActionPerformed

        saveAction();

    }//GEN-LAST:event_saveButtonActionPerformed

    private void importCountries() {

        JFileChooser fileChooser = DV.getFileChooser("c:/1Data/Data Virtue/Research/International/");

        File f = (File)fileChooser.getSelectedFile();

        int[] r = {0, 1, 2, 3, 4, 5};
        db.csvImport("countries", f, false, r, false);

    }

    private void notesTextAreaFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_notesTextAreaFocusGained
        messageField.setText("The Misc field can be used to store any information. (Searchable)");
    }//GEN-LAST:event_notesTextAreaFocusGained

    private void notesTextAreaFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_notesTextAreaFocusLost
        messageField.setText("Remember to click 'Save' when you modify a record.");
    }//GEN-LAST:event_notesTextAreaFocusLost

    private void invoiceToggleButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_invoiceToggleButtonActionPerformed
        //populateInvoices(true);
    }//GEN-LAST:event_invoiceToggleButtonActionPerformed

    private void purchaseHistoryButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_purchaseHistoryButtonActionPerformed
        doPurchaseReport();
    }//GEN-LAST:event_purchaseHistoryButtonActionPerformed

    private void invoiceReportButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_invoiceReportButtonActionPerformed

        doInvoiceReport();

    }//GEN-LAST:event_invoiceReportButtonActionPerformed

    private void jLabel9MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel9MouseClicked
        find();
    }//GEN-LAST:event_jLabel9MouseClicked

    private void allRadioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_allRadioActionPerformed
        refreshTable();
        clearFields();
        this.setFieldsEnabled(false);
    }//GEN-LAST:event_allRadioActionPerformed

    private void custRadioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_custRadioActionPerformed
        refreshTable();
        clearFields();
        this.setFieldsEnabled(false);
    }//GEN-LAST:event_custRadioActionPerformed

    private void suppRadioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_suppRadioActionPerformed
        refreshTable();
        clearFields();
        this.setFieldsEnabled(false);
    }//GEN-LAST:event_suppRadioActionPerformed

    private void unpaidRadioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_unpaidRadioActionPerformed
        refreshTable();
        clearFields();
        this.setFieldsEnabled(false);
    }//GEN-LAST:event_unpaidRadioActionPerformed

    private void selectButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_selectButtonActionPerformed
        if (connTable.getSelectedRow() < 0) {
            connectionBean = null;
        }
        this.setVisible(false);
    }//GEN-LAST:event_selectButtonActionPerformed

    private void cancelButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelButtonActionPerformed
        connectionBean = null;
        this.setVisible(false);
    }//GEN-LAST:event_cancelButtonActionPerformed

    private void viewImageButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_viewImageButtonActionPerformed
    }//GEN-LAST:event_viewImageButtonActionPerformed

    private void longitudeFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_longitudeFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_longitudeFieldActionPerformed

    private void employeeRadioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_employeeRadioActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_employeeRadioActionPerformed

    private void supplierCheckBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_supplierCheckBoxActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_supplierCheckBoxActionPerformed

    private void addressDialogButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addressDialogButtonActionPerformed
        ConnectionsAddressDialog addressDialog = new ConnectionsAddressDialog(null, true, application, connectionBean, false, false);
        addressDialog.setVisible(true);
    }//GEN-LAST:event_addressDialogButtonActionPerformed

    private void customerTypeRadioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_customerTypeRadioActionPerformed
        employeeCustomerLabel.setText("Customer ID");
        employeeCustomerIdField.setText(connectionBean.getCustomerId());
        if (employeeCustomerIdField.getText().isEmpty()) {
            accountIdButton.setVisible(true);
        } else {
            accountIdButton.setVisible(false);
        }
        payrollButton.setVisible(false);
    }//GEN-LAST:event_customerTypeRadioActionPerformed

    private void employeeTypeRadioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_employeeTypeRadioActionPerformed
        employeeCustomerLabel.setText("Employee ID");
        employeeCustomerIdField.setText(connectionBean.getEmployeeId());
        if (employeeCustomerIdField.getText().isEmpty()) {
            accountIdButton.setVisible(true);
        } else {
            accountIdButton.setVisible(false);
        }
        payrollButton.setVisible(true);
    }//GEN-LAST:event_employeeTypeRadioActionPerformed

    private void defaultAddressButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_defaultAddressButtonActionPerformed
        ConnectionsAddressDialog addressDialog = new ConnectionsAddressDialog(null, true, application, connectionBean, false, true);
        addressDialog.setVisible(true);
    }//GEN-LAST:event_defaultAddressButtonActionPerformed

    private void accountIdButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_accountIdButtonActionPerformed
        Class noparam[] = {};
        //String parameter
	Class[] params = new Class[1];	
	params[0] = AppSettings.class;
        Method getId;
        try {
            Class IdGeneratorClass = ClassFactory.loadClass("plugins/", "IdGenerator");
            Object IdObject = IdGeneratorClass.newInstance();
            getId = IdGeneratorClass.getDeclaredMethod("getId", params);
            String s = (String)getId.invoke(IdObject, application);
            employeeCustomerIdField.setText(s);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        
    }//GEN-LAST:event_accountIdButtonActionPerformed

    private void payrollButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_payrollButtonActionPerformed
        File file = new File("NevitiumPro.jar/com/datavirtue/nevitiumpro/plugins/CustomerIDGenerator.class");
        File newclass = new File("C:/1DATA/CustomerIDGenerator.class");
        //newclass.archiveCopyTo(file);
        file.archiveCopyFrom(newclass);
        try {
            file.umount(file);
        } catch (ArchiveException ex) {
            ex.printStackTrace();
        }
    }//GEN-LAST:event_payrollButtonActionPerformed

    private void doInvoiceReport() {
        int r = connTable.getSelectedRow();

        if (r < 0) {
            return;
        }
        if (!application.checkPermissions(user, user.getReports(), 500)) {
            return;
        }

        int k = (Integer) connTable.getModel().getValueAt(r, 0);

        if (k > 0) {
            //ReportFactory.generateCustomerStatement(application, k);
        }

    }

    private void doPurchaseReport() {

        if (!application.checkPermissions(user, user.getReports(), 500)) {
            return;
        }


        int row = connTable.getSelectedRow();

        if (row < 0) {
            return;
        }

        int k = edit_key;


        java.util.ArrayList al = db.search("invoice", 11, Integer.toString(k), false);

        if (al == null || al.size() < 1) {

            javax.swing.JOptionPane.showMessageDialog(null,
                    "No invoices found for this contact.");
            return;
        }


        k = (Integer) connTable.getModel().getValueAt(row, 0);
        PurchaseHistoryReport phr = new PurchaseHistoryReport(application);
        phr.SetTitle("Customer Purchase History Report");
        phr.setCustomer(k);
        phr.buildReport();
        new ReportTableDialog(parentWin, true, phr, props);

    }

    private void getCountry() {

        int[] r = {0};

        TableView tv = new TableView(parentWin, true, db, "countries", 0,
                "Select a country from the table.", r);

        tv.dispose();

    }
    Settings props;
    private DbEngine db = null;
    MyConnectionsDAO connDao = null;
    private int returnValue = -1;
    private boolean selectMode = false;
    //private Object [] dataOut = new Object [20];
    private int edit_key = 0;
    private int[] vals = {0, 15, 14, 13, 12, 11, 10, 3, 3, 3, 3, 3, 6, 6}; //col view removal
    private java.awt.Frame parentWin;
    private javax.swing.DefaultListModel lm = new javax.swing.DefaultListModel();
    private DbEngine zip;
    private String nl = System.getProperty("line.separator");
    private Image winIcon;
    private boolean small = false;
    private int searchColumn = 1;
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton accountIdButton;
    private javax.swing.JTextField address2Field;
    private javax.swing.JButton addressDialogButton;
    private javax.swing.JTextField addressField;
    private javax.swing.JPanel addressPanel;
    private javax.swing.JToggleButton allRadio;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.ButtonGroup buttonGroup2;
    private javax.swing.JButton cancelButton;
    private javax.swing.JTextField cityField;
    private javax.swing.JButton clearButton;
    private javax.swing.JTextField companyNameField;
    private javax.swing.JTable connTable;
    private javax.swing.JPanel contactPanel;
    private javax.swing.JComboBox countryCombo;
    private javax.swing.JToggleButton custRadio;
    private javax.swing.ButtonGroup customerEmployeeGroup;
    private javax.swing.JRadioButton customerTypeRadio;
    private javax.swing.JButton defaultAddressButton;
    private javax.swing.JButton deleteButton;
    private javax.swing.JPanel detailPanel;
    private javax.swing.JButton emailButton;
    private javax.swing.JTextField emailField;
    private javax.swing.JTextField employeeCustomerIdField;
    private javax.swing.JLabel employeeCustomerLabel;
    private javax.swing.JToggleButton employeeRadio;
    private javax.swing.JRadioButton employeeTypeRadio;
    private javax.swing.JButton exportButton;
    private javax.swing.JList fileList;
    private javax.swing.JToolBar filterToolbar;
    private javax.swing.JTextField findField;
    private javax.swing.JTextField firstNameField;
    private javax.swing.JToolBar functionToolbar;
    private javax.swing.JPanel idPanel;
    private javax.swing.JPanel imagePanel;
    private com.michaelbaranov.microba.calendar.DatePicker inceptionDateField;
    private javax.swing.JLabel invoiceLabel;
    private javax.swing.JButton invoiceReportButton;
    private javax.swing.JTable invoiceTable;
    private javax.swing.JButton invoiceToggleButton;
    private javax.swing.JToolBar invoiceToolbar;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton7;
    private javax.swing.JButton jButton8;
    private javax.swing.JComboBox jComboBox1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JToolBar.Separator jSeparator1;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JToolBar jToolBar1;
    private javax.swing.JToolBar jToolBar2;
    private javax.swing.JToolBar jToolBar3;
    private javax.swing.JToolBar jToolBar4;
    private javax.swing.JToolBar jToolBar5;
    private javax.swing.JPanel journalPanel;
    private javax.swing.JTextPane journalTextArea;
    private javax.swing.JButton labelButton;
    private javax.swing.JTextField latitudeField;
    private javax.swing.JTextField loginNameField;
    private javax.swing.JTextField longitudeField;
    private javax.swing.JButton mapButton;
    private javax.swing.JTextField messageField;
    private javax.swing.JButton newButton;
    private javax.swing.JTextField notesTextArea;
    private javax.swing.JButton passwordButton;
    private javax.swing.JButton payrollButton;
    private javax.swing.JTextField phoneField;
    private javax.swing.JTable phoneTable;
    private javax.swing.JTextField pinCodeField;
    private javax.swing.JTextField provenceField;
    private javax.swing.JButton purchaseHistoryButton;
    private javax.swing.JButton saveButton;
    private javax.swing.JComboBox searchFieldCombo;
    private javax.swing.JButton selectButton;
    private javax.swing.JTextField sirNameField;
    private javax.swing.JToggleButton suppRadio;
    private javax.swing.JCheckBox supplierCheckBox;
    private javax.swing.JButton supplierIDButton;
    private javax.swing.JTextField supplierIdField;
    private javax.swing.JCheckBox tax1CheckBox;
    private javax.swing.JCheckBox tax2CheckBox;
    private javax.swing.JButton toggleButton;
    private javax.swing.JToggleButton unpaidRadio;
    private javax.swing.JButton viewButton;
    private javax.swing.JButton viewImageButton;
    private javax.swing.JTextField websiteField;
    private javax.swing.JButton wwwButton;
    // End of variables declaration//GEN-END:variables
}
