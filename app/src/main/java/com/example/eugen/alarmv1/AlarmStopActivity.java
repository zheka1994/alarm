package com.example.eugen.alarmv1;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


public class AlarmStopActivity extends AppCompatActivity {
    private Button stopButton;
    private Intent alarmIntent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_stop);
        alarmIntent = new Intent(this,AlarmReceiver.class);
        stopButton = (Button)findViewById(R.id.buttonStop);
        stopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    alarmIntent.putExtra(AlarmActivity.SERVICE, false);
                    sendBroadcast(alarmIntent);
                    finish();
            }
        });
    }
}
