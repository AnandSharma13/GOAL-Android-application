package com.ph.Activities;

import android.net.Uri;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.ph.R;
import com.ph.fragments.ActivityHistoryFragment;
import com.ph.fragments.HomeFragment;
import com.ph.fragments.NextGoalFragment;
import com.ph.fragments.NutritionHistoryFragment;
import com.ph.fragments.StepsHistoryFragment;

import java.util.ArrayList;
import java.util.List;

public class HistoryActivity extends AppCompatActivity implements NutritionHistoryFragment.OnFragmentInteractionListener, ActivityHistoryFragment.OnFragmentInteractionListener, StepsHistoryFragment.OnFragmentInteractionListener {

    private ViewPager mViewPager;
    private android.support.v7.widget.Toolbar mToolBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        mViewPager = (ViewPager) findViewById(R.id.activity_history_viewpager);
        mToolBar = (android.support.v7.widget.Toolbar) findViewById(R.id.app_bar);
        mToolBar.setTitle("History");

        TabLayout tabLayout = (TabLayout) findViewById(R.id.activity_history_tabs);
        tabLayout.addTab(tabLayout.newTab());
        tabLayout.addTab(tabLayout.newTab());
        tabLayout.addTab(tabLayout.newTab());
        setupViewPages(mViewPager);
        tabLayout.setupWithViewPager(mViewPager);
    }



    public void setupViewPages(ViewPager viewPager){
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new NutritionHistoryFragment(), "Nutrition");
        adapter.addFragment(new ActivityHistoryFragment(), "Activity");
        adapter.addFragment(new StepsHistoryFragment(), "Steps");
        viewPager.setAdapter(adapter);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    class ViewPagerAdapter extends FragmentPagerAdapter {

        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }

    }


}
