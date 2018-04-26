package com.vyommaitreya.android.scienceup.activities;

public class TestArtist {
    public String name, genre;

    TestArtist() {

    }

    TestArtist(String name, String genre) {
        this.name = name;
        this.genre = genre;
    }

    public String getName() {
        return name;
    }

    public String getGenre() {
        return genre;
    }
}
