package com.dev.assignment.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.dev.assignment.R;
import com.dev.assignment.apiRegister.ApiClienRegister;
import com.dev.assignment.apiRegister.RegisterReponse;
import com.dev.assignment.apiRegister.RegisterRequest;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.Arrays;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {
    private EditText edName, edEmail, edPhone, edPassword, edPassword2, edMaso;
    private Button btnSigup;
    private TextInputLayout ipAddress;
    private Spinner spAddress;
    private TextView tvSigup;
    private ArrayList<String> arrAddress;
    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        edName = (EditText) findViewById(R.id.Ed_name);
//        edEmail= (EditText)findViewById(R.id.Ed_email);
        edPhone = (EditText) findViewById(R.id.Ed_phone);
        spAddress = (Spinner) findViewById(R.id.Sp_address);
        ipAddress = (TextInputLayout) findViewById(R.id.Ip_address);
//        edMaso=(EditText) findViewById(R.id.Ed_code);
        edPassword = (EditText) findViewById(R.id.Ed_password);
        edPassword2 = (EditText) findViewById(R.id.Ed_password2);
        btnSigup = (Button) findViewById(R.id.signup);
        spAddress = (Spinner) findViewById(R.id.Sp_address);
        tvSigup = (TextView) findViewById(R.id.TV_signup);
        arrAddress = new ArrayList<>();
        arrAddress.addAll(Arrays.asList("Quận Hải Châu", "Quận Liên Chiểu", "Quận Cẩm Lệ",
                "Quận Thanh Khê", "Quận Ngũ Hành Sơn", "Quận Sơn Trà", "Huyện Hòa Vang", "Huyện Hoàng Sa"));
        adapter = new ArrayAdapter<String>(RegisterActivity.this, android.R.layout.simple_spinner_item, arrAddress);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spAddress.setAdapter(adapter);
        //chuyển màn hình login
        tvSigup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
        //đăng kí
        btnSigup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //get loi
                String strname = edName.getText().toString();
//                String stremail = edEmail.getText().toString();
                String strphone = edPhone.getText().toString();
//                String straddress = ipAddress.getText().toString();
                String strpassword = edPassword.getText().toString();
                String strpassword2 = edPassword2.getText().toString();
//                String strcode=edMaso.getText().toString();

                //loi khi rong
                if (TextUtils.isEmpty(strname)
                        && TextUtils.isEmpty(strphone)
                        && TextUtils.isEmpty(strpassword)
                        && TextUtils.isEmpty(strpassword2)) {
                    edName.setError("Vui lòng nhập Name");
//                   edEmail.setError("Vui lòng nhập Email");
                    edPhone.setError("Vui lòng nhập Phone");
//                   spAddress.setError("Vui lòng nhập Address");
                    edPassword.setError("Vui lòng nhập Password");
                    edPassword2.setError("Vui lòng nhập lại Password");
                    Toast.makeText(RegisterActivity.this, "Đăng kí thất bại", Toast.LENGTH_LONG).show();
                } else if (edName.getText().toString().length() < 5) {
                    edName.setError("Name trên 5 ký tự");
                    Toast.makeText(RegisterActivity.this, "Đăng kí thất bại", Toast.LENGTH_LONG).show();
                }
                //k chưa ký tự dac biet name
                else if (!edName.getText().toString().matches("[a-z,A-Z,0-9]*")) {
                    edName.setError("Name không chứa kí tự đặc biệt");
                    Toast.makeText(RegisterActivity.this, "Đăng kí thất bại", Toast.LENGTH_LONG).show();
                }
//               else if (!isEmailValid(stremail)){
//                   edEmail.setError("Email phải đúng định dạng");
//                   Toast.makeText(RegisterActivity.this,"Đăng kí thất bại",Toast.LENGTH_LONG).show();
//               }
                else if (!isValidPhoneNumber(strphone)) {
                    edPhone.setError("Vui lòng nhập đúng SDT của bạn");
                    Toast.makeText(RegisterActivity.this, "Đăng kí thất bại", Toast.LENGTH_LONG).show();
                }
//               else if (TextUtils.isEmpty(straddress)){
//                   edAddress.setError("Vui lòng nhập Address");
//                   Toast.makeText(RegisterActivity.this,"Đăng kí thất bại",Toast.LENGTH_LONG).show();
//               }
                else if (edPassword.getText().toString().length() < 6) {
                    edPassword.setError("PassWord trên 6 ký tự");
                    Toast.makeText(RegisterActivity.this, "Đăng kí thất bại", Toast.LENGTH_LONG).show();
                }
                //xu lý nhạp lại mat khau 2
                else if (strpassword.equals(strpassword2)) {
                    startRegister(createRegister());
                } else {
                    edPassword2.setError("Bạn phải nhập đúng PassWord đã nhập trước đó");
                    Toast.makeText(RegisterActivity.this, "Đăng kí thất bại", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    //ham check phone
    public final static boolean isValidPhoneNumber(CharSequence target) {
        if (target == null || target.length() < 6 || target.length() > 13) {
            return false;
        } else {
            return android.util.Patterns.PHONE.matcher(target).matches();
        }

    }

    public RegisterRequest createRegister() {
        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setName(edName.getText().toString());
        registerRequest.setPhone(edPhone.getText().toString());
        registerRequest.setAddress(spAddress.getSelectedItem().toString());
        registerRequest.setPassword(edPassword.getText().toString());
        return registerRequest;
    }

    //dang ki
    private void startRegister(RegisterRequest request) {
        Call<RegisterReponse> reponseCall = ApiClienRegister.getApiRegisterInterface().registerUserApi(request);
        reponseCall.enqueue(new Callback<RegisterReponse>() {
            @Override
            public void onResponse(Call<RegisterReponse> call, Response<RegisterReponse> response) {
                if (response.isSuccessful()) {
                    RegisterReponse rpse = response.body();
                    Toast.makeText(RegisterActivity.this, rpse.getMessage(), Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(getApplication(),LoginActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(RegisterActivity.this, "Đăng kí thất bại", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<RegisterReponse> call, Throwable t) {

            }
        });

    }


}