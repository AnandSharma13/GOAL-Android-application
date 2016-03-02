package com.ph.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.ph.Adapters.ViewPagerAdapter;
import com.ph.R;
import com.ph.Utils.DateOperations;
import com.ph.view.NonSwipeableViewPager;
import com.ph.view.WeekNavigateButtonClickListener;
import com.ph.view.WeekNavigatePageChangeListener;

import java.util.Date;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link GoalHistoryFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link GoalHistoryFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GoalHistoryFragment extends Fragment {


    @Bind(R.id.viewpager_history_goal)
    NonSwipeableViewPager viewPager;
    private DateOperations dateOperations;
    @Bind(R.id.week_number_display)
    TextView weekTitle;
    @Bind(R.id.week_next)
    Button prev;
    @Bind(R.id.week_prev) Button next;
    private OnFragmentInteractionListener mListener;

    public GoalHistoryFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment GoalHistoryFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static GoalHistoryFragment newInstance(String param1, String param2) {
        GoalHistoryFragment fragment = new GoalHistoryFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_goal_history, container, false);

        ButterKnife.bind(this, v);

        prev.setOnClickListener(new WeekNavigateButtonClickListener(viewPager));
        next.setOnClickListener(new WeekNavigateButtonClickListener(viewPager));

        setupViewPager(viewPager);

        viewPager.addOnPageChangeListener(new WeekNavigatePageChangeListener(weekTitle, viewPager));

        return v;
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getChildFragmentManager()
        );

        dateOperations = new DateOperations(getContext());

        int week = dateOperations.getWeeksTillDate(new Date());

        for(int i=0;i<=week;i++)
        {
            adapter.addFragment(GoalHistoryDetails.newInstance(i), "Week " + (i + 1));
        }
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(week);

    }

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
}
