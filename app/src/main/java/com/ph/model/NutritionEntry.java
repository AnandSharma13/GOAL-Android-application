package com.ph.model;

import android.media.Image;

/**
 * Created by Anand on 12/27/2015.
 */
public class NutritionEntry {
    private int nutritionEntryID;
    private int goalID;
    private String nutritionType;
    private String timeStamp;
    private int countTowardsGoal;
    private String type;
    private int atticFood;
    private int dairy;
    private int vegetable;
    private int fruit;
    private int grain;
    private int waterIntake;
    private String notes;
    private Image image;

    public static String tableName = "nutritionentry";
    public static String column_nutritionEntryID = "nutritionEntry_id";
    public static String column_goalID = "goal_id";
    public static String column_nutritiontype = "nutrition_type";
    public static String column_timestamp = "timestamp";
    public static String column_counttowardsgoal = "towards_goal";
    public static String column_type = "type";
    public static String column_atticFood = "attic_food";
    public static String column_dairy = "dairy";
    public static String column_vegetable = "vegetable";
    public static String column_fruit = "fruit";
    public static String column_grain = "grain";
    public static String column_waterintake = "water_intake";
    public static String column_notes =  "notes";
    public static String column_image =  "image";


    public NutritionEntry(int nutritionEntryID, int goalID, String nutritionType, String timeStamp, int countTowardsGoal, String type, int atticFood, int dairy, int vegetable, int fruit, int grain, int waterIntake, String notes, Image image) {
        this.nutritionEntryID = nutritionEntryID;
        this.goalID = goalID;
        this.nutritionType = nutritionType;
        this.timeStamp = timeStamp;
        this.countTowardsGoal = countTowardsGoal;
        this.type = type;
        this.atticFood = atticFood;
        this.dairy = dairy;
        this.vegetable = vegetable;
        this.fruit = fruit;
        this.grain = grain;
        this.waterIntake = waterIntake;
        this.notes = notes;
        this.image = image;
    }



    public int getNutritionEntryID() {
        return nutritionEntryID;
    }

    public void setNutritionEntryID(int nutritionEntryID) {
        this.nutritionEntryID = nutritionEntryID;
    }

    public int getGoalID() {
        return goalID;
    }

    public void setGoalID(int goalID) {
        this.goalID = goalID;
    }

    public String getNutritionType() {
        return nutritionType;
    }

    public void setNutritionType(String nutritionType) {
        this.nutritionType = nutritionType;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public int getCountTowardsGoal() {
        return countTowardsGoal;
    }

    public void setCountTowardsGoal(int countTowardsGoal) {
        this.countTowardsGoal = countTowardsGoal;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getAtticFood() {
        return atticFood;
    }

    public void setAtticFood(int atticFood) {
        this.atticFood = atticFood;
    }

    public int getDairy() {
        return dairy;
    }

    public void setDairy(int dairy) {
        this.dairy = dairy;
    }

    public int getVegetable() {
        return vegetable;
    }

    public void setVegetable(int vegetable) {
        this.vegetable = vegetable;
    }

    public int getFruit() {
        return fruit;
    }

    public void setFruit(int fruit) {
        this.fruit = fruit;
    }

    public int getGrain() {
        return grain;
    }

    public void setGrain(int grain) {
        this.grain = grain;
    }

    public int getWaterIntake() {
        return waterIntake;
    }

    public void setWaterIntake(int waterIntake) {
        this.waterIntake = waterIntake;
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
