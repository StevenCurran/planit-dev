package com.planit.mvc.google;

import com.plaint.domainobjs.Person;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


/**
 * Created by Steven on 15/02/14.
 */
@RestController
@RequestMapping("/googlelogin")
public class GoogleController {

    @RequestMapping(method = RequestMethod.GET)
    public Person homeMethod(){
        Person p = new Person();
        p.setAge(10);
        return p;
    }


}
