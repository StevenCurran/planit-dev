package com.planit.mvc.google;

import com.google.api.services.calendar.model.Events;

import java.util.List;

/**
 * Created by Steven Curran on 16/02/14.
 */

//Class to async save to DB. May have to change this to callable in the future....
public class EventPersistenceTask implements Runnable {

    private final List<Events> eventInput;

    public EventPersistenceTask(final List<Events> eventInput){
        this.eventInput = eventInput;
    }




    @Override
    public void run() {

        //save events in here....
        try {
            Thread.sleep(10000);

            for (Events e : eventInput){
                System.out.println(e.getSummary());
            }


        } catch (InterruptedException e) {


        }

    }
}
