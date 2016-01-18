package com.ph.net;

/**
 * Created by Anup on 24-12-15.
 */

import android.accounts.Account;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SyncResult;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.ph.R;
import com.ph.model.DBOperations;

import org.json.JSONArray;

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
    ServerSync serverSync = new ServerSync(getContext());

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
                .appendEncodedPath(mContext.getString(R.string.server_path))
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

    /**
     * Soon be moved to the login screen.
     */
    @Deprecated
    private void getServerData() {
        RequestQueue queue = SingletonVolley.getInstance(getContext()).getRequestQueue();
        Map<String, String> params = new HashMap<String, String>();

        params.put("syncMode", "SC");

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());

        params.put("user_id",sharedPreferences.getString("user_id","0"));
        CustomVolleyGsonRequest req = new CustomVolleyGsonRequest(url, Object.class, params, new Response.Listener<Object>() {
            @Override
            public void onResponse(Object response) {

                Log.d("getServerData", "Response received");

                Map<String, String> tableValues;
                try {
                    tableValues = (Map<String, String>) response;
                    for (Map.Entry<String, String> entry :
                            tableValues.entrySet()) {
                        String tableName = entry.getKey();
                        String values = entry.getValue();

                        JSONArray jArray = new JSONArray(values);

                        serverSync.insertRows(tableName, jArray);



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


    private void SendClientData(String[] tables) {

        RequestQueue queue = SingletonVolley.getInstance(getContext()).getRequestQueue();

        for (String table : tables) {

                final ArrayList<Object> syncData = uop.getSyncRows(table);
                //skip if there's nothing to sync
                if (syncData.size() == 0) {
                    Log.i("SendClientData", "Nothing to sync for table\t"+table);
                    continue;
                }

            Log.i("SendClientData", "Syncing data for table\t"+table);

            final String tableName = table;
                Map<String, String> params = new HashMap<String, String>();
                String JSON;
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
