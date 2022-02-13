package com.dev.assignment.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.dev.assignment.R;
import com.dev.assignment.itemFB.ApiSeviceJoinFB;
import com.dev.assignment.itemFB.BackJson;
import com.dev.assignment.itemFB.FBemail;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProlifeUsersActivity extends AppCompatActivity {
    private String email1, avt;
    private TextView tvUser, tvgotoupdate;
    ImageView imgUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prolife_users);
        tvUser = findViewById(R.id.tvpruser);
        tvgotoupdate = findViewById(R.id.tvupdateuser);
        imgUser = findViewById(R.id.imgprUsrer);
        tvgotoupdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProlifeUsersActivity.this, UpdateUsers.class);
                startActivity(intent);

            }
        });
        if (checkLoginNumberupdate() > 0) {
//            cnEmail.setText(email1); //lam get api o doan ni neok lam di t thoat day
            FBemail fBemail = new FBemail(email1);
            ApiSeviceJoinFB.apiSeviceJoinFb.getDataFB(fBemail).enqueue(new Callback<BackJson>() {
                @Override
                public void onResponse(Call<BackJson> call, Response<BackJson> response) {
                    BackJson backJson = response.body();
                    tvUser.setText(backJson.getUser().getName());

                    avt = backJson.getUser().getAvatar();
                    Glide.with(ProlifeUsersActivity.this).load(avt).into(imgUser);

                }

                @Override
                public void onFailure(Call<BackJson> call, Throwable t) {
                }
            });
        }

    }

    public int checkLoginNumberupdate() {
        SharedPreferences sharedPreferences = getSharedPreferences("USERFBFILE", Context.MODE_PRIVATE);
        String ckmail = sharedPreferences.getString("EMAIL", "");
        if (ckmail != "") {
            email1 = sharedPreferences.getString("EMAIL", "");
            return 1;
        }
        return -1;
    }
}