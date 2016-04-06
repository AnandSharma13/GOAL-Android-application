package com.ph.view;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by Anup on 4/5/2016   .
 */
public class CustomRecyclerView extends RecyclerView {


    public CustomRecyclerView(Context context) {
        super(context);
    }

    public CustomRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomRecyclerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent e) {
        super.onInterceptTouchEvent(e);
        return true;
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {

        super.onTouchEvent(e);
        return false;
    }
}
