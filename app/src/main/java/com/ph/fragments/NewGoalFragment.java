package com.ph.fragments;

import android.animation.ObjectAnimator;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ph.Activities.NewGoal;
import com.ph.Activities.NutritionEntrySelect;
import com.ph.R;
import com.ph.model.DBOperations;
import com.ph.model.UserGoal;
import com.ph.model.UserSteps;
import com.ph.net.SyncUtils;
import com.ph.view.CustomProgressBar;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link NewGoalFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link NewGoalFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NewGoalFragment extends Fragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER



    private Button mNewGoalButton;

    private TextView mStepsCount;
    private CustomProgressBar mNutritionProgressBar;
    private CustomProgressBar mActivityProgressBar;
    private DBOperations mDbOperations;
    private RelativeLayout mUserStepsLayout;

    private OnFragmentInteractionListener mListener;

    public NewGoalFragment() {
        // Required empty public constructor

    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment NewGoalFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static NewGoalFragment newInstance(String param1, String param2) {
        NewGoalFragment fragment = new NewGoalFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
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


        UserGoal userGoalNutrition = mDbOperations.getCurrentGoalInfo("Nutrition");
        if(userGoalNutrition == null)
        {
            return view;
        }
        int  nutritionProgress= mDbOperations.getWeekProgress("Nutrition");
        mNutritionProgressBar.setText(String.valueOf(nutritionProgress));
        mNutritionProgressBar.setAim_text("Aim " + String.valueOf(userGoalNutrition.getWeekly_count()));
        ObjectAnimator animation2 = ObjectAnimator.ofInt(mNutritionProgressBar, "progress", nutritionProgress);
        animation2.setDuration(5000); //in milliseconds
        animation2.setInterpolator(new DecelerateInterpolator());
        animation2.start();


        int  activityProgress= mDbOperations.getWeekProgress("Activity");

        mActivityProgressBar.setText(String.valueOf(activityProgress));
        UserGoal userGoalActivity = mDbOperations.getCurrentGoalInfo("Activity");
        if(userGoalActivity == null) {
            mActivityProgressBar.setVisibility(View.GONE);
            return view;
        }
        mActivityProgressBar.setAim_text("Aim " + String.valueOf(userGoalActivity.getWeekly_count()));

        ObjectAnimator animation1 = ObjectAnimator.ofInt(mActivityProgressBar, "progress",activityProgress);
        animation1.setDuration(5000); //in milliseconds
        animation1.setInterpolator(new DecelerateInterpolator());
        animation1.start();


        return view;
    }


    public void setBtnClickListeners(){
        mActivityProgressBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right,android.R.anim.fade_in, android.R.anim.fade_out).add(R.id.activity_main_frame_layout,new ActivityEntryMainFragment()).addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

        mNutritionProgressBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),NutritionEntrySelect.class);
                startActivity(intent);
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


        mUserStepsLayout.setOnClickListener(new View.OnClickListener() {
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
