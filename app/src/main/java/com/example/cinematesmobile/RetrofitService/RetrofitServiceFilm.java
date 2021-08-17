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
    Call<MovieResponse> CercaFilmTMDB(@Query("api_key") String api_key, @Query("language") String lingua, @Query("query") String query);

    @GET("search/person")
    Call<AttoriResponse> CercaAttoreTMDB(@Query("api_key") String api_key, @Query("language") String lingua, @Query("query") String query);

    @GET("movie/popular")
    Call<PopularResponse> PrendiFilmPopolariTMDB(@Query("api_key") String api_key, @Query("language") String lingua);

    @GET("movie/upcoming")
    Call<UpcomingResponse> PrendiProssimeUsciteTMDB(@Query("api_key") String api_key, @Query("language") String lingua);

    @GET("movie/top_rated")
    Call<TopRatedResponse> PrendiFilmPiuVotatiTMDB(@Query("api_key") String api_key, @Query("language") String lingua);

    @GET("person/{person_id}")
    Call<AttoriDetails> PrendiDettagliAttoreTMDB(@Path("person_id") int person_id, @Query("api_key") String api_key, @Query("language") String lingua);

    @GET("person/{person_id}/images")
    Call<AttoriImage> PrendiImmaginiAttoriTMDB(@Path("person_id") int person_id, @Query("api_key") String api_key);

    @GET("movie/{movie_id}")
    Call<MovieDetail> PredndiDettagliFilmTMDB(@Path("movie_id") int movie_id, @Query("api_key") String api_key, @Query("language") String lingua);

    @GET("movie/{movie_id}/images")
    Call<MovieImage> PrendiImmaginiFilmTMDB(@Path("movie_id") int movie_id, @Query("api_key") String api_key);

    @GET("person/popular")
    Call<AttoriPopularResponse> PrendiAttoriPopolariTMDB(@Query("api_key") String api_key, @Query("language") String lingua);

    @GET("movie/now_playing")
    Call<NowPlayngResponse> OraInSalaTMDB(@Query("api_key") String api_key, @Query("language") String lingua);

    @GET("genre/movie/list")
    Call<GeneriResponse> PrendiGeneriTMDB(@Query("api_key") String api_key, @Query("language") String lingua);
}
