package com.ph.net;

/**
 * Created by Anup on 1/16/2016 .
 */

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.util.Log;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.ph.Activities.LoginActivity;
import com.ph.MainActivity;
import com.ph.R;
import com.ph.Utils.AlertDialogManager;
import com.ph.model.DBHandler;
import com.ph.model.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class SessionManager {
    // Shared Preferences
    SharedPreferences pref;

    // Editor for Shared preferences
    Editor editor;

    // Context
    Context _context;

    private String url;

    private ServerSync serverSync;

    // Sharedpref file name
    //private static final String PREF_NAME = "AndroidHivePref";

    // All Shared Preferences Keys
    private static final String IS_LOGIN = "IsLoggedIn";

    // User name (make variable public to access from outside)
    public static final String KEY_NAME = "name";

    // Email address (make variable public to access from outside)
    public static final String KEY_EMAIL = "email";

    private AlertDialogManager alertDialogManager;

    // Constructor
    public SessionManager(Context context){
        this._context = context;
        pref = PreferenceManager.getDefaultSharedPreferences(_context);
        editor = pref.edit();
        serverSync = new ServerSync(context);

        Uri.Builder builder = new Uri.Builder();
        builder.scheme(_context.getString(R.string.server_protocol))
                .authority(_context.getString(R.string.server_ip))
                .appendEncodedPath(_context.getString(R.string.server_path))
                .appendPath("login_user.php");

        url = builder.toString();

        alertDialogManager = new AlertDialogManager();
    }

    /**
     * Create login session
     * */
    public void createLoginSession(String name, int id){

        Map<String, String> params = new HashMap<>();

        params.put("first_name",name);
        params.put("ID", String.valueOf(id));


        CustomVolleyGsonRequest request = new CustomVolleyGsonRequest(url, Object.class, params, new Response.Listener<Object>() {
            @Override
            public void onResponse(Object response) {
                Log.d("createLoginSession", "Response received for login");



                //Parse response to see if the login information is valid.
                Map<String,String> responseMap = (Map<String,String>)response;

                String responseResult = responseMap.get("result");
                String data = responseMap.get("data");

                if(responseResult.equals("error"))
                {
                    alertDialogManager.showAlertDialog(_context,"Login Failed!",data);
                }
                else {
                    if (responseResult.equals("server_error")) {
                        alertDialogManager.showAlertDialog(_context,"Internal Server Error","Please contact support.");
                    } else {

                        try {

                            JSONObject tableData = new JSONObject(data);
                            Iterator<String> iter = tableData.keys();
                            while (iter.hasNext()) {
                                String key = iter.next();
                                try {
                                    String value = tableData.getString(key);
                                    JSONArray jsonArray = new JSONArray(value);

                                    if(key.equals(User.tableName))
                                    {

                                        boolean success = putUserDetailsToPreferences(jsonArray);
                                        if(success)
                                        {
                                            editor.putBoolean(IS_LOGIN, true);
                                            editor.commit();
                                        }
                                        else
                                        {
                                            editor.putBoolean(IS_LOGIN, false);
                                            editor.commit();
                                        }

                                    }
                                    else {
                                        serverSync.insertRows(key, jsonArray);
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }catch (ParseException p){
                                    p.printStackTrace();
                                }
                            }

                            if(isLoggedIn()) {
                                Intent i = new Intent(_context, MainActivity.class);
                                _context.startActivity(i);
                            }
                            else
                            {
                                alertDialogManager.showAlertDialog(_context,"Shared Preferences","Failed to store user information locally");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }

                // commit changes

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                editor.putBoolean(IS_LOGIN, false);
                error.printStackTrace();

                alertDialogManager.showAlertDialog(_context,"Error in Network Connection","Please check your internet connectivity");
                // commit changes
                editor.commit();
            }
        });

        SingletonVolley.getInstance(_context).addToRequestQueue(request);

    }

    private boolean putUserDetailsToPreferences(JSONArray userArray)
    {
        try {
            JSONObject userObj;
            if(userArray.length()>0)
                userObj = userArray.getJSONObject(0); //Get the first JSON object
            else
                return false;

            Iterator<String> iter = userObj.keys();

            while (iter.hasNext()) {
                String key = iter.next();
                String value = userObj.getString(key);

                editor.putString(key,value);
            }

            return true;
            } catch (JSONException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Check login method wil check user login status
     * If false it will redirect user to login page
     * Else won't do anything
     * */
    public void checkLogin(){
        // Check login status
        if(!this.isLoggedIn()){
            // user is not logged in redirect him to Login Activity
            Intent i = new Intent(_context, LoginActivity.class);

            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            // Staring Login Activity
            _context.startActivity(i);
        }

    }



    /**
     * Get stored session data
     * */
    public HashMap<String, String> getUserDetails(){
        HashMap<String, String> user = new HashMap<>();
        // user name
        user.put(KEY_NAME, pref.getString(KEY_NAME, null));

        // user email id
        user.put(KEY_EMAIL, pref.getString(KEY_EMAIL, null));

        // return user
        return user;
    }

    /**
     * Clear session details
     * */
    public void logoutUser(){
        // Clearing all data from Shared Preferences
        editor.clear();
        editor.commit();
        _context.deleteDatabase(DBHandler.DATABASE_NAME);
        checkLogin();
    }

    /**
     * Quick check for login
     * **/
    // Get Login State
    public boolean isLoggedIn(){
        return pref.getBoolean(IS_LOGIN, false);
    }
}