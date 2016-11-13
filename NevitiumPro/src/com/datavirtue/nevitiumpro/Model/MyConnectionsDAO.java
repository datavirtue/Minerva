/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.datavirtue.nevitiumpro.Model;

import com.datavirtue.nevitiumpro.entities.Connections;
import com.datavirtue.nevitiumpro.hibernate.BeanTableModel;
import com.datavirtue.nevitiumpro.hibernate.HibernateUtil;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.criterion.Restrictions;

/**
 *
 * @author Administrator
 */
public class MyConnectionsDAO extends AbstractDAO<Connections> {
       
    public MyConnectionsDAO(HibernateUtil hibernate){
        super(hibernate, Connections.class);
    }
    
    /* Takes a contact string value which forms the hibernate criteria used to return a filtered data set */ 
    public BeanTableModel getBeanTableModel(String contactType)throws Exception{
        this.getSess();
        Criteria criteria = sess.createCriteria(Connections.class);
        criteria.add(Restrictions.eq(contactType, true));//pulls all contacts of spcified type (customer, employee, etc...) who are marked true
        List<Connections> list = criteria.list();
        //select InventoryImages i where (from Inventory id = id)
        //Query query = sess.createQuery("from Inventory i join fetch i.inventoryImagesList p where i.id = :id");
        //query.setParameter("id", i);
        
        //List<InventoryImages> list = query.list();
        return new BeanTableModel(Connections.class, list);
    }

    public BeanTableModel getBeanTableModelUnpaid() throws Exception{
        
        return null;
    }
   
    @Override
    public void transactionSave(Object o, int timeout) throws Exception {
        //check for duplicates, throw exception
        
    }
    
       
    
}
