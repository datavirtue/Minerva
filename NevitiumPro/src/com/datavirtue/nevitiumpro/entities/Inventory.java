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
import javax.persistence.JoinTable;
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
@Table(name = "inventory")
@NamedQueries({
    @NamedQuery(name = "Inventory.findAll", query = "SELECT i FROM Inventory i")})
public class Inventory implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID", nullable = false)
    private Integer id;
    @Column(name = "UPC", length = 14)
    private String upc;
    @Column(name = "CODE", length = 16)
    private String code;
    @Column(name = "DESCRIPTION", length = 300)
    private String description;
    @Column(name = "ITEM_SIZE", length = 15)
    private String itemSize;
    @Column(name = "WEIGHT", length = 15)
    private String weight;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "ONHAND", precision = 22)
    private Double onhand;
    @Column(name = "COST", precision = 19, scale = 4)
    private BigDecimal cost;
    @Column(name = "PRICE", precision = 19, scale = 4)
    private BigDecimal price;
    @Column(name = "CATEGORY", length = 30)
    private String category;
    @Column(name = "TAX1")
    private Boolean tax1;
    @Column(name = "TAX2")
    private Boolean tax2;
    @Column(name = "AVAILABLE")
    private Boolean available;
    @Column(name = "LAST_SALE")
    private BigInteger lastSale;
    @Column(name = "LAST_RECEIVED")
    private BigInteger lastReceived;
    @Column(name = "CUTOFF", precision = 22)
    private Double cutoff;
    @Column(name = "PARTIAL_QTY_ALLOWED")
    private Boolean partialQtyAllowed;
    @JoinTable(name = "inventory_has_connections", joinColumns = {
        @JoinColumn(name = "INVENTORY_ID", referencedColumnName = "ID", nullable = false)}, inverseJoinColumns = {
        @JoinColumn(name = "CONNECTIONS_ID", referencedColumnName = "ID", nullable = false)})
    @ManyToMany
    private List<Connections> connectionsList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "inventory")
    private List<InventoryImages> inventoryImagesList;

    public Inventory() {
    }

    public Inventory(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUpc() {
        return upc;
    }

    public void setUpc(String upc) {
        this.upc = upc;
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

    public String getItemSize() {
        return itemSize;
    }

    public void setItemSize(String itemSize) {
        this.itemSize = itemSize;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public Double getOnhand() {
        return onhand;
    }

    public void setOnhand(Double onhand) {
        this.onhand = onhand;
    }

    public BigDecimal getCost() {
        return cost;
    }

    public void setCost(BigDecimal cost) {
        this.cost = cost;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
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

    public Boolean getAvailable() {
        return available;
    }

    public void setAvailable(Boolean available) {
        this.available = available;
    }

    public BigInteger getLastSale() {
        return lastSale;
    }

    public void setLastSale(BigInteger lastSale) {
        this.lastSale = lastSale;
    }

    public BigInteger getLastReceived() {
        return lastReceived;
    }

    public void setLastReceived(BigInteger lastReceived) {
        this.lastReceived = lastReceived;
    }

    public Double getCutoff() {
        return cutoff;
    }

    public void setCutoff(Double cutoff) {
        this.cutoff = cutoff;
    }

    public Boolean getPartialQtyAllowed() {
        return partialQtyAllowed;
    }

    public void setPartialQtyAllowed(Boolean partialQtyAllowed) {
        this.partialQtyAllowed = partialQtyAllowed;
    }

    public List<Connections> getConnectionsList() {
        return connectionsList;
    }

    public void setConnectionsList(List<Connections> connectionsList) {
        this.connectionsList = connectionsList;
    }

    public List<InventoryImages> getInventoryImagesList() {
        return inventoryImagesList;
    }

    public void setInventoryImagesList(List<InventoryImages> inventoryImagesList) {
        this.inventoryImagesList = inventoryImagesList;
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
        if (!(object instanceof Inventory)) {
            return false;
        }
        Inventory other = (Inventory) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.datavirtue.nevitiumpro.entities.Inventory[ id=" + id + " ]";
    }
    
}
