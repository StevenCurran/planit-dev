package com.planit.scheduling;

import com.planit.persistence.events.PlanitEvent;
import org.joda.time.DateTime;

import java.util.List;

/**
 * Created by Stephen et Josh on 24/03/14.
 */
public class DateTimeWithConflicts {

    private DateTime dateTime;
    private List<String> conflicts;

    public DateTimeWithConflicts(DateTime dateTime, List<String> conflicts)
    {
        this.dateTime = dateTime;
        this.conflicts = conflicts;
    }

    public DateTime getDateTime()
    {
        return dateTime;
    }

    public List<String> getConflicts()
    {
        return conflicts;
    }
}
