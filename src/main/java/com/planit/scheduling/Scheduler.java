package com.planit.scheduling;

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

    private float[] weightings = {1.0f, 1.0f, 1.0f};


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

    public DateTimeWithConflicts getBestDate(List<User> attendees, DateTime startDate, DateTime endDate, int duration, int priority) {
        List<UserSchedule> schedules = new LinkedList<UserSchedule>();

        for (User attendee : attendees) {
            UserSchedule s = new UserSchedule(attendee, startDate, endDate, userRepository.findEventsForUser(attendee.getProviderId()));
            schedules.add(s);
        }

        final int numPossibleEventPlacements = (int) Math.floor(schedules.get(0).length - duration + 1);
        float[] scores = new float[numPossibleEventPlacements];

        // for each user, get their schedule
        for (UserSchedule schedule : schedules) {
            schedule.displaySchedule();
            // get each possible event placement for that schedule and this duration
            DateTime currentDateTime = startDate;
            int i = 0;
            for (List<BlockVector> placement : getPossibleEventPlacements(schedule, duration)) {
                double score = f(placement);
                scores[i] += score * naivePreferenceMatrix[currentDateTime.plusMinutes(i * 30).getDayOfWeek()-1][currentDateTime.plusMinutes(i * 30).getHourOfDay()];
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
            List<BlockVector> meanwhile = schedule.getScheduleWindow(minIndex, minIndex + duration);
            for (int i = 0; i < meanwhile.size(); i++) {
                String thisId = meanwhile.get(i).getId();
                if (thisId != null && !conflicts.contains(thisId)) {
                    conflicts.add(thisId);
                }
            }
        }

        DateTime bestStartTime = startDate.plusMinutes(minIndex * 30);
        DateTimeWithConflicts dtwc = new DateTimeWithConflicts(bestStartTime, conflicts);


        return dtwc;
    }
}
