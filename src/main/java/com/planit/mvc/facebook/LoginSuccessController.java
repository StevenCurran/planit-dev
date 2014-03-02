package com.planit.mvc.facebook;

/**
 * Created by Steven Curran on 12/02/14.
 */

import org.brickred.socialauth.AuthProvider;
import org.brickred.socialauth.Contact;
import org.brickred.socialauth.Profile;
import org.brickred.socialauth.SocialAuthManager;
import org.brickred.socialauth.spring.bean.SocialAuthTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@RestController
public class LoginSuccessController {

    @Autowired
    private SocialAuthTemplate socialAuthTemplate;

    @RequestMapping(value = "/authSuccess")
    public Profile getRedirectURL(final HttpServletRequest request)
            throws Exception {

        List<Contact> contactsList = new ArrayList<>();
        SocialAuthManager manager = socialAuthTemplate.getSocialAuthManager();
        AuthProvider provider = manager.getCurrentAuthProvider();
/*
        List<Event> events = new ArrayList<>();
        if(provider != null && provider.isSupportedPlugin(EventPlugin.class)){
            EventPlugin ep = provider.getPlugin(EventPlugin.class);
            events = ep.getEvents();
        }
        */
        return provider.getUserProfile();
    }


    @RequestMapping(value = "/fbevents")
    public List<com.restfb.types.Event> getFbEvents(final HttpServletRequest request) throws Exception {

        SocialAuthManager manager = socialAuthTemplate.getSocialAuthManager();
        AuthProvider provider = manager.getCurrentAuthProvider();

        List<com.restfb.types.Event> events = new ArrayList<>();
        if (provider != null && provider.isSupportedPlugin(EventPlugin.class)) {
            EventPlugin ep = provider.getPlugin(EventPlugin.class);
            events = ep.getEvents();
        }

        return events;
    }

}