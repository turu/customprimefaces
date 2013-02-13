package org.primefaces.event;

import org.primefaces.model.EnrollScheduleEvent;

import javax.faces.component.UIComponent;
import javax.faces.component.behavior.Behavior;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.event.AjaxBehaviorListener;
import javax.faces.event.FacesListener;

/**
 * Author: Piotr Turek
 */
public class EnrollScheduleEntryMoveEvent extends AjaxBehaviorEvent {

    private EnrollScheduleEvent scheduleEvent;

    private int dayDelta;

    private int minuteDelta;

    public EnrollScheduleEntryMoveEvent(UIComponent component, Behavior behavior, EnrollScheduleEvent scheduleEvent, int dayDelta, int minuteDelta) {
        super(component, behavior);
        this.scheduleEvent = scheduleEvent;
        this.dayDelta = dayDelta;
        this.minuteDelta = minuteDelta;
    }

    @Override
    public boolean isAppropriateListener(FacesListener faceslistener) {
        return (faceslistener instanceof AjaxBehaviorListener);
    }

    @Override
    public void processListener(FacesListener faceslistener) {
        ((AjaxBehaviorListener) faceslistener).processAjaxBehavior(this);
    }

    public EnrollScheduleEvent getScheduleEvent() {
        return scheduleEvent;
    }

    public int getDayDelta() {
        return dayDelta;
    }

    public int getMinuteDelta() {
        return minuteDelta;
    }

}
