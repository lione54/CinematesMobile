package com.example.cinematesmobile.RetrofitClient;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClientFilm {
    private static final String TMDB = "https://api.themoviedb.org/3/";
    private static Retrofit retrofit = null;
    public static Retrofit getClient(){
        if(retrofit == null){
            retrofit = new Retrofit.Builder().baseUrl(TMDB).addConverterFactory(GsonConverterFactory.create()).build();
        }
        return retrofit;
    }
}
