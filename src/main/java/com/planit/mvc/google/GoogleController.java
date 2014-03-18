package com.planit.mvc.google;

import com.ProjectUtils;
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
import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.model.CalendarList;
import com.google.api.services.calendar.model.CalendarListEntry;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.Events;
import com.google.api.services.plus.Plus;
import com.google.api.services.plus.model.Person;
import com.planit.gcm.GCMBean;
import com.planit.persistence.registration.User;
import com.planit.persistence.registration.UserRepository;
import com.planit.persistence.events.EventRepository;
import com.planit.persistence.events.PlanitEvent;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.jdt.internal.compiler.lookup.SourceTypeBinding;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.SystemEnvironmentPropertySource;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.SecureRandom;
import java.util.*;


/**
 * Created by Steven on 15/02/14.
 */
@Controller
@RequestMapping("/googlelogin")
public class GoogleController {

    private static final String CLIENT_ID = "115023261213-vdpubj7qf78pu1jbk85t3pbt329fu4vv.apps.googleusercontent.com";
    private static final String CLIENT_SECRET = "-D-uoU496-0C868uId3NhlW4";
    private static final String USER_INFO_URL = "https://www.googleapis.com/oauth2/v1/userinfo";
    private static final JsonFactory JSON_FACTORY = new JacksonFactory();
    private static final HttpTransport HTTP_TRANSPORT = new NetHttpTransport();
    private static String CALLBACK_URI = "/oauthCallback";
    private static Collection<String> SCOPE = Arrays.asList("https://www.googleapis.com/auth/userinfo.profile;https://www.googleapis.com/auth/userinfo.email;https://www.googleapis.com/auth/calendar".split(";"));
    private final Log LOG = LogFactory.getLog(GoogleController.class);
    @Autowired
    private ThreadPoolTaskExecutor taskExecutor;
    private Calendar calendarClient;

    private String stateToken;
    private GoogleAuthorizationCodeFlow flow;
    private String authToken = "";
    private GoogleTokenResponse responseVar = null;
    private Credential credential = null;
    private User person = null;


    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private GCMBean gcmService;

    @RequestMapping(method = RequestMethod.GET, value = "/login")
    public String homeMethod() {
        CALLBACK_URI = ProjectUtils.getBaseUrl() + "/googlelogin/oauthCallback";
        flow = new GoogleAuthorizationCodeFlow.Builder(HTTP_TRANSPORT, JSON_FACTORY, CLIENT_ID, CLIENT_SECRET, SCOPE).build();

        generateStateToken();

        return "redirect:" + buildLoginUrl();

    }

    @RequestMapping(method = RequestMethod.GET, value = "/oauthCallback")
    @ResponseBody
    public Person callbackSuccess(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Cookie[] cookies = request.getCookies();
        if(cookies.length > 0){
            for (Cookie cookie : cookies) {
                System.out.println(cookie.getName());
                System.out.println(cookie.getValue());
            }

        }

        response.addHeader("authToken", request.getParameter("code"));
        response.addHeader("loginCookie", request.getCookies()[0].getName() + ":" + request.getCookies()[0].getValue()); // we may need this...
        this.authToken = request.getParameter("code");

        responseVar = flow.newTokenRequest(this.authToken).setRedirectUri(CALLBACK_URI).execute();
        credential = flow.createAndStoreCredential(responseVar, null);
        Plus plus = new Plus(HTTP_TRANSPORT, JSON_FACTORY, credential);
        Person profile = plus.people().get("me").execute();
        this.person = new User(profile);

        if (userRepository.findOne(profile.getId()) != null) {
            response.addHeader("valid_user", "true");
            return profile;
        }
        userRepository.save(this.person);

        return profile;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/gcalEvents")
    @ResponseBody
    public List<PlanitEvent> getGcalEvents() throws IOException {
        String jsonResp = "";
        CalendarList feed = null;
        List<PlanitEvent> events = new ArrayList<>();


        //perform some setup of the calendar information.
        //   GoogleTokenResponse responseVar = flow.newTokenRequest(this.authToken).setRedirectUri(CALLBACK_URI).execute();
        //   Credential credential = flow.createAndStoreCredential(responseVar, null);
        calendarClient = new Calendar.Builder(HTTP_TRANSPORT, JSON_FACTORY, credential).build();


        feed = calendarClient.calendarList().list().execute();


        java.util.Calendar cal = java.util.Calendar.getInstance();
        cal.add(java.util.Calendar.DATE, -7);
        Date today = cal.getTime();
        cal.add(java.util.Calendar.DATE, 30);


        for (CalendarListEntry entry : feed.getItems()) { //This line is for multiple calendars. ie the acls with different colours.

            Events eventsPerCalendar = calendarClient.events().list(entry.getId()).setSingleEvents(true).setTimeMin(new DateTime(today)).setTimeMax(new DateTime(cal.getTime())).execute();
            List<Event> items = eventsPerCalendar.getItems();
            for (Event e : items) {
                if (e.getStart() == null) {
                    continue;
                }
                if (e.getStart().getDateTime() == null) {
                    continue;
                }

                events.add(new PlanitEvent(e, this.person));
            }

        }

        taskExecutor.execute(new EventPersistenceTask(this.person, events, eventRepository));

        return events;


    }

    @RequestMapping(method = RequestMethod.POST, value = "/addEvent")
    public void addEvent(HttpServletRequest request) {
        String date = request.getHeader("date");
        String time = request.getHeader("time");
        //eventRepository.save(event);


        System.out.println("Event saved");
    }

    @RequestMapping(method = RequestMethod.GET, value = "/gcm")
    public void testGcmEvent() {
        gcmService.send(this.person.getFirstName() + " " + this.person.getLastName());
    }

    @RequestMapping(method = RequestMethod.GET, value = "/getAttendees")
    @ResponseBody
    public List<User> getAttendees() {
        return userRepository.findAll();
    }


    @RequestMapping(method = RequestMethod.POST, value = "/deviceregistration")
    @ResponseBody
    public void registerDevice(HttpServletRequest request) {
        String providerId = request.getHeader("providerid");
        String deviceId = request.getHeader("deviceid");

        User one = userRepository.findOne(providerId);
        if(one != null){
            one.setDeviceId(deviceId);
            userRepository.save(one);
        }
        gcmService.sendRegConfirm(this.person.getFirstName() + " " + this.person.getLastName(), deviceId);
    }


    public String buildLoginUrl() {

        final GoogleAuthorizationCodeRequestUrl url = flow.newAuthorizationUrl();

        return url.setRedirectUri(CALLBACK_URI).setState(stateToken).build();
    }

    /**
     * Generates a secure state token
     */
    private void generateStateToken() {

        SecureRandom sr1 = new SecureRandom();

        stateToken = "google;" + sr1.nextInt();

    }

    /**
     * Accessor for state token
     */
    public String getStateToken() {
        return stateToken;
    }

    /**
     * Expects an Authentication Code, and makes an authenticated request for the user's profile information
     *
     * @param authCode authentication code provided by google
     * @return JSON formatted user profile information
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
