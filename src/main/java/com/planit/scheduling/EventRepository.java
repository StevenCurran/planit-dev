package com.planit.scheduling;

import com.planit.persistence.registration.User;
import com.planit.persistence.scheduling.PlanItEvent;
import org.joda.time.DateTime;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Josh on 26/02/14.
 */
public class EventRepository {

    static List<PlanItEvent> getEventsByUser(User u) {
        return null;
    }

    static List<PlanItEvent> getEventsByUser(User u, DateTime startDate, DateTime endDate) {
        return null;
    }


    static List<User> getUsersByEvent(PlanItEvent e) {
        List<User> users = new LinkedList<User>();
        users.add(new User("Josh", "Lockhart", "", "", "", ""));
        users.add(new User("James", "Lockhart", "", "", "", ""));
        return users;

    }

}
