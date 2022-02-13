package com.dev.assignment.fragment;

import android.content.Context;
import android.os.Bundle;

import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import com.dev.assignment.adapter.PetAdapter;
import com.dev.assignment.adapter.ProductAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class StationeryFragment extends Fragment {
    RecyclerView recyclerView_Product;
    ArrayList<DataProduct> arrayList;

    private Context context;
    public StationeryFragment() {
        // Required empty public constructor
    }

    public StationeryFragment(ArrayList<DataProduct> arrayList, Context context) {
        this.arrayList = arrayList;
        this.context = context;
    }

    public static StationeryFragment newInstance(String param1, String param2) {
        StationeryFragment fragment = new StationeryFragment();
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
        View view = inflater.inflate(R.layout.fragment_stationery, container, false);
        mapView(view);
        extractProduct();
        initSearchView(view);
        return view;
    }
    private void mapView(View view){
        recyclerView_Product =view.findViewById(R.id.rcvStationery);
        arrayList = new ArrayList<>();
    }
    private void extractProduct() {
        String url = Constants.URL_GET_TYPE1+"Văn phòng phẩm";
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject jsonObject = response.getJSONObject(i);

                        DataProduct dataProduct = new DataProduct();
                        dataProduct.setProductId(Integer.parseInt(jsonObject.getString("productId")));
                        dataProduct.setName(jsonObject.getString("name"));
                        dataProduct.setPrice(jsonObject.getInt("price"));
                        dataProduct.setType1(jsonObject.getString("type1"));
                        dataProduct.setType2(jsonObject.getString("type2"));
                        dataProduct.setImage_1(jsonObject.getString("image_1"));
                        dataProduct.setDetail(jsonObject.getString("detail"));
                        dataProduct.setStatus(jsonObject.getString("status"));
                        dataProduct.setLockPr(Integer.parseInt(jsonObject.getString("lockPr")));
                        dataProduct.setIdUser(Integer.parseInt(jsonObject.getString("idUser")));
                        arrayList.add(dataProduct);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }

                recyclerView_Product.setHasFixedSize(true);
                LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
                recyclerView_Product.setLayoutManager(layoutManager);
                //Divider
                DividerItemDecoration d = new DividerItemDecoration(getActivity(),layoutManager.getOrientation());
                recyclerView_Product.addItemDecoration(d);
                PetAdapter petAdapter = new PetAdapter(arrayList, getActivity().getApplicationContext());
                recyclerView_Product.setAdapter(petAdapter);
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
    private void initSearchView(View view) {
        SearchView searchView = (SearchView) view.findViewById(R.id.search);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                ArrayList<DataProduct> filterProduct = new ArrayList<DataProduct>();
                for (DataProduct dataProduct : arrayList) {
                    if (dataProduct.getName().toLowerCase().contains(query.toLowerCase())) {
                        filterProduct.add(dataProduct);
                    }
                }
                recyclerView_Product.setHasFixedSize(true);
                LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
                recyclerView_Product.setLayoutManager(layoutManager);
                //Divider
                DividerItemDecoration d = new DividerItemDecoration(getActivity(), layoutManager.getOrientation());
                recyclerView_Product.addItemDecoration(d);
                PetAdapter adapter = new PetAdapter(filterProduct, context);
                recyclerView_Product.setAdapter(adapter);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                recyclerView_Product.setHasFixedSize(true);
                LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
                recyclerView_Product.setLayoutManager(layoutManager);
                //Divider
                DividerItemDecoration d = new DividerItemDecoration(getActivity(), layoutManager.getOrientation());
                recyclerView_Product.addItemDecoration(d);

                PetAdapter petAdapter = new PetAdapter(arrayList, getActivity().getApplicationContext());
                recyclerView_Product.setAdapter(petAdapter);
                return false;
            }
        });
    }

}