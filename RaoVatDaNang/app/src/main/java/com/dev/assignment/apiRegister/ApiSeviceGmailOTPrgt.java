package com.dev.assignment.apiRegister;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ApiSeviceGmailOTPrgt {
    public static final String DOMAIN = "http://192.168.0.106:2004/users/";
    Gson gson= new GsonBuilder().setDateFormat("yyyy MM dd HH:mm:ss").create();
    ApiSeviceGmailOTPrgt apiSeviceGmailOtPrgt = new Retrofit.Builder()
            .baseUrl(DOMAIN)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(ApiSeviceGmailOTPrgt.class);
    @POST("sendmail")
    Call<RegisterReponse> checkEmailregister(@Body RegisterEmail registerEmail);
}
