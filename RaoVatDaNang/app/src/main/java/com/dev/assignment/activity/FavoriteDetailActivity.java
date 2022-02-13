package com.dev.assignment.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
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

public class FavoriteDetailActivity extends AppCompatActivity {
    Toolbar toolbar;
    TextView tvName, tvPrice, tvDetail, tvNameK;
    ImageView imvPet, imvBack;
    SharedPreferences sharedPreferences;
    List<ListReport> listIdUser;
    String idUser, name1, image1, price;
    LinearLayout lnCall, lnSmS, tvChat1, tvChat2, tvChat3, tvChat4, lnFavorite2;
    String nPhone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite_detail);
        mapView();
        showData();
        getAllReport();
        onClick();
    }

    private void mapView() {
        tvName = findViewById(R.id.tv_name);
        tvNameK = findViewById(R.id.tvNameK);
        tvPrice = findViewById(R.id.tv_price);
        imvPet = findViewById(R.id.imv_pet);
        tvDetail = findViewById(R.id.tvDetail);
        imvBack = findViewById(R.id.imvBack);
        toolbar = findViewById(R.id.toolbar);
        lnCall = findViewById(R.id.lnCall);
        lnSmS = findViewById(R.id.lnSmS);
        tvChat3 = findViewById(R.id.tvChat3);
        tvChat4 = findViewById(R.id.tvChat4);
        tvChat2 = findViewById(R.id.tvChat2);
        tvChat1 = findViewById(R.id.tvChat1);
        lnFavorite2 = findViewById(R.id.lnFavorite2);
        sharedPreferences = getSharedPreferences("RefUser", Context.MODE_PRIVATE);
        listIdUser = new ArrayList<>();
        idUser = sharedPreferences.getString("IDUSER", "");

    }

    private void showData() {

        String url = Constants.URL_GET_RP_ID + "productId=" + Constants.PR_ID;
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        JsonArrayRequest jsonObjectRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                for (int i = 0; i < response.length(); i++) {
                    try {
                        //Get Data
                        JSONObject jsonObject = response.getJSONObject(i);
                        Constants.ID_USER_K = String.valueOf(jsonObject.getInt("idUser"));
                        String name = jsonObject.getString("name");
                        int price = jsonObject.getInt("price");
                        String image = jsonObject.getString("image_1");
                        String detail = jsonObject.getString("detail");
                        tvName.setText(name);
                        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
                        tvPrice.setText(decimalFormat.format(price) + " đ");
                        Log.e("51", Constants.ID_USER_K + "");
                        Picasso.get().load(image).into(imvPet);
                        tvDetail.setText(detail);
                        getUserK();
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
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        JsonArrayRequest jsonObjectRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                for (int i = 0; i < response.length(); i++) {
                    try {
                        //Get Data
                        JSONObject jsonObject = response.getJSONObject(i);
                        String user_name = jsonObject.getString("user_name");
                        String email = String.valueOf(jsonObject.get("email"));
                        nPhone = String.valueOf(jsonObject.get("phone"));
                        String address = String.valueOf(jsonObject.get("address"));
                        tvNameK.setText(user_name);
                        Log.e("134", nPhone + "1");
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
            onBackPressed();
        });
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.favorite:
                        setFavorite();
                        return true;
                    case R.id.share:
                        Toast.makeText(getApplicationContext(), "Đã chia sẽ!", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(this, "An error occurred", Toast.LENGTH_LONG).show();
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
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
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
            Toast.makeText(getApplicationContext(), "ok", Toast.LENGTH_SHORT).show();
        }
    }

    public void dialogRportFailed() {
        LayoutInflater inflater1 = getLayoutInflater();
        View view1 = inflater1.inflate(R.layout.dialog_report_failed, null);
        AlertDialog.Builder alert = new AlertDialog.Builder(getApplicationContext());
        alert.setView(view1);
        AlertDialog dialog = alert.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.show();
    }

    public void dialogRportFailed1() {
        LayoutInflater inflater1 = getLayoutInflater();
        View view1 = inflater1.inflate(R.layout.dialog_report_failed1, null);
        AlertDialog.Builder alert = new AlertDialog.Builder(getApplicationContext());
        alert.setView(view1);
        AlertDialog dialog = alert.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.show();
    }

    public void dialogReportSuccessfully() {
        LayoutInflater inflater1 = getLayoutInflater();
        View view1 = inflater1.inflate(R.layout.dialog_report_successfully, null);
        AlertDialog.Builder alert = new AlertDialog.Builder(getApplicationContext());
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
                    Toast.makeText(getApplicationContext(), "Response code: " + response.code(), Toast.LENGTH_SHORT).show();
                }
                try {
                    dialogReportSuccessfully();
                    Toast.makeText(getApplicationContext(), "Report successful!", Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), " Failed!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                t.printStackTrace();
                Toast.makeText(getApplicationContext(), "Call Api error!", Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(getApplicationContext(), "Response code: " + response.code(), Toast.LENGTH_SHORT).show();
                }
                try {
                    Toast.makeText(getApplicationContext(), "Đã thêm vào mục yêu thích!", Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), " Failed!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                t.printStackTrace();
                Toast.makeText(getApplicationContext(), "Call Api error!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}