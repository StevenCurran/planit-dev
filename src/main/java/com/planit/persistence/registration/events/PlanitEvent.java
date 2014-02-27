package com.planit.persistence.registration.events;

import com.google.api.services.calendar.model.Event;

import javax.persistence.*;

/**
 * Created by Steven Curran on 27/02/14.
 */
@Entity
@Table(name = "\"Event\"")
public class PlanitEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;



    public PlanitEvent(){

    }

    public PlanitEvent(Event googleEvent){


    }


}
