package com.ph.fragments;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.ph.Activities.MainActivity;
import com.ph.R;
import com.ph.Utils.AlertDialogManager;
import com.ph.Utils.DateOperations;
import com.ph.model.DBOperations;
import com.ph.model.UserGoal;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import butterknife.Bind;
import butterknife.ButterKnife;


/**
 * Created by Anand on 1/20/2016.
 */

public class NutritionEntryMainFragment extends Fragment implements View.OnClickListener{

    private static final int TAKE_PICTURE = 1;
    private static final int NEXT_INTENT = 2;
    public static final int MY_PERMISSION_WRITE_STORAGE = 3;
    @Bind(R.id.goalDetailsText)
    EditText mGoalDetails;
    DBOperations dbOperations;
    @Bind(R.id.nutrition_entry_main_tv_goal_text)
    TextView mCurrentGoalTv;
    @Bind(R.id.nutrition_entry_main_rg_count_towards_goal)
    RadioGroup mGoalRadioGroup;
    @Bind(R.id.nutrition_entry_main_btn_next)
    Button mNext;
    @Bind(R.id.nutrition_entry_main_btn_camera)
    ImageButton mCamera;
    @Bind(R.id.nutrition_entry_create_iv_image)
    ImageView mCameraImage;
    private DatePickerDialog.OnDateSetListener datePicker;
    private Calendar calendar;
    private EditText mNutritionEntryDate;
    private DateOperations mDateOperations;
    private String mNutritionType;
    private String mSqlDateFormatString;
    private String mImagePath = "";
    private Toolbar toolbar;
    private android.net.Uri imageUri = null;
    BitmapWorkerTask workerDisplayTask;

