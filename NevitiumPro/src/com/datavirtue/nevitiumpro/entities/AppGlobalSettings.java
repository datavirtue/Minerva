/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.datavirtue.nevitiumpro.entities;

import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 *
 * @author Administrator
 */
@Entity
@Table(name = "app_global_settings")
@NamedQueries({
    @NamedQuery(name = "AppGlobalSettings.findAll", query = "SELECT a FROM AppGlobalSettings a")})
public class AppGlobalSettings implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID", nullable = false)
    private Integer id;
    @Column(name = "COUNTRY_CODE", length = 2)
    private String countryCode;
    @Column(name = "COMPANY_NAME", length = 50)
    private String companyName;
    @Column(name = "TAX_ID", length = 20)
    private String taxId;
    @Column(name = "CURRENCY_SYMBOL", length = 3)
    private String currencySymbol;
    @Lob
    @Column(name = "COMPANY_LOGO")
    private byte[] companyLogo;
    @Column(name = "PAYMENT_SYS_URL", length = 200)
    private String paymentSysUrl;
    @Column(name = "WEB_BASED_PAYMENT_SYS")
    private Boolean webBasedPaymentSys;
    @Column(name = "PAYMENT_SYS_CHECKS")
    private Boolean paymentSysChecks;
    @Column(name = "PAYMENT_SYS_CC")
    private Boolean paymentSysCc;
    @Column(name = "TAX_1_NAME", length = 16)
    private String tax1Name;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "TAX_1_VALUE", precision = 19, scale = 4)
    private BigDecimal tax1Value;
    @Column(name = "TAX_2_NAME", length = 16)
    private String tax2Name;
    @Column(name = "TAX_2_VALUE", precision = 19, scale = 4)
    private BigDecimal tax2Value;
    @Column(name = "SHOW_TAX_1")
    private Boolean showTax1;
    @Column(name = "SHOW_TAX_2")
    private Boolean showTax2;
    @Column(name = "CURRENCY_ROUNDING", length = 4)
    private String currencyRounding;
    @Column(name = "CR_TERMS_RATE", precision = 19, scale = 4)
    private BigDecimal crTermsRate;
    @Column(name = "CR_DISCOUNT_DAYS")
    private Integer crDiscountDays;
    @Column(name = "CR_DISCOUNT_RATE", precision = 19, scale = 4)
    private BigDecimal crDiscountRate;
    @Column(name = "CR_GRACE_PERIOD")
    private Integer crGracePeriod;
    @Column(name = "INVOICE_NAME", length = 20)
    private String invoiceName;
    @Column(name = "QUOTE_NAME", length = 20)
    private String quoteName;
    @Column(name = "PRINT_ZERO_AMOUNTS")
    private Boolean printZeroAmounts;
    @Column(name = "DEFAULT_INVOICE_SCAN_FIELD", length = 10)
    private String defaultInvoiceScanField;
    @Column(name = "DEFAULT_INVENTORY_MARKUP", precision = 19, scale = 4)
    private BigDecimal defaultInventoryMarkup;
    @Column(name = "IGNORE_INVENTORY_QTY")
    private Boolean ignoreInventoryQty;
    @Column(name = "VAT_SYSTEM")
    private Boolean vatSystem;
    @Column(name = "METRIC")
    private Boolean metric;
    @Column(name = "NEXT_INVOICE_NUMBER", length = 16)
    private String nextInvoiceNumber;
    @Column(name = "NEXT_QUOTE_NUMBER", length = 16)
    private String nextQuoteNumber;
    @Column(name = "DEFAULT_INVENTORY_SCAN_FIELD", length = 10)
    private String defaultInventoryScanField;

    public AppGlobalSettings() {
    }

    public AppGlobalSettings(Integer id) {
        this.id = id;
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

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getTaxId() {
        return taxId;
    }

    public void setTaxId(String taxId) {
        this.taxId = taxId;
    }

    public String getCurrencySymbol() {
        return currencySymbol;
    }

    public void setCurrencySymbol(String currencySymbol) {
        this.currencySymbol = currencySymbol;
    }

    public byte[] getCompanyLogo() {
        return companyLogo;
    }

    public void setCompanyLogo(byte[] companyLogo) {
        this.companyLogo = companyLogo;
    }

    public String getPaymentSysUrl() {
        return paymentSysUrl;
    }

    public void setPaymentSysUrl(String paymentSysUrl) {
        this.paymentSysUrl = paymentSysUrl;
    }

    public Boolean getWebBasedPaymentSys() {
        return webBasedPaymentSys;
    }

    public void setWebBasedPaymentSys(Boolean webBasedPaymentSys) {
        this.webBasedPaymentSys = webBasedPaymentSys;
    }

    public Boolean getPaymentSysChecks() {
        return paymentSysChecks;
    }

    public void setPaymentSysChecks(Boolean paymentSysChecks) {
        this.paymentSysChecks = paymentSysChecks;
    }

    public Boolean getPaymentSysCc() {
        return paymentSysCc;
    }

    public void setPaymentSysCc(Boolean paymentSysCc) {
        this.paymentSysCc = paymentSysCc;
    }

    public String getTax1Name() {
        return tax1Name;
    }

    public void setTax1Name(String tax1Name) {
        this.tax1Name = tax1Name;
    }

    public BigDecimal getTax1Value() {
        return tax1Value;
    }

    public void setTax1Value(BigDecimal tax1Value) {
        this.tax1Value = tax1Value;
    }

    public String getTax2Name() {
        return tax2Name;
    }

    public void setTax2Name(String tax2Name) {
        this.tax2Name = tax2Name;
    }

    public BigDecimal getTax2Value() {
        return tax2Value;
    }

    public void setTax2Value(BigDecimal tax2Value) {
        this.tax2Value = tax2Value;
    }

    public Boolean getShowTax1() {
        return showTax1;
    }

    public void setShowTax1(Boolean showTax1) {
        this.showTax1 = showTax1;
    }

    public Boolean getShowTax2() {
        return showTax2;
    }

    public void setShowTax2(Boolean showTax2) {
        this.showTax2 = showTax2;
    }

    public String getCurrencyRounding() {
        return currencyRounding;
    }

    public void setCurrencyRounding(String currencyRounding) {
        this.currencyRounding = currencyRounding;
    }

    public BigDecimal getCrTermsRate() {
        return crTermsRate;
    }

    public void setCrTermsRate(BigDecimal crTermsRate) {
        this.crTermsRate = crTermsRate;
    }

    public Integer getCrDiscountDays() {
        return crDiscountDays;
    }

    public void setCrDiscountDays(Integer crDiscountDays) {
        this.crDiscountDays = crDiscountDays;
    }

    public BigDecimal getCrDiscountRate() {
        return crDiscountRate;
    }

    public void setCrDiscountRate(BigDecimal crDiscountRate) {
        this.crDiscountRate = crDiscountRate;
    }

    public Integer getCrGracePeriod() {
        return crGracePeriod;
    }

    public void setCrGracePeriod(Integer crGracePeriod) {
        this.crGracePeriod = crGracePeriod;
    }

    public String getInvoiceName() {
        return invoiceName;
    }

    public void setInvoiceName(String invoiceName) {
        this.invoiceName = invoiceName;
    }

    public String getQuoteName() {
        return quoteName;
    }

    public void setQuoteName(String quoteName) {
        this.quoteName = quoteName;
    }

    public Boolean getPrintZeroAmounts() {
        return printZeroAmounts;
    }

    public void setPrintZeroAmounts(Boolean printZeroAmounts) {
        this.printZeroAmounts = printZeroAmounts;
    }

    public String getDefaultInvoiceScanField() {
        return defaultInvoiceScanField;
    }

    public void setDefaultInvoiceScanField(String defaultInvoiceScanField) {
        this.defaultInvoiceScanField = defaultInvoiceScanField;
    }

    public BigDecimal getDefaultInventoryMarkup() {
        return defaultInventoryMarkup;
    }

    public void setDefaultInventoryMarkup(BigDecimal defaultInventoryMarkup) {
        this.defaultInventoryMarkup = defaultInventoryMarkup;
    }

    public Boolean getIgnoreInventoryQty() {
        return ignoreInventoryQty;
    }

    public void setIgnoreInventoryQty(Boolean ignoreInventoryQty) {
        this.ignoreInventoryQty = ignoreInventoryQty;
    }

    public Boolean getVatSystem() {
        return vatSystem;
    }

    public void setVatSystem(Boolean vatSystem) {
        this.vatSystem = vatSystem;
    }

    public Boolean getMetric() {
        return metric;
    }

    public void setMetric(Boolean metric) {
        this.metric = metric;
    }

    public String getNextInvoiceNumber() {
        return nextInvoiceNumber;
    }

    public void setNextInvoiceNumber(String nextInvoiceNumber) {
        this.nextInvoiceNumber = nextInvoiceNumber;
    }

    public String getNextQuoteNumber() {
        return nextQuoteNumber;
    }

    public void setNextQuoteNumber(String nextQuoteNumber) {
        this.nextQuoteNumber = nextQuoteNumber;
    }

    public String getDefaultInventoryScanField() {
        return defaultInventoryScanField;
    }

    public void setDefaultInventoryScanField(String defaultInventoryScanField) {
        this.defaultInventoryScanField = defaultInventoryScanField;
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
        if (!(object instanceof AppGlobalSettings)) {
            return false;
        }
        AppGlobalSettings other = (AppGlobalSettings) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.datavirtue.nevitiumpro.entities.AppGlobalSettings[ id=" + id + " ]";
    }
    
}
