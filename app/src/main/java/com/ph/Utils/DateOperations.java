package com.ph.Utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import org.joda.time.DateTime;
import org.joda.time.Weeks;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class DateOperations {

    private Date programStartDate;
    private int programLength;
    private SimpleDateFormat uniformDateFormat;
    private SimpleDateFormat mysqlDateFormat;
    private Date currentDate;
    private SharedPreferences sharedPreferences;

    public DateOperations(Context context)
    {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        programLength = Integer.parseInt(sharedPreferences.getString("program_length", "-1"));
        uniformDateFormat = new SimpleDateFormat("MM'/'dd'/'yyyy");//Problem with the format
        mysqlDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            programStartDate = mysqlDateFormat.parse(sharedPreferences.getString("program_start_date", ""));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        currentDate = new Date();
    }

    public StartEndDateObject getDatesFromWeekNumber(int weeks)
    {
        DateTime startDate = new DateTime(programStartDate);

        DateTime weekStart = startDate.plusWeeks(weeks);
        DateTime weekEnd = weekStart.plusDays(6);

        StartEndDateObject startEndDate = new StartEndDateObject();

        startEndDate.startDate = weekStart.toDate();
        startEndDate.endDate = weekEnd.toDate();
        return startEndDate;
    }

    public StartEndDateObject getDatesForToday()
    {

        int weeks = getWeeksTillDate(currentDate);

        return getDatesFromWeekNumber(weeks);
    }


    public StartEndDateObject getDatesforDate(Date givenDate)
    {
        int weeks = getWeeksTillDate(givenDate);

        return getDatesFromWeekNumber(weeks);
    }

    /**
     * Gets the number of weeks elapsed from the start date of the program till the givenDate. PLEASE note that the
     * function returns the number of weeks ELAPSED not the week number [i.e add one to the result for week number].
     * @param givenDate - The date to which the number of weeks are calculated
     * @return Number of weeks as an Integer
     */

    public int getWeeksTillDate(Date givenDate)
    {
        DateTime startDate = new DateTime(programStartDate);
        DateTime endDate = new DateTime(givenDate);

        return Weeks.weeksBetween(startDate,endDate).getWeeks();

    }


    public SimpleDateFormat getUniformDateFormat() {
        return uniformDateFormat;
    }

    public Date getProgramStartDate() {

        return programStartDate;
    }

    public SimpleDateFormat getMysqlDateFormat() {
        return mysqlDateFormat;
    }

    /*public void setUniformDateFormat(SimpleDateFormat uniformDateFormat) {
        this.uniformDateFormat = uniformDateFormat;
    }*/
}

