package com.dev.assignment.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dev.assignment.R;
import com.dev.assignment.activity.MainActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;

public class PostDisplayDetailFragment extends Fragment {

    TextView tvName, tvPrice, tvDetail, tvId, tvType1, tvType2;
    ImageView imv, imvBack;
    LinearLayout postUpdate;

    public PostDisplayDetailFragment() {
        // Required empty public constructor
    }

    public static PostDisplayDetailFragment newInstance(String param1, String param2) {
        PostDisplayDetailFragment fragment = new PostDisplayDetailFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_post_display_detail, container, false);
//        toolbar = view.findViewById(R.id.toolbar);
        tvId = view.findViewById(R.id.tvId);
        tvType1 = view.findViewById(R.id.tvType1);
        tvType2 = view.findViewById(R.id.tvType2);
        tvName = view.findViewById(R.id.tv_name);
        tvPrice = view.findViewById(R.id.tv_price);
        imv = view.findViewById(R.id.imv);
        postUpdate = view.findViewById(R.id.postUpdate);
        tvDetail = view.findViewById(R.id.tvDetail);
        imvBack = view.findViewById(R.id.imvBack);


        imvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFragmentManager().beginTransaction()
                        .replace(R.id.flyMain, new PostDisplayFragment())
                        .addToBackStack(null).commit();
            }
        });
        BottomNavigationView navBar = getActivity().findViewById(R.id.nvgMain);
        navBar.setVisibility(View.GONE);


        Bundle bundle = getArguments();
        if (bundle != null) {
            tvId.setText(bundle.getString("id"));
            tvType1.setText(bundle.getString("type1"));
            tvType2.setText(bundle.getString("type2"));
            tvName.setText(bundle.getString("name"));
            DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
            tvPrice.setText(decimalFormat.format(bundle.getInt("price")) + " đ");
            Picasso.get().load(bundle.getString("image_1")).into(imv);
            tvDetail.setText(bundle.getString("detail"));
        }


        String id = tvId.getText().toString();
        String type1 = tvType1.getText().toString();
        String type2 = tvType2.getText().toString();
        String name = tvName.getText().toString();
        String price = tvPrice.getText().toString().replaceAll("[^0-9]", "");
        String detail = tvDetail.getText().toString();
        postUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PostUpdateFragment postUpdateFragment = new PostUpdateFragment();
                Bundle bundle = new Bundle();
                bundle.putString("id", id);
                bundle.putString("type1", type1);
                bundle.putString("type2", type2);
                bundle.putString("name", name);
                bundle.putString("price", price);
                bundle.putString("detail", detail);
                postUpdateFragment.setArguments(bundle);

                AppCompatActivity activity = (AppCompatActivity) view.getContext();
                activity.getSupportFragmentManager().beginTransaction().
                        replace(R.id.flyMain, postUpdateFragment).addToBackStack(null).commit();
            }
        });
        return view;
    }
}