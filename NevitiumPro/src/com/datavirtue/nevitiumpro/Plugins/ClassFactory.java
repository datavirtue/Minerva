/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.datavirtue.nevitiumpro.Plugins;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

/**
 *
 * @author Administrator
 */
public class ClassFactory {

    public static Class loadClass(String folder, String clazz) throws MalformedURLException, ClassNotFoundException {
        File file = new File(folder + clazz + ".class");
        URL url = file.toURI().toURL();
        URL[] urls = new URL[]{url};
        ClassLoader cl = new URLClassLoader(urls);
        return cl.loadClass(clazz);        
    }
}
