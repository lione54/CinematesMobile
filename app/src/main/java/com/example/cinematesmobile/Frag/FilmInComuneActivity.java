package com.example.cinematesmobile.Frag;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;

import com.example.cinematesmobile.Frag.Adapter.MovieListInComAdapter;
import com.example.cinematesmobile.Frag.Adapter.MovieListPrefAdapter;
import com.example.cinematesmobile.Frag.Model.DBModelDataFilms;
import com.example.cinematesmobile.Frag.Model.DBModelDataListeFilm;
import com.example.cinematesmobile.ModelDBInterno.DBModelFilmsResponce;
import com.example.cinematesmobile.R;
import com.example.cinematesmobile.RetrofitClient.RetrofitClientDBInterno;
import com.example.cinematesmobile.RetrofitService.RetrofitServiceDBInterno;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FilmInComuneActivity extends AppCompatActivity {

    private String UserProprietario, UserAmico;
    private AppCompatImageButton Previously;
    private RetrofitServiceDBInterno retrofitServiceDBInterno;
    private RecyclerView film;
    private MovieListInComAdapter movieListInComAdapter;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_film_in_comune);
        UserAmico = getIntent().getExtras().getString("Nome_Utente");
        UserProprietario = getIntent().getExtras().getString("Nome_Proprietario");
        Previously = findViewById(R.id.previously_film);
        film = findViewById(R.id.lista_film_in_comune);
        retrofitServiceDBInterno = RetrofitClientDBInterno.getClient().create(RetrofitServiceDBInterno.class);
        Previously.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                onBackPressed();
            }
        });
        Call<DBModelFilmsResponce> filmsResponceCall = retrofitServiceDBInterno.PrendiFilmInComune(UserAmico, UserProprietario);
        filmsResponceCall.enqueue(new Callback<DBModelFilmsResponce>() {
            @Override public void onResponse(Call<DBModelFilmsResponce> call, Response<DBModelFilmsResponce> response) {
                DBModelFilmsResponce dbModelFilmsResponce = response.body();
                if(dbModelFilmsResponce != null){
                    List<DBModelDataFilms> filmList = dbModelFilmsResponce.getResults();
                    if(!(filmList.isEmpty())){
                        film.setLayoutManager(new LinearLayoutManager(FilmInComuneActivity.this, LinearLayoutManager.VERTICAL, false));
                        movieListInComAdapter = new MovieListInComAdapter(FilmInComuneActivity.this, filmList, UserProprietario);
                        film.setAdapter(movieListInComAdapter);
                        LayoutAnimationController controller = AnimationUtils.loadLayoutAnimation(FilmInComuneActivity.this, R.anim.layout_scorri_destra);
                        film.setLayoutAnimation(controller);
                        film.scheduleLayoutAnimation();
                    }else{

                    }
                }else{

                }
            }
            @Override public void onFailure(Call<DBModelFilmsResponce> call, Throwable t) {

            }
        });
    }
}