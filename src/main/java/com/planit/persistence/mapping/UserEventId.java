package com.planit.persistence.mapping;

/**
 * Created by Jay on 26/03/2014.
 */

import com.planit.persistence.events.PlanitEvent;
import com.planit.persistence.registration.User;

import javax.persistence.Embeddable;
import javax.persistence.ManyToOne;

import java.io.Serializable;

public class UserEventId implements Serializable {

    private String eventId;
    private String userId;

    public int hashCode() {
        return (int)(employeeId + projectId);
    }
 
    public boolean equals(Object object) {
        if (object instanceof ProjectAssociationId) {
            ProjectAssociationId otherId = (ProjectAssociationId) object;
            return (otherId.employeeId == this.employeeId) && (otherId.projectId == this.projectId);
        }
        return false;
    }
}


