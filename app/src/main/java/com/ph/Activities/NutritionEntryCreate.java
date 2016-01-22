package com.ph.Activities;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.SeekBar;
import android.widget.TextView;

import com.ph.R;
import com.ph.Utils.DateOperations;
import com.ph.model.DBOperations;
import com.ph.model.NutritionEntry;
import com.ph.net.SyncUtils;

import java.util.ArrayList;


/**
 * Created by Anand on 1/20/2016.
 */

public class NutritionEntryCreate extends AppCompatActivity {

    private DateOperations mDateOperations;
    private DBOperations mDBOperations;
    private SharedPreferences prefs;
    private Button mDecreaseBtn;
    private Button mIncreaseBtn;
    private Button mSave;
    private TextView mOthersCountTv;
    private Handler repeatUpdateHandler;
    private Boolean mIncrementFlag;
    private Boolean mDecrementFlag;
    private DBOperations dbOperations;
    private CheckBox mDairyOne;
    private CheckBox mDairyTwo;
    private CheckBox mDairyThree;
    private CheckBox mProteinOne;
    private CheckBox mProteinTwo;
    private CheckBox mProteinThree;
    private CheckBox mProteinFour;
    private CheckBox mProteinFive;
    private CheckBox mVegetableOne;
    private CheckBox mVegetableTwo;
    private CheckBox mVegetableThree;
    private CheckBox mFruitOne;
    private CheckBox mFruitTwo;

    private CheckBox mGrainsOne;
    private CheckBox mGrainsTwo;
    private CheckBox mGrainsThree;
    private CheckBox mGrainsFour;
    private CheckBox mGrainsFive;
    private CheckBox mGrainsSix;
    private SeekBar mWaterIntake;

    private int atticFoodCount;
    private int dairyCount;
    private int proteinCount;
    private int vegetableCount;
    private int fruitCount;
    private int grainsCount;
    private int waterIntakeCount;







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



        repeatUpdateHandler = new Handler();
        mDecrementFlag = false;
        mIncrementFlag = false;
        dbOperations = new DBOperations(getApplicationContext());
        getDayRecord();

        //For testing
       // addNutritionEntryToDB();

        return;
    }



    public void initControls(){
        mDecreaseBtn = (Button) findViewById(R.id.nutrition_entry_create_btn_decrease);
        mIncreaseBtn = (Button) findViewById(R.id.nutrition_entry_create_btn_increase);
        mSave = (Button) findViewById(R.id.nutrition_entry_create_btn_save);
        mOthersCountTv = (TextView) findViewById(R.id.nutrition_entry_create_tv_others_count);

        mDairyOne = (CheckBox) findViewById(R.id.nutrition_entry_create_chkBx_Dairy_one);
        mDairyTwo = (CheckBox) findViewById(R.id.nutrition_entry_create_chkBx_Dairy_two);
        mDairyThree = (CheckBox) findViewById(R.id.nutrition_entry_create_chkBx_Dairy_three);
        mProteinOne = (CheckBox) findViewById(R.id.nutrition_entry_create_chkBx_protein_one);
        mProteinTwo = (CheckBox) findViewById(R.id.nutrition_entry_create_chkBx_protein_two);
        mProteinThree = (CheckBox) findViewById(R.id.nutrition_entry_create_chkBx_protein_three);
        mProteinFour = (CheckBox) findViewById(R.id.nutrition_entry_create_chkBx_protein_four);
        mProteinFive = (CheckBox) findViewById(R.id.nutrition_entry_create_chkBx_protein_five);
        mVegetableOne = (CheckBox) findViewById(R.id.nutrition_entry_create_chkBx_vegetable_one);
        mVegetableTwo = (CheckBox) findViewById(R.id.nutrition_entry_create_chkBx_vegetable_two);
        mVegetableThree = (CheckBox) findViewById(R.id.nutrition_entry_create_chkBx_vegetable_three);
        mFruitOne = (CheckBox) findViewById(R.id.nutrition_entry_create_chkBx_fruit_one);
        mFruitTwo = (CheckBox) findViewById(R.id.nutrition_entry_create_chkBx_fruit_two);

        mGrainsOne = (CheckBox) findViewById(R.id.nutrition_entry_create_chkBx_grains_one);
        mGrainsTwo = (CheckBox) findViewById(R.id.nutrition_entry_create_chkBx_grains_two);
        mGrainsThree = (CheckBox) findViewById(R.id.nutrition_entry_create_chkBx_grains_three);
        mGrainsFour = (CheckBox) findViewById(R.id.nutrition_entry_create_chkBx_grains_four);
        mGrainsFive = (CheckBox) findViewById(R.id.nutrition_entry_create_chkBx_grains_five);
        mGrainsSix = (CheckBox) findViewById(R.id.nutrition_entry_create_chkBx_grains_six);


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

        settingsBundle.putInt("ListSize", 1);
        settingsBundle.putString("Table " + 0, "nutrition_entry");

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
        getDayRecord();

    }

    public void getDayRecord() {

        Cursor dayRecordCursor =  mDBOperations.getNutritionDayRecords();
        dayRecordCursor.moveToFirst();
        atticFoodCount = dayRecordCursor.getInt(dayRecordCursor.getColumnIndex(NutritionEntry.column_atticFood));
        dairyCount = dayRecordCursor.getInt(dayRecordCursor.getColumnIndex(NutritionEntry.column_dairy));
        vegetableCount = dayRecordCursor.getInt(dayRecordCursor.getColumnIndex(NutritionEntry.column_vegetable));
        fruitCount = dayRecordCursor.getInt(dayRecordCursor.getColumnIndex(NutritionEntry.column_fruit));
        grainsCount = dayRecordCursor.getInt(dayRecordCursor.getColumnIndex(NutritionEntry.column_grain));
        waterIntakeCount = dayRecordCursor.getInt(dayRecordCursor.getColumnIndex(NutritionEntry.column_waterintake));

        dayRecordCursor.close();
        Log.i("NutritionEntryCreate", "Getting day records");
        return;
    }


    public void onSaveClick(){


//        nutritionEntry.setGoal_id(1);
//        nutritionEntry.setNutrition_type("BreakFast");
//        nutritionEntry.setTowards_goal(1);
//        nutritionEntry.setType("Some Text");
//        nutritionEntry.setAttic_food(3);
//        nutritionEntry.setDairy(4);
//        nutritionEntry.setVegetable(5);
//        nutritionEntry.setGrain(7);
//        nutritionEntry.setWater_intake(50);
//        nutritionEntry.setImage("Image URI here");
//        nutritionEntry.setNotes("Nutrition Text here");

        String attic_food = mOthersCountTv.getText().toString();
        if (mDairyOne.isChecked()){

        }



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

