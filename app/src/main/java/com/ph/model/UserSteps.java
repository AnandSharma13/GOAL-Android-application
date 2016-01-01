package com.ph.model;

/**
 * Created by Anand on 12/27/2015.
 */
public class UserSteps {

    private int stepsID;
    private int stepsCount;
    private String userID;
    private String timeStamp;


    public static String tableName = "usersteps";
    public static String column_stepsID = "stepsId";
    public static String column_stepscount = "stepsCount";
    public static String column_userID = "userId";
    public static String column_timestamp = "timestamp";

    public static String column_type = "type";

    public UserSteps(int stepsID, int stepsCount, String userID, String timeStamp) {
        this.stepsID = stepsID;
        this.stepsCount = stepsCount;
        this.userID = userID;
        this.timeStamp = timeStamp;
    }
    public int getStepsID() {
        return stepsID;
    }

    public void setStepsID(int stepsID) {
        this.stepsID = stepsID;
    }

    public int getStepsCount() {
        return stepsCount;
    }

    public void setStepsCount(int stepsCount) {
        this.stepsCount = stepsCount;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }




}
