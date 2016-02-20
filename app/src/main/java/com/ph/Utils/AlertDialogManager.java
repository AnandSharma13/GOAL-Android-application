package com.ph.Utils;

/**
 * Created by Anup on 1/17/2016.
 */

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.util.Log;

import com.ph.Activities.NewGoal;
import com.ph.model.DBOperations;
import com.ph.model.UserGoal;

public class AlertDialogManager {
    /**
     * Function to display simple Alert Dialog
     * @param context - application context
     * @param title - alert dialog title
     * @param message - alert message
     * */
    public void showAlertDialog(Context context, String title, String message) {

        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        builder.setMessage(message)
                .setTitle(title)
                .setNeutralButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        AlertDialog dialog = builder.create();

        dialog.show();

    }

    public void showPastGoalsDialog(final Context context,String title, final String goalType)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        DBOperations dbOperations = new DBOperations(context);

        final Cursor cursor = dbOperations.getPastGoalsCursor(goalType);

        builder.setTitle(title)
                .setSingleChoiceItems(cursor, -1, "display_text", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Log.i("OnClick", String.valueOf(which));

                        cursor.moveToFirst();
                        cursor.move(which);


                        Log.i("OnClick", "id = " + String.valueOf(cursor.getInt(0)));
                        NewGoal newGoalInstance;
                        if(context instanceof NewGoal) {
                            newGoalInstance = (NewGoal) context;
                            int count = cursor.getInt(cursor.getColumnIndex(UserGoal.column_weeklyCount));
                            String text = cursor.getString(cursor.getColumnIndex(UserGoal.column_text));
                            newGoalInstance.setValues(count,text,goalType);
                        }
                    }
                })
        .setNeutralButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        AlertDialog alertDialog = builder.create();

        alertDialog.show();

    }
}
