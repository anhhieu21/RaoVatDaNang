package com.dev.assignment.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dev.assignment.R;
import com.dev.assignment.adapter.PostManagerViewPager2Adapter;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;


public class PostManagerFragment extends Fragment {

    private ViewPager2 mVp;
    private TabLayout mTl;
    public PostManagerFragment() {
        // Required empty public constructor
    }


    public static PostManagerFragment newInstance(String param1, String param2) {
        PostManagerFragment fragment = new PostManagerFragment();
        return fragment;
    }

    @Override
    public void onViewCreated(@NonNull  View view, @Nullable  Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mVp= view.findViewById(R.id.viewPager2);
        mTl= view.findViewById(R.id.tabLayout);
        PostManagerViewPager2Adapter adapter = new PostManagerViewPager2Adapter(getActivity());
        mVp.setAdapter(adapter);
        new TabLayoutMediator(mTl, mVp, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                if (position==0){
                    tab.setText("TIN ĐANG HIỂN THỊ");

                }else {
                    tab.setText("TIN BỊ TỪ CHỐI");
                }
            }
        }).attach();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_post_manager, container, false);

        BottomNavigationView navBar = getActivity().findViewById(R.id.nvgMain);
        navBar.setVisibility(View.VISIBLE);

        return view;
    }
}