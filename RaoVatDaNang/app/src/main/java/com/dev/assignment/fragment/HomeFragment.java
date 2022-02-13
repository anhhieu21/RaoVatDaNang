package com.dev.assignment.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.dev.assignment.Model.DataProduct;
import com.dev.assignment.R;
import com.dev.assignment.adapter.PetAdapter;
import com.dev.assignment.adapter.ProductAdapter;
import com.dev.assignment.adapter.SliderAdapter;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class HomeFragment extends Fragment {
    RecyclerView recyclerView_Product;
    ArrayList<DataProduct> arrayList;
    private static String JSON_URL = "http://192.168.0.106:6000/api/controllers/findAll";


    private Context context;
    SliderView sliderView;
    ImageView ivCar, ivElectD, ivFurniture, ivPet, ivStationery;
    int[] images = {
            R.mipmap.two,
            R.mipmap.three,
            R.mipmap.four
    };

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    public static HomeFragment newInstance() {
        HomeFragment fragment = new HomeFragment();
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
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        BottomNavigationView navBar = getActivity().findViewById(R.id.nvgMain);
        navBar.setVisibility(View.VISIBLE);
        mapView(view);
        slideView();
        slideCatalogue();
        onClick();
        extractProduct();
        initSearchView(view);
        return view;
    }


    public void mapView(View view) {
        sliderView = view.findViewById(R.id.image_slider);
        //image
        ivCar = view.findViewById(R.id.ivCar);
        ivElectD = view.findViewById(R.id.ivElectD);
        ivFurniture = view.findViewById(R.id.ivFurniture);
        ivPet = view.findViewById(R.id.ivPet);
        ivStationery = view.findViewById(R.id.ivStationery);
        recyclerView_Product = view.findViewById(R.id.rcProduct);
        arrayList = new ArrayList<>();
    }

    public void slideCatalogue() {
        String imageUri = "https://images.unsplash.com/photo-1541899481282-d53bffe3c35d?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=2000&q=80";
        Picasso.get().load(imageUri).into(ivCar);
        String imageUri2 = "https://images.unsplash.com/photo-1468495244123-6c6c332eeece?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=800&q=80";
        Picasso.get().load(imageUri2).into(ivElectD);
        String imageUri3 = "https://images.unsplash.com/photo-1618377384716-462f06a61706?ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&ixlib=rb-1.2.1&auto=format&fit=crop&w=800&q=80";
        Picasso.get().load(imageUri3).into(ivFurniture);
        String imageUri4 = "https://images.unsplash.com/photo-1548199973-03cce0bbc87b?ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&ixlib=rb-1.2.1&auto=format&fit=crop&w=800&q=80";
        Picasso.get().load(imageUri4).into(ivPet);
        String imageUri5 = "https://images.unsplash.com/photo-1593537052511-582fc3ddb192?ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&ixlib=rb-1.2.1&auto=format&fit=crop&w=800&q=80";
        Picasso.get().load(imageUri5).into(ivStationery);
    }

    public void slideView() {
        SliderAdapter sliderAdapter = new SliderAdapter(images);
        sliderView.setSliderAdapter(sliderAdapter);
        sliderView.setIndicatorAnimation(IndicatorAnimationType.WORM);
        sliderView.setSliderTransformAnimation(SliderAnimations.DEPTHTRANSFORMATION);
        sliderView.startAutoCycle();
    }

    private void onClick() {
        ivCar.setOnClickListener(view1 -> {
            getFragmentManager().beginTransaction()
                    .replace(R.id.flyMain, new VehicleFragment())
                    .addToBackStack(null).commit();
        });
        ivElectD.setOnClickListener(view1 -> {
            getFragmentManager().beginTransaction()
                    .replace(R.id.flyMain, new ElectDeviceFragment())
                    .addToBackStack(null).commit();
        });
        ivStationery.setOnClickListener(view1 -> {
            getFragmentManager().beginTransaction()
                    .replace(R.id.flyMain, new StationeryFragment())
                    .addToBackStack(null).commit();
        });
        //pet
        ivPet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFragmentManager().beginTransaction()
                        .replace(R.id.flyMain, new PetFragment())
                        .addToBackStack(null).commit();
            }
        });

        // do noi that
        ivFurniture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFragmentManager().beginTransaction()
                        .replace(R.id.flyMain, new HousewareFragment())
                        .addToBackStack(null).commit();
            }
        });
    }

    private void extractProduct() {
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, JSON_URL, null, new Response.Listener<JSONArray>() {
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
                GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 2);
                recyclerView_Product.setLayoutManager(layoutManager);

                ProductAdapter productAdapter = new ProductAdapter(arrayList, getActivity().getApplicationContext());
                recyclerView_Product.setAdapter(productAdapter);
                productAdapter.notifyDataSetChanged();

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
                GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 2);
                recyclerView_Product.setLayoutManager(layoutManager);
                ProductAdapter adapter = new ProductAdapter(filterProduct, context);
                recyclerView_Product.setAdapter(adapter);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                recyclerView_Product.setHasFixedSize(true);
                GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 2);
                recyclerView_Product.setLayoutManager(layoutManager);
                ProductAdapter productAdapter = new ProductAdapter(arrayList, getActivity().getApplicationContext());
                recyclerView_Product.setAdapter(productAdapter);
                productAdapter.notifyDataSetChanged();
                return false;
            }
        });
    }
}