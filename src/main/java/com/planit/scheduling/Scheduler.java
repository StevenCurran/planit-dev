package com.planit.scheduling;

import com.planit.persistence.events.PlanitEvent;
import com.planit.persistence.registration.User;
import com.planit.persistence.registration.UserRepository;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Josh & Stephen on 26/02/14.
 */
@Component
public class Scheduler {

    @Autowired
    private UserRepository userRepository;

    private float[] weightings = {1.0f, 1.0f, 1.0f, 1.0f};


    private double f(List<BlockVector> bv) {
        double x[] = new double[BlockVector.dimension];
        for (BlockVector v : bv) {
            for (int i = 0; i < x.length; i++) {
                x[i] += Math.pow((double) v.getAtIndex(i), 2);
            }
        }

        double score = 0.0;
        for (int i = 0; i < x.length; i++) {
            score += weightings[i] * (Math.sqrt(x[i]));
        }

        return score;
    }

    private List<List<BlockVector>> getPossibleEventPlacements(UserSchedule schedule, int duration) {
        List<List<BlockVector>> placements = new LinkedList<List<BlockVector>>();
        for (int i = 0; i < schedule.length; i++) {
            List<BlockVector> thisPlacement = schedule.getScheduleWindow(i, i + duration);
            if (thisPlacement == null) {
                break;
            } else {
                placements.add(thisPlacement);
            }
        }
        return placements;
    }

    public List<String> getConflictingEvents(User u, PlanitEvent event)
    {
        List<User> attendees = new LinkedList<User>();
        attendees.add(u);
        List<String> conflicts = getBestDate(attendees, new DateTime(event.getStartDate()),new DateTime(event.getEndDate()),(int)event.getDurationInHalfHours(),event.getPriority()).getConflicts();
        conflicts.remove(event.getEventId());
        return conflicts;
    }


    public DateTimeWithConflicts getBestDate(List<User> attendees, DateTime startDate, DateTime endDate, int duration, int priority) {
        List<UserSchedule> schedules = new LinkedList<UserSchedule>();

        for (User attendee : attendees) {
            List<PlanitEvent> e = userRepository.findEventsForUser(attendee.getProviderId());
            System.out.println("THIS USER'S EVENTS ARE ");
            System.out.println(e);
            UserSchedule s = new UserSchedule(attendee, startDate, endDate, e);
            schedules.add(s);
        }

        final int numPossibleEventPlacements = (int) Math.floor(schedules.get(0).length - duration + 1);
        float[] scores = new float[numPossibleEventPlacements];

        // for each user, get their schedule
        for (UserSchedule schedule : schedules) {
            // get each possible event placement for that schedule and this duration
            DateTime currentDateTime = startDate;
            int i = 0;
            for (List<BlockVector> placement : getPossibleEventPlacements(schedule, duration)) {
                double score = f(placement);
                scores[i] += score;
                i++;
            }
        }

        double minScore = 9999.0;
        int minIndex = 0;
        for (int i = 0; i < scores.length; i++) {
            if (scores[i] < minScore) {
                minScore = scores[i];
                minIndex = i;
            }
        }

        List<String> conflicts = new LinkedList<String>();

        for (UserSchedule schedule : schedules) {
            System.out.println("========= USER SCHEDULE CURRENTLY BEING ANALYSED:");
            schedule.displaySchedule();
            List<BlockVector> meanwhile = schedule.getScheduleWindow(minIndex, minIndex + duration-1);
            if(meanwhile != null){
                for (int i = 0; i < meanwhile.size(); i++) {
                    List<String> theseIds = meanwhile.get(i).getIds();
                    for(String thisId : theseIds){
                        if (thisId != null && !thisId.equals("") && !conflicts.contains(thisId)) {
                            conflicts.add(thisId);
                        }
                    }
                }
            }
        }

        DateTime bestStartTime = startDate.plusMinutes(minIndex * 30);
        DateTimeWithConflicts dtwc = new DateTimeWithConflicts(bestStartTime, conflicts);

        return dtwc;
    }
}
