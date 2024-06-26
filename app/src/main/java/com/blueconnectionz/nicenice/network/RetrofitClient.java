package com.blueconnectionz.nicenice.network;

import com.google.gson.Gson;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    //private static final String BASE_URL = "http://nicenice-api.ap-northeast-1.elasticbeanstalk.com/api/v1/";

    private static final String BASE_URL = "http://192.168.43.10:9091/api/v1/";

    private static RetrofitClient retrofitClient;
    private final Retrofit retrofit;
    Gson gson = new Gson();

    private RetrofitClient() {
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
    }

    public static synchronized RetrofitClient getRetrofitClient() {
        if (retrofitClient == null) {
            retrofitClient = new RetrofitClient();
        }
        return retrofitClient;
    }

    public API getAPI() {
        return retrofit.create(API.class);
    }
}
