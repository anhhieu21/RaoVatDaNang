package com.dev.assignment.fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.dev.assignment.Api;
import com.dev.assignment.Constants;
import com.dev.assignment.Model.ListFavorite;
import com.dev.assignment.Model.ListReport;
import com.dev.assignment.Model.User;
import com.dev.assignment.R;
import com.dev.assignment.activity.LoginActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class PetDetailFragment extends Fragment {
    Toolbar toolbar;
    TextView tvName, tvPrice, tvDetail, tvNameK;
    ImageView imvPet, imvBack, ivAvatar;
    SharedPreferences sharedPreferences;
    List<ListReport> listIdUser;
    List<String> listProductId = new ArrayList<>();
    String idUser, name1, image1, price;
    LinearLayout lnCall, lnSmS, tvChat1, tvChat2, tvChat3, tvChat4, lnFavorite2;
    String nPhone;
    User user;

    public PetDetailFragment() {
        // Required empty public constructor
    }

    public static PetDetailFragment newInstance(String param1, String param2) {
        PetDetailFragment fragment = new PetDetailFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_pet_detail, container, false);
        BottomNavigationView navBar = getActivity().findViewById(R.id.nvgMain);
        navBar.setVisibility(View.GONE);
        mapView(view);
        getUserK();
        onClick();
        getAllReport();
        getData();
        setDrawable();
        Bundle bundle = getArguments();
        if (bundle != null) {
            tvName.setText(bundle.getString("name"));
            name1 = bundle.getString("name");
            image1 = bundle.getString("image");
            DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
            tvPrice.setText(decimalFormat.format(bundle.getInt("price")) + " đ");
            price = decimalFormat.format(bundle.getInt("price")) + " đ";
            Picasso.get().load(bundle.getString("image")).into(imvPet);
            tvDetail.setText(bundle.getString("detail"));
        }

        return view;
    }

    public void mapView(View view) {
        tvName = view.findViewById(R.id.tv_name);
        tvNameK = view.findViewById(R.id.tvNameK);
        ivAvatar =view.findViewById(R.id.ivAvatar);
        tvPrice = view.findViewById(R.id.tv_price);
        imvPet = view.findViewById(R.id.imv_pet);
        tvDetail = view.findViewById(R.id.tvDetail);
        imvBack = view.findViewById(R.id.imvBack);
        toolbar = view.findViewById(R.id.toolbar);
        lnCall = view.findViewById(R.id.lnCall);
        lnSmS = view.findViewById(R.id.lnSmS);
        tvChat3 = view.findViewById(R.id.tvChat3);
        tvChat4 = view.findViewById(R.id.tvChat4);
        tvChat2 = view.findViewById(R.id.tvChat2);
        tvChat1 = view.findViewById(R.id.tvChat1);
        lnFavorite2 = view.findViewById(R.id.lnFavorite2);
        sharedPreferences = getActivity().getSharedPreferences("RefUser", Context.MODE_PRIVATE);
        listIdUser = new ArrayList<>();
        idUser = sharedPreferences.getString("IDUSER", "");
    }

    private void getData() {
        String url = Constants.URL_GET_FAVORITE + Constants.KEY_IDUSER;
        Log.e("12", url);
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        JsonArrayRequest jsonObjectRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject jsonObject = response.getJSONObject(i);
                        String productId = String.valueOf(jsonObject.get("productId"));
                        String idFavorites1 = String.valueOf(jsonObject.get("idFavorites"));
                        String name = String.valueOf(jsonObject.get("name"));
                        String price1 = String.valueOf(jsonObject.get("productId"));
                        String image = String.valueOf(jsonObject.get("image"));
                        String idUser1 = String.valueOf(jsonObject.get("idUser"));
                        int idFavorites = Integer.parseInt(idFavorites1);
                        int price = Integer.parseInt(price1);
                        int idUser = Integer.parseInt(idUser1);
                        ListFavorite listFavorite = new ListFavorite(idFavorites, productId, name, price, image, idUser);
                        Log.e("271", listFavorite.getProductId() + "");
                        listProductId.add(listFavorite.getProductId());
//                        ListReport listReport = new ListReport(id_lockPrCv, productIdCv, list_idUser, dateRP);
//                        Log.e("200", list_idUser + "");
//                        listIdUser.add(listReport);
//                        listC = new ArrayList<>();
//                        listC.add(productId);
//                        Log.e("272",listC+"");
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
                    String user_name = jObj.getString("name");
                    String url_ig = jObj.getString("avatar");
                    Picasso.get().load(url_ig).into(ivAvatar);
                    Log.e("138",jObj+"");
                    tvNameK.setText(user_name);
                }catch (JSONException e){
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
                params.put("id", Constants.ID_USER_K);
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

    private void onClick() {
        imvBack.setOnClickListener(view -> {
            getActivity().onBackPressed();
        });
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.favorite:
                        setFavoritePr();
                        item.setIcon(R.drawable.icons8_favorite_50px_1);
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
        lnCall.setOnClickListener(view -> {
            Uri u = Uri.parse("tel:" + nPhone);
            Intent i = new Intent(Intent.ACTION_DIAL, u);
            try {
                startActivity(i);
            } catch (SecurityException s) {
                Toast.makeText(getContext(), "An error occurred", Toast.LENGTH_LONG).show();
            }
        });
        lnSmS.setOnClickListener(view -> {
            Intent sendIntent = new Intent(Intent.ACTION_VIEW);
            sendIntent.setData(Uri.parse("sms:" + nPhone));
            sendIntent.putExtra("sms_body", "");
            startActivity(sendIntent);
        });
        tvChat1.setOnClickListener(view -> {
            Intent sendIntent = new Intent(Intent.ACTION_VIEW);
            sendIntent.setData(Uri.parse("sms:" + nPhone));
            sendIntent.putExtra("sms_body", "Sản phẩm này còn không bạn?");
            startActivity(sendIntent);
        });
        tvChat2.setOnClickListener(view -> {
            Intent sendIntent = new Intent(Intent.ACTION_VIEW);
            sendIntent.setData(Uri.parse("sms:" + nPhone));
            sendIntent.putExtra("sms_body", "Bạn có ship hàng không?");
            startActivity(sendIntent);
        });
        tvChat3.setOnClickListener(view -> {
            Intent sendIntent = new Intent(Intent.ACTION_VIEW);
            sendIntent.setData(Uri.parse("sms:" + nPhone));
            sendIntent.putExtra("sms_body", "Sản phẩm này có còn bảo hành không?");
            startActivity(sendIntent);
        });
        tvChat4.setOnClickListener(view -> {
            Intent sendIntent = new Intent(Intent.ACTION_VIEW);
            sendIntent.setData(Uri.parse("sms:" + nPhone));
            sendIntent.putExtra("sms_body", "Bạn có các sản phẩm khác tương tự?");
            startActivity(sendIntent);
        });
        lnFavorite2.setOnClickListener(view -> {
            setFavorite();
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
                        Log.e("200", list_idUser + "");
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

    public void setDrawable() {
//        Toast.makeText(getContext(), "Failed", Toast.LENGTH_SHORT).show();
        String idPr = Constants.PR_ID;
        boolean check = false;
        for (String s : listProductId) {
            Log.e("166", s + "");
            if (s.equalsIgnoreCase(idPr)) {
                check = true;
            }
        }
        if (check) {
            Toolbar toolbar = requireActivity().findViewById(R.id.toolbar);
            Menu menu = toolbar.getMenu();
            MenuItem item = menu.findItem(R.id.favorite);
            item.setIcon(R.drawable.icons8_favorite_50px_1);
        }
    }

    private void setFavoritePr() {
        String idPr = Constants.PR_ID;
        boolean check = false;
        for (String s : listProductId) {
            Log.e("167", s + "");
            if (s.equalsIgnoreCase(idPr)) {
                check = true;
            }
        }
        if (check) {
            Toast.makeText(getContext(), "Failed", Toast.LENGTH_SHORT).show();
        } else {
            setFavorite();
        }
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