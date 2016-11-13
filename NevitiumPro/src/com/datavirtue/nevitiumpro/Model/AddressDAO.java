/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.datavirtue.nevitiumpro.Model;

import com.datavirtue.nevitiumpro.entities.ConnectionsAddress;
import com.datavirtue.nevitiumpro.hibernate.HibernateUtil;

/**
 *
 * @author Administrator
 */
public class AddressDAO extends AbstractDAO<ConnectionsAddress> {
     public AddressDAO(HibernateUtil hibernate){
        super(hibernate, ConnectionsAddress.class);
    }
     
}
