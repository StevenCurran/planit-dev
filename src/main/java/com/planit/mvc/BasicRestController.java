package com.planit.mvc;

import com.plaint.domainobjs.Person;
import org.springframework.http.MediaType;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by Steven Curran on 12/02/14.
 */

@RestController
@RequestMapping("/rest")
public class BasicRestController {


    @RequestMapping(value = "/test/{name}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Person test(@PathVariable String name) {
        Person p = new Person();
        p.setAge(10);
        p.setFirstName(name);
        p.setLastName(name);
        return p;

    }

}
