package com.ph.Fragments;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ph.MainActivity;
import com.ph.R;
import com.ph.Utils.DateOperations;
import com.ph.Utils.Dateutils;

import java.util.Calendar;
import java.util.Date;

import butterknife.Bind;
import butterknife.ButterKnife;


public class ActivityEntryMainFragment extends Fragment {
    @Bind(R.id.activity_entry_date_text_view)
    TextView activityDate;
    @Bind(R.id.fragement_activity_entry_main_linear_layout_strength)
    LinearLayout mLinearLayoutStrength;
    @Bind(R.id.fragement_activity_entry_main_linear_layout_cardio)
    LinearLayout mLinearLayoutCardio;
    @Bind(R.id.fragement_activity_entry_main_linear_layout_lifestyle)
    LinearLayout mLinearLayoutLifstyle;
    @Bind(R.id.activity_entry_calendar_image)
    ImageView calendarImage;
    ActionBarDrawerToggle mDrawerToggle;
    private Calendar calendar;
    private DatePickerDialog.OnDateSetListener datePicker;
    private DateOperations dateOperations;
    private Toolbar toolbar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        calendar = Calendar.getInstance();
        dateOperations = new DateOperations(getContext());

        ((MainActivity) getActivity()).setDrawerState(false);
        ((MainActivity) getActivity()).updateToolbar("Add Activity Record", R.color.activity_entry_app_bar, R.color.black);

    }

    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity) getActivity()).setDrawerState(false);
        ((MainActivity) getActivity()).updateToolbar("Add Activity Record", R.color.activity_entry_app_bar, R.color.black);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_activity_entry_main, container, false);
        ButterKnife.bind(this, view);
        activityDate.setText(dateOperations.getUniformDateFormat().format(new Date()));

        Activity app = getActivity();
        mDrawerToggle =((MainActivity) app).getmDrawerToggle();
        mDrawerToggle.setDrawerIndicatorEnabled(false);

        datePicker = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, monthOfYear);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }
        };

       /* activityDate.setOnClickListener(new View.OnClickListener() {
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
*/
        activityDate.setOnClickListener(new ActivityEntryDateClickListener());
        calendarImage.setOnClickListener(new ActivityEntryDateClickListener());
        Typeface custom_font = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Eurostile.ttf");
        activityDate.setTypeface(custom_font);


        Button button, button1,button2;

        button = (Button) view.findViewById(R.id.button);
        button1 = (Button) view.findViewById(R.id.button1);
        button2 = (Button) view.findViewById(R.id.button3);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activityClick(v);
            }
        });

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activityClick(v);
            }
        });
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activityClick(v);
            }
        });
        return view;
    }

    private void updateLabel() {
        activityDate.setText(dateOperations.getUniformDateFormat().format(calendar.getTime()));
        Log.i("updateLabel", "Time has been set to the Edit Text");
    }

    public void activityClick(View view) {

        View parent = (View) view.getParent();
        Intent intent = new Intent(getContext(), ActivityEntryCreateFragment.class);
        String date= dateOperations.getMysqlDateFormat().format(calendar.getTime());
        Fragment fragment;
        switch (parent.getId()) {
            case R.id.fragement_activity_entry_main_linear_layout_cardio:
                intent.putExtra("key", "Cardio");
                fragment = ActivityEntryCreateFragment.newInstance("Cardio", date);
                ((MainActivity) getActivity()).setFragment(fragment, false);
                break;
            case R.id.fragement_activity_entry_main_linear_layout_strength:
                fragment = ActivityEntryCreateFragment.newInstance("Strength", date);
                ((MainActivity) getActivity()).setFragment(fragment, false);
                break;
            case R.id.fragement_activity_entry_main_linear_layout_lifestyle:
                fragment = ActivityEntryCreateFragment.newInstance("Lifestyle", date);
                ((MainActivity) getActivity()).setFragment(fragment, false);
                break;
        }
    }

    class ActivityEntryDateClickListener implements View.OnClickListener
    {
        @Override
        public void onClick(View v) {

            DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), datePicker, calendar
                    .get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH));

            Dateutils dateutils = new Dateutils(getContext());
            datePickerDialog = dateutils.setGoalPeriodWeek(datePickerDialog);
            datePickerDialog.show();
        }
    }
}
