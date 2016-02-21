package com.ph.view;

import android.support.v4.view.PagerAdapter;
import android.view.View;

import com.ph.R;

/**
 * Created by Anup on 2/20/2016 .
 */
public class WeekNavigateButtonClickListener implements View.OnClickListener {
    private NonSwipeableViewPager viewPager;


    public WeekNavigateButtonClickListener(NonSwipeableViewPager viewPager) {
        this.viewPager = viewPager;

    }
    @Override
    public void onClick(View v) {
        int current;
        switch (v.getId())
        {
            case R.id.week_prev:
                current = viewPager.getCurrentItem();

                if(current>0) {
                    int prev;
                    prev = current - 1;
                    viewPager.setCurrentItem(prev);
                }

                break;
            case R.id.week_next:
                current = viewPager.getCurrentItem();
                PagerAdapter adapter = viewPager.getAdapter();
                if(current<(adapter.getCount()-1)) {
                    int next;
                    next = current + 1;
                    viewPager.setCurrentItem(next);
                }
                break;
        }
    }
}
