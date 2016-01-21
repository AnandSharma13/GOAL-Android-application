package com.ph.Activities;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.ph.R;
import com.ph.Utils.DateOperations;
import com.ph.Utils.StartEndDateObject;
import com.ph.model.Activity;
import com.ph.model.DBOperations;
import com.ph.model.NutritionEntry;
import com.ph.model.UserGoal;
import com.ph.model.UserSteps;
import com.ph.net.SyncUtils;

import java.util.ArrayList;

public class NutritionEntryCreate extends AppCompatActivity {

    DateOperations mDateOperations;
    DBOperations mDBOperations;
    SharedPreferences prefs;

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


        addNutritionEntryToDB();
        StartEndDateObject obj = mDateOperations.getDatesForToday();
        return;
    }

    public void addNutritionEntryToDB(){
        NutritionEntry nutritionEntry = new NutritionEntry();
        Bundle settingsBundle = new Bundle();
        SyncUtils syncUtils = new SyncUtils();

        settingsBundle.putString("Type", "ClientSync");

        settingsBundle.putInt("ListSize", tablesList.size());
        for (int i = 0; i < tablesList.size(); i++) {
            settingsBundle.putString("Table " + i, tablesList.get(i));
        }

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


    public void getDayRecords(){



    }



        

}

