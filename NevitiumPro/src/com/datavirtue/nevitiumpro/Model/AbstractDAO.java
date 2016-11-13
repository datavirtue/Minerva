/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.datavirtue.nevitiumpro.Model;

import com.datavirtue.nevitiumpro.exceptions.NullEntityException;
import com.datavirtue.nevitiumpro.hibernate.BeanTableModel;
import com.datavirtue.nevitiumpro.hibernate.HibernateUtil;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;

/**
 *
 * @author Administrator
 */
public abstract class AbstractDAO<T> {

    HibernateUtil hibernate;
    Session sess;
    Class<T> targetClass;
    private short defaultTimeout = 30;
    
    public AbstractDAO(HibernateUtil hibernate, Class<T> t) {
        this.targetClass = t;
        this.hibernate = hibernate;
    }

    public Session getSess() throws Exception {
        return sess = hibernate.getSession();
    }

    
    public void transactionSave(Object o) throws Exception {
        this.transactionSave(o, defaultTimeout);
    }
    public void transactionSave(Object o, int timeout) throws Exception {
        hibernate.transactionSave(o, timeout);
    }
    public void transactionDelete(Object o) throws Exception {
        this.transactionSave(o, defaultTimeout);
    }
    public void transactionDelete(Object o, int timeout) throws Exception{
        hibernate.transactionDelete(o, timeout);
    }

    public T getBeanById(int id) throws Exception {
        this.getSess();
        Criteria criteria = sess.createCriteria(targetClass);
        Criterion byId = Restrictions.idEq(id);
        criteria.add(byId);
        List<T> list = criteria.list();
        T c = list.get(0);
        return c;
    }

    public BeanTableModel getBeanTableModelByHQL(String q) throws Exception {
        this.getSess();
        Query hql = sess.createQuery(q);
        List<T> list = hql.list();
        return new BeanTableModel(targetClass, list);
    }

    public BeanTableModel getTableModelAll(int rowLimit) throws Exception {
        this.getSess();
        Criteria criteria = sess.createCriteria(targetClass);
        if (rowLimit > 0){
            criteria.setMaxResults(rowLimit);
        }
        List<T> list = criteria.list();
        if (list == null || list.isEmpty()){
            return new BeanTableModel(targetClass);
        }
        BeanTableModel tm = new BeanTableModel(targetClass, list);
        return tm;

    }
    
    
}
