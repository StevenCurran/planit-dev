package com.planit.persistence.registration.events;

import com.google.api.services.calendar.model.Event;
import com.planit.persistence.registration.User;

import javax.persistence.*;
import java.util.*;

/**
 * Created by Steven Curran on 27/02/14.
 */
@Entity
@Table(name = "\"Event\"")
public class PlanitEvent {

    @Id
    private String eventId;

    private String name;
    private Date startDate;
    private Date endDate;
    private int duration; //half hour chunks.
    private String creator;
    private String description;
    private String location;
    private String timeZone;


    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(name = "event_user", joinColumns = {
            @JoinColumn(name = "eventId", nullable = false, updatable = false)},
            inverseJoinColumns = {@JoinColumn(name = "userId",
                    nullable = false, updatable = false)})

    private Set<User> attendees = new HashSet<>();


    public PlanitEvent() {

    }

    public PlanitEvent(Event googleEvent, User p) {
        this.eventId = googleEvent.getId();
        this.name = googleEvent.getSummary();
        this.startDate = new Date(googleEvent.getStart().getDateTime().getValue());
        this.endDate = new Date(googleEvent.getEnd().getDateTime().getValue());
        this.creator = p.getProviderId();


        this.description = googleEvent.getDescription();
        this.location = googleEvent.getLocation();
        this.timeZone = googleEvent.getStart().getTimeZone();

    }

    public static List<PlanitEvent> getEvents(List<Event> events, User person) {
        List<PlanitEvent> pE = new ArrayList<>();
        for (Event event : events) {
            PlanitEvent p = new PlanitEvent(event, person);
            p.addAttendee(person);
            pE.add(p);
        }
        return pE;
    }


    public Set<User> getCategories() {
        return this.attendees;
    }

    public void addAttendee(User u)
    {
        attendees.add(u);
    }


}