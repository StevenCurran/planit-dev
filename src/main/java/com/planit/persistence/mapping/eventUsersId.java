package com.planit.persistence.mapping;

/**
 * Created by Jay on 26/03/2014.
 */

import com.planit.persistence.events.PlanitEvent;
import com.planit.persistence.registration.User;

import javax.persistence.Embeddable;
import javax.persistence.ManyToOne;

@Embeddable
public class eventUsersId {

    @ManyToOne
    public PlanitEvent getPlanitEvent() {
        return planitEvent;
    }

    public void setPlanitEvent(PlanitEvent planitEvent) {
        this.planitEvent = planitEvent;
    }

    @ManyToOne
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    private PlanitEvent planitEvent;
    private User user;

}


