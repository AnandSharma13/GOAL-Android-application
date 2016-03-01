package com.ph.model;

/**
 * Created by Anand on 12/27/2015.
 */
public class UserGoal {
    public static final String tableName = "user_goal";
    public static final String column_goalID = "goal_id";
    public static final String column_userID = "user_id";
    public static final String column_timeStamp = "timestamp";
    public static final String column_type = "type";
    public static final String column_startDate = "start_date";
    public static final String column_endDate = "end_date";
    public static final String column_weeklyCount = "weekly_count";
    public static final String column_rewardType = "reward_type";
    public static final String column_text = "text";
    public static final String column_sync = "is_sync";
    private long goal_id;
    private int user_id;
    private String timestamp;
    private String type;
    private String start_date;
    private String end_date;
    private int weekly_count;
    private String reward_type;
    private String text;
    private int is_sync;


    public UserGoal(){}

    public UserGoal(int user_id, String type, String start_date, String endDate, int weeklyCount, String text) {
        this.user_id = user_id;
        //   this.timestamp = timestamp;
        this.type = type;
        this.start_date = start_date;
        this.end_date = endDate;
        this.weekly_count = weeklyCount;
        this.text = text;
    }


    public long getGoal_id() {
        return goal_id;
    }

    public void setGoal_id(long goal_id) {
        this.goal_id = goal_id;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStart_date() {
        return start_date;
    }

    public void setStart_date(String start_date) {
        this.start_date = start_date;
    }

    public String getEnd_date() {
        return end_date;
    }

    public void setEnd_date(String end_date) {
        this.end_date = end_date;
    }

    public int getWeekly_count() {
        return weekly_count;
    }

    public void setWeekly_count(int weekly_count) {
        this.weekly_count = weekly_count;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getReward_type() {
        return reward_type;
    }

    public void setReward_type(String reward_type) {
        this.reward_type = reward_type;
    }

    public int getIs_sync() {
        return is_sync;
    }

    public void setIs_sync(int is_sync) {
        this.is_sync = is_sync;
    }
}
