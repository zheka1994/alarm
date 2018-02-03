package com.example.eugen.alarmv1;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;

public class AnimationStopActivity extends AppCompatActivity {
    private GraphicsView graphicsView;
    private Intent alarmIntent;
    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animation_stop);
        //setContentView(new GraphicsView(this));
        graphicsView = (GraphicsView)findViewById(R.id.graphics);
        graphicsView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if(motionEvent.getAction()==MotionEvent.ACTION_DOWN){
                    alarmIntent = new Intent(AnimationStopActivity.this,AlarmReceiver.class);
                    alarmIntent.putExtra(AlarmActivity.SERVICE, false);
                    sendBroadcast(alarmIntent);
                    finish();
                }
                return true;
            }
        });
    }
}
