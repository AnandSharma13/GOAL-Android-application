package com.ph.Activities;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.ph.R;
import com.ph.Utils.DateOperations;
import com.ph.Utils.Dateutils;

import java.util.Calendar;


public class NutritionEntrySelect extends AppCompatActivity {

    private DatePickerDialog.OnDateSetListener datePicker;
    private Calendar calendar;
    private EditText mNutritionEntryDate;
    private DateOperations mDateOperations;
    private String mSqlDateFormatString;
    static final int RESULT_CODE=1;
    private android.support.v7.widget.Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nutrition_entry_select);
        calendar = Calendar.getInstance();
        mNutritionEntryDate = (EditText) findViewById(R.id.nutrition_entry_select_et_date_picker);
        mDateOperations = new DateOperations(NutritionEntrySelect.this);
        updateLabel();
        toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Nutrition Entry");
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

                DatePickerDialog datePickerDialog = new DatePickerDialog(NutritionEntrySelect.this, datePicker, calendar
                        .get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH));

                Dateutils dateutils = new Dateutils(NutritionEntrySelect.this);
                datePickerDialog = dateutils.setGoalPeriodWeek(datePickerDialog);
                datePickerDialog.show();
            }
        });
    }

    public void onClickBreakFast(View view){

        Intent intent = new Intent(NutritionEntrySelect.this, NutritionEntryMain.class);
        intent.putExtra("NutritionType", "BreakFast");
        intent.putExtra("Date", mSqlDateFormatString);

        startActivityForResult(intent, RESULT_CODE);

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode)
        {
            case RESULT_CODE:
                if(resultCode == Activity.RESULT_OK)
                {
                    Toast.makeText(this,"Entry Saved...",Toast.LENGTH_LONG).show();
                }
        }

    }
        public void onClickLunch(View view){

        Intent intent = new Intent(NutritionEntrySelect.this, NutritionEntryMain.class);
        intent.putExtra("NutritionType", "Lunch");
        intent.putExtra("Date", mSqlDateFormatString);
        startActivityForResult(intent, RESULT_CODE);

    }
    public void onClickDinner(View view){

        Intent intent = new Intent(NutritionEntrySelect.this, NutritionEntryMain.class);
        intent.putExtra("NutritionType", "Dinner");
        intent.putExtra("Date", mSqlDateFormatString);
        startActivityForResult(intent, RESULT_CODE);

    }

    public void onClickSnack(View view){

        Intent intent = new Intent(NutritionEntrySelect.this, NutritionEntryMain.class);
        intent.putExtra("NutritionType", "Snack");
        intent.putExtra("Date", mSqlDateFormatString);
        startActivityForResult(intent, RESULT_CODE);

    }

    public void updateLabel()
    {
        mNutritionEntryDate.setText(mDateOperations.getUniformDateFormat().format(calendar.getTime()));
        mSqlDateFormatString = mDateOperations.getMysqlDateFormat().format(calendar.getTime());
        Log.i("updateLabel", "Time has been set to the Edit Text");
    }



}
