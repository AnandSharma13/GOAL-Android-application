package com.ph.net;

/**
 * Created by Anup on 1/16/2016.
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

import java.util.HashMap;
import java.util.Map;

public class SessionManager {
    // Shared Preferences
    SharedPreferences pref;

    // Editor for Shared preferences
    Editor editor;

    // Context
    Context _context;

    private String url;

    // Shared pref mode
    int PRIVATE_MODE = 0;

    // Sharedpref file name
    //private static final String PREF_NAME = "AndroidHivePref";

    // All Shared Preferences Keys
    private static final String IS_LOGIN = "IsLoggedIn";

    // User name (make variable public to access from outside)
    public static final String KEY_NAME = "name";

    // Email address (make variable public to access from outside)
    public static final String KEY_EMAIL = "email";

    // Constructor
    public SessionManager(Context context){
        this._context = context;
        pref = PreferenceManager.getDefaultSharedPreferences(_context);
        editor = pref.edit();

        Uri.Builder builder = new Uri.Builder();
        builder.scheme(_context.getString(R.string.server_protocol))
                .authority(_context.getString(R.string.server_ip))
                .appendEncodedPath(_context.getString(R.string.server_path))
                .appendPath("login_user.php");

        url = builder.toString();
    }

    /**
     * Create login session
     * */
    public void createLoginSession(String name, int id){

        Map<String, String> params = new HashMap<String, String>();

        params.put("first_name",name);
        params.put("ID",String.valueOf(id));

        CustomVolleyGsonRequest request = new CustomVolleyGsonRequest(url, Object.class, params, new Response.Listener<Object>() {
            @Override
            public void onResponse(Object response) {
                Log.d("createLoginSession", "Response received for login");
                editor.putBoolean(IS_LOGIN, true);
                Intent i = new Intent(_context, MainActivity.class);
                _context.startActivity(i);

                // commit changes
                editor.commit();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                editor.putBoolean(IS_LOGIN, false);
                error.printStackTrace();

                // commit changes
                editor.commit();
            }
        });

        SingletonVolley.getInstance(_context).addToRequestQueue(request);

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
        HashMap<String, String> user = new HashMap<String, String>();
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

        //TODO: drop the database on logout
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