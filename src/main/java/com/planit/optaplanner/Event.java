package com.planit.optaplanner;

import java.util.Date;

/**
 * Created by Steven on 16/02/14.
 */
public class Event {

    private Date startDate;
    private Date endDate;

    public Event() {

    }

    public Event(Date startDate, Date endDate) {
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

}
