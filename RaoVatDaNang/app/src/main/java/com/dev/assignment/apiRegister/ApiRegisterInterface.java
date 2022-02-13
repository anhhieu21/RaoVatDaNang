package com.dev.assignment.apiRegister;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface ApiRegisterInterface {
    @POST("register")
    Call<RegisterReponse> registerUserApi(@Body RegisterRequest request);
}
