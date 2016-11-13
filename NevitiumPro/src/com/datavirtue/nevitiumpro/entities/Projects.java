/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.datavirtue.nevitiumpro.entities;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 *
 * @author Administrator
 */
@Entity
@Table(name = "projects")
@NamedQueries({
    @NamedQuery(name = "Projects.findAll", query = "SELECT p FROM Projects p")})
public class Projects implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID", nullable = false)
    private Integer id;
    @Column(name = "TITLE", length = 100)
    private String title;
    @Column(name = "MISSION", length = 32000)
    private String mission;
    @Column(name = "START_DATE")
    private BigInteger startDate;
    @Column(name = "TARGET_DATE")
    private BigInteger targetDate;
    @Column(name = "ACTUAL_END_DATE")
    private BigInteger actualEndDate;
    @Lob
    @Column(name = "NOTES", length = 65535)
    private String notes;
    @JoinTable(name = "projects_has_connections", joinColumns = {
        @JoinColumn(name = "PROJECTS_ID", referencedColumnName = "ID", nullable = false)}, inverseJoinColumns = {
        @JoinColumn(name = "CONNECTIONS_ID", referencedColumnName = "ID", nullable = false)})
    @ManyToMany
    private List<Connections> connectionsList;
    @JoinTable(name = "projects_has_quotes", joinColumns = {
        @JoinColumn(name = "PROJECTS_ID", referencedColumnName = "ID", nullable = false)}, inverseJoinColumns = {
        @JoinColumn(name = "QUOTES_ID", referencedColumnName = "ID", nullable = false)})
    @ManyToMany
    private List<Quotes> quotesList;
    @JoinTable(name = "projects_has_invoices", joinColumns = {
        @JoinColumn(name = "PROJECTS_ID", referencedColumnName = "ID", nullable = false)}, inverseJoinColumns = {
        @JoinColumn(name = "INVOICES_ID", referencedColumnName = "ID", nullable = false)})
    @ManyToMany
    private List<Invoices> invoicesList;

    public Projects() {
    }

    public Projects(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMission() {
        return mission;
    }

    public void setMission(String mission) {
        this.mission = mission;
    }

    public BigInteger getStartDate() {
        return startDate;
    }

    public void setStartDate(BigInteger startDate) {
        this.startDate = startDate;
    }

    public BigInteger getTargetDate() {
        return targetDate;
    }

    public void setTargetDate(BigInteger targetDate) {
        this.targetDate = targetDate;
    }

    public BigInteger getActualEndDate() {
        return actualEndDate;
    }

    public void setActualEndDate(BigInteger actualEndDate) {
        this.actualEndDate = actualEndDate;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public List<Connections> getConnectionsList() {
        return connectionsList;
    }

    public void setConnectionsList(List<Connections> connectionsList) {
        this.connectionsList = connectionsList;
    }

    public List<Quotes> getQuotesList() {
        return quotesList;
    }

    public void setQuotesList(List<Quotes> quotesList) {
        this.quotesList = quotesList;
    }

    public List<Invoices> getInvoicesList() {
        return invoicesList;
    }

    public void setInvoicesList(List<Invoices> invoicesList) {
        this.invoicesList = invoicesList;
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
        if (!(object instanceof Projects)) {
            return false;
        }
        Projects other = (Projects) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.datavirtue.nevitiumpro.entities.Projects[ id=" + id + " ]";
    }
    
}
