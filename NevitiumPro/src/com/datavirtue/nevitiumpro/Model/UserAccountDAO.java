/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.datavirtue.nevitiumpro.Model;

import com.datavirtue.nevitiumpro.entities.UserAccounts;
import com.datavirtue.nevitiumpro.hibernate.BeanTableModel;
import com.datavirtue.nevitiumpro.hibernate.HibernateUtil;
import java.util.List;
import org.hibernate.Query;

/**
 *
 * @author Administrator
 */
public class UserAccountDAO extends AbstractDAO<UserAccounts>{
    public UserAccountDAO(HibernateUtil hibernate){
        super(hibernate, UserAccounts.class);
    }
 public BeanTableModel getUserBean(String username) throws Exception{
        this.getSess();
        Query query = sess.createQuery("from UserAccounts where username = :u");
        query.setParameter("u", username);
        
        List<UserAccounts> list = query.list();
        if (list.isEmpty()) {
            return null;
        }        
        return new BeanTableModel(UserAccounts.class, list);
    }   
}
