/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.datavirtue.nevitiumpro.hibernate;

import com.datavirtue.nevitiumpro.Legacy.RuntimeManagement.AppSettings;
import com.datavirtue.nevitiumpro.Legacy.datavirtue.DV;
import com.datavirtue.nevitiumpro.entities.UserAccounts;
import com.datavirtue.nevitiumpro.entities.WindowPositions;
import java.awt.Window;
import java.util.List;
import org.hibernate.HibernateException;

/**
 *
 * @author Administrator
 */
public class GUIUtil {
    
    public static void recordWindowPosition(AppSettings application, Window window) throws HibernateException{
        String windowName = window.getAccessibleContext().getAccessibleName();
        HibernateUtil hibernate = application.getJPA();
        UserAccounts user = application.getUserAccount();
        WindowPositions storedPosition=null;
        WindowPositions temp;
        hibernate.getSession().update(user);
        List<WindowPositions> windowList = user.getWindowPositionsList();
        for(int w = 0; w < windowList.size(); w++){
            temp = windowList.get(w);
            if (temp.getDialogName().equals(windowName)){
                storedPosition = temp;
                break;
            }
        }
        if (storedPosition==null){
            storedPosition = new WindowPositions();
            storedPosition.setDialogName(windowName);
        }
        storedPosition.setLocationX(window.getX());
        storedPosition.setLocationY(window.getY());
        storedPosition.setWidth(window.getWidth());
        storedPosition.setHeight(window.getHeight());
        storedPosition.setUserAccounts(user);
        user.getWindowPositionsList().add(storedPosition);
        hibernate.transactionSave(user, application.getTimeout());
    }
    
    public static void setWindowPosition(AppSettings application, Window window) throws HibernateException{
        String windowName = window.getAccessibleContext().getAccessibleName();
        HibernateUtil hibernate = application.getJPA();
        UserAccounts user = application.getUserAccount();
        WindowPositions storedPosition=null;
        WindowPositions temp;
        hibernate.getSession().update(user);
        List<WindowPositions> windowList = user.getWindowPositionsList();
        if (windowList == null){
            centerWindow(window);
            return;
        }
        for(int w = 0; w < windowList.size(); w++){
            temp = windowList.get(w);
            if (temp.getDialogName().equals(windowName)){
                storedPosition = temp;
                break;
            }
        }
        if (storedPosition==null){
            centerWindow(window);
            return;
        }
        window.setBounds(storedPosition.getLocationX(), storedPosition.getLocationY(), 
                storedPosition.getWidth(), storedPosition.getHeight());
    }
    
    public static void centerWindow(Window window){
        java.awt.Dimension dim = DV.computeCenter(window);
        window.setLocation(dim.width, dim.height);
    }
    
}
