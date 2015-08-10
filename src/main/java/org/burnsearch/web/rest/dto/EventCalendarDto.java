package org.burnsearch.web.rest.dto;

import org.burnsearch.domain.Event;

import java.util.Date;
import java.util.List;

public class EventCalendarDto {
    // Day represented as epoch millis.
    private Date day;
    private List<Event> events;

    public Date getDay() {
        return day;
    }

    public void setDay(Date day) {
        this.day = day;
    }

    public List<Event> getEvents() {
        return events;
    }

    public void setEvents(List<Event> events) {
        this.events = events;
    }
}
