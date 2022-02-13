package com.dev.assignment.fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
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
import com.dev.assignment.Api;
import com.dev.assignment.Constants;
import com.dev.assignment.Model.ListReport;
import com.dev.assignment.R;
import com.dev.assignment.adapter.FavoriteAdapter;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class FavoriteDetailFragment extends Fragment {
    Toolbar toolbar;
    TextView tvName, tvPrice, tvDetail, tvNameK;
    ImageView imvPet, imvBack;
    SharedPreferences sharedPreferences;
    List<ListReport> listIdUser;
    String idUser, name1, image1, price;

    public FavoriteDetailFragment() {
        // Required empty public constructor
    }

    public static FavoriteDetailFragment newInstance(String param1, String param2) {
        FavoriteDetailFragment fragment = new FavoriteDetailFragment();
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
        View view = inflater.inflate(R.layout.fragment_favorite, container, false);

        mapView(view);
        showData();
        getUserK();
        getAllReport();
        onClick();
        return view;
    }

    private void mapView(View view) {
        tvName = view.findViewById(R.id.tv_name);
        tvNameK = view.findViewById(R.id.tvNameK);
        tvPrice = view.findViewById(R.id.tv_price);
        imvPet = view.findViewById(R.id.imv_pet);
        tvDetail = view.findViewById(R.id.tvDetail);
        imvBack = view.findViewById(R.id.imvBack);
        toolbar = view.findViewById(R.id.toolbar);
        sharedPreferences = getActivity().getSharedPreferences("RefUser", Context.MODE_PRIVATE);
        listIdUser = new ArrayList<>();
        idUser = sharedPreferences.getString("IDUSER", "");
    }

    private void showData() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
            tvPrice.setText(decimalFormat.format(bundle.getInt("price")) + " đ");
        }
        String url = Constants.URL_GET_RP_ID + "productId=" + Constants.PR_ID;
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        JsonArrayRequest jsonObjectRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                for (int i = 0; i < response.length(); i++) {
                    try {
                        //Get Data
                        JSONObject jsonObject = response.getJSONObject(i);
                        String name = jsonObject.getString("name");
                        int price = jsonObject.getInt("price");
                        String image = jsonObject.getString("image_1");
                        String detail = jsonObject.getString("detail");
                        tvName.setText(name);
                        Log.e("51",name+price+detail+"");
                        Picasso.get().load(image).into(imvPet);
                        tvDetail.setText(detail);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        requestQueue.add(jsonObjectRequest);
    }

    private void getUserK() {
        String url = "http://192.168.0.106:6000/api/controllers/findUserById?idUser=" + Constants.ID_USER_K;
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        JsonArrayRequest jsonObjectRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                for (int i = 0; i < response.length(); i++) {
                    try {
                        //Get Data
                        JSONObject jsonObject = response.getJSONObject(i);
                        String user_name = jsonObject.getString("user_name");
                        String email = String.valueOf(jsonObject.get("email"));
                        String phone = String.valueOf(jsonObject.get("phone"));
                        String address = String.valueOf(jsonObject.get("address"));
                        tvNameK.setText(user_name);
                        Log.e("134", user_name + "");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        requestQueue.add(jsonObjectRequest);
    }

    private void onClick() {
        imvBack.setOnClickListener(view -> {
            getActivity().onBackPressed();
        });
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.favorite:
                        setFavorite();
                        return true;
                    case R.id.share:
                        Toast.makeText(getActivity(), "Đã chia sẽ!", Toast.LENGTH_SHORT).show();
                        return true;
                    case R.id.report:
                        reportPr();
                        return true;
                }
                return false;
            }
        });
    }

    public void getAllReport() {
        String url = Constants.URL_GET_ALL_RP + "productId=" + Constants.PR_ID;
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        JsonArrayRequest jsonObjectRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                for (int i = 0; i < response.length(); i++) {
                    try {
                        //Get Data
                        JSONObject jsonObject = response.getJSONObject(i);
                        String list_idUser = String.valueOf(jsonObject.get("list_idUser"));
                        String id_lockPr = String.valueOf(jsonObject.get("id_lockPr"));
                        String productId = String.valueOf(jsonObject.get("productId"));
                        String dateRP = String.valueOf(jsonObject.get("dateRP"));
                        //Convert Data
                        int id_lockPrCv = Integer.parseInt(id_lockPr);
                        int productIdCv = Integer.parseInt(productId);
                        //Create and add obj in list
                        ListReport listReport = new ListReport(id_lockPrCv, productIdCv, list_idUser, dateRP);
//                        Log.e("200", list_idUser + "");
                        listIdUser.add(listReport);
//                        DataProduct dataProduct = new DataProduct();
//                        dataProduct.setIdProduct(jsonObject.getString("productId"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        requestQueue.add(jsonObjectRequest);
    }

    private void reportPr() {
        String yourId = Constants.KEY_IDUSER;
        String idK = Constants.ID_USER_K;
        Log.e("126", yourId + "" + idK);
        Log.e("501", "" + idUser);
        boolean check = false;
        for (ListReport listReport : listIdUser) {
            if (listReport.getList_idUser().equalsIgnoreCase(idUser)) {
                check = true;
            }
        }
        if (check) {
            dialogRportFailed();
        } else if (check || yourId.equals(idK)) {
            dialogRportFailed1();
        } else {
            postReport();
            setLockPr();
            Toast.makeText(getContext(), "ok", Toast.LENGTH_SHORT).show();
        }
    }

    public void dialogRportFailed() {
        LayoutInflater inflater1 = getLayoutInflater();
        View view1 = inflater1.inflate(R.layout.dialog_report_failed, null);
        AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
        alert.setView(view1);
        AlertDialog dialog = alert.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.show();
    }

    public void dialogRportFailed1() {
        LayoutInflater inflater1 = getLayoutInflater();
        View view1 = inflater1.inflate(R.layout.dialog_report_failed1, null);
        AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
        alert.setView(view1);
        AlertDialog dialog = alert.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.show();
    }

    public void dialogReportSuccessfully() {
        LayoutInflater inflater1 = getLayoutInflater();
        View view1 = inflater1.inflate(R.layout.dialog_report_successfully, null);
        AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
        alert.setView(view1);
        AlertDialog dialog = alert.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.show();
    }

    private void postReport() {
        String productId = Constants.PR_ID;
        String list_idUser = idUser;
        Retrofit retrofit = new Retrofit.Builder().baseUrl(Constants.URL_POST_RP).addConverterFactory(GsonConverterFactory.create()).build();
        Api reportAPI = retrofit.create(Api.class);
        reportAPI.report(productId, list_idUser).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(getContext(), "Response code: " + response.code(), Toast.LENGTH_SHORT).show();
                }
                try {
                    dialogReportSuccessfully();
                    Toast.makeText(getContext(), "Report successful!", Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(getContext(), " Failed!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                t.printStackTrace();
                Toast.makeText(getContext(), "Call Api error!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void setLockPr() {
        String productId = Constants.PR_ID;
        String lockPr1 = Constants.PR_LOCK;
        String lockPr = "";
        if (lockPr1.equals("0")) {
            lockPr = "1";

        } else if (lockPr1.equals("1")) {
            lockPr = "2";

        } else if (lockPr1.equals("2")) {
            lockPr = "3";

        }
        Retrofit retrofit = new Retrofit.Builder().baseUrl(Constants.URL_POST_RP).addConverterFactory(GsonConverterFactory.create()).build();
        Api reportAPI = retrofit.create(Api.class);
        reportAPI.setLockPr(productId, lockPr).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });

    }

    private void setFavorite() {
        String yourId = Constants.KEY_IDUSER;
        String name = name1;
        String image = image1;
        String price1 = price;
        String prId = Constants.PR_ID;
        Retrofit retrofit = new Retrofit.Builder().baseUrl(Constants.URL_POST_RP).addConverterFactory(GsonConverterFactory.create()).build();
        Api reportAPI = retrofit.create(Api.class);
        reportAPI.createFavorite(prId, name, price1, image, yourId).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(getContext(), "Response code: " + response.code(), Toast.LENGTH_SHORT).show();
                }
                try {
                    Toast.makeText(getContext(), "Đã thêm vào mục yêu thích!", Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(getContext(), " Failed!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                t.printStackTrace();
                Toast.makeText(getContext(), "Call Api error!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}