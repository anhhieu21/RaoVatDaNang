package com.dev.assignment.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dev.assignment.R;


public class Type2ElectDFragment extends Fragment {


    public Type2ElectDFragment() {
        // Required empty public constructor
    }

    public static Type2ElectDFragment newInstance(String param1, String param2) {
        Type2ElectDFragment fragment = new Type2ElectDFragment();
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
        View view= inflater.inflate(R.layout.fragment_type2_elect_d, container, false);
        return view;
    }
}