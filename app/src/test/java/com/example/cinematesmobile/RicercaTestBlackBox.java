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

public class RicercaTestBlackBox {

    private final static int CSEL_CE1 = -1;
    private final static int CSEL_CE2 = R.id.radio_ricerca_film;
    private final static int CSEL_CE3 = R.id.radio_ricerca_attori;
    private final static int CSEL_CE4 = R.id.radio_ricerca_amici;
    private final static String QRY_CE1 = "";
    private final static String QRY_CE2 = "avengers";
    private final static String QRY_CE3 = "tom";
    private final static String QRY_CE4 = "lione54";
    private RetrofitServiceDBInterno retrofitServiceDBInterno;
    private RetrofitServiceFilm retrofitServiceFilm;


    @Test (expected = RuntimeException.class)
    public void TestMetodoCSEL_CE1_QRY_CE1(){
        SearchFragment searchFragment = new SearchFragment();
        searchFragment.Ricerca(CSEL_CE1, QRY_CE1);
    }

    @Test (expected = RuntimeException.class)
    public void TestMetodoCSEL_CE1_QRY_CE2(){
        SearchFragment searchFragment = new SearchFragment();
        searchFragment.Ricerca(CSEL_CE1, QRY_CE2);
    }

    @Test (expected = RuntimeException.class)
    public void TestMetodoCSEL_CE2_QRY_CE1(){
        SearchFragment searchFragment = new SearchFragment();
        searchFragment.Ricerca(CSEL_CE2, QRY_CE1);
    }

    @Test
    public void TestMetodoCSEL_CE2_QRY_CE2(){
        retrofitServiceFilm = RetrofitClientFilm.getClient().create(RetrofitServiceFilm .class);
        SearchFragment searchFragment = new SearchFragment();
        searchFragment.Ricerca(CSEL_CE2, QRY_CE2);
        Call<MovieResponse> movieResponseCall = retrofitServiceFilm.CercaFilmTMDB(BuildConfig.THE_MOVIE_DB_APY_KEY, "it-IT", QRY_CE2);
        movieResponseCall.enqueue(new Callback<MovieResponse>() {
            @Override public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                MovieResponse movieResponse = response.body();
                Assert.assertNotNull(movieResponse);
            }
            @Override public void onFailure(Call<MovieResponse> call, Throwable t) {

            }
        });
    }

    @Test
    public void TestMetodoCSEL_CE3_QRY_CE3(){
        retrofitServiceFilm = RetrofitClientFilm.getClient().create(RetrofitServiceFilm .class);
        SearchFragment searchFragment = new SearchFragment();
        searchFragment.Ricerca(CSEL_CE3, QRY_CE3);
        Call<AttoriResponse> attoriResponseCall = retrofitServiceFilm.CercaAttoreTMDB(BuildConfig.THE_MOVIE_DB_APY_KEY, "it-IT", QRY_CE3);
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
    public void TestMetodoCSEL_CE4_QRY_CE4(){
        retrofitServiceDBInterno = RetrofitClientDBInterno.getClient().create(RetrofitServiceDBInterno .class);
        SearchFragment searchFragment = new SearchFragment();
        searchFragment.Ricerca(CSEL_CE4, QRY_CE4);
        Call<DBModelDataUser> userCall = retrofitServiceDBInterno.getUserByQuery(QRY_CE4, "test");
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
