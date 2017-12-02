package com.tomer.myapplication.rest;

import java.io.IOException;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ServiceGenerator {
    static final String BASE_URL = "https://pixabay.com/api/";
    public static final String PIXABAY_KEY = "7143795-63f8097bd68601f11b7e06188";
    public static final String IMAGE_TYPE = "photo";

    public static Retrofit.Builder retrofitBuilder = new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create());

    private static final int PER_PAGE = 20;
    public static OkHttpClient.Builder httpClient = new OkHttpClient.
            Builder().
            addInterceptor(new Interceptor() {
                               @Override
                               public Response intercept(Chain chain) throws IOException {
                                   Request original = chain.request();
                                   HttpUrl originalHttpUrl = original.url();

                                   HttpUrl url = originalHttpUrl.newBuilder()
                                           .addQueryParameter("key", ServiceGenerator.
                                                   PIXABAY_KEY)
                                           .addQueryParameter("image_type", ServiceGenerator.
                                                   IMAGE_TYPE)
                                           .addQueryParameter("per_page", Integer.toString(ServiceGenerator.
                                                   PER_PAGE))
                                           .build();

                                   // Request customization: add request headers
                                   Request.Builder requestBuilder = original.newBuilder()
                                           .url(url);

                                   Request request = requestBuilder.build();
                                   return chain.proceed(request);
                               }
                           }
            );

    public static Retrofit retrofit = retrofitBuilder
            .client(httpClient.build())
            .build();

    public static int currentPage = 0;
}
