package com.planit.mvc;

/**
 * Created by Steven Curran on 12/02/14.
 */

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.restfb.Connection;
import com.restfb.DefaultFacebookClient;
import com.restfb.FacebookClient;
import com.restfb.Parameter;
import com.restfb.types.Event;
import com.restfb.types.Page;
import com.restfb.types.User;
import com.socialauth.plugin.EventPlugin;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.brickred.socialauth.*;
import org.brickred.socialauth.provider.FacebookImpl;
import org.brickred.socialauth.spring.bean.SocialAuthTemplate;
import org.brickred.socialauth.util.AccessGrant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
public class FacebookSuccessController {

    @Autowired
    private SocialAuthTemplate socialAuthTemplate;

    @RequestMapping(value = "/authSuccess")
    public List<Event> getRedirectURL(final HttpServletRequest request)
            throws Exception {

        List<Contact> contactsList = new ArrayList<>();
        SocialAuthManager manager = socialAuthTemplate.getSocialAuthManager();
        AuthProvider provider = manager.getCurrentAuthProvider();

        List<Event> events = new ArrayList<>();
        if(provider.isSupportedPlugin(EventPlugin.class)){
            EventPlugin ep = provider.getPlugin(EventPlugin.class);
            events = ep.getEvents();
        }
        return events;
    }
}