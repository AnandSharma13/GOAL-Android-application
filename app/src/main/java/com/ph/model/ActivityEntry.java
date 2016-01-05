package com.ph.model;

import android.media.Image;

/**
 * Created by Anand on 12/27/2015.
 */
public class ActivityEntry {

    private int activity_entry_id;
    private int goal_id;
    private int activity_id;
    private String timestamp;
    private int rpe;
    private String activity_length;
    private int count_towards_goal;
    private String notes;
    private Image image;
    private int is_sync;

    public final static String tableName = "activity_entry";
    public static String column_activityEntryID = "activity_entry_id";
    public static String column_goalID = "goal_id";
    public static String column_activityID = "activity_id";
    public static String column_timestamp = "timestamp";
    public static String column_rpe = "rpe";
    public static String column_activitylength = "activity_length";
    public static String column_counttowardsgoal = "count_towards_goal";
    public static String column_notes = "notes";
    public static String column_image = "image";
    public static String column_sync = "is_sync";

    ;


    public ActivityEntry(int activity_entry_id, int goalID, int activityID, String timeStamp, int rpe, String activityLength, int countTowardsGoal, String notes, Image image) {
        this.activity_entry_id = activity_entry_id;
        this.goal_id = goalID;
        this.activity_id = activityID;
        this.timestamp = timeStamp;
        this.rpe = rpe;
        this.activity_length = activityLength;
        this.count_towards_goal = countTowardsGoal;
        this.notes = notes;
        this.image = image;
    }

    public ActivityEntry()
    {

    }

    public int getActivity_entry_id() {
        return activity_entry_id;
    }

    public void setActivity_entry_id(int activity_entry_id) {
        this.activity_entry_id = activity_entry_id;
    }

    public int getGoal_id() {
        return goal_id;
    }

    public void setGoal_id(int goal_id) {
        this.goal_id = goal_id;
    }

    public int getActivity_id() {
        return activity_id;
    }

    public void setActivity_id(int activity_id) {
        this.activity_id = activity_id;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public int getRpe() {
        return rpe;
    }

    public void setRpe(int rpe) {
        this.rpe = rpe;
    }

    public String getActivity_length() {
        return activity_length;
    }

    public void setActivity_length(String activity_length) {
        this.activity_length = activity_length;
    }

    public int getCount_towards_goal() {
        return count_towards_goal;
    }

    public void setCount_towards_goal(int count_towards_goal) {
        this.count_towards_goal = count_towards_goal;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    public int getIs_sync() {
        return is_sync;
    }

    public void setIs_sync(int is_sync) {
        this.is_sync = is_sync;
    }
}
