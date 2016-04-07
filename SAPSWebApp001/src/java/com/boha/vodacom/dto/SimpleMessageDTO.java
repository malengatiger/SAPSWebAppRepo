/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.boha.vodacom.dto;

import com.boha.vodacom.data.GcmDevice;
import com.boha.vodacom.data.Officer;
import com.boha.vodacom.util.WorkerBee;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.Query;

/**
 *
 * @author aubreymalabie
 */
public class SimpleMessageDTO {
    List<Integer> officerIDs, citizenIDs;
    PanicIncidentDTO incident;
    String message;
    List<String> registrationIDs = new ArrayList<>();

    public List<Integer> getOfficerIDs() {
        if (officerIDs == null) {
            officerIDs = new ArrayList<>();
        }
        return officerIDs;
    }

    public void setOfficerIDs(List<Integer> officerIDs) {
        this.officerIDs = officerIDs;
    }

    public List<Integer> getCitizenIDs() {
        if (citizenIDs == null) {
            citizenIDs = new ArrayList<>();
        }
        return citizenIDs;
    }

    public void setCitizenIDs(List<Integer> citizenIDs) {
        this.citizenIDs = citizenIDs;
    }
    

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    
    public PanicIncidentDTO getIncident() {
        return incident;
    }

    public void setIncident(PanicIncidentDTO incident) {
        this.incident = incident;
    }

    public List<String> getRegistrationIDs() {
      
        return registrationIDs;
    }

    public void addRegistrationID(String registrationID) {
        this.registrationIDs.add(registrationID);
    }
    
    public void buildRegistrationIDs(WorkerBee bee,
            double latitude,double longitude) {
        
        try {
            ResponseDTO w = bee.getPoliceStationsWithinRadius(
                    latitude, longitude, 25, WorkerBee.KILOMETRES);
            List<Integer> ids = new ArrayList<>();
            for (PoliceStationDTO ps : w.getPoliceStations()) {
                ids.add(ps.getPoliceStationID());
            }
            Query q = bee.getEm().createNamedQuery("Officer.findByPoliceStationList",Officer.class);
            q.setParameter("list", ids);       
            List<Officer> oList = q.getResultList();
            for (Officer officer : oList) {
                List<GcmDevice> gList = officer.getGcmDeviceList();
                for (GcmDevice gcmDevice : gList) {
                    registrationIDs.add(gcmDevice.getRegistrationID());
                }
            }
            
            
        } catch (Exception ex) {
            Logger.getLogger(SimpleMessageDTO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
