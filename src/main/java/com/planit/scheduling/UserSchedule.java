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
        System.out.println("trying to break userRepository %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");
        System.out.println(userRepository.toString());
        List<PlanitEvent> events = userRepository.findEventsForUser(u.getProviderId());



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
