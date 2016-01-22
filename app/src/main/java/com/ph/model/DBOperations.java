package com.ph.model;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.preference.PreferenceManager;
import android.util.Base64;
import android.util.Log;

import com.ph.Utils.DateOperations;
import com.ph.Utils.StartEndDateObject;
import com.ph.view.ImageHandler;

import java.io.IOException;
import java.net.URISyntaxException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Anup on 23-12-15.
 */
public class DBOperations {
    private DBHandler dbHandler;
    private Context context;

    public DBOperations(Context context) {
        this.context = context;
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
    public long insertRow(UserGoal usergoal) throws ParseException {
        SQLiteDatabase db = dbHandler.getWritableDatabase();
        ContentValues val = new ContentValues();
        java.sql.Date startDate = getSqlDate(usergoal.getStart_date());
        java.sql.Date endDate = getSqlDate(usergoal.getEnd_date());

        SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd");
        //setting userId from user class
        val.put(UserGoal.column_userID, usergoal.getUser_id());
        val.put(UserGoal.column_type, usergoal.getType());
        val.put(UserGoal.column_startDate, dateformat.format(startDate));
        val.put(UserGoal.column_endDate, dateformat.format(endDate));
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

    public long insertRow(ActivityEntry activityEntry)
    {
        SQLiteDatabase db = dbHandler.getWritableDatabase();
        ContentValues val = new ContentValues();

        if(activityEntry.getActivity_entry_id() != 0)
            val.put(ActivityEntry.column_activityID,activityEntry.getActivity_entry_id());
        val.put(ActivityEntry.column_activityID,activityEntry.getActivity_id());
        val.put(ActivityEntry.column_activitylength,activityEntry.getActivity_length());
        val.put(ActivityEntry.column_counttowardsgoal,activityEntry.getCount_towards_goal());
        val.put(ActivityEntry.column_goalID,activityEntry.getGoal_id());
        val.put(ActivityEntry.column_image,activityEntry.getImage());
        val.put(ActivityEntry.column_notes,activityEntry.getNotes());
        val.put(ActivityEntry.column_rpe,activityEntry.getRpe());
        val.put(ActivityEntry.column_sync,activityEntry.getIs_sync());
        val.put(ActivityEntry.column_date,getSqlDate(activityEntry.getDate()).toString());


        long id = db.insert(ActivityEntry.tableName, null, val);
        db.close();

        return id;

    }

    public long insertRow(NutritionEntry nutritionEntry)
    {
        SQLiteDatabase db = dbHandler.getWritableDatabase();
        ContentValues val = new ContentValues();
        if(nutritionEntry.getNutrition_entry_id() != 0)
            val.put(NutritionEntry.column_nutritionEntryID,nutritionEntry.getNutrition_entry_id());
        val.put(NutritionEntry.column_goalID,nutritionEntry.getGoal_id());
        val.put(NutritionEntry.column_atticFood,nutritionEntry.getAttic_food());
        val.put(NutritionEntry.column_dairy,nutritionEntry.getDairy());
        val.put(NutritionEntry.column_fruit,nutritionEntry.getFruit());
        val.put(NutritionEntry.column_grain,nutritionEntry.getGrain());
        val.put(NutritionEntry.column_image,nutritionEntry.getImage());
        val.put(NutritionEntry.column_notes,nutritionEntry.getNotes());
        val.put(NutritionEntry.column_nutritiontype,nutritionEntry.getNutrition_type());
        val.put(NutritionEntry.column_sync,nutritionEntry.getIs_sync());
       // val.put(NutritionEntry.column_timestamp,nutritionEntry.getTimestamp());
        val.put(NutritionEntry.column_type,nutritionEntry.getType());
        val.put(NutritionEntry.column_vegetable,nutritionEntry.getVegetable());
        val.put(NutritionEntry.column_waterintake,nutritionEntry.getWater_intake());

        long id = db.insert(NutritionEntry.tableName, null, val);
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


    public List<Activity> getActivities(String type)
    {
        SQLiteDatabase db = dbHandler.getWritableDatabase();
        List<Activity> activityList = new ArrayList<>();
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        String user_id =sharedPreferences.getString("user_id", "0");

        String query = "select * from activity where user_id in ("+user_id+", 0) and type = '"+type+"'";

        Cursor cursor = db.rawQuery(query,null);

        if (cursor.moveToFirst()) {
            do {
                Activity activity = new Activity();
                activity = populateRows(activity,cursor);
                activityList.add(activity);
            } while (cursor.moveToNext());
        }
        db.close(); //Close the db.
        cursor.close();
        return activityList;
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
            case ActivityEntry.tableName:
                return populateRows(new ActivityEntry(),cursor);
            case NutritionEntry.tableName:
                return populateRows(new NutritionEntry(),cursor);
            default: return null;
        }
    }


    private NutritionEntry populateRows(NutritionEntry nutritionEntry, Cursor cursor)
    {
        nutritionEntry.setNutrition_entry_id(cursor.getInt(cursor.getColumnIndex(NutritionEntry.column_nutritionEntryID)));
        nutritionEntry.setTimestamp(cursor.getString(cursor.getColumnIndex(NutritionEntry.column_timestamp)));
        nutritionEntry.setIs_sync(cursor.getInt(cursor.getColumnIndex(NutritionEntry.column_sync)));
        nutritionEntry.setAttic_food(cursor.getInt(cursor.getColumnIndex(NutritionEntry.column_atticFood)));
        nutritionEntry.setDairy(cursor.getInt(cursor.getColumnIndex(NutritionEntry.column_dairy)));
        nutritionEntry.setFruit(cursor.getInt(cursor.getColumnIndex(NutritionEntry.column_fruit)));
        nutritionEntry.setGoal_id(cursor.getInt(cursor.getColumnIndex(NutritionEntry.column_goalID)));
        nutritionEntry.setGrain(cursor.getInt(cursor.getColumnIndex(NutritionEntry.column_grain)));
        nutritionEntry.setNutrition_type(cursor.getString(cursor.getColumnIndex(NutritionEntry.column_nutritiontype)));
        nutritionEntry.setTowards_goal(cursor.getInt(cursor.getColumnIndex(NutritionEntry.column_counttowardsgoal)));
        nutritionEntry.setType(cursor.getString(cursor.getColumnIndex(NutritionEntry.column_type)));
        nutritionEntry.setVegetable(cursor.getInt(cursor.getColumnIndex(NutritionEntry.column_vegetable)));
        nutritionEntry.setWater_intake(cursor.getInt(cursor.getColumnIndex(NutritionEntry.column_waterintake)));
        nutritionEntry.setNotes(cursor.getString(cursor.getColumnIndex(NutritionEntry.column_notes)));


        String image_uri = cursor.getString(cursor.getColumnIndex(ActivityEntry.column_image));

        nutritionEntry.setImage(image_uri);




        //nutritionEntry.setImage(cursor.getInt(cursor.getColumnIndex(NutritionEntry.column_image)));


        return nutritionEntry;
    }

    private String getBase64Image(String uri) {
        try {
            java.net.URI URI = new java.net.URI(uri);
            byte[] imageByteArray = ImageHandler.getImageByteArray(URI.toURL());
            String imageBase64String = Base64.encodeToString(imageByteArray, Base64.DEFAULT);
            return imageBase64String;
        }catch(URISyntaxException e)
        {
            e.printStackTrace();
        }catch (IOException ex)
        {
            ex.printStackTrace();
        }catch(Exception e)
        {
            e.printStackTrace();
        }
        return "";
    }

    private ActivityEntry populateRows(ActivityEntry activityEntry, Cursor cursor) {
        activityEntry.setActivity_entry_id(cursor.getInt(cursor.getColumnIndex(ActivityEntry.column_activityEntryID)));
        activityEntry.setActivity_id(cursor.getInt(cursor.getColumnIndex(ActivityEntry.column_activityID)));

        String image_uri = cursor.getString(cursor.getColumnIndex(ActivityEntry.column_image));
        activityEntry.setImage(image_uri);

        //Now populate the base64 form of the image.
        if(image_uri.equals(""))
            activityEntry.setBase64Image("");
        else
            activityEntry.setBase64Image(getBase64Image(image_uri));

        activityEntry.setTimestamp(cursor.getString(cursor.getColumnIndex(ActivityEntry.column_timestamp)));
        activityEntry.setIs_sync(cursor.getInt(cursor.getColumnIndex(ActivityEntry.column_sync)));
        activityEntry.setNotes(cursor.getString(cursor.getColumnIndex(ActivityEntry.column_notes)));
        activityEntry.setActivity_length(cursor.getString(cursor.getColumnIndex(ActivityEntry.column_activitylength)));
        activityEntry.setCount_towards_goal(cursor.getInt(cursor.getColumnIndex(ActivityEntry.column_counttowardsgoal)));
        activityEntry.setGoal_id(cursor.getInt(cursor.getColumnIndex(ActivityEntry.column_goalID)));
        activityEntry.setRpe(cursor.getInt(cursor.getColumnIndex(ActivityEntry.column_rpe)));
        activityEntry.setDate(cursor.getString(cursor.getColumnIndex(ActivityEntry.column_date)));


        return activityEntry;
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

    public UserGoal getCurrentUserGoalFromDB(String type)
    {
        SQLiteDatabase db = dbHandler.getReadableDatabase();
        DateOperations dateOperations = new DateOperations(context);
        UserGoal userGoal = new UserGoal();
        StartEndDateObject startEndDateObject = dateOperations.getDatesForToday();
        String startDate = dateOperations.getMysqlDateFormat().format(startEndDateObject.startDate);
        String endDate = dateOperations.getMysqlDateFormat().format(startEndDateObject.endDate);
        String query = "select * from user_goal where timestamp between '"+startDate+"' and '"+endDate+"' and type= '"+type+"' order by timestamp";
        Cursor cursor = db.rawQuery(query,null);

        if(cursor.getCount() == 0)
        {
            return null;
        }
        if(cursor.moveToFirst())
        {
            userGoal = populateRows(userGoal,cursor);
        }
        return userGoal;
    }

    public java.sql.Date  getSqlDate(String date) {

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date parsed = null;
        try {
            parsed = format.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        java.sql.Date sqlDate = new java.sql.Date(parsed.getTime());
        return sqlDate;
    }

}
