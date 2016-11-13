/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.datavirtue.nevitiumpro.entities;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 *
 * @author Administrator
 */
@Entity
@Table(name = "connections")
@NamedQueries({
    @NamedQuery(name = "Connections.findAll", query = "SELECT c FROM Connections c")})
public class Connections implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID", nullable = false)
    private Integer id;
    @Column(name = "COMPANY_NAME", length = 40)
    private String companyName;
    @Basic(optional = false)
    @Column(name = "FIRST_NAME", nullable = false, length = 20)
    private String firstName;
    @Basic(optional = false)
    @Column(name = "LAST_NAME", nullable = false, length = 30)
    private String lastName;
    @Column(name = "EMAIL", length = 50)
    private String email;
    @Column(name = "WEBSITE", length = 100)
    private String website;
    @Column(name = "NOTE", length = 32000)
    private String note;
    @Column(name = "CUSTOM_1", length = 50)
    private String custom1;
    @Column(name = "CUSTOM_2", length = 50)
    private String custom2;
    @Column(name = "CUSTOMER")
    private Boolean customer;
    @Column(name = "EMPLOYEE")
    private Boolean employee;
    @Column(name = "SUPPLIER")
    private Boolean supplier;
    @Column(name = "TAX_1")
    private Boolean tax1;
    @Column(name = "TAX_2")
    private Boolean tax2;
    @Lob
    @Column(name = "PICTURE")
    private byte[] picture;
    @Column(name = "CUSTOMER_ID", length = 20)
    private String customerId;
    @Column(name = "EMPLOYEE_ID", length = 20)
    private String employeeId;
    @Column(name = "SUPPLIER_ID", length = 20)
    private String supplierId;
    @Column(name = "INCEPTION_DATE")
    private BigInteger inceptionDate;
    @ManyToMany(mappedBy = "connectionsList")
    private List<Projects> projectsList;
    @ManyToMany(mappedBy = "connectionsList")
    private List<Inventory> inventoryList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "connections")
    private List<ConnectionsPhones> connectionsPhonesList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "connections")
    private List<Quotes> quotesList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "connections")
    private List<ConnectionsDocuments> connectionsDocumentsList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "connections")
    private List<ConnectionsAddress> connectionsAddressList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "connections")
    private List<Invoices> invoicesList;

    public Connections() {
    }

    public Connections(Integer id) {
        this.id = id;
    }

    public Connections(Integer id, String firstName, String lastName) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getCustom1() {
        return custom1;
    }

    public void setCustom1(String custom1) {
        this.custom1 = custom1;
    }

    public String getCustom2() {
        return custom2;
    }

    public void setCustom2(String custom2) {
        this.custom2 = custom2;
    }

    public Boolean getCustomer() {
        return customer;
    }

    public void setCustomer(Boolean customer) {
        this.customer = customer;
    }

    public Boolean getEmployee() {
        return employee;
    }

    public void setEmployee(Boolean employee) {
        this.employee = employee;
    }

    public Boolean getSupplier() {
        return supplier;
    }

    public void setSupplier(Boolean supplier) {
        this.supplier = supplier;
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

    public byte[] getPicture() {
        return picture;
    }

    public void setPicture(byte[] picture) {
        this.picture = picture;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    public String getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(String supplierId) {
        this.supplierId = supplierId;
    }

    public BigInteger getInceptionDate() {
        return inceptionDate;
    }

    public void setInceptionDate(BigInteger inceptionDate) {
        this.inceptionDate = inceptionDate;
    }

    public List<Projects> getProjectsList() {
        return projectsList;
    }

    public void setProjectsList(List<Projects> projectsList) {
        this.projectsList = projectsList;
    }

    public List<Inventory> getInventoryList() {
        return inventoryList;
    }

    public void setInventoryList(List<Inventory> inventoryList) {
        this.inventoryList = inventoryList;
    }

    public List<ConnectionsPhones> getConnectionsPhonesList() {
        return connectionsPhonesList;
    }

    public void setConnectionsPhonesList(List<ConnectionsPhones> connectionsPhonesList) {
        this.connectionsPhonesList = connectionsPhonesList;
    }

    public List<Quotes> getQuotesList() {
        return quotesList;
    }

    public void setQuotesList(List<Quotes> quotesList) {
        this.quotesList = quotesList;
    }

    public List<ConnectionsDocuments> getConnectionsDocumentsList() {
        return connectionsDocumentsList;
    }

    public void setConnectionsDocumentsList(List<ConnectionsDocuments> connectionsDocumentsList) {
        this.connectionsDocumentsList = connectionsDocumentsList;
    }

    public List<ConnectionsAddress> getConnectionsAddressList() {
        return connectionsAddressList;
    }

    public void setConnectionsAddressList(List<ConnectionsAddress> connectionsAddressList) {
        this.connectionsAddressList = connectionsAddressList;
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
        if (!(object instanceof Connections)) {
            return false;
        }
        Connections other = (Connections) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.datavirtue.nevitiumpro.entities.Connections[ id=" + id + " ]";
    }
    
}
