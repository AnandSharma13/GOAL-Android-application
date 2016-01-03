package com.ph.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by Anup on 23-12-15.
 */
public class DBOperations {
    private DBHandler dbHandler;

    public DBOperations(Context context) {
        dbHandler = new DBHandler(context);
    }

    public DBOperations(DBHandler dbHandler) {
        this.dbHandler = dbHandler;
    }

    ///Inserts a row into user table
    public long insertRow(User user) {
        SQLiteDatabase db = dbHandler.getWritableDatabase();
        ContentValues val = new ContentValues();
        if (user.getUser_id() != 0)
            val.put(User.column_userID, user.getUser_id());
        val.put(User.column_firstName, user.getFirst_name());
        val.put(User.column_lastName, user.getLast_name());
        val.put(User.column_type, user.getType());
        val.put(User.column_age, user.getAge());
        val.put(User.column_phone, user.getPhone());
        val.put(User.column_gender, user.getGender());
        val.put(User.column_program, user.getProgram());
        val.put(User.column_rewardsCount, user.getRewards_count());
        val.put(User.column_sync, user.getIs_sync());

        long id = db.insert(User.tableName, null, val);
        db.close();
        return id;
    }

    //insert rows in usergoal
    public long insertRow(UserGoal usergoal){
        SQLiteDatabase db = dbHandler.getWritableDatabase();
        ContentValues val = new ContentValues();
        //setting userId from user class
        val.put(UserGoal.column_userID, usergoal.getUser_id());
        val.put(UserGoal.column_type, usergoal.getType());
        val.put(UserGoal.column_startDate, usergoal.getStart_date());
        val.put(UserGoal.column_endDate, usergoal.getEnd_date());
        val.put(UserGoal.column_weeklyCount, usergoal.getWeekly_count());
        val.put(UserGoal.column_text, usergoal.getText());
        val.put(UserGoal.column_sync, usergoal.getIs_sync());
        long id = db.insert(UserGoal.tableName, null,val);
        db.close();
        return id;
    }


    public long insertRow(UserSteps userSteps) {
        SQLiteDatabase db = dbHandler.getWritableDatabase();
        ContentValues val = new ContentValues();

        if (userSteps.getSteps_id() != 0)
            val.put(UserSteps.column_stepsID, userSteps.getSteps_id());
        val.put(UserSteps.column_userID, userSteps.getUser_id());
        val.put(UserSteps.column_stepscount, userSteps.getSteps_count());
        // val.put(UserSteps.column_timestamp,userSteps.getTimestamp());
        val.put(UserSteps.column_sync, userSteps.getIs_sync());

        long id = db.insert(UserSteps.tableName, null, val);
        db.close();
        return id;
    }

    public long insertRow(Activity activity) {
        SQLiteDatabase db = dbHandler.getWritableDatabase();
        ContentValues val = new ContentValues();
        if (activity.getActivity_id() != 0)
            val.put(Activity.column_activityID, activity.getActivity_id());
        val.put(Activity.column_userID, activity.getUser_id());
        val.put(Activity.column_name, activity.getName());
        val.put(Activity.column_hitCount, activity.getHit_count());
        val.put(Activity.column_lastUsed, activity.getLast_used());
        val.put(Activity.column_type, activity.getType());
        //  val.put(activity.column_timestamp, activity.getTimestamp());
        val.put(Activity.column_isSync, activity.getIs_sync());

        long id = db.insert(Activity.tableName, null, val);
        db.close();
        return id;
    }





    public ArrayList<Object> getSyncRows(String tableName) {
        SQLiteDatabase db = dbHandler.getWritableDatabase();
        ArrayList<Object> rows = new ArrayList<Object>();

        String q = "select * from " + tableName + " where " + User.column_sync + "=0";
        Cursor cursor = db.rawQuery(q, null);
        if (cursor.moveToFirst()) {
            do {
                Object rowObj = populateRows(tableName,cursor);
                rows.add(rowObj);
            } while (cursor.moveToNext());
        }
        db.close(); //Close the db.
        cursor.close();
        return rows;
    }

    //some polymorphism here
    private Object populateRows(String tableName, Cursor cursor)
    {
        switch (tableName)
        {
            case User.tableName:
                User user = new User();
                return populateRows(user,cursor);
            case UserGoal.tableName:
                UserGoal usergoal = new UserGoal();
                return populateRows(usergoal,cursor);
            case Activity.tableName:
                Activity activity = new Activity();
                return populateRows(activity, cursor);
            case UserSteps.tableName:
                UserSteps userSteps = new UserSteps();
                return populateRows(userSteps, cursor);
            default: return null;
        }
    }


