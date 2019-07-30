package com.example.black.go_tankuser.network;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    private static Retrofit retrofit = null;

    private static OkHttpClient h = new OkHttpClient.Builder().connectTimeout(1, TimeUnit.MINUTES)
            .readTimeout(30,TimeUnit.SECONDS).writeTimeout(30,TimeUnit.SECONDS)
            .build();
    public static Retrofit getClient(String url){
        if (retrofit == null){
            retrofit = new Retrofit.Builder().baseUrl(url)
                    .client(h)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

        }
        return retrofit;
    }
}
