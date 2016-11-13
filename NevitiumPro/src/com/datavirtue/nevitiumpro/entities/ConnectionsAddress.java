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
@Table(name = "connections_address")
@NamedQueries({
    @NamedQuery(name = "ConnectionsAddress.findAll", query = "SELECT c FROM ConnectionsAddress c")})
public class ConnectionsAddress implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID", nullable = false)
    private Integer id;
    @Column(name = "ATTN", length = 50)
    private String attn;
    @Column(name = "ADDRESS", length = 40)
    private String address;
    @Column(name = "ADDRESS_2", length = 40)
    private String address2;
    @Column(name = "CITY", length = 30)
    private String city;
    @Column(name = "PROVENCE", length = 20)
    private String provence;
    @Column(name = "POST_CODE", length = 12)
    private String postCode;
    @Column(name = "COUNTRY_CODE", length = 2)
    private String countryCode;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "LATITUDE", precision = 10, scale = 6)
    private BigDecimal latitude;
    @Column(name = "LONGITUDE", precision = 10, scale = 6)
    private BigDecimal longitude;
    @Column(name = "TRACKING_EMAIL", length = 50)
    private String trackingEmail;
    @Column(name = "NOTE", length = 140)
    private String note;
    @Column(name = "DEFAULT_ADDRESS")
    private Boolean defaultAddress;
    @JoinColumn(name = "CONNECTIONS_ID", referencedColumnName = "ID", nullable = false)
    @ManyToOne(optional = false)
    private Connections connections;

    public ConnectionsAddress() {
    }

    public ConnectionsAddress(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getAttn() {
        return attn;
    }

    public void setAttn(String attn) {
        this.attn = attn;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAddress2() {
        return address2;
    }

    public void setAddress2(String address2) {
        this.address2 = address2;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getProvence() {
        return provence;
    }

    public void setProvence(String provence) {
        this.provence = provence;
    }

    public String getPostCode() {
        return postCode;
    }

    public void setPostCode(String postCode) {
        this.postCode = postCode;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public BigDecimal getLatitude() {
        return latitude;
    }

    public void setLatitude(BigDecimal latitude) {
        this.latitude = latitude;
    }

    public BigDecimal getLongitude() {
        return longitude;
    }

    public void setLongitude(BigDecimal longitude) {
        this.longitude = longitude;
    }

    public String getTrackingEmail() {
        return trackingEmail;
    }

    public void setTrackingEmail(String trackingEmail) {
        this.trackingEmail = trackingEmail;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Boolean getDefaultAddress() {
        return defaultAddress;
    }

    public void setDefaultAddress(Boolean defaultAddress) {
        this.defaultAddress = defaultAddress;
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
        if (!(object instanceof ConnectionsAddress)) {
            return false;
        }
        ConnectionsAddress other = (ConnectionsAddress) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.datavirtue.nevitiumpro.entities.ConnectionsAddress[ id=" + id + " ]";
    }
    
}
