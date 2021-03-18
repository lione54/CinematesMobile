package com.example.cinematesmobile.Search;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.AdapterView;
import android.widget.Toast;
import com.example.cinematesmobile.BuildConfig;
import com.example.cinematesmobile.R;
import com.example.cinematesmobile.Search.Adapters.AttoriSearchAdapter;
import com.example.cinematesmobile.Search.Adapters.MovieSearchAdapter;
import com.example.cinematesmobile.Search.Client.RetrofitClient;
import com.example.cinematesmobile.Search.Interfaces.RetrofitService;
import com.example.cinematesmobile.Search.Model.AttoriResponse;
import com.example.cinematesmobile.Search.Model.AttoriResponseResults;
import com.example.cinematesmobile.Search.Model.MovieResponse;
import com.example.cinematesmobile.Search.Model.MovieResponseResults;
import com.example.cinematesmobile.SignIn.ConfirmCodeActivity;
import com.example.cinematesmobile.SignIn.SignInActivity;
import com.google.gson.Gson;

import org.angmarch.views.NiceSpinner;

import java.util.ArrayList;
import java.util.List;

import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchActivity extends AppCompatActivity {

    private NiceSpinner sourcSpinner;
    private AppCompatEditText queryEditText;
    private AppCompatButton querySearchButton;
    private String film = "Ricerca Per Titolo Film";
    private String attore = "Ricerca Per Attore";
    private RecyclerView recyclerView;
    private RetrofitService retrofitService;
    private AppCompatImageButton previous;
    private MovieSearchAdapter movieSearchAdapter;
    private AttoriSearchAdapter attoriSearchAdapter;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        sourcSpinner = findViewById(R.id.source_spinner);
        queryEditText = findViewById(R.id.query_edit_text);
        querySearchButton = findViewById(R.id.query_search_button);
        recyclerView = findViewById(R.id.results_recycle_view);
        previous = findViewById(R.id.previously);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        Paper.init(this);
        retrofitService = RetrofitClient.getClient().create(RetrofitService.class);
        final ArrayList<String> category = new ArrayList<>();
        category.add(film);
        category.add(attore);
        sourcSpinner.attachDataSource(category);
        if (Paper.book().read("position") != null) {
            int position = Paper.book().read("position");
            sourcSpinner.setSelectedIndex(position);
        }
        int position = sourcSpinner.getSelectedIndex();
        if (position == 0) {
            queryEditText.setHint("Inserisci Titolo Film...");
        } else {
            queryEditText.setHint("Inserisci Nome Attore...");
        }
        sourcSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    queryEditText.setHint("Inserisci Titolo Film...");
                } else {
                    queryEditText.setHint("Inserisci Nome Attore...");
                }
            }

            @Override public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        /*AGGIUNTA NUOVA*/
        previous.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                onBackPressed();
            }
        });
        /*if (Paper.book().read("cache") != null) {
            String results =Paper.book().read("cache");
            if(Paper.book().read("source") != null){
                String source = Paper.book().read("source");
                if(source.equals("film")){
                    MovieResponse movieResponse = new Gson().fromJson(results,MovieResponse.class);
                    if(movieResponse != null){
                        List<MovieResponseResults> movieResponseResults = movieResponse.getResults();
                        movieSearchAdapter = new MovieSearchAdapter(MainActivity.this, movieResponseResults);
                        recyclerView.setAdapter(movieSearchAdapter);
                        LayoutAnimationController controller = AnimationUtils.loadLayoutAnimation(MainActivity.this,R.anim.layout_scorri_destra);
                        recyclerView.setLayoutAnimation(controller);
                        recyclerView.scheduleLayoutAnimation();
                        Paper.book().write("cache", new Gson().toJson(movieResponseResults));
                        Paper.book().write("source", "film");
                    }
                }else{
                    AttoriResponse attoriResponse = new Gson().fromJson(results,AttoriResponse.class);
                    if(attoriResponse != null){
                        List<AttoriResponseResults> attoriResponseResults = attoriResponse.getResults();
                        attoriSearchAdapter = new AttoriSearchAdapter(MainActivity.this,attoriResponseResults);
                        recyclerView.setAdapter(attoriSearchAdapter);
                        LayoutAnimationController controller = AnimationUtils.loadLayoutAnimation(MainActivity.this,R.anim.layout_scorri_destra);
                        recyclerView.setLayoutAnimation(controller);
                        recyclerView.scheduleLayoutAnimation();
                        Paper.book().write("cache",new Gson().toJson(attoriResponseResults));
                        Paper.book().write("source", "attore");
                    }
                }
            }
        }*/
        querySearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (queryEditText.getText() != null) {
                    String query = queryEditText.getText().toString();
                    String lingua = "it-IT";
                    if (query.equals("") || query.equals(" ")) {
                        Toast.makeText(SearchActivity.this, "Scrivi Qualcosa", Toast.LENGTH_SHORT).show();
                    } else {
                        queryEditText.setText("");
                        String finalQuery = query.replaceAll(" ", "+");
                        if (category.size() > 0) {
                            String NomeCategoria = category.get(sourcSpinner.getSelectedIndex());
                            if (NomeCategoria.equals(film)) {
                                Call<MovieResponse> movieResponseCall = retrofitService.getMoviesByQuery(BuildConfig.THE_MOVIE_DB_APY_KEY, lingua, finalQuery);
                                movieResponseCall.enqueue(new Callback<MovieResponse>() {
                                    @Override public void onResponse(@NonNull Call<MovieResponse> call, @NonNull Response<MovieResponse> response) {
                                        MovieResponse movieResponse = response.body();
                                        if (movieResponse != null) {
                                            List<MovieResponseResults> movieResponseResults = movieResponse.getResults();
                                            recyclerView.setLayoutManager(new LinearLayoutManager(SearchActivity.this, LinearLayoutManager.VERTICAL, false));
                                            movieSearchAdapter = new MovieSearchAdapter(SearchActivity.this, movieResponseResults);
                                            recyclerView.setAdapter(movieSearchAdapter);
                                            LayoutAnimationController controller = AnimationUtils.loadLayoutAnimation(SearchActivity.this, R.anim.layout_scorri_destra);
                                            recyclerView.setLayoutAnimation(controller);
                                            recyclerView.scheduleLayoutAnimation();
                                            Paper.book().write("cache", new Gson().toJson(movieResponseResults));
                                            Paper.book().write("source", "film");
                                        } else {
                                            Toast.makeText(SearchActivity.this, "Nessuna Voce Corrisponde Ai Criteri Di Ricerca.", Toast.LENGTH_SHORT).show();
                                        }

                                    }

                                    @Override public void onFailure(@NonNull Call<MovieResponse> call, @NonNull Throwable t) {
                                        Toast.makeText(SearchActivity.this, "Ops Qualcosa è Andato Storto.", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            } else {
                                Call<AttoriResponse> attoriResponseCall = retrofitService.getPersonByQuery(BuildConfig.THE_MOVIE_DB_APY_KEY, lingua, finalQuery);
                                attoriResponseCall.enqueue(new Callback<AttoriResponse>() {
                                    @Override public void onResponse(@NonNull Call<AttoriResponse> call, @NonNull Response<AttoriResponse> response) {
                                        AttoriResponse attoriResponse = response.body();
                                        if (attoriResponse != null) {
                                            List<AttoriResponseResults> attoriResponseResults = attoriResponse.getResults();
                                            recyclerView.setLayoutManager(new LinearLayoutManager(SearchActivity.this, LinearLayoutManager.VERTICAL, false));
                                            attoriSearchAdapter = new AttoriSearchAdapter(SearchActivity.this, attoriResponseResults);
                                            recyclerView.setAdapter(attoriSearchAdapter);
                                            LayoutAnimationController controller = AnimationUtils.loadLayoutAnimation(SearchActivity.this, R.anim.layout_scorri_destra);
                                            recyclerView.setLayoutAnimation(controller);
                                            recyclerView.scheduleLayoutAnimation();
                                            Paper.book().write("cache", new Gson().toJson(attoriResponseResults));
                                            Paper.book().write("source", "attore");
                                        } else {
                                            Toast.makeText(SearchActivity.this, "Nessuna Voce Corrisponde Ai Criteri Di Ricerca.", Toast.LENGTH_SHORT).show();
                                        }
                                    }

                                    @Override public void onFailure(@NonNull Call<AttoriResponse> call, @NonNull Throwable t) {
                                        Toast.makeText(SearchActivity.this, "Ops Qualcosa è Andato Storto.", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        }
                    }
                }
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        Paper.book().write("position", sourcSpinner.getSelectedIndex());
    }
}
