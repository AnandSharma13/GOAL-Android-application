package com.ph.fragments;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.ph.Activities.MainActivity;
import com.ph.R;
import com.ph.Utils.DateOperations;
import com.ph.Utils.Dateutils;

import java.util.Calendar;

import butterknife.Bind;
import butterknife.ButterKnife;


public class NutritionEntrySelectFragment extends Fragment implements View.OnClickListener {

    static final int RESULT_CODE = 1;
    @Bind(R.id.nutrition_entry_select_et_date_picker)
    TextView mNutritionEntryDate;
    @Bind(R.id.nutrition_entry_select_btn_calendar)
    ImageButton mCalendarBtn;
    @Bind(R.id.nutrition_entry_select_btn_breakfast)
    Button mBreakFastBtn;
    @Bind(R.id.nutrition_entry_select_btn_lunch)
    Button mLunchBtn;
    @Bind(R.id.nutrition_entry_select_btn_dinner)
    Button mDinnerBtn;
    @Bind(R.id.nutrition_entry_select_btn_snack)
    Button mSnackBtn;
    private DatePickerDialog.OnDateSetListener datePicker;
    private Calendar calendar;
    private DateOperations mDateOperations;
    private String mSqlDateFormatString;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        calendar = Calendar.getInstance();
        mDateOperations = new DateOperations(getContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_nutrition_entry_select, container, false);
        ButterKnife.bind(this, view);
        Typeface typeFace=Typeface.createFromAsset(getActivity().getAssets(),"fonts/Eurostile.ttf");
        mNutritionEntryDate.setTypeface(typeFace);
        updateLabel();

        mCalendarBtn.setOnClickListener(this);
        mBreakFastBtn.setOnClickListener(this);
        mLunchBtn.setOnClickListener(this);
        mDinnerBtn.setOnClickListener(this);
        mSnackBtn.setOnClickListener(this);

        datePicker = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, monthOfYear);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }
        };
        return view;
    }



    @Override
    public void onResume() {
        super.onResume();

        ((MainActivity) getActivity()).setDrawerState(false);
        ((MainActivity) getActivity()).updateToolbar("Add Nutrition Record", R.color.nutrition_entry_app_bar, R.color.white);
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

    public void updateLabel() {
        mNutritionEntryDate.setText(mDateOperations.getUniformDateFormat().format(calendar.getTime()));
        mSqlDateFormatString = mDateOperations.getMysqlDateFormat().format(calendar.getTime());
        Log.i("updateLabel", "Time has been set to the Edit Text");
    }

    @Override
    public void onClick(View v) {
        NutritionEntryMainFragment fragment;
        switch (v.getId()){
            case R.id.nutrition_entry_select_btn_calendar:
                DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), R.style.nutritionEntryCalendarTheme, datePicker, calendar
                        .get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH));

                Dateutils dateutils = new Dateutils(getContext());
                datePickerDialog = dateutils.setGoalPeriodWeek(datePickerDialog);

                datePickerDialog.show();
                break;
            case R.id.nutrition_entry_select_btn_breakfast:
                 fragment = NutritionEntryMainFragment.newInstance("BreakFast", mSqlDateFormatString);
                ((MainActivity)getActivity()).setFragment(fragment, false);
                break;
            case R.id.nutrition_entry_select_btn_lunch:
                 fragment = NutritionEntryMainFragment.newInstance("Lunch", mSqlDateFormatString);
                ((MainActivity)getActivity()).setFragment(fragment, false);
                break;
            case R.id.nutrition_entry_select_btn_dinner:
                 fragment = NutritionEntryMainFragment.newInstance("Dinner", mSqlDateFormatString);
                ((MainActivity)getActivity()).setFragment(fragment, false);
                break;
            case R.id.nutrition_entry_select_btn_snack:
                fragment = NutritionEntryMainFragment.newInstance("Snack", mSqlDateFormatString);
                ((MainActivity)getActivity()).setFragment(fragment, false);
                break;

        }
    }
}
