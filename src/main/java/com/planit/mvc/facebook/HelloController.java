package com.planit.mvc.facebook;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/home")
public class HelloController {

	@RequestMapping(method = RequestMethod.GET)
        public String printWelcome(ModelMap model) {
            model.addAttribute("message", "Steven Curran");
            return "hello";
	}


    @RequestMapping("/name/{namevar}")
    public String printVariable(@PathVariable String namevar, ModelMap model) {
        model.addAttribute("message", "Hello: " + namevar + "!!");
        return "hello";
    }

    @RequestMapping("/first/{firstName}/second/{secondName}")
    public String printSurname(@PathVariable String firstName, @PathVariable String secondName, ModelMap model) {
        model.addAttribute("message", "Hello: " + firstName  + " " + secondName  + "!!");
        return "hello";
    }


}