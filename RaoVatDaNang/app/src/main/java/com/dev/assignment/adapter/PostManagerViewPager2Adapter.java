package com.dev.assignment.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.dev.assignment.fragment.PostDisplayFragment;
import com.dev.assignment.postmanagerfragment.DisplayFragment;
import com.dev.assignment.postmanagerfragment.RefuseFragment;

public class PostManagerViewPager2Adapter extends FragmentStateAdapter {

    public PostManagerViewPager2Adapter(@NonNull  FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull

    @Override
    public Fragment createFragment(int position) {
        Fragment fragment;
        if (position==0){
            fragment = PostDisplayFragment.newInstance();
        }else {
            fragment = RefuseFragment.newInstance();
        }
        return fragment;
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
