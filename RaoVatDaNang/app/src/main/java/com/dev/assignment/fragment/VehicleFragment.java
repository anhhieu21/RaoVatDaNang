package com.dev.assignment.fragment;

import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;

import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
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
import com.dev.assignment.adapter.ProductAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class VehicleFragment extends Fragment {
    RecyclerView recyclerView_Product;
    ArrayList<DataProduct> arrayList;
    LinearLayout typeCar, typeMoto, typeBicycle, typeTruck, typeMore, typeEcar, typeAccessory;
    String type2;
    private Context context;
    SearchView searchView;
    ProductAdapter productAdapter ;
    public VehicleFragment() {
        // Required empty public constructor
    }

    public VehicleFragment(ArrayList<DataProduct> arrayList, Context context) {
        this.arrayList = arrayList;
        this.context = context;
    }

    public static VehicleFragment newInstance(String param1, String param2) {
        VehicleFragment fragment = new VehicleFragment();
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
        View view = inflater.inflate(R.layout.fragment_vehicle, container, false);
        mapView(view);
        extractProduct();
        initSearchView(view);
        onClickC();

        return view;
    }

    private void mapView(View view) {
        recyclerView_Product = view.findViewById(R.id.rcProduct);
        typeCar = view.findViewById(R.id.typeCar);
        typeMoto = view.findViewById(R.id.typeMoto);
        typeBicycle = view.findViewById(R.id.typeBicycle);
        typeTruck = view.findViewById(R.id.typeTruck);
        typeEcar = view.findViewById(R.id.typeEcar);
        typeMore = view.findViewById(R.id.typeMore);
        typeAccessory = view.findViewById(R.id.typeAccessory);
        arrayList = new ArrayList<>();
        searchView = view.findViewById(R.id.search);
    }

    private void extractProduct() {
        String url = Constants.URL_GET_TYPE1 + "Xe c???";
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
                GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 2);
                recyclerView_Product.setLayoutManager(layoutManager);

                ProductAdapter productAdapter = new ProductAdapter(arrayList, getActivity().getApplicationContext());
                recyclerView_Product.setAdapter(productAdapter);
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
    private void onClickC() {
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                productAdapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (productAdapter != null){
                    productAdapter.getFilter().filter(newText);
                }

                return false;
            }
        });
        typeCar.setOnClickListener(view -> {
            type2 = "?? t??";
            Constants.TYPE2 = type2;
            getFragmentManager().beginTransaction()
                    .replace(R.id.flyMain, new Type2VehicleFragment())
                    .addToBackStack(null).commit();
        });
        typeMoto.setOnClickListener(view -> {
            type2 = "Xe m??y";
            Constants.TYPE2 = type2;
            getFragmentManager().beginTransaction()
                    .replace(R.id.flyMain, new Type2VehicleFragment())
                    .addToBackStack(null).commit();

        });
        typeBicycle.setOnClickListener(view -> {
            type2 = "Xe ?????p";
            Constants.TYPE2 = type2;
            getFragmentManager().beginTransaction()
                    .replace(R.id.flyMain, new Type2VehicleFragment())
                    .addToBackStack(null).commit();

        });
        typeTruck.setOnClickListener(view -> {
            type2 = "Xe t???i, xe ben";
            Constants.TYPE2 = type2;
            getFragmentManager().beginTransaction()
                    .replace(R.id.flyMain, new Type2VehicleFragment())
                    .addToBackStack(null).commit();

        });
        typeEcar.setOnClickListener(view -> {
            type2 = "Xe ??i???n";
            Constants.TYPE2 = type2;
            getFragmentManager().beginTransaction()
                    .replace(R.id.flyMain, new Type2VehicleFragment())
                    .addToBackStack(null).commit();

        });
        typeMore.setOnClickListener(view -> {
            type2 = "Ph????ng ti???n kh??c";
            Constants.TYPE2 = type2;
            getFragmentManager().beginTransaction()
                    .replace(R.id.flyMain, new Type2VehicleFragment())
                    .addToBackStack(null).commit();

        });
        typeAccessory.setOnClickListener(view -> {
            type2 = "Ph??? t??ng xe";
            Constants.TYPE2 = type2;
            getFragmentManager().beginTransaction()
                    .replace(R.id.flyMain, new Type2VehicleFragment())
                    .addToBackStack(null).commit();

        });
    }



}