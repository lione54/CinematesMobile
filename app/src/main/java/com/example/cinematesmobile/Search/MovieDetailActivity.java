package com.example.cinematesmobile.Search;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.cinematesmobile.BuildConfig;
import com.example.cinematesmobile.R;
import com.example.cinematesmobile.Search.Adapters.ImmagineProfiloAttoriAdapter;
import com.example.cinematesmobile.Search.Client.RetrofitClient;
import com.example.cinematesmobile.Search.Interfaces.RetrofitService;
import com.example.cinematesmobile.Search.Model.AttoriImmageResult;
import com.example.cinematesmobile.Search.Model.Generi;
import com.example.cinematesmobile.Search.Model.MovieDetail;
import com.example.cinematesmobile.Search.Model.MovieImage;
import com.example.cinematesmobile.Search.Model.Produttori;
import com.flaviofaria.kenburnsview.KenBurnsView;
import com.github.ivbaranov.mfb.MaterialFavoriteButton;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
public class MovieDetailActivity extends AppCompatActivity {

    private KenBurnsView MovieDetailsImageView;
    private LinearLayoutCompat filmnomeoriginalelayout;
    private LinearLayoutCompat filmdatauscitalayout;
    private LinearLayoutCompat filmlinguaoriginalelayout;
    private LinearLayoutCompat filmtramalayout;
    private LinearLayoutCompat FilmImageLayout;
    private LinearLayoutCompat filmDetailStatoLayout;
    private LinearLayoutCompat filmDetailGenereLayout;
    private LinearLayoutCompat filmDetailProduzioneLayout;
    private AppCompatTextView filmnomeoriginale;
    private AppCompatTextView filmdatauscita;
    private AppCompatTextView filmlinguaoriginale;
    private AppCompatTextView filmtrama;
    private AppCompatTextView titolo;
    private AppCompatTextView filmDetailStato;
    private AppCompatTextView filmDetailGenere;
    private AppCompatTextView filmDetailProduzione;
    private RecyclerView ImmagineFilmRecycleView;
    private ImmagineProfiloAttoriAdapter immagineProfiloAttoriAdapter;
    private ImageView Locandina;
    private MaterialFavoriteButton CuorePreferiti;
    private RetrofitService retrofitService;
    private RetrofitService retrofitService2;
    private AppCompatButton aggiungiA;
    private String preferiti = "Lista Preferiti";
    private AppCompatTextView rates;
    private boolean stato;
    private LinearLayoutCompat rates_layout;
    private AppCompatImageButton previous;
    public static final String JSON_ARRAY = "dbdata";
    private static final String INSURL = "http://192.168.1.9/cinematesdb/AggiungiAPreferiti.php";
    private static final String VERURL = "http://192.168.1.9/cinematesdb/VerificaSePresente.php";
    private static final String RIMURL = "http://192.168.1.9/cinematesdb/RimuoviDaiPreferiti.php";

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        aggiungiA = findViewById(R.id.source_spinner_aggiungi_a);
        MovieDetailsImageView = findViewById(R.id.dettagli_film_image_view);
        filmnomeoriginalelayout = findViewById(R.id.film_dettagli_nome_originale_layout);
        filmdatauscitalayout = findViewById(R.id.Film_datauscita_detail_layout);
        filmlinguaoriginalelayout = findViewById(R.id.Film_lingua_originale_layout);
        filmtramalayout = findViewById(R.id.Film_trama_layout);
        FilmImageLayout = findViewById(R.id.film_detail_images_layout);
        filmDetailStatoLayout =findViewById(R.id.film_detail_stato_layout);
        filmDetailGenereLayout =findViewById(R.id.film_detail_genere_layout);
        filmDetailProduzioneLayout = findViewById(R.id.film_detail_produzione_layout);
        filmnomeoriginale = findViewById(R.id.Film_nome_originale);
        filmdatauscita = findViewById(R.id.film_datauscita);
        filmlinguaoriginale = findViewById(R.id.Film_linguaoriginale);
        filmtrama = findViewById(R.id.film_trama);
        titolo = findViewById(R.id.Film_dettagli_nome);
        filmDetailStato = findViewById(R.id.film_detail_stato);
        filmDetailGenere = findViewById(R.id.film_detail_genere);
        filmDetailProduzione = findViewById(R.id.film_detail_produzione);
        ImmagineFilmRecycleView = findViewById(R.id.film_detail__image_recyclerView);
        Locandina =  findViewById(R.id.film_poster);
        rates = findViewById(R.id.film_detail_rated);
        rates_layout = findViewById(R.id.film_detail_rated_layout);
        previous = findViewById(R.id.previously);
        CuorePreferiti = findViewById(R.id.preferiticuore);
        ImmagineFilmRecycleView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL,false));
        final ArrayList<String> listefilm = new ArrayList<>();
        listefilm.add(preferiti);
        String lingua = "it-IT";
        Intent intent = getIntent();
        StringBuilder stringTitolo = new StringBuilder();
        StringBuilder stringPoster = new StringBuilder();
        retrofitService = RetrofitClient.getClient().create(RetrofitService.class);
        retrofitService2 = RetrofitClient.getClient().create(RetrofitService.class);
        if ( intent != null && intent.getExtras()!= null ){
            if(intent.getExtras().getString("id")!=null) {
                int id = Integer.parseInt(intent.getExtras().getString("id"));
                verificaSePresente(id);
                Call<MovieDetail> movieDetailCall = retrofitService2.getMovieDetail(id, BuildConfig.THE_MOVIE_DB_APY_KEY,lingua);
                movieDetailCall.enqueue(new Callback<MovieDetail>() {
                    @Override public void onResponse(@NonNull Call<MovieDetail> call,@NonNull Response<MovieDetail> response) {
                        MovieDetail movieDetailResponse = response.body();
                        if(movieDetailResponse != null){
                            prepareMovieDetails(movieDetailResponse);
                            stringPoster.append(movieDetailResponse.getBackdrop_path());
                            stringTitolo.append(movieDetailResponse.getTitle());
                        }else{
                            Toast.makeText(MovieDetailActivity.this,"Nessun dettaglio trovato",Toast.LENGTH_SHORT).show();
                        }
                    }
                    @Override public void onFailure(@NonNull Call<MovieDetail> call,@NonNull Throwable t) {
                        Toast.makeText(MovieDetailActivity.this,"Ops Qualcosa è Andato Storto",Toast.LENGTH_SHORT).show();
                    }
                });
                Call<MovieImage> movieImageCall = retrofitService.getMovieImage(id, BuildConfig.THE_MOVIE_DB_APY_KEY);
                movieImageCall.enqueue(new Callback<MovieImage>() {
                    @Override public void onResponse(Call<MovieImage> call, Response<MovieImage> response) {
                        MovieImage movieImage = response.body();
                        if(movieImage != null){
                            List<AttoriImmageResult> attoriImmageResultList = movieImage.getPosters();
                            if(attoriImmageResultList != null && attoriImmageResultList.size() > 0){
                                FilmImageLayout.setVisibility(View.VISIBLE);
                                immagineProfiloAttoriAdapter = new ImmagineProfiloAttoriAdapter(MovieDetailActivity.this,attoriImmageResultList);
                                ImmagineFilmRecycleView.setAdapter(immagineProfiloAttoriAdapter);
                                LayoutAnimationController controller = AnimationUtils.loadLayoutAnimation(MovieDetailActivity.this,R.anim.layout_scorri_destra);
                                ImmagineFilmRecycleView.setLayoutAnimation(controller);
                                ImmagineFilmRecycleView.scheduleLayoutAnimation();
                            }else{
                                FilmImageLayout.setVisibility(View.GONE);
                            }
                        }
                    }
                    @Override public void onFailure(Call<MovieImage> call, Throwable t) {
                        Toast.makeText(MovieDetailActivity.this, "Ops Qualcosa è Andato Storto",Toast.LENGTH_SHORT).show();
                    }
                });

                    CuorePreferiti.setOnFavoriteChangeListener(new MaterialFavoriteButton.OnFavoriteChangeListener() {
                        @Override public void onFavoriteChanged(MaterialFavoriteButton buttonView, boolean favorite) {
                            if(stato == false) {
                                InserisciNeiPreferiti(id, stringPoster.toString(), stringTitolo.toString());
                                CuorePreferiti.setImageResource(R.drawable.ic_likeactive);
                                stato = true;
                            }else {
                                RimuoviDaiPreferiti(id);
                                CuorePreferiti.setImageResource(R.drawable.ic_like);
                                recreate();
                            }
                        }
                    });
                /*AGGIUNTA NUOVA*/
                previous.setOnClickListener(new View.OnClickListener() {
                    @Override public void onClick(View v) {
                        onBackPressed();
                    }
                });
            }
        }
    }

    private void RimuoviDaiPreferiti(int id) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, RIMURL, new com.android.volley.Response.Listener<String>() {
            @Override public void onResponse(String response){
                Toast.makeText(MovieDetailActivity.this , "Film Rimosso Dalla Lista Dei Preferiti.", Toast.LENGTH_LONG).show();
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override public void onErrorResponse(VolleyError error) {
                Toast.makeText(MovieDetailActivity.this , error.toString(), Toast.LENGTH_LONG).show();
            }
        }) {
            @NotNull @Override protected Map<String, String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("Id_Film_Inserito", String.valueOf(id));
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void verificaSePresente(int id) {
        final int[] validiti = new int[1];
        StringRequest stringRequest = new StringRequest(Request.Method.POST, VERURL, new com.android.volley.Response.Listener<String>() {
            @Override public void onResponse(String response) {
                try{
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray array = jsonObject.getJSONArray(JSON_ARRAY);
                    for(int i = 0; i < array.length(); i++) {
                        JSONObject object = array.getJSONObject(i);
                        String respo = object.getString("id_film_inserito");
                        validiti[0] = Integer.parseInt(respo);
                    }
                    if(validiti[0] == 0) {
                        CuorePreferiti.setImageResource(R.drawable.ic_like);
                        stato = false;
                    }else{
                        CuorePreferiti.setImageResource(R.drawable.ic_likeactive);
                        stato = true;
                    }
                }catch (Exception e){
                    Toast.makeText(MovieDetailActivity.this, "" + e, Toast.LENGTH_LONG).show();
                }
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override public void onErrorResponse(VolleyError error) {
                Toast.makeText(MovieDetailActivity.this, error.toString(), Toast.LENGTH_LONG).show();
            }
        })
        {
            @NotNull @Override protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("Id_Film_Inserito", String.valueOf(id));
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    protected void InserisciNeiPreferiti(int id, String poster, String titolo) {
        String utente = "mattia.golino@gmail.com";
        String tipoLista = "Preferiti";
        String titoloMod = titolo.replaceAll("'", "/");
        StringRequest stringRequest = new StringRequest(Request.Method.POST, INSURL, new com.android.volley.Response.Listener<String>() {
            @Override public void onResponse(String response){
                    Toast.makeText(MovieDetailActivity.this , "Film Aggiunto Nella Lista Dei Presferiti.", Toast.LENGTH_LONG).show();
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override public void onErrorResponse(VolleyError error) {
                Toast.makeText(MovieDetailActivity.this , error.toString(), Toast.LENGTH_LONG).show();
            }
        }) {
            @NotNull @Override protected Map<String, String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("User_Proprietario", utente);
                params.put("Tipo_Lista",tipoLista);
                params.put("Id_Film_Inserito", String.valueOf(id));
                params.put("Titolo_Film", titoloMod);
                params.put("Url_Immagine", poster);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }


    private void prepareMovieDetails(@NotNull MovieDetail movieDetailsResponse) {
        String name = movieDetailsResponse.getTitle();
        String titolooriginale = movieDetailsResponse.getOriginal_title();
        String linguaoriginale = movieDetailsResponse.getOriginal_language();
        String datauscita = movieDetailsResponse.getRelease_date();
        String trama = movieDetailsResponse.getOverview();
        String posterPath = movieDetailsResponse.getBackdrop_path();
        String stato = movieDetailsResponse.getStatus();
        List<Generi> Genere = movieDetailsResponse.getGenres();
        List<Produttori> produttori = movieDetailsResponse.getProduction_companies();
        String locandina = movieDetailsResponse.getPoster_path();
        Float ratings = movieDetailsResponse.getVote_average();
        Picasso.with(this).load(posterPath).into(MovieDetailsImageView);
        Picasso.with(this).load(locandina).into(Locandina);
        if (name!=null){
            if(name.length()>0){
                titolo.setText(name);
                titolo.setVisibility(View.VISIBLE);
            }else{
                titolo.setVisibility(View.GONE);
            }
        }else{
            titolo.setVisibility(View.GONE);
        }
        if (titolooriginale!=null){
            if(titolooriginale.length()>0){
                filmnomeoriginale.setText(titolooriginale);
                filmnomeoriginalelayout.setVisibility(View.VISIBLE);
            }else{
                filmnomeoriginalelayout.setVisibility(View.GONE);
            }
        }else{
            filmnomeoriginalelayout.setVisibility(View.GONE);
        }
        if (linguaoriginale!=null){
            if(linguaoriginale.length()>0){
                filmlinguaoriginale.setText(linguaoriginale);
                filmlinguaoriginalelayout.setVisibility(View.VISIBLE);
            }else{
                filmlinguaoriginalelayout.setVisibility(View.GONE);
            }
        }else{
            filmlinguaoriginalelayout.setVisibility(View.GONE);
        }
        if (datauscita!=null){
            if(datauscita.length()>0){
                filmdatauscita.setText(datauscita);
                filmdatauscitalayout.setVisibility(View.VISIBLE);
            }else{
                filmdatauscitalayout.setVisibility(View.GONE);
            }
        }else{
            filmdatauscitalayout.setVisibility(View.GONE);
        }
        if (trama!=null){
            if(trama.length()>0){
                filmtrama.setText(trama);
                filmtramalayout.setVisibility(View.VISIBLE);
            }else{
                filmtramalayout.setVisibility(View.GONE);
            }
        }else{
            filmtramalayout.setVisibility(View.GONE);
        }
        if(stato != null){
            if(stato.length() > 0){
                filmDetailStato.setText(stato);
                filmDetailStatoLayout.setVisibility(View.VISIBLE);
            }else{
                filmDetailStatoLayout.setVisibility(View.GONE);
            }
        }else{
            filmDetailStatoLayout.setVisibility(View.GONE);
        }
        if(Genere != null) {
            if (Genere.size() > 0) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append(Genere.stream().map(generi -> generi.getName()).collect(Collectors.toList()));
                String geners = stringBuilder.toString().replaceAll("\\[", "");
                String gen = geners.replaceAll("\\]","");
                filmDetailGenere.setText(gen);
                filmDetailGenereLayout.setVisibility(View.VISIBLE);
            } else {
                filmDetailGenereLayout.setVisibility(View.GONE);
            }
        }else {
            filmDetailGenereLayout.setVisibility(View.GONE);
        }
        if(produttori != null) {
            if (produttori.size() > 0) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append(produttori.stream().map(generi -> generi.getName()).collect(Collectors.toList()));
                String prodactors = stringBuilder.toString().replaceAll("\\[", "");
                String prod = prodactors.replaceAll("\\]","");
                filmDetailProduzione.setText(prod);
                filmDetailProduzioneLayout.setVisibility(View.VISIBLE);
            } else {
                filmDetailProduzioneLayout.setVisibility(View.GONE);
            }
        }else {
            filmDetailProduzioneLayout.setVisibility(View.GONE);
        }
        if(ratings != null){
            if(ratings > 0){
                String Punteggio = Float.toString(ratings);
                rates.setText(Punteggio);
                rates_layout.setVisibility(View.VISIBLE);
            }else{
                rates.setText("TBA");
                rates_layout.setVisibility(View.VISIBLE);

            }
        }else{
            rates_layout.setVisibility(View.GONE);
        }
    }



    @Override public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
    }
}

