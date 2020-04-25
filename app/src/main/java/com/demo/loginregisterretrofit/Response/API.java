package com.demo.loginregisterretrofit.Response;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface API {

    //String BaseURL = "http://192.168.43.207/digital_reader/";
    String BaseURL = "https://pixeldev.in/webservices/digital_reader/";



    //register

    @FormUrlEncoded
    @POST("register.php")
    Call<RegsiterResponse> getUserRegi(
            @Field("first_name") String first_name,
            @Field("last_name") String last_name,
            @Field("email") String email,
            @Field("password") String password
    );

    //Update user
    @FormUrlEncoded
    @POST("Update_user.php/{user_id}")
    Call<UpdateUserResponse> sentUserUpdateddata(
            @Field("first_name") String first_name,
            @Field("last_name") String last_name,
            @Field("email") String email,
            @Field("password") String password,
            @Field("user_id") String user_id
    );



    //login
    @FormUrlEncoded
    @POST("login.php")
    Call<LoginUserResponse> getUserLogin(
            @Field("email") String email,
            @Field("password") String password
    );


}
