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
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 *
 * @author Administrator
 */
@Entity
@Table(name = "ar_register")
@NamedQueries({
    @NamedQuery(name = "ArRegister.findAll", query = "SELECT a FROM ArRegister a")})
public class ArRegister implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID", nullable = false)
    private Integer id;
    @Basic(optional = false)
    @Column(name = "CONNECTIONS_ID", nullable = false)
    private int connectionsId;
    @Column(name = "INVOICE_ID")
    private Integer invoiceId;
    @Basic(optional = false)
    @Column(name = "DATE", nullable = false)
    private long date;
    @Column(name = "MEMO", length = 50)
    private String memo;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "DEBIT", precision = 19, scale = 4)
    private BigDecimal debit;
    @Column(name = "CREDIT", precision = 19, scale = 4)
    private BigDecimal credit;

    public ArRegister() {
    }

    public ArRegister(Integer id) {
        this.id = id;
    }

    public ArRegister(Integer id, int connectionsId, long date) {
        this.id = id;
        this.connectionsId = connectionsId;
        this.date = date;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public int getConnectionsId() {
        return connectionsId;
    }

    public void setConnectionsId(int connectionsId) {
        this.connectionsId = connectionsId;
    }

    public Integer getInvoiceId() {
        return invoiceId;
    }

    public void setInvoiceId(Integer invoiceId) {
        this.invoiceId = invoiceId;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public BigDecimal getDebit() {
        return debit;
    }

    public void setDebit(BigDecimal debit) {
        this.debit = debit;
    }

    public BigDecimal getCredit() {
        return credit;
    }

    public void setCredit(BigDecimal credit) {
        this.credit = credit;
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
        if (!(object instanceof ArRegister)) {
            return false;
        }
        ArRegister other = (ArRegister) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.datavirtue.nevitiumpro.entities.ArRegister[ id=" + id + " ]";
    }
    
}
