package com.ph;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.animation.ObjectAnimator;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.ph.Activities.ActivityEntryMain;
import com.ph.Activities.FoodDetail;
import com.ph.Activities.NewGoal;
import com.ph.Activities.RecordActivity;
import com.ph.Utils.MyGestureDetector;
import com.ph.fragments.NavigationDrawerFragment;
import com.ph.model.ActivityEntry;
import com.ph.model.DBHandler;
import com.ph.model.NutritionEntry;
import com.ph.model.User;
import com.ph.model.UserGoal;
import com.ph.net.SyncUtils;
import com.ph.view.CustomProgressBar;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


/**
 * Created by Anand on 12/24/2016.
 */

public class MainActivity extends AppCompatActivity {

    private EditText nameView;
    private EditText emailView;
    private Button insertButton;
    private Button newGoalButton;
    private ContentResolver mContentResolver;
    private int userID;
    private ArrayList array;
    private ListView mHomeListView;
    SharedPreferences sharedPreferences;
    GestureDetector mGestureDetector = null;
    View.OnTouchListener mGestureListener = null;



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
        toolbar.setTitle("G.O.A.L");
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayShowCustomEnabled(true);


        NavigationDrawerFragment drawerFragment = (NavigationDrawerFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
        drawerFragment.setUp((DrawerLayout) findViewById(R.id.drawer_layout), toolbar);

//        sharedPreferences =  getSharedPreferences("user_values",Context.MODE_PRIVATE);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        SharedPreferences.Editor editor = sharedPreferences.edit();


        //Inserting a sample user information.
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM'/'DD'/'yyyy");
        String date = simpleDateFormat.format(new Date());
        editor.putInt("user_id", 2);
        editor.putString("first_name", "John");
        editor.putString("last_name", "Smith");
        editor.putString("type", "U");
        editor.putInt("age", 24);
        editor.putString("start_date", date);
        editor.putInt("program_length", 12);

        editor.commit();

        //initializing shared preferences for Goal
        initGoalSharedPrefs();


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

        /*SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt("userID", 1);
        editor.commit();*/


        array = new ArrayList<>();
        array.add("Goal Button");



        mHomeListView = (ListView) findViewById(R.id.home_list);


        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.home_list_item, array);
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


    private void initGoalSharedPrefs(){
//        sharedPreferences =  getSharedPreferences("goal_values",Context.MODE_PRIVATE);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putInt("nutrition_goal_count", -1);
        editor.putString("nutrition_goal_text", "");
        editor.putInt("activity_goal_count", -1);
        editor.putString("activity_goal_text", "");
        editor.commit();

    }


    public void onNutritionProgressBarClick(View view){
        Intent intent = new Intent(this, FoodDetail.class);
        startActivity(intent);
    }

    public void onActivityProgressBarClick(View view){
        Intent intent = new Intent(this,RecordActivity.class);
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
        Intent intent = new Intent(this, ActivityEntryMain.class);
        startActivity(intent);
    }

}
