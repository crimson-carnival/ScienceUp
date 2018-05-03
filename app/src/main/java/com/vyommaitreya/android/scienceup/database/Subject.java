package com.vyommaitreya.android.scienceup.database;

public class Subject {
    private String name, teacher, id;

    public Subject() {
        ;
    }

    public Subject(String name, String teacher, String id) {
        this.name = name;
        this.teacher = teacher;
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
        return teacher;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setTeacher(String teacher) {
        this.teacher = teacher;
    }
}