    public static NutritionEntryMainFragment newInstance(String param1, String param2) {
        NutritionEntryMainFragment fragment = new NutritionEntryMainFragment();
        Bundle args = new Bundle();
        args.putString("NUTRITION_TYPE", param1);
        args.putString("DATE", param2);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mNutritionType = getArguments().getString("NUTRITION_TYPE");
            mSqlDateFormatString = getArguments().getString("DATE");
        }


    }

    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity) getActivity()).setDrawerState(false);
        ((MainActivity) getActivity()).updateToolbar(mNutritionType, R.color.nutrition_entry_app_bar, R.color.white);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_nutrition_entry_main, container, false);
        ButterKnife.bind(this, view);
        dbOperations = new DBOperations(getContext());
        Typeface typeFace=Typeface.createFromAsset(getActivity().getAssets(),"fonts/Eurostile.ttf");
        mCurrentGoalTv.setTypeface(typeFace);
        mGoalDetails.setTypeface(typeFace);

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
                NutritionEntryCreateFragment fragment = NutritionEntryCreateFragment.newInstance(nutritionDetailsText, mImagePath, mSqlDateFormatString, goalCount, mNutritionType);

                ((MainActivity) getActivity()).setFragment(fragment, false);

            }
        });
        mCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickCamera(v);
            }
        });
        return view;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if(workerDisplayTask!=null)
            workerDisplayTask.cancel(true);
    }

    public void onClickCamera(View view) {
        checkForPermissions();
    }



    public void checkForPermissions() {
        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            // Should we show an explanation?

            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                // Show an expanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                Snackbar.make(getView(), "You must provide storage permissions in order to take a picture..", Snackbar.LENGTH_INDEFINITE)
                        .setActionTextColor(getResources().getColor(R.color.white))
                        .setAction("OK", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                        MY_PERMISSION_WRITE_STORAGE);
                            }
                        })
                        .show();

            } else {
                // No explanation needed, we can request the permission.
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        MY_PERMISSION_WRITE_STORAGE);
                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        } else
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
        String timestamp = dateFormat.format(new java.util.Date());
        String filename = user_id + "_" + timestamp + ".jpg";
        File photo = new File(Environment.getExternalStorageDirectory(), filename);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photo));
        intent.putExtra("name", filename);
        imageUri = Uri.fromFile(photo);
        startActivityForResult(intent, TAKE_PICTURE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case TAKE_PICTURE:
                if (resultCode == android.app.Activity.RESULT_OK) {
                    if (imageUri != null) {
                        workerDisplayTask = new BitmapWorkerTask(mCameraImage, imageUri.getPath());
                        workerDisplayTask.execute(200, 200);
                    } else {
                        AlertDialogManager dialogManager = new AlertDialogManager();
                        dialogManager.showAlertDialog(getContext(), "Permissions", "Unable to store the picture.");
                    }
                    break;
                }
        }
    }



    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.nutrition_entry_main_btn_next:
                String nutritionDetailsText = mGoalDetails.getText().toString();
                if (imageUri != null) {
                    mImagePath = imageUri.toString();
                }
                int goalCount = getRadioButtonClick(getView());
                NutritionEntryCreateFragment fragment = NutritionEntryCreateFragment.newInstance(nutritionDetailsText, mImagePath, mSqlDateFormatString, goalCount, mNutritionType);
                ((MainActivity) getActivity()).setFragment(fragment, false);
                break;
            case R.id.nutrition_entry_main_btn_camera:
                onClickCamera(v);
                break;
        }
    }

    class BitmapWorkerTask extends AsyncTask<Integer, Void, Bitmap> {
        private final WeakReference<ImageView> imageViewReference;
        private int length = 0;
        private int width = 0;
        private String path;
        private boolean running = true;

        public BitmapWorkerTask(ImageView imageView, String path) {
            // Use a WeakReference to ensure the ImageView can be garbage collected
            imageViewReference = new WeakReference<ImageView>(imageView);
            this.path = path;
        }

        // Decode image in background.
        @Override
        protected Bitmap doInBackground(Integer... params) {
            while (running) {
                length = params[0];
                width = params[1];
                return decodeSampledBitmapFromFile(path, length, width);
            }
            return null;
        }
        // Once complete, see if ImageView is still around and set bitmap.
        @Override
        protected void onPostExecute(Bitmap bitmap) {
            if (imageViewReference != null) {
                final ImageView imageView = imageViewReference.get();
                if (imageView != null) {
                    imageView.setImageBitmap(bitmap);
                    writeBitmapToFile(imageUri.getPath(), bitmap);
                }
            }
        }

        @Override
        protected void onCancelled() {
            running =  false;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSION_WRITE_STORAGE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    takePhoto();

                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Snackbar.make(getView(), "You cannot access the camera until you provide the permission.", Snackbar.LENGTH_LONG)
                            .setActionTextColor(getResources().getColor(R.color.white))
                            .setAction("OK", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                            MY_PERMISSION_WRITE_STORAGE);
                                }
                            })
                            .show();
                }
                return;
            }
            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    public void writeBitmapToFile(String path, Bitmap bitmap) {
        try {
            OutputStream stream = new FileOutputStream(path);
            Log.i("Bitmap", "Before writing file to bitmap");
            bitmap.compress(Bitmap.CompressFormat.JPEG, 50, stream);
        } catch (FileNotFoundException e) {
            AlertDialogManager dialogManager = new AlertDialogManager();
            dialogManager.showAlertDialog(getContext(), "Permissions", "Please provide storage permissions in settings and try again.");
        }

    }

    public static Bitmap decodeSampledBitmapFromFile(String path, int reqWidth, int reqHeight) {
        Bitmap b = BitmapFactory.decodeFile(path);
        return  Bitmap.createScaledBitmap(b, reqWidth, reqHeight, false);
    }

    public int getRadioButtonClick(View v) {
        int selectedIndex = mGoalRadioGroup.getCheckedRadioButtonId();
        RadioButton rb = (RadioButton) v.findViewById(selectedIndex);
        int count = Integer.parseInt(rb.getText().toString());
        return count;
    }

}
