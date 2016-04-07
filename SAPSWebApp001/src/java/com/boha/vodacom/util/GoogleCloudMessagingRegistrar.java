/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.boha.vodacom.util;

import com.boha.vodacom.dto.ResponseDTO;
import com.google.android.gcm.server.Constants;
import com.google.android.gcm.server.Message;
import com.google.android.gcm.server.Result;
import com.google.android.gcm.server.Sender;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Manage registration of mobile devices with Google Cloud Messaging servers
 *
 * @author Aubrey Malabie
 */
public class GoogleCloudMessagingRegistrar {

    static final SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM yyyy HH:MM");

    /**
     * Register a device to Google Cloud Message
     * @param regID
     * @return
     * @throws IOException 
     */
    public static ResponseDTO sendGCMRegistration(String regID) throws IOException {
        logger.log(Level.INFO,
                "#### sendGCMRegistration starting comms with Google servers...."
                + "sending registration of mobile device\n{0}", regID);
        ResponseDTO resp = new ResponseDTO();
        resp.setRegistrationID(regID);
        Sender sender = new Sender(API_KEY);
        Message message = new Message.Builder().addData(
                "message",
                "SAPS app registered to send and receive messages. Completed on "
                + sdf.format(new Date())).build();

        Result result = sender.send(message, regID, 5);
        logger.log(
                Level.INFO,
                "#### SAPS GCM Registration result, registrationID: {0} messageID: {1} errorCodeName: {2}",
                new Object[]{result.getCanonicalRegistrationId(),
                    result.getMessageId(), result.getErrorCodeName()});

        String error = result.getErrorCodeName();

        if (error != null && !error.equalsIgnoreCase("")) {
            if (error.equals(Constants.ERROR_NOT_REGISTERED)) {
                resp.setStatusCode(8);
                resp.setMessage("GCM device not registered. Error from GCM server");
                error = resp.getMessage();
                logger.log(Level.INFO,
                        "#### Google Cloud Messaging device not registered");
            }
            if (error.equals(Constants.ERROR_UNAVAILABLE)) {
                resp.setStatusCode(7);
                resp.setMessage("Google Cloud Messaging  servers not available");
                error = resp.getMessage();
                logger.log(Level.INFO, "#### GCM servers not available");
            }
            StringBuilder sb = new StringBuilder();
            sb.append("Google Cloud Messaging device registration failed.\n");
            sb.append("Error Code Name: ").append(error);

//            addErrorStore(em,StatusCode.ERROR_GCM, sb.toString());
        } else {
            // we have SUCCESS!!
            resp.setStatusCode(0);
            resp.setMessage("Google Cloud Messaging device registered to Google successfully");
            logger.log(Level.INFO, "#### Cloud messaging registration & server update - OK, id: {0}",
                    regID);
            StringBuilder sb = new StringBuilder();
            sb.append(resp.getMessage()).append("\n");
            sb.append("This device can now participate in push messaging");
            logger.log(Level.INFO, sb.toString());
            return resp;
        }

        // check canonical reg id
        if (result.getMessageId() != null) {
            String canonicalRegId = result.getCanonicalRegistrationId();
            if (canonicalRegId != null) {
                logger.log(Level.INFO, "### Canonical registration id found, will update  gcmDevices ???...");
                resp.setRegistrationID(result.getCanonicalRegistrationId());

            }
        }

        resp.setMessage("Device has been registered at Google GCM servers");
        return resp;
    }

    private static final Logger logger = Logger.getLogger(GoogleCloudMessagingRegistrar.class
            .getCanonicalName());

    public static final String API_KEY = "AIzaSyDM7YGrEcxzwvSE0Ofg7tEE4cqRvMGFpP0";
    public static final String PROJECT_NUMBER = "630759841968";
            
     
}
