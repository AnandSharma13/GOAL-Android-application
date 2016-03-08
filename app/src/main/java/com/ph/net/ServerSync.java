package com.ph.net;

import android.content.Context;
import android.util.Log;

import com.ph.model.Activity;
import com.ph.model.ActivityEntry;
import com.ph.model.DBOperations;
import com.ph.model.NutritionEntry;
import com.ph.model.User;
import com.ph.model.UserGoal;
import com.ph.model.UserSteps;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;

/**
 * Created by Anup on 1/17/2016.
 */
public class ServerSync {
    private Context context;
    private DBOperations dbOperations;

    public ServerSync(Context context) {
        this.context = context;
        dbOperations = new DBOperations(context);

    }

    public void insertRows(String tableName, JSONArray jArray) throws ParseException {

        switch (tableName) {
            case User.tableName:
                insertUserTableRows(jArray);
                break;
            case UserGoal.tableName:
                Log.i("insertRows", "insert user goal table");
                insertUserGoalTableRows(jArray);
                break;
            case Activity.tableName:
                Log.i("insertRows", "Insert Activity Table");
                insertActivityTableRows(jArray);
                break;
            case UserSteps.tableName:
                Log.i("insertRows", "Insert user_steps table");
                insertUserStepsTableRows(jArray);
                break;
            case ActivityEntry.tableName:
                Log.i("insertRows", "Insert Activity Entry Table");
                insertActivityEntryTableRows(jArray);
                break;
            case NutritionEntry.tableName:
                Log.i("insertRows", "Insert Nutrition Entry Table");
                insertNutritionEntryTableRows(jArray);

                break;
        }
    }


