package com.dev.assignment.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
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
import com.dev.assignment.adapter.FavoriteAdapter;
import com.dev.assignment.adapter.PetAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class FavoriteActivity extends AppCompatActivity {
    RecyclerView recyclerView_Pet;
    ArrayList<DataProduct> arrayList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite);
        recyclerView_Pet = findViewById(R.id.rcvProduct);
        arrayList = new ArrayList<>();
        extractPets();
    }
    private void extractPets() {
        String url = Constants.URL_GET_FAVORITE +Constants.KEY_IDUSER;
        RequestQueue queue = Volley.newRequestQueue(this);
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
                        dataPet.setImage_1(petObject.getString("image"));
                        arrayList.add(dataPet);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
                recyclerView_Pet.setHasFixedSize(true);
                LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
                recyclerView_Pet.setLayoutManager(layoutManager);
                //Divider
                DividerItemDecoration d = new DividerItemDecoration(getApplicationContext(), layoutManager.getOrientation());
                recyclerView_Pet.addItemDecoration(d);

                FavoriteAdapter petAdapter = new FavoriteAdapter(arrayList, getApplicationContext());
                recyclerView_Pet.setAdapter(petAdapter);
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                Log.d("tag", "onErrorResponse: " + error.getMessage());
                Toast.makeText(getApplicationContext(), "onErrorResponse: " + error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

        queue.add(jsonArrayRequest);
    }
}