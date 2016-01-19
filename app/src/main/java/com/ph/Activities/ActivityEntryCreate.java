package com.ph.Activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.ph.Adapters.ActivityViewAdapter;
import com.ph.R;
import com.ph.model.Activity;
import com.ph.model.DBOperations;

import java.util.List;

/**
 * Created by Anup on 1/16/2016.
 */
public class ActivityEntryCreate extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.Adapter adapter;
    private List<Activity> mData;
    private DBOperations dbOperations;
    private String activity_type;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entry_create);

        mRecyclerView = (RecyclerView) findViewById(R.id.activity_recycler_view);

        mRecyclerView.setHasFixedSize(true);

        dbOperations = new DBOperations(this);

        layoutManager = new GridLayoutManager(this,3);

        mRecyclerView.setLayoutManager(layoutManager);

        activity_type = getIntent().getExtras().getString("key");



        toolbar = (Toolbar) findViewById(R.id.app_bar);
        toolbar.setTitle(activity_type);



        mData = dbOperations.getActivities(activity_type);
        adapter = new ActivityViewAdapter(this, mData);

        mRecyclerView.setAdapter(adapter);



    }
}
