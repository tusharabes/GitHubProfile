package com.example.tusharagarwal.githubprofile.api;

import com.example.tusharagarwal.githubprofile.model.ItemResponse;
import retrofit2.Call;
import retrofit2.http.GET;

import retrofit2.http.Url;


public interface Service {


    @GET
    Call<ItemResponse> getItems(@Url String file);

}
