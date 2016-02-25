package com.ph.Activities;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.ph.MainActivity;
import com.ph.R;
import com.ph.Utils.DateOperations;
import com.ph.Utils.Dateutils;

import java.util.Calendar;

import butterknife.Bind;
import butterknife.ButterKnife;


public class NutritionEntrySelectFragment extends Fragment {

    private DatePickerDialog.OnDateSetListener datePicker;
    private Calendar calendar;
    @Bind(R.id.nutrition_entry_select_et_date_picker)
    EditText mNutritionEntryDate;
    private DateOperations mDateOperations;
    private String mSqlDateFormatString;
    static final int RESULT_CODE = 1;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        calendar = Calendar.getInstance();


        mDateOperations = new DateOperations(getContext());

        ((MainActivity)getActivity()).setDrawerState(false);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_nutrition_entry_select, container, false);
        ButterKnife.bind(this, view);
        updateLabel();
        datePicker = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, monthOfYear);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }
        };

        mNutritionEntryDate.setOnClickListener(new View.OnClickListener() {
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
        return view;
    }

    public void onClickBreakFast(View view) {

        Intent intent = new Intent(getContext(), NutritionEntryMain.class);
        intent.putExtra("NutritionType", "BreakFast");
        intent.putExtra("Date", mSqlDateFormatString);

        startActivityForResult(intent, RESULT_CODE);

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case RESULT_CODE:
                if (resultCode == Activity.RESULT_OK) {
                    Toast.makeText(getContext(), "Entry Saved...", Toast.LENGTH_LONG).show();
                }
        }

    }

    public void onClickLunch(View view) {

        Intent intent = new Intent(getContext(), NutritionEntryMain.class);
        intent.putExtra("NutritionType", "Lunch");
        intent.putExtra("Date", mSqlDateFormatString);
        startActivityForResult(intent, RESULT_CODE);

    }

    public void onClickDinner(View view) {

        Intent intent = new Intent(getContext(), NutritionEntryMain.class);
        intent.putExtra("NutritionType", "Dinner");
        intent.putExtra("Date", mSqlDateFormatString);
        startActivityForResult(intent, RESULT_CODE);

    }

    public void onClickSnack(View view) {

        Intent intent = new Intent(getContext(), NutritionEntryMain.class);
        intent.putExtra("NutritionType", "Snack");
        intent.putExtra("Date", mSqlDateFormatString);
        startActivityForResult(intent, RESULT_CODE);

    }

    public void updateLabel() {
        mNutritionEntryDate.setText(mDateOperations.getUniformDateFormat().format(calendar.getTime()));
        mSqlDateFormatString = mDateOperations.getMysqlDateFormat().format(calendar.getTime());
        Log.i("updateLabel", "Time has been set to the Edit Text");
    }


}
