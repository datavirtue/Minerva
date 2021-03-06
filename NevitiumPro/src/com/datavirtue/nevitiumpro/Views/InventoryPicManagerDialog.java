/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.datavirtue.nevitiumpro.Views;

import com.datavirtue.nevitiumpro.Views.ExceptionErrorDialog;
import com.datavirtue.nevitiumpro.Legacy.datavirtue.DV;
import com.datavirtue.nevitiumpro.entities.InventoryImages;
import com.datavirtue.nevitiumpro.hibernate.DataUtil;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.Date;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ActionMap;
import javax.swing.ImageIcon;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.KeyStroke;

/**
 *
 * @author Administrator
 */
public class InventoryPicManagerDialog extends javax.swing.JDialog {

    private InventoryImages imageData;
    private Image image;
    private boolean saved = false;
    private java.awt.Image winIcon;

    /**
     * Creates new form PicManagerDialog
     */
    public InventoryPicManagerDialog(java.awt.Frame parent, boolean modal, InventoryImages img) {
        super(parent, modal);
        initComponents();

        //BEGIN SCREEN SETTINGS
        Toolkit tools = Toolkit.getDefaultToolkit();
        winIcon = tools.getImage(getClass().getResource("/com/datavirtue/nevitiumpro/res/Orange.png"));
        this.setIconImage(winIcon);

        java.awt.Dimension d = DV.computeCenter((java.awt.Window) this);
        this.setLocation(d.width, d.height);
        //END SCREEN SETTINGS


        if (img == null) {
            imageData = new InventoryImages();

        } else {
            this.imageData = img;
            try {
                urlField.setText(img.getImageUrl());
                titleField.setText(img.getTitle());
                imageFormatField.setText(img.getImageFormat());
                datePicker.setDate(new Date(img.getDate().longValue()));
                refresh();

            } catch (Exception ex) {
                ExceptionErrorDialog dialog = new ExceptionErrorDialog(ex);
                dialog.pack();
                dialog.setVisible(true);
            }
        }


        this.addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent e) {
                //recordScreenPosition();
                //props.setProp("INVENTORY SEARCH", Integer.toString(searchFieldCombo.getSelectedIndex()));
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
            }
        };
        im.put(windowCloseStroke, windowCloseKey);
        am.put(windowCloseKey, windowCloseAction);


    }

    private void refresh() throws Exception {
        //image = (BufferedImage)Toolkit.getDefaultToolkit().createImage(imageData.getBitmap());

        try {
            image = imageData.getBitmap().getImage();
        } catch (Exception ex) {
            ExceptionErrorDialog errDialog = new ExceptionErrorDialog(ex);
            errDialog.pack();
            errDialog.setVisible(true);
        }
        int x = image.getWidth(null);
        int y = image.getHeight(null);
        if (!fitCheckBox.isSelected()) {
            statusLabel.setText("Real Size (pixels): " + x + "x" + y + "  (inches): " + (x / 72) + "x" + (y / 72));
        }

        ImageIcon i = fitImage();
        picLabel.setIcon(i);

    }

    private ImageIcon fitImage() {

        /*resize image to scrollpane viewport size */
        /* add image to picLabel */

        if (imagePanel.getSize().getHeight() >= image.getHeight(null) && fitCheckBox.isSelected() != true) {
            return new ImageIcon(image);
        } else {

            if (fitCheckBox.isSelected()) {
                return new ImageIcon(image.getScaledInstance(-1, (int) imagePanel.getSize().getHeight(), Image.SCALE_SMOOTH));
            } else {
                return new ImageIcon(image);
            }

        }
    }

    public InventoryImages getInventoryImageObject() {
        if (!saved) {
            return null;
        } else {
            return imageData;
        }

    }

    public boolean isSaved() {
        return saved;
    }

    /**
     * This method is called from within the constructor to initialize the form. WARNING: Do NOT modify this code. The
     * content of this method is always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        imagePanel = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        picLabel = new javax.swing.JLabel();
        controlsPanel = new javax.swing.JPanel();
        browsePhotoButton = new javax.swing.JButton();
        fitCheckBox = new javax.swing.JCheckBox();
        dataPanel = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        titleField = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        urlField = new javax.swing.JTextField();
        datePicker = new com.michaelbaranov.microba.calendar.DatePicker();
        imageFormatField = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        statusBar = new javax.swing.JToolBar();
        statusLabel = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        saveButton = new javax.swing.JButton();
        cancelButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        imagePanel.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        picLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        picLabel.setPreferredSize(new java.awt.Dimension(400, 300));
        jScrollPane1.setViewportView(picLabel);

        javax.swing.GroupLayout imagePanelLayout = new javax.swing.GroupLayout(imagePanel);
        imagePanel.setLayout(imagePanelLayout);
        imagePanelLayout.setHorizontalGroup(
            imagePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
        );
        imagePanelLayout.setVerticalGroup(
            imagePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1)
        );

        controlsPanel.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        browsePhotoButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/datavirtue/nevitiumpro/res/Aha-16/enabled/Folder tree.png"))); // NOI18N
        browsePhotoButton.setText("Browse");
        browsePhotoButton.setFocusable(false);
        browsePhotoButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        browsePhotoButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        browsePhotoButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                browsePhotoButtonActionPerformed(evt);
            }
        });

        fitCheckBox.setText("Fit Image");
        fitCheckBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                fitCheckBoxActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout controlsPanelLayout = new javax.swing.GroupLayout(controlsPanel);
        controlsPanel.setLayout(controlsPanelLayout);
        controlsPanelLayout.setHorizontalGroup(
            controlsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(controlsPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(controlsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(browsePhotoButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(fitCheckBox, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        controlsPanelLayout.setVerticalGroup(
            controlsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(controlsPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(browsePhotoButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(fitCheckBox)
                .addContainerGap())
        );

        dataPanel.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel1.setText("Title");

        jLabel2.setText("URL");

        jLabel3.setText("Format");

        jLabel4.setText("Date");

        javax.swing.GroupLayout dataPanelLayout = new javax.swing.GroupLayout(dataPanel);
        dataPanel.setLayout(dataPanelLayout);
        dataPanelLayout.setHorizontalGroup(
            dataPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(dataPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(dataPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, 61, Short.MAX_VALUE)
                    .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(dataPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(dataPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(titleField, javax.swing.GroupLayout.DEFAULT_SIZE, 312, Short.MAX_VALUE)
                        .addComponent(urlField))
                    .addComponent(datePicker, javax.swing.GroupLayout.PREFERRED_SIZE, 163, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(imageFormatField, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        dataPanelLayout.setVerticalGroup(
            dataPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(dataPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(dataPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(titleField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(dataPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(urlField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(dataPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(imageFormatField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(dataPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(datePicker, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        statusBar.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED));
        statusBar.setFloatable(false);
        statusBar.setRollover(true);
        statusBar.add(statusLabel);

        jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        saveButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/datavirtue/nevitiumpro/res/Aha-16/enabled/Floppy.png"))); // NOI18N
        saveButton.setText("Save");
        saveButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveButtonActionPerformed(evt);
            }
        });

        cancelButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/datavirtue/nevitiumpro/res/Aha-16/enabled/No.png"))); // NOI18N
        cancelButton.setText("Cancel");
        cancelButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(saveButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(cancelButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(cancelButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(saveButton)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(statusBar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(imagePanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(dataPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(controlsPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(imagePanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(controlsPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(dataPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(statusBar, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void browsePhotoButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_browsePhotoButtonActionPerformed
        JFileChooser fc = new JFileChooser();
        fc.showOpenDialog(this);
        String f = fc.getSelectedFile().toString();
        String title = fc.getSelectedFile().getName();
        try {
            imageFormatField.setText(f.substring(f.length() - 3));
            titleField.setText(title.substring(0, title.length() - 4));
            urlField.setText(f);
            imageData.setBitmap(DataUtil.readFileToByteArray(f));

            refresh();
        } catch (Exception ex) {
            ExceptionErrorDialog d = new ExceptionErrorDialog(ex);
            d.pack();
            d.setVisible(true);
        }

    }//GEN-LAST:event_browsePhotoButtonActionPerformed

    private void fitCheckBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_fitCheckBoxActionPerformed
        try {
            refresh();
        } catch (Exception ex) {
            ExceptionErrorDialog d = new ExceptionErrorDialog(ex);
            d.pack();
            d.setVisible(true);
        }
    }//GEN-LAST:event_fitCheckBoxActionPerformed

    private void saveButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveButtonActionPerformed
        imageData.setDate(DataUtil.convertToBigInt(datePicker.getDate().getTime()));
        imageData.setImageFormat(imageFormatField.getText());
        imageData.setImageUrl(urlField.getText());
        imageData.setTitle(titleField.getText());
        saved = true;
        this.setVisible(false);

    }//GEN-LAST:event_saveButtonActionPerformed

    private void cancelButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelButtonActionPerformed
        imageData = null;
        saved = false;
        this.setVisible(false);


    }//GEN-LAST:event_cancelButtonActionPerformed
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton browsePhotoButton;
    private javax.swing.JButton cancelButton;
    private javax.swing.JPanel controlsPanel;
    private javax.swing.JPanel dataPanel;
    private com.michaelbaranov.microba.calendar.DatePicker datePicker;
    private javax.swing.JCheckBox fitCheckBox;
    private javax.swing.JTextField imageFormatField;
    private javax.swing.JPanel imagePanel;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel picLabel;
    private javax.swing.JButton saveButton;
    private javax.swing.JToolBar statusBar;
    private javax.swing.JLabel statusLabel;
    private javax.swing.JTextField titleField;
    private javax.swing.JTextField urlField;
    // End of variables declaration//GEN-END:variables
}
