package com.ph.view;


import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

public class NonSwipeableViewPager extends ViewPager {

    public NonSwipeableViewPager(Context context) {
        super(context);
    }
    float x1, x2, y1, y2, dx, dy;
    String direction;

    public NonSwipeableViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }



    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        // Never allow swiping to switch between pages
        Log.i("ontouch", " intercept touch event ");
        //boolean retval = super.onInterceptTouchEvent(event);
        //Log.i("intercept val", String.valueOf(retval));
        return super.onInterceptTouchEvent(event);
    }


    private boolean isLeftRight(MotionEvent event)
    {

        switch(event.getAction()) {
            case(MotionEvent.ACTION_DOWN):
                x1 = event.getX();
                y1 = event.getY();
                break;
            case(MotionEvent.ACTION_MOVE): {
                x2 = event.getX();
                y2 = event.getY();
                dx = x2-x1;
                dy = y2-y1;

                // Use dx and dy to determine the direction
                if(Math.abs(dx) > Math.abs(dy)) {
                   // if(dx>0) direction = "right";
                    //else direction = "left";
                    return true;
                } else {
                   // if(dy>0) direction = "down";
                    //else direction = "up";
                    return false;
                }
                }
        }
        return false;
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.i("ontouch", "touch event");
       // boolean retval = super.onTouchEvent(event);
        //Log.i("Return val",String.valueOf(retval));
        // Never allow swiping to switch between pages

        return super.onTouchEvent(event);
    }
}