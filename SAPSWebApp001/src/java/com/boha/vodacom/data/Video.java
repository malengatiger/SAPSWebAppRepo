/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.boha.vodacom.data;

import java.io.Serializable;
import java.util.Date;
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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author aubreymalabie
 */
@Entity
@Table(name = "video")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Video.findAll", query = "SELECT v FROM Video v"),
    @NamedQuery(name = "Video.findByVideoID", query = "SELECT v FROM Video v WHERE v.videoID = :videoID"),
    @NamedQuery(name = "Video.findByDateTaken", query = "SELECT v FROM Video v WHERE v.dateTaken = :dateTaken"),
    @NamedQuery(name = "Video.findByDateUploaded", query = "SELECT v FROM Video v WHERE v.dateUploaded = :dateUploaded"),
    @NamedQuery(name = "Video.findByUrl", query = "SELECT v FROM Video v WHERE v.url = :url")})
public class Video implements Serializable {

    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "latitude")
    private Double latitude;
    @Column(name = "longitude")
    private Double longitude;

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "videoID")
    private Integer videoID;
    @Column(name = "dateTaken")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateTaken;
    @Column(name = "dateUploaded")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateUploaded;
    @Size(max = 512)
    @Column(name = "url")
    private String url;
    @JoinColumn(name = "panicIncidentID", referencedColumnName = "panicIncidentID")
    @ManyToOne
    private PanicIncident panicIncident;
    @JoinColumn(name = "citizenID", referencedColumnName = "citizenID")
    @ManyToOne
    private Citizen citizen;
    @JoinColumn(name = "officerID", referencedColumnName = "officerID")
    @ManyToOne
    private Officer officer;

    public Video() {
    }

    public Video(Integer videoID) {
        this.videoID = videoID;
    }

    public Integer getVideoID() {
        return videoID;
    }

    public void setVideoID(Integer videoID) {
        this.videoID = videoID;
    }

    public Date getDateTaken() {
        return dateTaken;
    }

    public void setDateTaken(Date dateTaken) {
        this.dateTaken = dateTaken;
    }

    public Date getDateUploaded() {
        return dateUploaded;
    }

    public void setDateUploaded(Date dateUploaded) {
        this.dateUploaded = dateUploaded;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public PanicIncident getPanicIncident() {
        return panicIncident;
    }

    public void setPanicIncident(PanicIncident panicIncident) {
        this.panicIncident = panicIncident;
    }

    public Citizen getCitizen() {
        return citizen;
    }

    public void setCitizen(Citizen citizen) {
        this.citizen = citizen;
    }

    public Officer getOfficer() {
        return officer;
    }

    public void setOfficer(Officer officer) {
        this.officer = officer;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (videoID != null ? videoID.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Video)) {
            return false;
        }
        Video other = (Video) object;
        if ((this.videoID == null && other.videoID != null) || (this.videoID != null && !this.videoID.equals(other.videoID))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.boha.vodacom.data.Video[ videoID=" + videoID + " ]";
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
    
}
