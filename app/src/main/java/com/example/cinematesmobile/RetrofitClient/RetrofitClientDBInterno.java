package com.example.cinematesmobile.RetrofitClient;

import com.example.cinematesmobile.BuildConfig;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClientDBInterno {
    private static final String urlBase = "http://" + BuildConfig.IP_AWS + "/cinematesdb/";
    private static Retrofit retrofit = null;
    public static Retrofit getClient(){
        if(retrofit == null){
            retrofit = new Retrofit.Builder().baseUrl(urlBase).addConverterFactory(GsonConverterFactory.create()).build();
        }
        return retrofit;
    }
}
