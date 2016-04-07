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
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author aubreymalabie
 */
@Entity
@Table(name = "panicIncident")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "PanicIncident.findByPoliceStationList", 
            query = "SELECT p FROM PanicIncident p where p.officer.policeStation.policeStationID in :list order by p.officer.officerID, p.dateSent desc"),
    
    @NamedQuery(name = "PanicIncident.findByPoliceStation", 
            query = "SELECT p FROM PanicIncident p where p.officer.policeStation.policeStationID = :policeStationID order by p.dateSent desc"),
    
    @NamedQuery(name = "PanicIncident.findByOfficer", 
            query = "SELECT p FROM PanicIncident p where p.officer.officerID = :officerID order by p.dateSent desc"),
    
})
public class PanicIncident implements Serializable {

    @JoinColumn(name = "policeStationID", referencedColumnName = "policeStationID")
    @ManyToOne
    private PoliceStation policeStation;

    @OneToMany(mappedBy = "panicIncident")
    private List<Photo> photoList;
    @OneToMany(mappedBy = "panicIncident")
    private List<Video> videoList;

    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "latitude")
    private Double latitude;
    @Column(name = "longitude")
    private Double longitude;

    @JoinColumn(name = "officerID", referencedColumnName = "officerID")
    @ManyToOne
    private Officer officer;
    
    @JoinColumn(name = "citizenID", referencedColumnName = "citizenID")
    @ManyToOne
    private Citizen citizen;

  
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "panicIncidentID")
    private Integer panicIncidentID;
    
    @Column(name = "dateSent")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateSent;
    @Column(name = "responseDate")
    @Temporal(TemporalType.TIMESTAMP)
    private Date responseDate;
    @JoinColumn(name = "panicTypeID", referencedColumnName = "panicTypeID")
    @ManyToOne
    private PanicType panicType;

    public PanicIncident() {
    }

    public PanicIncident(Integer panicIncidentID) {
        this.panicIncidentID = panicIncidentID;
    }

   

    public Integer getPanicIncidentID() {
        return panicIncidentID;
    }

    public void setPanicIncidentID(Integer panicIncidentID) {
        this.panicIncidentID = panicIncidentID;
    }

    public Officer getOfficer() {
        return officer;
    }

    public void setOfficer(Officer officer) {
        this.officer = officer;
    }

    public Citizen getCitizen() {
        return citizen;
    }

    public void setCitizen(Citizen citizen) {
        this.citizen = citizen;
    }

 

    public Date getDateSent() {
        return dateSent;
    }

    public void setDateSent(Date dateSent) {
        this.dateSent = dateSent;
    }

    public Date getResponseDate() {
        return responseDate;
    }

    public void setResponseDate(Date responseDate) {
        this.responseDate = responseDate;
    }


    @Override
    public int hashCode() {
        int hash = 0;
        hash += (panicIncidentID != null ? panicIncidentID.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof PanicIncident)) {
            return false;
        }
        PanicIncident other = (PanicIncident) object;
        if ((this.panicIncidentID == null && other.panicIncidentID != null) || (this.panicIncidentID != null && !this.panicIncidentID.equals(other.panicIncidentID))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.boha.vodacom.data.PanicIncident[ panicIncidentID=" + panicIncidentID + " ]";
    }

    public PanicType getPanicType() {
        return panicType;
    }

    public void setPanicType(PanicType panicType) {
        this.panicType = panicType;
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

    public PoliceStation getPoliceStation() {
        return policeStation;
    }

    public void setPoliceStation(PoliceStation policeStation) {
        this.policeStation = policeStation;
    }


    
    
}
