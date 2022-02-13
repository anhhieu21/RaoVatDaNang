package com.dev.assignment.notification;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

public class NotiViewPagerAdapter extends FragmentStatePagerAdapter {
    public NotiViewPagerAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return new ActivateFragment();
            case 1:
                return new NewsFragment();
            default:
                return new ActivateFragment();
        }

    }

    @Override
    public int getCount() {
        return 2;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position){
            case 0:
                return "HOẠT ĐỘNG";
            case 1:
                return "TIN TỨC";
            default:
                return "HOẠT ĐỘNG";
        }
    }
}
