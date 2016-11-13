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
@Table(name = "invoice_items")
@NamedQueries({
    @NamedQuery(name = "InvoiceItems.findAll", query = "SELECT i FROM InvoiceItems i")})
public class InvoiceItems implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID", nullable = false)
    private Integer id;
    @Column(name = "DATE")
    private BigInteger date;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "QTY_SOLD", precision = 19, scale = 4)
    private BigDecimal qtySold;
    @Column(name = "CODE", length = 20)
    private String code;
    @Column(name = "DESCRIPTION", length = 300)
    private String description;
    @Column(name = "UNIT_PRICE", precision = 19, scale = 4)
    private BigDecimal unitPrice;
    @Column(name = "TAX1")
    private Boolean tax1;
    @Column(name = "TAX2")
    private Boolean tax2;
    @Column(name = "COST", precision = 19, scale = 4)
    private BigDecimal cost;
    @JoinColumn(name = "INVOICES_ID", referencedColumnName = "ID", nullable = false)
    @ManyToOne(optional = false)
    private Invoices invoices;

    public InvoiceItems() {
    }

    public InvoiceItems(Integer id) {
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

    public BigDecimal getQtySold() {
        return qtySold;
    }

    public void setQtySold(BigDecimal qtySold) {
        this.qtySold = qtySold;
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

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    public Boolean getTax1() {
        return tax1;
    }

    public void setTax1(Boolean tax1) {
        this.tax1 = tax1;
    }

    public Boolean getTax2() {
        return tax2;
    }

    public void setTax2(Boolean tax2) {
        this.tax2 = tax2;
    }

    public BigDecimal getCost() {
        return cost;
    }

    public void setCost(BigDecimal cost) {
        this.cost = cost;
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
        if (!(object instanceof InvoiceItems)) {
            return false;
        }
        InvoiceItems other = (InvoiceItems) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.datavirtue.nevitiumpro.entities.InvoiceItems[ id=" + id + " ]";
    }
    
}
