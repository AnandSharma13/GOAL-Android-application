package com.ph.model;


public class ActivityEntry {

    private long activity_entry_id;
    private long goal_id;
    private String date;
    private long activity_id;
    private String timestamp;
    private int rpe;
    private String activity_length;
    private int count_towards_goal;
    private String notes;
    private String image;
    private int is_sync;
    private String base64Image;

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
    public static String column_date = "date";


    public ActivityEntry(int activity_entry_id, int goalID, int activityID, String timeStamp, int rpe, String activityLength, int countTowardsGoal, String notes, String image) {
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

    public long getActivity_entry_id() {
        return activity_entry_id;
    }

    public void setActivity_entry_id(long activity_entry_id) {
        this.activity_entry_id = activity_entry_id;
    }

    public long getGoal_id() {
        return goal_id;
    }

    public void setGoal_id(long goal_id) {
        this.goal_id = goal_id;
    }

    public long getActivity_id() {
        return activity_id;
    }

    public void setActivity_id(long activity_id) {
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

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getIs_sync() {
        return is_sync;
    }

    public void setIs_sync(int is_sync) {
        this.is_sync = is_sync;
    }

    public String getBase64Image() {
        return base64Image;
    }

    public void setBase64Image(String base64Image) {
        this.base64Image = base64Image;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
