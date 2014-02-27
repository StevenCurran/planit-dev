package com.planit.persistence.registration.events;

import org.springframework.data.repository.CrudRepository;

/**
 * Created by Steven Curran on 27/02/14.
 */
public interface EventRepository extends CrudRepository<PlanitEvent, String> {

}
