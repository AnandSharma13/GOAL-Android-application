package com.ph.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import com.ph.Adapters.ActivityViewAdapter;
import com.ph.Activities.MainActivity;
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


public class ActivityEntryCreateFragment extends Fragment implements View.OnClickListener {

    public static String activity_type = "";
    private static String date = "";
    public List<Activity> mData;
    @Bind(R.id.activity_recycler_view)
    RecyclerView mRecyclerView;
    @Bind(R.id.activity_entry_create_number_picker_rpe)
    NumberPicker numberPickerRPE;
    @Bind(R.id.activity_entry_create_number_picker_time)
    NumberPicker numberPickerTime;

    @Bind(R.id.btn_rpe_add)
    TextView rpeAddButton;
    @Bind(R.id.btn_rpe_minus)
    TextView rpeMinusButton;

    @Bind(R.id.btn_time_add)
    TextView timeAddButton;
    @Bind(R.id.btn_time_minus)
    TextView timeMinusButton;

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
    private int rpeCount = 0;
    private int timeCount = 0;

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

        ((MainActivity) getActivity()).updateToolbar(activity_type, R.color.activity_entry_app_bar, R.color.white);
        ((MainActivity) getActivity()).setDrawerState(false);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.activity_entry_create_fragment, container, false);
        ButterKnife.bind(this, view);

        rpeCount = numberPickerRPE.getValue();
        timeCount = numberPickerTime.getMaxValue();
        commentButton.setOnClickListener(this);
        rpeAddButton.setOnClickListener(this);
        rpeMinusButton.setOnClickListener(this);
        timeMinusButton.setOnClickListener(this);
        timeAddButton.setOnClickListener(this);
        saveButton.setOnClickListener(this);
        numberPickerRPE.setWrapSelectorWheel(false);
        numberPickerTime.setWrapSelectorWheel(false);

        numberPickerRPE.setMinValue(6);
        numberPickerRPE.setMaxValue(20);
        numberPickerTime.setMinValue(0);
        numberPickerTime.setMaxValue(120);
        numberPickerRPE.setValue(numberPickerRPE.getValue());

        if (numberPickerRPE.getValue() == 6 || numberPickerTime.getValue() == 0)
            saveButton.setEnabled(false);
        //Set Onchange listeners...

        numberPickerRPE.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                if (newVal > oldVal)
                    saveButton.setEnabled(true);
            }
        });

        numberPickerTime.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                if (newVal > oldVal)
                    saveButton.setEnabled(true);
            }
        });

        mRecyclerView.setHasFixedSize(true);
        dbOperations = new DBOperations(getContext());
        layoutManager = new GridLayoutManager(getContext(), 3);
        mRecyclerView.setLayoutManager(layoutManager);
        mData = dbOperations.getActivities(activity_type);
        activityViewAdapter = new ActivityViewAdapter(getContext(), mData);
        adapter = activityViewAdapter;
        mRecyclerView.setAdapter(adapter);
        return view;
    }

    @Override
    public void onClick(View v) {
        int newVal=0;
        switch (v.getId()) {
            case R.id.activity_entry_comment:
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
                break;
            case R.id.btn_rpe_add:
                numberPickerRPE.setValue(numberPickerRPE.getValue() + 1);
                newVal = numberPickerRPE.getValue();
                if ((newVal - rpeCount) > 0)
                    saveButton.setEnabled(true);
                break;
            case R.id.btn_rpe_minus:
                numberPickerRPE.setValue(numberPickerRPE.getValue() - 1);
                newVal = numberPickerRPE.getValue();
                if ((newVal - rpeCount) > 0)
                    saveButton.setEnabled(true);
                break;
            case R.id.btn_time_minus:
                numberPickerTime.setValue(numberPickerTime.getValue() - 1);
                newVal = numberPickerTime.getValue();
                if ((newVal - timeCount) > 0)
                    saveButton.setEnabled(true);
                break;
            case R.id.btn_time_add:
                numberPickerTime.setValue(numberPickerTime.getValue() + 1);
                newVal = numberPickerTime.getValue();
                if ((newVal - timeCount) > 0)
                    saveButton.setEnabled(true);
                break;
            case R.id.activity_entry_save:
                ActivityEntry activityEntry = new ActivityEntry();
                int selectedActivity = activityViewAdapter.getSelectedPos();

                long activityId = mData.get(selectedActivity).getActivity_id();
                int rpeVal = numberPickerRPE.getValue();
                int timeVal = numberPickerTime.getValue();


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
                break;
        }
    }
}
