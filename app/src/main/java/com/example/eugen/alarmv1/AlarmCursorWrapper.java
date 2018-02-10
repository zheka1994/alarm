package com.example.eugen.alarmv1;

import android.database.Cursor;
import android.database.CursorWrapper;

import java.util.UUID;

/**
 * Created by Eugen on 31.01.2018.
 */

public class AlarmCursorWrapper extends CursorWrapper {
    public AlarmCursorWrapper(Cursor cursor) {
        super(cursor);
    }
    public Alarm getAlarm(){
        String uuid = getString(getColumnIndex(MyDbContract.AlarmsTable.COLUMN_UUID));
        String name = getString(getColumnIndex(MyDbContract.AlarmsTable.COLUMN_NAME));
        int hours = getInt(getColumnIndex(MyDbContract.AlarmsTable.COLUMN_HOURS));
        int minutes = getInt(getColumnIndex(MyDbContract.AlarmsTable.COLUMN_MINUTES));
        int state = getInt(getColumnIndex(MyDbContract.AlarmsTable.COLUMN_STATE));
        String melodie = getString(getColumnIndex(MyDbContract.AlarmsTable.COLUMN_MELODIE));
        String daysOfWeek = getString(getColumnIndex(MyDbContract.AlarmsTable.COLUMN_DAYSOFWEEK));
        Alarm alarm = new Alarm(UUID.fromString(uuid));
        alarm.setName(name);
        alarm.setHours(hours);
        alarm.setMinutes(minutes);
        alarm.setState(state);
        alarm.setMelodie(melodie);
        alarm.setDaysOfWeek(daysOfWeek);
        return alarm;
    }
}
