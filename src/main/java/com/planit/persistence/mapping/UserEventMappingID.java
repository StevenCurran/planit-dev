package com.planit.persistence.mapping;

import com.planit.persistence.events.PlanitEvent;
import com.planit.persistence.registration.User;

import javax.persistence.Embeddable;
import javax.persistence.ManyToMany;

/**
 * Created by jordan on 24/03/2014.
 */
@Embeddable
public class UserEventMappingID {


    private User user;
    private PlanitEvent planitEvent;

    @ManyToMany
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public PlanitEvent getPlanitEvent() {
        return planitEvent;
    }

    @ManyToMany
    public void setPlanitEvent(PlanitEvent planitEvent) {
        this.planitEvent = planitEvent;
    }



}
