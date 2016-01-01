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
import android.os.Bundle;
import android.util.Log;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.ph.model.DBOperations;

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
    String url = "http://192.168.1.11/goalServer/sync.php";
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
                break;
            case "ClientSync":
                Log.i("OnPerformSync", "Push changes to the server");
                SendClientData(tables);
                break;
            default:
                Log.i("OnPerformSync", "Poll for server changes and push client changes");
                break;
        }

        //TODO: Have a singleton instance of a Volley class and write a custom request.

        /*DBHandler dbHandler = new DBHandler(getContext());
        SQLiteDatabase db = dbHandler.getWritableDatabase();
        Log.i("MainActivity", "executed get writable database");
        db.close();
        dbHandler.close();*/

      /*  final ArrayList<User> tableList = uop.getSyncUserRows();

        if(tableList.size() == 0)
        {
            Log.d("OnPerformSync","Nothing to sync in user table");
            return;
        }

        JSONArray jArray = new JSONArray();

        for (User user:
                tableList) {

            jArray.put(user.getJSONObject());

        }

       json = jArray.toString();


        RequestQueue req = Volley.newRequestQueue(getContext());

        StringRequest sReq = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("Volley", "Response:" + response.toString());
                Boolean successSync = uop.setSyncUserRows(tableList);
                if(!successSync)
                    Log.e("onPerformSync","Update failed Locally");

                try {
                    JSONArray serverData = new JSONArray(response);

                    for (int i = 0; i < serverData.length(); i++) {
                        JSONObject userRow = serverData.optJSONObject(i);
                        User user = new User();
                        user.setId(userRow.getInt("user_id"));
                        user.setName(userRow.getString("user_name"));
                        user.setEmail(userRow.getString("user_email"));
                        user.setIs_sync(1);

                        long ID = uop.insertRow(user);
                        Log.i("onPerformSync", "row with id "+String.valueOf(ID)+" has been inserted");
                    }
                    Log.i("onPerformSync",String.valueOf(serverData.length())+" rows have been updated from the server");
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Log.e("Volley","Error in getting the response");
                Log.e("Volley",error.getLocalizedMessage());
            }
        }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("userTable",json);
                params.put("syncMode","CS");
                return params;
            }

        };




        JsonArrayRequest jsonReq = new JsonArrayRequest(Request.Method.POST, url, jArray, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Log.e("Volley","Response:"+response.toString());
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Volley","Error in getting the response");
                Log.e("Volley",error.getLocalizedMessage());
            }
        });



        req.add(sReq);
*/
        //Toast.makeText(getContext(), "Sync Adapter seems to be working!", Toast.LENGTH_SHORT).show();

    }

    private String[] getArrayListOfTables(String tables) {

        return tables.split(";");
    }

    private void SendClientData(String[] tables) {

        RequestQueue queue = SingletonVolley.getInstance(getContext()).getRequestQueue();

        for (String table : tables) {
            if (table.equals("user") || table.equals("usergoal")) {
                final ArrayList<Object> syncData = uop.getSyncRows(table);
                //skip if there's nothing to sync
                if (syncData.size() == 0)
                    continue;
                final String tableName = table;
                Map<String, String> params = new HashMap<String, String>();
                String JSON = "";
                Gson gson = new Gson();
                JSON = gson.toJson(syncData);

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
                            Log.i("OnPerformSync", "Yet to be implemented. Parse Server Response");
                        }
                        uop.setSyncFlag(tableName);
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Log.e("Volley", "Error in getting the response");
//                        Log.e("Volley", error.getLocalizedMessage());

                    }
                }) {
                    @Override
                    protected Map<String, String> getParams() {
                        Map<String, String> params = new HashMap<String, String>();
                        String JSON = "";
                        Gson gson = new Gson();
                        JSON = gson.toJson(syncData);
                        params.put("tableName", tableName);
                        params.put("tableRows", JSON);
                        params.put("syncMode", "CS");
                        return params;
                    }
                };
                SingletonVolley.getInstance(getContext()).addToRequestQueue(req);
            }
        }

    }


}
