/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.boha.vodacom.util;

/**
 *
 * @author aubreyM
 */

import com.boha.vodacom.data.Officer;
import com.boha.vodacom.data.PanicType;
import com.boha.vodacom.dto.OfficerDTO;
import com.boha.vodacom.dto.PanicIncidentDTO;
import com.boha.vodacom.dto.PanicTypeDTO;
import com.boha.vodacom.dto.PoliceStationDTO;
import com.boha.vodacom.dto.ResponseDTO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class PanicWorkerBee {

    @PersistenceContext
    EntityManager em;

    private PreparedStatement preparedStatement;

    private static final String SQL_STATEMENT = "select a.panicIncidentID, a.citizenID,a.officerID, a.panicTypeID, a.dateSent, a.responseDate, a.latitude, a.longitude, "
            + "( ? * acos( cos( radians(?) ) * cos( radians( a.latitude) ) "
            + "* cos( radians( a.longitude ) - radians(?) ) + sin( radians(?) ) "
            + "* sin( radians( a.latitude ) ) ) ) "
            + "AS distance FROM panicIncident a HAVING distance < ? order by distance";
    
    public static final int KILOMETRES = 1, MILES = 2;
    static final int PARM_KM = 6371, PARM_MILES = 3959;

    private Connection conn;

    public ResponseDTO getIncidentsWithinRadius(double latitude, double longitude,
            int radius, int type)
            throws Exception {
        if (conn == null || conn.isClosed()) {
            conn = em.unwrap(Connection.class);
            log.log(Level.INFO, "..........SQL Connection unwrapped from EntityManager");
        }
        if (preparedStatement == null || preparedStatement.isClosed()) {
            preparedStatement = conn.prepareStatement(SQL_STATEMENT);
            log.log(Level.INFO, "..........SQL Statement prepared from Connection");
        }
        switch (type) {
            case KILOMETRES:
                preparedStatement.setInt(1, PARM_KM);
                break;
            case MILES:
                preparedStatement.setInt(1, PARM_MILES);
                break;
            case 0:
                preparedStatement.setInt(1, PARM_KM);
                break;
        }
        ResultSet resultSet;
        preparedStatement.setDouble(2, latitude);
        preparedStatement.setDouble(3, longitude);
        preparedStatement.setDouble(4, latitude);
        preparedStatement.setInt(5, radius);
        resultSet = preparedStatement.executeQuery();

        return buildIncidentList(resultSet);

    }

    private ResponseDTO buildIncidentList(ResultSet resultSet) throws SQLException {

        ResponseDTO resp = new ResponseDTO();
        List<PanicIncidentDTO> list = new ArrayList<>();      
        while (resultSet.next()) {
            int id = resultSet.getInt("panicIncidentID");
            Integer citizenID = resultSet.getInt("citizenID");
            Integer officerID = resultSet.getInt("officerID");
            Integer panicTypeID = resultSet.getInt("panicTypeID");
            Date dateSent = resultSet.getDate("dateSent");
            Date responseDate = resultSet.getDate("responseDate");
            
            double distance = resultSet.getDouble("distance");
            double lat = resultSet.getDouble("latitude");
            double lng = resultSet.getDouble("longitude");
            
            PanicIncidentDTO ps = new PanicIncidentDTO();
            ps.setPanicIncidentID(id);
            ps.setCitizenID(citizenID);
            ps.setOfficerID(officerID);
            ps.setDateSent(dateSent.getTime());
            if (responseDate != null) {
                ps.setResponseDate(responseDate.getTime());
            }
            ps.setDistance(distance);
            ps.setLatitude(lat);
            ps.setLongitude(lng);
            ps.setPanicType(new PanicTypeDTO(em.find(PanicType.class, panicTypeID)));
            list.add(ps);
            log.log(Level.INFO, "##### Incident within radius, name: {0} - {1} - {2} - lat: {3} lng: {4}", 
                    new Object[]{id, distance, id, lat, lng});
        }
        
        log.log(Level.INFO, "---- Incidents found: {0}", list.size());
       
        resultSet.close();
        resp.setIncidents(list);
        resp.setMessage("Incidents found: " + list.size());
        return resp;
    }
    static final Logger log = Logger.getLogger(PanicWorkerBee.class.getName());
}
