package com.dev.assignment.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DownloadManager;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.dev.assignment.R;
import com.dev.assignment.apiRegister.ApiSeviceGmailOTPrgt;
import com.dev.assignment.apiRegister.RegisterEmail;
import com.dev.assignment.apiRegister.RegisterReponse;
import com.dev.assignment.apiRegister.RegisterRequest;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterOTPActivity extends AppCompatActivity {
    private EditText emailOtp;
    private Button btncheckMailRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_otpactivity);
        emailOtp=(EditText)findViewById(R.id.email_OTP);
        btncheckMailRegister=(Button)findViewById(R.id.btn_checkMailRgt);
        btncheckMailRegister.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                String stemail= emailOtp.getText().toString();
                if (TextUtils.isEmpty(stemail)){
                    emailOtp.setError("Vui lòng nhập email!!!");
                }else if (!isEmailValid(stemail)){
                    emailOtp.setError("Email không đúng định dạnh");
                }else {
                    sendVerifyEmail();
                }
            }
        });
    }
    public  static boolean isEmailValid(CharSequence email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private void sendVerifyEmail() {
        String stremail;
        stremail = emailOtp.getText().toString().trim();
        RegisterEmail registerEmail= new RegisterEmail(stremail);
        ApiSeviceGmailOTPrgt.apiSeviceGmailOtPrgt.checkEmailregister(registerEmail).enqueue(new Callback<RegisterReponse>() {
            @Override
            public void onResponse(Call<RegisterReponse> call, Response<RegisterReponse> response) {
                if (response.isSuccessful()){
                    RegisterReponse rpse = response.body();
                    Toast.makeText(RegisterOTPActivity.this, rpse.getMessage(), Toast.LENGTH_LONG).show();
                    Intent intent= new Intent(RegisterOTPActivity.this,CodeOtpRegisterActivity.class);
                    startActivity(intent);
                }else {
                    Toast.makeText(RegisterOTPActivity.this, "Sai gmail or trùng", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<RegisterReponse> call, Throwable t) {

            }
        });
    }
}