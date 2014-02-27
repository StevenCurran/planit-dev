package com.planit.persistence.registration;

import com.google.api.services.plus.model.Person;
import com.planit.persistence.registration.events.PlanitEvent;
import org.hibernate.annotations.ValueGenerationType;


import javax.persistence.*;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by Steven on 20/02/14.
 */

@Entity
@Table(name = "\"User\"")
public class User {

    @Id
    private String id;

    private String firstName;
    private String lastName;
    private String profileUrl;
    private String email;
    private String location;





    //Allow Git

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "attendees")
    private Set<PlanitEvent> events = new HashSet<>();

    protected User() {
    }


    public User(String first, String last, String profileUrl, String email, String location, String providerId) {
        this.firstName = first;
        this.lastName = last;
        this.profileUrl = profileUrl;
        this.email = email;
        this.location = location;
        this.id = providerId;

    }

    public User(Person person) {
        this.firstName = person.getName().getGivenName();
        this.lastName = person.getName().getFamilyName();
        this.profileUrl = person.getImage().getUrl();
        this.email = person.getEmails().get(0).getValue();
        if (person.getPlacesLived() != null) {
            this.location = person.getPlacesLived().get(0).getValue();
        }
        this.id = person.getId();
    }


    public Set<PlanitEvent> getEvents() {
        return this.events;
    }

    public String getProviderId() {
        return id;
    }

    public void setProviderId(String providerId) {
        this.id = providerId;
    }


}
