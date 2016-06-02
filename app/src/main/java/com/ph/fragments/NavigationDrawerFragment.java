package com.ph.fragments;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ph.R;

import java.util.ArrayList;
import java.util.List;


public class NavigationDrawerFragment extends Fragment {


    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;
    private RecyclerView mRecyclerView;

    private boolean mUserLearnedDrawer;
    private boolean mFromSavedInstanceState;
    private View containerView;
    private DrawerAdapter adapter;

    private TextView userNameView;
    private TextView userIdView;

    public NavigationDrawerFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View drawerLayout = inflater.inflate(R.layout.fragment_navigation_drawer, container, false);
        userNameView = (TextView) drawerLayout.findViewById(R.id.navUserName);
        userIdView = (TextView) drawerLayout.findViewById(R.id.navUserId);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        userNameView.setText(sharedPreferences.getString("first_name","GOAL User")+" "+sharedPreferences.getString("last_name",""));
        userIdView.setText("ID: "+sharedPreferences.getString("user_id",""));

        mRecyclerView = (RecyclerView) drawerLayout.findViewById(R.id.drawerList);
        adapter = new DrawerAdapter(getActivity(),getData());
        mRecyclerView.setAdapter(adapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        return drawerLayout;
    }

    public List<DrawerListData> getData() {
        List<DrawerListData> data = new ArrayList<>();
        int[] images = {R.drawable.ic_launcher, R.drawable.ic_launcher,R.drawable.ic_launcher,R.drawable.ic_launcher,R.drawable.ic_launcher};
        String[] titles = {"Messages", "History", "Progress", "Reward","Settings"};
        for (int i = 0; i < images.length; i++) {
            DrawerListData itemData = new DrawerListData();
            itemData.setTitle(titles[i]);
            itemData.setListId(images[i]);
            data.add(itemData);
        }
        return data;
    }
    public void setUp(DrawerLayout drawerlayout, Toolbar toolbar) {
        mDrawerLayout = drawerlayout;
        mDrawerToggle = new ActionBarDrawerToggle(getActivity(), drawerlayout, toolbar, R.string.drawer_open,R.string.drawer_close){

            @Override
            public void onDrawerOpened(View drawerView) {
                getActivity().invalidateOptionsMenu();
                super.onDrawerOpened(drawerView);
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                getActivity().invalidateOptionsMenu();
                super.onDrawerClosed(drawerView);
            }
        };
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        mDrawerLayout.post(new Runnable() {
            @Override
            public void run() {
                Log.i("NavigationDrawerLayout","Sync state called");
                mDrawerToggle.syncState();
            }
        });
    }
}
