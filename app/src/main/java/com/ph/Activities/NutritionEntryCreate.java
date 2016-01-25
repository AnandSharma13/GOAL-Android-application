package com.ph.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.NumberPicker;
import android.widget.SeekBar;

import com.ph.R;
import com.ph.Utils.AlertDialogManager;
import com.ph.Utils.DateOperations;
import com.ph.model.DBOperations;
import com.ph.model.NutritionEntry;
import com.ph.model.UserGoal;
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
    private NumberPicker mAtticFoodCountNp;
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

    private String mNutritionType;
    private String mSqlDateFormatString;

   private ArrayList<CheckBox> dairyChkbxList;
// = new ArrayList<CheckBox>(){{
//        add(mDairyOne);
//        add(mDairyTwo);
//        add(mDairyThree);
//    }};

    private ArrayList<CheckBox> proteinChkbxList;

    private  ArrayList<CheckBox> vegetableChkbxList;

    private ArrayList<CheckBox> fruitChkbxList;

    private ArrayList<CheckBox> grainsChkbxList;

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
        mNutritionType= getIntent().getExtras().getString("NutritionType");
        mSqlDateFormatString = getIntent().getExtras().getString("Date");



        repeatUpdateHandler = new Handler();
        mDecrementFlag = false;
        mIncrementFlag = false;
        dbOperations = new DBOperations(getApplicationContext());

        getDayRecord();
        initControls();

        //For testing
    //   addNutritionEntryToDB();

        setViewUnclickable();
        return;
    }


    public void initControls() {
        mDecreaseBtn = (Button) findViewById(R.id.nutrition_entry_create_btn_decrease);
        mIncreaseBtn = (Button) findViewById(R.id.nutrition_entry_create_btn_increase);
        mSave = (Button) findViewById(R.id.nutrition_entry_create_btn_save);
        mAtticFoodCountNp = (NumberPicker) findViewById(R.id.nutrition_entry_create_np_attic_food_count);

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
        mWaterIntake = (SeekBar) findViewById(R.id.nutrition_entry_create_sb_water_intake);


        dairyChkbxList = new ArrayList<CheckBox>(){{
            add(mDairyOne);
            add(mDairyTwo);
            add(mDairyThree);
        }};

        proteinChkbxList = new ArrayList<CheckBox>(){{
            add(mProteinOne);
            add(mProteinTwo);
            add(mProteinThree);
            add(mProteinFour);
            add(mProteinFive);
        }};

        vegetableChkbxList = new ArrayList<CheckBox>(){{
            add(mVegetableOne);
            add(mVegetableTwo);
            add(mVegetableThree);
        }};
        fruitChkbxList = new ArrayList<CheckBox>(){{
            add(mFruitOne);
            add(mFruitTwo);
        }};

        grainsChkbxList = new ArrayList<CheckBox>(){{
            add(mGrainsOne);
            add(mGrainsTwo);
            add(mGrainsThree);
            add(mGrainsFour);
            add(mGrainsFive);
            add(mGrainsSix);
        }};

    }

    public void setViewUnclickable(){

        for(int i = 0; i<dairyCount;i++){
            CheckBox checkBox = dairyChkbxList.get(i);
            checkBox.setEnabled(false);
            checkBox.setChecked(true);
        }

        for(int i =0;i<proteinCount;i++){
            CheckBox checkBox = proteinChkbxList.get(i);
            checkBox.setEnabled(false);
            checkBox.setChecked(true);
        }

        for(int i =0;i<grainsCount;i++){
            CheckBox checkBox = grainsChkbxList.get(i);
            checkBox.setEnabled(false);
            checkBox.setChecked(true);
        }
        for(int i =0;i<fruitCount;i++){
            CheckBox checkBox = fruitChkbxList.get(i);
            checkBox.setEnabled(false);
            checkBox.setChecked(true);
        }
        for(int i =0;i<vegetableCount;i++){
            CheckBox checkBox = vegetableChkbxList.get(i);
            checkBox.setEnabled(false);
            checkBox.setChecked(true);
        }

        mWaterIntake.setProgress(waterIntakeCount);
        mAtticFoodCountNp.setMinValue(0);
        mAtticFoodCountNp.setMaxValue(20);
        mAtticFoodCountNp.setWrapSelectorWheel(false);
        mAtticFoodCountNp.setValue(atticFoodCount);


    }


    public void onClickIncreaseBtn(View view){
        mAtticFoodCountNp.setValue(mAtticFoodCountNp.getValue() + 1);

    }

    public void onClickDecreaseBtn(View view){
        mAtticFoodCountNp.setValue(mAtticFoodCountNp.getValue() -1);

    }





    public void getDayRecord() {

        Cursor dayRecordCursor = mDBOperations.getNutritionDayRecords(mSqlDateFormatString,mNutritionType);
        dayRecordCursor.moveToFirst();
        atticFoodCount = dayRecordCursor.getInt(dayRecordCursor.getColumnIndex(NutritionEntry.column_atticFood));
        proteinCount = dayRecordCursor.getInt(dayRecordCursor.getColumnIndex(NutritionEntry.column_protein));
        dairyCount = dayRecordCursor.getInt(dayRecordCursor.getColumnIndex(NutritionEntry.column_dairy));
        vegetableCount = dayRecordCursor.getInt(dayRecordCursor.getColumnIndex(NutritionEntry.column_vegetable));
        fruitCount = dayRecordCursor.getInt(dayRecordCursor.getColumnIndex(NutritionEntry.column_fruit));
        grainsCount = dayRecordCursor.getInt(dayRecordCursor.getColumnIndex(NutritionEntry.column_grain));
        waterIntakeCount = dayRecordCursor.getInt(dayRecordCursor.getColumnIndex(NutritionEntry.column_waterintake));

        dayRecordCursor.close();
        Log.i("NutritionEntryCreate", "Getting day records");
        return;
    }


    public void onSaveClick(View view) {

        int atticFoodNow = mAtticFoodCountNp.getValue();

        int dairyNow = isCheckedBoxChecked(mDairyOne) + isCheckedBoxChecked(mDairyTwo) + isCheckedBoxChecked(mDairyThree);
        int proteinNow = isCheckedBoxChecked(mProteinOne) + isCheckedBoxChecked(mProteinTwo) + isCheckedBoxChecked(mProteinThree) + isCheckedBoxChecked(mProteinFour) + isCheckedBoxChecked(mProteinFive);
        int fruitsNow = isCheckedBoxChecked(mFruitOne) + isCheckedBoxChecked(mFruitTwo);
        int vegetablesNow = isCheckedBoxChecked(mVegetableOne) + isCheckedBoxChecked(mVegetableTwo) + isCheckedBoxChecked(mVegetableThree);
        int grainsNow = isCheckedBoxChecked(mGrainsOne) + isCheckedBoxChecked(mGrainsTwo) + isCheckedBoxChecked(mGrainsThree) + isCheckedBoxChecked(mGrainsFour) + isCheckedBoxChecked(mGrainsFive) + isCheckedBoxChecked(mGrainsSix);
        int waterIntakeNow = mWaterIntake.getProgress();

        int dairyEntry = dairyNow -dairyCount;
        int atticFoodEntry = atticFoodNow - atticFoodCount;
        int proteinEntry = proteinNow -proteinCount;
        int fruitEntry = fruitsNow - fruitCount;
        int vegetableEntry = vegetablesNow - vegetableCount;
        int grainEntry = grainsNow -grainsCount;
        int waterIntakeEntry = waterIntakeNow - waterIntakeCount;


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
        UserGoal userGoal = dbOperations.getCurrentGoalInfo("Nutrition");

        if(userGoal == null)
        {
            AlertDialogManager alertDialogManager = new AlertDialogManager();
            alertDialogManager.showAlertDialog(NutritionEntryCreate.this,"No goal found","There is no goal associated for the current week");
            return;
        }
        nutritionEntry.setGoal_id(userGoal.getGoal_id());
        //TODO Passed from the first intent of nutrition entry
        nutritionEntry.setNutrition_type(mNutritionType);
        nutritionEntry.setTowards_goal(1);
        nutritionEntry.setType("Some Text");
        nutritionEntry.setAttic_food(atticFoodEntry);
        nutritionEntry.setProtein(proteinEntry);
        nutritionEntry.setDairy(dairyEntry);
        nutritionEntry.setVegetable(vegetableEntry);
        nutritionEntry.setFruit(fruitEntry);
        nutritionEntry.setGrain(grainEntry);
        nutritionEntry.setWater_intake(waterIntakeEntry);
        nutritionEntry.setImage("Image URI here");
        //TODO passed from previous intent
        nutritionEntry.setNotes("Nutrition Text here");
        DateOperations ds = new DateOperations(getApplicationContext());
        nutritionEntry.setDate(mSqlDateFormatString);
        mDBOperations.insertRow(nutritionEntry);
        syncUtils.TriggerRefresh(settingsBundle);
        Log.i("NutritionEntryCreate", "Sync called for nutrition entry");

        Intent returnIntent = new Intent();
        setResult(android.app.Activity.RESULT_OK,returnIntent);
        finish();


    }

    public int isCheckedBoxChecked(CheckBox chkbx) {
        if (chkbx.isChecked()) {
            return 1;
        }
        return 0;
    }

    //TODO check with for long click option
    class RepeatUpdater implements Runnable {
        @Override
        public void run() {
            if (mIncrementFlag) {
                Log.i("NutritionEntryCreate", "Increment Pressed");
                repeatUpdateHandler.postDelayed(new RepeatUpdater(), 50);
            } else if (mDecrementFlag) {
                Log.i("NutritionEntryCreate", "Decrement Pressed");
                repeatUpdateHandler.postDelayed(new RepeatUpdater(), 50);
            }
        }
    }


}

