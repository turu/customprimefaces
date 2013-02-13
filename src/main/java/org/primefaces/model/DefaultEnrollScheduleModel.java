package org.primefaces.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Author: Piotr Turek
 */
public class DefaultEnrollScheduleModel implements EnrollScheduleModel, Serializable {

    private List<EnrollScheduleEvent> events;

    public DefaultEnrollScheduleModel() {
        events = new ArrayList<EnrollScheduleEvent>();
    }

    public DefaultEnrollScheduleModel(List<EnrollScheduleEvent> events) {
        this.events = events;
    }

    public void addEvent(EnrollScheduleEvent event) {
        event.setId(UUID.randomUUID().toString());

        events.add(event);
    }

    public boolean deleteEvent(EnrollScheduleEvent event) {
        return events.remove(event);
    }

    public List<EnrollScheduleEvent> getEvents() {
        return events;
    }

    public EnrollScheduleEvent getEvent(String id) {
        for(EnrollScheduleEvent event : events) {
            if(event.getId().equals(id))
                return event;
        }

        return null;
    }

    public void updateEvent(EnrollScheduleEvent event) {
        int index = -1;

        for(int i = 0 ; i < events.size(); i++) {
            if(events.get(i).getId().equals(event.getId())) {
                index = i;

                break;
            }
        }

        if(index >= 0) {
            events.set(index, event);
        }
    }

    public int getEventCount() {
        return events.size();
    }

    public void clear() {
        events = new ArrayList<EnrollScheduleEvent>();
    }

}
