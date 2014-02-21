package com.planit.persistence.registration;

import com.google.api.services.plus.model.Person;
import org.hibernate.annotations.ValueGenerationType;


import javax.persistence.*;
import java.util.Map;

/**
 * Created by Steven on 20/02/14.
 */

@Entity
@Table(name="\"User\"")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private String firstName;
    private String lastName;
    private String profileUrl;
    private String email;
    private String location;

    protected User(){};

    public User(String first, String last, String profileUrl, String email, String location){
        this.firstName = first;
        this.lastName = last;
        this.profileUrl = profileUrl;
        this.email = email;
        this.location = location;

    }

    public User(Person person){
        this.firstName = person.getName().getGivenName();
        this.lastName = person.getName().getFamilyName();
        this.profileUrl = person.getImage().getUrl();
        this.email = person.getEmails().get(0).getValue();
        this.location = person.getCurrentLocation();
    }




}
