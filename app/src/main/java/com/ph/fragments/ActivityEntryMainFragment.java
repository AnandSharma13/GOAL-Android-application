package com.ph.fragments;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;


import com.ph.Activities.ActivityEntryCreate;
import com.ph.R;
import com.ph.Utils.DateOperations;
import com.ph.Utils.Dateutils;

import java.util.Calendar;
import java.util.Date;

import butterknife.Bind;
import butterknife.ButterKnife;


public class ActivityEntryMainFragment extends Fragment {
    private Calendar calendar;
    private DatePickerDialog.OnDateSetListener datePicker;
    @Bind(R.id.activity_entry_date_text_view)
    EditText activityDate;
    private DateOperations dateOperations;
    private Toolbar toolbar;
    @Bind(R.id.fragement_activity_entry_main_linear_layout_strength)
    LinearLayout mLinearLayoutStrength;

    @Bind(R.id.fragement_activity_entry_main_linear_layout_cardio)
    LinearLayout mLinearLayoutCardio;

    @Bind(R.id.fragement_activity_entry_main_linear_layout_lifestyle)
    LinearLayout mLinearLayoutLifstyle;





    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //   setContentView(R.layout.fragment_activity_entry_main);

        calendar = Calendar.getInstance();


        dateOperations = new DateOperations(getContext());


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_activity_entry_main, container, false);
        ButterKnife.bind(this, view);
        activityDate.setText(dateOperations.getUniformDateFormat().format(new Date()));
        datePicker = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, monthOfYear);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }
        };

        activityDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), datePicker, calendar
                        .get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH));

                Dateutils dateutils = new Dateutils(getContext());
                datePickerDialog = dateutils.setGoalPeriodWeek(datePickerDialog);
                datePickerDialog.show();
            }
        });

        mLinearLayoutCardio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activityClick(v);
            }
        });

        mLinearLayoutStrength.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activityClick(v);
            }
        });
        mLinearLayoutLifstyle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activityClick(v);
            }
        });
        return view;
    }

    private void updateLabel() {
        activityDate.setText(dateOperations.getUniformDateFormat().format(calendar.getTime()));
        Log.i("updateLabel", "Time has been set to the Edit Text");
    }

    public void activityClick(View view) {

        Intent intent = new Intent(getContext(), ActivityEntryCreate.class);
        intent.putExtra("date", dateOperations.getMysqlDateFormat().format(calendar.getTime()));
        switch (view.getId()) {
            case R.id.fragement_activity_entry_main_linear_layout_cardio:
                intent.putExtra("key", "Cardio");
                startActivity(intent);
                break;
            case R.id.fragement_activity_entry_main_linear_layout_strength:
                intent.putExtra("key", "Strength");
                startActivity(intent);
                break;
            case R.id.fragement_activity_entry_main_linear_layout_lifestyle:
                intent.putExtra("key", "Lifestyle");
                startActivity(intent);
                break;
        }
    }
}
