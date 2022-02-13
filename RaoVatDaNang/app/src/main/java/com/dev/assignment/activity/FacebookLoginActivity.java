package com.dev.assignment.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.dev.assignment.apiRegister.RegisterReponse;
import com.dev.assignment.itemFB.ApiLoginfb;
import com.dev.assignment.itemFB.FacebookEmail;
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.appevents.AppEventsLogger;


import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.dev.assignment.R;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.gson.JsonIOException;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Array;
import java.util.Arrays;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FacebookLoginActivity extends AppCompatActivity {
    private CallbackManager callbackManager;
    private LoginButton loginButton;
    private String email, name, id;

    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext()); // thiếu cái ni
        setContentView(R.layout.activity_facebook_login);
        loginButton = findViewById(R.id.login_button);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Vui lòng chờ...");
        callbackManager = CallbackManager.Factory.create();
        loginButton.setPermissions(Arrays.asList("user_gender,user_friends"));
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                loginButton.setVisibility(View.INVISIBLE);
                result();
                if (checkLogoutfb() < 0) {
                    LoginManager.getInstance().logOut();

                }
            }

            // cái fb ni có phải cái nớ k chịu á khi nay chạy dc chơ phài t có sửa chi dâu k thêm cai log out bo loi luon
            @Override
            public void onCancel() {
                Log.d("demo", "Login successfull");
            }

            @Override
            public void onError(@NonNull FacebookException e) {
                Log.d("demo", "Login successfull");
            }
        });


    }

    public void loginFB() {
        FacebookEmail facebookEmail = new FacebookEmail(email, name);
        ApiLoginfb.apiLoginfb.loginfb(facebookEmail).enqueue(new Callback<FacebookEmail>() {
            @Override
            public void onResponse(Call<FacebookEmail> call, Response<FacebookEmail> response) {
                Intent intent = new Intent(FacebookLoginActivity.this, MainActivity.class);
                startActivity(intent);
                progressDialog.show();
            }

            @Override
            public void onFailure(Call<FacebookEmail> call, Throwable t) {

            }
        });
    }

    //ok r ak ok thanhk tiền đây chứ thank gì :))Mà bị răng ma lỗi rứa
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) { ==> cái ni nên làm như dưới chứ @Override là ghi đè á ukm trc cung de rua ừm out đây ok
//        callbackManager.onActivityResult(requestCode, resultCode, data);
//        super.onActivityResult(requestCode, resultCode, data);
    public void result() {
        GraphRequest graphRequest = GraphRequest.newMeRequest(AccessToken.getCurrentAccessToken(),
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(@Nullable JSONObject jsonObject, @Nullable GraphResponse graphResponse) {
                        Log.d("Demo", jsonObject.toString());
                        try {
                            name = jsonObject.getString("name");
                            email = jsonObject.getString("email");
                            id = jsonObject.getString("id");
                            //    tvname.setText(name);
                            //    tvemail.setText(email);
                            //    Picasso.get().load("https://graph.Facebook.com/"+id+"/picture?type=large")
                            //            .into(img);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        loginFB();
                        RememberUserFB(email);
                    }
                });
        Bundle bundle = new Bundle();
        bundle.putString("fields", "gender , name ,email ,id , first_name , last_name");
        graphRequest.setParameters(bundle);
        graphRequest.executeAsync();
    }

    public void RememberUserFB(String email) {
        SharedPreferences sharedPreferences = getSharedPreferences("USERFBFILE", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("EMAIL", email);
        editor.commit();
    }

    public int checkLogoutfb() {
        SharedPreferences sharedPreferences = getSharedPreferences("USERFBFILE", MODE_PRIVATE);
        String chk = sharedPreferences.getString("EMAIL", "");
        if (chk != "") {
            return 1;
        }
        return -1;
    }

    AccessTokenTracker accessTokenTracker = new AccessTokenTracker() {
        @Override
        protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken) {
            if (currentAccessToken == null) {
                LoginManager.getInstance().logOut();
//                tvname.setText("");
//                tvemail.setText("");
//                img.setImageResource(0);
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        accessTokenTracker.stopTracking();
    }
}