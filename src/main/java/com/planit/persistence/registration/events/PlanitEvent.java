package com.planit.persistence.registration.events;

import com.google.api.services.calendar.model.Event;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Steven Curran on 27/02/14.
 */
@Entity
@Table(name = "\"Event\"")
public class PlanitEvent {

    @Id
    private String id;

    private String name;
    private Date startDate;
    private Date endDate;
    private int duration; //half hour chunks.
    private String creator;
    private String description;
    private String location;
    private String timeZone;


    public PlanitEvent() {

    }

    public PlanitEvent(Event googleEvent) {
        this.id = googleEvent.getId();
        this.name = googleEvent.getSummary();
        this.startDate = new Date(googleEvent.getOriginalStartTime().getDateTime().getValue());
        this.endDate = new Date(googleEvent.getEnd().getDateTime().getValue());
        this.creator = googleEvent.getCreator().getId();
        this.description = googleEvent.getDescription();
        this.location = googleEvent.getLocation();
        this.timeZone = googleEvent.getOriginalStartTime().getTimeZone();

    }

    public static List<PlanitEvent> getEvents(List<Event> events){
        List<PlanitEvent> pE = new ArrayList<>();
        for (Event event : events) {
            PlanitEvent p = new PlanitEvent(event);
            pE.add(p);
        }
        return pE;
    }


}
