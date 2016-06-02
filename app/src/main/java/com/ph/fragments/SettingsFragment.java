package com.ph.fragments;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.ph.Activities.MainActivity;
import com.ph.R;
import com.ph.Utils.AlertDialogManager;
import com.ph.net.SessionManager;

import butterknife.Bind;
import butterknife.ButterKnife;

public class SettingsFragment extends Fragment {

    private static final int MAKE_CALL=1;
    @Bind(R.id.fragment_settings_tv_program_name)
    TextView programName;
    @Bind(R.id.programStartDate)
    TextView programStartDate;
    @Bind(R.id.logOutButton)
    Button logOut;
    @Bind(R.id.phoneSupport)
    ImageView support;
    @Bind(R.id.fragment_settings_program_text)
    TextView programText;
    @Bind(R.id.fragment_settings_program_start_date_text)
    TextView startDateText;
    @Bind(R.id.settings_contact_support_text)
    TextView contactSupportText;
    private OnFragmentInteractionListener mListener;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((MainActivity) getActivity()).setDrawerState(false);
        ((MainActivity) getActivity()).updateToolbar("Settings", R.color.white, R.color.black);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);
        ButterKnife.bind(this, view);
        final SessionManager sessionManager = new SessionManager(getContext());
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());

        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialogManager().showTwoButtonDialog(getContext(),"Log out user","Are you sure? All unsynced changes will be lost.","Log Out",new logoutButtonDialogClickListener());
            }
        });

        support.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialogManager().showTwoButtonDialog(getContext(),"","Call Technical Support?","Call",new callButtonClickListener());
            }
        });

        String programNameVal = sharedPreferences.getString("program", "N/A");
        String programStartDateVal = sharedPreferences.getString("program_start_date", "N/A");

        Typeface custom_font = Typeface.createFromAsset(getActivity().getAssets(), "fonts/HelveticaNeue.ttf");

        programText.setTypeface(custom_font);
        startDateText.setTypeface(custom_font);
        contactSupportText.setTypeface(custom_font);

        programName.setTypeface(custom_font);
        programStartDate.setTypeface(custom_font);

        programName.setText(programNameVal);
        programStartDate.setText(programStartDateVal);
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }

    public class callButtonClickListener implements DialogInterface.OnClickListener{

        @Override
        public void onClick(DialogInterface dialog, int which) {
            try {

                if (ContextCompat.checkSelfPermission(getActivity(),
                        Manifest.permission.CALL_PHONE)
                        != PackageManager.PERMISSION_GRANTED) {

                    // Should we show an explanation?

                    if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                            Manifest.permission.CALL_PHONE)) {

                        // Show an expanation to the user *asynchronously* -- don't block
                        // this thread waiting for the user's response! After the user
                        // sees the explanation, try again to request the permission.
                        Snackbar.make(getView(),"You must provide permissions to make a call.",Snackbar.LENGTH_INDEFINITE)
                                .setActionTextColor(getResources().getColor(R.color.white))
                                .setAction("OK", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        requestPermissions(new String[]{Manifest.permission.CALL_PHONE},
                                                MAKE_CALL);
                                    }
                                })
                                .show();

                    } else {

                        // No explanation needed, we can request the permission.
                        requestPermissions(new String[]{Manifest.permission.CALL_PHONE},
                                MAKE_CALL);
                    }
                }
                else
                {
                    makeCall();
                }
            } catch (ActivityNotFoundException activityException) {
                Log.e("Calling a Phone Number", "Call failed", activityException);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }

    private void makeCall()
    {
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:"+getResources().getString(R.string.tech_support)));
        startActivity(callIntent);
    }



    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MAKE_CALL: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    makeCall();

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Snackbar.make(getView(),"You cannot make calls from the app without the permission.",Snackbar.LENGTH_LONG)
                            .setActionTextColor(getResources().getColor(R.color.white))
                            .setAction("OK", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    requestPermissions(new String[]{Manifest.permission.CALL_PHONE},
                                            MAKE_CALL);
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


    public class logoutButtonDialogClickListener implements DialogInterface.OnClickListener{
        @Override
        public void onClick(DialogInterface dialog, int which) {
            new SessionManager(getContext()).logoutUser();
        }
    }
}
