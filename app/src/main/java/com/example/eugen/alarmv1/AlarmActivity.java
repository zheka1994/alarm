package com.example.eugen.alarmv1;

import android.Manifest;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;
import java.util.UUID;

public class AlarmActivity extends AppCompatActivity implements View.OnClickListener, ChoiseSourseDialogFragment.ChoiseSourseDialogFragmentListener,AppMusicChoiseDialogFragment.AppMusicChoiseDialogFragmentListener, ChooseDaysDialogFragment.ChooseDaysOfWeekListener{
    private TimePicker timePicker;
    private EditText nameEditText;
    private Button choiseMusicButton;
    private Button choiseDaysOfWeekButton;
    private Button okButton;
    private Button cancelButton;
    private int action;
    private UUID uuid = null;
    private Alarm alarm;
    public static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 123;
    private StringBuffer choise;
    private int State;
    private AlarmManager alarmManager;
    private Intent alarmIntent;
    public static final  String SERVICE = "SERVICE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);
        timePicker = (TimePicker)findViewById(R.id.timePicker);
        timePicker.setIs24HourView(true);
        nameEditText = (EditText)findViewById(R.id.nameEditText);
        choiseMusicButton = (Button)findViewById(R.id.ChoiseRingtonebutton);
        okButton = (Button)findViewById(R.id.OkButton);
        cancelButton = (Button)findViewById(R.id.CancelButton);
        choiseDaysOfWeekButton = (Button)findViewById(R.id.ChoiseDayOfWeek);
        choiseMusicButton.setOnClickListener(this);
        okButton.setOnClickListener(this);
        cancelButton.setOnClickListener(this);
        choiseDaysOfWeekButton.setOnClickListener(this);
        action = getIntent().getIntExtra(MainActivity.ACTION,0);
        Uri uri = null;
        if(action == 0){
            alarm = new Alarm();
            State = 0;
            uri = Uri.parse("android.resource://com.example.eugen.alarmv1/"+R.raw.alarm1);
        }
        else if(action == 1){
          uuid = (UUID)getIntent().getSerializableExtra(MainActivity.UUID);
          alarm = ApplicationModel.getInstance(getApplicationContext()).getAlarm(uuid);
          State = alarm.getState();
          nameEditText.setText(alarm.getName());
          if(alarm.getMelodie()!= null)
              uri = Uri.parse(alarm.getMelodie());
          else
              uri = Uri.parse("android.resource://com.example.eugen.alarmv1/"+R.raw.alarm1);
          if(Build.VERSION.SDK_INT >= 23){
              timePicker.setHour(alarm.getHours());
              timePicker.setMinute(alarm.getMinutes());
          }
          else{
              timePicker.setCurrentHour(alarm.getHours());
              timePicker.setCurrentMinute(alarm.getMinutes());
          }
        }

