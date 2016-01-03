package com.ph.model;

/**
 * Created by Anand on 12/27/2015.
 */
public class UserSteps {

    public final static String tableName = "user_steps";
    public static String column_stepsID = "steps_id";
    public static String column_stepscount = "steps_count";
    public static String column_userID = "user_id";
    public static String column_timestamp = "timestamp";
    public static String column_sync = "is_sync";
    public static String column_type = "type";
    private int steps_id;
    private int steps_count;
    private int user_id;
    private String timestamp;
    private int is_sync = 0;

    public UserSteps(int steps_id, int steps_count, int user_id, String timeStamp) {
        this.steps_id = steps_id;
        this.steps_count = steps_count;
        this.user_id = user_id;
        this.timestamp = timeStamp;
    }

    public UserSteps() {

    }

    public int getSteps_id() {
        return steps_id;
    }

    public void setSteps_id(int steps_id) {
        this.steps_id = steps_id;
    }

    public int getSteps_count() {
        return steps_count;
    }

    public void setSteps_count(int steps_count) {
        this.steps_count = steps_count;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }


    public int getIs_sync() {
        return is_sync;
    }

    public void setIs_sync(int is_sync) {
        this.is_sync = is_sync;
    }
}
