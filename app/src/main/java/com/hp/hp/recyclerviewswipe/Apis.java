package com.hp.hp.recyclerviewswipe;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface Apis {

    @GET("photos")
    Call<List<Photos>> viewphotos();
}
