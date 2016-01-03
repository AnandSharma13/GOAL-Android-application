package com.ph.net;

/**
 * Created by Anup on 24-12-15.
 */

import android.accounts.Account;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.Context;
import android.content.SyncResult;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.ph.R;
import com.ph.model.Activity;
import com.ph.model.DBOperations;
import com.ph.model.User;
import com.ph.model.UserGoal;
import com.ph.model.UserSteps;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Handle the transfer of data between a server and an
 * app, using the Android sync adapter framework.
 */
public class SyncAdapter extends AbstractThreadedSyncAdapter {
    // Global variables
    // Define a variable to contain a content resolver instance
    ContentResolver mContentResolver;
    Context mContext;
    String url = ""; //URL will be built in the constructor
    String json = "";
    DBOperations uop = new DBOperations(getContext());

    /**
     * Set up the sync adapter
     */
    public SyncAdapter(Context context, boolean autoInitialize) {
        super(context, autoInitialize);
        /*
         * If your app uses a content resolver, get an instance of it
         * from the incoming Context
         */
        mContentResolver = context.getContentResolver();
        mContext = context;
        Uri.Builder builder = new Uri.Builder();
        builder.scheme(mContext.getString(R.string.server_protocol))
                .authority(mContext.getString(R.string.server_ip))
                .appendPath(mContext.getString(R.string.server_path))
                .appendPath(mContext.getString(R.string.server_sync_script));

        url = builder.toString();


    }


    /**
     * Set up the sync adapter. This form of the
     * constructor maintains compatibility with Android 3.0
     * and later platform versions
     */
    public SyncAdapter(
            Context context,
            boolean autoInitialize,
            boolean allowParallelSyncs) {
        super(context, autoInitialize, allowParallelSyncs);
        /*
         * If your app uses a content resolver, get an instance of it
         * from the incoming Context
         */
        mContentResolver = context.getContentResolver();
    }

    /*
   * Specify the code you want to run in the sync adapter. The entire
   * sync adapter runs in a background thread, so you don't have to set
   * up your own background processing.
   */
    @Override
    public void onPerformSync(
            Account account,
            Bundle extras,
            String authority,
            ContentProviderClient provider,
            SyncResult syncResult) {
    /*
     * Put the data transfer code here.
     */
        Log.e("OnPerformSync", "Sync Function called!");

        String type = extras.getString("Type", "");
        String[] tables = new String[extras.getInt("ListSize", 0)];
        for (int i = 0; i < tables.length; i++) {
            tables[i] = extras.getString("Table " + i);
        }

        switch (type) {
            case "ServerSync":
                Log.i("OnPerformSync", "Get rows from the server and put it locally");
                getServerData();
                break;
            case "ClientSync":
                Log.i("OnPerformSync", "Push changes to the server");
                SendClientData(tables);
                break;
            default:
                Log.i("OnPerformSync", "Poll for server changes and push client changes");
                break;
        }

    }

    @Deprecated
    private String[] getArrayListOfTables(String tables) {

        return tables.split(";");
    }

