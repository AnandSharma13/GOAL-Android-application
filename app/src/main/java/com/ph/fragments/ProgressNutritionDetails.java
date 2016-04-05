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
import com.ph.view.CustomProgressBar;

import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ProgressNutritionDetails.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ProgressNutritionDetails#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProgressNutritionDetails extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "week";
    //private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private int weekNumber;
    private TextView nutritionDetails;
    private UserGoal goalInfo;
    private DateOperations dateOperations;
    private DBOperations dbOperations;
    private UserGoal userGoalNutrition;
    private CustomProgressBar mCustomProgressBar;


    private OnFragmentInteractionListener mListener;

    public ProgressNutritionDetails() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param week Parameter 1.
     *
     * @return A new instance of fragment ProgressNutritionDetails.
     */
    // TODO: Rename and change types and number of parameters
    public static ProgressNutritionDetails newInstance(int week) {
        ProgressNutritionDetails fragment = new ProgressNutritionDetails();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM1, week);
       // args.putString(ARG_PARAM2, param2);
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
        View v = inflater.inflate(R.layout.fragment_progress_nutrition_details, container, false);

        if(weekNumber == -1)
            weekNumber = dateOperations.getWeeksTillDate(new Date());
        goalInfo = dbOperations.getuserGoalFromDB("Nutrition",weekNumber);
        nutritionDetails = (TextView) v.findViewById(R.id.progress_nutrition_info);
        mCustomProgressBar = (CustomProgressBar) v.findViewById(R.id.progress_bar_nutrition_details);
        userGoalNutrition = dbOperations.getuserGoalFromDB("Nutrition",weekNumber);
        if(goalInfo != null) {
            nutritionDetails.setText(goalInfo.getWeekly_count() + " " + goalInfo.getType());
        }else {
            nutritionDetails.setText("No goal was associated for the week");
            return v;
        }
        int weekProgress = dbOperations.getWeekProgress("Nutrition");
        mCustomProgressBar.setAim_text("Aim "+ goalInfo.getWeekly_count());
        mCustomProgressBar.setText(String.valueOf(weekProgress));
        mCustomProgressBar.setMax(goalInfo.getWeekly_count());
        mCustomProgressBar.setProgress(weekProgress);
        return v;
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
