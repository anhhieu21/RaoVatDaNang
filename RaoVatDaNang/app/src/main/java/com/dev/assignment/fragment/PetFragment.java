package com.dev.assignment.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.dev.assignment.Constants;
import com.dev.assignment.Model.DataProduct;
import com.dev.assignment.R;
import com.dev.assignment.adapter.PetAdapter;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class PetFragment extends Fragment {

    ImageView imvBack, ivChicken, ivDog, ivCat, ivBird, ivMore;
    RecyclerView recyclerView_Pet;
    ArrayList<DataProduct> arrayList;
    String type2;
    private Context context;
    private static String JSON_URL = "http://192.168.0.106:6000/api/controllers/type1?type1=Th%C3%BA%20c%C6%B0ng";
    PetAdapter petAdapter;

    public PetFragment() {
        // Required empty public constructor
    }

    public PetFragment(ArrayList<DataProduct> arrayList, Context context) {
        this.arrayList = arrayList;
        this.context = context;
    }

    public static PetFragment newInstance(String param1, String param2) {
        PetFragment fragment = new PetFragment();
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
        View view = inflater.inflate(R.layout.fragment_pet, container, false);

        BottomNavigationView navBar = getActivity().findViewById(R.id.nvgMain);
        navBar.setVisibility(View.VISIBLE);
        mapView(view);
        extractPets();
        initSearchView(view);
        onClick();

        return view;
    }

    private void mapView(View view) {
        imvBack = view.findViewById(R.id.imvBack);
        recyclerView_Pet = view.findViewById(R.id.recyclerView_pet);
        arrayList = new ArrayList<>();
        ivChicken = view.findViewById(R.id.ivChicken);
        ivDog = view.findViewById(R.id.ivDogs);
        ivCat = view.findViewById(R.id.ivCat);
        ivBird = view.findViewById(R.id.ivBird);
        ivMore = view.findViewById(R.id.ivMore);
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
                recyclerView_Pet.setHasFixedSize(true);
                LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
                recyclerView_Pet.setLayoutManager(layoutManager);
                //Divider
                DividerItemDecoration d = new DividerItemDecoration(getActivity(), layoutManager.getOrientation());
                recyclerView_Pet.addItemDecoration(d);
                PetAdapter adapter = new PetAdapter(filterProduct, context);
                recyclerView_Pet.setAdapter(adapter);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                recyclerView_Pet.setHasFixedSize(true);
                LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
                recyclerView_Pet.setLayoutManager(layoutManager);
                //Divider
                DividerItemDecoration d = new DividerItemDecoration(getActivity(), layoutManager.getOrientation());
                recyclerView_Pet.addItemDecoration(d);

                PetAdapter petAdapter = new PetAdapter(arrayList, getActivity().getApplicationContext());
                recyclerView_Pet.setAdapter(petAdapter);
                return false;
            }
        });
    }

    private void onClick() {
        imvBack.setOnClickListener(view -> {
            getActivity().onBackPressed();
        });
        ivChicken.setOnClickListener(view -> {
            type2 = "Gà";
            Constants.TYPE2 = type2;
            getFragmentManager().beginTransaction()
                    .replace(R.id.flyMain, new Type2PetFragment())
                    .addToBackStack(null).commit();
        });
        ivDog.setOnClickListener(view -> {
            type2 = "Chó";
            Constants.TYPE2 = type2;
            getFragmentManager().beginTransaction()
                    .replace(R.id.flyMain, new Type2PetFragment())
                    .addToBackStack(null).commit();
        });
        ivCat.setOnClickListener(view -> {
            type2 = "Mèo";
            Constants.TYPE2 = type2;
            getFragmentManager().beginTransaction()
                    .replace(R.id.flyMain, new Type2PetFragment())
                    .addToBackStack(null).commit();
        });
        ivBird.setOnClickListener(view -> {
            type2 = "Chim";
            Constants.TYPE2 = type2;
            getFragmentManager().beginTransaction()
                    .replace(R.id.flyMain, new Type2PetFragment())
                    .addToBackStack(null).commit();
        });
        ivMore.setOnClickListener(view -> {
            type2 = "Thú cưng khác";
            Constants.TYPE2 = type2;
            getFragmentManager().beginTransaction()
                    .replace(R.id.flyMain, new Type2PetFragment())
                    .addToBackStack(null).commit();
        });

    }

    private void extractPets() {
        String url = Constants.URL_GET_TYPE1 + "Thú cưng";
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
                        dataPet.setDetail(petObject.getString("detail"));
                        dataPet.setStatus(petObject.getString("status"));
                        dataPet.setLockPr(Integer.parseInt(petObject.getString("lockPr")));
                        dataPet.setIdUser(Integer.parseInt(petObject.getString("idUser")));
                        arrayList.add(dataPet);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
                recyclerView_Pet.setHasFixedSize(true);
                LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
                recyclerView_Pet.setLayoutManager(layoutManager);
                //Divider
                DividerItemDecoration d = new DividerItemDecoration(getActivity(), layoutManager.getOrientation());
                recyclerView_Pet.addItemDecoration(d);

                PetAdapter petAdapter = new PetAdapter(arrayList, getActivity().getApplicationContext());
                recyclerView_Pet.setAdapter(petAdapter);
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