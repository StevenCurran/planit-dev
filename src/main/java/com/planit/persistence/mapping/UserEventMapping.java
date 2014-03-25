package com.planit.persistence.mapping;

import com.planit.persistence.events.PlanitEvent;
import com.planit.persistence.registration.User;

import javax.persistence.*;

/**
 * Created by jordan on 24/03/2014.
 */



//@Entity
//@Table(name = "\"event_user\"")
//@AssociationOverrides({
//        @AssociationOverride(name = "pk.planitEvent",joinColumns = @JoinColumn(name = "eventId")),
//        @AssociationOverride(name = "pk.userId", joinColumns = @JoinColumn(name = "userId")) })
public class UserEventMapping {

//    @EmbeddedId
    private UserEventMappingID pk = new UserEventMappingID();

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

    public UserEventMappingID getPk() {
        return pk;
    }

    public void setPk(UserEventMappingID pk) {
        this.pk = pk;
    }

    public PlanitEvent getPlanitEvent(){
        return getPk().getPlanitEvent();
    }

    public void setPlanitEvent(PlanitEvent planitEvent) {
        getPk().setPlanitEvent(planitEvent);
    }

    public User getUser() {
        return getPk().getUser();
    }

    public void setUser(User user) {
        getPk().setUser(user);
    }



    public enum Attending{
        NOT_ATTENDING, PENDING, ATTENDING;
    }



}
