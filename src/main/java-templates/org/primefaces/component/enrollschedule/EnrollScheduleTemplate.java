import java.util.Iterator;
import java.util.Map;
import java.util.HashMap;
import java.util.Date;
import java.util.Calendar;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.TimeZone;
import javax.faces.component.UIComponent;
import javax.faces.event.FacesEvent;
import javax.faces.event.AjaxBehaviorEvent;
import org.primefaces.util.Constants;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.EnrollScheduleEntryMoveEvent;
import org.primefaces.event.EnrollScheduleEntryResizeEvent;
import org.primefaces.event.MultiImpossibleEvent;
import org.primefaces.model.EnrollScheduleModel;
import org.primefaces.model.EnrollScheduleEvent;
import java.util.StringTokenizer;
import java.util.List;
import java.util.LinkedList;
import org.primefaces.event.MultiImpossibleEvent;

private static final Collection<String>EVENT_NAMES=Collections.unmodifiableCollection(Arrays.asList("dateSelect","eventSelect","eventMultiImpossible","eventMove","eventResize"));

private java.util.Locale appropriateLocale;

java.util.Locale calculateLocale(FacesContext facesContext){
        if(appropriateLocale==null){
        Object userLocale=getLocale();
if(userLocale!=null){
        if(userLocale instanceof String)
        appropriateLocale=new java.util.Locale((String)userLocale,"");
else if(userLocale instanceof java.util.Locale)
        appropriateLocale=(java.util.Locale)userLocale;
else
        throw new IllegalArgumentException("Type:"+userLocale.getClass()+" is not a valid locale type for calendar:"+this.getClientId(facesContext));
}else{
        appropriateLocale=facesContext.getViewRoot().getLocale();
}
        }

        return appropriateLocale;
}

private TimeZone appropriateTimeZone;

public java.util.TimeZone calculateTimeZone(){
        if(appropriateTimeZone==null){
        Object usertimeZone=getTimeZone();
if(usertimeZone!=null){
        if(usertimeZone instanceof String)
        appropriateTimeZone=TimeZone.getTimeZone((String)usertimeZone);
else if(usertimeZone instanceof java.util.TimeZone)
        appropriateTimeZone=(TimeZone)usertimeZone;
else
        throw new IllegalArgumentException("TimeZone could be either String or java.util.TimeZone");
}else{
        appropriateTimeZone=TimeZone.getDefault();
}
        }

        return appropriateTimeZone;
}

@Override
public void queueEvent(FacesEvent event){
        FacesContext context=getFacesContext();
Map<String, String>params=context.getExternalContext().getRequestParameterMap();
String eventName=params.get(Constants.PARTIAL_BEHAVIOR_EVENT_PARAM);
String clientId=this.getClientId(context);
TimeZone tz=calculateTimeZone();

if(isSelfRequest(context)){

        AjaxBehaviorEvent behaviorEvent=(AjaxBehaviorEvent)event;
FacesEvent wrapperEvent=null;

if(eventName.equals("dateSelect")){
        Calendar calendar=Calendar.getInstance();
calendar.setTimeInMillis(Long.valueOf(params.get(clientId+"_selectedDate")));
calendar.setTimeZone(tz);
Date selectedDate=calendar.getTime();
SelectEvent selectEvent=new SelectEvent(this,behaviorEvent.getBehavior(),selectedDate);
selectEvent.setPhaseId(behaviorEvent.getPhaseId());

wrapperEvent=selectEvent;
}
        else if(eventName.equals("eventSelect")){
        String selectedEventId=params.get(clientId+"_selectedEventId");
EnrollScheduleEvent selectedEvent=this.getValue().getEvent(selectedEventId);

wrapperEvent=new SelectEvent(this,behaviorEvent.getBehavior(),selectedEvent);
}
        else if(eventName.equals("eventMultiImpossible")){
        String selectedEventIds=params.get(clientId+"_selectedEventIds");
StringTokenizer tokenizer=new StringTokenizer(selectedEventIds,",");
Collection<EnrollScheduleEvent>events=new LinkedList<EnrollScheduleEvent>();
while(tokenizer.hasMoreTokens()){
        events.add(this.getValue().getEvent(tokenizer.nextToken()));
}

        wrapperEvent=new MultiImpossibleEvent(this,behaviorEvent.getBehavior(),events,params.get("form:multiReasonArea"));

}
        else if(eventName.equals("eventMove")){
        String movedEventId=params.get(clientId+"_movedEventId");
EnrollScheduleEvent movedEvent=this.getValue().getEvent(movedEventId);
int dayDelta=Integer.valueOf(params.get(clientId+"_dayDelta"));
int minuteDelta=Integer.valueOf(params.get(clientId+"_minuteDelta"));

Calendar calendar=Calendar.getInstance();
calendar.setTime(movedEvent.getStartDate());
calendar.setTimeZone(tz);
calendar.add(Calendar.DATE,dayDelta);
calendar.add(Calendar.MINUTE,minuteDelta);
movedEvent.getStartDate().setTime(calendar.getTimeInMillis());

calendar=Calendar.getInstance();
calendar.setTime(movedEvent.getEndDate());
calendar.setTimeZone(tz);
calendar.add(Calendar.DATE,dayDelta);
calendar.add(Calendar.MINUTE,minuteDelta);
movedEvent.getEndDate().setTime(calendar.getTimeInMillis());

wrapperEvent=new EnrollScheduleEntryMoveEvent(this,behaviorEvent.getBehavior(),movedEvent,dayDelta,minuteDelta);
}
        else if(eventName.equals("eventResize")){
        String resizedEventId=params.get(clientId+"_resizedEventId");
EnrollScheduleEvent resizedEvent=this.getValue().getEvent(resizedEventId);
int dayDelta=Integer.valueOf(params.get(clientId+"_dayDelta"));
int minuteDelta=Integer.valueOf(params.get(clientId+"_minuteDelta"));

Calendar calendar=Calendar.getInstance();
calendar.setTime(resizedEvent.getEndDate());
calendar.setTimeZone(tz);
calendar.add(Calendar.DATE,dayDelta);
calendar.add(Calendar.MINUTE,minuteDelta);
resizedEvent.getEndDate().setTime(calendar.getTimeInMillis());

wrapperEvent=new EnrollScheduleEntryResizeEvent(this,behaviorEvent.getBehavior(),resizedEvent,dayDelta,minuteDelta);
}

        wrapperEvent.setPhaseId(behaviorEvent.getPhaseId());

super.queueEvent(wrapperEvent);
}
        else{
        super.queueEvent(event);
}
        }

private boolean isSelfRequest(FacesContext context){
        return this.getClientId(context).equals(context.getExternalContext().getRequestParameterMap().get(Constants.PARTIAL_SOURCE_PARAM));
}

@Override
public Collection<String>getEventNames(){
        return EVENT_NAMES;
}