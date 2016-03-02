package com.ph.fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.NumberPicker;
import android.widget.RatingBar;
import android.widget.SeekBar;
import android.widget.Toast;

import com.ph.MainActivity;
import com.ph.R;
import com.ph.Utils.AlertDialogManager;
import com.ph.Utils.DateOperations;
import com.ph.model.DBOperations;
import com.ph.model.NutritionEntry;
import com.ph.model.UserGoal;
import com.ph.net.SyncUtils;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;


/**
 * Created by Anand on 1/20/2016.
 */

public class NutritionEntryCreateFragment extends Fragment {

    private DateOperations mDateOperations;
    private DBOperations mDBOperations;
    private SharedPreferences prefs;

    @Bind(R.id.nutrition_entry_create_btn_decrease)
    ImageButton mDecreaseBtn;
    @Bind(R.id.nutrition_entry_create_btn_increase)
    ImageButton mIncreaseBtn;
    @Bind(R.id.nutrition_entry_create_btn_save)
    Button mSave;
    @Bind(R.id.nutrition_entry_create_np_attic_food_count)
    NumberPicker mAtticFoodCountNp;
    private Handler repeatUpdateHandler;
    private Boolean mIncrementFlag;
    private Boolean mDecrementFlag;
    private DBOperations dbOperations;

    @Bind(R.id.nutrition_entry_create_chkBx_Dairy_one)
    CheckBox mDairyOne;
    @Bind(R.id.nutrition_entry_create_chkBx_Dairy_two)
    CheckBox mDairyTwo;
    @Bind(R.id.nutrition_entry_create_chkBx_Dairy_three)
    CheckBox mDairyThree;
    @Bind(R.id.nutrition_entry_create_chkBx_protein_one)
    CheckBox mProteinOne;
    @Bind(R.id.nutrition_entry_create_chkBx_protein_two)
    CheckBox mProteinTwo;
    @Bind(R.id.nutrition_entry_create_chkBx_protein_three)
    CheckBox mProteinThree;
    @Bind(R.id.nutrition_entry_create_chkBx_protein_four)
    CheckBox mProteinFour;
    @Bind(R.id.nutrition_entry_create_chkBx_protein_five)
    CheckBox mProteinFive;
    @Bind(R.id.nutrition_entry_create_chkBx_vegetable_one)
    CheckBox mVegetableOne;
    @Bind(R.id.nutrition_entry_create_chkBx_vegetable_two)
    CheckBox mVegetableTwo;
    @Bind(R.id.nutrition_entry_create_chkBx_vegetable_three)
    CheckBox mVegetableThree;
    @Bind(R.id.nutrition_entry_create_chkBx_fruits_one)
    CheckBox mFruitOne;
    @Bind(R.id.nutrition_entry_create_chkBx_fruits_two)
    CheckBox mFruitTwo;

    @Bind(R.id.nutrition_entry_create_chkBx_grains_one)
    CheckBox mGrainsOne;
    @Bind(R.id.nutrition_entry_create_chkBx_grains_two)
    CheckBox mGrainsTwo;
    @Bind(R.id.nutrition_entry_create_chkBx_grains_three)
    CheckBox mGrainsThree;
    @Bind(R.id.nutrition_entry_create_chkBx_grains_four)
    CheckBox mGrainsFour;
    @Bind(R.id.nutrition_entry_create_chkBx_grains_five)
    CheckBox mGrainsFive;
    @Bind(R.id.nutrition_entry_create_chkBx_grains_six)
    CheckBox mGrainsSix;
    @Bind(R.id.nutrition_entry_create_sb_water_intake)
    SeekBar mWaterIntake;
    @Bind(R.id.nutrition_entry_create_rating_bar_water_intake)
    RatingBar mRatingBar;

    private int atticFoodCount;
    private int dairyCount;
    private int proteinCount;
    private int vegetableCount;
    private int fruitCount;
    private int grainsCount;
    private int waterIntakeCount;
    private String mNutritionEntryText;
    private String mImagePath;
    private int mGoalCount;
    int checkCount = 0;


    private String mNutritionType;
    private String mSqlDateFormatString;

    private ArrayList<CheckBox> dairyChkbxList;


    private ArrayList<CheckBox> proteinChkbxList;

    private ArrayList<CheckBox> vegetableChkbxList;

    private ArrayList<CheckBox> fruitChkbxList;

    private ArrayList<CheckBox> grainsChkbxList;

