package com.example.aep;

public class EventData {

    private String teamName;
    private String eventLocation;
    private String eventDate;
    private String eventTime;

    // Constructors, getters, setters...

    // Example constructor
    public EventData(String teamName, String eventLocation, String eventDate, String eventTime) {
        this.teamName = teamName;
        this.eventLocation = eventLocation;
        this.eventDate = eventDate;
        this.eventTime = eventTime;
    }

    public String getTeamName() {
        return teamName;
    }

    public String getEventLocation() {
        return eventLocation;
    }

    public void setEventLocation(String eventLocation) {
        this.eventLocation = eventLocation;
    }

    public String getEventDate() {
        return eventDate;
    }

    public void setEventDate(String eventDate) {
        this.eventDate = eventDate;
    }

    public String getEventTime() {
        return eventTime;
    }

    public void setEventTime(String eventTime) {
        this.eventTime = eventTime;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }
}
