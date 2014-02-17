package com.planit.optaplanner;

import org.optaplanner.core.api.domain.entity.PlanningEntity;
import org.optaplanner.core.api.domain.variable.PlanningVariable;

/**
 * Created by Steven on 16/02/14.
 */

@PlanningEntity()
public class EventProcess {

    private Event event;

    public Event getEvent(){
        return event;
    }

    public void setEvent(Event event){
        this.event = event;
    }

}
