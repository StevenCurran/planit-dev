package com.planit.mvc.google;

import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.CalendarScopes;
import com.plaint.domainobjs.Person;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeRequestUrl;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Collection;


/**
 * Created by Steven on 15/02/14.
 */
@RestController
@RequestMapping("/googlelogin")
public class GoogleControllerNew {

    private final Log LOG = LogFactory.getLog(GoogleControllerNew.class);

    private static final String CLIENT_ID = "115023261213-vdpubj7qf78pu1jbk85t3pbt329fu4vv.apps.googleusercontent.com";
    private static final String CLIENT_SECRET = "-D-uoU496-0C868uId3NhlW4";

    private static final String CALLBACK_URI = "http://localhost:8080/googlelogin/oauthCallback";
    //private static final String CALLBACK_URI = "http://planit-dev.herokuapp.com/googlelogin/oauthCallback";

    private static Collection<String> SCOPE = Arrays.asList("https://www.googleapis.com/auth/userinfo.profile;https://www.googleapis.com/auth/userinfo.email;https://www.googleapis.com/auth/calendar".split(";"));
    private static final String USER_INFO_URL = "https://www.googleapis.com/oauth2/v1/userinfo";
    private static final JsonFactory JSON_FACTORY = new JacksonFactory();
    private static final HttpTransport HTTP_TRANSPORT = new NetHttpTransport();

    private String stateToken;
    private GoogleAuthorizationCodeFlow flow;


    @RequestMapping(method = RequestMethod.GET, value = "/login")
    public String homeMethod(){
        flow = new GoogleAuthorizationCodeFlow.Builder(HTTP_TRANSPORT,JSON_FACTORY, CLIENT_ID, CLIENT_SECRET, SCOPE).build();

        generateStateToken();

        return "redirect:/" + buildLoginUrl();
    }

    @RequestMapping(method = RequestMethod.GET, value = "/oauthCallback")
    public String callbackSuccess(HttpServletRequest request,HttpServletResponse response){
        //session.setAttribute("state", helper.getStateToken());
        String attribute = (String) request.getParameter("code");
        String jsonResp = "";
        try {
            jsonResp = getUserInfoJson(attribute);
            LOG.info(jsonResp);
            LOG.debug(jsonResp);
        } catch (IOException e) {
            e.printStackTrace();
        }

        Person p = new Person();
        p.setFirstName("Steven");
        return jsonResp;
    }


    public String buildLoginUrl() {

        final GoogleAuthorizationCodeRequestUrl url = flow.newAuthorizationUrl();

        return url.setRedirectUri(CALLBACK_URI).setState(stateToken).build();
    }

    /**
     * Generates a secure state token
     */
    private void generateStateToken(){

        SecureRandom sr1 = new SecureRandom();

        stateToken = "google;"+sr1.nextInt();

    }

    /**
     * Accessor for state token
     */
    public String getStateToken(){
        return stateToken;
    }

    /**
     * Expects an Authentication Code, and makes an authenticated request for the user's profile information
     * @return JSON formatted user profile information
     * @param authCode authentication code provided by google
     */
    public String getUserInfoJson(final String authCode) throws IOException {

        final GoogleTokenResponse response = flow.newTokenRequest(authCode).setRedirectUri(CALLBACK_URI).execute();
        final Credential credential = flow.createAndStoreCredential(response, null);
        final HttpRequestFactory requestFactory = HTTP_TRANSPORT.createRequestFactory(credential);
        // Make an authenticated request
        final GenericUrl url = new GenericUrl(USER_INFO_URL);
        final HttpRequest request = requestFactory.buildGetRequest(url);
        request.getHeaders().setContentType("application/json");
        final String jsonIdentity = request.execute().parseAsString();
        return jsonIdentity;

    }


}
