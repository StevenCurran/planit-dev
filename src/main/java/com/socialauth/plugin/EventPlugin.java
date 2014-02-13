package com.socialauth.plugin;


import com.restfb.types.Event;
import org.brickred.socialauth.plugin.Plugin;

import java.util.List;

/**
 * Created by Steven on 13/02/14.
 */
public interface EventPlugin extends Plugin {
    public List<Event> getEvents() throws Exception;
}
