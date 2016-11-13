/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.datavirtue.nevitiumpro.Model;

import com.datavirtue.nevitiumpro.entities.Connections;
import com.datavirtue.nevitiumpro.entities.Inventory;
import com.datavirtue.nevitiumpro.entities.InventoryImages;
import com.datavirtue.nevitiumpro.hibernate.BeanTableModel;
import com.datavirtue.nevitiumpro.hibernate.HibernateUtil;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Restrictions;

/**
 *
 * @author Administrator
 */
public class InventoryDAO extends AbstractDAO<Inventory>{
    
    public InventoryDAO(HibernateUtil hibernate){
        super(hibernate, Inventory.class);
    }
    public BeanTableModel getEagerImagesByInventoryId(int i) throws Exception{
        this.getSess();
        /*Criteria criteria = sess.createCriteria(InventoryImages.class);
        criteria.add(Restrictions.eq("Inventory", i)).setFetchMode("Bitmap", FetchMode.JOIN);
        List<InventoryImages> list = criteria.list();*/
        //select InventoryImages i where (from Inventory id = id)
                
        Query query = sess.createQuery("from Inventory i join fetch i.inventoryImagesList p where i.id = :id");
        query.setParameter("id", i);
        
        List<InventoryImages> list = query.list();
        return new BeanTableModel(InventoryImages.class, list);
    }
    
    public BeanTableModel getItemByCriteria(String column, String contains) throws Exception {
        this.getSess();
        Criteria criteria = sess.createCriteria(Connections.class);
        criteria.add(Restrictions.ilike(column, contains));//pulls all contacts of spcified type (customer, employee, etc...) who are marked true
        List<Inventory> list = criteria.list();
        
        return new BeanTableModel(Inventory.class, list);
    }
            
}
