/*
 * To change this license header, choose License Headers in ProjectDTO Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.boha.vodacom.dto;

import com.boha.vodacom.data.Citizen;
import com.boha.vodacom.data.GcmDevice;
import com.boha.vodacom.data.Officer;


/**
 *
 * @author aubreyM
 */
public class GcmDeviceDTO {

    private Integer gcmDeviceID;
    private String registrationID;
    private String manufacturer;
    private String model;
    private String app, name;
    private long dateRegistered;
    private String serialNumber, androidVersion;
    private Integer citizenID,officerID;

    public GcmDeviceDTO(GcmDevice a) {
        gcmDeviceID = a.getGcmDeviceID();
        registrationID = a.getRegistrationID();
        manufacturer = a.getManufacturer();
        model = a.getModel();
        app = a.getApp();
        if (a.getDateRegistered() != null) {
            dateRegistered = a.getDateRegistered().getTime();
        }
        serialNumber = a.getSerialNumber();
        Citizen s = a.getCitizen();
        if (s != null) {
            citizenID = s.getCitizenID();
            name = s.getName();
        }
        Officer m = a.getOfficer();
        if (m != null) {
            officerID = m.getOfficerID();
            name = m.getName();
        }
        
        androidVersion = a.getAndroidVersion();

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public String getApp() {
        return app;
    }

    public void setApp(String app) {
        this.app = app;
    }

    public Integer getGcmDeviceID() {
        return gcmDeviceID;
    }

    public void setGcmDeviceID(Integer gcmDeviceID) {
        this.gcmDeviceID = gcmDeviceID;
    }

    public String getRegistrationID() {
        return registrationID;
    }

    public void setRegistrationID(String registrationID) {
        this.registrationID = registrationID;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public long getDateRegistered() {
        return dateRegistered;
    }

    public void setDateRegistered(long dateRegistered) {
        this.dateRegistered = dateRegistered;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public String getAndroidVersion() {
        return androidVersion;
    }

    public void setAndroidVersion(String androidVersion) {
        this.androidVersion = androidVersion;
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

 

}
