/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.boha.vodacom.data;

import java.io.Serializable;
import java.util.Date;
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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author aubreymalabie
 */
@Entity
@Table(name = "citizen")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Citizen.findAll", query = "SELECT c FROM Citizen c"),
    @NamedQuery(name = "Citizen.signIn", 
            query = "SELECT c FROM Citizen c WHERE c.email = :email and c.password = :password"),
    @NamedQuery(name = "Citizen.findByName", query = "SELECT c FROM Citizen c WHERE c.name = :name"),
    @NamedQuery(name = "Citizen.findByEmail", query = "SELECT c FROM Citizen c WHERE c.email = :email"),
    @NamedQuery(name = "Citizen.findByCellphone", query = "SELECT c FROM Citizen c WHERE c.cellphone = :cellphone")
})
public class Citizen implements Serializable {

    @OneToMany(mappedBy = "citizen")
    private List<Photo> photoList;
    @OneToMany(mappedBy = "citizen")
    private List<Video> videoList;

    @Size(max = 50)
    @Column(name = "password")
    private String password;
    
    @Column(name = "gender")
    private Integer gender;

    @OneToMany(mappedBy = "citizen")
    private List<GcmDevice> gcmDeviceList;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "citizen")
    private List<PanicIncident> panicIncidentList;

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "citizenID")
    private Integer citizenID;
    @Size(max = 256)
    @Column(name = "name")
    private String name;
    @Size(max = 256)
    @Column(name = "email")
    private String email;
    @Size(max = 256)
    @Column(name = "cellphone")
    private String cellphone;
    @Column(name = "dateRegistered")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateRegistered;
    @Column(name = "latitude")
    private Double latitude;
    @Column(name = "longitude")
    private Double longitude;

    public Citizen() {
    }

    public Citizen(Integer citizenID) {
        this.citizenID = citizenID;
    }

    public Integer getCitizenID() {
        return citizenID;
    }

    public void setCitizenID(Integer citizenID) {
        this.citizenID = citizenID;
    }

    public Integer getGender() {
        return gender;
    }

    public void setGender(Integer gender) {
        this.gender = gender;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCellphone() {
        return cellphone;
    }

    public void setCellphone(String cellphone) {
        this.cellphone = cellphone;
    }

    public Date getDateRegistered() {
        return dateRegistered;
    }

    public void setDateRegistered(Date dateRegistered) {
        this.dateRegistered = dateRegistered;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (citizenID != null ? citizenID.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Citizen)) {
            return false;
        }
        Citizen other = (Citizen) object;
        if ((this.citizenID == null && other.citizenID != null) || (this.citizenID != null && !this.citizenID.equals(other.citizenID))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.boha.vodacom.data.Citizen[ citizenID=" + citizenID + " ]";
    }

    @XmlTransient
    public List<PanicIncident> getPanicIncidentList() {
        return panicIncidentList;
    }

    public void setPanicIncidentList(List<PanicIncident> panicIncidentList) {
        this.panicIncidentList = panicIncidentList;
    }

    @XmlTransient
    public List<GcmDevice> getGcmDeviceList() {
        return gcmDeviceList;
    }

    public void setGcmDeviceList(List<GcmDevice> gcmDeviceList) {
        this.gcmDeviceList = gcmDeviceList;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @XmlTransient
    public List<Photo> getPhotoList() {
        return photoList;
    }

    public void setPhotoList(List<Photo> photoList) {
        this.photoList = photoList;
    }

    @XmlTransient
    public List<Video> getVideoList() {
        return videoList;
    }

    public void setVideoList(List<Video> videoList) {
        this.videoList = videoList;
    }
    
}
