package com.ph.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ph.R;
import com.ph.Utils.DateOperations;
import com.ph.model.DBOperations;
import com.ph.model.UserGoal;

import java.util.Date;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link GoalHistoryDetails.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link GoalHistoryDetails#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GoalHistoryDetails extends Fragment {

    private static final String ARG_PARAM1 = "week";
    private int weekNumber=-1;
    @Bind(R.id.goal_history_activity_info)
    TextView activityGoalInfo;
    @Bind(R.id.goal_history_nutrition_info)
    TextView nutritionGoalInfo;


    private OnFragmentInteractionListener mListener;
    private DateOperations dateOperations;
    private DBOperations dbOperations;
    private UserGoal activityInfo;
    private UserGoal nutritionInfo;

    public GoalHistoryDetails() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     * @return A new instance of fragment GoalHistoryDetails.
     */
    public static GoalHistoryDetails newInstance(int week) {
        GoalHistoryDetails fragment = new GoalHistoryDetails();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM1, week);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            weekNumber = getArguments().getInt(ARG_PARAM1);
        }
        dateOperations = new DateOperations(getContext());
        dbOperations = new DBOperations(getContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_goal_history_details, container, false);

        ButterKnife.bind(this,v);

        if(weekNumber == -1)
            weekNumber = dateOperations.getWeeksTillDate(new Date());

        activityInfo = dbOperations.getuserGoalFromDB("Activity",weekNumber);
        nutritionInfo = dbOperations.getuserGoalFromDB("Nutrition",weekNumber);

        activityGoalInfo.setText(getFormattedGoalInfoText(activityInfo));
        nutritionGoalInfo.setText(getFormattedGoalInfoText(nutritionInfo));


        return v;
    }

    private String getFormattedGoalInfoText(UserGoal obj)
    {
        if(obj == null)
            return "";
        String text = obj.getText();
        if(text.equals(""))
            return  String.valueOf(obj.getWeekly_count());
        else
            return obj.getWeekly_count()+" - "+obj.getText();
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
}
