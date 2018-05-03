package com.vyommaitreya.android.scienceup.database;

public class Course {
    private String id, course_name, course_coordinator;

    public Course() {
    }

    public Course(String id, String course_name, String course_coordinator) {
        this.id = id;
        this.course_name = course_name;
        this.course_coordinator = course_coordinator;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setCourse_name(String course_name) {
        this.course_name = course_name;
    }

    public void setCourse_coordinator(String course_coordinator) {
        this.course_coordinator = course_coordinator;
    }

    public String getId() {

        return id;
    }

    public String getCourse_name() {
        return course_name;
    }

    public String getCourse_coordinator() {
        return course_coordinator;
    }
}
