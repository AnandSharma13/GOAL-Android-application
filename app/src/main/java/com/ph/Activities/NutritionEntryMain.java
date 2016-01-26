package com.ph.Activities;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.ph.R;
import com.ph.Utils.AlertDialogManager;
import com.ph.Utils.DateOperations;
import com.ph.Utils.Dateutils;
import com.ph.model.DBOperations;
import com.ph.model.UserGoal;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Calendar;


/**
 * Created by Anand on 1/20/2016.
 */

public class NutritionEntryMain extends AppCompatActivity {

    private EditText mGoalDetails;
    private DatePickerDialog.OnDateSetListener datePicker;
    private Calendar calendar;
    private EditText mNutritionEntryDate;
    private DateOperations mDateOperations;
    private String mNutritionType;
    private String mSqlDateFormatString;
    private String mImagePath = "";
    DBOperations dbOperations;
    private TextView mCurrentGoalTv;
    private RadioGroup mGoalRadioGroup;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nutrition_entry_main);
        dbOperations = new DBOperations(getApplicationContext());
        mNutritionType = getIntent().getExtras().getString("NutritionType");
        mGoalRadioGroup = (RadioGroup) findViewById(R.id.nutrition_entry_main_rg_count_towards_goal);
        mSqlDateFormatString = getIntent().getExtras().getString("Date");
        mGoalDetails = (EditText) findViewById(R.id.goalDetailsText);
        mCurrentGoalTv = (TextView) findViewById(R.id.nutrition_entry_main_tv_goal_text);
        UserGoal userGoal = dbOperations.getCurrentGoalInfo("Nutrition");
        mCurrentGoalTv.setText(userGoal.getText());

    }

    public void onClickCamera(View view) {

        takePhoto();

    }


    private android.net.Uri imageUri;
    private static final int TAKE_PICTURE = 1;
    private static final int NEXT_INTENT = 2;

    public void takePhoto() {
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");

        /*
        Proposed logic for creating a unique file_name:
        FileName = user's ID + "_" + current timestamp.
         */
        String user_id = "1"; //get user's actual user_id here.
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddhhmmss");

        String timestamp = dateFormat.format(new java.util.Date());
        String filename = user_id + "_" + timestamp + ".jpg";

        File photo = new File(Environment.getExternalStorageDirectory(), filename);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photo));
        intent.putExtra("name", filename);
        imageUri = Uri.fromFile(photo);
        Log.i("Take Photo", imageUri.toString());
        startActivityForResult(intent, TAKE_PICTURE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case TAKE_PICTURE:
                if (resultCode == android.app.Activity.RESULT_OK) {
                    //   Uri selectedImage = imageUri;
                    File file = new File(imageUri.getPath());
                    //Specify the pixels of compressed image here
                    Bitmap bitmap = decodeSampledBitmapFromFile(file.getAbsolutePath(), 500, 500);
                    //Deletes the original file from memory. We don't need to store high quality image
                    file.delete();
                    writeBitmapToFile(imageUri.getPath(), bitmap);
                    break;
                }
            case NEXT_INTENT:
                if (resultCode == Activity.RESULT_OK) {
                    finish();
                }
                break;

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


    public void onClickNext(View view) {
        String nutritionDetailsText = mGoalDetails.getText().toString();
        if (imageUri != null) {
            mImagePath = imageUri.toString();
        }
        int goalCount = getRadioButtonClick();
        Intent intent = new Intent(this, NutritionEntryCreate.class);
        intent.putExtra("Date", mSqlDateFormatString);
        intent.putExtra("NutritionType", mNutritionType);
        intent.putExtra("GoalCount", goalCount);
        intent.putExtra("nutritionDetailsText", nutritionDetailsText);
        intent.putExtra("imagePath", mImagePath);
        startActivityForResult(intent, NEXT_INTENT);
    }

    public int getRadioButtonClick() {
        int selectedIndex = mGoalRadioGroup.getCheckedRadioButtonId();
        RadioButton rb = (RadioButton) findViewById(selectedIndex);
        int count = Integer.parseInt(rb.getText().toString());

        return count;
    }


}
