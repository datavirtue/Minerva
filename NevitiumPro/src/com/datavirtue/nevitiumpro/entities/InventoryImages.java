/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.datavirtue.nevitiumpro.entities;

import java.io.Serializable;
import java.math.BigInteger;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.swing.ImageIcon;

/**
 *
 * @author Administrator
 */
@Entity
@Table(name = "inventory_images")
@NamedQueries({
    @NamedQuery(name = "InventoryImages.findAll", query = "SELECT i FROM InventoryImages i")})
public class InventoryImages implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID", nullable = false)
    private Integer id;
    @Column(name = "TITLE", length = 128)
    private String title;
    @Column(name = "DATE")
    private BigInteger date;
    @Column(name = "IMAGE_FORMAT", length = 3)
    private String imageFormat;
    @Column(name = "IMAGE_URL", length = 80)
    private String imageUrl;
    @Lob
    @Column(name = "BITMAP")
    private byte[] bitmap;
    @JoinColumn(name = "INVENTORY_ID", referencedColumnName = "ID", nullable = false)
    @ManyToOne(optional = false)
    private Inventory inventory;

    public InventoryImages() {
    }

    public InventoryImages(Integer id) {
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

    public BigInteger getDate() {
        return date;
    }

    public void setDate(BigInteger date) {
        this.date = date;
    }

    public String getImageFormat() {
        return imageFormat;
    }

    public void setImageFormat(String imageFormat) {
        this.imageFormat = imageFormat;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public ImageIcon getBitmap() {
        return new ImageIcon(bitmap);
    }

    public void setBitmap(byte[] bitmap) {
        this.bitmap = bitmap;
    }

    public Inventory getInventory() {
        return inventory;
    }

    public void setInventory(Inventory inventory) {
        this.inventory = inventory;
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
        if (!(object instanceof InventoryImages)) {
            return false;
        }
        InventoryImages other = (InventoryImages) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.datavirtue.nevitiumpro.entities.InventoryImages[ id=" + id + " ]";
    }
    
}
