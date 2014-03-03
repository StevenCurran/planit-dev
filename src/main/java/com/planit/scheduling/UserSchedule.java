package com.planit.scheduling;

import com.planit.persistence.registration.User;
import org.joda.time.DateTime;
import org.joda.time.Duration;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

/**
 * Created by Josh on 26/02/14.
 */
public class UserSchedule {
    private List<BlockVector> schedule;
    public long length;

    public UserSchedule(User u, DateTime startDate, DateTime endDate) {
        schedule = new LinkedList<BlockVector>();
        Duration duration = new Duration(startDate, endDate);
        long numHalfHoursInWindow = duration.getStandardHours() * 2;
        length = numHalfHoursInWindow;
        //List<PlanItEvent> events = EventRepository.getEventsByUser(u,startDate,endDate);

        // let's just add random data for now

        for (int block = 0; block < numHalfHoursInWindow; block++) {
            // if there is an event in the user's schedule that spans this block then put the details into a new BlockVector v

            // else create a new BlockVector v with 0s in all entries

            // for now let's just put random values into the block vector so we can write the brunt of the algorithm
            Random r = new Random();
            //r.setSeed(DateTime.now().);
            BlockVector v = new BlockVector(r.nextInt(5), r.nextInt(5));

            // append BlockVector v to schedule list
            schedule.add(v);
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
