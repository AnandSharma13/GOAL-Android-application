package com.ph.fragments;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.ph.MainActivity;
import com.ph.R;
import com.ph.Utils.DateOperations;
import com.ph.model.DBOperations;
import com.ph.model.UserGoal;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import butterknife.Bind;
import butterknife.ButterKnife;


/**
 * Created by Anand on 1/20/2016.
 */

public class NutritionEntryMainFragment extends Fragment {

    @Bind(R.id.goalDetailsText)
    EditText mGoalDetails;
    private DatePickerDialog.OnDateSetListener datePicker;
    private Calendar calendar;
    private EditText mNutritionEntryDate;
    private DateOperations mDateOperations;
    private String mNutritionType;
    private String mSqlDateFormatString;
    private String mImagePath = "";
    DBOperations dbOperations;
    @Bind(R.id.nutrition_entry_main_tv_goal_text)
    TextView mCurrentGoalTv;
    @Bind(R.id.nutrition_entry_main_rg_count_towards_goal)
    RadioGroup mGoalRadioGroup;
    private Toolbar toolbar;
    @Bind(R.id.nutrition_entry_main_btn_next)
    Button mNext;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mNutritionType = getArguments().getString("NUTRITION_TYPE");
            mSqlDateFormatString = getArguments().getString("DATE");
        }


    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_nutrition_entry_main, container, false);
        ButterKnife.bind(this, view);

        dbOperations = new DBOperations(getContext());

        UserGoal nutritionGoal = dbOperations.getCurrentGoalInfo("Nutrition");
        UserGoal activityGoal = dbOperations.getCurrentGoalInfo("Activity");
        mCurrentGoalTv.setText(nutritionGoal.getText() + "\n" + activityGoal.getText());

        mNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nutritionDetailsText = mGoalDetails.getText().toString();
                if (imageUri != null) {
                    mImagePath = imageUri.toString();
                }
                int goalCount = getRadioButtonClick(getView());
//        Intent intent = new Intent(getContext(), NutritionEntryCreateFragment.class);
//        intent.putExtra("Date", mSqlDateFormatString);
//        intent.putExtra("NutritionType", mNutritionType);
//        intent.putExtra("GoalCount", goalCount);
//        intent.putExtra("nutritionDetailsText", nutritionDetailsText);
//        intent.putExtra("imagePath", mImagePath);
//        startActivityForResult(intent, NEXT_INTENT);
                NutritionEntryCreateFragment fragment = NutritionEntryCreateFragment.newInstance(nutritionDetailsText, mImagePath, mSqlDateFormatString, goalCount, mNutritionType);

                (( MainActivity)getActivity()).setFragment(fragment, false);

            }
        });


        return view;
    }


    public static NutritionEntryMainFragment newInstance(String param1, String param2)     {
        NutritionEntryMainFragment fragment = new NutritionEntryMainFragment();
        Bundle args = new Bundle();
        args.putString("NUTRITION_TYPE", param1);
        args.putString("DATE", param2);
        fragment.setArguments(args);
        return fragment;
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
                    getActivity().finish();
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
          int goalCount = getRadioButtonClick(view);
//        Intent intent = new Intent(getContext(), NutritionEntryCreateFragment.class);
//        intent.putExtra("Date", mSqlDateFormatString);
//        intent.putExtra("NutritionType", mNutritionType);
//        intent.putExtra("GoalCount", goalCount);
//        intent.putExtra("nutritionDetailsText", nutritionDetailsText);
//        intent.putExtra("imagePath", mImagePath);
//        startActivityForResult(intent, NEXT_INTENT);
        NutritionEntryCreateFragment fragment = NutritionEntryCreateFragment.newInstance(nutritionDetailsText, mImagePath, mSqlDateFormatString, goalCount, mNutritionType);


    }

    public int getRadioButtonClick(View v) {
        int selectedIndex = mGoalRadioGroup.getCheckedRadioButtonId();
        RadioButton rb = (RadioButton) v.findViewById(selectedIndex);
        int count = Integer.parseInt(rb.getText().toString());
        return count;
    }


}
