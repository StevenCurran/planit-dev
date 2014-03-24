package com.planit.mvc.events;

import com.google.android.gcm.server.Message;
import com.planit.gcm.GCMBean;
import com.planit.persistence.events.EventRepository;
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
import java.util.ArrayList;
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
    private EventRepository eventRepository;

    @Autowired
    private Scheduler scheduler;

    @Autowired
    private GCMBean gcmBean;

    @RequestMapping(method = RequestMethod.POST, value = "/planit")
    @ResponseBody
    public String planit(HttpServletRequest request) {
        String attendees = request.getParameter("attendees");
        String startDate = request.getParameter("startDate");
        String endDate = request.getParameter("endDate");
        String duration = request.getParameter("duration");
        String priority = request.getParameter("priority");


        List<String> attendeeList = Arrays.asList(attendees.split(","));
        List<User> users = new LinkedList<>();
        for (String attendee : attendeeList) {
            User a = userRepository.findOne(attendee);
            users.add(a);
        }

        String[] sd = startDate.split(",");
        String[] ed = endDate.split(",");


        DateTime pStartDate = new DateTime(Integer.parseInt(sd[0]), Integer.parseInt(sd[1]), Integer.parseInt(sd[2]), Integer.parseInt(sd[3]), Integer.parseInt(sd[4]));
        DateTime pEndDate = new DateTime(Integer.parseInt(ed[0]), Integer.parseInt(ed[1]), Integer.parseInt(ed[2]), Integer.parseInt(ed[3]), Integer.parseInt(ed[4]));

        int pDuration = Integer.parseInt(duration);
        int pPriority = Integer.parseInt(priority);

        return getBestDate(users, pStartDate, pEndDate, pDuration, pPriority);
    }

    public String getBestDate(List<User> attendees, DateTime startDate, DateTime endDate, int duration, int priority) {
        return scheduler.getBestDate(attendees, startDate, endDate, duration, priority).toDate().toString();
    }

    @RequestMapping(method = RequestMethod.POST, value = "/addevent")
    @ResponseBody
    public void addEvent(HttpServletRequest request) {
        String attendees = request.getParameter("attendees");
        String userid = request.getParameter("userid");
        String startDate = request.getParameter("startDate");
        String endDate = request.getParameter("endDate");
        String eventName = request.getParameter("eventname");

        List<String> deviceIds = new ArrayList<>();

        String[] people = attendees.split(",");
        for (String s : people) {
            User single = userRepository.findOne(s);
            if (single.getDeviceId() != null) {
                deviceIds.add(single.getDeviceId());
            }
        }

        User currentUser = userRepository.findOne(userid);
        //notificationRepository.save(new Notification());
        Message m = new Message.Builder().addData("message_type", "gcm").addData("data", currentUser.getFirstName() + " has been added to the event: " + eventName).addData("planit_message", "confirm").build();

        gcmBean.sendMessageToUsers(m, deviceIds);

    }

}
