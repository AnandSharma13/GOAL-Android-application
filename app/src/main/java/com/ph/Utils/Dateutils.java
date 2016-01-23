package com.ph.Utils;

import android.app.DatePickerDialog;
import android.content.Context;

import org.joda.time.DateTime;

import java.util.Date;

/**
 * Created by Anup on 1/10/2016.
 */
public class Dateutils {
    private Context context;

    public Dateutils(Context context) {
        this.context = context;
    }


    public DatePickerDialog setGoalPeriodWeek(DatePickerDialog datePickerDialog) {
        //Implement logic related to current week.
        DateOperations dateOperations = new DateOperations(context);

        StartEndDateObject startEndDateObject = dateOperations.getDatesForToday();


        Date startDate = startEndDateObject.startDate;

        Date currentDate = new Date();

        datePickerDialog.getDatePicker().setMaxDate(new DateTime(currentDate).getMillis());
        datePickerDialog.getDatePicker().setMinDate(new DateTime(startDate).getMillis());

        return datePickerDialog;
    }
}