    private void insertNutritionEntryTableRows(JSONArray jArray) {


        for (int i = 0; i < jArray.length(); i++) {


            try {
                JSONObject row = jArray.optJSONObject(i);

                NutritionEntry nutritionEntry = new NutritionEntry();
                nutritionEntry.setNutrition_entry_id(row.getLong(NutritionEntry.column_nutritionEntryID));
                nutritionEntry.setTimestamp(row.getString(NutritionEntry.column_timestamp));
                nutritionEntry.setAttic_food(row.getInt(NutritionEntry.column_atticFood));
                nutritionEntry.setDairy(row.getInt(NutritionEntry.column_dairy));
                nutritionEntry.setFruit(row.getInt(NutritionEntry.column_fruit));
                nutritionEntry.setGoal_id(row.getLong(NutritionEntry.column_goalID));
                nutritionEntry.setGrain(row.getInt(NutritionEntry.column_grain));
                nutritionEntry.setNutrition_type(row.getString(NutritionEntry.column_nutritiontype));
                nutritionEntry.setTowards_goal(row.getInt(NutritionEntry.column_counttowardsgoal));
                nutritionEntry.setType(row.getString(NutritionEntry.column_type));
                nutritionEntry.setVegetable(row.getInt(NutritionEntry.column_vegetable));
                nutritionEntry.setWater_intake(row.getInt(NutritionEntry.column_waterintake));
                nutritionEntry.setNotes(row.getString(NutritionEntry.column_notes));
                //nutritionEntry.setImage(row.getInt(NutritionEntry.column_image)));
                nutritionEntry.setDate(row.getString(NutritionEntry.column_date));
                nutritionEntry.setIs_sync(1);
                long id = dbOperations.insertRow(nutritionEntry);

                Log.i("NutritionTableEntry", "A row with an ID " + String.valueOf(id) + " has been inserted into nutrition_entry table");

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

    private void insertActivityEntryTableRows(JSONArray jArray) {
        for (int i = 0; i < jArray.length(); i++) {


            try {
                JSONObject row = jArray.optJSONObject(i);
                ActivityEntry activityEntry = new ActivityEntry();
                activityEntry.setActivity_entry_id(row.getLong(ActivityEntry.column_activityEntryID));
                activityEntry.setActivity_id(row.getLong(ActivityEntry.column_activityID));
                activityEntry.setImage(row.getString(ActivityEntry.column_image));
                activityEntry.setTimestamp(row.getString(ActivityEntry.column_timestamp));
                activityEntry.setDate(row.getString(ActivityEntry.column_date));
                activityEntry.setNotes(row.getString(ActivityEntry.column_notes));
                activityEntry.setActivity_length(row.getString(ActivityEntry.column_activitylength));
                activityEntry.setCount_towards_goal(row.getInt(ActivityEntry.column_counttowardsgoal));
                activityEntry.setGoal_id(row.getLong(ActivityEntry.column_goalID));
                activityEntry.setRpe(row.getInt(ActivityEntry.column_rpe));
                activityEntry.setIs_sync(1);
                long id = dbOperations.insertRow(activityEntry);

                Log.i("ActivityEntryTableRows", "A row with an ID " + String.valueOf(id) + " has been inserted into activity_entry table");

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

    private void insertUserTableRows(JSONArray jArray) {
        for (int i = 0; i < jArray.length(); i++) {


            try {
                JSONObject row = jArray.optJSONObject(i);

                User user = new User();
                user.setUser_id(row.getInt("user_id"));
                user.setFirst_name(row.getString("first_name"));
                user.setLast_name(row.getString("last_name"));
                user.setType(row.getString("type"));
                user.setAge(row.getInt("age"));
                user.setPhone(row.getString("phone"));
                user.setGender(row.getString("gender"));
                user.setProgram(row.getString("program"));
                user.setRewards_count(row.getInt("rewards_count"));
                user.setIs_sync(1);
                long id = dbOperations.insertRow(user);

                Log.i("insertUserTableRows", "A row with an ID " + String.valueOf(id) + " has been inserted into user table");

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

    private void insertUserGoalTableRows(JSONArray jArray) throws ParseException {

        for (int i = 0; i < jArray.length(); i++) {


            try {
                JSONObject row = jArray.optJSONObject(i);

                UserGoal userGoal = new UserGoal();
                userGoal.setGoal_id(row.getLong("goal_id"));
                userGoal.setUser_id(row.getInt("user_id"));
                userGoal.setTimestamp(row.getString("timestamp"));
                userGoal.setType(row.getString("type"));
                userGoal.setStart_date(row.getString("start_date"));
                userGoal.setEnd_date(row.getString("end_date"));
                userGoal.setWeekly_count(row.getInt("weekly_count"));
                userGoal.setReward_type(row.getString("reward_type"));
                userGoal.setText(row.getString("text"));
                userGoal.setIs_sync(1);


                long id = dbOperations.insertRow(userGoal);

                Log.i("insertUserGoalTableRows", "A row with an ID " + String.valueOf(id) + " has been inserted into user_goal table");

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

    private void insertActivityTableRows(JSONArray jArray) {
        for (int i = 0; i < jArray.length(); i++) {

            try {
                JSONObject row = jArray.optJSONObject(i);

                Activity activity = new Activity();

                activity.setActivity_id(row.getLong(Activity.column_activityID));
                activity.setUser_id(row.getInt(Activity.column_userID));
                activity.setName(row.getString(Activity.column_name));
                activity.setLast_used(row.getString(Activity.column_lastUsed));
                activity.setHit_count(row.getInt(Activity.column_hitCount));
                activity.setType(row.getString(Activity.column_type));
                activity.setTimestamp(row.getString(Activity.column_timestamp));
                activity.setIs_sync(1);
                long id = dbOperations.insertRow(activity);

                Log.i("insertActivityTableRows", "A row with an ID " + String.valueOf(id) + " has been inserted into activity table"); //TODO: Replicate the log message in other functions.

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

    private void insertUserStepsTableRows(JSONArray jArray) {
        for (int i = 0; i < jArray.length(); i++) {

            try {
                JSONObject row = jArray.optJSONObject(i);
                UserSteps userSteps = new UserSteps();

                userSteps.setSteps_id(row.getLong(UserSteps.column_stepsID));
                userSteps.setUser_id(row.getInt(UserSteps.column_userID));
                userSteps.setTimestamp(row.getString(UserSteps.column_timestamp));
                userSteps.setSteps_count(row.getInt(UserSteps.column_stepscount));
                userSteps.setIs_sync(1);
                long id = dbOperations.insertRow(userSteps);

                Log.i("UserStepsTableRows", "A row with an ID " + String.valueOf(id) + " has been inserted into user_steps table");

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

}
