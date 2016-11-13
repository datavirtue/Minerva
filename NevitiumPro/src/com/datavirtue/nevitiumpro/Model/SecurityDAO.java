/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.datavirtue.nevitiumpro.Model;

import com.datavirtue.nevitiumpro.entities.UserAccounts;
import com.datavirtue.nevitiumpro.hibernate.HibernateUtil;

/**
 *
 * @author Administrator
 */
public class SecurityDAO extends AbstractDAO<UserAccounts> {
    public SecurityDAO(HibernateUtil hibernate){
        super(hibernate, UserAccounts.class);
    }
    
    
    
}
