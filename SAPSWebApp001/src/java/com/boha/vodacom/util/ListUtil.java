/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.boha.vodacom.util;

import com.boha.vodacom.data.PanicIncident;
import com.boha.vodacom.dto.PanicIncidentDTO;
import com.boha.vodacom.dto.ResponseDTO;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author aubreymalabie
 */
@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class ListUtil {

    @PersistenceContext
    EntityManager em;

    public ResponseDTO getPanicTypeList() throws DataException {
        ResponseDTO w = new ResponseDTO();
        try {

        } catch (Exception e) {
            log.log(Level.SEVERE, "Failed", e);
        }

        return w;
    }

    public ResponseDTO getIncidentListByOfficer(Integer officerID) throws DataException {
        ResponseDTO w = new ResponseDTO();
        try {
            Query q = em.createNamedQuery("PanicIncident.findByOfficer", PanicIncident.class);
            q.setParameter("officerID", officerID);
            List<PanicIncident> list = q.getResultList();
            for (PanicIncident panicIncident : list) {
                w.getIncidents().add(new PanicIncidentDTO(panicIncident));
            }
            w.setMessage("Incidents found: " + w.getIncidents().size());
        } catch (Exception e) {
            log.log(Level.SEVERE, "Failed", e);
            throw new DataException();
        }
        return w;
    }

    public ResponseDTO getIncidentListByPoliceStation(Integer policeStationID) throws DataException {
        ResponseDTO w = new ResponseDTO();
        try {
            Query q = em.createNamedQuery("PanicIncident.findByPoliceStation", PanicIncident.class);
            q.setParameter("policeStationID", policeStationID);
            List<PanicIncident> list = q.getResultList();
            for (PanicIncident panicIncident : list) {
                w.getIncidents().add(new PanicIncidentDTO(panicIncident));
            }
            w.setMessage("Incidents found: " + w.getIncidents().size());
        } catch (Exception e) {
            log.log(Level.SEVERE, "Failed", e);
            throw new DataException();
        }
        return w;
    }
    static final Logger log = Logger.getLogger(ListUtil.class.getName());
}
