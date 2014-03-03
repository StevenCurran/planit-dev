package com.planit.scheduling;

import com.planit.persistence.registration.User;
import com.planit.persistence.registration.UserRepository;
import com.planit.persistence.registration.events.PlanitEvent;
import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

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
        long numHalfHoursInWindow = duration.getStandardHours() * 2;
        length = numHalfHoursInWindow;

        List<PlanitEvent> events = userRepository.findEventsForUser(u.getProviderId());

        // remove dates that are not in the window

        // initialise vectors to zero
        for (int i = 0; i < numHalfHoursInWindow; i++)
        {
            schedule.add(new BlockVector());
        }

        // iterate through each event and add the relevant data to the relevant block vectors

        for (PlanitEvent event : events)
        {
            long eventDuration = event.getDurationInHours()*2;
            long offset = event.getStartTimeOffset(startDate)*2;

            for (int i = 0; i < eventDuration; i++)
            {
                schedule.set((int)offset+i,new BlockVector(event.getPriority(),event.getNumberOfAttendees()));
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
