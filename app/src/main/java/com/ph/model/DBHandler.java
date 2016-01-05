package com.ph.model;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ph.R;
import com.ph.net.SyncUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Anand on 25-12-15.
 */
public class DBHandler extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 2;
    private static final String DATABASE_NAME = "goal.db";
    private Context mContext;
    private String URL;

    public DBHandler(Context context) {

        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        mContext = context;
        Uri.Builder builder = new Uri.Builder();
        builder.scheme(mContext.getString(R.string.server_protocol))
                .authority(mContext.getString(R.string.server_ip))
                .appendPath(mContext.getString(R.string.server_path))
                .appendPath(mContext.getString(R.string.server_sync_script));

        URL = builder.toString();


    }

    @Override
    public void onCreate(SQLiteDatabase db) {

//        String userTable = "create table " + com.ph.model.User.tableName + "("
//                + com.ph.model.User.column_userID + " INTEGER PRIMARY KEY AUTOINCREMENT ,"
//                + com.ph.model.User.column_firstName + " TEXT, "
//                + com.ph.model.User.column_lastName + " TEXT, "
//                + com.ph.model.User.column_type + " TEXT, "
//                + com.ph.model.User.column_age + " INTEGER, "
//                + com.ph.model.User.column_phone + " TEXT, "
//                + com.ph.model.User.column_gender + " TEXT, "
//                + com.ph.model.User.column_program + " TEXT, "
//                + com.ph.model.User.column_rewardsCount + " INTEGER, "
//                + com.ph.model.User.column_sync + " INTEGER )";

        String userGoalTable = "create table " + UserGoal.tableName + "("
                + UserGoal.column_goalID + " INTEGER PRIMARY KEY AUTOINCREMENT ,"
                + UserGoal.column_userID + " INTEGER, "
                + UserGoal.column_timeStamp + " DATETIME DEFAULT CURRENT_TIMESTAMP, "
                + UserGoal.column_type + " TEXT, "
                + UserGoal.column_startDate + " TEXT, " //Don't we need a DATETIME type here?
                + UserGoal.column_endDate + " TEXT, "
                + UserGoal.column_weeklyCount + " INTEGER, "
                + UserGoal.column_rewardType + " TEXT DEFAULT 'NONE',"
                + UserGoal.column_text + " TEXT, "
                + UserGoal.column_sync + " INTEGER, "
                + "FOREIGN KEY (" + UserGoal.column_userID + ") REFERENCES " + com.ph.model.User.tableName + "(" + com.ph.model.User.column_userID + ")" + ")";

        String activityTable = "create table " + Activity.tableName + "("
                + Activity.column_activityID + " INTEGER PRIMARY KEY AUTOINCREMENT ,"
                + Activity.column_userID + " INTEGER, "
                + Activity.column_name + " TEXT, "
                + Activity.column_type + " TEXT, "
                + Activity.column_hitCount + " INTEGER, "
                + Activity.column_lastUsed + " TEXT, " //Don't we need Datetime type here?
                + Activity.column_timestamp + " DATETIME DEFAULT CURRENT_TIMESTAMP, "
                + Activity.column_isSync + " INTEGER, "
                + "FOREIGN KEY (" + Activity.column_userID + ") REFERENCES " + com.ph.model.User.tableName + "(" + com.ph.model.User.column_userID + ")" + ")";

        String activityEntryTable = "create table " + ActivityEntry.tableName + "("
                + ActivityEntry.column_activityEntryID + " INTEGER PRIMARY KEY AUTOINCREMENT ,"
                + ActivityEntry.column_goalID + " INTEGER, "
                + ActivityEntry.column_activityID + " INTEGER, "
                + ActivityEntry.column_timestamp + " DATETIME DEFAULT CURRENT_TIMESTAMP, "
                + ActivityEntry.column_rpe + " INTEGER, "
                + ActivityEntry.column_activitylength + " INTEGER, "
                + ActivityEntry.column_counttowardsgoal + " INTEGER, "
                + ActivityEntry.column_notes + " TEXT, "
                + ActivityEntry.column_image + " BLOB, "
                + ActivityEntry.column_sync + " INTEGER, "
                + "FOREIGN KEY (" + ActivityEntry.column_goalID + ") REFERENCES " + UserGoal.tableName + "(" + UserGoal.column_goalID + "), "
                + "FOREIGN KEY (" + ActivityEntry.column_activityID + ") REFERENCES " + Activity.tableName + "(" + Activity.column_activityID + ")" + ")";

        String nutritionEntryTable = "create table " + NutritionEntry.tableName + "("
                + NutritionEntry.column_nutritionEntryID + " INTEGER PRIMARY KEY AUTOINCREMENT ,"
                + NutritionEntry.column_goalID + " INTEGER, "
                + NutritionEntry.column_nutritiontype + " TEXT, "
                + NutritionEntry.column_timestamp + " DATETIME DEFAULT CURRENT_TIMESTAMP, "
                + NutritionEntry.column_counttowardsgoal + " INTEGER, "
                + NutritionEntry.column_type + " INTEGER, "
                + NutritionEntry.column_atticFood + " INTEGER, "
                + NutritionEntry.column_dairy + " TEXT, "
                + NutritionEntry.column_vegetable + " INTEGER, "
                + NutritionEntry.column_fruit + " INTEGER, "
                + NutritionEntry.column_grain + " INTEGER, "
                + NutritionEntry.column_waterintake + " INTEGER, "
                + NutritionEntry.column_notes + " TEXT, "
                + NutritionEntry.column_image + " BLOB, "
                + NutritionEntry.column_sync + " INTEGER, "
                + "FOREIGN KEY (" + NutritionEntry.column_goalID + ") REFERENCES " + UserGoal.tableName + "(" + UserGoal.column_goalID + ")" + ")";

        String userStepsTable = "create table " + UserSteps.tableName + "("
                + UserSteps.column_stepsID + " INTEGER PRIMARY KEY AUTOINCREMENT ,"
                + UserSteps.column_userID + " INTEGER, "
                + UserSteps.column_stepscount + " INTEGER, "
                + UserSteps.column_timestamp + " DATETIME DEFAULT CURRENT_TIMESTAMP, "
                + UserSteps.column_sync + " INTEGER, "
                + "FOREIGN KEY (" + UserSteps.column_userID + ") REFERENCES " + com.ph.model.User.tableName + "(" + com.ph.model.User.column_userID + ")" + ")";


//        db.execSQL(userTable);
//        Log.i("DBHandler", "user table created");
        db.execSQL(userGoalTable);
        Log.i("DBHandler", "Goal table created");
        db.execSQL(activityTable);
        Log.i("DBHandler", "Activity table created");

        db.execSQL(userStepsTable);
        Log.i("DBHandler", "user steps table created");


//         sqlite file gets corrupted when activityentry and nutritionentry table are created. Still an open issue.
        db.execSQL(activityEntryTable);
        Log.i("DBHandler", "Activity entry table created");
        db.execSQL(nutritionEntryTable);
        Log.i("DBHandler", "Nutrition entry table created");

        Bundle bundle = new Bundle();

        bundle.putString("Type", "ServerSync");
        SyncUtils.TriggerRefresh(bundle);

    }


    @Deprecated
    private void getRowsFromServer(String tableName) {
        //Use the variable URL instead of this.
        // String url = "http://192.168.1.110/goalServer/sync.php"; //Change to your server IP here
        Log.i("DBHandler", "Getting info from server");
        RequestQueue rQueue = Volley.newRequestQueue(mContext);

        StringRequest sReq = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.i("DBHandler", "Response: " + response);
                try {
                    JSONArray rows = new JSONArray(response);
                    for (int i = 0; i < rows.length(); i++) {
                        JSONObject userRow = rows.optJSONObject(i);
                        User user = new User();
                        DBOperations uop = new DBOperations(mContext);

                        user.setUser_id(userRow.getInt(User.column_userID));
                        user.setFirst_name(userRow.getString(User.column_firstName));
                        user.setLast_name(userRow.getString(User.column_lastName));
                        user.setType(userRow.getString(User.column_type));
                        user.setAge(userRow.getInt(User.column_age));
                        user.setPhone(userRow.getString(User.column_phone));
                        //TODO: Add phone model in here
                        user.setGender(userRow.getString(User.column_gender));
                        user.setProgram(userRow.getString(User.column_gender));
                        user.setRewards_count(userRow.getInt(User.column_rewardsCount));
                        user.setIs_sync(1);
                        uop.insertRow(user);
                    }

                    Log.i("DBHandler", "Sync succesful");
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e("DBHandler", "Sync from server failed");
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Volley", "Error in getting the response");
                Log.e("Volley", error.getLocalizedMessage());
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("syncMode", "SC");
                params.put("tableName", User.tableName);
                return params;
            }
        };
        rQueue.add(sReq);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed, all data will be gone!!!
        db.execSQL("DROP TABLE IF EXISTS " + com.ph.model.User.tableName);
        Log.w("DBHandler", "Version upgraded.");
        // Create tables again
        onCreate(db);
    }
}
