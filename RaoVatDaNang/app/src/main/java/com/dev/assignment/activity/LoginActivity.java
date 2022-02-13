package com.dev.assignment.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.dev.assignment.Constants;
import com.dev.assignment.apiRegister.UserAPI;
import com.dev.assignment.Model.Status;
import com.dev.assignment.Model.User;
import com.dev.assignment.R;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class LoginActivity extends AppCompatActivity {
    private EditText edEmail, edPassword;
    private Button btnLogin;
    private TextView TV_signup;

    private Intent intent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        edEmail = findViewById(R.id.Ed_email);
        edPassword = findViewById(R.id.Ed_password);
        btnLogin = findViewById(R.id.login);

        TV_signup = findViewById(R.id.TV_signup);

        login();
        TV_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);

            }
        });
    }

    public void login() {
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = edEmail.getText().toString().trim();
                String password = edPassword.getText().toString().trim();
                // Kiêm tra ở đây này
                if (TextUtils.isEmpty(edEmail.getText().toString()) || TextUtils.isEmpty(edPassword.getText().toString())) {
                    Toast.makeText(LoginActivity.this, "Email / Password Required", Toast.LENGTH_LONG).show();
                } else {
                    //login
                }

                Retrofit retrofit = new Retrofit.Builder().baseUrl(MainActivity.URL).addConverterFactory(GsonConverterFactory.create()).build();
                UserAPI userAPI = retrofit.create(UserAPI.class);
                userAPI.login(new User(email, password)).enqueue(new Callback<Status>() {
                    @Override
                    public void onResponse(Call<Status> call, Response<Status> response) {
                        if (response.isSuccessful()) {
                            Status status = response.body();
                            if (status.isStatus()) {
                                String id = String.valueOf(status.getUser().getId());
                                String name = status.getUser().getName();
                                String address = status.getUser().getAddress();
                                Constants.ADDRESS_YOUR = address;
                                Constants.NAME_YOUR = name;
                                Constants.KEY_IDUSER = id;
                                String emailYour = status.getUser().getEmail();
                                Constants.EMAIL_YOUR=emailYour;
                                Log.e("201",id);
                                intent = new Intent(LoginActivity.this, MainActivity.class);
                                startActivity(intent);
                                finish();
                                Toast.makeText(LoginActivity.this, "Login Successful", Toast.LENGTH_LONG).show();
                            }
                        } else {
                            Toast.makeText(LoginActivity.this, "Login Failed", Toast.LENGTH_LONG).show();
                        }

                    }

                    @Override
                    public void onFailure(Call<Status> call, Throwable t) {
                        Log.e("2001",t+"");
                        Toast.makeText(LoginActivity.this, "Call Api Erro", Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
    }


}