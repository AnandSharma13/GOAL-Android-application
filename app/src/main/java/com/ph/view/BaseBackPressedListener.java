package com.ph.view;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;

public class BaseBackPressedListener implements OnBackPressedListener {

    private Activity mActivity;

    public BaseBackPressedListener(Activity activity) {
        this.mActivity = activity;
    }


    @Override
    public void onBackPress() {

        ((AppCompatActivity) mActivity).getSupportFragmentManager().popBackStack();


    }


}
