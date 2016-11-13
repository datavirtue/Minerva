/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.datavirtue.nevitiumpro.Validation;

import java.awt.Color;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.InputVerifier;
import javax.swing.JComponent;
import javax.swing.JTextField;

/**
 *
 * @author Administrator
 */
public class TextFieldRegExVerifier extends InputVerifier {
         
    private String regex;
    private String message;
    boolean required = false;
    boolean yieldFocus = false;
    private String oldToolTip="";
    private Color background;
    private Color warningColor = Color.PINK;
    
    public TextFieldRegExVerifier(String regex, String message, boolean required, boolean yieldFocus){
        this.regex = regex;
        this.message = message;
        this.required = required;
        this.yieldFocus = yieldFocus;
    }
    @Override
    public boolean verify(JComponent input) {
             JTextField tf = (JTextField) input;
             String text = tf.getText();
             
             Pattern p = Pattern.compile(regex);
             Matcher m = p.matcher(text);           
             if ((required && text.trim().length() == 0) || !m.matches()){
                 //Logger.getGlobal().info("Input verification activated.");
                 oldToolTip = tf.getToolTipText();
                 tf.setToolTipText(message);
                 background = tf.getBackground();
                 tf.setBackground(warningColor);
                 return false;
             }else{
                 if(oldToolTip.length() > 0) {
                     tf.setToolTipText(oldToolTip);
                 }
                 if (background != null) {
                     tf.setBackground(background);
                 }
                 return true;
             }             
         }
    
    @Override
    public boolean shouldYieldFocus(JComponent jc){
        return yieldFocus;
    }
    
    public void setWarningColor(Color c){
        this.warningColor = c;
    }
    
}
