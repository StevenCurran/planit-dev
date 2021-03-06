package com.planit.mvc.events;

import com.google.android.gcm.server.Message;
import com.planit.gcm.GCMBean;
import com.planit.persistence.events.EventRepository;
import com.planit.persistence.events.PlanitEvent;
import com.planit.persistence.registration.User;
import com.planit.persistence.registration.UserRepository;
import com.planit.scheduling.DateTimeWithConflicts;
import com.planit.scheduling.Scheduler;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.sound.midi.Soundbank;
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
        StringBuilder response = new StringBuilder();
        DateTimeWithConflicts bestDatewc = scheduler.getBestDate(attendees, startDate, endDate, duration, priority);

        response.append(bestDatewc.getDateTime().minusHours(1).toDate().getTime() + ",");
        String prefix = "";
        for (String s : bestDatewc.getConflicts()) {
            response.append(prefix + s);
            prefix = "|";
        }

        return response.toString();
    }

    @RequestMapping(method = RequestMethod.POST, value = "/getConflicts")
    @ResponseBody
    public String getConflicts(HttpServletRequest request) {
        String userId = request.getParameter("userid");
        String eventId = request.getParameter("eventid");

        User u = userRepository.findOne(userId);
        PlanitEvent e = eventRepository.findOne(eventId);

        List<String> conflicts = scheduler.getConflictingEvents(u, e);

        String prefix = "";

        StringBuilder response = new StringBuilder();
        for (String s : conflicts) {
            response.append(prefix + s);
            prefix = "|";
        }

        return response.toString();
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
        Message m = new Message.Builder().addData("message_type", "gcm").addData("data", currentUser.getFirstName() + " has added you to an event!: " + eventName).addData("planit_message", "confirm").build();
        //add in the events

        gcmBean.sendMessageToUsers(m, deviceIds);

    }

    @RequestMapping(method = RequestMethod.GET, value = "/getevent")
    @ResponseBody
    public PlanitEvent getEvents(HttpServletRequest request) {
        String eventId = request.getParameter("eventid");
        return eventRepository.findOne(eventId);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/getpendingevents")
    @ResponseBody
    public List<PlanitEventWithConflicts> getPendingEvents(HttpServletRequest request) {
        String userid = request.getParameter("userid");
        ArrayList<PlanitEvent> pendingEventsForUser = userRepository.findPendingEventsForUser(userid);
        User u = userRepository.findOne(userid);

        List<PlanitEventWithConflicts> returnEvents = new ArrayList<>();
        //List<PlanitEvent> returnEvents = new ArrayList<>();

        System.out.println("Pending events size:" + pendingEventsForUser.size());
        for (PlanitEvent planitEvent : pendingEventsForUser) {
            PlanitEventWithConflicts pc = new PlanitEventWithConflicts(planitEvent);
            List<String> conflicts = scheduler.getConflictingEvents(u, planitEvent);
            System.out.println("Title: " + planitEvent.getName() + " Has conflicts: " + conflicts.size());
            Iterable<PlanitEvent> all = eventRepository.findAll(conflicts);
            for(PlanitEvent p : all)
            {
                pc.addConflict(p);
            }
            System.out.println("Size is now: " + pc.getConflictingEvents().size());
            returnEvents.add(pc);
        }


        return returnEvents;
    }
}