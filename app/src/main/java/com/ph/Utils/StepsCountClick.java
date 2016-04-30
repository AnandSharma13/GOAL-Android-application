package com.ph.Utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.ph.R;
import com.ph.model.DBOperations;
import com.ph.model.UserSteps;
import com.ph.net.SyncUtils;

/**
 * Created by Anup on 2/17/2016 .
 */
public class StepsCountClick implements View.OnClickListener {

    private DBOperations mDbOperations;
    private Context context;
   // private TextView mStepsCount;
    private DialogInterface.OnDismissListener dismissListener;

    public StepsCountClick(Context context, DialogInterface.OnDismissListener dismissListener)
    {
        this.context = context;
        mDbOperations = new DBOperations(context);
      //  this.mStepsCount = mStepsCount;
        this.dismissListener = dismissListener;
    }


    @Override
    public void onClick(View v) {
        LayoutInflater li = LayoutInflater.from(context);
        View dialogView = li.inflate(R.layout.user_steps_input, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        builder.setView(dialogView);

        final EditText userStepsInput = (EditText) dialogView.findViewById(R.id.user_steps_input);

        builder
                .setCancelable(false)
                .setPositiveButton("Save",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {

                                int steps = Integer.parseInt(userStepsInput.getText().toString());

                                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
                                int user_id = Integer.parseInt(sharedPreferences.getString("user_id", "-1"));

                                UserSteps userSteps = new UserSteps();

                                userSteps.setSteps_count(steps);
                                userSteps.setUser_id(user_id);

                                mDbOperations.insertRow(userSteps);

                                Bundle settingsBundle = new Bundle();
                                settingsBundle.putString("Type", "ClientSync");

                                settingsBundle.putInt("ListSize", 1);

                                settingsBundle.putString("Table " + 0, UserSteps.tableName);

                                SyncUtils.TriggerRefresh(settingsBundle);

                               // mStepsCount.setText(String.valueOf(mDbOperations.getStepsCountForToday()));

                                Toast.makeText(context, "Successfully saved the steps count", Toast.LENGTH_SHORT).show();
                            }
                        })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
        // create alert dialog
        AlertDialog alertDialog = builder.create();

        alertDialog.setOnDismissListener(dismissListener);
        // show it
        alertDialog.show();

        final Button positiveButton = alertDialog.getButton(DialogInterface.BUTTON_POSITIVE);

        positiveButton.setEnabled(false);

        userStepsInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String val = s.toString();

                if (val.equals(""))
                    positiveButton.setEnabled(false);
                else
                    positiveButton.setEnabled(true);

            }
        });
    }


}
