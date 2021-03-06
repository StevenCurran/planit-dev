package com.planit.persistence.registration;


import com.planit.persistence.events.PlanitEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;

/**
 * Created by Steven on 20/02/14.
 */

@Repository
public interface UserRepository extends JpaRepository<User, String> {


    //    @Query(value = "select distinct e.* from \"Event\" e inner join \"event_user\" eu on eu.eventid = e.eventid inner join \"User\" u on u.userid = eu.userid where u.userid = ?1", nativeQuery = true)
//from Event as e inner join e.eventid as eid where userid = :userId

    @Query(value = "select distinct e from PlanitEvent e inner join e.userEvents att where att.userId = ?1")
    public ArrayList<PlanitEvent> findEventsForUser(String userId);

    @Query(value = "select distinct e from PlanitEvent e inner join e.userEvents att where att.userId = ?1 and att.status = 0")
    public ArrayList<PlanitEvent> findPendingEventsForUser(String userId);

/*

select distinct e.* from "Event" e inner join "event_user" eu on eu.eventid = e.eventid inner join "User" u on u.userid = eu.userid where u.userid = '114700121025537154047'

select distinct u.* from "User" u inner join event_user eu on eu.userid = u.userid inner join "Event" e on e.eventid = eu.eventid where e.eventid = '04u9nd81ua1c6eogdbnarjk0cs_20140228T100000Z'



 */


}
