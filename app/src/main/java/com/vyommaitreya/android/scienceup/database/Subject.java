package com.vyommaitreya.android.scienceup.database;

public class Subject {
    private String name, course, id;

    public Subject() {
        ;
    }

    public Subject(String name, String course, String id) {
        this.name = name;
        this.course = course;
        this.id = id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {

        return id;
    }

    public String getName() {

        return name;
    }

    public String getTeacher() {
        return course;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setTeacher(String course) {
        this.course = course;
    }
}
