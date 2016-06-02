package com.ph;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;

import com.ph.Utils.DateOperations;
import com.ph.Utils.StartEndDateObject;
import com.ph.model.Activity;
import com.ph.model.ActivityEntry;
import com.ph.model.DBOperations;
import com.ph.model.User;
import com.ph.model.UserGoal;
import com.ph.model.UserSteps;
import com.ph.net.SyncUtils;
import com.ph.view.ImageHandler;
import com.ph.view.TestMy;

import junit.framework.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


@Deprecated
public class TempMain extends AppCompatActivity {

    private static final int TAKE_PICTURE = 1;
    private android.net.Uri imageUri;
    String imageBase64String;
    private Calendar calendar;
    private DatePickerDialog.OnDateSetListener date;
    private EditText nutriDate;
    Button newActivity;



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
        nutriDate = (EditText) findViewById(R.id.nutrition_date);
        newActivity = (Button) findViewById(R.id.new_activity_test);


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


        newActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(getApplicationContext(), TestMy.class);
                startActivity(myIntent);
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



        //Date related stuff...

        calendar = Calendar.getInstance();

         date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, monthOfYear);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
               updateLabel();
            }
        };


        nutriDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DatePickerDialog datePickerDialog = new DatePickerDialog(TempMain.this, date, calendar
                        .get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH));



                datePickerDialog.show();
            }
        });
    }


    private void updateLabel() {

        String myFormat = "MM/dd/yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        nutriDate.setText(sdf.format(calendar.getTime()));

        logWeekStats(calendar.getTime());
    }

    private void logWeekStats(Date givenDate)
    {
        DateOperations dateOperations = new DateOperations(TempMain.this);
        SharedPreferences sharedPreferences = getSharedPreferences("user_values", Context.MODE_PRIVATE);
        String progStartDate = dateOperations.getUniformDateFormat().format(dateOperations.getProgramStartDate());
        Log.d("WeekStats","Program Start Date = "+progStartDate);
        int numWeeks = dateOperations.getWeeksTillDate(givenDate);
        Log.d("WeekStats","Number of weeks for the above date= "+ String.valueOf(numWeeks));
        StartEndDateObject startEndDateObject = dateOperations.getDatesforDate(givenDate);
        String weekStart = dateOperations.getUniformDateFormat().format(startEndDateObject.startDate);
        String weekEnd = dateOperations.getUniformDateFormat().format(startEndDateObject.endDate);
        Log.d("WeekStats","Start Date = "+weekStart+" start end "+weekEnd);

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

    public void takePhoto() {
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");

        /*
        Proposed logic for creating a unique file_name:
        FileName = user's ID + "_" + current timestamp.
         */
        String user_id = "1"; //get user's actual user_id here.
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddhhmmss");

        String timestamp = dateFormat.format(new Date());
        String filename = user_id + "_" + timestamp+".jpg";

        File photo = new File(Environment.getExternalStorageDirectory(), filename);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photo));
        intent.putExtra("name", filename);

        imageUri = Uri.fromFile(photo);
        Log.i("Take Photo", imageUri.toString());
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

                    File file = new File(imageUri.getPath());

                    //Specify the pixels of compressed image here
                    Bitmap bitmap = decodeSampledBitmapFromFile(file.getAbsolutePath(), 500, 500);
                    int size = bitmap.getAllocationByteCount();
                    Log.i("Size", String.valueOf(size));
                    file.delete();
                    writeBitmapToFile(imageUri.getPath(), bitmap);
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

    public void writeBitmapToFile(String path, Bitmap bitmap) {

        try {
            OutputStream stream = new FileOutputStream(path);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 50, stream);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }

    public static Bitmap decodeSampledBitmapFromFile(String path, int reqWidth, int reqHeight) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);

        // Calculate inSampleSize, Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        int inSampleSize = 1;

        if (height > reqHeight) {
            inSampleSize = Math.round((float) height / (float) reqHeight);
        }
        int expectedWidth = width / inSampleSize;
        if (expectedWidth > reqWidth) {
            inSampleSize = Math.round((float) width / (float) reqWidth);
        }
        options.inSampleSize = inSampleSize;
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(path, options);
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
        byte[] byteArray = Base64.decode(imageBase64String, Base64.DEFAULT);
        Snackbar.make(view, "value saved in imageBase64String variable", Snackbar.LENGTH_LONG).setAction("Action", null).show();
        produceImage(imageByteArray);

    }


    //To test the correctness of byte array
    //Producing image from byte array
    public void produceImage(byte[] imageByteArray) {
        ImageView imageView = (ImageView) findViewById(R.id.imageView);

        Bitmap bitmap = BitmapFactory.decodeByteArray(imageByteArray, 0, imageByteArray.length);
        imageView.setImageBitmap(Bitmap.createScaledBitmap(bitmap, 200, 200, false));

    }


}




