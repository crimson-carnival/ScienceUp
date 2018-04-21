package com.vyommaitreya.android.scienceup.database;

import android.provider.BaseColumns;

public class TimetableContract {

    public static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + ListEntry.TABLE_NAME + " (" +
                    ListEntry._ID + " INTEGER PRIMARY KEY," +
                    ListEntry.COLUMN_NAME_DAY + " TEXT," +
                    ListEntry.COLUMN_NAME_TIME + " TEXT UNIQUE," +
                    ListEntry.COLUMN_NAME_SUBJECT+ " TEXT," +
                    ListEntry.COLUMN_NAME_ROOM+ " TEXT)";

    public static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + ListEntry.TABLE_NAME;

    private TimetableContract() {
    }

    public static class ListEntry implements BaseColumns {
        public static final String TABLE_NAME = "list";
        public static final String COLUMN_NAME_SUBJECT= "subject";
        public static final String COLUMN_NAME_ROOM= "room";
        public static final String COLUMN_NAME_TIME= "time";
        public static final String COLUMN_NAME_DAY= "day";
    }
}
