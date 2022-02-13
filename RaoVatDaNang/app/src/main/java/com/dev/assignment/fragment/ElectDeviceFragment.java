package com.dev.assignment.fragment;

import android.content.Context;
import android.os.Bundle;

import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
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
import com.google.android.material.bottomsheet.BottomSheetDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class ElectDeviceFragment extends Fragment {
    RecyclerView recyclerView_Product;
    ArrayList<DataProduct> arrayList;
    ImageView ivPhone, ivTablet, ivLaptop, ivPc, ivTv;
    String type2;
    TextView tvMade;
    private Context context;

    public ElectDeviceFragment() {
        // Required empty public constructor
    }

    public ElectDeviceFragment(ArrayList<DataProduct> arrayList, Context context) {
        this.arrayList = arrayList;
        this.context = context;
    }

    public static ElectDeviceFragment newInstance(String param1, String param2) {
        ElectDeviceFragment fragment = new ElectDeviceFragment();
        Bundle args = new Bundle();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_elect_device, container, false);
        mapView(view);
        extractProduct();
        initSearchView(view);
        onClick();
        return view;
    }

    private void mapView(View view) {
        recyclerView_Product = view.findViewById(R.id.rcProduct);
        arrayList = new ArrayList<>();
        tvMade=view.findViewById(R.id.tvFilter);
        ivPhone = view.findViewById(R.id.ivPhone);
        ivTablet = view.findViewById(R.id.ivTablet);
        ivLaptop = view.findViewById(R.id.ivLaptop);
        ivPc = view.findViewById(R.id.ivPc);
        ivTv = view.findViewById(R.id.ivTv);
    }

    private void extractProduct() {
        String url = Constants.URL_GET_TYPE1 + "Đồ điện tử";
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
    private void onClick() {
        tvMade.setOnClickListener(view -> {
            dialogChose();
        });
        ivPhone.setOnClickListener(view -> {
            type2 = "Điện thoại";
            Constants.TYPE2 = type2;
            getFragmentManager().beginTransaction()
                    .replace(R.id.flyMain, new Type2VehicleFragment())
                    .addToBackStack(null).commit();
        });
        ivTablet.setOnClickListener(view -> {
            type2 = "Máy tính bảng";
            Constants.TYPE2 = type2;
            getFragmentManager().beginTransaction()
                    .replace(R.id.flyMain, new Type2VehicleFragment())
                    .addToBackStack(null).commit();

        });
        ivLaptop.setOnClickListener(view -> {
            type2 = "Laptop";
            Constants.TYPE2 = type2;
            getFragmentManager().beginTransaction()
                    .replace(R.id.flyMain, new Type2VehicleFragment())
                    .addToBackStack(null).commit();

        });
        ivPc.setOnClickListener(view -> {
            type2 = "Máy tính để bàn";
            Constants.TYPE2 = type2;
            getFragmentManager().beginTransaction()
                    .replace(R.id.flyMain, new Type2VehicleFragment())
                    .addToBackStack(null).commit();

        });
        ivTv.setOnClickListener(view -> {
            type2 = "Tivi, Âm thanh";
            Constants.TYPE2 = type2;
            getFragmentManager().beginTransaction()
                    .replace(R.id.flyMain, new Type2VehicleFragment())
                    .addToBackStack(null).commit();

        });
    }

    private void dialogChose() {

        BottomSheetDialog sheetDialog = new BottomSheetDialog(getContext(), R.style.BottomSheetDialogTheme);
        sheetDialog.setContentView(R.layout.dialog_electro_device);
        sheetDialog.setCanceledOnTouchOutside(false);
        sheetDialog.show();


    }
}