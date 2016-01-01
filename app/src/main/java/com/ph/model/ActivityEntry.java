package com.ph.model;

import android.media.Image;

/**
 * Created by Anand on 12/27/2015.
 */
public class ActivityEntry {

    private int activityEntryID;
    private int goalID;
    private int activityID;
    private String timeStamp;
    private int rpe;
    private String activityLength;
    private int countTowardsGoal;
    private String notes;
    private Image image;

    public static String tableName = "activityentry";
    public static String column_activityEntryID = "activity_Entry_id";
    public static String column_goalID = "goal_id";
    public static String column_activityID = "activity_id";
    public static String column_timestamp = "timestamp";
    public static String column_rpe = "rpe";
    public static String column_activitylength = "activity_length";
    public static String column_counttowardsgoal = "count_towards_goal";
    public static String column_notes = "notes";
    public static String column_image = "image";

    ;


    public ActivityEntry(int activityEntryID, int goalID, int activityID, String timeStamp, int rpe, String activityLength, int countTowardsGoal, String notes, Image image) {
        this.activityEntryID = activityEntryID;
        this.goalID = goalID;
        this.activityID = activityID;
        this.timeStamp = timeStamp;
        this.rpe = rpe;
        this.activityLength = activityLength;
        this.countTowardsGoal = countTowardsGoal;
        this.notes = notes;
        this.image = image;
    }

    public int getActivityEntryID() {
        return activityEntryID;
    }

    public void setActivityEntryID(int activityEntryID) {
        this.activityEntryID = activityEntryID;
    }

    public int getGoalID() {
        return goalID;
    }

    public void setGoalID(int goalID) {
        this.goalID = goalID;
    }

    public int getActivityID() {
        return activityID;
    }

    public void setActivityID(int activityID) {
        this.activityID = activityID;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public int getRpe() {
        return rpe;
    }

    public void setRpe(int rpe) {
        this.rpe = rpe;
    }

    public String getActivityLength() {
        return activityLength;
    }

    public void setActivityLength(String activityLength) {
        this.activityLength = activityLength;
    }

    public int getCountTowardsGoal() {
        return countTowardsGoal;
    }

    public void setCountTowardsGoal(int countTowardsGoal) {
        this.countTowardsGoal = countTowardsGoal;
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
}
