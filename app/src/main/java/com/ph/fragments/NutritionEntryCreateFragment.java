package com.ph.fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.NumberPicker;
import android.widget.RatingBar;
import android.widget.Toast;

import com.ph.Activities.MainActivity;
import com.ph.R;
import com.ph.Utils.AlertDialogManager;
import com.ph.Utils.DateOperations;
import com.ph.model.DBOperations;
import com.ph.model.NutritionEntry;
import com.ph.model.UserGoal;
import com.ph.net.SyncUtils;

import butterknife.Bind;
import butterknife.ButterKnife;


/**
 * Created by Anand on 1/20/2016.
 */

public class NutritionEntryCreateFragment extends Fragment implements View.OnClickListener, RatingBar.OnRatingBarChangeListener {

    @Bind(R.id.nutrition_entry_create_btn_decrease)
    ImageButton mDecreaseBtn;
    @Bind(R.id.nutrition_entry_create_btn_increase)
    ImageButton mIncreaseBtn;
    @Bind(R.id.nutrition_entry_create_btn_save)
    Button mSave;
    @Bind(R.id.nutrition_entry_create_rating_bar_dairy)
    RatingBar mDairyRatingBar;

    @Bind(R.id.nutrition_entry_create_rating_bar_vegetables)
    RatingBar mVegetablesRatingBar;

    @Bind(R.id.nutrition_entry_create_rating_bar_protein)
    RatingBar mProteinRatingBar;

    @Bind(R.id.nutrition_entry_create_rating_bar_fruits)
    RatingBar mFruitsRatingBar;

    @Bind(R.id.nutrition_entry_create_number_picker_attic)
    NumberPicker mAtticFoodCountNp;

    @Bind(R.id.nutrition_entry_create_rating_bar_grains)
    RatingBar mGrainsRatingBar;
    @Bind(R.id.nutrition_entry_create_rating_bar_water_intake)
    RatingBar mWaterRatingBar;

    int checkCount = 0;
    private DateOperations mDateOperations;
    private DBOperations mDBOperations;
    private SharedPreferences prefs;
    private DBOperations dbOperations;
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
    private String mNutritionType;
    private String mSqlDateFormatString;
    private Toolbar toolbar;
    private boolean mDecrementFlag;
    private boolean mIncrementFlag;

    public static NutritionEntryCreateFragment newInstance(String nutritionDetailsText, String mImagePath, String mSqlDateFormatString, int goalCount, String mNutritionType) {
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
    }


    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity) getActivity()).setDrawerState(false);
        ((MainActivity) getActivity()).updateToolbar(mNutritionType, R.color.nutrition_entry_app_bar, R.color.white);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_nutrition_entry_create, container, false);
        ButterKnife.bind(this, view);
        getDayRecord();
        if (checkCount == 0)
            mSave.setEnabled(false);
        mWaterRatingBar.setOnRatingBarChangeListener(this);
        mDecreaseBtn.setOnClickListener(this);
        mIncreaseBtn.setOnClickListener(this);
        mSave.setOnClickListener(this);

        mAtticFoodCountNp.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                if ((newVal - atticFoodCount) > 0)
                    mSave.setEnabled(true);
                else if ((newVal - atticFoodCount) <atticFoodCount && checkCount == 0 && (mWaterRatingBar.getRating() - atticFoodCount) == 0)
                    mSave.setEnabled(false);
            }
        });


        setViewUnclickable();
        return view;
    }


    public void setViewUnclickable() {
        mDairyRatingBar.setRating(dairyCount);
        mProteinRatingBar.setRating(proteinCount);
        mGrainsRatingBar.setRating(grainsCount);
        mFruitsRatingBar.setRating(fruitCount);
        mVegetablesRatingBar.setRating(fruitCount);
        mWaterRatingBar.setRating(waterIntakeCount);
        mAtticFoodCountNp.setMinValue(0);
        mAtticFoodCountNp.setMaxValue(20);
        mAtticFoodCountNp.setWrapSelectorWheel(false);
        mAtticFoodCountNp.setValue(atticFoodCount);
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
    }

    public void onSaveClick(View view) {

        int atticFoodNow = mAtticFoodCountNp.getValue();
        int dairyNow = (int) mDairyRatingBar.getRating();
        int proteinNow = (int) mProteinRatingBar.getRating();
        int fruitsNow = (int) mFruitsRatingBar.getRating();
        int vegetablesNow = (int) mVegetablesRatingBar.getRating();
        int grainsNow = (int) mGrainsRatingBar.getRating();
        int waterIntakeNow = (int) mWaterRatingBar.getRating();
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
        SyncUtils.TriggerRefresh(settingsBundle);
        //     Log.i("NutritionEntryCreateFragment", "Sync called for nutrition entry");
        Toast.makeText(getActivity(), "Entry Saved.. ", Toast.LENGTH_LONG).show();
        Intent i = new Intent(getContext(), MainActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        NutritionEntryCreateFragment.this.startActivity(i);
    }


    @Override
    public void onClick(View v) {
        int newVal;
        switch (v.getId()) {
            case R.id.nutrition_entry_create_btn_save:
                onSaveClick(v);
                return;
            case R.id.nutrition_entry_create_btn_decrease:
                mAtticFoodCountNp.setValue(mAtticFoodCountNp.getValue() - 1);
                break;
            case R.id.nutrition_entry_create_btn_increase:
                mAtticFoodCountNp.setValue(mAtticFoodCountNp.getValue() + 1);
                break;
        }
        newVal = mAtticFoodCountNp.getValue();
        if ((newVal - atticFoodCount) > 0)
            mSave.setEnabled(true);
    }

    @Override
    public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
        switch (ratingBar.getId()) {
            case R.id.nutrition_entry_create_rating_bar_water_intake:
                if ((rating - waterIntakeCount) > 0) {
                    mSave.setEnabled(true);
                } else if ((rating - waterIntakeCount) < waterIntakeCount && checkCount == 0 && (mAtticFoodCountNp.getValue() - atticFoodCount) == 0)
                    mSave.setEnabled(false);
                break;
        }
    }

    @Deprecated
    class MyCheckedChangeListener implements CompoundButton.OnCheckedChangeListener {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if (isChecked)
                ++checkCount;
            else
                --checkCount;

            if (checkCount > 0)
                mSave.setEnabled(true);
            else if (checkCount <= 0 && (mWaterRatingBar.getRating() - waterIntakeCount) == 0 && (mAtticFoodCountNp.getValue() - atticFoodCount) == 0) {
                checkCount = 0;
                mSave.setEnabled(false);
            }
        }
    }

    @Deprecated
    public int isCheckedBoxChecked(CheckBox chkbx) {
        if (chkbx.isChecked()) {
            return 1;
        }
        return 0;
    }


}

