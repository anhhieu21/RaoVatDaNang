package com.dev.assignment.apiRegister;

import com.dev.assignment.Model.Status;
import com.dev.assignment.Model.User;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface UserAPI {
    @POST("/users/login")
    public Call<Status> login(@Body User user);
}
// nhap tam bay no cung dc ko nhap cung đc kkk