package com.example.cinematesmobile;

import com.example.cinematesmobile.Frag.SearchFragment;
import com.example.cinematesmobile.ModelDBInterno.DBModelDataUser;
import com.example.cinematesmobile.RetrofitClient.RetrofitClientDBInterno;
import com.example.cinematesmobile.RetrofitClient.RetrofitClientFilm;
import com.example.cinematesmobile.RetrofitService.RetrofitServiceDBInterno;
import com.example.cinematesmobile.RetrofitService.RetrofitServiceFilm;
import com.example.cinematesmobile.Search.ModelMovieActor.AttoriResponse;
import com.example.cinematesmobile.Search.ModelMovieActor.MovieResponse;

import org.junit.Assert;
import org.junit.Test;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RicercaTestWhiteBox {

    private RetrofitServiceFilm retrofitServiceFilm;
    private RetrofitServiceDBInterno retrofitServiceDBInterno;

    @Test (expected = RuntimeException.class)
    public void TestRicercaWhiteBoxPath_2_3_4(){
        SearchFragment searchFragment = new SearchFragment();
        searchFragment.Ricerca(-1, "Avengers");
    }

    @Test(expected = RuntimeException.class)
    public void TestRicercaWhiteBoxPath_2_3_5_6_9(){
        SearchFragment searchFragment = new SearchFragment();
        searchFragment.Ricerca(R.id.radio_ricerca_film, "");
    }

    @Test
    public void TestRicercaWhiteBoxPath_2_3_5_6_10_15_18(){
        retrofitServiceFilm = RetrofitClientFilm.getClient().create(RetrofitServiceFilm.class);
        SearchFragment searchFragment = new SearchFragment();
        searchFragment.Ricerca(R.id.radio_ricerca_film, "asdf");
        Call<MovieResponse> movieResponseCall = retrofitServiceFilm.CercaFilmTMDB(BuildConfig.THE_MOVIE_DB_APY_KEY, "it-IT", "asdf");
        movieResponseCall.enqueue(new Callback<MovieResponse>() {
            @Override public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                MovieResponse movieResponse = response.body();
                Assert.assertNull(movieResponse);
            }
            @Override public void onFailure(Call<MovieResponse> call, Throwable t) {

            }
        });
    }

    @Test
    public void TestRicercaWhiteBoxPath_2_3_5_6_10_15(){
        retrofitServiceFilm = RetrofitClientFilm.getClient().create(RetrofitServiceFilm.class);
        SearchFragment searchFragment = new SearchFragment();
        searchFragment.Ricerca(R.id.radio_ricerca_film, "avengers");
        Call<MovieResponse> movieResponseCall = retrofitServiceFilm.CercaFilmTMDB(BuildConfig.THE_MOVIE_DB_APY_KEY, "it-IT", "avengers");
        movieResponseCall.enqueue(new Callback<MovieResponse>() {
            @Override public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                MovieResponse movieResponse = response.body();
                Assert.assertNull(movieResponse);
            }
            @Override public void onFailure(Call<MovieResponse> call, Throwable t) {

            }
        });
    }

    @Test (expected = RuntimeException.class)
    public void TestRicercaWhiteBoxPath_2_3_5_7_11(){
        SearchFragment searchFragment = new SearchFragment();
        searchFragment.Ricerca(R.id.radio_ricerca_attori, "");
    }

    @Test
    public void TestRicercaWhiteBoxPath_2_3_5_7_12_16(){
        retrofitServiceFilm = RetrofitClientFilm.getClient().create(RetrofitServiceFilm.class);
        SearchFragment searchFragment = new SearchFragment();
        searchFragment.Ricerca(R.id.radio_ricerca_attori, "tom");
        Call<AttoriResponse> attoriResponseCall = retrofitServiceFilm.CercaAttoreTMDB(BuildConfig.THE_MOVIE_DB_APY_KEY, "it-IT", "tom");
        attoriResponseCall.enqueue(new Callback<AttoriResponse>() {
            @Override public void onResponse(Call<AttoriResponse> call, Response<AttoriResponse> response) {
                AttoriResponse attoriResponse = response.body();
                Assert.assertNotNull(attoriResponse);
            }
            @Override public void onFailure(Call<AttoriResponse> call, Throwable t) {

            }
        });
    }

    @Test
    public void TestRicercaWhiteBoxPath_2_3_5_7_12_19(){
        retrofitServiceFilm = RetrofitClientFilm.getClient().create(RetrofitServiceFilm.class);
        SearchFragment searchFragment = new SearchFragment();
        searchFragment.Ricerca(R.id.radio_ricerca_attori, "qwerty");
        Call<AttoriResponse> attoriResponseCall = retrofitServiceFilm.CercaAttoreTMDB(BuildConfig.THE_MOVIE_DB_APY_KEY, "it-IT", "qwerty");
        attoriResponseCall.enqueue(new Callback<AttoriResponse>() {
            @Override public void onResponse(Call<AttoriResponse> call, Response<AttoriResponse> response) {
                AttoriResponse attoriResponse = response.body();
                Assert.assertNull(attoriResponse);
            }
            @Override public void onFailure(Call<AttoriResponse> call, Throwable t) {

            }
        });
    }

    @Test (expected = RuntimeException.class)
    public void TestRicercaWhiteBoxPath_2_3_5_8_13(){
        SearchFragment searchFragment = new SearchFragment();
        searchFragment.Ricerca(R.id.radio_ricerca_amici, "");
    }

    @Test (expected = NullPointerException.class)
    public void TestRicercaWhiteBoxPath_2_3_5_8_14_17_20(){
        retrofitServiceDBInterno = RetrofitClientDBInterno.getClient().create(RetrofitServiceDBInterno.class);
        SearchFragment searchFragment = new SearchFragment();
        searchFragment.Ricerca(R.id.radio_ricerca_amici, "lione64");
        Call<DBModelDataUser> userCall = retrofitServiceDBInterno.getUserByQuery("lione64", "test");
        userCall.enqueue(new Callback<DBModelDataUser>() {
            @Override public void onResponse(Call<DBModelDataUser> call, Response<DBModelDataUser> response) {
                DBModelDataUser dbModelDataUser = response.body();
                Assert.assertNull(dbModelDataUser);
            }
            @Override public void onFailure(Call<DBModelDataUser> call, Throwable t) {

            }
        });
    }

    @Test (expected = NullPointerException.class)
    public void TestRicercaWhiteBoxPath_2_3_5_8_14_17(){
        retrofitServiceDBInterno = RetrofitClientDBInterno.getClient().create(RetrofitServiceDBInterno.class);
        SearchFragment searchFragment = new SearchFragment();
        searchFragment.Ricerca(R.id.radio_ricerca_amici, "lione54");
        Call<DBModelDataUser> userCall = retrofitServiceDBInterno.getUserByQuery("lione54", "test");
        userCall.enqueue(new Callback<DBModelDataUser>() {
            @Override public void onResponse(Call<DBModelDataUser> call, Response<DBModelDataUser> response) {
                DBModelDataUser dbModelDataUser = response.body();
                Assert.assertNotNull(dbModelDataUser);
            }
            @Override public void onFailure(Call<DBModelDataUser> call, Throwable t) {

            }
        });
    }
}
