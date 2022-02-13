package com.dev.assignment.apiSetupUser;

import com.dev.assignment.apiRegister.RegisterReponse;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface ApiSevice {
    public static final String DOMAIN = "http://192.168.0.106:2004/users/";
    Gson gson= new GsonBuilder().setDateFormat("yyyy MM dd HH:mm:ss").create();
    ApiSevice apiSevice = new Retrofit.Builder()
            .baseUrl(DOMAIN)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(ApiSevice.class);
    @Multipart
    @POST("update")
    Call<RegisterReponse> uploarUserApi(
            @Part (ApiClienUsers.KEY_EMAIL)RequestBody email ,
            @Part (ApiClienUsers.KEY_NAME)RequestBody name,
            @Part (ApiClienUsers.KEY_PHONE)RequestBody phone,
            @Part (ApiClienUsers.KEY_ADDRESS)RequestBody address,
            @Part MultipartBody.Part avatar);

}


