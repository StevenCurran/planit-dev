package com.planit.scheduling;

import com.planit.persistence.registration.User;
import org.joda.time.DateTime;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Josh & Stephen on 26/02/14.
 */
public class Scheduler {

    private float[] weightings = {1.0f, 1.0f, 1.0f};

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
        System.out.println(score);

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

    private int searchSchedules(List<UserSchedule> schedules, int duration) {
        final int numPossibleEventPlacements = (int) Math.floor(schedules.get(0).length - duration + 1);
        float[] scores = new float[numPossibleEventPlacements];

        // for each user, get their schedule
        for (UserSchedule schedule : schedules) {
            schedule.displaySchedule();
            // get each possible event placement for that schedule and this duration
            int i = 0;
            for (List<BlockVector> placement : getPossibleEventPlacements(schedule, duration)) {
                double score = f(placement);
                scores[i++] += score;
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
        return minIndex;
    }

    public DateTime getBestDate(List<User> attendees, DateTime startDate, DateTime endDate, int duration, int priority) {
        List<UserSchedule> schedules = new LinkedList<UserSchedule>();

        for (User attendee : attendees) {
            UserSchedule s=new UserSchedule(attendee, startDate, endDate);
            System.out.println("============================ HELLO JOSH-KUN, FOR ATTENDEE ID " + attendee.getProviderId() + " WE HAVE A SCHEDULE THAT LOOKS LIKE ");
            s.displaySchedule();
            schedules.add(s);
        }

        int bestStartBlock = searchSchedules(schedules, duration);

        DateTime bestStartTime = startDate.plusMinutes(bestStartBlock * 30);
        return bestStartTime;
    }
}
