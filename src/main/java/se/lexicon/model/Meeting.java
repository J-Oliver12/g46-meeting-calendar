package se.lexicon.model;

import java.sql.Timestamp;
import java.time.LocalDateTime;

public class Meeting {

    private int id;
    private String title;
    private Timestamp startTime;
    private Timestamp endTime;
    private String description;
    private int calendarId;
    private MeetingCalendar calendar;

    public Meeting() {
    }

    public Meeting(String title, Timestamp startTime, Timestamp endTime, String description, int calendarId) {
        this.title = title;
        this.startTime = startTime;
        this.endTime = endTime;
        this.description = description;
        this.calendarId = calendarId;
    }

    public Meeting(int id, String title, Timestamp startTime, Timestamp endTime, String description, int calendarId) {
        this.id = id;
        this.title = title;
        this.startTime = startTime;
        this.endTime = endTime;
        this.description = description;
        this.calendarId = calendarId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Timestamp getStartTime() {
        return startTime;
    }

    public void setStartTime(Timestamp startTime) {
        this.startTime = startTime;
    }

    public Timestamp getEndTime() {
        return endTime;
    }

    public void setEndTime(Timestamp endTime) {
        this.endTime = endTime;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getCalendarId() {
        return calendarId;
    }

    public void setCalendarId(int calendarId) {
        this.calendarId = calendarId;
    }

    public MeetingCalendar getCalendar() {
        return calendar;
    }

    public void setCalendar(MeetingCalendar calendar) {
        this.calendar = calendar;
    }

    public String meetingInfo() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Meeting Info:").append("\n");
        stringBuilder.append("Id ").append(id).append("\n");
        stringBuilder.append("Title").append(title).append("\n");
        stringBuilder.append("StartTime ").append("StartTime ").append(startTime).append("\n");
        stringBuilder.append("EndTime ").append(endTime).append("\n");
        stringBuilder.append("Description ").append(description).append("\n");
        stringBuilder.append("CalendarId: ").append(calendarId).append("\n");
        return stringBuilder.toString();
    }

    private void timeValidation() {

    }


}
