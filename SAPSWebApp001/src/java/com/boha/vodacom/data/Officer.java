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
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
@Table(name = "officer")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Officer.findByPoliceStationList",
            query = "SELECT o FROM Officer o where o.policeStation in :list order by o.policeStation.policeStationID, o.name"),
    @NamedQuery(name = "Officer.signIn",
            query = "SELECT o FROM Officer o WHERE o.email = :email and o.password = :password"),
    @NamedQuery(name = "Officer.findByName", query = "SELECT o FROM Officer o WHERE o.name = :name"),
    @NamedQuery(name = "Officer.findByEmail", query = "SELECT o FROM Officer o WHERE o.email = :email"),
    @NamedQuery(name = "Officer.findByCellphone", query = "SELECT o FROM Officer o WHERE o.cellphone = :cellphone"),
    @NamedQuery(name = "Officer.findByDateRegistered", query = "SELECT o FROM Officer o WHERE o.dateRegistered = :dateRegistered")})
public class Officer implements Serializable {

    @OneToMany(mappedBy = "officer")
    private List<Photo> photoList;
    @OneToMany(mappedBy = "officer")
    private List<Video> videoList;

    @Size(max = 50)
    @Column(name = "password")
    private String password;
    @Column(name = "gender")
    private Integer gender;
    @OneToMany(mappedBy = "officer")
    private List<GcmDevice> gcmDeviceList;

    @OneToMany(mappedBy = "officer")
    private List<PanicIncident> panicIncidentList;

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "officerID")
    private Integer officerID;
    @Size(max = 256)
    @Column(name = "name")
    private String name;
    @Size(max = 256)
    @Column(name = "email")
    private String email;
    @Size(max = 50)
    @Column(name = "cellphone")
    private String cellphone;
    @Column(name = "dateRegistered")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateRegistered;
    @JoinColumn(name = "policeStationID", referencedColumnName = "policeStationID")
    @ManyToOne
    private PoliceStation policeStation;

    public Officer() {
    }

    public Officer(Integer officerID) {
        this.officerID = officerID;
    }

    public PoliceStation getPoliceStation() {
        return policeStation;
    }

    public Integer getGender() {
        return gender;
    }

    public void setGender(Integer gender) {
        this.gender = gender;
    }

    public void setPoliceStation(PoliceStation policeStation) {
        this.policeStation = policeStation;
    }

    public Integer getOfficerID() {
        return officerID;
    }

    public void setOfficerID(Integer officerID) {
        this.officerID = officerID;
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

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (officerID != null ? officerID.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Officer)) {
            return false;
        }
        Officer other = (Officer) object;
        if ((this.officerID == null && other.officerID != null) || (this.officerID != null && !this.officerID.equals(other.officerID))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.boha.vodacom.data.Officer[ officerID=" + officerID + " ]";
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
