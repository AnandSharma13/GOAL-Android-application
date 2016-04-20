package com.ph.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.ph.MainActivity;
import com.ph.R;
import com.ph.Utils.AlertDialogManager;
import com.ph.Utils.DateOperations;
import com.ph.Utils.StartEndDateObject;
import com.ph.model.Activity;
import com.ph.model.DBOperations;
import com.ph.model.UserGoal;
import com.ph.model.UserSteps;
import com.ph.net.SyncUtils;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;




public class NewGoal extends AppCompatActivity {

    private Button mSave;
    private EditText mEditNutGoalOnes;
    private EditText mEditNutGoalText;
    private EditText mEditActGoalOnes;
    private EditText mEditActGoalText;
    private EditText mEditTimesText;

    private DateOperations dateOperations;
    private DBOperations dbOperations;

    UserGoal currentActivityGoal;
    UserGoal currentNutritionGoal;
    int prefsNutCount;
    String prefsNutText;
    int prefsActCount;
    int prefsActTimes;
    String prefsActText;

    private Button pastGoalNutrition;
    private Button pastGoalActivity;
    private Toolbar toolbar;


    int userId,operatingWeek,currentWeek;
    static int count = 0;

    public final ArrayList<String> tablesList = new ArrayList<String>() {{
        add(UserGoal.tableName);
        add(Activity.tableName);
        add(UserSteps.tableName);

    }};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_goal);

        mEditNutGoalOnes = (EditText) findViewById(R.id.nutritionGoalOnes);
        mEditNutGoalText = (EditText) findViewById(R.id.nutritionGoalText);
        mEditActGoalOnes = (EditText) findViewById(R.id.activityGoalOnes);
        mEditActGoalText = (EditText) findViewById(R.id.activityGoalText);
        mEditTimesText = (EditText) findViewById(R.id.activity_tv_new_goal_times);
        dateOperations = new DateOperations(this);
        dbOperations = new DBOperations(this);

        toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        pastGoalActivity = (Button) findViewById(R.id.past_goal_activity);
        pastGoalNutrition = (Button) findViewById(R.id.past_goal_nutrition);
        getSupportActionBar().setTitle("New Goal");


       getSupportActionBar().setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(this, R.color.home_fragment_btn_new_goal_color)));
        pastGoalActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialogManager alertDialogManager = new AlertDialogManager();
                alertDialogManager.showPastGoalsDialog(NewGoal.this,"Use Past Goals","Activity");
            }
        });

        pastGoalNutrition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialogManager alertDialogManager = new AlertDialogManager();
                alertDialogManager.showPastGoalsDialog(NewGoal.this,"Use Past Goals","Nutrition");
            }
        });

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);

        //Get the operating week of the program. It could be the current or period or the next one. Known bug: operating_week is not reset. Change the
        //implementation.

        operatingWeek = prefs.getInt("operating_week",-1);
        currentWeek = dateOperations.getWeeksTillDate(new Date());

        if(operatingWeek == -1)
        {
            operatingWeek = currentWeek;
        }


        if(operatingWeek == currentWeek) {
            currentActivityGoal = dbOperations.getCurrentGoalInfo("Activity");
            currentNutritionGoal = dbOperations.getCurrentGoalInfo("Nutrition");
        }
        else {
            currentActivityGoal = dbOperations.getuserGoalFromDB("Activity", currentWeek);
            currentNutritionGoal = dbOperations.getuserGoalFromDB("Nutrition", currentWeek);
        }


        if(currentActivityGoal == null)
        {
            prefsActCount = -1;
            prefsActText="";
        }
        else
        {
            prefsActCount = currentActivityGoal.getWeekly_count();
            prefsActText = currentActivityGoal.getText();
            prefsActTimes =  currentActivityGoal.getTimes();
            setValues(prefsActCount,prefsActText,"Activity", prefsActTimes);

        }


        if(currentNutritionGoal == null)
        {
            prefsNutCount = -1;
            prefsNutText = "";
        }
        else
        {
            prefsNutCount = currentNutritionGoal.getWeekly_count();
            prefsNutText = currentNutritionGoal.getText();
            setValues(prefsNutCount,prefsNutText,"Nutrition",0);
        }

    }

    public void setValues(int count,String text,String type, int times) {
        if (type == "Nutrition") {
            mEditNutGoalOnes.setText(String.valueOf(count));
            mEditNutGoalText.setText(text);
        }
        else if (type == "Activity") {
            mEditActGoalOnes.setText(String.valueOf(count));
            mEditActGoalText.setText(text);
            mEditTimesText.setText(String.valueOf(times));
        }
    }

    public void onRandomClick(View view) throws ParseException {

        int nutritionGoalCount = Integer.parseInt(mEditNutGoalOnes.getText().toString());
        String nutritionGoalText = mEditNutGoalText.getText().toString();
    //    int nutritionGoalCount = nutritionGoalTens * 10 + nutritionGoalOnes * 1;


        int activityGoalCount = Integer.parseInt(mEditActGoalOnes.getText().toString());

        String activityGoalText = mEditActGoalText.getText().toString();
    //    int activityGoalCount = activityGoalHundreds * 100 + activityGoalTens * 10 + activityGoalOnes * 1;

        int activityTimes = Integer.parseInt(mEditTimesText.getText().toString());
        GoalPeriod goalPeriod = getGoalPeriod();
        String startDate = goalPeriod.startDate;
        String endDate = goalPeriod.endDate;


        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        userId = Integer.parseInt(prefs.getString("user_id", "-1"));



        //checking for nutrition goal
        if (prefsNutCount != nutritionGoalCount || !prefsNutText.equals(nutritionGoalText)) {

            UserGoal nutritionGoal = new UserGoal(userId, "Nutrition", startDate, endDate, nutritionGoalCount, nutritionGoalText,0);
            long id = insertGoal(nutritionGoal, view);
            nutritionGoal.setGoal_id(id);


            if(operatingWeek == currentWeek) {
                Gson gson = new Gson();
                String nutritionUserGoal = gson.toJson(nutritionGoal);
                SharedPreferences.Editor editor = prefs.edit();
                editor.putString("current_nutrition_goal", nutritionUserGoal); //stored as json.
                editor.putInt("current_goal_week_record", currentWeek);
                editor.commit();
            }
        }


        //checking for activity goal
        if (prefsActCount != activityGoalCount  || prefsActTimes != activityTimes || !prefsActText.equals(activityGoalText)) {
            UserGoal activityGoal = new UserGoal(userId, "Activity", startDate, endDate, activityGoalCount, activityGoalText, activityTimes);
            long id = insertGoal(activityGoal, view);
            activityGoal.setGoal_id(id);

            if(operatingWeek == currentWeek) {
                SharedPreferences.Editor editor = prefs.edit();
                Gson gson = new Gson();

                String activityUserGoal = gson.toJson(activityGoal);
                editor.putString("current_activity_goal", activityUserGoal); //stored as json.
                editor.putInt("current_goal_week_record",currentWeek);
                editor.commit();
            }

        }
        Toast.makeText(NewGoal.this,"Changes Saved",Toast.LENGTH_SHORT).show();



        Bundle settingsBundle = new Bundle();
        settingsBundle.putString("Type", "ClientSync");

        settingsBundle.putInt("ListSize", tablesList.size());
        for (int i = 0; i < tablesList.size(); i++) {
            settingsBundle.putString("Table " + i, tablesList.get(i));
        }
        SyncUtils.TriggerRefresh(settingsBundle);
        Log.i("NewGoal", "Goal inserted");
        Snackbar.make(view, "New goal created. Check Goal2 database ", Snackbar.LENGTH_LONG).setAction("Action", null).show();


        // Redirect to main activity.
                Intent i = new Intent(NewGoal.this, MainActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i);

    }

    public long insertGoal(UserGoal goal, View view) throws ParseException {



        long id = dbOperations.insertRow(goal);
        Log.i("Goal button", String.valueOf(id));

        return id;

    }

    public GoalPeriod getGoalPeriod() {



        StartEndDateObject goalWeek = dateOperations.getDatesFromWeekNumber(operatingWeek);


        Date startDate;
        if(operatingWeek == dateOperations.getWeeksTillDate(new Date()))
            startDate = new Date();
        else
            startDate = goalWeek.startDate;

                    Date endDate = goalWeek.endDate;


        GoalPeriod goalPeriod = new GoalPeriod(dateOperations.getMysqlDateFormat().format(startDate), dateOperations.getMysqlDateFormat().format(endDate));
        return goalPeriod;
    }




}

class GoalPeriod {

    String startDate;
    String endDate;

    public GoalPeriod(String startDate, String endDate) {
        this.startDate = startDate;
        this.endDate = endDate;
    }
}
