package com.socialauth.plugin;

import com.restfb.Connection;
import com.restfb.DefaultFacebookClient;
import com.restfb.FacebookClient;
import com.restfb.Parameter;
import com.restfb.types.Event;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.brickred.socialauth.util.Constants;
import org.brickred.socialauth.util.MethodType;
import org.brickred.socialauth.util.ProviderSupport;
import org.brickred.socialauth.util.Response;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Steven on 13/02/14.
 */
public class EventPluginFacebookImpl implements EventPlugin{

    private static final String EVENTS_URL = "https://graph.facebook.com/me/events";
    private final Log LOG = LogFactory.getLog(this.getClass());


    private ProviderSupport providerSupport;

    public EventPluginFacebookImpl(final ProviderSupport providerSupport){
        this.providerSupport = providerSupport;
    }

    @Override
    public List<Event> getEvents() throws Exception {


        FacebookClient facebookClient = new DefaultFacebookClient(providerSupport.getAccessGrant().getKey());
        Connection<Event> myEvents = facebookClient.fetchConnection("me/events", Event.class, Parameter.with("fields", "description, name, location"));
        List<Event> events = myEvents.getData();

        return events;
    }

    @Override
    public ProviderSupport getProviderSupport() {
        return providerSupport;
    }

    @Override
    public void setProviderSupport(ProviderSupport providerSupport) {
        this.providerSupport = providerSupport;
    }
}
