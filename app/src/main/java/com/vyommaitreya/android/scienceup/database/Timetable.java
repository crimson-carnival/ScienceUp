package com.vyommaitreya.android.scienceup.database;

public class Timetable {
    private String id;
    private String day;
    private String time;
    private String subject;
    private String room;

    public Timetable() {

    }

    public Timetable(String id, String day, String time, String subject, String room) {
        this.id = id;
        this.day = day;
        this.time = time;
        this.subject = subject;
        this.room = room;
    }

    public String getId() {
        return id;
    }

    public String getDay() {
        return day;
    }

    public String getTime() {
        return time;
    }

    public String getSubject() {
        return subject;
    }

    public String getRoom() {
        return room;
    }
}
