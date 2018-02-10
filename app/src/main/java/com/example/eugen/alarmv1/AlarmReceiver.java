package com.example.eugen.alarmv1;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import java.util.Calendar;
import java.util.UUID;

import static android.content.Context.ALARM_SERVICE;

public class AlarmReceiver extends BroadcastReceiver {
    private boolean isServiceStarted = false;
    private UUID uuid;
    private AlarmManager am;
    private Intent alarmIntent;
    @Override
    public void onReceive(Context context, Intent intent) {
        isServiceStarted  = intent.getBooleanExtra(AlarmActivity.SERVICE,false);
        uuid = (UUID)intent.getSerializableExtra(MainActivity.UUID);
        Intent intent1 = new Intent(context,MyService.class);
        intent1.putExtra(MainActivity.UUID,uuid);
        if(isServiceStarted) {
            context.startService(intent1);
            am = (AlarmManager)context.getSystemService(ALARM_SERVICE);
            alarmIntent = new Intent(context,AlarmReceiver.class);
            alarmIntent.putExtra(AlarmActivity.SERVICE,true);
            alarmIntent.putExtra(MainActivity.UUID,uuid);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context,uuid.hashCode(),alarmIntent,0);
            Calendar calendar = Calendar.getInstance();
            Alarm alarm = ApplicationModel.getInstance(context).getAlarm(uuid);
            Calendar calendar1 = Calendar.getInstance();
            calendar1.set(Calendar.HOUR_OF_DAY,alarm.getHours());
            calendar1.set(Calendar.MINUTE,alarm.getMinutes());
            DayWeek dayWeek = new DayWeek(calendar);
            int next = dayWeek.getNextAlarm(alarm);
            am.setExact(AlarmManager.RTC_WAKEUP,calendar1.getTimeInMillis()+next*24*60*60*1000,pendingIntent);
        }
        else
            context.stopService(intent1);
    }

}
