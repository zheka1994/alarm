package com.example.eugen.alarmv1;

import android.provider.BaseColumns;

/**
 * Created by Eugen on 31.01.2018.
 */

public class MyDbContract {
    MyDbContract(){}
    public static class AlarmsTable implements BaseColumns{
        public static final String TABLE_NAME = "alarms";
        public static final String COLUMN_UUID = "uuid";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_HOURS = "hours";
        public static final String COLUMN_MINUTES = "minutes";
        public static final String COLUMN_STATE = "state";
        public static final String COLUMN_MELODIE = "melodie";
        public static final String COLUMN_DAYSOFWEEK = "daysofweek";
        public static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " +
                TABLE_NAME + " (" +
                _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_UUID + " TEXT, " +
                COLUMN_NAME + " TEXT, " +
                COLUMN_HOURS + " INTEGER, " +
                COLUMN_MINUTES + " INTEGER, " +
                COLUMN_STATE + " INTEGER, " +
                COLUMN_MELODIE + " TEXT," +
                COLUMN_DAYSOFWEEK + " TEXT" +
                ")";
    }
}
