package com.ph.model;

/**
 * Created by Anand on 12/27/2015.
 */
public class User {

    private int userId = 0;
    private String firstName;
    private String lastName;
    private String type;
    private int age;
    private String phone;
    private String gender;
    private String phoneModel;
    private String program;
    private int rewardsCount;


    private int isSync;

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


    public User(String firstName, String lastName, String type, int age, String phone, String gender, String phoneModel, String program, int rewardsCount) {
        //  this.userID = userID;
        this.firstName = firstName;
        this.lastName = lastName;
        this.type = type;
        this.age = age;
        this.phone = phone;
        this.gender = gender;
        this.phoneModel = phoneModel;
        this.program = program;
        this.rewardsCount = rewardsCount;
    }

    public User() {

    }


    public int getUserId() {
        return userId;
    }

//    user id once set will not be set again

//    public void setUserId(int userId) {
//        this.userId = this.userId == -1 ? userId: this.userId;
//    }

    public void setUserId(int userId) {
        this.userId = this.userId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
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

    public String getPhoneModel() {
        return phoneModel;
    }

    public void setPhoneModel(String phoneModel) {
        this.phoneModel = phoneModel;
    }

    public String getProgram() {
        return program;
    }

    public void setProgram(String program) {
        this.program = program;
    }

    public int getRewardsCount() {
        return rewardsCount;
    }

    public void setRewardsCount(int rewardsCount) {
        this.rewardsCount = rewardsCount;
    }

    public int getIsSync() {
        return isSync;
    }

    public void setIsSync(int isSync) {
        this.isSync = isSync;
    }

}

