package com.example.eugen.alarmv1;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.example.eugen.alarmv1.MyDbContract.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by Eugen on 31.01.2018.
 */

public class ApplicationModel {
    private static Context context;
    private static ApplicationModel applicationModel = null;
    SQLiteDatabase db;

    public static ApplicationModel getInstance(Context context){
        if(applicationModel == null){
            applicationModel = new ApplicationModel(context);
        }
        return applicationModel;
    }

    private ApplicationModel(Context context){
        this.context = context.getApplicationContext();
        db = new AlarmDbHelper(this.context).getWritableDatabase();
    }

    public void addRow(Alarm alarm){
        db.insert(AlarmsTable.TABLE_NAME, null, getContentValues(alarm));
    }

    public void updateRow(Alarm alarm){
        String uuid = alarm.getId().toString();
        ContentValues cv = getContentValues(alarm);
        db.update(AlarmsTable.TABLE_NAME,cv,AlarmsTable.COLUMN_UUID+"=?",new String[]{uuid});

    }

    public void deleteRow(Alarm alarm){
        String uuid = alarm.getId().toString();
        db.delete(AlarmsTable.TABLE_NAME,AlarmsTable.COLUMN_UUID + "=?",new String[]{uuid});
    }

    public Alarm getAlarm(UUID uuid){
        AlarmCursorWrapper cursor = queryAlarms(AlarmsTable.COLUMN_UUID+"=?",new String[]{uuid.toString()});
        try {
            if (cursor.moveToFirst())
                return cursor.getAlarm();
            return null;
        }
        finally {
            cursor.close();
        }
    }

    private AlarmCursorWrapper queryAlarms(String whereClause, String[] whereArgs){
        Cursor cursor = db.query(AlarmsTable.TABLE_NAME,null,whereClause,whereArgs,null,null,null);
        return new AlarmCursorWrapper(cursor);
    }

    public List<Alarm> getAlarms(){
        List<Alarm> alarms = new ArrayList<>();
        AlarmCursorWrapper cursor = queryAlarms(null,null);
        try{
            cursor.moveToFirst();
            while(!cursor.isAfterLast()){
                alarms.add(cursor.getAlarm());
                cursor.moveToNext();
            }
        }finally{
            cursor.close();
        }
        return alarms;
    }

    public ContentValues getContentValues(Alarm alarm){
        ContentValues cv = new ContentValues();
        cv.put(AlarmsTable.COLUMN_UUID,alarm.getId().toString());
        cv.put(AlarmsTable.COLUMN_NAME,alarm.getName());
        cv.put(AlarmsTable.COLUMN_HOURS,alarm.getHours());
        cv.put(AlarmsTable.COLUMN_MINUTES,alarm.getMinutes());
        cv.put(AlarmsTable.COLUMN_STATE,alarm.getState());
        cv.put(AlarmsTable.COLUMN_MELODIE,alarm.getMelodie());
        cv.put(AlarmsTable.COLUMN_DAYSOFWEEK,alarm.getDaysOfWeek());
        return cv;
    }
}
