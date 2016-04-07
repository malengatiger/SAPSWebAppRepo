/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.boha.vodacom.util;

import com.boha.vodacom.data.Citizen;
import com.boha.vodacom.data.City;
import com.boha.vodacom.data.GcmDevice;
import com.boha.vodacom.data.Officer;
import com.boha.vodacom.data.PanicIncident;
import com.boha.vodacom.data.PanicType;
import com.boha.vodacom.data.Photo;
import com.boha.vodacom.data.PoliceStation;
import com.boha.vodacom.data.Video;
import com.boha.vodacom.dto.CitizenDTO;
import com.boha.vodacom.dto.GcmDeviceDTO;
import com.boha.vodacom.dto.OfficerDTO;
import com.boha.vodacom.dto.PanicIncidentDTO;
import com.boha.vodacom.dto.PanicTypeDTO;
import com.boha.vodacom.dto.PhotoDTO;
import com.boha.vodacom.dto.PoliceStationDTO;
import com.boha.vodacom.dto.ResponseDTO;
import com.boha.vodacom.dto.SimpleMessageDTO;
import com.boha.vodacom.dto.VideoDTO;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author aubreymalabie
 */
@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class DataUtil {

    @PersistenceContext
    EntityManager em;

    public ResponseDTO signInOfficer(String email, String password, GcmDeviceDTO device) {
        ResponseDTO r = new ResponseDTO();
        try {
            Query q = em.createNamedQuery("Officer.signIn", Officer.class);
            q.setParameter("email", email);
            q.setParameter("password", password);
            Officer c = (Officer) q.getSingleResult();
            r.getOfficers().add(new OfficerDTO(c));
            if (device != null) {
                device.setOfficerID(c.getOfficerID());
                r.getDevices().add(addDevice(device, null, c));
            }
            r.setPanicTypes(getPanicTypeList().getPanicTypes());
            log.log(Level.INFO, "Officer signed in OK");
        } catch (NoResultException e) {
            r.setStatusCode(1);
            r.setMessage("Email or password is not valid");
        } catch (Exception e) {
            log.log(Level.SEVERE, "Failed signin", e);
        }

        return r;
    }

    public ResponseDTO signInCitizen(String email, String password, GcmDeviceDTO device) {
        ResponseDTO r = new ResponseDTO();
        try {
            Query q = em.createNamedQuery("Citizen.signIn", Citizen.class);
            q.setParameter("email", email);
            q.setParameter("password", password);
            Citizen c = (Citizen) q.getSingleResult();
            r.getCitizens().add(new CitizenDTO(c));
            if (device != null) {
                device.setCitizenID(c.getCitizenID());
                r.getDevices().add(addDevice(device, c, null));
            }
            r.setPanicTypes(getPanicTypeList().getPanicTypes());
            log.log(Level.INFO, "Citizen signed in OK");
        } catch (NoResultException e) {
            r.setStatusCode(1);
            r.setMessage("Email or password is not valid");
        } catch (Exception e) {
            log.log(Level.SEVERE, "Failed signin", e);
        }

        return r;
    }

    public ResponseDTO registerCitizen(CitizenDTO c) throws DataException {
        ResponseDTO r = new ResponseDTO();
        try {
            Citizen citizen = new Citizen();
            citizen.setEmail(c.getEmail());
            citizen.setName(c.getName());
            citizen.setDateRegistered(new Date());
            citizen.setCellphone(c.getCellphone());
            citizen.setGender(c.getGender());
            em.persist(citizen);
            em.flush();

            r.getCitizens().add(new CitizenDTO(citizen));
            if (!c.getDeviceList().isEmpty()) {
                GcmDeviceDTO x = c.getDeviceList().get(0);
                r.getDevices().add(addDevice(x, citizen, null));
            }
            r.setPanicTypes(getPanicTypeList().getPanicTypes());
            r.setMessage("Citizen registered OK");
            log.log(Level.INFO, "Citizen registered OK");
        } catch (NoResultException e) {
            r.setStatusCode(1);
            r.setMessage("Email or password is not valid");
        } catch (Exception e) {
            log.log(Level.SEVERE, "Failed signin", e);
            throw new DataException();
        }

        return r;
    }

    public ResponseDTO getPanicTypeList() throws DataException {
        ResponseDTO w = new ResponseDTO();
        try {
            Query q = em.createNamedQuery("PanicType.findAll",PanicType.class);
            List<PanicType> list = q.getResultList();
            for (PanicType pt : list) {
                w.getPanicTypes().add(new PanicTypeDTO(pt));
            }

        } catch (Exception e) {
            log.log(Level.SEVERE, "Failed", e);
        }

        return w;
    }

    public ResponseDTO registerOfficer(OfficerDTO c) throws DataException {
        ResponseDTO r = new ResponseDTO();
        try {
            Officer officer = new Officer();
            officer.setEmail(c.getEmail());
            officer.setName(c.getName());
            officer.setDateRegistered(new Date());
            officer.setCellphone(c.getCellphone());
            officer.setPassword(c.getPassword());
            officer.setGender(c.getGender());
            officer.setPoliceStation(em.find(PoliceStation.class, c.getPoliceStationID()));
            em.persist(officer);
            em.flush();

            r.getOfficers().add(new OfficerDTO(officer));
            if (!c.getDeviceList().isEmpty()) {
                GcmDeviceDTO x = c.getDeviceList().get(0);
                r.getDevices().add(addDevice(x, null, officer));
            }
            r.setPanicTypes(getPanicTypeList().getPanicTypes());
            r.setMessage("Officer registered OK");
            log.log(Level.INFO, "Officer registered OK");
        } catch (NoResultException e) {
            r.setStatusCode(1);
            r.setMessage("Email or password is not valid");
        } catch (Exception e) {
            log.log(Level.SEVERE, "Failed registration", e);
            throw new DataException();
        }

        return r;
    }

    private GcmDeviceDTO addDevice(GcmDeviceDTO x, Citizen c, Officer o) throws DataException {

        try {
            GcmDevice g = new GcmDevice();
            Query q = em.createNamedQuery("GcmDevice.findByAppSerialNumber", GcmDevice.class);
            q.setParameter("app", x.getApp());
            q.setParameter("serialNumber", x.getSerialNumber());
            List<GcmDevice> gList = q.getResultList();
            if (!gList.isEmpty()) {
                g = gList.get(0);
            }

            g.setAndroidVersion(x.getAndroidVersion());
            g.setApp(x.getApp());
            g.setCitizen(c);
            g.setOfficer(o);
            g.setDateRegistered(new Date());
            g.setManufacturer(x.getManufacturer());
            g.setModel(x.getModel());
            g.setSerialNumber(x.getSerialNumber());
            g.setRegistrationID(x.getRegistrationID());
            if (gList.isEmpty()) {
                em.persist(g);
                em.flush();
                log.log(Level.INFO, "Device added: {0}", g.getModel());
            } else {
                em.merge(g);
                log.log(Level.INFO, "Device updated: {0}", g.getModel());
            }
            SimpleMessageDTO m = new SimpleMessageDTO();
            m.addRegistrationID(g.getRegistrationID());
            m.setMessage("You have been successfully registered to Cloud messaging");
            GoogleCloudMessageUtil.sendMessage(em, m);
            return new GcmDeviceDTO(g);

        } catch (Exception e) {
            log.log(Level.SEVERE, "Failed", e);
            throw new DataException();
        }
    }

    public ResponseDTO addVideo(VideoDTO c) throws DataException {
        ResponseDTO r = new ResponseDTO();
        try {
            Video i = new Video();
            if (c.getCitizenID() != null) {
                i.setCitizen(em.find(Citizen.class, c.getCitizenID()));
            }
            if (c.getOfficerID() != null) {
                i.setOfficer(em.find(Officer.class, c.getOfficerID()));
            }
            if (c.getPanicIncidentID() != null) {
                i.setPanicIncident(em.find(PanicIncident.class, c.getPanicIncidentID()));
            }
            i.setDateTaken(new Date(c.getDateTaken()));
            i.setDateUploaded(new Date());
            i.setUrl(c.getUrl());
            em.persist(i);
            em.flush();

            r.getVideos().add(new VideoDTO(i));
            r.setMessage("Video metadata added OK");
            log.log(Level.INFO, "Video metadata added  OK");

        } catch (Exception e) {
            log.log(Level.SEVERE, "Failed video addition", e);
            throw new DataException();
        }
        return r;
    }

    public ResponseDTO addPhoto(PhotoDTO c) throws DataException {
        ResponseDTO r = new ResponseDTO();
        try {
            Photo i = new Photo();
            if (c.getCitizenID() != null) {
                i.setCitizen(em.find(Citizen.class, c.getCitizenID()));
            }
            if (c.getOfficerID() != null) {
                i.setOfficer(em.find(Officer.class, c.getOfficerID()));
            }
            if (c.getPanicIncidentID() != null) {
                i.setPanicIncident(em.find(PanicIncident.class, c.getPanicIncidentID()));
            }
            i.setDateTaken(new Date(c.getDateTaken()));
            i.setDateUploaded(new Date());
            i.setUrl(c.getUrl());
            em.persist(i);
            em.flush();

            r.getPhotos().add(new PhotoDTO(i));
            r.setMessage("Photo metadata added OK");
            log.log(Level.INFO, "Photo metadata added  OK");

        } catch (Exception e) {
            log.log(Level.SEVERE, "Failed photo addition", e);
            throw new DataException();
        }
        return r;
    }

    public ResponseDTO addPanicIncident(
            WorkerBee bee, PanicIncidentDTO c) throws DataException {
        ResponseDTO r = new ResponseDTO();
        try {
            PanicIncident i = new PanicIncident();
            if (c.getCitizenID() != null) {
                i.setCitizen(em.find(Citizen.class, c.getCitizenID()));
            }
            if (c.getOfficerID() != null) {
                i.setOfficer(em.find(Officer.class, c.getOfficerID()));
            }
            if (c.getPoliceStationID() != null) {
                i.setPoliceStation(em.find(PoliceStation.class, c.getPoliceStationID()));
            }
            i.setDateSent(new Date());
            i.setPanicType(em.find(PanicType.class, c.getPanicType().getPanicTypeID()));
            i.setLatitude(c.getLatitude());
            i.setLongitude(c.getLongitude());
            em.persist(i);
            em.flush();

            r.getIncidents().add(new PanicIncidentDTO(i));
            SimpleMessageDTO s = new SimpleMessageDTO();
            s.setIncident(r.getIncidents().get(0));
            GoogleCloudMessageUtil.sendIncident(bee, s);
            

            r.setMessage("PanicIncident registered OK");
            log.log(Level.INFO, "PanicIncident registered OK");

        } catch (Exception e) {
            log.log(Level.SEVERE, "Failed panic addition", e);
            throw new DataException();
        }
        return r;
    }

    public ResponseDTO updatePanicIncident(PanicIncidentDTO c) throws DataException {
        ResponseDTO r = new ResponseDTO();
        try {
            PanicIncident i = em.find(PanicIncident.class, c.getPanicIncidentID());
            i.setCitizen(em.find(Citizen.class, c.getCitizenID()));
            if (c.getOfficerID() != null) {
                i.setOfficer(em.find(Officer.class, c.getOfficerID()));
                i.setResponseDate(new Date());
            }
            if (c.getPoliceStationID() != null) {
                i.setPoliceStation(em.find(PoliceStation.class, c.getPoliceStationID()));
            }

            i.setPanicType(em.find(PanicType.class, c.getPanicType().getPanicTypeID()));
            em.merge(i);
            em.flush();
            r.getIncidents().add(new PanicIncidentDTO(i));
            r.setMessage("PanicIncident updated OK");
            log.log(Level.INFO, "PanicIncident updated OK");

        } catch (Exception e) {
            log.log(Level.SEVERE, "Failed update", e);
            throw new DataException();
        }
        return r;
    }

    public ResponseDTO addPoliceStation(PoliceStationDTO c) throws DataException {
        ResponseDTO r = new ResponseDTO();
        try {
            PoliceStation i = new PoliceStation();
            i.setName(c.getName());
            i.setLatitude(c.getLatitude());
            i.setLongitude(c.getLongitude());
            i.setAddress(c.getAddress());
            i.setCellphone(c.getCellphone());
            if (c.getCityID() != null) {
                i.setCity(em.find(City.class, c.getCityID()));
            }
            em.persist(i);
            em.flush();
            r.getPoliceStations().add(new PoliceStationDTO(i));
            r.setMessage("PoliceStation added OK: " + c.getName());
            log.log(Level.INFO, "PoliceStation added OK");

        } catch (Exception e) {
            log.log(Level.SEVERE, "Failed to add police station", e);
            throw new DataException();
        }
        return r;
    }

    public ResponseDTO updatePoliceStation(PoliceStationDTO c) throws DataException {
        ResponseDTO r = new ResponseDTO();
        try {
            PoliceStation i = em.find(PoliceStation.class, c.getPoliceStationID());
            i.setName(c.getName());
            i.setLatitude(c.getLatitude());
            i.setLongitude(c.getLongitude());
            i.setAddress(c.getAddress());
            i.setCellphone(c.getCellphone());
            if (c.getCityID() != null) {
                i.setCity(em.find(City.class, c.getCityID()));
            }
            em.merge(i);
            em.flush();
            r.getPoliceStations().add(new PoliceStationDTO(i));
            r.setMessage("PoliceStation updated OK: " + c.getName());
            log.log(Level.INFO, "PoliceStation updated OK");

        } catch (Exception e) {
            log.log(Level.SEVERE, "Failed to update police station", e);
            throw new DataException();
        }
        return r;
    }
    static final Logger log = Logger.getLogger(DataUtil.class.getSimpleName());
}
