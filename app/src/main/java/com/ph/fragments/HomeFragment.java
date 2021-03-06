package com.ph.fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ph.Activities.MainActivity;
import com.ph.R;
import com.ph.Utils.DateOperations;
import com.ph.Utils.StepsCountClick;
import com.ph.model.DBOperations;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link HomeFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {
    public static final int HOME_FRAGMENT_POSITION = 0;
    public static final int NEWGOAL_FRAGMENT_POSITION = 1;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    @Bind(R.id.fragment_new_goal_viewpager) ViewPager mViewPager;

    @Bind(R.id.fragment_home_tv_steps_count)
    TextView mStepsCount;


    @Bind(R.id.steps_count_layout)
    RelativeLayout mUserStepsLayout;
    int operatingWeek;

    String currentWeekToolBarText,nextWeekToolBarText;


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    private DBOperations mDbOperations;
    @Bind(R.id.fragment_home_tv_steps_count_text)
     TextView stepsCountText;
    @Bind(R.id.fragment_home_tv_average_steps_count)
     TextView averageStepsCount;
    @Bind(R.id.fragment_home_tv_average_steps_count_text)
     TextView averageStepsCountText;

//    public HomeFragment() {
//        // Required empty public constructor
//    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        mDbOperations = new DBOperations(getContext());
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        ButterKnife.bind(this, view);
        changeToolBarTitle(currentWeekToolBarText);


        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                Log.i("selected page\t", String.valueOf(position));

                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());

                SharedPreferences.Editor editor = sharedPreferences.edit();

                switch (position) {
                    case HOME_FRAGMENT_POSITION:
                        editor.putInt("operating_week", operatingWeek);
                        editor.commit();
                        Toast.makeText(getActivity(), "Current week selected", Toast.LENGTH_SHORT).show();
                        changeToolBarTitle(currentWeekToolBarText);
                        mStepsCount.setText(String.valueOf(mDbOperations.getStepsCountForToday()));
                        break;
                    case NEWGOAL_FRAGMENT_POSITION:
                        editor.putInt("operating_week", operatingWeek + 1);
                        editor.commit();
                        Toast.makeText(getContext(), "Next Goal selected", Toast.LENGTH_SHORT).show();
                        changeToolBarTitle(nextWeekToolBarText);
                        mStepsCount.setText(String.valueOf(mDbOperations.getStepsCountForToday()));
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                Log.i("Page scroller", "page swiped");
            }
        });

        mStepsCount.setText(String.valueOf(mDbOperations.getStepsCountForToday()));

        Typeface custom_font = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Eurostile.ttf");
        mStepsCount.setTypeface(custom_font);
        stepsCountText.setTypeface(custom_font);
        averageStepsCount.setTypeface(custom_font);
        averageStepsCountText.setTypeface(custom_font);

        mUserStepsLayout.setOnClickListener(new StepsCountClick(getActivity(), new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                mStepsCount.setText(String.valueOf(mDbOperations.getStepsCountForToday()));
            }
        }));

        return view;
    }

    private void setToolBarTitle() {
        DateOperations dateOperations = new DateOperations(getContext());
        operatingWeek = dateOperations.getWeeksTillDate(new Date());

        Date startDate = dateOperations.getDatesFromWeekNumber(operatingWeek).startDate;

        SimpleDateFormat customDateFormat = new SimpleDateFormat("MM/dd");

        currentWeekToolBarText = "Week "+(operatingWeek+1) + " " + customDateFormat.format(startDate);

        startDate = dateOperations.getDatesFromWeekNumber(operatingWeek+1).startDate;

        nextWeekToolBarText = "Week "+(operatingWeek+2) + " " + customDateFormat.format(startDate);
    }

    private void changeToolBarTitle(String text)
    {
        setToolBarTitle();

        ((MainActivity) getActivity()).setDrawerState(true);
        ((MainActivity) getActivity()).updateToolbar(text, R.color.white, R.color.black);
    }


    @Override
    public void onResume() {
        super.onResume();
        setupViewPages(mViewPager);
        int i = mViewPager.getCurrentItem();
        Log.i("from on resume", "on resume");

        if(i == HOME_FRAGMENT_POSITION)
            changeToolBarTitle(currentWeekToolBarText);
        else if(i == NEWGOAL_FRAGMENT_POSITION)
            changeToolBarTitle(nextWeekToolBarText);
        else
            changeToolBarTitle("GOAL");
        mStepsCount.setText(String.valueOf(mDbOperations.getStepsCountForToday()));


    }

    @Override
    public void onViewStateRestored(Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
    }

    @Override
    public void onInflate(Context context, AttributeSet attrs, Bundle savedInstanceState) {
        super.onInflate(context, attrs, savedInstanceState);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
       /*  if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }*/
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    public void setupViewPages(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getChildFragmentManager());
        adapter.addFragment(NewGoalFragment.newInstance(operatingWeek,0), "Current Goal");
        adapter.addFragment(NewGoalFragment.newInstance(operatingWeek+1,1), "Next Goal");
        //Proposed....
        /*DateOperations dateOperations = new DateOperations(getContext());
        adapter.addFragment(NewGoalFragment.newInstance(dateOperations.getWeeksTillDate(new Date())), "Current Goal");
        adapter.addFragment(NewGoalFragment.newInstance((dateOperations.getWeeksTillDate(new Date())+1)), "Next Goal");*/
        viewPager.setAdapter(adapter);
    }


    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
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
