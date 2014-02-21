package com.planit.persistence.registration;


import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Steven on 20/02/14.
 */

@Repository
public interface UserRepository extends CrudRepository<User, Long> {

    User findByUsername(String username);
}