    private UserSteps populateRows(UserSteps userSteps, Cursor cursor) {
        userSteps.setSteps_id(cursor.getInt(cursor.getColumnIndex(UserSteps.column_stepsID)));
        userSteps.setUser_id(cursor.getInt(cursor.getColumnIndex(UserSteps.column_userID)));
        userSteps.setSteps_count(cursor.getInt(cursor.getColumnIndex(UserSteps.column_stepscount)));
        userSteps.setTimestamp(cursor.getString(cursor.getColumnIndex(UserSteps.column_timestamp)));
        return userSteps;
    }

    private Activity populateRows(Activity activity, Cursor cursor) {
        activity.setActivity_id(cursor.getInt(cursor.getColumnIndex(Activity.column_activityID)));
        activity.setUser_id(cursor.getInt(cursor.getColumnIndex(Activity.column_userID)));
        activity.setName(cursor.getString(cursor.getColumnIndex(Activity.column_name)));
        activity.setType(cursor.getString(cursor.getColumnIndex(Activity.column_type)));
        activity.setHit_count(cursor.getInt(cursor.getColumnIndex(Activity.column_hitCount)));
        activity.setLast_used(cursor.getString(cursor.getColumnIndex(Activity.column_lastUsed)));
        activity.setTimestamp(cursor.getString(cursor.getColumnIndex(Activity.column_timestamp)));
        return activity;
    }

    /**
     * Puts the contents of the row from the user table into the user object
     * @param user The user object
     * @param cursor cursor returned from the query
     * @return user object
     */
    private User populateRows(User user, Cursor cursor)
    {
        user.setUser_id(cursor.getInt(cursor.getColumnIndex(User.column_userID)));
        user.setFirst_name(cursor.getString(cursor.getColumnIndex(User.column_firstName)));
        user.setLast_name(cursor.getString(cursor.getColumnIndex(User.column_lastName)));
        user.setType(cursor.getString(cursor.getColumnIndex(User.column_type)));
        user.setAge(cursor.getInt(cursor.getColumnIndex(User.column_age)));
        user.setPhone(cursor.getString(cursor.getColumnIndex(User.column_phone)));
        user.setGender(cursor.getString(cursor.getColumnIndex(User.column_gender)));
        user.setProgram(cursor.getString(cursor.getColumnIndex(User.column_program)));
        user.setRewards_count(cursor.getInt(cursor.getColumnIndex(User.column_rewardsCount)));
        return user;
    }

    private UserGoal populateRows(UserGoal userGoal, Cursor cursor)
    {
        userGoal.setGoal_id(cursor.getInt(cursor.getColumnIndex(UserGoal.column_goalID)));
        userGoal.setUser_id(cursor.getInt(cursor.getColumnIndex(UserGoal.column_userID)));
        userGoal.setTimestamp(cursor.getString(cursor.getColumnIndex(UserGoal.column_timeStamp)));
        userGoal.setType(cursor.getString(cursor.getColumnIndex(UserGoal.column_type)));
        userGoal.setStart_date(cursor.getString(cursor.getColumnIndex(UserGoal.column_startDate)));
        userGoal.setEnd_date(cursor.getString(cursor.getColumnIndex(UserGoal.column_endDate)));
        userGoal.setWeekly_count(cursor.getInt(cursor.getColumnIndex(UserGoal.column_weeklyCount)));
        userGoal.setReward_type(cursor.getString(cursor.getColumnIndex(UserGoal.column_rewardType)));
        userGoal.setText(cursor.getString(cursor.getColumnIndex(UserGoal.column_text)));
        return userGoal;
    }

    @Deprecated
    public boolean setSyncUserRows(ArrayList<User> userList)
    {
        SQLiteDatabase db = dbHandler.getWritableDatabase();

        String updateQuery="";
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();

        ContentValues val = new ContentValues();

        val.put("is_sync", 1);
        try {
            for (User user :
                    userList) {
                db.update(User.tableName, val, "user_id=?", new String[]{String.valueOf(user.getUser_id())});
            }

            db.close();
            return true;
        }catch (Exception ex)
        {
            Log.e("setSyncUserRows",ex.getLocalizedMessage());
            db.close();
            return false;
        }
    }

    public void setSyncFlag(String tableName)
    {
        SQLiteDatabase db = dbHandler.getWritableDatabase();
        String query = "Update " + tableName + " set is_sync=1 where is_sync=0"; //A little optimization to reduce query runtime.
        db.execSQL(query);
        db.close(); //Close the db after the operation is finished.
    }


}
