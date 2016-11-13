/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.datavirtue.nevitiumpro.Model;

import com.datavirtue.nevitiumpro.entities.Quotes;
import com.datavirtue.nevitiumpro.hibernate.HibernateUtil;

/**
 *
 * @author Administrator
 */
public class QuoteDAO extends AbstractDAO<Quotes> {
    public QuoteDAO(HibernateUtil hibernate){
        super(hibernate, Quotes.class);
    }
}
