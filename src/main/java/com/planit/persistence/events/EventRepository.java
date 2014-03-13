package com.planit.persistence.events;

import com.planit.persistence.registration.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * Created by Steven Curran on 27/02/14.
 */
public interface EventRepository extends CrudRepository<PlanitEvent, String> {

    @Query(value = "select distinct u.* from \"User\" u inner join event_user eu on eu.userid = u.userid inner join \"Event\" e on e.eventid = eu.eventid where e.eventid = ?1", nativeQuery = true)
    public List<User> findUsersForEvent(String eventId);
}
