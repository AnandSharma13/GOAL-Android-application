package com.ph.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.ph.Adapters.ActivityViewAdapter;
import com.ph.MainActivity;
import com.ph.R;
import com.ph.Utils.AlertDialogManager;
import com.ph.model.Activity;
import com.ph.model.ActivityEntry;
import com.ph.model.DBOperations;
import com.ph.model.UserGoal;
import com.ph.net.SyncUtils;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Anup on 1/16/2016.
 */
public class ActivityEntryCreateFragment extends Fragment {

    public static String activity_type = "";
    private static String date = "";
    public List<Activity> mData;
    @Bind(R.id.activity_recycler_view) RecyclerView mRecyclerView;
    @Bind(R.id.activity_entry_rpe_seek)
    SeekBar rpeSeekBar;
    @Bind(R.id.rpe_indicator)
    TextView rpeIndicator;
    @Bind(R.id.activity_entry_time_seek)
    SeekBar timeSeekBar;
    @Bind(R.id.time_indicator)
    TextView timeIndicator;
    @Bind(R.id.activity_entry_save)
    Button saveButton;
    @Bind(R.id.activity_entry_comment)
    ImageButton commentButton;
    @Bind(R.id.activity_entry_count_goal)
    CheckBox countGoal;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.Adapter adapter;
    private ActivityViewAdapter activityViewAdapter;
    private DBOperations dbOperations;
    private String userNotes = "";

    private Toolbar toolbar;

    public static ActivityEntryCreateFragment newInstance(String param1, String param2) {
        ActivityEntryCreateFragment fragment = new ActivityEntryCreateFragment();
        Bundle args = new Bundle();
        args.putString("ACTIVITY_TYPE", param1);
        args.putString("DATE", param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            activity_type = getArguments().getString("ACTIVITY_TYPE");
            date = getArguments().getString("DATE");
        }

        ((MainActivity)getActivity()).updateToolbar(activity_type,R.color.activity_entry_app_bar,R.color.white);
        ((MainActivity)getActivity()).setDrawerState(false);

        //((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(activity_type);
        //((AppCompatActivity)getActivity()).getSupportActionBar().setBackgroundDrawable(getContext().getDrawable(R.color.activity_entry_app_bar));


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.activity_entry_create_fragment,container,false);
        ButterKnife.bind(this,view);




        commentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater li = LayoutInflater.from(getContext());
                View dialogView = li.inflate(R.layout.activity_entry_comment, null);
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

                builder.setView(dialogView);

                final EditText notes = (EditText) dialogView.findViewById(R.id.activity_entry_notes);
                notes.setText(userNotes);

                builder
                        .setCancelable(false)
                        .setPositiveButton("OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        userNotes = notes.getText().toString();
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

            }
        });




        if (rpeSeekBar.getProgress() == 0 || timeSeekBar.getProgress() == 0)
            saveButton.setEnabled(false);
        //Set Onchange listeners...

        rpeSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                rpeIndicator.setText(String.valueOf(progress));
                if (progress > 0 && timeSeekBar.getProgress() > 0)
                    saveButton.setEnabled(true);
                else
                    saveButton.setEnabled(false);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        timeSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                timeIndicator.setText(String.valueOf(progress));
                if (progress > 0 && rpeSeekBar.getProgress() > 0)
                    saveButton.setEnabled(true);
                else
                    saveButton.setEnabled(false);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });



        mRecyclerView.setHasFixedSize(true);

        dbOperations = new DBOperations(getContext());

        layoutManager = new GridLayoutManager(getContext(), 3);

        mRecyclerView.setLayoutManager(layoutManager);

        // activity_type = getIntent().getExtras().getString("key");
        // date = getIntent().getExtras().getString("date");



        mData = dbOperations.getActivities(activity_type);
        activityViewAdapter = new ActivityViewAdapter(getContext(), mData);
        adapter = activityViewAdapter;


        mRecyclerView.setAdapter(adapter);


        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityEntry activityEntry = new ActivityEntry();
                int selectedActivity = activityViewAdapter.getSelectedPos();

                long activityId = mData.get(selectedActivity).getActivity_id();
                int rpeVal = rpeSeekBar.getProgress();
                int timeVal = timeSeekBar.getProgress();


                UserGoal userGoal = dbOperations.getCurrentGoalInfo("Activity");
                if (userGoal == null) {
                    AlertDialogManager alertDialogManager = new AlertDialogManager();
                    alertDialogManager.showAlertDialog(getContext(), "No Goal found!", "There was no goal created for this week. Please create a goal before you enter any activity.");
                    return;
                }
                int countasGoal = countGoal.isChecked() ? 1 : 0;
                activityEntry.setGoal_id(userGoal.getGoal_id());
                activityEntry.setActivity_id(activityId);
                activityEntry.setCount_towards_goal(countasGoal);
                activityEntry.setNotes(userNotes);
                activityEntry.setImage(""); //TODO: There shouldn't be an image in activity_entry
                activityEntry.setRpe(rpeVal);
                activityEntry.setActivity_length(String.valueOf(timeVal));
                activityEntry.setDate(date);


                dbOperations.insertRow(activityEntry);

                Bundle settingsBundle = new Bundle();
                settingsBundle.putString("Type", "ClientSync");

                settingsBundle.putInt("ListSize", 1);

                settingsBundle.putString("Table " + 0, ActivityEntry.tableName);

                SyncUtils.TriggerRefresh(settingsBundle);

                //Notify the user.
                Toast.makeText(getContext(), "Activity Entry succesfully recorded", Toast.LENGTH_SHORT).show();

                //Redirect to main activity.
                Intent i = new Intent(getContext(), MainActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                ActivityEntryCreateFragment.this.startActivity(i);


            }
        });


        return view;
    }
}
