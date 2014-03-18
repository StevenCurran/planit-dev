package com.planit.persistence.registration;

import com.google.api.services.plus.model.Person;
import com.planit.persistence.events.PlanitEvent;
import org.codehaus.jackson.annotate.JsonIgnore;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Steven on 20/02/14.
 */

@Entity
@Table(name = "\"User\"")
public class User {

    @Id
    private String userId;

    private String firstName;
    private String lastName;
    private String profileUrl;
    private String email;
    private String location;
    private String deviceId;
    @JsonIgnore
    @ManyToMany(fetch = FetchType.EAGER, mappedBy = "attendees")
    private Set<PlanitEvent> events = new HashSet<>();

    protected User() {
    }

    //Allow Git

    public User(String first, String last, String profileUrl, String email, String location, String providerId) {
        this.firstName = first;
        this.lastName = last;
        this.profileUrl = profileUrl;
        this.email = email;
        this.location = location;
        this.userId = providerId;
    }


    public User(Person person) {
        this.firstName = person.getName().getGivenName();
        this.lastName = person.getName().getFamilyName();
        this.profileUrl = person.getImage().getUrl();
        this.email = person.getEmails().get(0).getValue();
        if (person.getPlacesLived() != null) {
            this.location = person.getPlacesLived().get(0).getValue();
        }
        this.userId = person.getId();
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @JsonIgnore
    public Set<PlanitEvent> getEvents() {
        return this.events;
    }

    public String getProviderId() {
        return userId;
    }

    public void setProviderId(String providerId) {
        this.userId = providerId;
    }


    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }
}
