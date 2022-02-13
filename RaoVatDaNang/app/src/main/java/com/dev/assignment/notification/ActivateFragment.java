package com.dev.assignment.notification;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dev.assignment.R;


public class ActivateFragment extends Fragment {
    private View mView;

    public ActivateFragment() {
        // Required empty public constructor
    }

    public static ActivateFragment newInstance(String param1, String param2) {
        ActivateFragment fragment = new ActivateFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView =  inflater.inflate(R.layout.fragment_activate, container, false);
        // Code here!

        return mView;
    }
}