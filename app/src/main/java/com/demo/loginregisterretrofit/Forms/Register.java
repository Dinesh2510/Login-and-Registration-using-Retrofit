package com.demo.loginregisterretrofit.Forms;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;

import com.demo.loginregisterretrofit.HomeScreen;
import com.demo.loginregisterretrofit.R;
import com.demo.loginregisterretrofit.Response.API;
import com.demo.loginregisterretrofit.Response.RegsiterResponse;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import android.content.Intent;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import org.json.JSONException;
import org.json.JSONObject;

import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

import static com.demo.loginregisterretrofit.Response.API.BaseURL;


public class Register extends AppCompatActivity {
    private EditText et_fname, et_lname, et_email, etpassword;
    private FloatingActionButton btnregister;
    private TextView tvlogin;
    String firstName;
    String lastName;
    String email;
    String password;
    LinearLayout lyt_linear;
    ImageView u_image;
    Pattern pattern_pwd = Pattern.compile("^[a-zA-Z0-9]+$");

    public Register() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStatusBarGradiant(this);
        setContentView(R.layout.activity_register);

        et_fname = (EditText) findViewById(R.id.et_fname);
        et_lname = (EditText) findViewById(R.id.et_lname);
        et_email = (EditText) findViewById(R.id.et_email);
        etpassword = (EditText) findViewById(R.id.etpassword);
        btnregister = findViewById(R.id.btn);
        u_image = findViewById(R.id.u_image);
        lyt_linear = findViewById(R.id.lyt_linear);
        tvlogin = (TextView) findViewById(R.id.tvlogin);

        tvlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Register.this, LoginActivity.class);
                startActivity(intent);
                Register.this.finish();
            }
        });
        u_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Register.this, LoginActivity.class);
                startActivity(intent);
                Register.this.finish();
            }
        });

        btnregister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firstName = et_fname.getText().toString();
                lastName = et_lname.getText().toString();
                email = et_email.getText().toString();
                password = etpassword.getText().toString();

                Log.d("userdata", "onClick: " + email + password);
                registerMe();
                //ata mla sang kai karu mla response blank yet ahi
                //response server ka bata pehle
//                if (!firstName.isEmpty()) {
//                    if (!lastName.isEmpty()) {
//                        if (!email.isEmpty() && Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
//
//                            if (!password.isEmpty() && pattern_pwd.matcher(password).matches()) {
//
//                            } else {
//                                Snackbar.make(lyt_linear, "Enter the Valid Password", Snackbar.LENGTH_SHORT).show();
//
//                            }
//                        } else {
//                            Snackbar.make(lyt_linear, "Enter the Valid Email", Snackbar.LENGTH_SHORT).show();
//
//                        }
//                    } else {
//                        Snackbar.make(lyt_linear, "Enter the Valid Last name", Snackbar.LENGTH_SHORT).show();
//
//                    }
//                } else {
//                    Snackbar.make(lyt_linear, "Enter the Valid First name", Snackbar.LENGTH_SHORT).show();
//
//                }

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

    private void registerMe() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BaseURL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        API api = retrofit.create(API.class);
        Call<RegsiterResponse> call = api.getUserRegi(firstName, lastName, email, password);
        call.enqueue(new Callback<RegsiterResponse>() {
            @Override
            public void onResponse(Call<RegsiterResponse> call, Response<RegsiterResponse> response) {
                Log.i("Responsestring", response.body().toString());
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        Log.d("responseLog", response.body().getResponse());
                        Log.i("onSuccess", response.body().toString());

                        String jsonresponse = response.body().getResponse();
                        try {
                            parseRegData(jsonresponse);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } else {
                        Log.i("onEmptyResponse", "Returned empty response");//Toast.makeText(getContext(),"Nothing returned",Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<RegsiterResponse> call, Throwable t) {
                Log.d("go", "onFailure: " + t.toString());
            }
        });
    }


    private void parseRegData(String response) throws JSONException {
        Log.d("juststring", response);
        if (response.equals("success")) {
            Toast.makeText(Register.this, "Registered Successfully!", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(Register.this, LoginActivity.class);
            finish();
            startActivity(intent);
        } else {
            Toast.makeText(Register.this, "OOPS", Toast.LENGTH_SHORT).show();
        }
    }

//    private void saveInfo(String response) {
//        try {
//            JSONObject jsonObject = new JSONObject(response);
//            if (jsonObject.getString("response").contains("username")) {
//                JSONArray dataArray = jsonObject.getJSONArray("response");
//                for (int i = 0; i < dataArray.length(); i++) {
//                    JSONObject dataobj = dataArray.getJSONObject(i);
//                    Log.d("TAG", "saveInfo: " + dataobj.toString());
//
//                    sharedPreferences = getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
//                    editor = sharedPreferences.edit();
//                    editor.putString(USER_ID, dataobj.getString("user_id"));
//                    editor.putString(NAME, dataobj.getString("name"));
//                    editor.putString(HOBBY, dataobj.getString("hobby"));
//                    editor.putString(PASSWORD, dataobj.getString("password"));
//                    editor.putString(USER_NAME, dataobj.getString("username"));
//                    editor.apply();
//                    Log.d("kon", "saveInfo: " + USER_NAME);
//
//                }
//                Toast.makeText(this, "Success", Toast.LENGTH_SHORT).show();
//            }
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//    }
}

