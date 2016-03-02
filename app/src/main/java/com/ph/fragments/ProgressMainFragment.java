package com.ph.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ph.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;


public class ProgressMainFragment extends Fragment implements ProgressActivityFragment.OnFragmentInteractionListener, ProgressNutritionFragment.OnFragmentInteractionListener, ProgressStepsFragment.OnFragmentInteractionListener, ProgressActivityDetails.OnFragmentInteractionListener, ProgressNutritionDetails.OnFragmentInteractionListener , StepsDay.OnFragmentInteractionListener, StepsWeek.OnFragmentInteractionListener{

    @Bind(R.id.fragment_progress_view_pager) ViewPager pager;
    @Bind(R.id.fragment_progress_main_tab_layout_tabs)TabLayout mTabLayout;

    private OnFragmentInteractionListener mListener;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_progress_main, container, false);
        ButterKnife.bind(this, view);
        setupViewPager(pager);
        mTabLayout.setupWithViewPager(pager);
        return view;
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getChildFragmentManager());
        adapter.addFragment(new ProgressActivityFragment(), "Activity");
        adapter.addFragment(new ProgressNutritionFragment(), "Nutrition");
        adapter.addFragment(new ProgressStepsFragment(), "User Steps");
        viewPager.setAdapter(adapter);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }
    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
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
