package com.ph.Activities;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import com.ph.R;
import com.ph.Utils.DateOperations;
import com.ph.Utils.StartEndDateObject;
import com.ph.model.DBOperations;
import com.ph.model.NutritionEntry;
import com.ph.net.SyncUtils;

import java.util.ArrayList;


public class NutritionEntryCreate extends AppCompatActivity {

    DateOperations mDateOperations;
    DBOperations mDBOperations;
    SharedPreferences prefs;
    Button mDecreaseBtn;
    Button mIncreaseBtn;
    TextView mOthersCountTv;
    private Handler repeatUpdateHandler;
    private Boolean mIncrementFlag;
    private Boolean mDecrementFlag;

    public final ArrayList<String> tablesList = new ArrayList<String>() {{
        // add(User.tableName);
        //add(ActivityEntry.tableName);
        add(NutritionEntry.tableName);
    }};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nutrition_entry_create);
        mDateOperations = new DateOperations(this);
        mDBOperations = new DBOperations(getApplicationContext());


        mDecreaseBtn = (Button) findViewById(R.id.nutrition_entry_create_btn_decrease);
        mIncreaseBtn = (Button) findViewById(R.id.nutrition_entry_create_btn_increase);
        mOthersCountTv = (TextView) findViewById(R.id.nutrition_entry_create_tv_others_count);
        repeatUpdateHandler = new Handler();
        mDecrementFlag = false;
        mIncrementFlag = false;



        //For testing
        addNutritionEntryToDB();
        StartEndDateObject obj = mDateOperations.getDatesForToday();
        return;
    }



    public void addNutritionEntryToDB() {
        NutritionEntry nutritionEntry = new NutritionEntry();
        Bundle settingsBundle = new Bundle();
        SyncUtils syncUtils = new SyncUtils();

        settingsBundle.putString("Type", "ClientSync");

        /*settingsBundle.putInt("ListSize", tablesList.size());
        for (int i = 0; i < tablesList.size(); i++) {
            settingsBundle.putString("Table " + i, tablesList.get(i));
        }*/

        settingsBundle.putInt("ListSize",1);
        settingsBundle.putString("Table "+0, "nutrition_entry");

        nutritionEntry.setGoal_id(1);
        nutritionEntry.setNutrition_type("BreakFast");
        nutritionEntry.setTowards_goal(1);
        nutritionEntry.setType("Some Text");
        nutritionEntry.setAttic_food(3);
        nutritionEntry.setDairy(4);
        nutritionEntry.setVegetable(5);
        nutritionEntry.setGrain(7);
        nutritionEntry.setWater_intake(50);
        nutritionEntry.setImage("Image URI here");
        nutritionEntry.setNotes("Nutrition Text here");

        mDBOperations.insertRow(nutritionEntry);

        syncUtils.TriggerRefresh(settingsBundle);
        Log.i("NutritionEntryCreate", "Sync called for nutrition entry");


    }


    public void getDayRecords() {

    }

    //TODO check with for long click option
    class RepeatUpdater implements Runnable {
        @Override
        public void run() {
            if (mIncrementFlag){
                Log.i("NutritionEntryCreate", "Increment Pressed");
                repeatUpdateHandler.postDelayed(new RepeatUpdater(), 50);
            }
            else if (mDecrementFlag){
                Log.i("NutritionEntryCreate", "Decrement Pressed");
                repeatUpdateHandler.postDelayed(new RepeatUpdater(), 50);
            }
        }
    }



}

