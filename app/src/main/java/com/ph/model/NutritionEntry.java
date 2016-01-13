package com.ph.model;

/**
 * Created by Anand on 12/27/2015.
 */
public class NutritionEntry {
    private int nutrition_entry_id;
    private int goal_id;
    private String nutrition_type;
    private String timestamp;
    private int towards_goal;
    private String type;
    private int attic_food;
    private int dairy;
    private int vegetable;
    private int fruit;
    private int grain;
    private int water_intake;
    private String notes;
    private String image;
    private String base64Image;
    private int is_sync;

    public final static String tableName = "nutrition_entry";
    public static String column_nutritionEntryID = "nutrition_entry_id";
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
    public static String column_sync = "is_sync";


    public NutritionEntry(int nutritionEntryID, int goalID, String nutritionType, String timeStamp, int countTowardsGoal, String type, int atticFood, int dairy, int vegetable, int fruit, int grain, int waterIntake, String notes) {
        this.nutrition_entry_id = nutritionEntryID;
        this.goal_id = goalID;
        this.nutrition_type = nutritionType;
        this.timestamp = timeStamp;
        this.towards_goal = countTowardsGoal;
        this.type = type;
        this.attic_food = atticFood;
        this.dairy = dairy;
        this.vegetable = vegetable;
        this.fruit = fruit;
        this.grain = grain;
        this.water_intake = waterIntake;
        this.notes = notes;
        //this.image = image;
    }

    public NutritionEntry()
    {

    }

    public int getNutrition_entry_id() {
        return nutrition_entry_id;
    }

    public void setNutrition_entry_id(int nutrition_entry_id) {
        this.nutrition_entry_id = nutrition_entry_id;
    }

    public int getGoal_id() {
        return goal_id;
    }

    public void setGoal_id(int goal_id) {
        this.goal_id = goal_id;
    }

    public String getNutrition_type() {
        return nutrition_type;
    }

    public void setNutrition_type(String nutrition_type) {
        this.nutrition_type = nutrition_type;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public int getTowards_goal() {
        return towards_goal;
    }

    public void setTowards_goal(int towards_goal) {
        this.towards_goal = towards_goal;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getAttic_food() {
        return attic_food;
    }

    public void setAttic_food(int attic_food) {
        this.attic_food = attic_food;
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

    public int getWater_intake() {
        return water_intake;
    }

    public void setWater_intake(int water_intake) {
        this.water_intake = water_intake;
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
}
