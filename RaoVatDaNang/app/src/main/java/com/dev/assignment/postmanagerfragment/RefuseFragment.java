package com.dev.assignment.postmanagerfragment;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.dev.assignment.Constants;
import com.dev.assignment.Model.DataProduct;
import com.dev.assignment.R;
import com.dev.assignment.adapter.PostDisplayAdapter;
import com.dev.assignment.fragment.PostDisplayFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class RefuseFragment extends Fragment {
    RecyclerView rcvPostDisplay;
    ArrayList<DataProduct> arrayList;
    PostDisplayAdapter postDisplayAdapter;
    private RefuseViewModel mViewModel;

    public RefuseFragment() {
        // Required empty public constructor
    }

    public static RefuseFragment newInstance() {
        RefuseFragment fragment = new RefuseFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.refuse_fragment, container, false);

        BottomNavigationView navBar = getActivity().findViewById(R.id.nvgMain);
        navBar.setVisibility(View.VISIBLE);

        rcvPostDisplay = view.findViewById(R.id.rcvPostDisplay);
        arrayList = new ArrayList<>();

        extract();

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(RefuseViewModel.class);
        // TODO: Use the ViewModel
    }

    private void extract() {
        String url = Constants.URL_GET_RP_LOCK+Constants.KEY_IDUSER;
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                for (int i = 0; i < response.length(); i++) {
                    JSONObject petObject = null;
                    try {
                        petObject = response.getJSONObject(i);

                        DataProduct dataPet = new DataProduct();
                        dataPet.setProductId(Integer.parseInt(petObject.getString("productId")));
                        dataPet.setName(petObject.getString("name"));
                        dataPet.setPrice(petObject.getInt("price"));
                        dataPet.setType1(petObject.getString("type1"));
                        dataPet.setType2(petObject.getString("type2"));
                        dataPet.setImage_1(petObject.getString("image_1"));
                        dataPet.setImage_2(petObject.getString("image_2"));
                        dataPet.setDetail(petObject.getString("detail"));
                        dataPet.setStatus(petObject.getString("status"));
                        dataPet.setLockPr(Integer.parseInt(petObject.getString("lockPr")));
                        dataPet.setIdUser(Integer.parseInt(petObject.getString("idUser")));
                        arrayList.add(dataPet);
                        Log.e("115", dataPet.getImage_1() + "");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
                rcvPostDisplay.setHasFixedSize(true);
                LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
                rcvPostDisplay.setLayoutManager(layoutManager);
                //Divider
//                DividerItemDecoration d = new DividerItemDecoration(getActivity(),layoutManager.getOrientation());
//                rcvPostDisplay.addItemDecoration(d);

                PostDisplayAdapter postDisplayAdapter = new PostDisplayAdapter(arrayList, getActivity().getApplicationContext());
                rcvPostDisplay.setAdapter(postDisplayAdapter);
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                Log.d("tag", "onErrorResponse: " + error.getMessage());
                Toast.makeText(getActivity(), "onErrorResponse: " + error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

        queue.add(jsonArrayRequest);
    }
}