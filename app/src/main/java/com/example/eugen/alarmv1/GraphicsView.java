package com.example.eugen.alarmv1;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.TextView;

/**
 * Created by Eugen on 03.02.2018.
 */

public class GraphicsView extends View {
    private Animation animation;
    private Bitmap bmp;
    private int centerXOffset;
    private int centerYOffset;
    private Intent alarmIntent;
    private Context mContext;
    public GraphicsView(Context context){ //Конструктор для программного создания кастомного View
      super(context);
      bmp = BitmapFactory.decodeResource(getResources(),R.drawable.bell);
      centerXOffset = bmp.getWidth()/2;
      centerYOffset = bmp.getHeight()/2;
      mContext = context;
    }
    public GraphicsView(Context context, AttributeSet attrs){ //Конструктор для внедрения в разметку XML
        super(context,attrs);
        bmp = BitmapFactory.decodeResource(getResources(),R.drawable.bell);
        centerXOffset = bmp.getWidth()/2;
        centerYOffset = bmp.getHeight()/2;
        mContext = context;
    }
    public void createAnimation(Canvas canvas){
      animation = new RotateAnimation(-90,90,canvas.getWidth()/2,canvas.getHeight()/2);
      animation.setRepeatMode(Animation.REVERSE);
      animation.setRepeatCount(Animation.INFINITE);
      animation.setDuration(1000L);
      animation.setInterpolator(new AccelerateDecelerateInterpolator());
      startAnimation(animation);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (animation == null) {
            createAnimation(canvas);
        }
        int centerX = canvas.getWidth() / 2;
        int centerY = canvas.getHeight() / 2;
        canvas.drawBitmap(bmp, centerX - centerXOffset, centerY - centerYOffset, null);
    }

   /* @Override
    public boolean onTouchEvent(MotionEvent event) {
        PointF current = new PointF(event.getX(),event.getY());
        if(event.getAction() == MotionEvent.ACTION_DOWN){
            alarmIntent = new Intent(mContext,AlarmReceiver.class);
            alarmIntent.putExtra(AlarmActivity.SERVICE, false);
            mContext.sendBroadcast(alarmIntent);
        }
        return true;
    }*/
   @Override
   public boolean performClick() {
       super.performClick();
       return true;
   }
}
