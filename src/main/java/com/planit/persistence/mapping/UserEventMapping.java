package com.planit.persistence.mapping;

import com.planit.persistence.events.PlanitEvent;
import com.planit.persistence.registration.User;

import javax.persistence.*;

/**
 * Created by jordan on 24/03/2014.
 */



@Entity
@Table(name = "\"event_user\"")
@AssociationOverrides({
        @AssociationOverride(name = "Event",joinColumns = @JoinColumn(name = "eventId")),
        @AssociationOverride(name = "User", joinColumns = @JoinColumn(name = "userId")) })
public class UserEventMapping {

    private Attending attendingStatus;
    private User user;
    private PlanitEvent planitEvent;

    public UserEventMapping(User user, PlanitEvent event, Attending attendingStatus){
        this.user = user;
        this.planitEvent = event;
        this.attendingStatus = attendingStatus;
    }

    public Attending getStatus() {
        return attendingStatus;
    }

    public void setStatus(Attending attendingStatus) {
        this.attendingStatus = attendingStatus;
    }



    public enum Attending{
        NOT_ATTENDING, PENDING, ATTENDING;
    }



}
