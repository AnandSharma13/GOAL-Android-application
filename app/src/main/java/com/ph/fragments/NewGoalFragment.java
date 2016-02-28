package com.ph.Fragments;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ph.Activities.NewGoal;
import com.ph.MainActivity;
import com.ph.R;
import com.ph.Utils.DateOperations;
import com.ph.Utils.StepsCountClick;
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

    private TextView mStepsCount;
    private CustomProgressBar mNutritionProgressBar;
    private CustomProgressBar mActivityProgressBar;
    private DBOperations mDbOperations;
    private RelativeLayout mUserStepsLayout;
    private int weekNumber= -1;
    private int mFragmentPosition;
    private OnFragmentInteractionListener mListener;

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
        View view =  inflater.inflate(R.layout.fragment_new_goal, container, false);
        mActivityProgressBar = (CustomProgressBar) view.findViewById(R.id.fragment_next_goal_progress_bar_activity);
        mNutritionProgressBar = (CustomProgressBar) view.findViewById(R.id.fragment_home_progress_bar_nutrition);
        mStepsCount = (TextView) view.findViewById(R.id.fragment_home_tv_steps_count);
        mUserStepsLayout = (RelativeLayout) view.findViewById(R.id.steps_count_layout);
        mNewGoalButton = (Button) view.findViewById(R.id.btnNewGoal);



        //sets up click listeners..
        setBtnClickListeners();

    //    UserGoal userGoalNutrition = mDbOperations.getCurrentGoalInfo("Nutrition");
//        UserGoal userGoalActivity = mDbOperations.getCurrentGoalInfo("Activity");

        if(weekNumber == -1)
            weekNumber = new DateOperations(getContext()).getWeeksTillDate(new Date());
        UserGoal userGoalNutrition = mDbOperations.getuserGoalFromDB("Nutrition",weekNumber);
        UserGoal userGoalActivity = mDbOperations.getuserGoalFromDB("Activity",weekNumber);


        if(userGoalActivity == null || userGoalNutrition == null) {
            mActivityProgressBar.setVisibility(View.INVISIBLE);
            mNutritionProgressBar.setVisibility(View.INVISIBLE);
            return view;
        }
        else
        {
            mActivityProgressBar.setVisibility(View.VISIBLE);
            mNutritionProgressBar.setVisibility(View.VISIBLE);
        }
        String activityText = "A "+" "+userGoalActivity.getWeekly_count()+" mins / week";
        String nutritionText = "N "+" "+userGoalNutrition.getWeekly_count()+" food / week";
        String goalText = activityText+"\n"+nutritionText;
        mNewGoalButton.setText(goalText);
        int nutritionProgress =0;
        int activityProgress =0;
        if(mFragmentPosition == 0) {
            nutritionProgress = mDbOperations.getWeekProgress("Nutrition");
            mNutritionProgressBar.setText(String.valueOf(nutritionProgress));
            activityProgress = mDbOperations.getWeekProgress("Activity");
            mActivityProgressBar.setText(String.valueOf(activityProgress));

        }

        mNutritionProgressBar.setAim_text("Aim " + String.valueOf(userGoalNutrition.getWeekly_count()));
        mNutritionProgressBar.setMax(userGoalNutrition.getWeekly_count());
        ObjectAnimator animation2 = ObjectAnimator.ofInt(mNutritionProgressBar, "progress", nutritionProgress);
        animation2.setDuration(5000); //in milliseconds
        animation2.setInterpolator(new DecelerateInterpolator());
        animation2.start();



        mActivityProgressBar.setAim_text("Aim " + String.valueOf(userGoalActivity.getWeekly_count()));
        mActivityProgressBar.setMax( userGoalActivity.getWeekly_count());

        ObjectAnimator animation1 = ObjectAnimator.ofInt(mActivityProgressBar, "progress",activityProgress);
        animation1.setDuration(5000);
        animation1.setInterpolator(new DecelerateInterpolator());
        animation1.start();
        return view;
    }


    public void setBtnClickListeners(){
        mActivityProgressBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) getActivity()).setFragment(new ActivityEntryMainFragment(), true);

            }
        });

        mNutritionProgressBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) getActivity()).setFragment(new NutritionEntrySelectFragment(), true);

            }
        });
        mNewGoalButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent newGoalIntent = new Intent(getActivity(), NewGoal.class);
                startActivity(newGoalIntent);
            }
        });


        mStepsCount.setText(String.valueOf(mDbOperations.getStepsCountForToday()));


        /*mUserStepsLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                LayoutInflater li = LayoutInflater.from(getActivity());
                View dialogView = li.inflate(R.layout.user_steps_input, null);
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

                builder.setView(dialogView);

                final EditText userStepsInput = (EditText) dialogView.findViewById(R.id.user_steps_input);

                builder
                        .setCancelable(false)
                        .setPositiveButton("Save",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,int id) {

                                        int steps = Integer.parseInt(userStepsInput.getText().toString());

                                        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
                                        int user_id = Integer.parseInt(sharedPreferences.getString("user_id", "-1"));

                                        UserSteps userSteps = new UserSteps();

                                        userSteps.setSteps_count(steps);
                                        userSteps.setUser_id(user_id);

                                        mDbOperations.insertRow(userSteps);

                                        Bundle settingsBundle = new Bundle();
                                        settingsBundle.putString("Type", "ClientSync");

                                        settingsBundle.putInt("ListSize", 1);

                                        settingsBundle.putString("Table " + 0, UserSteps.tableName);

                                        SyncUtils.TriggerRefresh(settingsBundle);

                                        mStepsCount.setText(String.valueOf(mDbOperations.getStepsCountForToday()));

                                        Toast.makeText(getActivity(), "Successfully saved the steps count", Toast.LENGTH_SHORT).show();
                                    }
                                })
                        .setNegativeButton("Cancel",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                });
                // create alert dialog
                AlertDialog alertDialog = builder.create();

                // show it
                alertDialog.show();

                final Button positiveButton = alertDialog.getButton(DialogInterface.BUTTON_POSITIVE);

                positiveButton.setEnabled(false);

                userStepsInput.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        String val = s.toString();

                        if (val.equals(""))
                            positiveButton.setEnabled(false);
                        else
                            positiveButton.setEnabled(true);

                    }
                });



            }
        });
*/
        mUserStepsLayout.setOnClickListener(new StepsCountClick(getActivity(), mStepsCount));
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

    @Override
    public void onClick(View v) {

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
