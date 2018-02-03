package com.example.eugen.alarmv1;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

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
      //Toast.makeText(context,"It works",Toast.LENGTH_LONG).show();
        //Запуск службы надо сделать
        isServiceStarted  = intent.getBooleanExtra(AlarmActivity.SERVICE,false);
        uuid = (UUID)intent.getSerializableExtra(MainActivity.UUID);
        Intent intent1 = new Intent(context,MyService.class);
        intent1.putExtra(MainActivity.UUID,uuid);
        if(isServiceStarted) {
            context.startService(intent1);
            if(Build.VERSION.SDK_INT>=19){
                am = (AlarmManager)context.getSystemService(ALARM_SERVICE);
                alarmIntent = new Intent(context,AlarmReceiver.class);
                alarmIntent.putExtra(AlarmActivity.SERVICE,true);
                alarmIntent.putExtra(MainActivity.UUID,uuid);
                PendingIntent pendingIntent = PendingIntent.getBroadcast(context,uuid.hashCode(),alarmIntent,0);
                Alarm alarm = ApplicationModel.getInstance(context).getAlarm(uuid);
                Calendar calendar = Calendar.getInstance();
                am.setExact(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis()+24*60*60*1000,pendingIntent);
            }
        }
        else
            context.stopService(intent1);
    }
}
