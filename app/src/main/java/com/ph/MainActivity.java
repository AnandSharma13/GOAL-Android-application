package com.ph;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.animation.ObjectAnimator;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.ph.Activities.ActivityEntryMain;
import com.ph.Activities.NewGoal;
import com.ph.Activities.NutritionEntrySelect;
import com.ph.Utils.DateOperations;
import com.ph.Utils.MyGestureDetector;
import com.ph.fragments.NavigationDrawerFragment;
import com.ph.model.ActivityEntry;
import com.ph.model.DBOperations;
import com.ph.model.NutritionEntry;
import com.ph.model.User;
import com.ph.model.UserGoal;
import com.ph.model.UserSteps;
import com.ph.net.SessionManager;
import com.ph.net.SyncUtils;
import com.ph.view.CustomProgressBar;

import java.util.ArrayList;


/**
 * Created by Anand on 12/24/2016.
 */

public class MainActivity extends AppCompatActivity {

    private EditText nameView;
    private EditText emailView;
    private Button insertButton;
    private Button newGoalButton;
    private ContentResolver mContentResolver;
    private ArrayList array;
    private ListView mHomeListView;
    SharedPreferences sharedPreferences;
    GestureDetector mGestureDetector = null;
    View.OnTouchListener mGestureListener = null;
    private SessionManager sessionManager;
    private DateOperations dateOperations;
    private DBOperations dbOperations;
    private TextView stepsCount;
    private LinearLayout userStepsLayout;


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
        //Check LOGIN status
        sessionManager = new SessionManager(this);

        if(!sessionManager.checkLogin())
        {
            return;
        }


        dateOperations = new DateOperations(this);
        dbOperations = new DBOperations(this);


        toolbar = (Toolbar) findViewById(R.id.app_bar);
        toolbar.setTitle("G.O.A.L");
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayShowCustomEnabled(true);


        NavigationDrawerFragment drawerFragment = (NavigationDrawerFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
        drawerFragment.setUp((DrawerLayout) findViewById(R.id.drawer_layout), toolbar);


        mdrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        stepsCount = (TextView) findViewById(R.id.steps_count);
        userStepsLayout = (LinearLayout) findViewById(R.id.steps_count_layout);


        userStepsLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                LayoutInflater li = LayoutInflater.from(MainActivity.this);
                View dialogView = li.inflate(R.layout.user_steps_input, null);
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

                builder.setView(dialogView);

                final EditText userStepsInput = (EditText) dialogView.findViewById(R.id.user_steps_input);

                builder
                        .setCancelable(false)
                        .setPositiveButton("Save",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,int id) {
                                        int steps = Integer.parseInt(userStepsInput.getText().toString());

                                        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
                                        int user_id = Integer.parseInt(sharedPreferences.getString("user_id", "-1"));

                                        UserSteps userSteps = new UserSteps();

                                        userSteps.setSteps_count(steps);
                                        userSteps.setUser_id(user_id);

                                        dbOperations.insertRow(userSteps);

                                        Bundle settingsBundle = new Bundle();
                                        settingsBundle.putString("Type", "ClientSync");

                                        settingsBundle.putInt("ListSize", 1);

                                        settingsBundle.putString("Table " + 0, UserSteps.tableName);

                                        SyncUtils.TriggerRefresh(settingsBundle);

                                        stepsCount.setText(String.valueOf(dbOperations.getStepsCount()));

                                        Toast.makeText(MainActivity.this,"Successfully saved the steps count",Toast.LENGTH_SHORT).show();
                                    }
                                })
                        .setNegativeButton("Cancel",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                });
                // create alert dialog
                AlertDialog alertDialog = builder.create();

                // show it
                alertDialog.show();

            }
        });

        SyncUtils.CreateSyncAccount(this);
        mContentResolver = getContentResolver();



        stepsCount.setText(String.valueOf(dbOperations.getStepsCount()));



        //Below code would be move to an appropriate function
        CustomProgressBar nutritionProgressBar = (CustomProgressBar) findViewById(R.id.nutritionProgressBar);

        UserGoal userGoalNutrition = dbOperations.getCurrentGoalInfo("Nutrition");
        int  nutritionProgress= dbOperations.getWeekProgress("Nutrition");
        nutritionProgressBar.setText(String.valueOf(nutritionProgress));

        nutritionProgressBar.setAim_text("Aim "+String.valueOf(userGoalNutrition.getWeekly_count()));
        ObjectAnimator animation = ObjectAnimator.ofInt(nutritionProgressBar, "progress", 0, 100);
