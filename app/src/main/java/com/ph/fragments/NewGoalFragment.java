package com.ph.fragments;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.TextView;

import com.ph.Activities.NewGoal;
import com.ph.Activities.MainActivity;
import com.ph.R;
import com.ph.Utils.AlertDialogManager;
import com.ph.Utils.DateOperations;
import com.ph.model.DBOperations;
import com.ph.model.UserGoal;
import com.ph.view.CustomProgressBar;

import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link NewGoalFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link NewGoalFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NewGoalFragment extends Fragment implements View.OnClickListener {

    private Button mNewGoalButton;
    private CustomProgressBar mNutritionProgressBar;
    private CustomProgressBar mActivityProgressBar;
    private DBOperations mDbOperations;
    private TextView mNutritionTv;
    private TextView mActivityTv;
    private int weekNumber= -1;
    private int mFragmentPosition;
    private OnFragmentInteractionListener mListener;
    private SharedPreferences pref;
    private int programLength;
    private int nutritionProgress;
    private int activityProgress;

    public NewGoalFragment() {
        // Required empty public constructor

    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment NewGoalFragment.
     */
    // TODO: Send week number from Main Activity
    public static NewGoalFragment newInstance(int weekNumber, int fragmentPosition) {
        NewGoalFragment fragment = new NewGoalFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        fragment.weekNumber = weekNumber;
        fragment.mFragmentPosition = fragmentPosition;
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mDbOperations = new DBOperations(getContext());
        pref = PreferenceManager.getDefaultSharedPreferences(getContext());
        programLength = Integer.valueOf(pref.getString("program_length","-1"));

        View view =  inflater.inflate(R.layout.fragment_new_goal, container, false);
        mActivityProgressBar = (CustomProgressBar) view.findViewById(R.id.fragment_next_goal_progress_bar_activity);
        mNutritionProgressBar = (CustomProgressBar) view.findViewById(R.id.fragment_home_progress_bar_nutrition);
        mNutritionTv = (TextView) view.findViewById(R.id.fragment_new_goal_tv_nutrition);
        mActivityTv = (TextView) view.findViewById(R.id.fragment_new_goal_tv_activity);
        mNewGoalButton = (Button) view.findViewById(R.id.btnNewGoal);

        Typeface typeFace=Typeface.createFromAsset(getActivity().getAssets(),"fonts/Eurostile.ttf");
        mActivityTv.setTypeface(typeFace);
        mNutritionTv.setTypeface(typeFace);
        mNewGoalButton.setTypeface(typeFace);

        mActivityProgressBar.setOnClickListener(this);
        mNutritionProgressBar.setOnClickListener(this);
        mNewGoalButton.setOnClickListener(this);


        if(weekNumber == -1)
            weekNumber = new DateOperations(getContext()).getWeeksTillDate(new Date());
        UserGoal userGoalNutrition = mDbOperations.getuserGoalFromDB("Nutrition",weekNumber);
        UserGoal userGoalActivity = mDbOperations.getuserGoalFromDB("Activity",weekNumber);
        //get count for activity
        if(userGoalActivity == null || userGoalNutrition == null) {
            mActivityProgressBar.setVisibility(View.INVISIBLE);
            mNutritionProgressBar.setVisibility(View.INVISIBLE);


            if(mFragmentPosition == 0 && weekNumber<programLength) {
                new AlertDialogManager().showAlertDialog(getContext(), "Goal Not Set", "You have not set a goal for this week. You must create a goal to continue.", "Create Goal", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        Intent newGoalIntent = new Intent(getActivity(), NewGoal.class);
                        startActivity(newGoalIntent);

                    }
                });
            }
            if(weekNumber>=programLength)
            {
                String finishText = "Program complete.";
                mNewGoalButton.setText(finishText);
                mNewGoalButton.setEnabled(false);
            }
            return view;
        }
        else
        {
            mActivityProgressBar.setVisibility(View.VISIBLE);
            mNutritionProgressBar.setVisibility(View.VISIBLE);
        }

        int activityCount = userGoalActivity.getTimes() * userGoalActivity.getWeekly_count();
        if(weekNumber<programLength) {

            String activityText = "A " + " " + activityCount + " mins / week";
            String nutritionText = "N " + " " + userGoalNutrition.getWeekly_count() + " food / week";
            String goalText = activityText + "\n" + nutritionText;
            mNewGoalButton.setText(goalText);
        }
        else
        {
            String finishText = "Program complete.";
            mNewGoalButton.setText(finishText);
            mNewGoalButton.setEnabled(false);
        }

        nutritionProgress =0;
        activityProgress =0;
        if(mFragmentPosition == 0) {
            nutritionProgress = mDbOperations.getWeekProgress("Nutrition");
            mNutritionProgressBar.setText(String.valueOf(nutritionProgress));
            activityProgress = mDbOperations.getWeekProgress("Activity");
            mActivityProgressBar.setText(String.valueOf(activityProgress));

        }

        mNutritionProgressBar.setAim_text("Aim " + String.valueOf(userGoalNutrition.getWeekly_count()));
        mNutritionProgressBar.setMax(userGoalNutrition.getWeekly_count());
        mActivityProgressBar.setAim_text("Aim " + String.valueOf(activityCount));
        mActivityProgressBar.setMax(activityCount);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
            ObjectAnimator animation2 = ObjectAnimator.ofInt(mNutritionProgressBar, "progress", nutritionProgress);
            animation2.setDuration(5000); //in milliseconds
            animation2.setInterpolator(new DecelerateInterpolator());
            animation2.start();
            ObjectAnimator animation1 = ObjectAnimator.ofInt(mActivityProgressBar, "progress", activityProgress);
            animation1.setDuration(5000);
            animation1.setInterpolator(new DecelerateInterpolator());
            animation1.start();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.fragment_next_goal_progress_bar_activity:
                ((MainActivity) getActivity()).setFragment(new ActivityEntryMainFragment(), true);
                break;
            case R.id.fragment_home_progress_bar_nutrition:
                ((MainActivity) getActivity()).setFragment(new NutritionEntrySelectFragment(), true);
                break;
            case R.id.btnNewGoal:
                Intent newGoalIntent = new Intent(getActivity(), NewGoal.class);
                startActivity(newGoalIntent);
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onStart() {
        super.onStart();
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
