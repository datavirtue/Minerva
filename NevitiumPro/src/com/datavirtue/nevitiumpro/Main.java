/*
 * Main.java
 *
 * Created on June 22, 2006, 9:44 AM
 *
 * This application will contain various modules (JDialogs) that operate
 *on the various databases allowing modification and providing consolidated
 *veiws of the data to help manage a service or retail oriented business
 ** Copyright (c) Data Virtue 2006
 *
 */
package com.datavirtue.nevitiumpro;

import com.datavirtue.nevitiumpro.Views.AccessDialog;
import com.datavirtue.nevitiumpro.Views.ExceptionErrorDialog;
import com.datavirtue.nevitiumpro.Legacy.datavirtue.DV;
import com.jtattoo.plaf.acryl.AcrylLookAndFeel;
import com.jtattoo.plaf.aero.AeroLookAndFeel;
import com.jtattoo.plaf.aluminium.AluminiumLookAndFeel;
import com.jtattoo.plaf.bernstein.BernsteinLookAndFeel;
import com.jtattoo.plaf.fast.FastLookAndFeel;
import com.jtattoo.plaf.graphite.GraphiteLookAndFeel;
import com.jtattoo.plaf.hifi.HiFiLookAndFeel;
import com.jtattoo.plaf.luna.LunaLookAndFeel;
import com.jtattoo.plaf.mcwin.McWinLookAndFeel;
import com.jtattoo.plaf.mint.MintLookAndFeel;
import com.jtattoo.plaf.noire.NoireLookAndFeel;
import com.jtattoo.plaf.smart.SmartLookAndFeel;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.Constants;
import org.osgi.framework.launch.Framework;
import org.osgi.framework.launch.FrameworkFactory;

/**
 *
 * @author Sean K Anderson - Data Virtue
 * @rights Copyright Data Virtue 2006, 2007 All Rights Reserved.
 */
public class Main {

    /**
     * Creates a new instance of Main
     */
    public Main() {
    }

