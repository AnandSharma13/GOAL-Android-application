package com.ph.Activities;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;


import com.ph.R;
import com.ph.model.Activity;
import com.ph.model.DBOperations;
import com.ph.model.UserGoal;
import com.ph.model.UserSteps;
import com.ph.net.SyncUtils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class NewGoal extends AppCompatActivity {

    int userId = -1;
    static int count = 0;

    public final ArrayList<String> tablesList = new ArrayList<String>() {{
        // add(User.tableName);
        add(UserGoal.tableName);
        add(Activity.tableName);
        add(UserSteps.tableName);
        //add(ActivityEntry.tableName);
        //add(NutritionEntry.tableName);
    }};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_goal);

    }

    public void onRandomClick(View view) throws ParseException {

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        userId = preferences.getInt("userID", -1);
        GoalPeriod goalPeriod = getGoalPeriod();
        String startDate = goalPeriod.startDate;
        String endDate = goalPeriod.endDate;
        //user input value
        int nutritionWeeklyCount = 5;
        String nutritionText = "Apples";
        UserGoal nutritionGoal = new UserGoal(userId, "Nutrition", startDate, endDate, nutritionWeeklyCount, nutritionText);
        insertGoal(nutritionGoal, view);

        int activityMinutes = 10;
        String activityText = "Squats";
        UserGoal activityGoal = new UserGoal(userId, "Activity", startDate, endDate, activityMinutes, activityText);
        insertGoal(activityGoal, view);
    }

    public void insertGoal(UserGoal goal, View view) throws ParseException {

        DBOperations dbOps = new DBOperations(getApplicationContext());
        long id = dbOps.insertRow(goal);
        Log.i("Goal button", String.valueOf(id));
        Bundle settingsBundle = new Bundle();
        settingsBundle.putString("Type", "ClientSync");

        settingsBundle.putInt("ListSize", tablesList.size());
        for (int i = 0; i < tablesList.size(); i++) {
            settingsBundle.putString("Table " + i, tablesList.get(i));
        }
        SyncUtils.TriggerRefresh(settingsBundle);
        Log.i("NewGoal", "Goal inserted");
        Snackbar.make(view, "New goal created. Check Goal2 database ", Snackbar.LENGTH_LONG).setAction("Action", null).show();

    }

    public GoalPeriod getGoalPeriod() {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Calendar cal = Calendar.getInstance();
        String startDateTime = df.format(cal.getTime());
        cal.setTime(new java.util.Date());
        //Counting today's date as well
        cal.add(Calendar.DATE, 6);
        String endDateTime = df.format(cal.getTime());
        GoalPeriod goalPeriod = new GoalPeriod(startDateTime, endDateTime);
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
