package com.dev.assignment.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.dev.assignment.Constants;
import com.dev.assignment.R;
import com.squareup.picasso.Picasso;

public class ProfileUser extends AppCompatActivity {
    TextView tvName, tvUpdate, tvCreatedAt,tvAddress;
    ImageView ivAvatar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_user);
        mapView();
        onCLick();
    }

    private void mapView() {
        tvName = findViewById(R.id.tvName);
        tvName.setText(Constants.NAME_YOUR);
        tvUpdate = findViewById(R.id.tvupdateuser);
        tvAddress = findViewById(R.id.tvAddress);
        ivAvatar = findViewById(R.id.ivAvatar);
        tvCreatedAt = findViewById(R.id.tvCreatedAt);
        tvCreatedAt.setText("Ngày tham gia: "+Constants.CREATE_AC_YOUR);
        Picasso.get().load(Constants.URL_AVATAR_YOUR).into(ivAvatar);
        tvAddress.setText("Địa chỉ: "+Constants.ADDRESS_YOUR);
    }

    private void onCLick() {
        tvUpdate.setOnClickListener(view -> {
            Intent intent = new Intent(getApplication(), UpdateUsers.class);
            startActivity(intent);
        });
    }
}