package com.ph;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.ph.Activities.SettingsActivity;
import com.ph.Utils.DateOperations;
import com.ph.fragments.DrawerAdapter;
import com.ph.fragments.HomeFragment;
import com.ph.fragments.NavigationDrawerFragment;
import com.ph.fragments.NextGoalFragment;
import com.ph.model.ActivityEntry;
import com.ph.model.DBOperations;
import com.ph.model.NutritionEntry;
import com.ph.model.User;
import com.ph.model.UserGoal;
import com.ph.net.SessionManager;
import com.ph.net.SyncUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;



public class MainActivity extends AppCompatActivity implements HomeFragment.OnFragmentInteractionListener, NextGoalFragment.OnFragmentInteractionListener{

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
    private TabLayout tabLayout;
    private ViewPager mViewPager;
    private RecyclerView mDrawerRecylerView;


    // Constants
    // The authority for the sync adapter's content provider
    public static final String AUTHORITY = "com.ph";
    // An account type, in the form of a domain name
    public static final String ACCOUNT_TYPE = "example.com";
    // The account name
    public static final String ACCOUNT = "dummyaccount";
    public static final int HOME_FRAGMENT_POSITION=0;
    public static final int NEWGOAL_FRAGMENT_POSITION=1;

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

        mDrawerRecylerView = (RecyclerView) mdrawerLayout.findViewById(R.id.drawerList);

        final GestureDetector drawerGestureDector = new GestureDetector(MainActivity.this, new GestureDetector.SimpleOnGestureListener() {

            @Override public boolean onSingleTapUp(MotionEvent e) {
                return true;
            }

        });


        mDrawerRecylerView.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
            @Override
            public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
                View child = rv.findChildViewUnder(e.getX(), e.getY());

                if (child != null && drawerGestureDector.onTouchEvent(e)) {
                    mdrawerLayout.closeDrawers();
                    RecyclerView.ViewHolder vh = rv.getChildViewHolder(child);
                    DrawerAdapter drawerAdapter = (DrawerAdapter) rv.getAdapter();

                    String title = drawerAdapter.getList().get(rv.getChildAdapterPosition(child)).getTitle();
                    Intent intent;

                    switch (title) {
                        case "Logout":
                            sessionManager.logoutUser();
                            break;
                        case "Temp":
                            intent = new Intent(MainActivity.this, TempMain.class);
                            startActivity(intent);
                            break;
                        case "Settings":
                            intent = new Intent(MainActivity.this, SettingsActivity.class);
                            startActivity(intent);
                            break;
                        default:
                            Toast.makeText(MainActivity.this, "The Item Clicked is: " + title, Toast.LENGTH_SHORT).show();
                    }

                    return true;

                }

                return false;
            }

            @Override
            public void onTouchEvent(RecyclerView rv, MotionEvent e) {

            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

            }
        });

//        stepsCount = (TextView) findViewById(R.id.steps_count);
//        userStepsLayout = (LinearLayout) findViewById(R.id.steps_count_layout);


//        userStepsLayout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                LayoutInflater li = LayoutInflater.from(MainActivity.this);
//                View dialogView = li.inflate(R.layout.user_steps_input, null);
//                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
//
//                builder.setView(dialogView);
//
//                final EditText userStepsInput = (EditText) dialogView.findViewById(R.id.user_steps_input);
//
//                builder
//                        .setCancelable(false)
//                        .setPositiveButton("Save",
//                                new DialogInterface.OnClickListener() {
//                                    public void onClick(DialogInterface dialog,int id) {
//                                        int steps = Integer.parseInt(userStepsInput.getText().toString());
//
//                                        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
//                                        int user_id = Integer.parseInt(sharedPreferences.getString("user_id", "-1"));
//
//                                        UserSteps userSteps = new UserSteps();
//
//                                        userSteps.setSteps_count(steps);
//                                        userSteps.setUser_id(user_id);
//
//                                        dbOperations.insertRow(userSteps);
//
//                                        Bundle settingsBundle = new Bundle();
//                                        settingsBundle.putString("Type", "ClientSync");
//
//                                        settingsBundle.putInt("ListSize", 1);
//
//                                        settingsBundle.putString("Table " + 0, UserSteps.tableName);
//
//                                        SyncUtils.TriggerRefresh(settingsBundle);
//
//                                        stepsCount.setText(String.valueOf(dbOperations.getStepsCount()));
//
//                                        Toast.makeText(MainActivity.this,"Successfully saved the steps count",Toast.LENGTH_SHORT).show();
//                                    }
//                                })
//                        .setNegativeButton("Cancel",
//                                new DialogInterface.OnClickListener() {
//                                    public void onClick(DialogInterface dialog, int id) {
//                                        dialog.cancel();
//                                    }
//                                });
//                // create alert dialog
//                AlertDialog alertDialog = builder.create();
//
//                // show it
//                alertDialog.show();
//
//            }
//        });

        SyncUtils.CreateSyncAccount(this);
        mContentResolver = getContentResolver();



     //  stepsCount.setText(String.valueOf(dbOperations.getStepsCount()));



        //Below code would be move to an appropriate function
   //    CustomProgressBar nutritionProgressBar = (CustomProgressBar) findViewById(R.id.nutritionProgressBar);

