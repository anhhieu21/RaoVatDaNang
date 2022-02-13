package com.dev.assignment.itemFB;

import com.dev.assignment.apiRegister.ApiSeviceCode;
import com.dev.assignment.apiRegister.Maso;
import com.dev.assignment.apiRegister.RegisterReponse;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ApiLoginfb {
    public static final String DOMAIN = "http://192.168.0.106:2004//users/";
    Gson gson= new GsonBuilder().setDateFormat("yyyy MM dd HH:mm:ss").create();
    ApiLoginfb apiLoginfb = new Retrofit.Builder()
            .baseUrl(DOMAIN)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(ApiLoginfb.class);
    @POST("loginfb")
    Call<FacebookEmail> loginfb(@Body FacebookEmail facebookEmail);
}
