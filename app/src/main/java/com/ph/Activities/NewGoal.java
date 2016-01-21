package com.ph.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


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


/**
 * Created by Anand on 12/28/2016.
 */

public class NewGoal extends AppCompatActivity {

    private Button mSave;
    private EditText mEditNutGoalOnes;
    private EditText mEditNutGoalTens;
    private EditText mEditNutGoalText;
    private EditText mEditActGoalOnes;
    private EditText mEditActGoalTens;
    private EditText mEditActGoalHundreds;
    private EditText mEditActGoalText;


    int userId;
    static int count = 0;

    public final ArrayList<String> tablesList = new ArrayList<String>() {{
        // add(User.tableName);
        add(UserGoal.tableName);
        add(Activity.tableName);
        add(UserSteps.tableName);
        //add(ActivityEntry.tableName);
        //add(NutrittionEntry.tableName);
    }};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_goal);

        mEditNutGoalOnes = (EditText) findViewById(R.id.nutritionGoalOnes);
        mEditNutGoalTens = (EditText) findViewById(R.id.nutritionGoalTens);
        mEditNutGoalText = (EditText) findViewById(R.id.nutritionGoalText);

        mEditActGoalOnes = (EditText) findViewById(R.id.activityGoalOnes);
        mEditActGoalTens = (EditText) findViewById(R.id.activityGoalTens);
        mEditActGoalHundreds = (EditText) findViewById(R.id.activityGoalHundreds);
        mEditActGoalText = (EditText) findViewById(R.id.activityGoalText);


        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        int prefNutCount = prefs.getInt("nutrition_goal_count", -1);
        String prefsNutText = prefs.getString("nutrition_goal_text", "");
//
//        if (prefNutCount != -1 || !prefsNutText.equals("")) {
//            int prefsNutTens = prefNutCount / 10;
//            int prefNutOnes = prefNutCount % 10;
//            mEditNutGoalOnes.setText(prefNutOnes);
//            mEditNutGoalTens.setText(prefsNutTens);
//            mEditActGoalText.setText(prefsNutText);
//        }
//
//        int prefActCount = prefs.getInt("activity_goal_count", -1);
//        String prefsActText = prefs.getString("activity_goal_text", "");
//        if (prefActCount != -1 || !prefsActText.equals("")) {
//            int prefActHundreds = prefActCount / 100;
//            int prefActTens = (prefActCount % 100) / 10;
//            int prefsActOnes = (prefActCount % 100) % 10;
//            mEditActGoalOnes.setText(prefsActOnes);
//            mEditActGoalTens.setText(prefActTens);
//            mEditActGoalHundreds.setText(prefActHundreds);
//            mEditActGoalText.setText(prefsActText);
//        }

    }

    public void onRandomClick(View view) throws ParseException {

        int nutritionGoalOnes = Integer.parseInt(mEditNutGoalOnes.getText().toString());
        int nutritionGoalTens = Integer.parseInt(mEditNutGoalTens.getText().toString());
        String nutritionGoalText = mEditNutGoalText.getText().toString();
        int nutritionGoalCount = nutritionGoalTens * 10 + nutritionGoalOnes * 1;


        int activityGoalOnes = Integer.parseInt(mEditActGoalOnes.getText().toString());
        int activityGoalTens = Integer.parseInt(mEditActGoalTens.getText().toString());
        int activityGoalHundreds = Integer.parseInt(mEditActGoalHundreds.getText().toString());
        String activityGoalText = mEditActGoalText.getText().toString();
        int activityGoalCount = activityGoalHundreds * 100 + activityGoalTens * 10 + activityGoalOnes * 1;

        GoalPeriod goalPeriod = getGoalPeriod();
        String startDate = goalPeriod.startDate;
        String endDate = goalPeriod.endDate;


        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        userId = Integer.parseInt(prefs.getString("user_id", "-1"));

        int prefsNutCount = prefs.getInt("nutrition_goal_count", -1);
        String prefsNutText = prefs.getString("nutrition_goal_text", "");
        int prefsActCount = prefs.getInt("activity_goal_count", -1);
        String prefsActText = prefs.getString("activity_goal_text", "");

        //checking for nutrition goal
        if (prefsNutCount != nutritionGoalCount || !prefsNutText.equals(nutritionGoalText)) {

            UserGoal nutritionGoal = new UserGoal(userId, "Nutrition", startDate, endDate, nutritionGoalCount, nutritionGoalText);
            insertGoal(nutritionGoal, view);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putInt("nutrition_goal_count", nutritionGoalCount);
            editor.putString("nutrition_goal_text", nutritionGoalText);
            editor.commit();

        }


        //checking for nutrition goal
        if (prefsActCount != activityGoalCount || !prefsActText.equals(activityGoalText)) {
            UserGoal activityGoal = new UserGoal(userId, "Activity", startDate, endDate, activityGoalCount, activityGoalText);
            insertGoal(activityGoal, view);

            SharedPreferences.Editor editor = prefs.edit();
            editor.putInt("activity_goal_count", activityGoalCount);
            editor.putString("activity_goal_text", activityGoalText);
            editor.commit();

        }
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
