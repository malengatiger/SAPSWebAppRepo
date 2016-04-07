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
import com.boha.vodacom.data.PanicIncident;
import com.boha.vodacom.dto.OfficerDTO;
import com.boha.vodacom.dto.PanicIncidentDTO;
import com.boha.vodacom.dto.PoliceStationDTO;
import com.boha.vodacom.dto.ResponseDTO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
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
public class WorkerBee {

    @PersistenceContext
    EntityManager em;

    private PreparedStatement preparedStatement;

    private static final String SQL_STATEMENT = "select policeStationID, name, latitude, longitude, "
            + "( ? * acos( cos( radians(?) ) * cos( radians( latitude) ) "
            + "* cos( radians( longitude ) - radians(?) ) + sin( radians(?) ) "
            + "* sin( radians( latitude ) ) ) ) "
            + "AS distance FROM policeStation order by distance";

    private static final String SQL_STATEMENT2 = "select policeStationID, a.name, a.latitude, a.longitude,  "
            + "( ? * acos( cos( radians(?) ) * cos( radians( a.latitude) ) "
            + "* cos( radians( a.longitude ) - radians(?) ) + sin( radians(?) ) "
            + "* sin( radians( a.latitude ) ) ) ) "
            + "AS distance FROM policeStation a where distance < ? order by distance";

    public static final int KILOMETRES = 1, MILES = 2;
    static final int PARM_KM = 6371, PARM_MILES = 3959;

    private Connection conn;

    public ResponseDTO getPoliceStationsWithinRadius(double latitude, double longitude,
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
        //preparedStatement.setInt(5, radius);
        resultSet = preparedStatement.executeQuery();

        return buildPoliceStationList(resultSet);

    }

    private ResponseDTO buildPoliceStationList(ResultSet resultSet) throws SQLException {

        ResponseDTO resp = new ResponseDTO();
        List<PoliceStationDTO> list = new ArrayList<>();
        List<Integer> ids = new ArrayList<>();
        while (resultSet.next()) {
            if (resultSet.getDouble("distance") > 25) {
                continue;
            }
            int id = resultSet.getInt("policeStationID");
            ids.add(id);
            String name = resultSet.getString("name");
            double distance = resultSet.getDouble("distance");
            double lat = resultSet.getDouble("latitude");
            double lng = resultSet.getDouble("longitude");
            log.log(Level.INFO, "##### Police Station within radius, name: {0} - {1} - {2} - lat: {3} lng: {4}",
                    new Object[]{id, distance, name, lat, lng});
            PoliceStationDTO ps = new PoliceStationDTO();
            ps.setPoliceStationID(id);
            ps.setName(name.trim());
            ps.setDistance(distance);
            ps.setLatitude(lat);
            ps.setLongitude(lng);
            list.add(ps);
        }
        Query q = em.createNamedQuery("Officer.findByPoliceStationList", Officer.class);
        q.setParameter("list", ids);
        List<Officer> oList = q.getResultList();
        q = em.createNamedQuery("PanicIncident.findByPoliceStationList", PanicIncident.class);
        q.setParameter("list", ids);
        List<PanicIncident> iList = q.getResultList();
        for (Officer officer : oList) {
            for (PoliceStationDTO ps : list) {
                if (Objects.equals(ps.getPoliceStationID(), officer.getPoliceStation().getPoliceStationID())) {
                    ps.getOfficerList().add(new OfficerDTO(officer));
                }
            }

        }
        for (PanicIncident pi : iList) {
            for (PoliceStationDTO p : list) {
                if (Objects.equals(pi.getPoliceStation().getPoliceStationID(), p.getPoliceStationID())) {
                    p.getPanicIncidentList().add(new PanicIncidentDTO(pi));
                }
            }

        }

        log.log(Level.INFO, "---- PoliceStations found: {0}", list.size());

        resultSet.close();
        resp.setPoliceStations(list);
        resp.setMessage("Police Stations found: " + list.size());
        return resp;
    }

    public EntityManager getEm() {
        return em;
    }
    
    static final Logger log = Logger.getLogger(WorkerBee.class.getName());
}