//        animation.setDuration(5000);
//        animation.setInterpolator(new DecelerateInterpolator());
//        animation.start();

        CustomProgressBar activityProgressBar = (CustomProgressBar) findViewById(R.id.activityProgressBar);
        UserGoal userGoalActivity = dbOperations.getCurrentGoalInfo("Activity");
        activityProgressBar.setAim_text("Aim "+String.valueOf(userGoalActivity.getWeekly_count()));
        int  activityProgress= dbOperations.getWeekProgress("Activity");
        activityProgressBar.setText(String.valueOf(activityProgress));

//        ObjectAnimator animation1 = ObjectAnimator.ofInt(activityProgressBar, "progress", 0, 100);
//        animation1.setDuration(5000); //in milliseconds
//        animation1.setInterpolator(new DecelerateInterpolator());
//        animation1.start();




        array = new ArrayList<>();
        array.add("Goal Button");



        mHomeListView = (ListView) findViewById(R.id.home_list);


        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.home_list_item, array);
        mHomeListView.setAdapter(adapter);




    mGestureDetector = new GestureDetector(this, new MyGestureDetector(getApplicationContext()));
    mGestureListener = new View.OnTouchListener() {
        public boolean onTouch(View v, MotionEvent aEvent) {
            if (mGestureDetector.onTouchEvent(aEvent))
                return true;
            else
                return false;
        }
    };
    mHomeListView.setOnTouchListener(mGestureListener);
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

//    @Override
//    protected void onResume() {
//        super.onResume();
//
//        if(!sessionManager.checkLogin())
//        {
//            return;
//        }
//
//        CustomProgressBar nutritionProgressBar = (CustomProgressBar) findViewById(R.id.nutritionProgressBar);
//        UserGoal userGoalNutrition = dbOperations.getCurrentGoalInfo("Nutrition");
//        nutritionProgressBar.setAim_text("Aim "+String.valueOf(userGoalNutrition.getWeekly_count()));
//
//
//        CustomProgressBar activityProgressBar = (CustomProgressBar) findViewById(R.id.activityProgressBar);
//        UserGoal userGoalActivity = dbOperations.getCurrentGoalInfo("Activity");
//        activityProgressBar.setAim_text("Aim " + String.valueOf(userGoalActivity.getWeekly_count()));
//    }
//


    @Override
    protected void onResumeFragments() {
        super.onResumeFragments();
        if(!sessionManager.checkLogin())
        {
            return;
        }

        CustomProgressBar nutritionProgressBar = (CustomProgressBar) findViewById(R.id.nutritionProgressBar);
        UserGoal userGoalNutrition = dbOperations.getCurrentGoalInfo("Nutrition");
        int  nutritionProgress= dbOperations.getWeekProgress("Nutrition");
        nutritionProgressBar.setText(String.valueOf(nutritionProgress));
        nutritionProgressBar.setAim_text("Aim "+String.valueOf(userGoalNutrition.getWeekly_count()));


        CustomProgressBar activityProgressBar = (CustomProgressBar) findViewById(R.id.activityProgressBar);
        int  activityProgress= dbOperations.getWeekProgress("Activity");
        activityProgressBar.setText(String.valueOf(activityProgress));
        UserGoal userGoalActivity = dbOperations.getCurrentGoalInfo("Activity");
        activityProgressBar.setAim_text("Aim " + String.valueOf(userGoalActivity.getWeekly_count()));
    }

    public void onNutritionProgressBarClick(View view){
        Intent intent = new Intent(this, NutritionEntrySelect.class);
        startActivity(intent);
    }

    public void onActivityProgressBarClick(View view){
        Intent intent = new Intent(this,ActivityEntryMain.class);
        startActivity(intent);
    }

    public void  onNewGoalClick (View view){
        Intent newGoalIntent = new Intent(this, NewGoal.class);
        startActivity(newGoalIntent);
    }

    public void startNext(View view) {
        Intent intent = new Intent(this, TempMain.class);
        startActivity(intent);
    }

    public void activityEntryButtonClick(View view)
    {
        sessionManager.logoutUser();
    }

}
