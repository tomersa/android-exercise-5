package com.tomer.myapplication.rest;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface PixabayService {
    @GET("/api")
    Call<ImageSearchResult> getImageSearchResult(@Query("key") String key,
                                         @Query("q") String query,
                                         @Query("image_type") String imageType);

}
