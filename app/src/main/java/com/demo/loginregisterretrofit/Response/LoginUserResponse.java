package com.demo.loginregisterretrofit.Response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LoginUserResponse {
    @SerializedName("response")
    @Expose()
    public Loginresponse loginresponse;

    public Loginresponse getLoginresponse() {
        return loginresponse;
    }
}
