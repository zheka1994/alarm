package com.example.eugen.alarmv1;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.IBinder;
import android.os.PowerManager;
import android.util.Log;

import java.util.UUID;

public class MyService extends Service {
    private MediaPlayer mediaPlayer;
    private Alarm alarm;
    private boolean isOn;
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        UUID uuid = (UUID)intent.getSerializableExtra(MainActivity.UUID);
        alarm = ApplicationModel.getInstance(getApplicationContext()).getAlarm(uuid);
        String uri = alarm.getMelodie();
        Uri URI = Uri.parse(uri);
        mediaPlayer = MediaPlayer.create(this,URI);
        mediaPlayer.setLooping(true);
        mediaPlayer.start();
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_SCREEN_ON);
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        filter.addAction("SOME_OTHER_ACTION");
        this.registerReceiver(broadcastReceiver,filter);
        NotificationManager notificationManager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
        //Intent activitystartintent = new Intent(this,AlarmStopActivity.class);
        //NEW
        Intent activitystartintent = new Intent(this,AnimationStopActivity.class);
        //
        activitystartintent.putExtra(MainActivity.UUID,uuid);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,0,activitystartintent,0);
        Notification notification = new Notification.Builder(this)
                .setContentTitle("Alarm off")
                .setContentText("Click me")
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .setSmallIcon(android.R.drawable.sym_def_app_icon)
                .build();
        notificationManager.notify(0,notification);
        if(!isOn){
            PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
            PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP, "tag");
            wl.acquire();
        }
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mediaPlayer.stop();
    }

    @Override
    public IBinder onBind(Intent intent) {
       return null;
    }
    //Ресивер для включения экрана если он выключен
    private final BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getAction().equals(Intent.ACTION_SCREEN_ON)) {
                isOn = true;
                Log.i("SCRENN_STATE",""+isOn);
            }
            else if(intent.getAction().equals(Intent.ACTION_SCREEN_OFF))
                isOn = false;
                 Log.i("SCRENN_STATE",""+isOn);
        }
    };
}
