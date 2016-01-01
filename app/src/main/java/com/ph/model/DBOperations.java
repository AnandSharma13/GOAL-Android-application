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
        if(user.getUserId() != 0)
            val.put(user.column_userID, user.getUserId());
        val.put(user.column_firstName,user.getFirstName());
        val.put(user.column_lastName,user.getLastName());
        val.put(user.column_type,user.getType());
        val.put(user.column_age,user.getAge());
        val.put(user.column_phone,user.getPhone());
        val.put(user.column_gender,user.getGender());
        val.put(user.column_program,user.getProgram());
        val.put(user.column_rewardsCount,user.getRewardsCount());
        val.put(user.column_sync,0);

        long id = db.insert(User.tableName, null, val);
        db.close();
        return id;
    }

    //insert rows in usergoal
    public long insertRow(UserGoal usergoal){
        SQLiteDatabase db = dbHandler.getWritableDatabase();
        ContentValues val = new ContentValues();
        //setting userId from user class
        val.put(usergoal.column_userID, usergoal.getUserID());
        val.put(usergoal.column_type, usergoal.getType());
        val.put(usergoal.column_startDate, usergoal.getStartDate());
        val.put(usergoal.column_endDate, usergoal.getEndDate());
        val.put(usergoal.column_weeklyCount, usergoal.getWeeklyCount());
        val.put(usergoal.column_text, usergoal.getText());
        val.put(usergoal.column_sync,0);
        long id = db.insert(UserGoal.tableName, null,val);
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
            default: return null;
        }
    }

    /**
     * Puts the contents of the row from the user table into the user object
     * @param user The user object
     * @param cursor cursor returned from the query
     * @return user object
     */
    private User populateRows(User user, Cursor cursor)
    {
        user.setUserId(cursor.getInt(cursor.getColumnIndex(User.column_userID)));
        user.setFirstName(cursor.getString(cursor.getColumnIndex(User.column_firstName)));
        user.setLastName(cursor.getString(cursor.getColumnIndex(User.column_lastName)));
        user.setType(cursor.getString(cursor.getColumnIndex(User.column_type)));
        user.setAge(cursor.getInt(cursor.getColumnIndex(User.column_age)));
        user.setPhone(cursor.getString(cursor.getColumnIndex(User.column_phone)));
        user.setGender(cursor.getString(cursor.getColumnIndex(User.column_gender)));
        user.setProgram(cursor.getString(cursor.getColumnIndex(User.column_program)));
        user.setRewardsCount(cursor.getInt(cursor.getColumnIndex(User.column_rewardsCount)));
        return user;
    }

    private UserGoal populateRows(UserGoal userGoal, Cursor cursor)
    {
        userGoal.setUserID(cursor.getInt(cursor.getColumnIndex(UserGoal.column_userID)));
        userGoal.setTimeStamp(cursor.getString(cursor.getColumnIndex(UserGoal.column_timeStamp)));
        userGoal.setType(cursor.getString(cursor.getColumnIndex(UserGoal.column_type)));
        userGoal.setStartDate(cursor.getString(cursor.getColumnIndex(UserGoal.column_startDate)));
        userGoal.setEndDate(cursor.getString(cursor.getColumnIndex(UserGoal.column_endDate)));
        userGoal.setWeeklyCount(cursor.getInt(cursor.getColumnIndex(UserGoal.column_weeklyCount)));
        userGoal.setRewardType(cursor.getString(cursor.getColumnIndex(UserGoal.column_rewardType)));
        userGoal.setText(cursor.getString(cursor.getColumnIndex(UserGoal.column_text)));
        return userGoal;
    }


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
                db.update(User.tableName, val, "user_id=?", new String[]{String.valueOf(user.getUserId())});
            }


            return true;
        }catch (Exception ex)
        {
            Log.e("setSyncUserRows",ex.getLocalizedMessage());
            return false;
        }
    }

    public void setSyncFlag(String tableName)
    {
        SQLiteDatabase db = dbHandler.getWritableDatabase();
        String query = "Update "+tableName+" set is_sync=1";
        db.execSQL(query);
    }


}
