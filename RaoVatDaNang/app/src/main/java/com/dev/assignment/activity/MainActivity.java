package com.dev.assignment.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.dev.assignment.R;
import com.dev.assignment.fragment.DetailFragment;
import com.dev.assignment.fragment.HomeFragment;
import com.dev.assignment.fragment.NotificationFragment;
import com.dev.assignment.fragment.PostFragment;
import com.dev.assignment.fragment.PostManagerFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    private FrameLayout frameLayoutMain;
    private BottomNavigationView navigationViewMain;
    public static String URL = "http://192.168.0.106:2004";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();

        navigationViewMain.setOnNavigationItemSelectedListener(listener);
        loadFragment(new HomeFragment());
    }


    private BottomNavigationView.OnNavigationItemSelectedListener listener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {


            Fragment fragment;
            switch (item.getItemId()) {
                case R.id.frmHome: {
                    loadFragment(new HomeFragment());
                    return true;
                }
                case R.id.frmManagerPost: {
                    loadFragment(new PostManagerFragment());
                    return true;
                }
                case R.id.frmPost: {
                    loadFragment(new PostFragment());
                    return true;
                }
                case R.id.frmNotification: {
                    loadFragment(new NotificationFragment());
                    return true;
                }
                case R.id.frmDetail: {
                    loadFragment(new DetailFragment());
                    return true;
                }
            }

            return false;
        }
    };

    private void loadFragment(Fragment fragment) {
        // load fragment
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.flyMain, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private void initView() {
        frameLayoutMain = findViewById(R.id.flyMain);
        navigationViewMain = findViewById(R.id.nvgMain);
    }
}