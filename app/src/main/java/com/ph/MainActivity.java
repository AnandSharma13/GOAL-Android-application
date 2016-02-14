package com.ph;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.ph.Activities.FragmentProgressMain;
import com.ph.Activities.HistoryActivity;
import com.ph.Activities.SettingsActivity;
import com.ph.fragments.DrawerAdapter;
import com.ph.fragments.HomeFragment;
import com.ph.fragments.NavigationDrawerFragment;
import com.ph.fragments.NewGoalFragment;
import com.ph.fragments.NextGoalFragment;
import com.ph.fragments.ProgressActivityDetails;
import com.ph.fragments.ProgressActivityFragment;
import com.ph.fragments.ProgressNutritionDetails;
import com.ph.fragments.ProgressNutritionFragment;
import com.ph.fragments.ProgressStepsFragment;
import com.ph.fragments.RewardsFragment;
import com.ph.fragments.StepsDay;
import com.ph.fragments.StepsWeek;
import com.ph.net.SessionManager;
import com.ph.net.SyncUtils;
import com.ph.view.OnBackPressedListener;


public class MainActivity extends AppCompatActivity implements SettingsActivity.OnFragmentInteractionListener, NewGoalFragment.OnFragmentInteractionListener,
        RewardsFragment.OnFragmentInteractionListener, StepsWeek.OnFragmentInteractionListener, StepsDay.OnFragmentInteractionListener,ProgressStepsFragment.OnFragmentInteractionListener, ProgressNutritionDetails.OnFragmentInteractionListener, FragmentProgressMain.OnFragmentInteractionListener,ProgressActivityDetails.OnFragmentInteractionListener, ProgressNutritionFragment.OnFragmentInteractionListener, ProgressActivityFragment.OnFragmentInteractionListener, NextGoalFragment.OnFragmentInteractionListener {


    private SessionManager sessionManager;
    private RecyclerView mDrawerRecylerView;
    protected OnBackPressedListener mOnBackPressedListener;


    public static final int HOME_FRAGMENT_POSITION = 0;
    public static final int NEWGOAL_FRAGMENT_POSITION = 1;
    ActionBarDrawerToggle mDrawerToggle;

    private Toolbar mToolbar;


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
                R.string.drawer_open, R.string.drawer_close){
            @Override
            public void onDrawerClosed(View drawerView) {
                mDrawerToggle.setDrawerIndicatorEnabled(true);
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                mDrawerToggle.setDrawerIndicatorEnabled(false);
                super.onDrawerOpened(drawerView);
            }
        };


        mDrawerLayout.setDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();

        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
                    mDrawerToggle.setDrawerIndicatorEnabled(true);
                    mDrawerLayout.closeDrawer(Gravity.LEFT);

                } else {
                    mDrawerToggle.setDrawerIndicatorEnabled(false);
                    mDrawerLayout.openDrawer(Gravity.LEFT);
                }

            }
        });

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
                    //TODO: Refactor the names
                    switch (title) {
                        case "Logout":
                            sessionManager.logoutUser();
                            break;
                        case "Temp":
                            intent = new Intent(MainActivity.this, TempMain.class);
                            startActivity(intent);
                            break;
                        case "Settings":
                            //intent = new Intent(MainActivity.this, SettingsActivity.class);
                            //startActivity(intent);
                            setFragment(new SettingsActivity(), true);
                            break;
                        case "History":
                            intent = new Intent(MainActivity.this, HistoryActivity.class);
                            startActivity(intent);
                            break;

                        case "Reward":

                            setFragment(new RewardsFragment(), true);
                            break;
                        case "Progress":
                            setFragment(new FragmentProgressMain(), true);
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

        setFragment(new HomeFragment(), false);

    }


    protected void setFragment(Fragment fragment, boolean isNavigationDrawerItem) {
        String fragmentName = fragment.getClass().getName();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        if (isNavigationDrawerItem)
            fragmentTransaction.add(R.id.activity_main_frame_layout, fragment).addToBackStack(fragmentName);
        else
            fragmentTransaction.replace(R.id.activity_main_frame_layout, fragment);

        fragmentTransaction.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
        //TODO: work on Fragment.detach()
        fragmentTransaction.commit();
    }


    @Override
    public void onFragmentInteraction(Uri uri) {
        Toast.makeText(this, "Success", Toast.LENGTH_SHORT).show();
    }




    public void setOnBackPressedListener(OnBackPressedListener onBackPressedListener) {
        this.mOnBackPressedListener = onBackPressedListener;
    }


    @Override
    public void onBackPressed() {
        if (mOnBackPressedListener != null && getSupportFragmentManager().getBackStackEntryCount() != 0)
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
}
