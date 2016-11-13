/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.datavirtue.nevitiumpro.Views;

import com.jidesoft.dialog.ButtonPanel;
import com.jidesoft.plaf.UIDefaultsLookup;
import com.jidesoft.swing.MultilineLabel;
import com.l2fprod.common.swing.BannerPanel;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.border.BevelBorder;

/**
 *
 * @author Administrator
 */
public class ExceptionErrorDialog extends com.jidesoft.dialog.StandardDialog{
    
    Exception error; 
    public ExceptionErrorDialog(Exception e){
        error = e;    
    }
        
    @Override
    public JComponent createBannerPanel() {
        BannerPanel headerPanel1 = new BannerPanel();
        headerPanel1.setTitle("Nevitium Error!");
        headerPanel1.setSubtitle("A problem has ocurred that could cause erratic behavior.");        
        //headerPanel1.setIcon(new ImageIcon(getClass().getResource("/com/datavirtue/nevitiumpro/res/Aha-48/problem.png")));
        headerPanel1.setFont(new Font("Tahoma", Font.PLAIN, 11));
        headerPanel1.setBackground(Color.WHITE);
        headerPanel1.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
        return headerPanel1;
    }

    @Override
    public JComponent createContentPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 0, 10));
        JTextArea textArea = new MultilineLabel();
        textArea.setColumns(50);
        textArea.setRows(20);
        textArea.setText("If you send this message to support they can fix the problem and issue an update.\nYou should probably restart Nevitium after sending the report.\n(The following information is all that will be sent)\n\n" 
                + error.getMessage() +
                "\n\n"+error.getClass().getSimpleName()+"\n\n"+distillStackTrace());
        panel.add(new JScrollPane(textArea), BorderLayout.CENTER);
        //setInitFocusedComponent(textField);
        return panel;
    }

    private String distillStackTrace(){
        StringBuilder sb = new StringBuilder();
        int count = 0;
        for(StackTraceElement stack : error.getStackTrace()){
            sb.append(stack.toString());
            sb.append("\n");
            count++;
            if (count == 20){
                return sb.toString();
            }
        }
        return sb.toString();
    }
    
    @Override
    public ButtonPanel createButtonPanel() {
        ButtonPanel buttonPanel = new ButtonPanel(SwingConstants.CENTER);
        JButton okButton = new JButton();
        JButton cancelButton = new JButton();
        okButton.setName(OK);
        cancelButton.setName(CANCEL);
        buttonPanel.addButton(okButton, ButtonPanel.AFFIRMATIVE_BUTTON);
        buttonPanel.addButton(cancelButton, ButtonPanel.CANCEL_BUTTON);

        okButton.setAction(new AbstractAction("Send Report") {
            public void actionPerformed(ActionEvent e) {
                setDialogResult(RESULT_AFFIRMED);
                setVisible(false);
                dispose();
            }
        });
        cancelButton.setAction(new AbstractAction(UIDefaultsLookup.getString("OptionPane.cancelButtonText")) {
            public void actionPerformed(ActionEvent e) {
                setDialogResult(RESULT_CANCELLED);
                setVisible(false);
                dispose();
            }
        });
        setDefaultCancelAction(cancelButton.getAction());
        setDefaultAction(okButton.getAction());
        getRootPane().setDefaultButton(okButton);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        return buttonPanel;
    }    
    
    
}
