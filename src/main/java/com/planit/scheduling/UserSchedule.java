package com.planit.scheduling;

import com.planit.persistence.events.PlanitEvent;
import com.planit.persistence.registration.User;
import org.joda.time.DateTime;
import org.joda.time.Duration;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Josh on 26/02/14.
 */

public class UserSchedule {
    private List<BlockVector> schedule;
    public long length;

    private float[][] naivePreferenceMatrix = {
            {2.2f, 2.4f, 2.6f, 2.4f, 2.2f, 2.0f, 1.2f, 0.6f, 0.5f, 0.4f, 0.3f, 0.2f, 0.3f, 1.4f, 0.2f, 0.4f, 0.6f, 0.8f, 1.0f, 1.2f, 1.4f, 1.6f, 1.8f, 2.0f},
            {2.2f, 2.4f, 2.6f, 2.4f, 2.2f, 2.0f, 1.2f, 0.6f, 0.5f, 0.4f, 0.3f, 0.2f, 0.3f, 1.4f, 0.2f, 0.4f, 0.6f, 0.8f, 1.0f, 1.2f, 1.4f, 1.6f, 1.8f, 2.0f},
            {2.2f, 2.4f, 2.6f, 2.4f, 2.2f, 2.0f, 1.2f, 0.6f, 0.5f, 0.4f, 0.3f, 0.2f, 0.3f, 1.4f, 0.2f, 0.4f, 0.6f, 0.8f, 1.0f, 1.2f, 1.4f, 1.6f, 1.8f, 2.0f},
            {2.2f, 2.4f, 2.6f, 2.4f, 2.2f, 2.0f, 1.2f, 0.6f, 0.5f, 0.4f, 0.3f, 0.2f, 0.3f, 1.4f, 0.2f, 0.4f, 0.6f, 0.8f, 1.0f, 1.2f, 1.4f, 1.6f, 1.8f, 2.0f},
            {2.2f, 2.4f, 2.6f, 2.4f, 2.2f, 2.0f, 1.2f, 0.6f, 0.5f, 0.4f, 0.3f, 0.2f, 0.3f, 1.4f, 0.2f, 0.4f, 0.6f, 0.8f, 1.0f, 1.2f, 1.4f, 1.6f, 1.8f, 2.0f},
            {2.2f, 2.4f, 2.6f, 2.4f, 2.2f, 2.0f, 1.2f, 0.6f, 0.5f, 0.4f, 0.3f, 0.2f, 0.3f, 1.4f, 0.2f, 0.4f, 0.6f, 0.8f, 1.0f, 1.2f, 1.4f, 1.6f, 1.8f, 2.0f},
            {2.2f, 2.4f, 2.6f, 2.4f, 2.2f, 2.0f, 1.2f, 0.6f, 0.5f, 0.4f, 0.3f, 0.2f, 0.3f, 1.4f, 0.2f, 0.4f, 0.6f, 0.8f, 1.0f, 1.2f, 1.4f, 1.6f, 1.8f, 2.0f}
            //{0.1f, 0.2f, 0.3f, 0.4f, 0.5f, 0.6f, 0.7f, 0.8f, 0.85f, 0.9f, 1.0f, 0.85f, 0.5f, 1.0f, 0.9f, 0.85f, 0.8f, 0.7f, 0.6f, 0.5f, 0.4f, 0.3f, 0.2f, 0.1f},
    };

    public UserSchedule(User u, DateTime startDate, DateTime endDate, List<PlanitEvent> events) {
        schedule = new LinkedList<BlockVector>();
        Duration duration = new Duration(startDate, endDate);
        length = duration.getStandardHours() * 2;
        //List<PlanitEvent> events = userRepository.findEventsForUser(u.getProviderId());

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
                if (pos >= 0 && pos < length) {
                    float uPref = naivePreferenceMatrix[startDate.plusMinutes(pos * 30).getDayOfWeek()-1][startDate.plusMinutes(pos * 30).getHourOfDay()];
                    BlockVector newBv = schedule.get(pos);
                    newBv.add(new BlockVector(event.getEventId(), event.getPriority(), event.getNumberOfAttendees(), uPref));
                    schedule.set(pos, newBv);
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
        if ((s1 <= s0) || (s1 > this.length)) {
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
