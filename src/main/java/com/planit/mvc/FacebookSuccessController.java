package com.planit.mvc;

/**
 * Created by Steven Curran on 12/02/14.
 */

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.brickred.socialauth.AuthProvider;
import org.brickred.socialauth.Contact;
import org.brickred.socialauth.SocialAuthManager;
import org.brickred.socialauth.spring.bean.SocialAuthTemplate;
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
    public List<Contact> getRedirectURL(final HttpServletRequest request)
            throws Exception {

        List<Contact> contactsList = new ArrayList<>();
        SocialAuthManager manager = socialAuthTemplate.getSocialAuthManager();
        AuthProvider provider = manager.getCurrentAuthProvider();

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

        return contactsList;
    }
}