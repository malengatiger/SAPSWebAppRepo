/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.boha.vodacom.dto;

import com.boha.vodacom.data.*;
import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author aubreymalabie
 */
public class PhotoDTO implements Serializable {

    private static final long serialVersionUID = 1L;
    private Integer photoID;
    private Long dateTaken;
    private Long dateUploaded;
    private String url, citizenName, officerName, incidentName;
    private Integer citizenID;
    private Integer officerID;
    private Integer panicIncidentID;

    public PhotoDTO() {
    }

    public PhotoDTO(Photo a) {
        this.photoID = a.getPhotoID();
        dateTaken = a.getDateTaken().getTime();
        dateUploaded = a.getDateUploaded().getTime();
        url = a.getUrl();
        if (a.getCitizen() != null) {
            citizenID = a.getCitizen().getCitizenID();
            citizenName = a.getCitizen().getName();
        }
        if (a.getOfficer() != null) {
            officerID = a.getOfficer().getOfficerID();
            officerName = a.getOfficer().getName();
        }
        if (a.getPanicIncident() != null) {
            panicIncidentID = a.getPanicIncident().getPanicIncidentID();
            incidentName = a.getPanicIncident().getPanicType().getName();
        }
    }

    public Long getDateTaken() {
        return dateTaken;
    }

    public void setDateTaken(Long dateTaken) {
        this.dateTaken = dateTaken;
    }

    public Long getDateUploaded() {
        return dateUploaded;
    }

    public void setDateUploaded(Long dateUploaded) {
        this.dateUploaded = dateUploaded;
    }

    public String getCitizenName() {
        return citizenName;
    }

    public void setCitizenName(String citizenName) {
        this.citizenName = citizenName;
    }

    public String getOfficerName() {
        return officerName;
    }

    public void setOfficerName(String officerName) {
        this.officerName = officerName;
    }

    public String getIncidentName() {
        return incidentName;
    }

    public void setIncidentName(String incidentName) {
        this.incidentName = incidentName;
    }

    public Integer getCitizenID() {
        return citizenID;
    }

    public void setCitizenID(Integer citizenID) {
        this.citizenID = citizenID;
    }

    public Integer getOfficerID() {
        return officerID;
    }

    public void setOfficerID(Integer officerID) {
        this.officerID = officerID;
    }

    public Integer getPanicIncidentID() {
        return panicIncidentID;
    }

    public void setPanicIncidentID(Integer panicIncidentID) {
        this.panicIncidentID = panicIncidentID;
    }

    public Integer getPhotoID() {
        return photoID;
    }

    public void setPhotoID(Integer photoID) {
        this.photoID = photoID;
    }

  
    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

   

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (photoID != null ? photoID.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof PhotoDTO)) {
            return false;
        }
        PhotoDTO other = (PhotoDTO) object;
        if ((this.photoID == null && other.photoID != null) || (this.photoID != null && !this.photoID.equals(other.photoID))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.boha.vodacom.data.Photo[ photoID=" + photoID + " ]";
    }
    
}
