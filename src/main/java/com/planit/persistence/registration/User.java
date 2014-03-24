package com.planit.persistence.registration;

import com.google.api.services.plus.model.Person;
import com.planit.persistence.events.PlanitEvent;
import com.planit.persistence.mapping.UserEventMapping;
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
    @ManyToMany(fetch = FetchType.EAGER, mappedBy = "eventAttendees")
    private Set<UserEventMapping> events = new HashSet<UserEventMapping>();

    protected User() {
    }

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



    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getProfileUrl() {
        return profileUrl;
    }

    public void setProfileUrl(String profileUrl) {
        this.profileUrl = profileUrl;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    //Allow Git

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @JsonIgnore
    public Set<UserEventMapping> getEvents() {
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

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }
}
