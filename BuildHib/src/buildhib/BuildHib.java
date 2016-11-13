/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package buildhib;

import java.io.File;
import java.io.FileFilter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 *
 * @author Administrator
 */
public class BuildHib {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        if (args.length < 4){
            System.out.println("USEAGE: java -jar buildhib <package> <class_package> <[.class][.java]_folder> <destination_filename>");
        }
    
        // destination_package_name target_folder output_filename
        String mainPackage = args[0];
        String classPackage = args[1];
        if (!classPackage.endsWith(".")) classPackage = classPackage+'.';
        String targetFolder = args[2]; 
        String destinationFile = args[3];
        String nl = System.lineSeparator();
        File target = new File(targetFolder);
        File [] files = target.listFiles();
        File basket;
        String classname;
        StringBuilder sb = new StringBuilder();
        
        sb.append("<?xml version='1.0' encoding='utf-8'?>"+nl);
        sb.append("<!DOCTYPE hibernate-configuration PUBLIC \"-//Hibernate/Hibernate Configuration DTD//EN\" \"http://hibernate.sourceforge.net/hibernate-configuration-3.0.dtd\">"+nl);
        sb.append("<hibernate-configuration>"+nl);    
        sb.append("<session-factory>"+nl);
        sb.append("    <property name=\"hibernate.id.new_generator_mappings\">true</property>"+nl);
        sb.append("    <mapping package=\""+mainPackage+"\"/>"+nl); 
        
        for (int i = 0; i < files.length; i++){
            
            basket = files[i];
            classname = basket.getName();
            if (classname.contains(".java")) {
                classname = classname.replaceFirst(".java", "");
            }else if(classname.contains(".class")) {
                classname = classname.replaceFirst(".class", "");
            }else continue;
            sb.append("    <mapping class=\""+classPackage+classname+"\"/>"+nl);
        }
        sb.append("</session-factory>"+nl);
        sb.append("</hibernate-configuration>"+nl);    
        
        System.out.println(sb.toString());
        
        try {
        FileWriter outFile = new FileWriter(destinationFile);
        PrintWriter out = new PrintWriter(outFile);
        out.print(sb.toString());  
        out.close();
        } catch (IOException e){
           e.printStackTrace();
        }
        
    }
}
