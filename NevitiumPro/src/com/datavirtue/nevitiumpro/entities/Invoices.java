/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.datavirtue.nevitiumpro.entities;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 *
 * @author Administrator
 */
@Entity
@Table(name = "invoices")
@NamedQueries({
    @NamedQuery(name = "Invoices.findAll", query = "SELECT i FROM Invoices i")})
public class Invoices implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID", nullable = false)
    private Integer id;
    @Column(name = "INVOICE_NUMBER", length = 12)
    private String invoiceNumber;
    @Column(name = "DATE")
    private BigInteger date;
    @Column(name = "CUSTOMER", length = 200)
    private String customer;
    @Column(name = "MESSAGE", length = 4000)
    private String message;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "TAX1", precision = 19, scale = 4)
    private BigDecimal tax1;
    @Column(name = "TAX2", precision = 19, scale = 4)
    private BigDecimal tax2;
    @Column(name = "PAID")
    private Boolean paid;
    @Column(name = "VOIDED")
    private Boolean voided;
    @Column(name = "QUOTE_ID")
    private Integer quoteId;
    @ManyToMany(mappedBy = "invoicesList")
    private List<Projects> projectsList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "invoices")
    private List<InvoiceItems> invoiceItemsList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "invoices")
    private List<InvoiceReturns> invoiceReturnsList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "invoices")
    private List<InvoicePayments> invoicePaymentsList;
    @JoinColumn(name = "CONNECTIONS_ID", referencedColumnName = "ID", nullable = false)
    @ManyToOne(optional = false)
    private Connections connections;

    public Invoices() {
    }

    public Invoices(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getInvoiceNumber() {
        return invoiceNumber;
    }

    public void setInvoiceNumber(String invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }

    public BigInteger getDate() {
        return date;
    }

    public void setDate(BigInteger date) {
        this.date = date;
    }

    public String getCustomer() {
        return customer;
    }

    public void setCustomer(String customer) {
        this.customer = customer;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public BigDecimal getTax1() {
        return tax1;
    }

    public void setTax1(BigDecimal tax1) {
        this.tax1 = tax1;
    }

    public BigDecimal getTax2() {
        return tax2;
    }

    public void setTax2(BigDecimal tax2) {
        this.tax2 = tax2;
    }

    public Boolean getPaid() {
        return paid;
    }

    public void setPaid(Boolean paid) {
        this.paid = paid;
    }

    public Boolean getVoided() {
        return voided;
    }

    public void setVoided(Boolean voided) {
        this.voided = voided;
    }

    public Integer getQuoteId() {
        return quoteId;
    }

    public void setQuoteId(Integer quoteId) {
        this.quoteId = quoteId;
    }

    public List<Projects> getProjectsList() {
        return projectsList;
    }

    public void setProjectsList(List<Projects> projectsList) {
        this.projectsList = projectsList;
    }

    public List<InvoiceItems> getInvoiceItemsList() {
        return invoiceItemsList;
    }

    public void setInvoiceItemsList(List<InvoiceItems> invoiceItemsList) {
        this.invoiceItemsList = invoiceItemsList;
    }

    public List<InvoiceReturns> getInvoiceReturnsList() {
        return invoiceReturnsList;
    }

    public void setInvoiceReturnsList(List<InvoiceReturns> invoiceReturnsList) {
        this.invoiceReturnsList = invoiceReturnsList;
    }

    public List<InvoicePayments> getInvoicePaymentsList() {
        return invoicePaymentsList;
    }

    public void setInvoicePaymentsList(List<InvoicePayments> invoicePaymentsList) {
        this.invoicePaymentsList = invoicePaymentsList;
    }

    public Connections getConnections() {
        return connections;
    }

    public void setConnections(Connections connections) {
        this.connections = connections;
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
        if (!(object instanceof Invoices)) {
            return false;
        }
        Invoices other = (Invoices) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.datavirtue.nevitiumpro.entities.Invoices[ id=" + id + " ]";
    }
    
}
