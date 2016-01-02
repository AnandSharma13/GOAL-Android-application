package com.ph.model;

/**
 * Created by Anand on 12/27/2015.
 */
public class Activity {


    public final static String tableName = "activity";
    public static String column_activityID = "activity_id";
    public static String column_userID = "user_id";
    public static String column_name = "name";
    public static String column_type = "type";
    public static String column_hitCount = "hit_count";
    public static String column_lastUsed = "last_used";
    public static String column_timestamp = "timestamp";
    public static String column_isSync = "is_sync";
    private int activity_id;
    private int user_id;
    private String name;
    private String type;
    private int hit_count;
    private String last_used;
    private String timestamp;
    private int is_sync;


    public Activity(int activity_id, int user_id, String name, String type, int hit_count, String last_used) {
        this.activity_id = activity_id;
        this.user_id = user_id;
        this.name = name;
        this.type = type;
        this.hit_count = hit_count;
        this.last_used = last_used;
    }

    public Activity() {

    }

    public int getActivity_id() {
        return activity_id;
    }

    public void setActivity_id(int activity_id) {
        this.activity_id = activity_id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
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

    public int getHit_count() {
        return hit_count;
    }

    public void setHit_count(int hit_count) {
        this.hit_count = hit_count;
    }

    public String getLast_used() {
        return last_used;
    }

    public void setLast_used(String last_used) {
        this.last_used = last_used;
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