       choise = new StringBuffer(uri.toString());
       alarmManager = (AlarmManager)getSystemService(ALARM_SERVICE);
       alarmIntent = new Intent(this,AlarmReceiver.class);
    }

    @Override
    public void onClick(View view) {
        //Обработка нажатий кнопок
        if(view == choiseMusicButton){
            DialogFragment dialogFragment = new ChoiseSourseDialogFragment();
            dialogFragment.show(getSupportFragmentManager(),"ChoiseMusic");
        }
        else if(view == choiseDaysOfWeekButton){
            //Выбираем дни недели
            DialogFragment dialogFragment = null;
            switch (action) {
                case 0:
                    dialogFragment = ChooseDaysDialogFragment.newInstance(null, 0);
                    break;
                case 1:
                    dialogFragment = ChooseDaysDialogFragment.newInstance(alarm.getId(), 1);
                    break;
            }
                dialogFragment.show(getSupportFragmentManager(), "ChooseDays");
        }
        else if(view == okButton){
            String name = nameEditText.getText().toString();
            Calendar calendar = Calendar.getInstance();
            if(Build.VERSION.SDK_INT >= 23){
                calendar.set(Calendar.HOUR_OF_DAY,timePicker.getHour());
                calendar.set(Calendar.MINUTE,timePicker.getMinute());
            }
            else{
                calendar.set(Calendar.HOUR_OF_DAY,timePicker.getCurrentHour());
                calendar.set(Calendar.MINUTE,timePicker.getCurrentMinute());
            }
            calendar.set(Calendar.SECOND,0);
            alarm.setName(name);
            alarm.setHours(calendar.get(Calendar.HOUR_OF_DAY));
            alarm.setMinutes(calendar.get(Calendar.MINUTE));
            alarm.setState(State);
            if (action == 0) {
                alarm.setMelodie(choise.toString());
                ApplicationModel.getInstance(getApplicationContext()).addRow(alarm);
                if(alarm.getState() == 1){
                    startAlarm(calendar);
                }
            }
            else if(action == 1){
                alarm.setMelodie(choise.toString());
                ApplicationModel.getInstance(getApplicationContext()).updateRow(alarm);
                if(alarm.getState() == 0){
                    stopAlarm();
                }
                else{
                    startAlarm(calendar);
                }
            }
            finish();
        }
        else if(view == cancelButton){
            finish();
        }
    }

    @Override
    public void onFinishEditDialog(int v) {
        switch (v){
            case 0: //с карты памяти
                if (checkPermissionREAD_EXTERNAL_STORAGE(AlarmActivity.this)){
                    Intent i = new Intent(Intent.ACTION_GET_CONTENT);
                    i.setType("audio/mp3");
                    if (i.resolveActivity(getPackageManager()) != null)
                        startActivityForResult(i, 1);
                }
                    break;
            case 1: // музыка приложения
                DialogFragment dialogFragment = new AppMusicChoiseDialogFragment();
                dialogFragment.show(getSupportFragmentManager(),"ChoiseMusic1");
                break;
        }
    }
    @Override
    public void onFinishDialog(int v) {
        Uri fileUri = null;
        switch(v){
            case 0:
                fileUri = Uri.parse("android.resource://com.example.eugen.alarmv1/"+R.raw.alarm1);
                break;
            case 1:
                fileUri = Uri.parse("android.resource://com.example.eugen.alarmv1/"+R.raw.alarm2);
                break;
            case 2:
                fileUri = Uri.parse("android.resource://com.example.eugen.alarmv1/"+R.raw.alarm3);
                break;
            case 3:
                fileUri = Uri.parse("android.resource://com.example.eugen.alarmv1/"+R.raw.alarm4);
                break;
        }
        if(fileUri != null)
        {
            choise.setLength(0);
            choise.append(fileUri.toString());
        }
    }
    //Разрешения
    public boolean checkPermissionREAD_EXTERNAL_STORAGE(final Context context) {
        int currentAPIVersion = Build.VERSION.SDK_INT;
        if (currentAPIVersion >= android.os.Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(
                        (Activity) context, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    showDialog("External storage", context,Manifest.permission.READ_EXTERNAL_STORAGE);

                } else {
                    ActivityCompat
                            .requestPermissions(
                                    (Activity) context,
                                    new String[] { Manifest.permission.READ_EXTERNAL_STORAGE },
                                    MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                }
                return false;
            } else {
                return true;
            }

        } else {
            return true;
        }
    }

    public void showDialog(final String msg, final Context context, final String permission) {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);
        alertBuilder.setCancelable(true);
        alertBuilder.setTitle("Permission necessary");
        alertBuilder.setMessage(msg + " permission is necessary");
        alertBuilder.setPositiveButton(android.R.string.yes,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        ActivityCompat.requestPermissions((Activity) context,
                                new String[] { permission },
                                MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                    }
                });
        AlertDialog alert = alertBuilder.create();
        alert.show();
    }
    //
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == 1 & data != null){
            Uri fileUri = data.getData();
            choise.setLength(0);
            choise.append(fileUri.toString());
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.alarm_menu, menu);
        Switch sw = (Switch)menu.findItem(R.id.menuShowDue).getActionView();
        sw.setText("Включить/Отключить");
        if(action == 1){
            if(alarm.getState() == 1)
            sw.setChecked(true);
        }
        sw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b)
                    State = 1;
                else
                    State = 0;
            }
        });
        return true;
    }
    public void startAlarm(Calendar calendar){
       alarmIntent.putExtra(SERVICE,true);
       alarmIntent.putExtra(MainActivity.UUID,alarm.getId());
       PendingIntent pendingIntent = PendingIntent.getBroadcast(this,alarm.getId().hashCode(),alarmIntent,0);
       DayWeek dayWeek = new DayWeek(calendar);
       int next = dayWeek.getNextAlarm(alarm);
        if(next != -1) {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis() + next * 24 * 60 * 60 * 1000, pendingIntent);
                int t = calendar.get(Calendar.DAY_OF_MONTH)+next;
                calendar.set(Calendar.DAY_OF_MONTH,t);
                long timeUp = calendar.getTimeInMillis();
                long diff = timeUp - System.currentTimeMillis();
                long diffSeconds = diff / 1000 % 60;
                long diffMinutes = diff / (60 * 1000) % 60;
                long diffHours = diff / (60 * 60 * 1000) % 24;
                long diffDays = diff / (24 * 60 * 60 * 1000);
                StringBuilder sb = new StringBuilder();
                sb.append(this.getResources().getQuantityString(R.plurals.days,(int)diffDays,(int)diffDays));
                sb.append(", ");
                sb.append(this.getResources().getQuantityString(R.plurals.hours,(int)diffHours,(int)diffHours));
                sb.append(", ");
                sb.append(this.getResources().getQuantityString(R.plurals.minutes,(int)diffMinutes,(int)diffMinutes));
                sb.append(", ");
                sb.append(this.getResources().getQuantityString(R.plurals.seconds,(int)diffSeconds,(int)diffSeconds));
                Toast.makeText(this,getString(R.string.alarmthrow)+" "+sb.toString(),Toast.LENGTH_LONG).show();
        }
    }
    public void stopAlarm(){
      if(alarmManager!=null){
            PendingIntent pendingIntent = PendingIntent.getBroadcast(this,alarm.getId().hashCode(),alarmIntent,0);
            alarmManager.cancel(pendingIntent);
            if(alarmIntent != null){
                alarmIntent.putExtra(SERVICE,false);
                sendBroadcast(alarmIntent);
            }
        }
    }

    @Override
    public void onFinishChooseDay(String s) {
        alarm.setDaysOfWeek(s);
    }
}
