package com.ph;

import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
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
import android.widget.TextView;
import android.widget.Toast;

import com.ph.Activities.SettingsActivity;
import com.ph.Fragments.ActivityHistoryDetails;
import com.ph.Fragments.ActivityHistoryFragment;
import com.ph.Fragments.DrawerAdapter;
import com.ph.Fragments.GoalHistoryDetails;
import com.ph.Fragments.GoalHistoryFragment;
import com.ph.Fragments.HistoryMainFragment;
import com.ph.Fragments.HomeFragment;
import com.ph.Fragments.NavigationDrawerFragment;
import com.ph.Fragments.NewGoalFragment;
import com.ph.Fragments.NextGoalFragment;
import com.ph.Fragments.NutritionHistoryDetails;
import com.ph.Fragments.NutritionHistoryFragment;
import com.ph.Fragments.ProgressActivityDetails;
import com.ph.Fragments.ProgressActivityFragment;
import com.ph.Fragments.ProgressMainFragment;
import com.ph.Fragments.ProgressNutritionDetails;
import com.ph.Fragments.ProgressNutritionFragment;
import com.ph.Fragments.ProgressStepsFragment;
import com.ph.Fragments.RewardsFragment;
import com.ph.Fragments.StepsDay;
import com.ph.Fragments.StepsWeek;
import com.ph.net.SessionManager;
import com.ph.net.SyncUtils;


public class MainActivity extends AppCompatActivity implements SettingsActivity.OnFragmentInteractionListener, NewGoalFragment.OnFragmentInteractionListener,
        RewardsFragment.OnFragmentInteractionListener, StepsWeek.OnFragmentInteractionListener, StepsDay.OnFragmentInteractionListener, ProgressStepsFragment.OnFragmentInteractionListener, ProgressNutritionDetails.OnFragmentInteractionListener, ProgressMainFragment.OnFragmentInteractionListener, ProgressActivityDetails.OnFragmentInteractionListener, ProgressNutritionFragment.OnFragmentInteractionListener, ProgressActivityFragment.OnFragmentInteractionListener, NextGoalFragment.OnFragmentInteractionListener, HistoryMainFragment.OnFragmentInteractionListener, ActivityHistoryFragment.OnFragmentInteractionListener, NutritionHistoryFragment.OnFragmentInteractionListener, GoalHistoryFragment.OnFragmentInteractionListener, ActivityHistoryDetails.OnFragmentInteractionListener, NutritionHistoryDetails.OnFragmentInteractionListener, GoalHistoryDetails.OnFragmentInteractionListener {


    private SessionManager sessionManager;
    private RecyclerView mDrawerRecylerView;
    private ActionBarDrawerToggle mDrawerToggle;
    private Toolbar mToolbar;
    DrawerLayout mDrawerLayout;
    private TextView mToolbarText;


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
        setSupportActionBar(mToolbar);
        updateToolbar("GOAL", R.color.white, R.color.black);


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

        setmDrawerToggle(new ActionBarDrawerToggle(
                this, mDrawerLayout, mToolbar,
                R.string.drawer_open, R.string.drawer_close) {
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }
        });


        mDrawerLayout.setDrawerListener(getmDrawerToggle());
        getmDrawerToggle().syncState();

        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (getSupportFragmentManager().getBackStackEntryCount() > 1 && !getmDrawerToggle().isDrawerIndicatorEnabled()) {
                    onBackPressed();
                } else if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
                    mDrawerLayout.closeDrawer(Gravity.LEFT);

                } else {
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
                            setFragment(new SettingsActivity(), true);
                            break;
                        case "History":
                            setFragment(new HistoryMainFragment(), true);
                            break;

                        case "Reward":
                            setFragment(new RewardsFragment(), true);
                            break;
                        case "Progress":
                            setFragment(new ProgressMainFragment(), true);
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

        getSupportFragmentManager().addOnBackStackChangedListener(getListener());

        setFragment(new HomeFragment(), false);

    }


    private FragmentManager.OnBackStackChangedListener getListener() {
        FragmentManager.OnBackStackChangedListener result = new FragmentManager.OnBackStackChangedListener() {
            public void onBackStackChanged() {
                FragmentManager manager = getSupportFragmentManager();
                if (manager != null) {
                    int backStackEntryCount = manager.getBackStackEntryCount();
                    if (backStackEntryCount == 0) {
                        finish();
                    }
                    Fragment fragment = manager.getFragments()
                            .get(backStackEntryCount);
                    fragment.onResume();
                }
            }
        };
        return result;
    }


    public void setFragment(Fragment fragment, boolean isNavigationDrawerItem) {
        String fragmentName = fragment.getClass().getName();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
        if (isNavigationDrawerItem) {
            while (fragmentManager.getBackStackEntryCount() != 1)
                fragmentManager.popBackStackImmediate();
            fragmentTransaction.add(R.id.activity_main_frame_layout, fragment).addToBackStack(fragmentName);
        } else
            fragmentTransaction.add(R.id.activity_main_frame_layout, fragment).addToBackStack(fragmentName);
        fragmentTransaction.commit();
    }


    @Override
    public void onFragmentInteraction(Uri uri) {
        Toast.makeText(this, "Success", Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 2)
            getSupportFragmentManager().popBackStack();
        else if (getSupportFragmentManager().getBackStackEntryCount() == 2) {
            updateToolbar("GOAL", R.color.white, R.color.black);
            getmDrawerToggle().setDrawerIndicatorEnabled(true);
            getSupportFragmentManager().popBackStack();
            setDrawerState(true);
        } else {
            super.finish();
        }
    }

    @Override
    protected void onResumeFragments() {
        super.onResumeFragments();
        sessionManager.checkLogin();

    }

    public ActionBarDrawerToggle getmDrawerToggle() {
        return mDrawerToggle;
    }

    public void setmDrawerToggle(ActionBarDrawerToggle mDrawerToggle) {
        this.mDrawerToggle = mDrawerToggle;
    }


    //TODO custom titles
    public void updateToolbar(String text, int backgroundColor, int textColor) {
        mToolbarText = (TextView) mToolbar.findViewById(R.id.app_bar_tv_title);
        mToolbarText.setText(text);
        mToolbarText.setTextColor(textColor);
        Typeface custom_font = Typeface.createFromAsset(getAssets(), "fonts/Eurostile.ttf");
        mToolbarText.setTypeface(custom_font);
        mToolbar.setBackground(new ColorDrawable(ContextCompat.getColor(this, backgroundColor)));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
    }

    public void setDrawerState(boolean isEnabled) {
        if (isEnabled) {
            mDrawerLayout.setDrawerLockMode(mDrawerLayout.LOCK_MODE_UNLOCKED);
            mDrawerToggle.setDrawerIndicatorEnabled(true);
            mDrawerToggle.syncState();
        } else {
            mDrawerLayout.setDrawerLockMode(mDrawerLayout.LOCK_MODE_LOCKED_CLOSED);
            mDrawerToggle.setDrawerIndicatorEnabled(false);
            mDrawerToggle.syncState();
        }
    }


}
