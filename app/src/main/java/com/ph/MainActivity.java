package com.ph;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.animation.ObjectAnimator;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Canvas;
import android.os.Bundle;

import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;


import com.ph.model.ActivityEntry;
import com.ph.model.DBHandler;
import com.ph.model.NutritionEntry;
import com.ph.model.UserGoal;
import com.ph.model.User;
import com.ph.net.SyncUtils;
import com.ph.fragments.NavigationDrawerFragment;
import com.ph.view.CustomProgressBar;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private EditText nameView;
    private EditText emailView;
    private Button insertButton;
    private Button newGoalButton;
    private ContentResolver mContentResolver;

    // Constants
    // The authority for the sync adapter's content provider
    public static final String AUTHORITY = "com.ph";
    // An account type, in the form of a domain name
    public static final String ACCOUNT_TYPE = "example.com";
    // The account name
    public static final String ACCOUNT = "dummyaccount";
    private Toolbar toolbar;

    //This will go inside the onPerformSync bundle
    public final ArrayList<String> tablesList = new ArrayList<String>() {{
        add(User.tableName);
        add(UserGoal.tableName);
        add(ActivityEntry.tableName);
        add(NutritionEntry.tableName);
    }};


    DrawerLayout mdrawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.app_bar);
        toolbar.setTitle("Goal App");
        toolbar.setTitleTextColor(-1);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayShowCustomEnabled(true);


        NavigationDrawerFragment drawerFragment = (NavigationDrawerFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
        drawerFragment.setUp((DrawerLayout) findViewById(R.id.drawer_layout), toolbar);


        mdrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        SyncUtils.CreateSyncAccount(this);
        mContentResolver = getContentResolver();
        DBHandler dbHandler = new DBHandler(getApplicationContext());
        SQLiteDatabase db = dbHandler.getWritableDatabase();


        //Below code would be move to an appropriate function
        CustomProgressBar nutritionProgressBar = (CustomProgressBar) findViewById(R.id.nutritionProgressBar);
        nutritionProgressBar.setText("Nutrition");
        ObjectAnimator animation = ObjectAnimator.ofInt(nutritionProgressBar, "progress", 0, 100);
//        animation.setDuration(5000);
//        animation.setInterpolator(new DecelerateInterpolator());
//        animation.start();

        CustomProgressBar activityProgressBar = (CustomProgressBar) findViewById(R.id.activityProgressBar);
        activityProgressBar.setText("Activity+");

//        ObjectAnimator animation1 = ObjectAnimator.ofInt(activityProgressBar, "progress", 0, 100);
//        animation1.setDuration(5000); //in milliseconds
//        animation1.setInterpolator(new DecelerateInterpolator());
//        animation1.start();

    }


    /**
     * Create a new dummy account for the sync adapter
     *
     * @param context The application context
     */
    public static Account CreateSyncAccount(Context context) {
        // Create the account type and default account
        Account newAccount = new Account(
                ACCOUNT, ACCOUNT_TYPE);
        // Get an instance of the Android account manager
        AccountManager accountManager =
                (AccountManager) context.getSystemService(
                        ACCOUNT_SERVICE);
        /*
         * Add the account and account type, no password or user data
         * If successful, return the Account object, otherwise report an error.
         */
        if (accountManager.addAccountExplicitly(newAccount, null, null)) {
            /*
             * If you don't set android:syncable="true" in
             * in your <provider> element in the manifest,
             * then call context.setIsSyncable(account, AUTHORITY, 1)
             * here.
             */
            return newAccount;
        } else {
            /*
             * The account exists or some other error occurred. Log this, report it,
             * or handle it internally.
             */
            Log.e("MainActivity", "There is a problem in setting the account");
        }
        return newAccount;
    }


    public void startNext(View view) {
        Intent intent = new Intent(this, TempMain.class);
        startActivity(intent);
    }

}
