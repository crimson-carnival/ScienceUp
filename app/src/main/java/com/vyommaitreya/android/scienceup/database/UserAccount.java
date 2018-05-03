package com.vyommaitreya.android.scienceup.database;

public class UserAccount {
    private String id;
    private Subject subject;

    public UserAccount() {
        ;
    }

    public UserAccount(String id, Subject subject) {
        this.id = id;
        this.subject = subject;
    }

    public String getId() {
        return id;
    }

    public Subject getSubject() {
        return subject;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setSubject(Subject subject) {

        this.subject = subject;
    }
}
