package com.ph.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.ph.R;

public class FoodDetail extends AppCompatActivity {

    EditText mGoalDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_detail);

        mGoalDetails = (EditText) findViewById(R.id.goalDetailsText);
    }


    public void onClickNext(View view)
    {
        Bundle bundle =  new Bundle();
        String goalText = mGoalDetails.getText().toString();
        bundle.putString("goalText", goalText);
        Intent intent = new Intent(this, RecordFood.class);
        intent.putExtras(bundle);
        startActivity(intent);

    }

}
