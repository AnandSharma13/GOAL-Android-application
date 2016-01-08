package com.ph;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.ph.model.Activity;
import com.ph.model.ActivityEntry;
import com.ph.model.DBOperations;
import com.ph.model.User;
import com.ph.model.UserGoal;
import com.ph.model.UserSteps;
import com.ph.net.SyncUtils;
import com.ph.view.ImageHandler;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class TempMain extends AppCompatActivity {

    private static final int TAKE_PICTURE = 1;
    private android.net.Uri imageUri;
    String imageBase64String;



    public final ArrayList<String> tablesList = new ArrayList<String>() {{
        //add(User.tableName);
        add(UserGoal.tableName);
        add(Activity.tableName);
        add(UserSteps.tableName);

        add(ActivityEntry.tableName);
        //add(NutritionEntry.tableName);
    }};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_temp_main);
        Button insertButton = (Button) findViewById(R.id.btnInsert);
        Button newGoalButton = (Button) findViewById(R.id.btnNewGoal);
        final Button activityButton = (Button) findViewById(R.id.activity_button);
        Button userStepsButton = (Button) findViewById(R.id.user_steps_button);
        final Button activityEntryButton = (Button) findViewById(R.id.activity_entry_button);
        Button nutritionButton = (Button) findViewById(R.id.nutrition_entry_button);


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

//        newGoalButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                DBOperations ut = new DBOperations(getApplicationContext());
//                UserGoal newGoal = new UserGoal(0, "goal", "Dec-28", "Jan-5", 5, "50 gm protein");
//                long id = ut.insertRow(newGoal);
//                Log.i("Goal button", String.valueOf(id));
//                Bundle settingsBundle = new Bundle();
//                settingsBundle.putString("Type", "ClientSync");
//
//                settingsBundle.putInt("ListSize", tablesList.size());
//                for (int i = 0; i < tablesList.size(); i++) {
//                    settingsBundle.putString("Table " + i, tablesList.get(i));
//                }
//                SyncUtils.TriggerRefresh(settingsBundle);
//                Snackbar.make(v, "Goal button pressed ", Snackbar.LENGTH_LONG).setAction("Action", null).show();
//            }
//        });

        activityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DBOperations ut = new DBOperations(getApplicationContext());
                Activity activity = new Activity(0, 1, "Sample", "U", 5, new Date().toString());
                long id = ut.insertRow(activity);
                Log.i("Activity button", String.valueOf(id));
                Bundle settingsBundle = new Bundle();
                settingsBundle.putString("Type", "ClientSync");

                settingsBundle.putInt("ListSize", tablesList.size());
                for (int i = 0; i < tablesList.size(); i++) {
                    settingsBundle.putString("Table " + i, tablesList.get(i));
                }
                SyncUtils.TriggerRefresh(settingsBundle);
                Snackbar.make(v, "Activity button pressed ", Snackbar.LENGTH_LONG).setAction("Action", null).show();

            }
        });

        userStepsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DBOperations ut = new DBOperations(getApplicationContext());
                UserSteps userSteps = new UserSteps();

                userSteps.setUser_id(1);
                userSteps.setSteps_count(250);

                ut.insertRow(userSteps);
                Bundle settingsBundle = new Bundle();
                settingsBundle.putString("Type", "ClientSync");

                settingsBundle.putInt("ListSize", tablesList.size());
                for (int i = 0; i < tablesList.size(); i++) {
                    settingsBundle.putString("Table " + i, tablesList.get(i));
                }
                SyncUtils.TriggerRefresh(settingsBundle);
                Snackbar.make(v, "User Steps button pressed ", Snackbar.LENGTH_LONG).setAction("Action", null).show();

            }
        });

        activityEntryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                takePhoto();

            }
        });
        nutritionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }



    public void addActivityEntrytoDB() throws IOException, URISyntaxException {

        DBOperations ut = new DBOperations(getApplicationContext());

        ActivityEntry activityEntry = new ActivityEntry();

        activityEntry.setGoal_id(1);
        activityEntry.setActivity_id(1);
        activityEntry.setRpe(2);
        activityEntry.setActivity_length("some_length");
        activityEntry.setCount_towards_goal(3);
        activityEntry.setNotes("some notes");

        activityEntry.setImage(imageUri.toString());

        ut.insertRow(activityEntry);


        Bundle settingsBundle = new Bundle();
        settingsBundle.putString("Type", "ClientSync");

        settingsBundle.putInt("ListSize", tablesList.size());
        for (int i = 0; i < tablesList.size(); i++) {
            settingsBundle.putString("Table " + i, tablesList.get(i));
        }
        SyncUtils.TriggerRefresh(settingsBundle);
        //Snackbar.make(v, "Activity Entry button pressed ", Snackbar.LENGTH_LONG).setAction("Action", null).show();



    }
    public void takePhoto(View view) {
        takePhoto();
    }

    public void takePhoto()
    {
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");

        /*
        Proposed logic for creating a unique file_name:

        FileName = user's ID + "_" + current timestamp.
         */
        String user_id = "1"; //get user's actual user_id here.
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddhhmm");

        String timestamp = dateFormat.format(new Date());
        String filename = user_id+"_"+timestamp;

        File photo = new File(Environment.getExternalStorageDirectory(),  filename);

        intent.putExtra(MediaStore.EXTRA_OUTPUT,
                Uri.fromFile(photo));
        imageUri = Uri.fromFile(photo);
        startActivityForResult(intent, TAKE_PICTURE);
    }

    //  file:///storage/emulated/0/Picture.jpg
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case TAKE_PICTURE:
                if (resultCode == android.app.Activity.RESULT_OK) {
                    Uri selectedImage = imageUri;
                    Log.i("Image", selectedImage.toString());

                    try {
                        addActivityEntrytoDB();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (URISyntaxException e) {
                        e.printStackTrace();
                    }

                }
        }
    }

    public byte[] getImageByteArray() throws URISyntaxException, IOException {
        java.net.URI uri = new java.net.URI(imageUri.toString());
        byte[] imageByteArray = ImageHandler.getImageByteArray(uri.toURL());
        return imageByteArray;
    }


    public void getBase64String(View view) throws URISyntaxException, IOException {
        java.net.URI uri = new java.net.URI(imageUri.toString());
        byte[] imageByteArray = ImageHandler.getImageByteArray(uri.toURL());
        imageBase64String = Base64.encodeToString(imageByteArray, Base64.DEFAULT);
        byte [] byteArray = Base64.decode(imageBase64String, Base64.DEFAULT);
        Snackbar.make(view, "value saved in imageBase64String variable", Snackbar.LENGTH_LONG).setAction("Action", null).show();
        produceImage(imageByteArray);

    }



    //To test the correctness of byte array
    //Producing image from byte array
    public void produceImage(byte[] imageByteArray){
        ImageView imageView = (ImageView) findViewById(R.id.imageView);

        Bitmap bitmap = BitmapFactory.decodeByteArray(imageByteArray, 0, imageByteArray.length);
        imageView.setImageBitmap(Bitmap.createScaledBitmap(bitmap, 200, 200, false));

    }



}




