package com.tomer.myapplication.rest;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ServiceGenerator {
    static final String BASE_URL = "https://pixabay.com/api/";
    public static final String PIXABAY_KEY = "7143795-63f8097bd68601f11b7e06188";
    public static final String IMAGE_TYPE = "photo";

    public static Retrofit.Builder retrofitBuilder = new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create());

    public static OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

    public static Retrofit retrofit = retrofitBuilder
            .client(httpClient.build())
            .build();
}
