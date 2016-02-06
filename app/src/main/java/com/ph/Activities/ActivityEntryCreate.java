package com.ph.Activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
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

/**
 * Created by Anup on 1/16/2016.
 */
public class ActivityEntryCreate extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.Adapter adapter;
    private ActivityViewAdapter activityViewAdapter;
    private List<Activity> mData;
    private DBOperations dbOperations;
    private String activity_type;
    private SeekBar rpeSeekBar;
    private TextView rpeIndicator;
    private SeekBar timeSeekBar;
    private TextView timeIndicator;
    private Button saveButton;
    private Button commentButton;
    private String date;
    private CheckBox countGoal;
    private String userNotes = "";

    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entry_create);

        toolbar = (Toolbar) findViewById(R.id.app_bar);

        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        rpeSeekBar = (SeekBar) findViewById(R.id.activity_entry_rpe_seek);
        rpeIndicator = (TextView) findViewById(R.id.rpe_indicator);

        timeSeekBar = (SeekBar) findViewById(R.id.activity_entry_time_seek);
        timeIndicator = (TextView) findViewById(R.id.time_indicator);
        countGoal = (CheckBox) findViewById(R.id.activity_entry_count_goal);

        commentButton = (Button) findViewById(R.id.activity_entry_comment);


        commentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater li = LayoutInflater.from(ActivityEntryCreate.this);
                View dialogView = li.inflate(R.layout.activity_entry_comment, null);
                AlertDialog.Builder builder = new AlertDialog.Builder(ActivityEntryCreate.this);

                builder.setView(dialogView);

                final EditText notes = (EditText) dialogView.findViewById(R.id.activity_entry_notes);

                builder
                        .setCancelable(false)
                        .setPositiveButton("OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,int id) {
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


        saveButton = (Button) findViewById(R.id.activity_entry_save);

        if(rpeSeekBar.getProgress() ==0 || timeSeekBar.getProgress() ==0)
            saveButton.setEnabled(false);
        //Set Onchange listeners...

        rpeSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                rpeIndicator.setText(String.valueOf(progress));
                if(progress>0 && timeSeekBar.getProgress()>0)
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
                if(progress>0 && rpeSeekBar.getProgress()>0)
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

        mRecyclerView = (RecyclerView) findViewById(R.id.activity_recycler_view);

        mRecyclerView.setHasFixedSize(true);

        dbOperations = new DBOperations(this);

        layoutManager = new GridLayoutManager(this,3);

        mRecyclerView.setLayoutManager(layoutManager);

        activity_type = getIntent().getExtras().getString("key");
        date = getIntent().getExtras().getString("date");

        getSupportActionBar().setTitle(activity_type);








        mData = dbOperations.getActivities(activity_type);
        activityViewAdapter = new ActivityViewAdapter(this, mData);
        adapter = activityViewAdapter;



        mRecyclerView.setAdapter(adapter);




        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityEntry activityEntry = new ActivityEntry();
                int selectedActivity = activityViewAdapter.getSelectedPos();

                int activityId = mData.get(selectedActivity).getActivity_id();
                int rpeVal = rpeSeekBar.getProgress();
                int timeVal = timeSeekBar.getProgress();


                UserGoal userGoal = dbOperations.getCurrentGoalInfo("Activity");
                if(userGoal == null)
                {
                    AlertDialogManager alertDialogManager = new AlertDialogManager();
                    alertDialogManager.showAlertDialog(ActivityEntryCreate.this,"No Goal found!","There was no goal created for this week. Please create a goal before you enter any activity.");
                    return;
                }
                int countasGoal = countGoal.isChecked()?1:0;
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
                Toast.makeText(ActivityEntryCreate.this,"Activity Entry succesfully recorded",Toast.LENGTH_SHORT).show();

                //Redirect to main activity.
                Intent i = new Intent(ActivityEntryCreate.this, MainActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                ActivityEntryCreate.this.startActivity(i);


            }
        });



    }
}
