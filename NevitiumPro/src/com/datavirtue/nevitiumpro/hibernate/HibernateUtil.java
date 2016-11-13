/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.datavirtue.nevitiumpro.hibernate;

import com.datavirtue.nevitiumpro.Views.ExceptionErrorDialog;
import org.hibernate.HibernateException;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.AnnotationConfiguration;

/**
 *
 *
 * @author Administrator
 */
public class HibernateUtil {

    private Session session;
    private SessionFactory sessionFactory;
    private boolean embedded = false;

    public HibernateUtil() {
        this("org.hibernate.dialect.HSQLDialect",
                "jdbc:hsqldb:file:c:/data/nevitium",
                "sa", "");
    }

    public HibernateUtil(String dataPath){
        this("org.hibernate.dialect.HSQLDialect",
                "jdbc:hsqldb:file:" + dataPath,
                "sa", "");
    }
    
    public HibernateUtil(String SqlDialect, String connectionURL, String username, String password) {
        String driver = "org.hsqldb.jdbcDriver";

        /* Hypersonic SQL DB (embedded) */
        if (SqlDialect.equals("org.hibernate.dialect.HSQLDialect")) {
            //default is HSQLDB 
            this.embedded = true;
        }
        /* Oracle MySQL Server 5.X */
        if (SqlDialect.equals("org.hibernate.dialect.MySQL5Dialect")) {
            driver = "com.mysql.jdbc.Driver";
        }
        /* Microsoft SQL Server 2005 - 2008 */
        if (SqlDialect.equals("org.hibernate.dialect.SQLServerDialect")) {
            driver = "com.microsoft.sqlserver.Driver";
        }
        AnnotationConfiguration config = new AnnotationConfiguration()
                .setProperty("hibernate.connection.driver_class", driver)
                .setProperty("hibernate.connection.url", connectionURL)
                .setProperty("hibernate.dialect", SqlDialect)
                .setProperty("hibernate.show_sql", "true")
                .setProperty("hibernate.hbm2ddl.auto", "update")
                .setProperty("hibernate.connection.username", username)
                .setProperty("hibernate.connection.password", password);

        config.configure("/com/datavirtue/nevitiumpro/hibernate/hibernate.cfg.xml");

        this.sessionFactory = config.buildSessionFactory();
    }

    /* Use of this method guarantees that the we are only using one session,
     * and that session is different for each query or update.
     * This means that items get detached making it necessary to reattach 
     * and explicitly load entities.
     */
    public Session getSession() throws HibernateException {
        if (session != null && session.isOpen()) {
            session.close();
        }
        this.session = sessionFactory.openSession();
        return session;
    }

    private void closeSession() throws HibernateException {
        if (session != null && session.isOpen()) {
            session.close();
        }
        session = null;
    }
    
    public void transactionSave(Object obj, int timeout) throws HibernateException {
        getSession();
        Transaction t = null;
        try {

            t = session.beginTransaction();
            t.setTimeout(timeout);
            session.saveOrUpdate(obj);
            t.commit();

        } catch (RuntimeException re) {

            try {
                t.rollback();
            } catch (Exception ex) {
                ExceptionErrorDialog errDialog = new ExceptionErrorDialog(ex);
                errDialog.pack();
                errDialog.setVisible(true);
            }

            throw re;

        } finally {

            if (session != null) {
                session.close();
            }
        }
    }

    public void transactionDelete(Object obj, int timeout) throws HibernateException {
        getSession();
        Transaction t = null;
        try {

            t = session.beginTransaction();
            t.setTimeout(timeout);
            session.delete(obj);
            t.commit();

        } catch (RuntimeException re) {

            try {
                t.rollback();
            } catch (Exception ex) {
                ExceptionErrorDialog errDialog = new ExceptionErrorDialog(ex);
                errDialog.pack();
                errDialog.setVisible(true);
            }

            throw re;

        } finally {

            if (session != null) {
                session.close();
            }
        }
    }
   

    /*
     * Handles HSQLDB embedded database system shutdown procdure,
     * and properly frees resources and connections.
     */
    public void shutdown(){
        shutdown(false);
    }    
    public void shutdown(boolean compactDatabase) throws HibernateException {
        //this.closeSession();
        if (this.embedded) {
            this.getSession();
            /* HSQLDB doesn't exit/close/cleanup properly without this query! */
            SQLQuery query ;
            if (compactDatabase){
                //COMPACT shrinks the database, should be used periodically
                query = session.createSQLQuery("SHUTDOWN COMPACT");
            }else{
                query = session.createSQLQuery("SHUTDOWN");
            }
            query.executeUpdate();
        }
        this.closeSession();
        sessionFactory.close();
    }
}
