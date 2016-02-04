package com.ph.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.ProgressBar;

/**
 * Created by Anand on 1/3/2016.
 */
public class CustomProgressBar extends ProgressBar{

    private String text ="";



    public String getAim_text() {
        return aim_text;
    }

    public void setAim_text(String aim_text) {
        this.aim_text = aim_text;
    }

    private String aim_text= "Aim";
    @Override
    protected synchronized void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Paint textPaint = new Paint();
        int width = this.getMeasuredWidth()/2;
        int height = this.getMeasuredHeight()/2;
        textPaint.setTextAlign(Paint.Align.CENTER);
        textPaint.setTextSize(45);
        canvas.drawText(text, width, height, textPaint);

        Paint aimTextPaint = new Paint();
        int wh = this.getMeasuredWidth()/2;
        int ht = this.getMeasuredHeight()*3/4;
        aimTextPaint.setTextAlign(Paint.Align.CENTER);
        aimTextPaint.setTextSize(25);
        canvas.drawText(aim_text,wh,ht, aimTextPaint);

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
