package com.ph.Adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import com.ph.fragments.ActivityHistoryDetails;
import com.ph.fragments.GoalHistoryDetails;
import com.ph.fragments.NutritionHistoryDetails;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Anup on 2/13/2016 .
 */
public class ViewPagerAdapter extends FragmentPagerAdapter {

    private final List<Fragment> mFragmentList = new ArrayList<>();
    private final List<String> mFragmentTitleList = new ArrayList<>();
    private boolean mIsLocked=false;
    private int lockedFragment = 0;

    public ViewPagerAdapter(FragmentManager manager) {
        super(manager);

    }

    @Override
    public Fragment getItem(int position) {
        return mFragmentList.get(position);
    }

    @Override
    public int getCount() {
        return mIsLocked?1:mFragmentList.size();
    }

    public void addFragment(Fragment fragment, String title) {
        mFragmentList.add(fragment);
        mFragmentTitleList.add(title);
    }

    public void setLocked(boolean isLocked,int position) {
        mIsLocked = isLocked;
        lockedFragment = position;
        notifyDataSetChanged();
    }


    @Override
    public int getItemPosition(Object object) {
        if(mIsLocked)
        {
            if(object instanceof NutritionHistoryDetails)
            {
                NutritionHistoryDetails details = (NutritionHistoryDetails) object;
                return details.weekNumber;
            }
            else if(object instanceof ActivityHistoryDetails)
            {
                ActivityHistoryDetails details = (ActivityHistoryDetails) object;
                return details.weekNumber;
            }
            else if(object instanceof GoalHistoryDetails)
            {
                GoalHistoryDetails details = (GoalHistoryDetails) object;
                return details.weekNumber;
            }
        }
        return super.getItemPosition(object);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mFragmentTitleList.get(position);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        super.destroyItem(container, position, object);
    }
}
