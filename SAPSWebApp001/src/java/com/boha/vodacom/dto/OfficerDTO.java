/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.boha.vodacom.dto;

import com.boha.vodacom.data.Officer;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author aubreymalabie
 */
public class OfficerDTO implements Serializable {
    private List<PanicIncidentDTO> panicIncidentList;
    private static final long serialVersionUID = 1L;
    private Integer officerID;
    private String name;
    private String email, password;
    private String cellphone;
    private Long dateRegistered;
    private Integer gender;
    private Integer policeStationID;
    private List<PhotoDTO> photoList;
    private List<GcmDeviceDTO> deviceList;
    private List<VideoDTO> videoList;

    public OfficerDTO() {
    }

    public OfficerDTO(Officer a) {
        this.officerID = a.getOfficerID();
        name = a.getName();
        cellphone = a.getCellphone();
        email = a.getEmail();
        gender = a.getGender();
        dateRegistered = a.getDateRegistered().getTime();
        policeStationID = a.getPoliceStation().getPoliceStationID();
    }

    public List<PhotoDTO> getPhotoList() {
        if (photoList == null) {
            photoList = new ArrayList<>();
        }
        return photoList;
    }

    public void setPhotoList(List<PhotoDTO> photoList) {
        this.photoList = photoList;
    }

    public List<GcmDeviceDTO> getDeviceList() {
        if (deviceList == null) {
            deviceList = new ArrayList<>();
        }
        return deviceList;
    }

    public Integer getGender() {
        return gender;
    }

    public void setGender(Integer gender) {
        this.gender = gender;
    }

    public void setDeviceList(List<GcmDeviceDTO> deviceList) {
        this.deviceList = deviceList;
    }

    public List<VideoDTO> getVideoList() {
        if (videoList == null) {
            videoList = new ArrayList<>();
        }
        return videoList;
    }

    public void setVideoList(List<VideoDTO> videoList) {
        this.videoList = videoList;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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

    public Long getDateRegistered() {
        return dateRegistered;
    }

    public void setDateRegistered(Long dateRegistered) {
        this.dateRegistered = dateRegistered;
    }

    public Integer getPoliceStationID() {
        return policeStationID;
    }

    public void setPoliceStationID(Integer policeStationID) {
        this.policeStationID = policeStationID;
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
        if (!(object instanceof OfficerDTO)) {
            return false;
        }
        OfficerDTO other = (OfficerDTO) object;
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
    public List<PanicIncidentDTO> getPanicIncidentList() {
        return panicIncidentList;
    }

    public void setPanicIncidentList(List<PanicIncidentDTO> panicIncidentList) {
        this.panicIncidentList = panicIncidentList;
    }
    
}
