/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.datavirtue.nevitiumpro.hibernate;

import java.awt.Canvas;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

/**
 *
 * @author Administrator
 */

/* Standard, application-wide data conversion and formatting class. */
public class DataUtil {

    public static BigInteger convertToBigInt(Long l) {
        return BigInteger.valueOf(l);
    }

    public static Double convertToDouble(Float f) {
        return Double.valueOf(Double.parseDouble(Float.toString(f)));
    }

    public static BigDecimal convertToBigDec(Float f) {
        return BigDecimal.valueOf(convertToDouble(f));
    }

    public static Double convertToDouble(Integer i) {
        return Double.valueOf(i.toString());
    }

    public static String money(BigDecimal money) {
        //Locale locale = Locale.getDefault();        
        money = BigDecimal.valueOf((Math.round(money.doubleValue() * 100.00) / 100.00));
        NumberFormat formatter = new DecimalFormat("#,###,##0.00");
        return formatter.format(money);
    }

    public static String shortDate(BigInteger date) {
        return DateFormat.getDateInstance(DateFormat.SHORT).format(date);
    }

    public static boolean isValidDoubleString(String s) {
        try {
            Double.parseDouble(s);
            return true;
        } catch (NumberFormatException ex) {
            return false;
        }
    }
    
       public static byte[] convertImageIconToByteArray(ImageIcon image) throws IOException{
        /* Geesh!  Convert a ImageIcon to a byte stream, WTF!? */
        Image img = image.getImage();
        BufferedImage bi = new BufferedImage(image.getIconWidth(), image.getIconHeight(), BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = bi.createGraphics();
        g2d.drawImage(img, 0, 0, new Canvas());        
        ByteArrayOutputStream baos=new ByteArrayOutputStream();
        ImageIO.write(bi, "jpg", baos );
        return baos.toByteArray();        
    }
    
    public static byte[] readFileToByteArray(String f) throws FileNotFoundException, IOException {
        File file = new File(f);
        byte [] fileData = new byte[(int)file.length()];
        DataInputStream dis;
       
        dis = new DataInputStream((new FileInputStream(file)));
       
        
        dis.readFully(fileData);
        
        dis.close();
        return fileData;
    }
    
    public static void writeBytesToFile(byte[] b, String f) throws FileNotFoundException, IOException{
        FileOutputStream out = new FileOutputStream(f);  
        try {  
            out.write(b);  
        } finally {  
            out.close();  
        }              
    }
    
    
}
