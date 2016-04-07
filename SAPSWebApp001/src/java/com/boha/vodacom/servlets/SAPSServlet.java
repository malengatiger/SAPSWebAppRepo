/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.boha.vodacom.servlets;

import com.boha.vodacom.dto.RequestDTO;
import com.boha.vodacom.dto.ResponseDTO;
import com.boha.vodacom.util.DataUtil;
import com.boha.vodacom.util.GoogleCloudMessageUtil;
import com.boha.vodacom.util.GoogleCloudMessagingRegistrar;
import com.boha.vodacom.util.ListUtil;
import com.boha.vodacom.util.PanicWorkerBee;
import com.boha.vodacom.util.PoliceStationLoader;
import com.boha.vodacom.util.WorkerBee;
import com.google.gson.Gson;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author aubreymalabie
 */
@WebServlet(name = "SAPSServlet", urlPatterns = {"/main"})
public class SAPSServlet extends HttpServlet {

    @EJB
    DataUtil dataUtil;
    @EJB
    ListUtil listUtil;
    @EJB
    WorkerBee workerBee;
    @EJB
    PoliceStationLoader stationLoader;
    @EJB
    PanicWorkerBee panicWorkerBee;

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        long start = System.currentTimeMillis();
        ResponseDTO resp = new ResponseDTO();
        RequestDTO req = getRequest(request);

        try {
            switch (req.getRequestType()) {
                case RequestDTO.SEND_SIMPLE_MESSAGE:
                    resp = GoogleCloudMessageUtil.sendMessage(workerBee.getEm(), req.getSimpleMessage());
                    break;
                case RequestDTO.SEND_GCM_REGISTRATION:
                    resp = GoogleCloudMessagingRegistrar.sendGCMRegistration(req.getRegistrationID());
                    break;
                case RequestDTO.GET_INCIDENTS_BY_STATION:
                    resp = listUtil.getIncidentListByPoliceStation(req.getPoliceStationID());
                    break;
                case RequestDTO.GET_INCIDENTS_BY_OFFICER:
                    resp = listUtil.getIncidentListByOfficer(req.getOfficerID());
                    break;
                case RequestDTO.FIND_INCIDENTS_IN_RADIUS:
                    resp = panicWorkerBee.getIncidentsWithinRadius(
                            req.getLatitude(), req.getLongitude(),
                            req.getRadius(), PanicWorkerBee.KILOMETRES);
                    break;
                case RequestDTO.ADD_INCIDENT:
                    resp = dataUtil.addPanicIncident(workerBee,req.getIncident());
                    break;
                case RequestDTO.RESPOND_INCIDENT:
                    resp = dataUtil.updatePanicIncident(req.getIncident());
                    break;
                case RequestDTO.FIND_POLICE_STATIONS_IN_RADIUS:
                    resp = workerBee.getPoliceStationsWithinRadius(
                            req.getLatitude(), req.getLongitude(),
                            req.getRadius(), WorkerBee.KILOMETRES);
                    break;
                case RequestDTO.REGISTER_CITIZEN:
                    resp = dataUtil.registerCitizen(req.getCitizen());
                    break;
                case RequestDTO.REGISTER_OFFICER:
                    resp = dataUtil.registerOfficer(req.getOfficer());
                    break;
                case RequestDTO.REGISTER_POLICE_STATION:
                    resp = dataUtil.addPoliceStation(req.getPoliceStation());
                    break;
                case RequestDTO.SIGN_IN_CITIZEN:
                    resp = dataUtil.signInCitizen(req.getEmail(),
                            req.getPassword(), req.getGcmDevice());
                    break;
                case RequestDTO.SIGN_IN_OFFICER:
                    resp = dataUtil.signInOfficer(req.getEmail(),
                            req.getPassword(), req.getGcmDevice());
                    break;
                case RequestDTO.LOAD_POLICE_STATIONS:
                    stationLoader.load();
                    break;
                    case RequestDTO.GENERATE_DATA:
                    stationLoader.generateData();
                    break;
            }
        } catch (Exception e) {
            log.log(Level.SEVERE, "Shit happened!",e);
            resp.setStatusCode(9);
            resp.setMessage("Server has run into a bit of a problem");

        } finally {
            String json = gson.toJson(resp);
            log.log(Level.OFF, "JSON returned: {0}", json.length());
            response.setContentType("application/json;charset=UTF-8");
            try (PrintWriter out = response.getWriter()) {
                out.println(json);
            }
        }

        long end = System.currentTimeMillis();
        log.log(Level.INFO, "---> SAPSServlet completed in {0} seconds",
                getElapsed(start, end));

    }

    public static double getElapsed(long start, long end) {
        BigDecimal m = new BigDecimal(end - start).divide(new BigDecimal(1000));
        return m.doubleValue();
    }

    private RequestDTO getRequest(HttpServletRequest req) {

        String json = req.getParameter("JSON");
        log.log(Level.OFF, "....incoming JSON = {0}", json);
        RequestDTO cr = new RequestDTO();
        try {
            cr = gson.fromJson(json, RequestDTO.class);
        } catch (Exception e) {
            log.log(Level.SEVERE, "Failed JSON", e);
        }
        return cr;
    }
    static final Gson gson = new Gson();

    private static final Logger log = Logger.getLogger(SAPSServlet.class
            .getSimpleName());

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
