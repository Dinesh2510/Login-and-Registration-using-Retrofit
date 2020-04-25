package com.demo.loginregisterretrofit.Response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UpdateUserResponse  {
    @SerializedName("response")
    @Expose()
    public UpdateResponse updateResponse;

    public UpdateResponse getUpdateResponse() {
        return updateResponse;
    }
}
