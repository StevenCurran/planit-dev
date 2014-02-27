package com.planit.mvc.google;

import com.google.api.services.calendar.model.Event;
import com.google.api.services.plus.model.Person;

import java.util.List;

/**
 * Created by Steven Curran on 16/02/14.
 */

//Class to async save to DB. May have to change this to callable in the future....
public class EventPersistenceTask implements Runnable {

    private final List<Event> eventInput;

    public EventPersistenceTask(final Person person, final List<Event> eventInput) {
        this.eventInput = eventInput;
    }


    @Override
    public void run() {

        //save events in here....
        for (Event event : eventInput) {
            System.out.println(event.getSummary());
            System.out.println(event.getLocation());
            System.out.println(event.getOriginalStartTime());
            System.out.println(event.getEnd());
            System.out.println("_____________________");
        }

    }
}
