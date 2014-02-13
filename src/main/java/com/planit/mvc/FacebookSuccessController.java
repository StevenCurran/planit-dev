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
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.brickred.socialauth.AuthProvider;
import org.brickred.socialauth.Contact;
import org.brickred.socialauth.Permission;
import org.brickred.socialauth.SocialAuthManager;
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

        /*
        contactsList = provider.getContactList();

        if (contactsList != null && contactsList.size() > 10) {
            contactsList = contactsList.subList(1,10);
            for (Contact p : contactsList) {
                if (!StringUtils.hasLength(p.getFirstName())
                        && !StringUtils.hasLength(p.getLastName())) {
                    p.setFirstName(p.getDisplayName());
                }
            }
        }

*/

        FacebookClient facebookClient = new DefaultFacebookClient(provider.getAccessGrant().getKey());
        Connection<Event> myEvents = facebookClient.fetchConnection("me/events", Event.class, Parameter.with("fields", "description, name, location"));
        List<Event> events = myEvents.getData();

        User user = facebookClient.fetchObject("me", User.class);

        provider.logout();
        return events;
    }
}