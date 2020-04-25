package com.demo.loginregisterretrofit.Forms;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.demo.loginregisterretrofit.HomeScreen;
import com.demo.loginregisterretrofit.R;
import com.demo.loginregisterretrofit.Response.API;
import com.demo.loginregisterretrofit.Response.RetrofitClient;
import com.demo.loginregisterretrofit.Response.UpdateUserResponse;
import com.google.android.material.snackbar.Snackbar;

import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.demo.loginregisterretrofit.Forms.LoginActivity.EMAIL;
import static com.demo.loginregisterretrofit.Forms.LoginActivity.FNAME;
import static com.demo.loginregisterretrofit.Forms.LoginActivity.LNAME;
import static com.demo.loginregisterretrofit.Forms.LoginActivity.SHARED_PREFERENCES_NAME;
import static com.demo.loginregisterretrofit.Forms.LoginActivity.USER_ID;

public class UpdateActivity extends AppCompatActivity {
    private EditText et_fname, et_lname, et_email, etpassword;
    String str_email, str_fname, str_lname, str_userid, str_passowrd;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    Button submit_btn, logout;
    TextView u_name, u_email, post_count, bookmark_count, following_count;
    ImageView u_image;
    LinearLayout expandableView, layt_bk, layt_post, layt_follow;
    Pattern pattern_pwd = Pattern.compile("^[a-zA-Z0-9]+$");
    public static String userid = "", userfname = "", userlname = "", useremail = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);


        sharedPreferences = getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
        str_userid = sharedPreferences.getString(USER_ID, "");
        str_fname = sharedPreferences.getString(FNAME, "");
        str_lname = sharedPreferences.getString(LNAME, "");
        str_email = sharedPreferences.getString(EMAIL, "");


        et_fname = (EditText) findViewById(R.id.user_fname);
        et_lname = (EditText) findViewById(R.id.user_lname);
        et_email = (EditText) findViewById(R.id.user_email);
        etpassword = (EditText) findViewById(R.id.user_id);
        submit_btn = findViewById(R.id.update_btn);
        et_fname.setText(str_fname);
        et_lname.setText(str_lname);
        et_email.setText(str_email);
        etpassword.setText(" ");

        submit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                str_email = et_email.getText().toString().trim();
                str_fname = et_fname.getText().toString().trim();
                str_lname = et_lname.getText().toString().trim();
                str_passowrd = etpassword.getText().toString().trim();

                if (!str_email.isEmpty() && Patterns.EMAIL_ADDRESS.matcher(str_email).matches()) {

                    if (!str_passowrd.isEmpty() && pattern_pwd.matcher(str_passowrd).matches()) {

                        if (!str_fname.isEmpty()) {

                            if (!str_lname.isEmpty()) {

                                updateData();

                            } else {
                                Snackbar.make(expandableView, "Enter the Last Name", Snackbar.LENGTH_SHORT).show();
                            }
                        } else {
                            Snackbar.make(expandableView, "Enter the First Name", Snackbar.LENGTH_SHORT).show();
                        }
                    } else {
                        Snackbar.make(expandableView, "Enter the Valid Password", Snackbar.LENGTH_SHORT).show();
                    }
                } else {
                    Snackbar.make(expandableView, "Enter the Valid Email", Snackbar.LENGTH_SHORT).show();
                }

            }
        });

    }

    private void updateData() {


        API api = RetrofitClient.getRetrofitInstance().create(API.class);

        Call<UpdateUserResponse> call = api.sentUserUpdateddata(str_fname, str_lname, str_email, str_passowrd, str_userid);

        call.enqueue(new Callback<UpdateUserResponse>() {
            @Override
            public void onResponse(Call<UpdateUserResponse> call, Response<UpdateUserResponse> response) {
                Log.i("Responsestring", response.body().toString());
                if (response.isSuccessful()) {
                    if (response.body() != null) {

                        Log.i("onSuccess", response.body().updateResponse.getEmail());
                        userid = response.body().updateResponse.user_id;
                        useremail = response.body().updateResponse.email;
                        userfname = response.body().updateResponse.first_name;
                        userlname = response.body().updateResponse.last_name;
                        try {
                            parseUpdateData();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        Log.i("onEmptyResponse", "Returned empty response");//Toast.makeText(getContext(),"Nothing returned",Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<UpdateUserResponse> call, Throwable t) {

                Log.d("if_o f gh_book", "onFailure: " + t);

            }
        });

    }

    private void parseUpdateData() {
        try {
            if (!useremail.isEmpty()) {
                saveInfo();
            } else {
                Log.d("loginresponse", "empty");
            }
            Toast.makeText(this, "Updated Successfully!", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, HomeScreen.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            this.finish();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void saveInfo() {
        try {
            Log.d("sherf_1", "called" + useremail);
            if (!useremail.isEmpty()) {
                Log.d("sherf", "called" + useremail);
                sharedPreferences = getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
                editor = sharedPreferences.edit();
                editor.putString(USER_ID, userid);
                editor.putString(FNAME, userfname);
                editor.putString(LNAME, userlname);
                editor.putString(EMAIL, useremail);
                editor.apply();

                Intent intent = new Intent(this, HomeScreen.class);
                startActivity(intent);
                finish();
            } else {
                //ye ata toast
                Toast.makeText(this, "failed", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
