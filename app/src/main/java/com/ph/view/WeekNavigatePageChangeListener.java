package com.ph.view;

import android.support.v4.view.ViewPager;
import android.util.Log;
import android.widget.TextView;

/**
 * Created by Anup on 2/20/2016 .
 */
public class WeekNavigatePageChangeListener implements ViewPager.OnPageChangeListener {
    private NonSwipeableViewPager viewPager;
    private TextView weekTitle;

    public WeekNavigatePageChangeListener(TextView weekTitle, NonSwipeableViewPager viewPager) {
        this.viewPager = viewPager;
        this.weekTitle = weekTitle;
    }
    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {


        weekTitle.setText(viewPager.getAdapter().getPageTitle(viewPager.getCurrentItem()));
    }

    @Override
    public void onPageSelected(int position) {
        Log.i("onPageSelected", "Page selected event");
    }

    @Override
    public void onPageScrollStateChanged(int state) {


    }
}
