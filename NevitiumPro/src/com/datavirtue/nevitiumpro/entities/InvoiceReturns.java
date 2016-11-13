/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.datavirtue.nevitiumpro.entities;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 *
 * @author Administrator
 */
@Entity
@Table(name = "invoice_returns")
@NamedQueries({
    @NamedQuery(name = "InvoiceReturns.findAll", query = "SELECT i FROM InvoiceReturns i")})
public class InvoiceReturns implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID", nullable = false)
    private Integer id;
    @Column(name = "DATE")
    private BigInteger date;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "QTY_RETURNED", precision = 19, scale = 4)
    private BigDecimal qtyReturned;
    @Column(name = "CODE", length = 20)
    private String code;
    @Column(name = "DESCRIPTION", length = 300)
    private String description;
    @Column(name = "CREDIT", precision = 19, scale = 4)
    private BigDecimal credit;
    @JoinColumn(name = "INVOICES_ID", referencedColumnName = "ID", nullable = false)
    @ManyToOne(optional = false)
    private Invoices invoices;

    public InvoiceReturns() {
    }

    public InvoiceReturns(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public BigInteger getDate() {
        return date;
    }

    public void setDate(BigInteger date) {
        this.date = date;
    }

    public BigDecimal getQtyReturned() {
        return qtyReturned;
    }

    public void setQtyReturned(BigDecimal qtyReturned) {
        this.qtyReturned = qtyReturned;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getCredit() {
        return credit;
    }

    public void setCredit(BigDecimal credit) {
        this.credit = credit;
    }

    public Invoices getInvoices() {
        return invoices;
    }

    public void setInvoices(Invoices invoices) {
        this.invoices = invoices;
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
        if (!(object instanceof InvoiceReturns)) {
            return false;
        }
        InvoiceReturns other = (InvoiceReturns) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.datavirtue.nevitiumpro.entities.InvoiceReturns[ id=" + id + " ]";
    }
    
}
