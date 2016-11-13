/*
 * ReportFactory.java
 *
 * Created on November 26, 2006, 11:52 PM
 *
 * * Copyright (c) Data Virtue 2006
 */

package com.datavirtue.nevitiumpro.Reports;
import com.datavirtue.nevitiumpro.Legacy.RuntimeManagement.AppSettings;

import com.datavirtue.nevitiumpro.Util.NewEmail;
import com.datavirtue.nevitiumpro.Util.Tools;
import com.datavirtue.nevitiumpro.InvoiceQuote.PDFInvoice;
import com.datavirtue.nevitiumpro.InvoiceQuote.InvoiceModel;
import com.datavirtue.nevitiumpro.Legacy.datavirtue.DV;
import com.datavirtue.nevitiumpro.Legacy.datavirtue.DbEngine;
import com.datavirtue.nevitiumpro.Legacy.datavirtue.Settings;
import com.datavirtue.nevitiumpro.Legacy.datavirtue.TableSorter;

import java.util.*;
import java.text.DateFormat;
import com.lowagie.text.BadElementException;
import java.net.MalformedURLException;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Rectangle;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.Font;
import com.lowagie.text.Image;
import java.awt.Desktop;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.File;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import javax.swing.table.*;

/**
 *
 * @author  Sean K Anderson - Data Virtue
 * @rights Copyright Data Virtue 2006, 2007 All Rights Reserved.
 */

public class ReportFactory {
    
    /** Creates a new instance of ReportFactory */
    public ReportFactory() {
        
    }
         
 public static String veiwPDF (String file, AppSettings application) {
     
     String acro = application.getAppUserSettings().getPdfReaderPath();
     boolean desktop = application.getAppUserSettings().getUseOsDefaults();
     if (Desktop.isDesktopSupported() && desktop){
         if (Desktop.getDesktop().isSupported(Desktop.Action.OPEN)){
                try {
                    File f = new File(file);
                    Desktop.getDesktop().open(f);
                    return "";
                } catch (IOException ex) {
                  javax.swing.JOptionPane.showMessageDialog(null,
                          "There was a problem trying to view the pdf with the default viewer, Nevitium will now try to use the viewer found in Settings->Output. ");

                }
             
         }
     }

     /* Old Slow Method of launching PDFs (Before JDK 6)*/
    String pdfLocation = "";
    String a="";
    String nl = System.getProperty("line.separator");
    String osName = System.getProperty("os.name").toLowerCase();
    boolean mac = osName.contains("mac");
    boolean unix = osName.contains("nix");
    boolean linux = osName.contains("nux");
    boolean windows = osName.contains("windows");

    boolean debug = false;
   
    
    if (debug) System.out.println("ReportFactory:viewPDF:osName="+osName);

    try {
        if(windows){
            pdfLocation = '"' + acro + '"'+ " "+'"' + file.replace('/','\\')+'"';
            if (debug) System.out.println(pdfLocation);
            Runtime.getRuntime().exec(pdfLocation);
        }
        if (linux || unix || mac){
            File wd = new File("/bin");
            Process proc = null;

            proc = Runtime.getRuntime().exec("bash", null, wd);

            if (proc != null) {
                BufferedReader in = new BufferedReader(new InputStreamReader(proc.getInputStream()));
                PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(proc.getOutputStream())), true);
                out.println(acro + " " +'"'+ file + '"');
                out.println("exit");
                try {
                    String line;
                    while ((line = in.readLine()) != null) {
                        System.out.println(line);
                    }
                    proc.waitFor();
                    in.close();
                    out.close();
                    proc.destroy();
                }catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

       
    } catch (IOException ex) {
        a = "error: There was a problem launching your PDF reader." + nl +
        "Verify 'PDF Reader' in the Output tab. (File Menu --->Settings)";
    }
    return a;
 }   
    
  public static void windowsFastPrint (String file, AppSettings application){

      boolean desktop = application.getAppUserSettings().getUseOsDefaults();
     if (Desktop.isDesktopSupported() && desktop){
         if (Desktop.getDesktop().isSupported(Desktop.Action.PRINT)){
                try {
                    Desktop.getDesktop().print(new File(file));
                    return;
                } catch (IOException ex) {
                  //try the old manual method below
                }

         }
     }else {
          javax.swing.JOptionPane.showMessageDialog(null,
                  "Your OS is not reporting a default application () for printing this type of file: "+file);
     }


      
  } 
  
}
