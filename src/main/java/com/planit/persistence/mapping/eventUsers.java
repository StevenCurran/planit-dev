package com.planit.persistence.mapping;

/**
 * Created by Jay on 26/03/2014.
 */

import com.planit.persistence.events.PlanitEvent;
import com.planit.persistence.registration.User;

import javax.persistence.*;

@Entity
@Table(name = "eventUsers")
@AssociationOverrides({
        @AssociationOverride(name = "pk.planitEvent",joinColumns = @JoinColumn(name = "eventId")),
        @AssociationOverride(name = "pk.user",joinColumns = @JoinColumn(name = "userId"))
})
public class eventUsers {

    @EmbeddedId
    public eventUsersId getPk() {
        return pk;
    }

    public void setPk(eventUsersId pk) {
        this.pk = pk;
    }

    @Transient
    public PlanitEvent getPlanitEvent() {
        return getPk().getPlanitEvent();
    }

    public void setPlanitEvent(PlanitEvent event) {
        getPk().setPlanitEvent(event);
    }

    @Transient
    public User getUser() {
        return getPk().getUser();
    }

    public void setUser(User user) {
        getPk().setUser(user);
    }

    private eventUsersId pk = new eventUsersId();

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    private int status = 1;


}
