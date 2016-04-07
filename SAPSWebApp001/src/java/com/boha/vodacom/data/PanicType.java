/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.boha.vodacom.data;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author aubreymalabie
 */
@Entity
@Table(name = "panicType")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "PanicType.findAll", query = "SELECT p FROM PanicType p"),
    @NamedQuery(name = "PanicType.findByPanicTypeID", query = "SELECT p FROM PanicType p WHERE p.panicTypeID = :panicTypeID"),
    @NamedQuery(name = "PanicType.findByName", query = "SELECT p FROM PanicType p WHERE p.name = :name")})
public class PanicType implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "panicTypeID")
    private Integer panicTypeID;
    @Size(max = 256)
    @Column(name = "name")
    private String name;
    @OneToMany(mappedBy = "panicType")
    private List<PanicIncident> panicIncidentList;

    public PanicType() {
    }

    public PanicType(Integer panicTypeID) {
        this.panicTypeID = panicTypeID;
    }

    public Integer getPanicTypeID() {
        return panicTypeID;
    }

    public void setPanicTypeID(Integer panicTypeID) {
        this.panicTypeID = panicTypeID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @XmlTransient
    public List<PanicIncident> getPanicIncidentList() {
        return panicIncidentList;
    }

    public void setPanicIncidentList(List<PanicIncident> panicIncidentList) {
        this.panicIncidentList = panicIncidentList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (panicTypeID != null ? panicTypeID.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof PanicType)) {
            return false;
        }
        PanicType other = (PanicType) object;
        if ((this.panicTypeID == null && other.panicTypeID != null) || (this.panicTypeID != null && !this.panicTypeID.equals(other.panicTypeID))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.boha.vodacom.data.PanicType[ panicTypeID=" + panicTypeID + " ]";
    }
    
}
