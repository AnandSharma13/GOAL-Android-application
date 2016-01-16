package com.ph.Utils;

import android.content.Context;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.Toast;

/**
 * Created by Anand on 1/16/2016.
 */

public class MyGestureDetector extends GestureDetector.SimpleOnGestureListener {
    private static final int SWIPE_MIN_DISTANCE = 150;
    private static final int SWIPE_MAX_OFF_PATH = 100;
    private static final int SWIPE_THRESHOLD_VELOCITY = 100;
    private MotionEvent mLastOnDownEvent = null;
    Context context;

   public MyGestureDetector(Context context) {
        this.context = context;

    }

    @Override
    public boolean onDown(MotionEvent e) {

        mLastOnDownEvent = e;
        return super.onDown(e);
    }

    @Override
    public boolean onSingleTapConfirmed(MotionEvent e) {
        //TODO - handle edit goal functionality here
        return super.onSingleTapConfirmed(e);
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        if (e1 == null)
            e1 = mLastOnDownEvent;
        if (e1 == null || e2 == null)
            return false;
        float dX = e2.getX() - e1.getX();
        float dY = e1.getY() - e2.getY();
        if (Math.abs(dY) < SWIPE_MAX_OFF_PATH &&
                Math.abs(velocityX) >= SWIPE_THRESHOLD_VELOCITY &&
                Math.abs(dX) >= SWIPE_MIN_DISTANCE) {
            if (dX > 0) {
                Toast.makeText(context, "Right Swipe", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, "Left Swipe", Toast.LENGTH_SHORT).show();
            }
            return true;
        }
        return false;
    }
}