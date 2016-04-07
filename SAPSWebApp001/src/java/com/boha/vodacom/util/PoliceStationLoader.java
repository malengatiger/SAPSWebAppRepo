/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.boha.vodacom.util;

import com.boha.vodacom.data.Citizen;
import com.boha.vodacom.data.Officer;
import com.boha.vodacom.data.PanicIncident;
import com.boha.vodacom.data.PanicType;
import com.boha.vodacom.data.PoliceStation;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import nl.knaw.dans.common.dbflib.CorruptedTableException;
import nl.knaw.dans.common.dbflib.IfNonExistent;
import nl.knaw.dans.common.dbflib.Record;
import nl.knaw.dans.common.dbflib.Table;
import org.joda.time.DateTime;

/**
 *
 * @author aubreymalabie
 */
@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class PoliceStationLoader {

    @PersistenceContext
    EntityManager em;

    public void load() throws IOException {
        File file = new File("/work/saps/station_points/Police_points.dbf");
        if (!file.exists()) {
            file = new File("/opt/saps/station_points/Police_points.dbf");
        }
        if (!file.exists()) {
            throw new IOException("Shape File DBF Not found");
        }
        log.log(Level.INFO, "*** Police_Points DBF file is: {0}", file.getAbsolutePath());
        if (!file.exists()) {
            throw new IOException();
        }
        Table table = new Table(file);
        try {
            table.open(IfNonExistent.ERROR);
            Iterator<Record> iterator = table.recordIterator();
            int count = 0;
            while (iterator.hasNext()) {
                Record record = (Record) iterator.next();
                addPoliceStation(record);
                count++;
            }
            em.flush();
            System.out.println("############# POLICE STATIONS added to database: " + count + " ###################");
        } catch (IOException | CorruptedTableException e) {
            throw new IOException();
        } catch (DataException ex) {
            Logger.getLogger(PoliceStationLoader.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            table.close();
        }

        log.log(Level.INFO, "============================> DBF read & process completed! OK!\n\n\n");
    }

    private void addPoliceStation(Record rec) throws DataException {
        PoliceStation ps = new PoliceStation();
        ps.setName(rec.getStringValue("COMPNT_NM"));
        ps.setLatitude((Double) rec.getNumberValue("LOCATION_Y"));
        ps.setLongitude((Double) rec.getNumberValue("LOCATION_X"));
        try {
            em.persist(ps);
            log.log(Level.INFO, "### Police Station added:{0} latitude: {1} longitude: {2}",
                    new Object[]{ps.getName(), ps.getLatitude(), ps.getLongitude()});
        } catch (Exception e) {
            log.log(Level.SEVERE, "Failed", e);
            throw new DataException();
        }
    }
    static Random random = new Random(System.currentTimeMillis());

    public void generateData() {
        Query n = em.createNamedQuery("PanicType.findAll", PanicType.class);
        List<PanicType> panicList = n.getResultList();

        Query q = em.createNamedQuery("PoliceStation.findAll", PoliceStation.class);
        List<PoliceStation> psList = q.getResultList();
        for (PoliceStation ps : psList) {
            int count = random.nextInt(20);
            if (count < 10) {
                count = 10;
            }
            for (int i = 0; i < count; i++) {
                Citizen c = new Citizen();
                String name = firstNames[random.nextInt(firstNames.length - 1)];
                String lname = lastNames[random.nextInt(lastNames.length - 1)];
                c.setName(name + " " + lname);
                c.setEmail(name + lname + "@gmail.com");
                c.setCellphone("099 999 9999");
                c.setDateRegistered(new Date());
                em.persist(c);
                em.flush();
                log.log(Level.INFO, "######### Citizen: {0} added", c.getName());
                int count2 = random.nextInt(8);
                if (count2 < 2) {
                    count2 = 4;
                }
                for (int j = 0; j < count2; j++) {
                    PanicIncident inc = new PanicIncident();
                    inc.setCitizen(c);
                    inc.setDateSent(getDate());
                    int k = random.nextInt(panicList.size() - 1);
                    inc.setPanicType(panicList.get(k));
                    inc.setPoliceStation(ps);
                    //add lat long
                    inc.setLatitude(getAdjustedPoint(ps.getLatitude()));
                    inc.setLongitude(getAdjustedPoint(ps.getLongitude()));
                    
                    em.persist(inc);
                    log.log(Level.INFO, "+++++++ Incident: {0} added", inc.getPanicType().getName());
                }
                
            }
            int count3 = random.nextInt(15);
                if (count3 < 5) {
                    count3 = 5;
                }
                for (int m = 0; m < count3; m++) {
                    Officer o = new Officer();
                    String name2 = firstNames[random.nextInt(firstNames.length - 1)];
                    String lname2 = lastNames[random.nextInt(lastNames.length - 1)];
                    o.setName(name2 + " " + lname2);
                    o.setEmail(name2 + lname2 + "@gmail.com");
                    o.setCellphone("099 999 9999");
                    o.setDateRegistered(new Date());
                    em.persist(o);
                    log.log(Level.INFO, "****** Officer: {0} added", o.getName());
                }

        }
    }

    private double getAdjustedPoint(double l) {
        double d = random.nextInt(200) * 0.0156; 
        int y = random.nextInt(10);
        if (y > 5) {
            return l + d;
        } else {
            return l - d;
        }
        
    }
    private Date getDate() {
        DateTime dt = DateTime.now();
        int days = random.nextInt(6);
        DateTime then = dt.minusDays(days);
        return then.toDate();
    }

    private String[] firstNames = {"Ayanda", "Ayibongwe", "Banele", "Basetsana", "Joseph",
        "Bongani", "Buyisiwe", "Dikgang", "Dikeledi", "Dipalesa", "Fhulufhuwani",
        "Gugu", "Kagiso", "Khauhelo", "Kwanele", "Lebohang", "Lerato", "Maletsatsi",
        "Mbali", "Moeketsi", "Marianne", "Ivy", "Jane", "Piet", "John", "David", "Frank",
        "Nana", "Joy", "Letlotlo", "Kgabi", "Portia", "Beauty", "Catherine", "Tiger", "Malenga",
        "Gary", "Ben", "Aubrey", "Vincent", "Mark","Marcus", "Themba", "Thabo", "Ntemi",
        "Mmaphefo", "Tshidi", "Matshidiso", "Mmakau","Mmaletsatsi", "Dorcas", "Lerato", "Clive",
        "Veronica", "Sammy", "Nancy", "Gloria", "Shabane", "Obakeng","Sipho","Hans", "Lawrence",
        "Thomas", "Benjamin", "Kokosi", "Maria", "Samuel", "Peter", "Petros", "James", "Banele"
    };
    private String[] lastNames = {"Maluleke", "Samuelson", "Mbiza", "Nkosi", "Baker",
        "Mabuza", "Sono", "Khuzwayo", "Mathebula", "Mthembu", "Hlongwane", "Peters",
        "Brown", "Smith", "Swanepoel", "Mhinga", "Harris", "Fonseca", "Matjila",
        "Tsotetsi", "Fakude", "Mkhabela", "Nyoka", "Ringane", "Masango", "Patel", "Brinks",
        "Paulson", "Hlabathi", "Renken", "Hamalala",
        "Naidoo", "Miller", "Malebo", "Ntini", "Baloyi", "Mabaso", "Zulu", "Xaba", "Tau",
        "Sithole", "Maringa", "Bhengu", "Mantashe", "Nkuna", "Chauke", "Skosana", "Matlhoko",
        "Kobue", "Matjeni", "Briggs", "Jones", "Smith", "Venter", "du Preez", "Vermeulen"
    };

    static final Logger log = Logger.getLogger(PoliceStationLoader.class.getSimpleName());
}
