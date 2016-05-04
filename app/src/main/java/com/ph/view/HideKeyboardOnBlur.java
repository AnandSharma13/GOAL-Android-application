package com.ph.view;

import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

/**
 * Created by Anup on 5/4/2016   .
 */
public class HideKeyboardOnBlur implements View.OnFocusChangeListener{

    private Context context;
    public HideKeyboardOnBlur(Context context)
    {
        this.context = context;
    }

    public void onFocusChange(View v, boolean hasFocus){

        if(!hasFocus) {

            InputMethodManager imm =  (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);

        }
    }
}
