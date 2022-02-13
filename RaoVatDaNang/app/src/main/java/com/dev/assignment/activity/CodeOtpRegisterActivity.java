package com.dev.assignment.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dev.assignment.R;
import com.dev.assignment.apiRegister.ApiSeviceCode;
import com.dev.assignment.apiRegister.Maso;
import com.dev.assignment.apiRegister.RegisterReponse;
import com.dev.assignment.apiRegister.RegisterRequest;

import java.util.Locale;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CodeOtpRegisterActivity extends AppCompatActivity {
    private EditText code1, code2, code3, code4;
    private Button btnCheckCodeRegister;
    TextView tvNumberSecons;
    LinearLayout gropCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_code_otp_register);
        code1 = (EditText) findViewById(R.id.code1);
        code2 = (EditText) findViewById(R.id.code2);
        code3 = (EditText) findViewById(R.id.code3);
        code4 = (EditText) findViewById(R.id.code4);
        gropCode = findViewById(R.id.lnGropCode);
        tvNumberSecons = findViewById(R.id.tvSecons);
        long duration = TimeUnit.MINUTES.toMillis(1);
        new CountDownTimer(duration, 1000) {
            @Override
            public void onTick(long l) {
                String sDuration = String.format(Locale.ENGLISH, "%02d : %02d"
                        , TimeUnit.MILLISECONDS.toMinutes(l)
                        , TimeUnit.MILLISECONDS.toSeconds(l) -
                                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(l)));
                tvNumberSecons.setText(sDuration);
            }

            @Override
            public void onFinish() {
                gropCode.setVisibility(View.GONE);
                Toast.makeText(CodeOtpRegisterActivity.this, "Hết thời gian chờ,vui lòng gửi Email lại", Toast.LENGTH_LONG).show();

            }
        }.start();
        btnCheckCodeRegister = (Button) findViewById(R.id.btn_checkCode);
        code1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence s, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0) {
                    code2.requestFocus();
                }
            }
        });
        code2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence s, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0) {
                    code3.requestFocus();
                }
            }
        });
        code3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence s, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0) {
                    code4.requestFocus();
                }
            }
        });
        btnCheckCodeRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CheckCode();
            }
        });
    }

    private void CheckCode() {
        String inputcode;
        inputcode = code1.getText().toString().trim() +
                code2.getText().toString().trim() +
                code3.getText().toString().trim()
                + code4.getText().toString().trim();
        Maso maso = new Maso(inputcode);
        ApiSeviceCode.apiSeviceCode.checkcodeRegister(maso).enqueue(new Callback<RegisterReponse>() {
            @Override
            public void onResponse(Call<RegisterReponse> call, Response<RegisterReponse> response) {
                if (response.isSuccessful()) {
                    RegisterReponse rpse = response.body();
                    Toast.makeText(CodeOtpRegisterActivity.this, rpse.getMessage(), Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(CodeOtpRegisterActivity.this, RegisterActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(CodeOtpRegisterActivity.this, "Mã sai", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<RegisterReponse> call, Throwable t) {

            }
        });


    }

}