    public static void main(String args[]) {

        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {

                    String os = System.getProperty("os.name").toLowerCase();

                    /* X Setup */
                    if (!os.contains("nix") && !os.contains("nux") && !os.contains("mac")) {
                        //setLookAndFeel();
                    }
                    //setLookAndFeel();
                    //Start Equinox OSGi framework!!
                    //this part from Neil Bartlett
                   /* FrameworkFactory factory = getFrameworkFactory();
                    Map<String, String> config = new HashMap<String, String>();
                    config.put(Constants.FRAMEWORK_SYSTEMPACKAGES_EXTRA,
                            "com.datavirtue.nevitiumpro");
                    Framework osgiFramework = factory.newFramework(config);
                    osgiFramework.start();

                    //Load Plugins
                    BundleContext context = osgiFramework.getBundleContext();
                    List<Bundle> installedBundles = new LinkedList<Bundle>();

                    installedBundles.add(context.installBundle(
                            "file:plugins/TestPlugin.jar"));
                    
                    for (Bundle bundle : installedBundles) {
                        bundle.start();
                    }
*/
                    // This will log in the user, load the persistance engine, and start the ControlCenter
                    AccessDialog accessPanel = new AccessDialog(null, true);
                    accessPanel.setVisible(true);


                } catch (java.lang.UnsupportedClassVersionError e) {
                    javax.swing.JOptionPane.showMessageDialog(null,
                            "Nevitium encountered: Unsupported Class Version error. Try updating Java.");
                } catch (Exception ex) {
                    ExceptionErrorDialog errDialog = new ExceptionErrorDialog(ex);
                    errDialog.pack();
                    errDialog.setVisible(true);

                }
            }
        });
    }

    //this from Felix emnedded example that overall doesnt work or is out of date
    private static FrameworkFactory getFrameworkFactory() throws Exception {
        java.net.URL url = Main.class.getClassLoader().getResource(
                "META-INF/services/org.osgi.framework.launch.FrameworkFactory");
        if (url != null) {
            BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));
            try {
                for (String s = br.readLine(); s != null; s = br.readLine()) {
                    s = s.trim();
                    // Try to load first non-empty, non-commented line.
                    if ((s.length() > 0) && (s.charAt(0) != '#')) {
                        return (FrameworkFactory) Class.forName(s).newInstance();
                    }
                }
            } finally {
                if (br != null) {
                    br.close();
                }
            }
        }

        throw new Exception("Could not find framework factory.");
    }

    private static void setLookAndFeel() throws Exception {

        //com.jtattoo.plaf.acryl.AcrylLookAndFeel
        //com.jtattoo.plaf.aero.AeroLookAndFeel
        //com.jtattoo.plaf.aluminium.AluminiumLookAndFeel
        //com.jtattoo.plaf.bernstein.BernsteinLookAndFeel
        //com.jtattoo.plaf.fast.FastLookAndFeel
        //com.jtattoo.plaf.graphite.GraphiteLookAndFeel
        //com.jtattoo.plaf.hifi.HiFiLookAndFeel
        //com.jtattoo.plaf.luna.LunaLookAndFeel
        //com.jtattoo.plaf.mcwin.McWinLookAndFeel
        //com.jtattoo.plaf.mint.MintLookAndFeel
        //com.jtattoo.plaf.noire.NoireLookAndFeel
        //com.jtattoo.plaf.smart.SmartLookAndFeel


        Properties props = new Properties();
        props.put("logoString", "Nevitium Pro");
        props.put("licenseKey", "9da3-xq85-p1ft-guz5");

        String LAF = DV.readFile("theme.ini");
        LAF = LAF.trim();

        if (LAF.equals("com.jtattoo.plaf.acryl.AcrylLookAndFeel")) {
            AcrylLookAndFeel.setCurrentTheme(props);
        };
        if (LAF.equals("com.jtattoo.plaf.aero.AeroLookAndFeel")) {
            AeroLookAndFeel.setCurrentTheme(props);
        }
        if (LAF.equals("com.jtattoo.plaf.aluminium.AluminiumLookAndFeel")) {
            AluminiumLookAndFeel.setCurrentTheme(props);
        }
        if (LAF.equals("com.jtattoo.plaf.bernstein.BernsteinLookAndFeel")) {
            BernsteinLookAndFeel.setCurrentTheme(props);
        }
        if (LAF.equals("com.jtattoo.plaf.fast.FastLookAndFeel")) {
            FastLookAndFeel.setCurrentTheme(props);
        }
        if (LAF.equals("com.jtattoo.plaf.graphite.GraphiteLookAndFeel")) {
            GraphiteLookAndFeel.setCurrentTheme(props);
        }
        if (LAF.equals("com.jtattoo.plaf.hifi.HiFiLookAndFeel")) {
            HiFiLookAndFeel.setCurrentTheme(props);
        }
        if (LAF.equals("com.jtattoo.plaf.luna.LunaLookAndFeel")) {
            LunaLookAndFeel.setCurrentTheme(props);
        }
        if (LAF.equals("com.jtattoo.plaf.mcwin.McWinLookAndFeel")) {
            McWinLookAndFeel.setCurrentTheme(props);
        }
        if (LAF.equals("com.jtattoo.plaf.mint.MintLookAndFeel")) {
            MintLookAndFeel.setCurrentTheme(props);
        }
        if (LAF.equals("com.jtattoo.plaf.noire.NoireLookAndFeel")) {
            NoireLookAndFeel.setCurrentTheme(props);
        }
        if (LAF.equals("com.jtattoo.plaf.smart.SmartLookAndFeel")) {
            SmartLookAndFeel.setCurrentTheme(props);
        }

        if (LAF == null || LAF.equals("")) {
            LAF = "DEFAULT";
        }
        try {
            if (LAF.equals("DEFAULT")) {
                return;
                //don't set any look and feel
            } else {
                System.out.println(LAF);
                javax.swing.UIManager.setLookAndFeel(LAF);
            }

        } catch (Exception ex) {
            DV.writeFile("theme.ini", "DEFAULT", false);
            ex.printStackTrace();
            /*Contimue with default theme */
        }

    }
}
