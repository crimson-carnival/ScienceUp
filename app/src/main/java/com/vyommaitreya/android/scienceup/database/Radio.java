package com.vyommaitreya.android.scienceup.database;

public class Radio {

    private String id, title, artist, description, date;

    public Radio() {

    }

    public Radio(String id, String title, String artist, String description, String date) {
        this.id = id;
        this.title = title;
        this.artist = artist;
        this.description = description;
        this.date = date;
    }

    public String getTitle() {
        return title;
    }

    public String getArtist() {
        return artist;
    }

    public String getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public String getDate() {
        return date;
    }
}
