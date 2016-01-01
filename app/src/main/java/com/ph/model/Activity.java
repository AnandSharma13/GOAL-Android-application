package com.ph.model;

/**
 * Created by Anand on 12/27/2015.
 */
public class Activity {


    private int activityID;
    private int userID;
    private String name;
    private String type;
    private int hitCount;
    private String lastUsed;


    public static String tableName = "activity";
    public static String column_activityID = "activity_id";
    public static String column_userID = "user_id";
    public static String column_name = "name";
    public static String column_type = "type";
    public static String column_hitCount = "hit_count";
    public static String column_lastUsed = "last_used";


    public Activity(int activityID, int userID, String name, String type, int hitCount, String lastUsed) {
        this.activityID = activityID;
        this.userID = userID;
        this.name = name;
        this.type = type;
        this.hitCount = hitCount;
        this.lastUsed = lastUsed;
    }

    public int getActivityID() {
        return activityID;
    }

    public void setActivityID(int activityID) {
        this.activityID = activityID;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getHitCount() {
        return hitCount;
    }

    public void setHitCount(int hitCount) {
        this.hitCount = hitCount;
    }

    public String getLastUsed() {
        return lastUsed;
    }

    public void setLastUsed(String lastUsed) {
        this.lastUsed = lastUsed;
    }

}
