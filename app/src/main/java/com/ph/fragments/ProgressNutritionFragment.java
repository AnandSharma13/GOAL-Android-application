package com.ph.Fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.ph.R;
import com.ph.Utils.DateOperations;
import com.ph.view.NonSwipeableViewPager;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ProgressNutritionFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ProgressNutritionFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProgressNutritionFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private NonSwipeableViewPager viewPager;
    private DateOperations dateOperations;
    private TextView weekTitle;
    private Button prev;
    private Button next;


    private OnFragmentInteractionListener mListener;

    public ProgressNutritionFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProgressNutritionFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProgressNutritionFragment newInstance(String param1, String param2) {
        ProgressNutritionFragment fragment = new ProgressNutritionFragment();
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_progress_nutrition,container,false);
        viewPager = (NonSwipeableViewPager) v.findViewById(R.id.viewpager_progress_nutrition);

        weekTitle = (TextView) v.findViewById(R.id.week_number_display_nutrition);
        prev = (Button) v.findViewById(R.id.progress_nutrition_details_prev);
        next = (Button) v.findViewById(R.id.progress_nutrition_details_next);

        prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int current = viewPager.getCurrentItem();

                if(current>0) {
                    int prev;
                    prev = current - 1;
                    viewPager.setCurrentItem(prev);
                }

            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int current = viewPager.getCurrentItem();
                PagerAdapter adapter = viewPager.getAdapter();
                if(current<(adapter.getCount()-1)) {
                    int next;
                    next = current + 1;
                    viewPager.setCurrentItem(next);
                }
            }
        });

        setupViewPager(viewPager);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
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
        });
        weekTitle.setText(viewPager.getAdapter().getPageTitle(viewPager.getCurrentItem()));

        return v;
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getChildFragmentManager()
        );

        dateOperations = new DateOperations(getContext());

        int week = dateOperations.getWeeksTillDate(new Date());

        for(int i=0;i<=week;i++)
        {
            adapter.addFragment(ProgressNutritionDetails.newInstance(i), "Week " + (i + 1));
        }
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(week);

    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
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
    public void onDetach() {
        super.onDetach();
        mListener = null;
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

    class ViewPagerAdapter extends FragmentStatePagerAdapter {
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
        public int getItemPosition (Object object) { return POSITION_NONE; }

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
