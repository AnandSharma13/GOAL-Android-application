package com.ph.view;


import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.RadialGradient;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.util.DisplayMetrics;

import com.ph.R;


/**
 * Created by Anand on 2/13/2016.
 */

public class PieProgressBar extends Drawable {

    Paint mPaint;
    RectF mBoundsF;
    RectF mInnerBoundsF;
    final float START_ANGLE = 0.f;

    public void setmDrawTo(float mDrawTo) {
        this.mDrawTo = mDrawTo;
    }

    int mProgressBarBackGround = 0;
    int mProgressBarOuterRing = 0;
    float mDrawTo;

    public PieProgressBar(int mProgressBarBackGround, int mProgressBarOuterRing) {
        super();
        this.mProgressBarBackGround = mProgressBarBackGround;
        this.mProgressBarOuterRing = mProgressBarOuterRing;
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    }


    public void setBorderWidth(float widthDp, DisplayMetrics dm) {
        float borderWidth = widthDp * dm.density;
        mPaint.setStrokeWidth(borderWidth);
    }

    /**
     * @param color you want the pie to be drawn in
     */
    public void setColor(int color) {
        mPaint.setColor(color);
    }

    @Override
    public void draw(Canvas canvas) {

        canvas.rotate(-90f, getBounds().centerX(), getBounds().centerY());
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(5.0f);
        mPaint.setColor(mProgressBarOuterRing);
        canvas.drawOval(mBoundsF, mPaint);

        //canvas.drawArc(mBoundsF, START_ANGLE, mDrawTo, true, mPaint);
        mPaint.setColor(mProgressBarBackGround);

        mPaint.setStyle(Paint.Style.FILL);
        canvas.drawArc(mInnerBoundsF, START_ANGLE, mDrawTo, true, mPaint);
        mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
//        mPaint.setShader(new RadialGradient(canvas.getWidth() / 2, canvas.getHeight() / 2,
//                canvas.getHeight() / 3, Color.TRANSPARENT, Color.BLACK, Shader.TileMode.MIRROR));


        String text = "0";
        String aim_text = "AIM TEXT";
        Paint textPaint = new Paint();


        int xCenter = (canvas.getWidth() / 2);
        int yCenter = (int) ((canvas.getHeight() / 2) - ((textPaint.descent() + textPaint.ascent()) / 2));

        canvas.save();
        textPaint.setTextAlign(Paint.Align.CENTER);
        textPaint.setTextSize(80);
        canvas.rotate(90, xCenter, yCenter);


        Rect rec = new Rect();
        mBoundsF.round(rec);

        drawTextCentred(canvas, textPaint, rec, text.toCharArray(), xCenter, yCenter);

        Paint aimTextPaint = new Paint();
        aimTextPaint.setColor(Color.BLUE);
        aimTextPaint.setTextAlign(Paint.Align.CENTER);
        aimTextPaint.setTextSize(25);
        canvas.drawText(aim_text, xCenter, yCenter * 9, aimTextPaint);
        canvas.restore();
    }

    public void drawTextCentred(Canvas canvas, Paint paint, Rect textBounds, char[] text, float cx, float cy) {
        paint.getTextBounds(text, 0, text.length, textBounds);
        canvas.drawText(String.valueOf(text), cx, cy - textBounds.exactCenterY(), paint);
    }

    @Override
    protected void onBoundsChange(Rect bounds) {
        super.onBoundsChange(bounds);
        mBoundsF = mInnerBoundsF = new RectF(bounds);
        final int halfBorder = (int) (mPaint.getStrokeWidth() / 2f + 0.5f);
        mInnerBoundsF.inset(halfBorder, halfBorder);
    }

    @Override
    public boolean onLevelChange(int level) {
        final float drawTo = START_ANGLE + ((float) 360 * level) / 100f;
        boolean update = drawTo != mDrawTo;
        mDrawTo = drawTo;
        return update;
    }

    @Override
    public void setAlpha(int alpha) {
        mPaint.setAlpha(alpha);
    }

    @Override
    public void setColorFilter(ColorFilter cf) {
        mPaint.setColorFilter(cf);
    }

    @Override
    public int getOpacity() {
        return mPaint.getAlpha();
    }
}

