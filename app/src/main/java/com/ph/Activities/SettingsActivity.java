package com.ph.Activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.ph.MainActivity;
import com.ph.R;
import com.ph.Utils.AlertDialogManager;
import com.ph.net.SessionManager;

import butterknife.Bind;
import butterknife.ButterKnife;

public class SettingsActivity extends Fragment {

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

        }
    }

    public class logoutButtonDialogClickListener implements DialogInterface.OnClickListener{

        @Override
        public void onClick(DialogInterface dialog, int which) {
            new SessionManager(getContext()).logoutUser();
        }
    }
}
