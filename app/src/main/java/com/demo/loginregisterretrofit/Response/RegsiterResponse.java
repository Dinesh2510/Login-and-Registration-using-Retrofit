package com.demo.loginregisterretrofit.Response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RegsiterResponse {
    @SerializedName("response")
    @Expose
    private String response;
    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }
}
