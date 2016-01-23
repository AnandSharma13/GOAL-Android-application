package com.ph.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
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
    private Toolbar toolbar;
    private SeekBar rpeSeekBar;
    private TextView rpeIndicator;
    private SeekBar timeSeekBar;
    private TextView timeIndicator;
    private Button saveButton;
    private String date;
    private CheckBox countGoal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entry_create);

        rpeSeekBar = (SeekBar) findViewById(R.id.activity_entry_rpe_seek);
        rpeIndicator = (TextView) findViewById(R.id.rpe_indicator);

        timeSeekBar = (SeekBar) findViewById(R.id.activity_entry_time_seek);
        timeIndicator = (TextView) findViewById(R.id.time_indicator);
        countGoal = (CheckBox) findViewById(R.id.activity_entry_count_goal);


        //Set Onchange listeners...

        rpeSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                rpeIndicator.setText(String.valueOf(progress));
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



        toolbar = (Toolbar) findViewById(R.id.app_bar);
        toolbar.setTitle(activity_type);



        mData = dbOperations.getActivities(activity_type);
        activityViewAdapter = new ActivityViewAdapter(this, mData);
        adapter = activityViewAdapter;





        mRecyclerView.setAdapter(adapter);



        saveButton = (Button) findViewById(R.id.activity_entry_save);

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
                activityEntry.setNotes("Coming from activity Entry page");//TODO: Logic for Notes
                activityEntry.setImage(""); //TODO: Discuss about the image
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
