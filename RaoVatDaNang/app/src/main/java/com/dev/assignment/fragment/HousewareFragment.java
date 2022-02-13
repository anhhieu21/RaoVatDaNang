package com.dev.assignment.fragment;

import android.content.Context;
import android.os.Bundle;

import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.dev.assignment.Constants;
import com.dev.assignment.Houseware.Houseware;
import com.dev.assignment.Houseware.HousewareAdapter;
import com.dev.assignment.Model.DataProduct;
import com.dev.assignment.R;
import com.dev.assignment.activity.MainActivity;
import com.dev.assignment.adapter.PetAdapter;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class HousewareFragment extends Fragment {
    private View mView;
    private RecyclerView rcvHouseware;
    private MainActivity mMainActivity;
    RecyclerView recyclerView_Furniture;
    ArrayList<DataProduct> arrayList;
    ImageView ivTable, ivBed, ivStove, ivMore, ivCabinet;
    String type2;
    private Context context;

    public HousewareFragment() {
        // Required empty public constructor
    }

    public HousewareFragment(ArrayList<DataProduct> arrayList, Context context) {
        this.arrayList = arrayList;
        this.context = context;
    }

    public static HousewareFragment newInstance(String param1, String param2) {
        HousewareFragment fragment = new HousewareFragment();
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
        mView = inflater.inflate(R.layout.fragment_houseware, container, false);
        extractFurniture();
        mapView(mView);
        onClick();
        initSearchView(mView);
        mMainActivity = (MainActivity) getActivity();
        return mView;
    }

    private void mapView(View view) {
        BottomNavigationView navBar = getActivity().findViewById(R.id.nvgMain);
        navBar.setVisibility(View.VISIBLE);
        rcvHouseware = mView.findViewById(R.id.rcv_houseware);
        recyclerView_Furniture = mView.findViewById(R.id.recyclerView_Furniture);
        arrayList = new ArrayList<>();
        ivTable = view.findViewById(R.id.ivTable);
        ivBed = view.findViewById(R.id.ivBed);
        ivStove = view.findViewById(R.id.ivStove);
        ivMore = view.findViewById(R.id.ivMore);
        ivCabinet = view.findViewById(R.id.ivCabinet);
    }

    private void extractFurniture() {
        String url = Constants.URL_GET_TYPE1 + "Nội thất";
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                for (int i = 0; i < response.length(); i++) {
                    JSONObject jsonObject = null;
                    try {
                        jsonObject = response.getJSONObject(i);
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
                recyclerView_Furniture.setHasFixedSize(true);
                LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
                recyclerView_Furniture.setLayoutManager(layoutManager);
                //Divider
                DividerItemDecoration d = new DividerItemDecoration(getActivity(), layoutManager.getOrientation());
                recyclerView_Furniture.addItemDecoration(d);

                PetAdapter productAdapter = new PetAdapter(arrayList, getActivity().getApplicationContext());
                recyclerView_Furniture.setAdapter(productAdapter);
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
                recyclerView_Furniture.setHasFixedSize(true);
                LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
                recyclerView_Furniture.setLayoutManager(layoutManager);
                //Divider
                DividerItemDecoration d = new DividerItemDecoration(getActivity(), layoutManager.getOrientation());
                recyclerView_Furniture.addItemDecoration(d);
                PetAdapter adapter = new PetAdapter(filterProduct, context);
                recyclerView_Furniture.setAdapter(adapter);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                recyclerView_Furniture.setHasFixedSize(true);
                LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
                recyclerView_Furniture.setLayoutManager(layoutManager);
                //Divider
                DividerItemDecoration d = new DividerItemDecoration(getActivity(), layoutManager.getOrientation());
                recyclerView_Furniture.addItemDecoration(d);

                PetAdapter petAdapter = new PetAdapter(arrayList, getActivity().getApplicationContext());
                recyclerView_Furniture.setAdapter(petAdapter);
                return false;
            }
        });
    }

    private void onClick() {
        ivTable.setOnClickListener(view -> {
            type2 = "Bàn ghế";
            Constants.TYPE2 = type2;
            getFragmentManager().beginTransaction()
                    .replace(R.id.flyMain, new Type2VehicleFragment())
                    .addToBackStack(null).commit();
        });
        ivBed.setOnClickListener(view -> {
            type2 = "Giường, chăn ga gối nệm";
            Constants.TYPE2 = type2;
            getFragmentManager().beginTransaction()
                    .replace(R.id.flyMain, new Type2VehicleFragment())
                    .addToBackStack(null).commit();
        });
        ivStove.setOnClickListener(view -> {
            type2 = "Dụng cụ nhà bếp";
            Constants.TYPE2 = type2;
            getFragmentManager().beginTransaction()
                    .replace(R.id.flyMain, new Type2VehicleFragment())
                    .addToBackStack(null).commit();
        });
        ivCabinet.setOnClickListener(view -> {
            type2 = "Tủ, kệ gia đình";
            Constants.TYPE2 = type2;
            getFragmentManager().beginTransaction()
                    .replace(R.id.flyMain, new Type2VehicleFragment())
                    .addToBackStack(null).commit();
        });
        ivMore.setOnClickListener(view -> {
            type2 = "Nội thất, đồ gia dụng khác";
            Constants.TYPE2 = type2;
            getFragmentManager().beginTransaction()
                    .replace(R.id.flyMain, new Type2VehicleFragment())
                    .addToBackStack(null).commit();
        });

    }
}