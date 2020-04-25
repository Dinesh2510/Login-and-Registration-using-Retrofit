package com.demo.loginregisterretrofit.Forms;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.demo.loginregisterretrofit.HomeScreen;
import com.demo.loginregisterretrofit.R;
import com.demo.loginregisterretrofit.Response.API;
import com.demo.loginregisterretrofit.Response.LoginUserResponse;
import com.demo.loginregisterretrofit.Response.RestAdapter;
import com.demo.loginregisterretrofit.Response.RetrofitClient;
import com.google.android.material.snackbar.Snackbar;

import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class LoginActivity extends AppCompatActivity {

    private EditText etUname, etPass;
    private Button btnlogin;
    private TextView tvreg, skip_txt;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    public static final String SHARED_PREFERENCES_NAME = "login_portal";
    public static final String USER_ID = "user_id";
    public static final String FNAME = "fname";
    public static final String LNAME = "lname";
    public static final String EMAIL = "email";
    public static final String PASSWORD = "password";
    private View parent_view;
    String email, password;
    RelativeLayout rl_pwd;
    LinearLayout ll_lay;
    Pattern pattern_pwd = Pattern.compile("^[a-zA-Z0-9]+$");
    public static String userid = "", userfname = "", userlname = "", useremail = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStatusBarGradiant(this);
        setContentView(R.layout.activity_main);

        SharedPreferences prefs = getSharedPreferences("prefs", MODE_PRIVATE);
        final boolean firstStart = prefs.getBoolean("firstStart", true);


        ll_lay = findViewById(R.id.ll_lay);
        rl_pwd = findViewById(R.id.rl_pwd);
        etUname = (EditText) findViewById(R.id.etemail);
        etPass = (EditText) findViewById(R.id.etpassword);
        skip_txt = findViewById(R.id.skip_txt);
        btnlogin = (Button) findViewById(R.id.btn);
        tvreg = (TextView) findViewById(R.id.tvreg);

        skip_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(LoginActivity.this, "Opps! Module is Not Ready.", Toast.LENGTH_SHORT).show();
            }
        });

        tvreg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, Register.class);
                startActivity(intent);
                LoginActivity.this.finish();
            }
        });


        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email = etUname.getText().toString().trim();
                password = etPass.getText().toString().trim();

                Log.d("userdata", "onClick: " + email + password);

                if (!email.isEmpty() && Patterns.EMAIL_ADDRESS.matcher(email).matches()) {

                    if (!password.isEmpty() && pattern_pwd.matcher(password).matches()) {

                        loginUser();

                    } else {
                        Snackbar.make(rl_pwd, "Enter the Valid Password", Snackbar.LENGTH_SHORT).show();
                    }
                } else {
                    Snackbar.make(ll_lay, "Enter the Valid Email", Snackbar.LENGTH_SHORT).show();
                }

            }
        });
    }

    protected void setStatusBarGradiant(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = activity.getWindow();
            Drawable background = activity.getResources().getDrawable(R.drawable.ic_gradient_bg);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(activity.getResources().getColor(android.R.color.transparent));
            window.setBackgroundDrawable(background);
        }
    }

    private void loginUser() {
        API api = RestAdapter.createAPI();

//        API service = RetrofitClient.getRetrofitInstance().create(API.class);
        Log.d("call_call", "onClick: " + email + password);
        Call<LoginUserResponse> call = api.getUserLogin(email, password);
        call.enqueue(new Callback<LoginUserResponse>() {
            @Override
            public void onResponse(Call<LoginUserResponse> call, Response<LoginUserResponse> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {

                        Log.i("onSuccess", response.body().loginresponse.getEmail());
                        userid = response.body().loginresponse.user_id;
                        useremail = response.body().loginresponse.email;
                        userfname = response.body().loginresponse.first_name;
                        userlname = response.body().loginresponse.last_name;

                        try {
                            parseLoginData();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        Log.i("onEmptyResponse", "Returned empty response");//Toast.makeText(getContext(),"Nothing returned",Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<LoginUserResponse> call, Throwable t) {
                Log.d("LoginBHai_error", "onFailure: " + "Throw" + t.toString());
            }
        });

    }

    private void parseLoginData() {
        try {
            if (!useremail.isEmpty()) {
                saveInfo();
            } else {
                Log.d("loginresponse", "empty");
            }
            Toast.makeText(LoginActivity.this, "Login Successfully!", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(LoginActivity.this, HomeScreen.class);
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

                Intent intent = new Intent(LoginActivity.this, HomeScreen.class);
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
