package com.ph.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.widget.ProgressBar;

/**
 * Created by Anand on 1/3/2016.
 */
public class CustomProgressBar extends ProgressBar{

    private String text="";
    @Override
    protected synchronized void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Paint textPaint = new Paint();
        int width = this.getMeasuredWidth()/2;
        int height = this.getMeasuredHeight()/2;
        textPaint.setTextAlign(Paint.Align.CENTER);
        canvas.drawText(text,width,height, textPaint);
    }

    public CustomProgressBar(Context context) {
        super(context);
    }

    public CustomProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public CustomProgressBar(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }
    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

}
