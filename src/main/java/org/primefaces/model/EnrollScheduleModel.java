package org.primefaces.model;

import java.util.List;

/**
 * Author: Piotr Turek
 */
public interface EnrollScheduleModel {

    public void addEvent(EnrollScheduleEvent event);

    public boolean deleteEvent(EnrollScheduleEvent event);

    public List<EnrollScheduleEvent> getEvents();

    public EnrollScheduleEvent getEvent(String id);

    public void updateEvent(EnrollScheduleEvent event);

    public int getEventCount();

    public boolean isPeriodic();

    public void clear();

}
