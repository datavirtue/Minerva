/*
 * This object changes to hold settings and state information that
 * need communicated between various apps residnig in the Nevitium System
 * This object will handle, error reporting and recovery, user settings (Properties),
 * screen state settings, security, etc
 * This prevents changing contructors when new objects need visibility.
 * Every object that interacts with the user must have access to the GlobalDaemon
 */
package com.datavirtue.nevitiumpro.Legacy.RuntimeManagement;

import com.datavirtue.nevitiumpro.Views.ExceptionErrorDialog;
import com.datavirtue.nevitiumpro.Legacy.datavirtue.DbEngine;
import com.datavirtue.nevitiumpro.Legacy.datavirtue.Settings;
import com.datavirtue.nevitiumpro.entities.AppGlobalSettings;
import com.datavirtue.nevitiumpro.entities.AppUserSettings;
import com.datavirtue.nevitiumpro.entities.UserAccounts;
import com.datavirtue.nevitiumpro.hibernate.HibernateUtil;
import java.util.ArrayList;
import java.util.ResourceBundle;
import org.osgi.framework.BundleException;
import org.osgi.framework.launch.Framework;

/**
 *
 * @author dataVirtue
 */
public class AppSettings {

    public AppSettings() {
    }
    private String workingPath = ".";
    private DbEngine db;
    private Settings props;
    private ArrayList runtimeIncidents = new ArrayList();
    private HibernateUtil jpa;
    private int globalJPATimeout = 10;
    private AppGlobalSettings globalSettings;
    private AppUserSettings userSettings;
    private UserAccounts UserAccounts;
    private ArrayList mediators = new ArrayList();
    private ArrayList appReturnObjects;
    private ResourceBundle langText;
    private int[] inventoryReturnValue = null;
    private int[] connectionsReturnValue = null;
    private Framework osgi;

    public void setOsgi(Framework f) {
        osgi = f;
    }

    public Framework getOsgi() {
        return osgi;
    }
    private boolean waiting = false;

    public void setWaiting(boolean wait) {
        waiting = wait;
    }

    public boolean isWaiting() {

        return waiting;
    }

    public void setResourceBundle(ResourceBundle r) {
        langText = r;
    }

    public ResourceBundle getLangText() {
        return langText;
    }

    public void setMediator(Mediator m) {
        mediators.add(m);
    }

    public Mediator getMediator() {
        mediators.trimToSize();
        Mediator m = (Mediator) mediators.get(mediators.size() - 1);
        /* return the last mediator in the list and remove it  */
        mediators.remove(mediators.size() - 1);
        return m;
    }

    public boolean checkPermissions(UserAccounts user, int userLevel, int required) {

        if (user.getMaster()) {
            return true;
        }
        if (userLevel >= required) {
            return true;
        } else {
            this.showSecurityDenialMessage();
            return false;
        }
    }

    public void showSecurityDenialMessage() {
        javax.swing.JOptionPane.showMessageDialog(null, "You do not have permission to access that feature.");
    }

    public void setProps(Settings s) {
        props = s;
    }

    public Settings getProps() {

        return props;
    }

    public void registerRuntimeIncident(RuntimeIncident rti) {
        runtimeIncidents.add(rti);
        /* Add to error file log.*/
        if (rti.isShowStopper()) {
            /* Tell the user and provide option to exit. */
            /* Provide other cleanup */
        }
    }

    public ArrayList getRuntimeIncidentList() {
        runtimeIncidents.trimToSize();
        return runtimeIncidents;
    }

    /**
     * @return the db
     */
    public DbEngine getDb() {
        return db;
    }

    /**
     * @param db the db to set
     */
    public void setDb(DbEngine db) {
        this.db = db;
    }

    public void setJPA(HibernateUtil hibernate) {
        jpa = hibernate;
    }

    public HibernateUtil getJPA() {
        return jpa;
    }

    public void setJPATimeout(int timeout) {
        globalJPATimeout = timeout;
    }

    public int getTimeout() {
        return globalJPATimeout;
    }

    public void setUserAccount(UserAccounts obj) {
        UserAccounts = obj;
    }

    public UserAccounts getUserAccount() {
        return UserAccounts;
    }

    public void setAppGlobalSettings(AppGlobalSettings s) {
        globalSettings = s;
    }

    public AppGlobalSettings getAppGlobalSettings() {
        return globalSettings;
    }

    public void setAppUserSettings(AppUserSettings s) {
        userSettings = s;
    }

    public AppUserSettings getAppUserSettings() {
        return userSettings;
    }

    /**
     * @return the workingPath
     */
    public String getWorkingPath() {
        return workingPath;
    }

    /**
     * @param workingPath the workingPath to set
     */
    public void setWorkingPath(String workingPath) {
        this.workingPath = workingPath;
    }

    /**
     * @return the appReturnObjects
     */
    public ArrayList getAppReturnObjects() {
        if (appReturnObjects == null) {
            return null;
        }
        ArrayList a = new ArrayList(appReturnObjects);
        appReturnObjects = new ArrayList();
        return a;
    }

    /**
     * @param appReturnObjects the appReturnObjects to set
     */
    public void setAppReturnObjects(ArrayList appReturnObjects) {
        this.appReturnObjects = appReturnObjects;
    }
    private String app_name = "";

    public void setAppName(String app) {
        app_name = app;
    }

    public String getAppName() {
        return app_name;
    }

    public void shutdown(boolean compactDatabase) {

        if (compactDatabase) {
            jpa.shutdown(compactDatabase);
        } else {
            jpa.shutdown();
        }
        try {
            //shuts down the system bundle, you have to get each plugin and stop
            osgi.getBundleContext().getBundle().stop();
            //osgi.uninstall();
        }catch(BundleException be){
            ExceptionErrorDialog errDialog = new ExceptionErrorDialog(be);
            errDialog.pack();
            errDialog.setVisible(true);
        }finally {
            System.exit(0);
        }



    }
}
