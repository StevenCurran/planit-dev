package com.planit.mvc.google;

import com.planit.persistence.registration.User;
import com.planit.persistence.registration.events.EventRepository;
import com.planit.persistence.registration.events.PlanitEvent;

import java.util.List;

/**
 * Created by Steven Curran on 16/02/14.
 */

//Class to async save to DB. May have to change this to callable in the future....
public class EventPersistenceTask implements Runnable {

    private final List<PlanitEvent> eventInput;
    private final EventRepository eventRepository;

    public EventPersistenceTask(final User person, final List<PlanitEvent> eventInput, EventRepository eventRepository) {
        this.eventInput = eventInput;
        this.eventRepository = eventRepository;
    }


    @Override
    public void run() {
        System.out.println("Adding events...!!");
        eventRepository.save(eventInput);
        System.out.println("Events have been added!!");
    }
}
