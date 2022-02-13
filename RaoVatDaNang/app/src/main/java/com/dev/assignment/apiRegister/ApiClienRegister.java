package com.dev.assignment.apiRegister;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClienRegister {
    private static Retrofit getRetrofit(){
        HttpLoggingInterceptor httpLoggingInterceptor= new HttpLoggingInterceptor();
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient okHttpClient= new OkHttpClient.Builder().addInterceptor(httpLoggingInterceptor).build();
        Retrofit retrofit= new Retrofit.Builder()
                .baseUrl("http://192.168.0.106:2004/users/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build();
        return retrofit;
    }
    public static ApiRegisterInterface getApiRegisterInterface(){
        ApiRegisterInterface apiRegisterInterface = getRetrofit().create(ApiRegisterInterface.class);
        return apiRegisterInterface;
    }
}
