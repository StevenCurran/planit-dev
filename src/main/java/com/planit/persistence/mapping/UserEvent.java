package com.planit.persistence.mapping;

/**
 * Created by Jay on 26/03/2014.
 */

import com.planit.persistence.events.PlanitEvent;
import com.planit.persistence.registration.User;

import javax.persistence.*;

@Entity
@Table(name = "\"UserEvent\"")
@IdClass(UserEventId.class)
public class UserEvent {


    @Id
    private String userId;
    @Id
    private String eventId;


    @Column(name = "status")
    private int status;

    @ManyToOne
    @PrimaryKeyJoinColumn(name = "userId", referencedColumnName = "userId")
    private User user;

    @ManyToOne
    @PrimaryKeyJoinColumn(name = "eventId", referencedColumnName = "eventId")
    private PlanitEvent event;


    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public PlanitEvent getEvent() {
        return event;
    }

    public void setEvent(PlanitEvent event) {
        this.event = event;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

}