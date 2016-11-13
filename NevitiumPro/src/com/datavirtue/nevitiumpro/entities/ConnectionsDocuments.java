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

/**
 *
 * @author Administrator
 */
@Entity
@Table(name = "connections_documents")
@NamedQueries({
    @NamedQuery(name = "ConnectionsDocuments.findAll", query = "SELECT c FROM ConnectionsDocuments c")})
public class ConnectionsDocuments implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID", nullable = false)
    private Integer id;
    @Column(name = "SUBJECT", length = 80)
    private String subject;
    @Column(name = "DATE")
    private BigInteger date;
    @Column(name = "APPLICATION", length = 30)
    private String application;
    @Column(name = "TYPE", length = 3)
    private String type;
    @Lob
    @Column(name = "TEXT_DOCUMENT", length = 65535)
    private String textDocument;
    @Lob
    @Column(name = "BIN_DOCUMENT")
    private byte[] binDocument;
    @Column(name = "CONNECTIONS_DOCUMENTScol", length = 45)
    private String cONNECTIONSDOCUMENTScol;
    @JoinColumn(name = "CONNECTIONS_ID", referencedColumnName = "ID", nullable = false)
    @ManyToOne(optional = false)
    private Connections connections;

    public ConnectionsDocuments() {
    }

    public ConnectionsDocuments(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public BigInteger getDate() {
        return date;
    }

    public void setDate(BigInteger date) {
        this.date = date;
    }

    public String getApplication() {
        return application;
    }

    public void setApplication(String application) {
        this.application = application;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTextDocument() {
        return textDocument;
    }

    public void setTextDocument(String textDocument) {
        this.textDocument = textDocument;
    }

    public byte[] getBinDocument() {
        return binDocument;
    }

    public void setBinDocument(byte[] binDocument) {
        this.binDocument = binDocument;
    }

    public String getCONNECTIONSDOCUMENTScol() {
        return cONNECTIONSDOCUMENTScol;
    }

    public void setCONNECTIONSDOCUMENTScol(String cONNECTIONSDOCUMENTScol) {
        this.cONNECTIONSDOCUMENTScol = cONNECTIONSDOCUMENTScol;
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
        if (!(object instanceof ConnectionsDocuments)) {
            return false;
        }
        ConnectionsDocuments other = (ConnectionsDocuments) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.datavirtue.nevitiumpro.entities.ConnectionsDocuments[ id=" + id + " ]";
    }
    
}
