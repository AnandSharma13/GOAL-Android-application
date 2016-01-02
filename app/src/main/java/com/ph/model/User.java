package com.ph.model;

/**
 * Created by Anand on 12/27/2015.
 */
public class User {

    public final static String tableName = "user";
    public static String column_userID = "user_id";
    public static String column_firstName = "first_name";
    public static String column_lastName = "last_name";
    public static String column_type = "type";
    public static String column_age = "age";
    public static String column_phone = "phone";
    public static String column_gender = "gender";
    public static String column_phoneModel = "phone_model";
    public static String column_program = "program";
    public static String column_rewardsCount = "rewards_count";
    public static String column_sync = "is_sync";
    private int user_id = 0;
    private String first_name;
    private String last_name;
    private String type;
    private int age;
    private String phone;
    private String gender;
    private String phone_model;
    private String program;
    private int rewards_count;
    private int is_sync;


    public User(String first_name, String lastName, String type, int age, String phone, String gender, String phoneModel, String program, int rewardsCount) {
        //  this.userID = userID;
        this.first_name = first_name;
        this.last_name = lastName;
        this.type = type;
        this.age = age;
        this.phone = phone;
        this.gender = gender;
        this.phone_model = phoneModel;
        this.program = program;
        this.rewards_count = rewardsCount;
    }

    public User() {

    }


    public int getUser_id() {
        return user_id;
    }


    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getPhone_model() {
        return phone_model;
    }

    public void setPhone_model(String phone_model) {
        this.phone_model = phone_model;
    }

    public String getProgram() {
        return program;
    }

    public void setProgram(String program) {
        this.program = program;
    }

    public int getRewards_count() {
        return rewards_count;
    }

    public void setRewards_count(int rewards_count) {
        this.rewards_count = rewards_count;
    }

    public int getIs_sync() {
        return is_sync;
    }

    public void setIs_sync(int is_sync) {
        this.is_sync = is_sync;
    }

}

