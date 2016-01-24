package com.ph.Activities;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;

import com.ph.R;
import com.ph.Utils.DateOperations;
import com.ph.Utils.Dateutils;

import java.sql.Date;
import java.util.Calendar;


/**
 * Created by Anand on 1/20/2016.
 */

public class NutritionEntryMain extends AppCompatActivity {

    private EditText mGoalDetails;
    private DatePickerDialog.OnDateSetListener datePicker;
    private Calendar calendar;
    private EditText mNutritionEntryDate;
    private DateOperations mDateOperations;
    private String mNutritionType;
    private String mSqlDateFormatString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nutrition_entry_main);
        mNutritionType= getIntent().getExtras().getString("NutritionType");
        mSqlDateFormatString = getIntent().getExtras().getString("Date");
        mGoalDetails = (EditText) findViewById(R.id.goalDetailsText);
    }



    public void onClickNext(View view)
    {
        String goalText = mGoalDetails.getText().toString();
        Intent intent = new Intent(this, NutritionEntryCreate.class);
        intent.putExtra("Date", mSqlDateFormatString);
        intent.putExtra("NutritionType", mNutritionType);
        intent.putExtra("goalText", goalText);
        startActivityForResult(intent, 1);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1){
            if(resultCode == Activity.RESULT_OK){
                finish();
            }
        }
    }
}
