package com.planit.persistence.registration;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Steven on 20/02/14.
 */

@Repository
public interface UserRepository extends JpaRepository<User, String> {


    //@Query("select User from event_user ")
   // public List<User> findEvents();


}
