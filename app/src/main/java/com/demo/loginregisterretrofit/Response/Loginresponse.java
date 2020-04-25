package com.demo.loginregisterretrofit.Response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Loginresponse {
    @SerializedName("user_id")
    @Expose()
    public String user_id;


    @SerializedName("first_name")
    @Expose()
    public String first_name;

    @SerializedName("last_name")
    @Expose()
    public String last_name;

    @SerializedName("email")
    @Expose()
    public String email;


    public String getEmail() {
        return email;
    }


    public String getFirst_name() {
        return first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public String getUser_id() {
        return user_id;
    }
}
