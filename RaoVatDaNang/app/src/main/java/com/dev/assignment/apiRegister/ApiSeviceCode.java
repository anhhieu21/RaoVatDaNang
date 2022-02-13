package com.dev.assignment.apiRegister;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface ApiSeviceCode {
    public static final String DOMAIN = "http://192.168.0.106:2004/users/";
    Gson gson= new GsonBuilder().setDateFormat("yyyy MM dd HH:mm:ss").create();
    ApiSeviceCode apiSeviceCode = new Retrofit.Builder()
            .baseUrl(DOMAIN)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(ApiSeviceCode.class);
    @POST("checkcode")
    Call<RegisterReponse> checkcodeRegister(@Body Maso maso);
}
