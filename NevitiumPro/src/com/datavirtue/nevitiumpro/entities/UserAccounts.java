/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.datavirtue.nevitiumpro.entities;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 *
 * @author Administrator
 */
@Entity
@Table(name = "user_accounts")
@NamedQueries({
    @NamedQuery(name = "UserAccounts.findAll", query = "SELECT u FROM UserAccounts u")})
public class UserAccounts implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID", nullable = false)
    private Integer id;
    @Column(name = "USERNAME", length = 20)
    private String username;
    @Column(name = "PASSWORD", length = 200)
    private String password;
    @Column(name = "MASTER")
    private Boolean master;
    @Column(name = "INVENTORY")
    private Integer inventory;
    @Column(name = "CONNECTIONS")
    private Integer connections;
    @Column(name = "INVOICE_MANAGER")
    private Integer invoiceManager;
    @Column(name = "INVOICES")
    private Integer invoices;
    @Column(name = "QUOTES")
    private Integer quotes;
    @Column(name = "REPORTS")
    private Integer reports;
    @Column(name = "CHECKS")
    private Integer checks;
    @Column(name = "EXPORTS")
    private Integer exports;
    @Column(name = "SETTINGS")
    private Integer settings;
    @Column(name = "LAF", length = 100)
    private String laf;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "userAccounts")
    private List<WindowPositions> windowPositionsList;

    public UserAccounts() {
    }

    public UserAccounts(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Boolean getMaster() {
        return master;
    }

    public void setMaster(Boolean master) {
        this.master = master;
    }

    public Integer getInventory() {
        return inventory;
    }

    public void setInventory(Integer inventory) {
        this.inventory = inventory;
    }

    public Integer getConnections() {
        return connections;
    }

    public void setConnections(Integer connections) {
        this.connections = connections;
    }

    public Integer getInvoiceManager() {
        return invoiceManager;
    }

    public void setInvoiceManager(Integer invoiceManager) {
        this.invoiceManager = invoiceManager;
    }

    public Integer getInvoices() {
        return invoices;
    }

    public void setInvoices(Integer invoices) {
        this.invoices = invoices;
    }

    public Integer getQuotes() {
        return quotes;
    }

    public void setQuotes(Integer quotes) {
        this.quotes = quotes;
    }

    public Integer getReports() {
        return reports;
    }

    public void setReports(Integer reports) {
        this.reports = reports;
    }

    public Integer getChecks() {
        return checks;
    }

    public void setChecks(Integer checks) {
        this.checks = checks;
    }

    public Integer getExports() {
        return exports;
    }

    public void setExports(Integer exports) {
        this.exports = exports;
    }

    public Integer getSettings() {
        return settings;
    }

    public void setSettings(Integer settings) {
        this.settings = settings;
    }

    public String getLaf() {
        return laf;
    }

    public void setLaf(String laf) {
        this.laf = laf;
    }

    public List<WindowPositions> getWindowPositionsList() {
        return windowPositionsList;
    }

    public void setWindowPositionsList(List<WindowPositions> windowPositionsList) {
        this.windowPositionsList = windowPositionsList;
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
        if (!(object instanceof UserAccounts)) {
            return false;
        }
        UserAccounts other = (UserAccounts) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.datavirtue.nevitiumpro.entities.UserAccounts[ id=" + id + " ]";
    }
    
}
