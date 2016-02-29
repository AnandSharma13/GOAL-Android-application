package com.ph.Activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.ph.R;
import com.ph.Utils.AlertDialogManager;
import com.ph.net.SessionManager;

/**
 * Created by Anup on 1/16/2016.
 */
public class LoginActivity extends AppCompatActivity {

    EditText firstName;
    EditText ID;
    Button Login;
    SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        firstName = (EditText) findViewById(R.id.user_first_name);
        ID = (EditText) findViewById(R.id.user_id);
        Login = (Button) findViewById(R.id.login_button);

        sessionManager = new SessionManager(this);

        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String first_name = firstName.getText().toString();
                String id = ID.getText().toString();


                if(first_name.equals("") || id =="") {
                    AlertDialogManager alert = new AlertDialogManager();
                    alert.showAlertDialog(LoginActivity.this, "Error","User name or ID is missing");
                }
                else
                    sessionManager.createLoginSession(first_name,Integer.parseInt(id));
            }
        });


    }
}