//        UserGoal userGoalNutrition = dbOperations.getCurrentGoalInfo("Nutrition");
//        int  nutritionProgress= dbOperations.getWeekProgress("Nutrition");
//        nutritionProgressBar.setText(String.valueOf(nutritionProgress));
//
//        nutritionProgressBar.setAim_text("Aim "+String.valueOf(userGoalNutrition.getWeekly_count()));
//        ObjectAnimator animation = ObjectAnimator.ofInt(nutritionProgressBar, "progress", 0, 100);
//        animation.setDuration(5000);
//        animation.setInterpolator(new DecelerateInterpolator());
//        animation.start();

//        CustomProgressBar activityProgressBar = (CustomProgressBar) findViewById(R.id.activityProgressBar);
//        UserGoal userGoalActivity = dbOperations.getCurrentGoalInfo("Activity");
//        activityProgressBar.setAim_text("Aim "+String.valueOf(userGoalActivity.getWeekly_count()));
//        int  activityProgress= dbOperations.getWeekProgress("Activity");
//        activityProgressBar.setText(String.valueOf(activityProgress));

//        ObjectAnimator animation1 = ObjectAnimator.ofInt(activityProgressBar, "progress", 0, 100);
//        animation1.setDuration(5000); //in milliseconds
//        animation1.setInterpolator(new DecelerateInterpolator());
//        animation1.start();




        array = new ArrayList<>();
 //       array.add("Goal Button");



   //     mHomeListView = (ListView) findViewById(R.id.home_list);


//        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.home_list_item, array);
//        mHomeListView.setAdapter(adapter);




//    mGestureDetector = new GestureDetector(this, new MyGestureDetector(getApplicationContext()));
//    mGestureListener = new View.OnTouchListener() {
//        public boolean onTouch(View v, MotionEvent aEvent) {
//            if (mGestureDetector.onTouchEvent(aEvent))
//                return true;
//            else
//                return false;
//        }
//    };
//    mHomeListView.setOnTouchListener(mGestureListener);


        //new code here
        mViewPager = (ViewPager) findViewById(R.id.viewpager);

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                Log.i("selected page\t", String.valueOf(position));

                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                int operatingWeek;
                SharedPreferences.Editor editor = sharedPreferences.edit();

                switch (position) {
                    case HOME_FRAGMENT_POSITION:
                        operatingWeek = new DateOperations(getApplicationContext()).getWeeksTillDate(new Date());
                        editor.putInt("operating_week", operatingWeek);
                        editor.commit();
                        Toast.makeText(getApplicationContext(), "Current week selected",Toast.LENGTH_SHORT).show();
                        break;
                    case NEWGOAL_FRAGMENT_POSITION:
                        operatingWeek = new DateOperations(getApplicationContext()).getWeeksTillDate(new Date())+1;
                        editor.putInt("operating_week", operatingWeek);
                        editor.commit();
                        Toast.makeText(getApplicationContext(), "Next Goal selected",Toast.LENGTH_SHORT).show();
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                Log.i("Page scroller", "page swiped");
            }
        });

        setupViewPages(mViewPager);

}

    public void setupViewPages(ViewPager viewPager){
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new HomeFragment(), "Current Goal");
        adapter.addFragment(new NextGoalFragment(), "Next Goal");
        viewPager.setAdapter(adapter);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {
        Toast.makeText(this, "Success", Toast.LENGTH_SHORT).show();
    }

    class ViewPagerAdapter extends FragmentPagerAdapter{

        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }

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



    @Override
    protected void onResumeFragments() {
        super.onResumeFragments();
        if(!sessionManager.checkLogin())
        {
            return;
        }

    }



    //Do not delete this..... Logout of fragments still point here
    public void activityEntryButtonClick(View view)
    {
        sessionManager.logoutUser();
    }

}
