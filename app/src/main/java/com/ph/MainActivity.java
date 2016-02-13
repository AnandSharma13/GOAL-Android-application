package com.ph;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

import android.widget.Toast;

import com.ph.Activities.ActivityProgressMain;
import com.ph.Activities.HistoryActivity;
import com.ph.Activities.SettingsActivity;
import com.ph.Utils.DateOperations;
import com.ph.fragments.DrawerAdapter;
import com.ph.fragments.NewGoalFragment;
import com.ph.fragments.NavigationDrawerFragment;
import com.ph.fragments.HomeFragment;
import com.ph.fragments.NextGoalFragment;
import com.ph.fragments.RewardsFragment;
import com.ph.model.ActivityEntry;
import com.ph.model.DBOperations;
import com.ph.model.NutritionEntry;
import com.ph.model.User;
import com.ph.model.UserGoal;
import com.ph.net.SessionManager;
import com.ph.net.SyncUtils;
import com.ph.view.OnBackPressedListener;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity implements HomeFragment.OnFragmentInteractionListener, NewGoalFragment.OnFragmentInteractionListener,
        RewardsFragment.OnFragmentInteractionListener, NextGoalFragment.OnFragmentInteractionListener {


    private Button newGoalButton;
    private ContentResolver mContentResolver;

    SharedPreferences sharedPreferences;
    GestureDetector mGestureDetector = null;
    View.OnTouchListener mGestureListener = null;
    private SessionManager sessionManager;
    private DateOperations dateOperations;
    private DBOperations dbOperations;
    private ViewPager mViewPager;
    private RecyclerView mDrawerRecylerView;
    protected OnBackPressedListener mOnBackPressedListener;


    // Constants
    // The authority for the sync adapter's content provider
    public static final String AUTHORITY = "com.ph";
    // An account type, in the form of a domain name
    public static final String ACCOUNT_TYPE = "example.com";
    // The account name
    public static final String ACCOUNT = "dummyaccount";
    public static final int HOME_FRAGMENT_POSITION = 0;
    public static final int NEWGOAL_FRAGMENT_POSITION = 1;
    ActionBarDrawerToggle mDrawerToggle;

    private Toolbar mToolbar;

    //This will go inside the onPerformSync bundle
    public final ArrayList<String> tablesList = new ArrayList<String>() {{
        add(User.tableName);
        add(UserGoal.tableName);
        add(ActivityEntry.tableName);
        add(NutritionEntry.tableName);
    }};


    DrawerLayout mDrawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Check LOGIN status
        sessionManager = new SessionManager(this);

        if (!sessionManager.checkLogin()) {
            return;
        }


        dateOperations = new DateOperations(this);
        dbOperations = new DBOperations(this);


        mToolbar = (Toolbar) findViewById(R.id.app_bar);
        mToolbar.setTitle("G.O.A.L");
        setSupportActionBar(mToolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayShowCustomEnabled(true);


        NavigationDrawerFragment drawerFragment = (NavigationDrawerFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
        drawerFragment.setUp((DrawerLayout) findViewById(R.id.drawer_layout), mToolbar);


        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        mDrawerRecylerView = (RecyclerView) mDrawerLayout.findViewById(R.id.drawerList);

        final GestureDetector drawerGestureDector = new GestureDetector(MainActivity.this, new GestureDetector.SimpleOnGestureListener() {

            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                return true;
            }

        });

        mDrawerToggle = new ActionBarDrawerToggle(
                this, mDrawerLayout, mToolbar,
                R.string.drawer_open, R.string.drawer_close
        );

        mDrawerLayout.setDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();

        mDrawerRecylerView.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
            @Override
            public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
                View child = rv.findChildViewUnder(e.getX(), e.getY());

                if (child != null && drawerGestureDector.onTouchEvent(e)) {
                    mDrawerLayout.closeDrawers();
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
                        case "History":
                            intent = new Intent(MainActivity.this, HistoryActivity.class);
                            startActivity(intent);
                            break;

                        case "Reward":
                            mDrawerToggle.setDrawerIndicatorEnabled(false);
                            setFragment(new RewardsFragment(), true);
                            break;
                        case "Progress":
                            intent = new Intent(MainActivity.this, ActivityProgressMain.class);
                            startActivity(intent);
                            break;
                        default:
                            Toast.makeText(MainActivity.this, "The Item Clicked is: " + title, Toast.LENGTH_SHORT).show();

                            return true;

                    }
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

    SyncUtils.CreateSyncAccount(this);
        mContentResolver = getContentResolver();



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
//        mViewPager = (ViewPager) findViewById(R.id.viewpager);
//
//        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
//            @Override
//            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//
//            }
//
//            @Override
//            public void onPageSelected(int position) {
//                Log.i("selected page\t", String.valueOf(position));
//
//                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
//                int operatingWeek;
//                SharedPreferences.Editor editor = sharedPreferences.edit();
//
//                switch (position) {
//                    case HOME_FRAGMENT_POSITION:
//                        operatingWeek = new DateOperations(getApplicationContext()).getWeeksTillDate(new Date());
//                        editor.putInt("operating_week", operatingWeek);
//                        editor.commit();
//                        Toast.makeText(getApplicationContext(), "Current week selected",Toast.LENGTH_SHORT).show();
//                        break;
//                    case NEWGOAL_FRAGMENT_POSITION:
//                        operatingWeek = new DateOperations(getApplicationContext()).getWeeksTillDate(new Date())+1;
//                        editor.putInt("operating_week", operatingWeek);
//                        editor.commit();
//                        Toast.makeText(getApplicationContext(), "Next Goal selected",Toast.LENGTH_SHORT).show();
//                        break;
//                }
//            }
//
//            @Override
//            public void onPageScrollStateChanged(int state) {
//                Log.i("Page scroller", "page swiped");
//            }
//        });
//
//        setupViewPages(mViewPager);


        setFragment(new HomeFragment() ,false);

    }

    protected void setFragment(Fragment fragment, boolean isNavigationDrawerItem) {
        String fragmentName = fragment.getClass().getName();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        if(isNavigationDrawerItem)
            fragmentTransaction.add(R.id.activity_main_frame_layout, fragment).addToBackStack(fragmentName);
        else
            fragmentTransaction.replace(R.id.activity_main_frame_layout, fragment);

        fragmentTransaction.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
        fragmentTransaction.commit();
    }


    @Override
    public void onFragmentInteraction(Uri uri) {
        Toast.makeText(this, "Success", Toast.LENGTH_SHORT).show();
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {

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

    public void setOnBackPressedListener(OnBackPressedListener onBackPressedListener){
        this.mOnBackPressedListener = onBackPressedListener;
    }

    @Override
    public void onBackPressed() {
       if(mOnBackPressedListener!=null && getSupportFragmentManager().getBackStackEntryCount()!=0)
           mOnBackPressedListener.onBackPress();
        else
            super.onBackPressed();
    }

    @Override
    protected void onResumeFragments() {
        super.onResumeFragments();
        if (!sessionManager.checkLogin()) {
            return;
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:

                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    //Do not delete this..... Logout of fragments still point here
    public void activityEntryButtonClick(View view) {
        sessionManager.logoutUser();
    }

}
