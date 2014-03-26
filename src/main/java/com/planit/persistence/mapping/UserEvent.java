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
    @PrimaryKeyJoinColumn(name="userId", referencedColumnName="userId")
    private User user;
    
    @ManyToOne
    @PrimaryKeyJoinColumn(name="eventId", referencedColumnName="eventId")
    private Event event;
}