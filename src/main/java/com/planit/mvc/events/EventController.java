package com.planit.mvc.events;

import com.planit.persistence.registration.User;
import com.planit.persistence.registration.UserRepository;
import com.planit.scheduling.Scheduler;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Josh & Stephen on 18/03/14.
 */
@Controller
@RequestMapping("/events")
public class EventController {


    @Autowired
    private UserRepository userRepository;

    @Autowired
    private Scheduler scheduler;

    public static String getBody(HttpServletRequest request) throws IOException {

        String body = null;
        StringBuilder stringBuilder = new StringBuilder();
        BufferedReader bufferedReader = null;

        try {
            InputStream inputStream = request.getInputStream();
            if (inputStream != null) {
                bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                char[] charBuffer = new char[128];
                int bytesRead = -1;
                while ((bytesRead = bufferedReader.read(charBuffer)) > 0) {
                    stringBuilder.append(charBuffer, 0, bytesRead);
                }
            } else {
                stringBuilder.append("");
            }
        } catch (IOException ex) {
            throw ex;
        } finally {
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (IOException ex) {
                    throw ex;
                }
            }
        }

        body = stringBuilder.toString();
        return body;
    }

    @RequestMapping(method = RequestMethod.POST, value = "/planit")
    @ResponseBody
    public String planit(HttpServletRequest request) {
        System.out.println("===================== HELLO ENDPOINT GOT HIT =======================");
        String attendees = request.getParameter("attendees");
        String startDate = request.getParameter("startDate");
        String endDate = request.getParameter("endDate");
        String duration = request.getParameter("duration");
        String priority = request.getParameter("priority");


        List<String> attendeeList = Arrays.asList(attendees.split(","));
        System.out.println("%%%%%%%%%%%%%%%%%%%%%%%%%% attendee list looks like this: " + attendeeList);
        List<User> users = new LinkedList<>();
        System.out.println("going to go through the attendee id list:");
        for (String attendee : attendeeList) {
            System.out.println("pulling out attendee from db with id = " + attendee);
            User a = userRepository.findOne(attendee);
            System.out.println("we got a provider id of " + a.getProviderId());
            users.add(a);
        }

        String[] sd = startDate.split(",");
        String[] ed = endDate.split(",");

        DateTime pStartDate = new DateTime(Integer.parseInt(sd[0]), Integer.parseInt(sd[1]), Integer.parseInt(sd[2]), Integer.parseInt(sd[3]), Integer.parseInt(sd[4]));
        DateTime pEndDate = new DateTime(Integer.parseInt(ed[0]), Integer.parseInt(ed[1]), Integer.parseInt(ed[2]), Integer.parseInt(ed[3]), Integer.parseInt(ed[4]));

        int pDuration = Integer.parseInt(duration);
        int pPriority = Integer.parseInt(priority);

        String bestDate = getBestDate(users, pStartDate, pEndDate, pDuration, pPriority);
        return bestDate;
    }

    public String getBestDate(List<User> attendees, DateTime startDate, DateTime endDate, int duration, int priority) {

        StringBuilder response = new StringBuilder();
        DateTime bestDate = scheduler.getBestDate(attendees, startDate, endDate, duration, priority);
        response.append(bestDate);
        return response.toString();
    }

}
