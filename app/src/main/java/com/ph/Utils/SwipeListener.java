package com.ph.Utils;

import android.content.Context;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.ph.R;

/**
 * Created by Anand on 1/15/2016.
 */
public class SwipeListener implements View.OnTouchListener {
    ListView mHomeList;
    private GestureDetector mGestureDetector;
    private Context mContext;

    public SwipeListener() {
        super();
    }

    public SwipeListener(Context context, ListView listView) {
        mGestureDetector = new GestureDetector(context, new GestureListener());
        this.mContext = context;
        this.mHomeList = listView;
    }


    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return mGestureDetector.onTouchEvent(event);
    }
    public void onSwipeRight(int pos) {

    }

    public void onSwipeLeft(int pos) {
        showDeleteButton(pos);
    }
    private boolean showDeleteButton(int pos) {
        View child = mHomeList.getChildAt(pos);
        if (child != null){
            Button delete = (Button) child.findViewById(R.id.b_edit_in_list);
            Log.i("ShowDeleteButton", "before if");
            if (delete != null)
                if (delete.getVisibility() == View.INVISIBLE)
                    delete.setVisibility(View.VISIBLE);
                else
                    delete.setVisibility(View.INVISIBLE);
            return true;
        }
        return false;
    }

    private final class GestureListener extends GestureDetector.SimpleOnGestureListener {

        private static final int SWIPE_THRESHOLD = 100;
        private static final int SWIPE_VELOCITY_THRESHOLD = 100;

        @Override
        public boolean onDown(MotionEvent e) {
            return true;
        }

        private int getPostion(MotionEvent e1) {
            return mHomeList.pointToPosition((int) e1.getX(), (int) e1.getY());
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            Log.i("Swipe", "Fling called");
            float distanceX = e2.getX() - e1.getX();
            float distanceY = e2.getY() - e1.getY();
            if (Math.abs(distanceX) > Math.abs(distanceY) && Math.abs(distanceX) > SWIPE_THRESHOLD && Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
                if (distanceX > 0)
                    onSwipeRight(getPostion(e1));
                else
                    onSwipeLeft(getPostion(e1));
                return true;
            }
            return false;
        }

//        private boolean showDeleteButton(MotionEvent e1) {
//            int pos = mHomeList.pointToPosition((int) e1.getX(), (int) e1.getY());
//            return showDeleteButton(pos);
//        }



    }

}
