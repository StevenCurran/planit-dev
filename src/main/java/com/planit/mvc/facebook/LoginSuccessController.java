package com.planit.mvc.facebook;

/**
 * Created by Steven Curran on 12/02/14.
 */

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.brickred.socialauth.*;
import org.brickred.socialauth.spring.bean.SocialAuthTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}