    private void getServerData() {
        RequestQueue queue = SingletonVolley.getInstance(getContext()).getRequestQueue();
        Map<String, String> params = new HashMap<String, String>();

        params.put("syncMode", "SC");

        CustomVolleyGsonRequest req = new CustomVolleyGsonRequest(url, Object.class, params, new Response.Listener<Object>() {
            @Override
            public void onResponse(Object response) {

                Log.d("getServerData", "Response received");

                DBOperations uot = new DBOperations(getContext());

                Map<String, String> tableValues;
                try {
                    tableValues = (Map<String, String>) response;
                    for (Map.Entry<String, String> entry :
                            tableValues.entrySet()) {
                        String tableName = entry.getKey();
                        String values = entry.getValue();

                        JSONArray jArray = new JSONArray(values);

                        insertRows(tableName, jArray);



                    }
                } catch (Exception e) {
                    Log.e("getServerData", e.getLocalizedMessage());
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Log.e("Volley","Error in getting the response");
                Log.e("Volley",error.getLocalizedMessage());

            }
        });

        SingletonVolley.getInstance(getContext()).addToRequestQueue(req);

    }

    private void insertRows(String tableName, JSONArray jArray) {

        switch (tableName) {
            case "user":
                insertUserTableRows(jArray);
                break;
            case "user_goal":
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
        }
    }

    private void insertUserTableRows(JSONArray jArray) {
        DBOperations uot = new DBOperations(getContext());
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
                long id = uot.insertRow(user);

                Log.i("insertUserTableRows", "A row with an ID " + String.valueOf(id) + " has been inserted into user table");

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

    private void insertUserGoalTableRows(JSONArray jArray) {
        DBOperations uot = new DBOperations(getContext());
        for (int i = 0; i < jArray.length(); i++) {


            try {
                JSONObject row = jArray.optJSONObject(i);

                UserGoal userGoal = new UserGoal();
                userGoal.setGoal_id(row.getInt("goal_id"));
                userGoal.setUser_id(row.getInt("user_id"));
                userGoal.setTimestamp(row.getString("timestamp"));
                userGoal.setType(row.getString("type"));
                userGoal.setStart_date(row.getString("start_date"));
                userGoal.setEnd_date(row.getString("end_date"));
                userGoal.setWeekly_count(row.getInt("weekly_count"));
                userGoal.setReward_type(row.getString("reward_type"));
                userGoal.setText(row.getString("text"));
                userGoal.setIs_sync(1);


                long id = uot.insertRow(userGoal);

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

                activity.setActivity_id(row.getInt(Activity.column_activityID));
                activity.setUser_id(row.getInt(Activity.column_userID));
                activity.setName(row.getString(Activity.column_name));
                activity.setLast_used(row.getString(Activity.column_lastUsed));
                activity.setHit_count(row.getInt(Activity.column_hitCount));
                activity.setType(row.getString(Activity.column_type));
                activity.setTimestamp(row.getString(Activity.column_timestamp));
                activity.setIs_sync(1);
                long id = uop.insertRow(activity);

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

                userSteps.setSteps_id(row.getInt(UserSteps.column_stepsID));
                userSteps.setUser_id(row.getInt(UserSteps.column_userID));
                userSteps.setTimestamp(row.getString(UserSteps.column_timestamp));
                userSteps.setSteps_count(row.getInt(UserSteps.column_stepscount));
                userSteps.setIs_sync(1);
                long id = uop.insertRow(userSteps);

                Log.i("UserStepsTableRows", "A row with an ID " + String.valueOf(id) + " has been inserted into user_steps table");

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

    private void SendClientData(String[] tables) {

        RequestQueue queue = SingletonVolley.getInstance(getContext()).getRequestQueue();

        for (String table : tables) {

                final ArrayList<Object> syncData = uop.getSyncRows(table);
                //skip if there's nothing to sync
                if (syncData.size() == 0) {
                    Log.i("SendClientData", "Nothing to sync");
                    continue;
                }
                final String tableName = table;
                Map<String, String> params = new HashMap<String, String>();
                String JSON = "";
                Gson gson = new Gson();
            JSON = gson.toJson(syncData); //Could take a while.

                Log.d("OnPerformSync", "the request json is " + JSON);
                params.put("tableName", tableName);
                params.put("tableRows", JSON);
                params.put("syncMode", "CS");
                CustomVolleyGsonRequest req = new CustomVolleyGsonRequest(url, Object.class, params, new Response.Listener<Object>() {
                    @Override
                    public void onResponse(Object response) {

                        if (response instanceof String) {
                            String result = (String) response;
                            Log.d("Volley", "Response:" + result);
                        } else {
                            Log.i("OnPerformSync", "Yet to be implemented. Parse Server Response"); //TODO: Take care of this.
                        }
                        uop.setSyncFlag(tableName);
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Log.e("Volley", "Error in getting the response");
//                        Log.e("Volley", error.getLocalizedMessage());

                    }
                });
                SingletonVolley.getInstance(getContext()).addToRequestQueue(req);
        }

    }


}