    public final ArrayList<String> tablesList = new ArrayList<String>() {{

        add(NutritionEntry.tableName);
    }};
    private Toolbar toolbar;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mDateOperations = new DateOperations(getContext());
        mDBOperations = new DBOperations(getContext());
      //  mNutritionType = getIntent().getExtras().getString("NutritionType");
      //  mSqlDateFormatString = getIntent().getExtras().getString("Date")

        mDecrementFlag = false;
        mIncrementFlag = false;
        dbOperations = new DBOperations(getContext());


        if (getArguments() != null) {
            mNutritionType = getArguments().getString("NUTRITION_TYPE");
            mSqlDateFormatString = getArguments().getString("DATE");
            mNutritionEntryText = getArguments().getString("nutritionDetailsText");
            mImagePath = getArguments().getString("imagePath");
            mGoalCount = getArguments().getInt("GoalCount", 0);
        }



        return;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_nutrition_entry_create, container, false);
        ButterKnife.bind(this, view);
        getDayRecord();
        initControls();

        if (checkCount == 0)
            mSave.setEnabled(false);

        mWaterIntake.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if ((progress - waterIntakeCount) > 0) {
                    mSave.setEnabled(true);
                } else if ((progress - waterIntakeCount) == 0 && checkCount == 0 && (mAtticFoodCountNp.getValue() - atticFoodCount) == 0)
                    mSave.setEnabled(false);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mRatingBar.setRating(seekBar.getProgress());
            }
        });

        mAtticFoodCountNp.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                if ((newVal - atticFoodCount) > 0)
                    mSave.setEnabled(true);
                else if ((newVal - atticFoodCount) == 0 && checkCount == 0 && (mWaterIntake.getProgress() - atticFoodCount) == 0)
                    mSave.setEnabled(false);
            }
        });

        mDecreaseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAtticFoodCountNp.setValue(mAtticFoodCountNp.getValue() - 1);
                int newVal = mAtticFoodCountNp.getValue();
                if ((newVal - atticFoodCount) > 0)
                    mSave.setEnabled(true);
            }
        });


        mIncreaseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAtticFoodCountNp.setValue(mAtticFoodCountNp.getValue() + 1);
                int newVal = mAtticFoodCountNp.getValue();
                if ((newVal - atticFoodCount) == 0 && checkCount == 0 && (mWaterIntake.getProgress() - atticFoodCount) == 0)
                    mSave.setEnabled(false);

            }
        });


        mSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSaveClick(getView());
            }
        });


        //For testing
        //   addNutritionEntryToDB();

        setViewUnclickable();

        addListenerstoCheckBoxes();

        return view;
    }

    private void addListenerstoCheckBoxes() {
        for (CheckBox cb : dairyChkbxList) {
            cb.setOnCheckedChangeListener(new MyCheckedChangeListener());
        }
        for (CheckBox cb : proteinChkbxList) {
            cb.setOnCheckedChangeListener(new MyCheckedChangeListener());
        }
        for (CheckBox cb : vegetableChkbxList) {
            cb.setOnCheckedChangeListener(new MyCheckedChangeListener());
        }
        for (CheckBox cb : fruitChkbxList) {
            cb.setOnCheckedChangeListener(new MyCheckedChangeListener());
        }
        for (CheckBox cb : grainsChkbxList) {
            cb.setOnCheckedChangeListener(new MyCheckedChangeListener());
        }
    }


    public void initControls() {
//        mDecreaseBtn = (ImageButton) findViewById(R.id.nutrition_entry_create_btn_decrease);
//        mIncreaseBtn = (ImageButton) findViewById(R.id.nutrition_entry_create_btn_increase);
//        mSave = (Button) findViewById(R.id.nutrition_entry_create_btn_save);
//        mAtticFoodCountNp = (NumberPicker) findViewById(R.id.nutrition_entry_create_np_attic_food_count);
//
//
//        mDairyOne = (CheckBox) findViewById(R.id.nutrition_entry_create_chkBx_Dairy_one);
//        mDairyTwo = (CheckBox) findViewById(R.id.nutrition_entry_create_chkBx_Dairy_two);
//        mDairyThree = (CheckBox) findViewById(R.id.nutrition_entry_create_chkBx_Dairy_three);
//        mProteinOne = (CheckBox) findViewById(R.id.nutrition_entry_create_chkBx_protein_one);
//        mProteinTwo = (CheckBox) findViewById(R.id.nutrition_entry_create_chkBx_protein_two);
//        mProteinThree = (CheckBox) findViewById(R.id.nutrition_entry_create_chkBx_protein_three);
//        mProteinFour = (CheckBox) findViewById(R.id.nutrition_entry_create_chkBx_protein_four);
//        mProteinFive = (CheckBox) findViewById(R.id.nutrition_entry_create_chkBx_protein_five);
//        mVegetableOne = (CheckBox) findViewById(R.id.nutrition_entry_create_chkBx_vegetable_one);
//        mVegetableTwo = (CheckBox) findViewById(R.id.nutrition_entry_create_chkBx_vegetable_two);
//        mVegetableThree = (CheckBox) findViewById(R.id.nutrition_entry_create_chkBx_vegetable_three);
//        mFruitOne = (CheckBox) findViewById(R.id.nutrition_entry_create_chkBx_fruits_one);
//        mFruitTwo = (CheckBox) findViewById(R.id.nutrition_entry_create_chkBx_fruits_two);
//        mRatingBar = (RatingBar) findViewById(R.id.nutrition_entry_create_rating_bar_water_intake);
//        mGrainsOne = (CheckBox) findViewById(R.id.nutrition_entry_create_chkBx_grains_one);
//        mGrainsTwo = (CheckBox) findViewById(R.id.nutrition_entry_create_chkBx_grains_two);
//        mGrainsThree = (CheckBox) findViewById(R.id.nutrition_entry_create_chkBx_grains_three);
//        mGrainsFour = (CheckBox) findViewById(R.id.nutrition_entry_create_chkBx_grains_four);
//        mGrainsFive = (CheckBox) findViewById(R.id.nutrition_entry_create_chkBx_grains_five);
//        mGrainsSix = (CheckBox) findViewById(R.id.nutrition_entry_create_chkBx_grains_six);
//        mWaterIntake = (SeekBar) findViewById(R.id.nutrition_entry_create_sb_water_intake);



        dairyChkbxList = new ArrayList<CheckBox>() {{
            add(mDairyOne);
            add(mDairyTwo);
            add(mDairyThree);
        }};

        proteinChkbxList = new ArrayList<CheckBox>() {{
            add(mProteinOne);
            add(mProteinTwo);
            add(mProteinThree);
            add(mProteinFour);
            add(mProteinFive);
        }};

        vegetableChkbxList = new ArrayList<CheckBox>() {{
            add(mVegetableOne);
            add(mVegetableTwo);
            add(mVegetableThree);
        }};
        fruitChkbxList = new ArrayList<CheckBox>() {{
            add(mFruitOne);
            add(mFruitTwo);
        }};

        grainsChkbxList = new ArrayList<CheckBox>() {{
            add(mGrainsOne);
            add(mGrainsTwo);
            add(mGrainsThree);
            add(mGrainsFour);
            add(mGrainsFive);
            add(mGrainsSix);
        }};

    }

    public void setViewUnclickable() {

        for (int i = 0; i < dairyCount; i++) {
            CheckBox checkBox = dairyChkbxList.get(i);
            checkBox.setEnabled(false);
            checkBox.setChecked(true);
        }

        for (int i = 0; i < proteinCount; i++) {
            CheckBox checkBox = proteinChkbxList.get(i);
            checkBox.setEnabled(false);
            checkBox.setChecked(true);
        }

        for (int i = 0; i < grainsCount; i++) {
            CheckBox checkBox = grainsChkbxList.get(i);
            checkBox.setEnabled(false);
            checkBox.setChecked(true);
        }
        for (int i = 0; i < fruitCount; i++) {
            CheckBox checkBox = fruitChkbxList.get(i);
            checkBox.setEnabled(false);
            checkBox.setChecked(true);
        }
        for (int i = 0; i < vegetableCount; i++) {
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


    public void onClickIncreaseBtn(View view) {
        mAtticFoodCountNp.setValue(mAtticFoodCountNp.getValue() + 1);
        int newVal = mAtticFoodCountNp.getValue();
        if ((newVal - atticFoodCount) > 0)
            mSave.setEnabled(true);


    }

    public void onClickDecreaseBtn(View view) {
        mAtticFoodCountNp.setValue(mAtticFoodCountNp.getValue() - 1);
        int newVal = mAtticFoodCountNp.getValue();
        if ((newVal - atticFoodCount) == 0 && checkCount == 0 && (mWaterIntake.getProgress() - atticFoodCount) == 0)
            mSave.setEnabled(false);


    }


    public void getDayRecord() {

//        Cursor dayRecordCursor = mDBOperations.getNutritionDayRecords(mSqlDateFormatString,mNutritionType);
        Cursor dayRecordCursor = mDBOperations.getNutritionDayRecords(mSqlDateFormatString);

        dayRecordCursor.moveToFirst();
        atticFoodCount = dayRecordCursor.getInt(dayRecordCursor.getColumnIndex(NutritionEntry.column_atticFood));
        proteinCount = dayRecordCursor.getInt(dayRecordCursor.getColumnIndex(NutritionEntry.column_protein));
        dairyCount = dayRecordCursor.getInt(dayRecordCursor.getColumnIndex(NutritionEntry.column_dairy));
        vegetableCount = dayRecordCursor.getInt(dayRecordCursor.getColumnIndex(NutritionEntry.column_vegetable));
        fruitCount = dayRecordCursor.getInt(dayRecordCursor.getColumnIndex(NutritionEntry.column_fruit));
        grainsCount = dayRecordCursor.getInt(dayRecordCursor.getColumnIndex(NutritionEntry.column_grain));
        waterIntakeCount = dayRecordCursor.getInt(dayRecordCursor.getColumnIndex(NutritionEntry.column_waterintake));

        dayRecordCursor.close();
      //  Log.i("NutritionEntryCreateFragment", "Getting day records");
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

        int dairyEntry = dairyNow - dairyCount;
        int atticFoodEntry = atticFoodNow - atticFoodCount;
        int proteinEntry = proteinNow - proteinCount;
        int fruitEntry = fruitsNow - fruitCount;
        int vegetableEntry = vegetablesNow - vegetableCount;
        int grainEntry = grainsNow - grainsCount;
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

        if (userGoal == null) {
            AlertDialogManager alertDialogManager = new AlertDialogManager();
            alertDialogManager.showAlertDialog(getContext(), "No goal found", "There is no goal associated for the current week");
            return;
        }
        nutritionEntry.setGoal_id(userGoal.getGoal_id());
        //TODO Passed from the first intent of nutrition entry
        nutritionEntry.setNutrition_type(mNutritionType);
        nutritionEntry.setTowards_goal(mGoalCount);
        nutritionEntry.setType("Nutrition");
        nutritionEntry.setAttic_food(atticFoodEntry);
        nutritionEntry.setProtein(proteinEntry);
        nutritionEntry.setDairy(dairyEntry);
        nutritionEntry.setVegetable(vegetableEntry);
        nutritionEntry.setFruit(fruitEntry);
        nutritionEntry.setGrain(grainEntry);
        nutritionEntry.setWater_intake(waterIntakeEntry);
        nutritionEntry.setImage(mImagePath);
        //TODO passed from previous intent
        nutritionEntry.setNotes(mNutritionEntryText);
        nutritionEntry.setDate(mSqlDateFormatString);
        mDBOperations.insertRow(nutritionEntry);
        syncUtils.TriggerRefresh(settingsBundle);
        Log.i("NutritionEntryCreateFragment", "Sync called for nutrition entry");

        Toast.makeText(getActivity(), "Entry Saved.. ", Toast.LENGTH_LONG).show();
        Intent returnIntent = new Intent();
        Intent i = new Intent(getContext(), MainActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        NutritionEntryCreateFragment.this.startActivity(i);


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
                Log.i("NutritionEntryCreateFragment", "Increment Pressed");
                repeatUpdateHandler.postDelayed(new RepeatUpdater(), 50);
            } else if (mDecrementFlag) {
                Log.i("NutritionEntryCreateFragment", "Decrement Pressed");
                repeatUpdateHandler.postDelayed(new RepeatUpdater(), 50);
            }
        }
    }


    public static NutritionEntryCreateFragment newInstance(String nutritionDetailsText, String mImagePath,String mSqlDateFormatString, int goalCount,String mNutritionType) {
        NutritionEntryCreateFragment fragment = new NutritionEntryCreateFragment();
        Bundle args = new Bundle();
        args.putString("nutritionDetailsText", nutritionDetailsText);
        args.putString("imagePath", mImagePath);
        args.putString("DATE", mSqlDateFormatString);
        args.putInt("GoalCount", goalCount);
        args.putString("NUTRITION_TYPE", mNutritionType);
        fragment.setArguments(args);
        return fragment;
    }

    class MyCheckedChangeListener implements CompoundButton.OnCheckedChangeListener {

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if (isChecked)
                ++checkCount;
            else
                --checkCount;

            if (checkCount > 0)
                mSave.setEnabled(true);
            else if (checkCount <= 0 && (mWaterIntake.getProgress() - waterIntakeCount) == 0 && (mAtticFoodCountNp.getValue() - atticFoodCount) == 0) {
                checkCount = 0;
                mSave.setEnabled(false);
            }
        }
    }


}

