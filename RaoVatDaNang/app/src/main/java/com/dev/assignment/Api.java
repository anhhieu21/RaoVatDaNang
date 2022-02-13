package com.dev.assignment;

import com.dev.assignment.Model.Users;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;

import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface Api {

    @Multipart
    @POST("insert")
    Call<ResponseBody> create(
            @Part(Constants.KEY_NAME) RequestBody name,
            @Part(Constants.KEY_PRICE) RequestBody price,
            @Part(Constants.KEY_TYPE1) RequestBody type1,
            @Part(Constants.KEY_TYPE2) RequestBody type2,
            @Part("status") RequestBody status,
            @Part("lockPr") RequestBody lockPr,
            @Part("idUser") RequestBody idUser,
            @Part(Constants.KEY_DETAIL) RequestBody detail,
            @Part MultipartBody.Part image_1
    );

    @Multipart
    @PUT("update")
    Call<ResponseBody> update(
            @Part(Constants.KEY_PRODUCTID) RequestBody productId,
            @Part(Constants.KEY_NAME) RequestBody name,
            @Part(Constants.KEY_PRICE) RequestBody price,
            @Part(Constants.KEY_TYPE1) RequestBody type1,
            @Part(Constants.KEY_TYPE2) RequestBody type2,
            @Part(Constants.KEY_DETAIL) RequestBody detail,
            @Part MultipartBody.Part image_1
    );


    @POST("login")
    Call<Users> login(@Query("email") String email,
                      @Query("password") String password);

    @POST("report")
    Call<ResponseBody> report(@Query("productId") String productId,
                              @Query("list_idUser") String list_idUser);

    @PUT("updateLock")
    Call<ResponseBody> setLockPr(@Query("productId") String productId,
                                 @Query("lockPr") String lockPr);

    @POST("favorite")
    Call<ResponseBody> createFavorite(@Query("productId") String productId,
                                      @Query("name") String name,
                                      @Query("price") String price,
                                      @Query("image") String image,
                                      @Query("idUser") String idUser);

}
