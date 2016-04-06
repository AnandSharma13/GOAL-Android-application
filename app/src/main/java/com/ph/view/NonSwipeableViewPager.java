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
    //This class makes the view pager non swipeable by default. Use setSwipeLocked to make it swipeable again.
    private boolean swipeLocked=true;

    public NonSwipeableViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        setSwipeLocked(true);
    }


    public boolean getSwipeLocked() {
        return swipeLocked;
    }

    public void setSwipeLocked(boolean swipeLocked) {
        this.swipeLocked = swipeLocked;
    }


    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        // Never allow swiping to switch between pages
        Log.i("ontouch", " intercept touch event ");
        //boolean retval = super.onInterceptTouchEvent(event);
        //Log.i("intercept val", String.valueOf(retval));
        return !swipeLocked&&super.onInterceptTouchEvent(event);

    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.i("ontouch", "touch event");
       // boolean retval = super.onTouchEvent(event);
        //Log.i("Return val",String.valueOf(retval));
        // Never allow swiping to switch between pages

        return !swipeLocked&&super.onTouchEvent(event);
    }

    @Override
    public boolean canScrollHorizontally(int direction) {
        return !swipeLocked&&super.canScrollHorizontally(direction);
    }
}