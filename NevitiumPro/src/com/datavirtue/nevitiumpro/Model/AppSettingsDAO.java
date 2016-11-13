/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.datavirtue.nevitiumpro.Model;


import com.datavirtue.nevitiumpro.Legacy.datavirtue.DV;
import com.datavirtue.nevitiumpro.entities.AppGlobalSettings;
import com.datavirtue.nevitiumpro.entities.AppUserSettings;
import com.datavirtue.nevitiumpro.hibernate.HibernateUtil;
import java.awt.Desktop;
import java.math.BigDecimal;
import org.hibernate.HibernateException;

/**
 *
 * @author Administrator
 */
public class AppSettingsDAO extends AbstractDAO<AppGlobalSettings>{
    
    public AppSettingsDAO(HibernateUtil hibernate){
        super(hibernate, AppGlobalSettings.class);
    }
    
    public AppGlobalSettings getGlobalSettings() throws Exception{
       return (AppGlobalSettings)this.getSess().get(AppGlobalSettings.class, 1);        
    }
    
    public AppUserSettings getUserSettings(int id) throws Exception{
        
        return (AppUserSettings)this.getSess().get(AppUserSettings.class, id);
    }
    
    public static AppGlobalSettings buildDefaultGlobalSettings(AbstractDAO dao){
        AppGlobalSettings globalBean = new AppGlobalSettings();
        globalBean.setCompanyName("My Company");
        globalBean.setCountryCode("US");
        globalBean.setPaymentSysUrl("");
        globalBean.setWebBasedPaymentSys(false);
        globalBean.setPaymentSysCc(false);
        globalBean.setPaymentSysChecks(false);

        globalBean.setTax1Value(new BigDecimal(.07));
        globalBean.setTax2Value(new BigDecimal(0));
        globalBean.setTax1Name("Tax1");
        globalBean.setTax2Name("Tax2");
        globalBean.setShowTax1(true);
        globalBean.setShowTax2(false);
        globalBean.setVatSystem(false);
        globalBean.setCurrencyRounding("None");

        globalBean.setCrTermsRate(new BigDecimal(0));
        globalBean.setCrGracePeriod(30);
        globalBean.setDefaultInvoiceScanField("UPC");
        globalBean.setInvoiceName("Invoice");
        globalBean.setQuoteName("Quote");

        globalBean.setMetric(false);
        globalBean.setCurrencySymbol("$");
        globalBean.setPrintZeroAmounts(false);

        //globalBean.setDefaultInventorySearchField("UPC");
        globalBean.setDefaultInventoryMarkup(new BigDecimal(2.5));
        globalBean.setIgnoreInventoryQty(false);

        String iValue = "10000";
        String qValue = "10000";
        String tmp;

        do {

            tmp = javax.swing.JOptionPane.showInputDialog("Please provide a starting INVOICE # between 1000 and 50000", iValue);

            if (tmp == null) {
                iValue = "10000";
            } else {
                iValue = tmp.trim();
            }

            if (DV.validIntString(iValue) && Integer.parseInt(iValue) < 50000 && Integer.parseInt(iValue) > 999) {
                break;
            } else {
                continue;
            }

        } while (true);
        globalBean.setNextInvoiceNumber(iValue);

        do { //setup starting quote number
            tmp = javax.swing.JOptionPane.showInputDialog("Please provide a starting QUOTE # between 1000 and 50000", qValue);

            if (tmp == null) {
                qValue = "10000";
            } else {
                qValue = tmp.trim();
            }

            if (DV.validIntString(qValue) && Integer.parseInt(qValue) < 50000 && Integer.parseInt(iValue) > 999) {
                break;
            } else {
                continue;
            }

        } while (true);
        globalBean.setNextQuoteNumber(qValue);
        dao.transactionSave(globalBean, 10);        
        return globalBean;
    }
    
    public static AppUserSettings buildDefaultAppUserSettings(int userId, AbstractDAO dao) throws Exception{
        String home = System.getProperty("user.home");
        String adobe = "C:/Program Files (x86)/Adobe/Reader 10.0/Reader/AcroRd32.exe";
        AppUserSettings userBean = new AppUserSettings();
        userBean.setUserId(userId);
        userBean.setRetrieveRemoteMessage(true);
        userBean.setTheme("DEFAULT");
        userBean.setShowToolbar(true);
        userBean.setAddress1("");
        userBean.setAddress2("");
        userBean.setCity("");
        userBean.setProvence("");
        userBean.setPinCode("");
        userBean.setPhone("");
        userBean.setEmailServer("");
        userBean.setEmailPort(0);
        userBean.setEmailSsl(false);
        userBean.setEmailAddress("");
        userBean.setEmailUsername("");
        userBean.setEmailPw("");
        userBean.setDefaultInvoiceFolder(home + "1NevitiumInvoices");
        userBean.setDefaultReportFolder(home + "1NevitiumReports");
        userBean.setDefaultQuoteFolder(home + "1NevitiumQuotes");
        userBean.setPdfReaderPath(adobe);
        userBean.setUseOsDefaults(Desktop.isDesktopSupported());
        userBean.setBackupFolder(home);
        userBean.setBackupMirrorFolder("");
        userBean.setPosMode(false);
        userBean.setPosPaperWidth(80);
        userBean.setInvoiceNumberPrefix("I");
        userBean.setQuoteNumberPrefix("Q");
        userBean.setDefaultProcessPayment(true);
        dao.transactionSave(userBean, 10);
        return userBean;        
    }
    
}
