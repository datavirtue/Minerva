/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.datavirtue.nevitiumpro.entities;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 *
 * @author Administrator
 */
@Entity
@Table(name = "app_user_settings")
@NamedQueries({
    @NamedQuery(name = "AppUserSettings.findAll", query = "SELECT a FROM AppUserSettings a")})
public class AppUserSettings implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID", nullable = false)
    private Integer id;
    @Column(name = "COUNTRY_CODE", length = 2)
    private String countryCode;
    @Column(name = "ADDRESS1", length = 50)
    private String address1;
    @Column(name = "ADDRESS2", length = 50)
    private String address2;
    @Column(name = "CITY", length = 45)
    private String city;
    @Column(name = "PROVENCE", length = 35)
    private String provence;
    @Column(name = "PIN_CODE", length = 16)
    private String pinCode;
    @Column(name = "PHONE", length = 20)
    private String phone;
    @Column(name = "EMAIL_ADDRESS", length = 50)
    private String emailAddress;
    @Column(name = "WEBSITE", length = 100)
    private String website;
    @Column(name = "EMAIL_SERVER", length = 50)
    private String emailServer;
    @Column(name = "EMAIL_PORT")
    private Integer emailPort;
    @Column(name = "EMAIL_SSL")
    private Boolean emailSsl;
    @Column(name = "EMAIL_USERNAME", length = 50)
    private String emailUsername;
    @Column(name = "EMAIL_PW", length = 256)
    private String emailPw;
    @Column(name = "SHOW_TOOLBAR")
    private Boolean showToolbar;
    @Column(name = "THEME", length = 200)
    private String theme;
    @Column(name = "POS_MODE")
    private Boolean posMode;
    @Column(name = "POS_PAPER_WIDTH")
    private Integer posPaperWidth;
    @Column(name = "DEFAULT_PROCESS_PAYMENT")
    private Boolean defaultProcessPayment;
    @Column(name = "DEFAULT_REPORT_FOLDER", length = 200)
    private String defaultReportFolder;
    @Column(name = "DEFAULT_INVOICE_FOLDER", length = 200)
    private String defaultInvoiceFolder;
    @Column(name = "DEFAULT_QUOTE_FOLDER", length = 200)
    private String defaultQuoteFolder;
    @Column(name = "PDF_READER_PATH", length = 200)
    private String pdfReaderPath;
    @Column(name = "USE_OS_DEFAULTS")
    private Boolean useOsDefaults;
    @Column(name = "BACKUP_FOLDER", length = 200)
    private String backupFolder;
    @Column(name = "BACKUP_MIRROR_FOLDER", length = 200)
    private String backupMirrorFolder;
    @Column(name = "INVOICE_NUMBER_PREFIX", length = 16)
    private String invoiceNumberPrefix;
    @Column(name = "QUOTE_NUMBER_PREFIX", length = 16)
    private String quoteNumberPrefix;
    @Column(name = "RETRIEVE_REMOTE_MESSAGE")
    private Boolean retrieveRemoteMessage;
    @Basic(optional = false)
    @Column(name = "USER_ID", nullable = false)
    private int userId;

    public AppUserSettings() {
    }

    public AppUserSettings(Integer id) {
        this.id = id;
    }

    public AppUserSettings(Integer id, int userId) {
        this.id = id;
        this.userId = userId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getAddress1() {
        return address1;
    }

    public void setAddress1(String address1) {
        this.address1 = address1;
    }

    public String getAddress2() {
        return address2;
    }

    public void setAddress2(String address2) {
        this.address2 = address2;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getProvence() {
        return provence;
    }

    public void setProvence(String provence) {
        this.provence = provence;
    }

    public String getPinCode() {
        return pinCode;
    }

    public void setPinCode(String pinCode) {
        this.pinCode = pinCode;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getEmailServer() {
        return emailServer;
    }

    public void setEmailServer(String emailServer) {
        this.emailServer = emailServer;
    }

    public Integer getEmailPort() {
        return emailPort;
    }

    public void setEmailPort(Integer emailPort) {
        this.emailPort = emailPort;
    }

    public Boolean getEmailSsl() {
        return emailSsl;
    }

    public void setEmailSsl(Boolean emailSsl) {
        this.emailSsl = emailSsl;
    }

    public String getEmailUsername() {
        return emailUsername;
    }

    public void setEmailUsername(String emailUsername) {
        this.emailUsername = emailUsername;
    }

    public String getEmailPw() {
        return emailPw;
    }

    public void setEmailPw(String emailPw) {
        this.emailPw = emailPw;
    }

    public Boolean getShowToolbar() {
        return showToolbar;
    }

    public void setShowToolbar(Boolean showToolbar) {
        this.showToolbar = showToolbar;
    }

    public String getTheme() {
        return theme;
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }

    public Boolean getPosMode() {
        return posMode;
    }

    public void setPosMode(Boolean posMode) {
        this.posMode = posMode;
    }

    public Integer getPosPaperWidth() {
        return posPaperWidth;
    }

    public void setPosPaperWidth(Integer posPaperWidth) {
        this.posPaperWidth = posPaperWidth;
    }

    public Boolean getDefaultProcessPayment() {
        return defaultProcessPayment;
    }

    public void setDefaultProcessPayment(Boolean defaultProcessPayment) {
        this.defaultProcessPayment = defaultProcessPayment;
    }

    public String getDefaultReportFolder() {
        return defaultReportFolder;
    }

    public void setDefaultReportFolder(String defaultReportFolder) {
        this.defaultReportFolder = defaultReportFolder;
    }

    public String getDefaultInvoiceFolder() {
        return defaultInvoiceFolder;
    }

    public void setDefaultInvoiceFolder(String defaultInvoiceFolder) {
        this.defaultInvoiceFolder = defaultInvoiceFolder;
    }

    public String getDefaultQuoteFolder() {
        return defaultQuoteFolder;
    }

    public void setDefaultQuoteFolder(String defaultQuoteFolder) {
        this.defaultQuoteFolder = defaultQuoteFolder;
    }

    public String getPdfReaderPath() {
        return pdfReaderPath;
    }

    public void setPdfReaderPath(String pdfReaderPath) {
        this.pdfReaderPath = pdfReaderPath;
    }

    public Boolean getUseOsDefaults() {
        return useOsDefaults;
    }

    public void setUseOsDefaults(Boolean useOsDefaults) {
        this.useOsDefaults = useOsDefaults;
    }

    public String getBackupFolder() {
        return backupFolder;
    }

    public void setBackupFolder(String backupFolder) {
        this.backupFolder = backupFolder;
    }

    public String getBackupMirrorFolder() {
        return backupMirrorFolder;
    }

    public void setBackupMirrorFolder(String backupMirrorFolder) {
        this.backupMirrorFolder = backupMirrorFolder;
    }

    public String getInvoiceNumberPrefix() {
        return invoiceNumberPrefix;
    }

    public void setInvoiceNumberPrefix(String invoiceNumberPrefix) {
        this.invoiceNumberPrefix = invoiceNumberPrefix;
    }

    public String getQuoteNumberPrefix() {
        return quoteNumberPrefix;
    }

    public void setQuoteNumberPrefix(String quoteNumberPrefix) {
        this.quoteNumberPrefix = quoteNumberPrefix;
    }

    public Boolean getRetrieveRemoteMessage() {
        return retrieveRemoteMessage;
    }

    public void setRetrieveRemoteMessage(Boolean retrieveRemoteMessage) {
        this.retrieveRemoteMessage = retrieveRemoteMessage;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof AppUserSettings)) {
            return false;
        }
        AppUserSettings other = (AppUserSettings) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.datavirtue.nevitiumpro.entities.AppUserSettings[ id=" + id + " ]";
    }
    
}
