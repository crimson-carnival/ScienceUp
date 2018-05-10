package com.vyommaitreya.android.scienceup.database;

public class Attendance {
    private int total, attended;

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getAttended() {
        return attended;
    }

    public void setAttended(int attended) {
        this.attended = attended;
    }

    public Attendance(int total, int attended) {

        this.total = total;
        this.attended = attended;
    }

    public Attendance() {

    }
}
