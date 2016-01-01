package com.ph;

import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.ph.model.ActivityEntry;
import com.ph.model.DBOperations;
import com.ph.model.NutritionEntry;
import com.ph.model.User;
import com.ph.model.UserGoal;
import com.ph.net.SyncUtils;

import java.util.ArrayList;

public class TempMain extends AppCompatActivity {

    public final ArrayList<String> tablesList = new ArrayList<String>() {{
        add(User.tableName);
        add(UserGoal.tableName);
        add(ActivityEntry.tableName);
        add(NutritionEntry.tableName);
    }};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_temp_main);
        Button insertButton = (Button) findViewById(R.id.btnInsert);
        Button newGoalButton = (Button) findViewById(R.id.btnNewGoal);


        insertButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DBOperations ut = new DBOperations(getApplicationContext());
                User user = new User("john", "smith", "U", 24, "8121231234", "M", "MOTO G", "Goal", 1);
                long ID = ut.insertRow(user);
                Log.i("OnclickInsert", String.valueOf(ID));
                Bundle settingsBundle = new Bundle();
                // settingsBundle.putBoolean(
                //       ContentResolver.SYNC_EXTRAS_MANUAL, true);
                //settingsBundle.putBoolean(
                //      ContentResolver.SYNC_EXTRAS_EXPEDITED, true);
                settingsBundle.putString("Type", "ClientSync");

                settingsBundle.putInt("ListSize", tablesList.size());
                for (int i = 0; i < tablesList.size(); i++) {
                    settingsBundle.putString("Table " + i, tablesList.get(i));
                }

                SyncUtils.TriggerRefresh(settingsBundle);
                Snackbar.make(v, "New Row inserted with an ID " + String.valueOf(ID) + "", Snackbar.LENGTH_LONG).setAction("Action", null).show();
            }
        });

        newGoalButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DBOperations ut = new DBOperations(getApplicationContext());
                UserGoal newGoal = new UserGoal(0, "goal", "Dec-28", "Jan-5", 5, "50 gm protein");
                long id = ut.insertRow(newGoal);
                Log.i("Goal button", String.valueOf(id));
                Bundle settingsBundle = new Bundle();
                settingsBundle.putString("Type", "ClientSync");

                settingsBundle.putInt("ListSize", tablesList.size());
                for (int i = 0; i < tablesList.size(); i++) {
                    settingsBundle.putString("Table " + i, tablesList.get(i));
                }
                SyncUtils.TriggerRefresh(settingsBundle);
                Snackbar.make(v, "Goal button pressed ", Snackbar.LENGTH_LONG).setAction("Action", null).show();
            }
        });
    }




}




