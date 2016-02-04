package com.ph.Activities;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.ph.R;
import com.ph.net.SessionManager;

/**
 * Created by Anup on 2/3/2016 .
 */
public class SettingsActivity extends AppCompatActivity {

    private TextView programName;
    private TextView programStartDate;
    private Button logOut;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        final SessionManager sessionManager = new SessionManager(this);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        programName = (TextView) findViewById(R.id.programName);
        programStartDate = (TextView) findViewById(R.id.programStartDate);
        logOut = (Button) findViewById(R.id.logOutButton);


        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sessionManager.logoutUser();
            }
        });

        String programNameVal = sharedPreferences.getString("program","");
        String programStartDateVal = sharedPreferences.getString("program_start_date","");

        programName.setText(programNameVal);
        programStartDate.setText(programStartDateVal);



    }

}
