package com.ph.model;

/**
 * Created by Anand on 12/27/2015.
 */
public class UserGoal {
    private int goalID;
    private int userID;
    private String timeStamp;
    private String type;
    private String startDate;
    private String endDate;
    private int weeklyCount;


    public String rewardType;
    private String text;

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
    public static String column_sync = "is_sync";


    public UserGoal(){}

    public UserGoal(int userID, String type, String startDate, String endDate, int weeklyCount, String text) {
        this.userID = userID;
     //   this.timeStamp = timeStamp;
        this.type = type;
        this.startDate = startDate;
        this.endDate = endDate;
        this.weeklyCount = weeklyCount;
        this.text = text;
    }



    public int getGoalID() {
        return goalID;
    }

    public void setGoalID(int goalID) {
        this.goalID = goalID;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public int getWeeklyCount() {
        return weeklyCount;
    }

    public void setWeeklyCount(int weeklyCount) {
        this.weeklyCount = weeklyCount;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
    public String getRewardType() {
        return rewardType;
    }

    public void setRewardType(String rewardType) {
        this.rewardType = rewardType;
    }

}
