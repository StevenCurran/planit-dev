package com.planit.mvc.events;

import com.planit.persistence.events.PlanitEvent;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Steven Curran on 26/03/14.
 */
public class PlanitEventWithConflicts {

    public PlanitEvent getPlanitEvent() {
        return planitEvent;
    }

    public void setPlanitEvent(PlanitEvent planitEvent) {
        this.planitEvent = planitEvent;
    }

    public List<PlanitEvent> getConflictingEvents() {
        return conflictingEvents;
    }

    public void setConflictingEvents(List<PlanitEvent> conflictingEvents) {
        this.conflictingEvents = conflictingEvents;
    }

    private PlanitEvent planitEvent;
    private List<PlanitEvent> conflictingEvents = new ArrayList<>();

    public PlanitEventWithConflicts(PlanitEvent event) {
        this.planitEvent = event;
    }

    public void addConflict(PlanitEvent event) {
        this.conflictingEvents.add(event);
    }


}
