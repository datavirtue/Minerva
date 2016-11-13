/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.datavirtue.nevitiumpro.Model;

import com.datavirtue.nevitiumpro.entities.Invoices;
import com.datavirtue.nevitiumpro.hibernate.HibernateUtil;

/**
 *
 * @author Administrator
 */
public class InvoiceDAO extends AbstractDAO<Invoices> {
    public InvoiceDAO(HibernateUtil hibernate){
        super(hibernate, Invoices.class);
    }
    
    
    
}
