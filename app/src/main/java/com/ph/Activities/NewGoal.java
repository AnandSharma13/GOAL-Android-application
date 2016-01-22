package com.ph.Activities;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.gson.Gson;
import com.ph.R;
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

    private DateOperations dateOperations;

    UserGoal currentActivityGoal;
    UserGoal currentNutritionGoal;
    int prefsNutCount;
    String prefsNutText;
    int prefsActCount;
    String prefsActText;



    int userId,operatingWeek;
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

        dateOperations = new DateOperations(this);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);

        //Get the operating week of the program. It could be the current or period or the next one.

        operatingWeek = prefs.getInt("operating_week",-1);

        if(operatingWeek == -1)
        {
            operatingWeek = dateOperations.getWeeksTillDate(new Date());
        }


        currentActivityGoal = getCurrentGoalInfo("Activity");


        if(currentActivityGoal == null)
        {
            prefsActCount = -1;
            prefsActText="";
        }
        else
        {
            prefsActCount = currentActivityGoal.getWeekly_count();
            prefsActText = currentActivityGoal.getText();
            int tempActCount = prefsActCount,tens,ones,hundreds,temp;
            hundreds = tempActCount/100;
            temp = tempActCount %100;
            tens = temp/10;
            ones = temp%10;

            mEditActGoalHundreds.setText(String.valueOf(hundreds));
            mEditActGoalTens.setText(String.valueOf(tens));
            mEditActGoalOnes.setText(String.valueOf(ones));
            mEditActGoalText.setText(prefsActText);


        }



        currentNutritionGoal = getCurrentGoalInfo("Nutrition");

        if(currentNutritionGoal == null)
        {
            prefsNutCount = -1;
            prefsNutText = "";
        }
        else
        {
            prefsNutCount = currentNutritionGoal.getWeekly_count();
            prefsNutText = currentNutritionGoal.getText();

            int tens,ones;

            tens = prefsNutCount/10;
            ones = prefsNutCount%10;

            mEditNutGoalTens.setText(String.valueOf(tens));
            mEditNutGoalOnes.setText(String.valueOf(ones));
            mEditNutGoalText.setText(prefsNutText);
        }




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



        //checking for nutrition goal
        if (prefsNutCount != nutritionGoalCount || !prefsNutText.equals(nutritionGoalText)) {

            UserGoal nutritionGoal = new UserGoal(userId, "Nutrition", startDate, endDate, nutritionGoalCount, nutritionGoalText);
            insertGoal(nutritionGoal, view);
            Gson gson = new Gson();
            String nutritionUserGoal = gson.toJson(nutritionGoal);

            SharedPreferences.Editor editor = prefs.edit();
            editor.putString("current_nutrition_goal",nutritionUserGoal); //stored as json.
            editor.commit();
        }


        //checking for activity goal
        if (prefsActCount != activityGoalCount || !prefsActText.equals(activityGoalText)) {
            UserGoal activityGoal = new UserGoal(userId, "Activity", startDate, endDate, activityGoalCount, activityGoalText);
            insertGoal(activityGoal, view);

            SharedPreferences.Editor editor = prefs.edit();
            Gson gson = new Gson();
            String activityUserGoal = gson.toJson(activityGoal);
            editor.putString("current_activity_goal",activityUserGoal); //stored as json.
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


    private UserGoal getCurrentGoalInfo(String type)
    {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(NewGoal.this);
        Gson gson = new Gson();
        DBOperations dbOperations = new DBOperations(NewGoal.this);
        String currentGoalJson;
        if(type.equals("Activity"))
        {
            currentGoalJson = sharedPreferences.getString("current_activity_goal","");
            if(currentGoalJson.equals(""))
            {
                //Get it from the Database.

                return dbOperations.getCurrentUserGoalFromDB(type);
            }
            else
            {
                UserGoal userGoal = gson.fromJson(currentGoalJson, UserGoal.class);
                return userGoal;
            }
        }
        else if(type.equals("Nutrition"))
        {
            currentGoalJson = sharedPreferences.getString("current_nutrition_goal","");
            if(currentGoalJson.equals(""))
            {
                //Get it from the Database.
                return dbOperations.getCurrentUserGoalFromDB(type);
            }
            else
            {
                UserGoal userGoal = gson.fromJson(currentGoalJson, UserGoal.class);
                return userGoal;
            }
        }
        else
        {
            return null;
        }

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
