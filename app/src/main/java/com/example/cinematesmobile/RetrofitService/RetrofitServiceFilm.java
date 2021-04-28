package com.example.cinematesmobile.RetrofitService;

import com.example.cinematesmobile.Search.ModelMovieActor.AttoriDetails;
import com.example.cinematesmobile.Search.ModelMovieActor.AttoriImage;
import com.example.cinematesmobile.Search.ModelMovieActor.AttoriPopularResponse;
import com.example.cinematesmobile.Search.ModelMovieActor.AttoriResponse;
import com.example.cinematesmobile.Search.ModelMovieActor.GeneriResponse;
import com.example.cinematesmobile.Search.ModelMovieActor.MovieDetail;
import com.example.cinematesmobile.Search.ModelMovieActor.MovieImage;
import com.example.cinematesmobile.Search.ModelMovieActor.MovieResponse;
import com.example.cinematesmobile.Search.ModelMovieActor.NowPlayngResponse;
import com.example.cinematesmobile.Search.ModelMovieActor.PopularResponse;
import com.example.cinematesmobile.Search.ModelMovieActor.TopRatedResponse;
import com.example.cinematesmobile.Search.ModelMovieActor.UpcomingResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface RetrofitServiceFilm {
    @GET("search/movie")
    Call<MovieResponse> getMoviesByQuery(@Query("api_key") String api_key, @Query("language") String lingua, @Query("query") String query);

    @GET("search/person")
    Call<AttoriResponse> getPersonByQuery(@Query("api_key") String api_key, @Query("language") String lingua, @Query("query") String query);

    @GET("movie/popular")
    Call<PopularResponse> getPopularByQuery(@Query("api_key") String api_key, @Query("language") String lingua);

    @GET("movie/upcoming")
    Call<UpcomingResponse> getUpcomingByQuery(@Query("api_key") String api_key, @Query("language") String lingua);

    @GET("movie/top_rated")
    Call<TopRatedResponse> getTopRatedByQuery(@Query("api_key") String api_key, @Query("language") String lingua);

    @GET("person/{person_id}")
    Call<AttoriDetails> getAttoriDetails(@Path("person_id") int person_id, @Query("api_key") String api_key, @Query("language") String lingua);

    @GET("person/{person_id}/images")
    Call<AttoriImage> getAttoriImage(@Path("person_id") int person_id, @Query("api_key") String api_key);

    @GET("movie/{movie_id}")
    Call<MovieDetail> getMovieDetail(@Path("movie_id") int person_id, @Query("api_key") String api_key, @Query("language") String lingua);

    @GET("movie/{movie_id}/images")
    Call<MovieImage> getMovieImage(@Path("movie_id") int person_id, @Query("api_key") String api_key);

    @GET("person/popular")
    Call<AttoriPopularResponse> getAttoriPopular(@Query("api_key") String api_key, @Query("language") String lingua);

    @GET("movie/now_playing")
    Call<NowPlayngResponse> getNowPlayng(@Query("api_key") String api_key, @Query("language") String lingua);

    @GET("genre/movie/list")
    Call<GeneriResponse> getGeneri(@Query("api_key") String api_key, @Query("language") String lingua);
}