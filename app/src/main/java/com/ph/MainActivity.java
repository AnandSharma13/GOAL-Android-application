package com.ph;

import android.animation.ObjectAnimator;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.ph.Activities.ActivityEntryMain;
import com.ph.Activities.NewGoal;
import com.ph.Activities.NutritionEntrySelect;
import com.ph.Utils.DateOperations;
import com.ph.Utils.MyGestureDetector;
import com.ph.fragments.NavigationDrawerFragment;
import com.ph.model.ActivityEntry;
import com.ph.model.NutritionEntry;
import com.ph.model.User;
import com.ph.model.UserGoal;
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
    private int userID;
    private ArrayList array;
    private ListView mHomeListView;
    SharedPreferences sharedPreferences;
    GestureDetector mGestureDetector = null;
    View.OnTouchListener mGestureListener = null;
    private SessionManager sessionManager;
    private DateOperations dateOperations;



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
        dateOperations = new DateOperations(this);

        if(!sessionManager.checkLogin())
        {
            return;
        }


        toolbar = (Toolbar) findViewById(R.id.app_bar);
        toolbar.setTitle("G.O.A.L");
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayShowCustomEnabled(true);


        NavigationDrawerFragment drawerFragment = (NavigationDrawerFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
        drawerFragment.setUp((DrawerLayout) findViewById(R.id.drawer_layout), toolbar);


        mdrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        SyncUtils.CreateSyncAccount(this);
        mContentResolver = getContentResolver();





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
