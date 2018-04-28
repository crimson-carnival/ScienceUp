package com.vyommaitreya.android.scienceup.database;

public class Feedback {
    private String id, date, feedback, author;

    public Feedback() {

    }

    public Feedback(String id, String feedback, String author, String date) {
        this.id = id;
        this.date = date;
        this.feedback = feedback;
        this.author = author;
    }

    public String getId() {
        return id;
    }

    public String getDate() {
        return date;
    }

    public String getFeedback() {
        return feedback;
    }

    public String getAuthor() {
        return author;
    }
}
