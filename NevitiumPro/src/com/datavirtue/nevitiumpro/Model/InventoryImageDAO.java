/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.datavirtue.nevitiumpro.Model;

import com.datavirtue.nevitiumpro.entities.InventoryImages;
import com.datavirtue.nevitiumpro.hibernate.HibernateUtil;

/**
 *
 * @author Administrator
 */
public class InventoryImageDAO extends AbstractDAO<InventoryImages>{
    
    public InventoryImageDAO(HibernateUtil hibernate){
        super(hibernate, InventoryImages.class);
    }
    
        
    
}
