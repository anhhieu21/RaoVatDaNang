package com.dev.assignment.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.dev.assignment.Constants;
import com.dev.assignment.R;
import com.dev.assignment.activity.FavoriteActivity;
import com.dev.assignment.activity.LoginActivity;
import com.dev.assignment.activity.ProfileUser;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class DetailFragment extends Fragment {
    TextView tvFavorite, tvName;
    private Context context;
    Button btnLogout;
    LinearLayout lnDetailProfile;
    ImageView ivAvatar;

    public DetailFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    public static DetailFragment newInstance() {
        DetailFragment fragment = new DetailFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detail, container, false);

        BottomNavigationView navBar = getActivity().findViewById(R.id.nvgMain);
        navBar.setVisibility(View.VISIBLE);
        mapView(view);
        onClick();
        showData();
        return view;
    }

    private void mapView(View view) {
        tvFavorite = view.findViewById(R.id.lnFavorite);
        btnLogout = view.findViewById(R.id.btnLogout);
        tvName = view.findViewById(R.id.tvName);
        tvName.setText(Constants.NAME_YOUR);
        lnDetailProfile = view.findViewById(R.id.lnDetailProfile);
        ivAvatar = view.findViewById(R.id.ivAvatar);
    }

    private void onClick() {
        lnDetailProfile.setOnClickListener(view -> {
            Intent intent = new Intent(getActivity(), ProfileUser.class);
            startActivity(intent);
        });
        tvFavorite.setOnClickListener(view1 -> {
            Intent intent = new Intent(getActivity(), FavoriteActivity.class);
            startActivity(intent);
        });
        btnLogout.setOnClickListener(view -> {
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            startActivity(intent);
            getActivity().finish();
        });
    }

    private void showData() {
        String url = "http://192.168.0.106:2004/users/getUserById";
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("136", response + "");
                try {
                    JSONObject jObj = null;
                    JSONObject jsonObject = new JSONObject(response);
                    jObj = jsonObject.getJSONObject("user");
                    String url_ig = jObj.getString("avatar");
                    String date = jObj.getString("createdAt");
                    Constants.CREATE_AC_YOUR=date;
                    Picasso.get().load(url_ig).into(ivAvatar);
                    Constants.URL_AVATAR_YOUR = url_ig;

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Log.e("135", error + "");
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("id", Constants.KEY_IDUSER);
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type", "application/x-www-form-urlencoded");
                return params;
            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);
    }
}