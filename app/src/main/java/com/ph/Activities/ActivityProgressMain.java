package com.ph.Activities;

import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.ph.R;
import com.ph.fragments.ProgressActivityDetails;
import com.ph.fragments.ProgressActivityFragment;
import com.ph.fragments.ProgressNutritionFragment;
import com.ph.fragments.ProgressStepsFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Anup on 2/4/2016 .
 */
public class ActivityProgressMain extends AppCompatActivity implements ProgressActivityFragment.OnFragmentInteractionListener, ProgressNutritionFragment.OnFragmentInteractionListener, ProgressStepsFragment.OnFragmentInteractionListener, ProgressActivityDetails.OnFragmentInteractionListener {

    ViewPager pager;
    TabLayout tabLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_progress_main);

        pager = (ViewPager)findViewById(R.id.progress_view_pager);
        tabLayout = (TabLayout) findViewById(R.id.progress_tabs);

        setupViewPager(pager);

        
        tabLayout.setupWithViewPager(pager);



    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new ProgressActivityFragment(), "Activity");
        adapter.addFragment(new ProgressNutritionFragment(),"Nutrition");
        adapter.addFragment(new ProgressStepsFragment(),"User Steps");

        viewPager.setAdapter(adapter);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {
        Toast.makeText(this,"Success!",Toast.LENGTH_LONG).show();
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


        public Fragment setItem(int position)
        {
            if(position<mFragmentList.size())
            return mFragmentList.get(position);
            else
                return mFragmentList.get(0);
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
