package com.example.edcspring.entity;

public class EventData {
    private String EventId;
    private String EventTitle;
    private String AOPs;



    public String getEventId() {
        return EventId;
    }

    public void setEventId(String eventId) {
        EventId = eventId;
    }

    public String getEventTitle() {
        return EventTitle;
    }

    public void setEventTitle(String eventTitle) {
        EventTitle = eventTitle;
    }
    public String getAOPs() {
        return AOPs;
    }

    public void setAOPs(String AOPs) {
        this.AOPs = AOPs;
    }
}
