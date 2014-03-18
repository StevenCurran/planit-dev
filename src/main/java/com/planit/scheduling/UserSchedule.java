package com.planit.scheduling;

import com.planit.persistence.registration.User;
import com.planit.persistence.registration.UserRepository;
import com.planit.persistence.events.PlanitEvent;
import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Josh on 26/02/14.
 */

@Component
public class UserSchedule {
    private List<BlockVector> schedule;
    public long length;

    @Autowired
    private UserRepository userRepository;

    public UserSchedule(User u, DateTime startDate, DateTime endDate) {
        schedule = new LinkedList<BlockVector>();
        Duration duration = new Duration(startDate, endDate);
        length = duration.getStandardHours() * 2;

        System.out.println("JOSH-KUN, WE ARE NOW BUILDING A SCHEDULE FOR USER "+u.getProviderId());
        List<PlanitEvent> events = userRepository.findEventsForUser(u.getProviderId());

        /*
        List<PlanitEvent> events = new LinkedList<>();
        PlanitEvent e1 = new PlanitEvent();

        e1.setName("Event 1");
        DateTime sd = new DateTime(2012, 12, 12, 12, 00);
        DateTime ed = new DateTime(2012, 12, 12, 13, 00);
        e1.setStartDate(sd.toDate());
        e1.setEndDate(ed.toDate());
        e1.setPriority(2);
        events.add(e1);

        PlanitEvent e2 = new PlanitEvent();
        e2.setName("Event 2");
        sd = new DateTime(2012, 12, 12, 14, 00);
        ed = new DateTime(2012, 12, 12, 16, 00);
        e2.setStartDate(sd.toDate());
        e2.setEndDate(ed.toDate());
        e2.setPriority(3);
        events.add(e2);

        PlanitEvent e3 = new PlanitEvent();
        e3.setName("Event 3");
        sd = new DateTime(2012, 12, 12, 17, 00);
        ed = new DateTime(2012, 12, 12, 17, 30);
        e3.setStartDate(sd.toDate());
        e3.setEndDate(ed.toDate());
        e3.setPriority(4);
        events.add(e3);

        PlanitEvent e4 = new PlanitEvent();
        e4.setName("Event 2");
        sd = new DateTime(2012, 12, 12, 16, 00);
        ed = new DateTime(2012, 12, 12, 17, 00);
        e4.setStartDate(sd.toDate());
        e4.setEndDate(ed.toDate());
        e4.setPriority(5);
        events.add(e4);
        */

        // initialise vectors to zero
        for (int i = 0; i < length; i++) {
            schedule.add(new BlockVector());
        }

        // iterate through each event and add the relevant data to the relevant block vectors
        for (PlanitEvent event : events) {
            long eventDuration = event.getDurationInHalfHours();
            long offset = event.getStartTimeOffset(startDate);
            for (int i = 0; i < eventDuration; i++) {
                // here we build up the block matrix
                // preferenceScore = user.getUserPreferenceMatrix[day][halfHour][tag];
                int pos = (int) offset + i;
                // don't include this event if the offset is negative or longer than the duration of the window
                if(pos >= 0 && pos < length){
                    schedule.set(pos, new BlockVector(event.getPriority(), event.getNumberOfAttendees()));
                }
            }
        }
    }

    public void displaySchedule() {
        System.out.println("-----------------------");
        for (BlockVector bv : schedule) {
            for (int i = 0; i < bv.dimension; i++) {
                System.out.print(bv.getAtIndex(i) + " ");
            }
            System.out.println("");
            System.out.println("");
        }
        System.out.println("-----------------------");
    }

    public UserSchedule() {
        schedule = new LinkedList<BlockVector>();
    }

    public List<BlockVector> getScheduleWindow(int s0, int s1) {
        if ((s1 <= s0) || (s1 > schedule.size())) {
            return null;
        }

        List<BlockVector> l = new LinkedList<BlockVector>();
        for (int i = s0; i < s1; i++) {
            // maybe a list is not the best structure to be used here in terms of efficiency but this is something to be thought about later.
            l.add(this.schedule.get(i));
        }
        return l;
    }

}
