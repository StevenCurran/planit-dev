package com.planit.persistence.events;

import com.google.api.services.calendar.model.Event;
import com.planit.persistence.mapping.eventUsers;
import com.planit.persistence.registration.User;
import org.joda.time.DateTime;
import org.joda.time.Duration;

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
    private String creator;
    private String description;
    private String location;
    private String timeZone;
    private int priority;


    private Set<eventUsers> attendees = new HashSet<>();

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
        this.priority = 3;

        addAttendee(p);
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getCreator() {
        return creator;
    }

    public String getDescription() {
        return description;
    }

    public String getLocation() {
        return location;
    }

    public String getTimeZone() {
        return timeZone;
    }

    @OneToMany(fetch = FetchType.LAZY, mappedBy ="pk.PlanitEvent",cascade = CascadeType.ALL )
    public Set<eventUsers> getAttendees() {
        return attendees;
    }

    public String getEventId() {
        return eventId;
    }

    public Set<eventUsers> getCategories() {
        return this.attendees;
    }

    public void addAttendee(User u) {
        attendees.add(new eventUsers());
    }

    public long getDurationInHalfHours() {
        DateTime startDateJ = new DateTime(this.startDate);
        DateTime endDateJ = new DateTime(this.endDate);

        Duration duration = new Duration(startDateJ, endDateJ);
        return duration.getStandardMinutes() / 30;
    }

    public long getStartTimeOffset(Date d) {
        return getStartTimeOffset(new DateTime(d));
    }

    public long getStartTimeOffset(DateTime d) {
        DateTime startDateJ = new DateTime(this.startDate);
        DateTime dJ = new DateTime(d);

        Duration duration = new Duration(dJ, startDateJ);
        return duration.getStandardMinutes() / 30;
    }

    public int getPriority() {
        return this.priority;
    }

    public void setPriority(int p) {
        this.priority = p;
    }

    public int getNumberOfAttendees() {
        return attendees.size();
    }


}
