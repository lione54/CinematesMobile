package com.example.cinematesmobile.Search;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Toast;
import com.example.cinematesmobile.BuildConfig;
import com.example.cinematesmobile.Frag.Model.DBModelAttributiListaResults;
import com.example.cinematesmobile.Frag.Model.DBModelDataListeFilm;
import com.example.cinematesmobile.Frag.Model.DBModelDettagliCinematesResponce;
import com.example.cinematesmobile.ModelDBInterno.DBModelAttributiLista;
import com.example.cinematesmobile.ModelDBInterno.DBModelDataListeFilmResponce;
import com.example.cinematesmobile.ModelDBInterno.DBModelDettagliCinemates;
import com.example.cinematesmobile.ModelDBInterno.DBModelResponseToInsert;
import com.example.cinematesmobile.ModelDBInterno.DBModelVerifica;
import com.example.cinematesmobile.ModelDBInterno.DBModelVerificaResults;
import com.example.cinematesmobile.R;
import com.example.cinematesmobile.Recensioni.RecensioniActivity;
import com.example.cinematesmobile.RetrofitClient.RetrofitClientDBInterno;
import com.example.cinematesmobile.RetrofitService.RetrofitServiceDBInterno;
import com.example.cinematesmobile.Search.Adapters.ImmagineProfiloAttoriAdapter;
import com.example.cinematesmobile.RetrofitClient.RetrofitClientFilm;
import com.example.cinematesmobile.RetrofitService.RetrofitServiceFilm;
import com.example.cinematesmobile.Search.ModelMovieActor.AttoriImmageResult;
import com.example.cinematesmobile.Search.ModelMovieActor.Generi;
import com.example.cinematesmobile.Search.ModelMovieActor.MovieDetail;
import com.example.cinematesmobile.Search.ModelMovieActor.MovieImage;
import com.example.cinematesmobile.Search.ModelMovieActor.Produttori;
import com.flaviofaria.kenburnsview.KenBurnsView;
import com.github.ivbaranov.mfb.MaterialFavoriteButton;
import com.google.android.material.textfield.TextInputEditText;
import com.squareup.picasso.Picasso;
import org.angmarch.views.NiceSpinner;
import org.jetbrains.annotations.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
public class MovieDetailActivity extends AppCompatActivity {

    private AppCompatButton Recensioni;
    private AlertDialog.Builder dialogBilder, dialogBilder2;
    private AlertDialog CreaLista, Spoiler;
    private AppCompatButton Conferma,Annulla, Ok;
    private TextInputEditText InserisciTitolo, InserisciDescrizione;
    private RadioGroup visibility;
    private String id_film;
    private KenBurnsView MovieDetailsImageView;
    private LinearLayoutCompat filmnomeoriginalelayout,filmdatauscitalayout, filmlinguaoriginalelayout, filmtramalayout, FilmImageLayout, filmDetailStatoLayout, filmDetailGenereLayout, filmDetailProduzioneLayout;
    private AppCompatTextView filmnomeoriginale, filmdatauscita,  filmlinguaoriginale,  filmtrama, titolo, filmDetailStato, filmDetailGenere, filmDetailProduzione;
    private RecyclerView ImmagineFilmRecycleView;
    private ImmagineProfiloAttoriAdapter immagineProfiloAttoriAdapter;
    private ImageView Locandina;
    private String tipoLista = null;
    private MaterialFavoriteButton CuorePreferiti, OcchialiDaVedere;
    private RetrofitServiceFilm retrofitServiceFilm, retrofitServiceFilm2;
    private RetrofitServiceDBInterno retrofitServiceDBInterno;
    private NiceSpinner aggiungiA;
    private String preferiti = "Aggiungi A...", crealista = "Nuova Lista", Descrizione = "null", Visibilità = "null", UserName;
    private AppCompatTextView rates;
    private boolean stato, stato_V;
    private boolean firstuse = true;
    private LinearLayoutCompat rates_layout;
    private AppCompatImageButton previous;
    final ArrayList<String> listefilm = new ArrayList<>();
    private Integer Numero_Recensioni;
    private double Valutazione_Media;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        UserName = getIntent().getExtras().getString("UsernameProprietario");
        Recensioni = findViewById(R.id.button_recensioni);
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
        OcchialiDaVedere = findViewById(R.id.occhialidavedere);
        ImmagineFilmRecycleView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL,false));
        retrofitServiceDBInterno = RetrofitClientDBInterno.getClient().create(RetrofitServiceDBInterno.class);
        if(firstuse) {
            firstuse = false;
            listefilm.add(preferiti);
            listefilm.add(crealista);
            Call<DBModelDataListeFilmResponce> listePresentiCall = retrofitServiceDBInterno.TrovaListe(UserName);
            listePresentiCall.enqueue(new Callback<DBModelDataListeFilmResponce>() {
                @Override public void onResponse(@NonNull Call<DBModelDataListeFilmResponce> call,@NonNull Response<DBModelDataListeFilmResponce> response) {
                     DBModelDataListeFilmResponce dbModelDataListeFilmResponce = response.body();
                    if(dbModelDataListeFilmResponce != null){
                        List<DBModelDataListeFilm> dbModelDataListeFilms = dbModelDataListeFilmResponce.getListeFilms();
                        if(!(dbModelDataListeFilms.isEmpty())){
                            for(int i = 0; i < dbModelDataListeFilms.size(); i++){
                                listefilm.add(dbModelDataListeFilms.get(i).getTitoloLista());
                            }
                            aggiungiA.attachDataSource(listefilm);
                        }
                    }else{
                        Toast.makeText(MovieDetailActivity.this, "Liste non trovate",Toast.LENGTH_SHORT).show();
                        aggiungiA.attachDataSource(listefilm);
                    }
                    aggiungiA.attachDataSource(listefilm);
                }
                @Override public void onFailure(@NonNull Call<DBModelDataListeFilmResponce> call,@NonNull Throwable t) {
                    Toast.makeText(MovieDetailActivity.this, "Ops qualcosa è andato storto",Toast.LENGTH_SHORT).show();
                }
            });
        }else{
            Call<DBModelDataListeFilmResponce> listePresentiCall = retrofitServiceDBInterno.TrovaListe(UserName);
            listePresentiCall.enqueue(new Callback<DBModelDataListeFilmResponce>() {
                @Override public void onResponse(@NonNull Call<DBModelDataListeFilmResponce> call,@NonNull Response<DBModelDataListeFilmResponce> response) {
                    DBModelDataListeFilmResponce dbModelDataListeFilmResponce = response.body();
                    if(dbModelDataListeFilmResponce != null){
                        List<DBModelDataListeFilm> dbModelDataListeFilms = dbModelDataListeFilmResponce.getListeFilms();
                        if(!(dbModelDataListeFilms.isEmpty())){
                            for(int i = 0; i < dbModelDataListeFilms.size(); i++){
                                listefilm.add(dbModelDataListeFilms.get(i).getTitoloLista());
                            }
                            aggiungiA.attachDataSource(listefilm);
                        }
                    }else{
                        Toast.makeText(MovieDetailActivity.this, "Liste non trovate",Toast.LENGTH_SHORT).show();
                        aggiungiA.attachDataSource(listefilm);
                    }
                    aggiungiA.attachDataSource(listefilm);
                }
                @Override public void onFailure(@NonNull Call<DBModelDataListeFilmResponce> call,@NonNull Throwable t) {
                    Toast.makeText(MovieDetailActivity.this, "Ops qualcosa è andato storto",Toast.LENGTH_SHORT).show();
                }
            });
        }
        String lingua = "it-IT";
        Intent intent = getIntent();
        StringBuilder stringTitolo = new StringBuilder();
        StringBuilder stringPoster = new StringBuilder();
        retrofitServiceFilm = RetrofitClientFilm.getClient().create(RetrofitServiceFilm.class);
        retrofitServiceFilm2 = RetrofitClientFilm.getClient().create(RetrofitServiceFilm.class);
        if ( intent != null && intent.getExtras()!= null ){
            if(intent.getExtras().getString("id")!=null) {
                int id = Integer.parseInt(intent.getExtras().getString("id"));
                Call<DBModelVerifica> verificaPrefCall = retrofitServiceDBInterno.VerificaSePresenteNeiPreferiti(String.valueOf(id), UserName);
                verificaPrefCall.enqueue(new Callback<DBModelVerifica>() {
                    @Override public void onResponse(@NonNull Call<DBModelVerifica> call,@NonNull Response<DBModelVerifica> response) {
                        DBModelVerifica dbModelVerifica = response.body();
                        if(dbModelVerifica != null){
                            List<DBModelVerificaResults> verificaResults = dbModelVerifica.getResults();
                            if (verificaResults.get(0).getCodVerifica() == 0){
                                CuorePreferiti.setImageResource(R.drawable.ic_like);
                                stato = false;
                            }else{
                                CuorePreferiti.setImageResource(R.drawable.ic_likeactive);
                                stato = true;
                            }
                        }else {
                            Toast.makeText(MovieDetailActivity.this, "Verifica fallita",Toast.LENGTH_SHORT).show();
                        }
                    }
                    @Override public void onFailure(@NonNull Call<DBModelVerifica> call,@NonNull Throwable t) {
                        Toast.makeText(MovieDetailActivity.this, "Ops qualcosa è andato storto",Toast.LENGTH_SHORT).show();
                    }
                });
                Call<DBModelVerifica> verificaDaVedCall = retrofitServiceDBInterno.VerificaSePresenteNeiDaVedere(String.valueOf(id), UserName);
                verificaDaVedCall.enqueue(new Callback<DBModelVerifica>() {
                    @Override public void onResponse(@NonNull Call<DBModelVerifica> call,@NonNull Response<DBModelVerifica> response) {
                        DBModelVerifica dbModelVerifica = response.body();
                        if(dbModelVerifica != null){
                            List<DBModelVerificaResults> verificaResults = dbModelVerifica.getResults();
                            if (verificaResults.get(0).getCodVerifica() == 0){
                                OcchialiDaVedere.setImageResource(R.drawable.ic__d_glasses);
                                stato_V = false;
                            }else{
                                OcchialiDaVedere.setImageResource(R.drawable.ic__d_glasses_active);
                                stato_V = true;
                            }
                        }else{
                            Toast.makeText(MovieDetailActivity.this, "Verifica fallita",Toast.LENGTH_SHORT).show();
                        }
                    }
                    @Override public void onFailure(@NonNull Call<DBModelVerifica> call,@NonNull Throwable t) {
                        Toast.makeText(MovieDetailActivity.this, "Ops qualcosa è andato storto",Toast.LENGTH_SHORT).show();
                    }
                });
                Call<MovieDetail> movieDetailCall = retrofitServiceFilm2.PredndiDettagliFilmTMDB(id, BuildConfig.THE_MOVIE_DB_APY_KEY,lingua);
                movieDetailCall.enqueue(new Callback<MovieDetail>() {
                    @Override public void onResponse(@NonNull Call<MovieDetail> call,@NonNull Response<MovieDetail> response) {
                        MovieDetail movieDetailResponse = response.body();
                        if(movieDetailResponse != null){
                            prepareMovieDetails(movieDetailResponse);
                            id_film = String.valueOf(intent.getExtras().getString("id"));
                            stringPoster.append(movieDetailResponse.getPoster_path());
                            stringTitolo.append(movieDetailResponse.getTitle());
                        }else{
                            Toast.makeText(MovieDetailActivity.this,"Nessun dettaglio trovato",Toast.LENGTH_SHORT).show();
                        }
                    }
                    @Override public void onFailure(@NonNull Call<MovieDetail> call,@NonNull Throwable t) {
                        Toast.makeText(MovieDetailActivity.this,"Ops qualcosa è andato storto",Toast.LENGTH_SHORT).show();
                    }
                });
                Call<MovieImage> movieImageCall = retrofitServiceFilm.PrendiImmaginiFilmTMDB(id, BuildConfig.THE_MOVIE_DB_APY_KEY);
                movieImageCall.enqueue(new Callback<MovieImage>() {
                    @Override public void onResponse(@NonNull Call<MovieImage> call,@NonNull Response<MovieImage> response) {
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
                    @Override public void onFailure(@NonNull Call<MovieImage> call,@NonNull Throwable t) {
                        Toast.makeText(MovieDetailActivity.this, "Ops qualcosa è andato storto",Toast.LENGTH_SHORT).show();
                    }
                });
                CuorePreferiti.setOnClickListener(new View.OnClickListener() {
                    @Override public void onClick(View v) {
                        if(!stato) {
                            tipoLista = "Preferiti";
                            Visibilità = "Solo Amici";
                            Descrizione = "null";
                            Call<DBModelResponseToInsert> aggiungiCall = retrofitServiceDBInterno.AggiungiFilmAlDatabase(String.valueOf(id), tipoLista, UserName, stringTitolo.toString(), stringPoster.toString(), Descrizione, Visibilità);
                            aggiungiCall.enqueue(new Callback<DBModelResponseToInsert>() {
                                @Override public void onResponse(@NonNull Call<DBModelResponseToInsert> call,@NonNull Response<DBModelResponseToInsert> response) {
                                    DBModelResponseToInsert dbModelResponseToInsert = response.body();
                                    if(dbModelResponseToInsert != null){
                                        if(dbModelResponseToInsert.getStato().equals("Successfull")){
                                            if(tipoLista.equals("Preferiti") || tipoLista.equals("Da Vedere")){
                                                if(tipoLista.equals("Preferiti")){
                                                    Call<DBModelVerifica> verificaPrefCall = retrofitServiceDBInterno.VerificaSePresenteNeiPreferiti(String.valueOf(id), UserName);
                                                    verificaPrefCall.enqueue(new Callback<DBModelVerifica>() {
                                                        @Override public void onResponse(@NonNull Call<DBModelVerifica> call,@NonNull Response<DBModelVerifica> response) {
                                                            DBModelVerifica dbModelVerifica = response.body();
                                                            if(dbModelVerifica != null){
                                                                List<DBModelVerificaResults> verificaResults = dbModelVerifica.getResults();
                                                                if (verificaResults.get(0).getCodVerifica() == 0){
                                                                    CuorePreferiti.setImageResource(R.drawable.ic_like);
                                                                    stato = false;
                                                                }else{
                                                                    CuorePreferiti.setImageResource(R.drawable.ic_likeactive);
                                                                    stato = true;
                                                                    Toast.makeText(MovieDetailActivity.this, "Film aggiunto nella lista " + tipoLista + ".", Toast.LENGTH_SHORT).show();
                                                                }
                                                            }else {
                                                                Toast.makeText(MovieDetailActivity.this, "Verifica fallita",Toast.LENGTH_SHORT).show();
                                                            }
                                                        }
                                                        @Override public void onFailure(@NonNull Call<DBModelVerifica> call,@NonNull Throwable t) {
                                                            Toast.makeText(MovieDetailActivity.this, "Ops qualcosa è andato storto",Toast.LENGTH_SHORT).show();
                                                        }
                                                    });
                                                } else{
                                                    Call<DBModelVerifica> verificaDaVedCall = retrofitServiceDBInterno.VerificaSePresenteNeiDaVedere(String.valueOf(id), UserName);
                                                    verificaDaVedCall.enqueue(new Callback<DBModelVerifica>() {
                                                        @Override public void onResponse(@NonNull Call<DBModelVerifica> call,@NonNull Response<DBModelVerifica> response) {
                                                            DBModelVerifica dbModelVerifica = response.body();
                                                            if(dbModelVerifica != null){
                                                                List<DBModelVerificaResults> verificaResults = dbModelVerifica.getResults();
                                                                if (verificaResults.get(0).getCodVerifica() == 0){
                                                                    OcchialiDaVedere.setImageResource(R.drawable.ic__d_glasses);
                                                                    stato_V = false;
                                                                }else{
                                                                    OcchialiDaVedere.setImageResource(R.drawable.ic__d_glasses_active);
                                                                    stato_V = true;
                                                                    Toast.makeText(MovieDetailActivity.this, "Film aggiunto nella lista " + tipoLista + ".", Toast.LENGTH_SHORT).show();
                                                                }
                                                            }else{
                                                                Toast.makeText(MovieDetailActivity.this, "Verifica fallita",Toast.LENGTH_SHORT).show();
                                                            }
                                                        }
                                                        @Override public void onFailure(@NonNull Call<DBModelVerifica> call,@NonNull Throwable t) {
                                                            Toast.makeText(MovieDetailActivity.this, "Ops qualcosa è andato storto",Toast.LENGTH_SHORT).show();
                                                        }
                                                    });
                                                }
                                            }else {
                                                aggiungiA.setSelectedIndex(0);
                                                Toast.makeText(MovieDetailActivity.this, "Film aggiunto nella lista " + tipoLista + ".", Toast.LENGTH_SHORT).show();
                                            }
                                        }else{
                                            Toast.makeText(MovieDetailActivity.this, "Aggiunta film fallita",Toast.LENGTH_SHORT).show();
                                        }
                                    }else{
                                        Toast.makeText(MovieDetailActivity.this, "Impossibile aggiungere il film",Toast.LENGTH_SHORT).show();
                                    }
                                }
                                @Override public void onFailure(@NonNull Call<DBModelResponseToInsert> call,@NonNull Throwable t) {
                                    Toast.makeText(MovieDetailActivity.this, "Ops qualcosa è andato storto",Toast.LENGTH_SHORT).show();
                                }
                            });
                        }else {
                            Call<DBModelResponseToInsert> rimprefCall = retrofitServiceDBInterno.RimuoviFilmDaPreferiti(String.valueOf(id), UserName);
                            rimprefCall.enqueue(new Callback<DBModelResponseToInsert>() {
                                @Override public void onResponse(@NonNull Call<DBModelResponseToInsert> call,@NonNull Response<DBModelResponseToInsert> response) {
                                    DBModelResponseToInsert dbModelResponseToInsert = response.body();
                                    if(dbModelResponseToInsert != null){
                                        if (dbModelResponseToInsert.getStato().equals("Successfull")){
                                            Call<DBModelVerifica> verificaPrefCall = retrofitServiceDBInterno.VerificaSePresenteNeiPreferiti(String.valueOf(id), UserName);
                                            verificaPrefCall.enqueue(new Callback<DBModelVerifica>() {
                                                @Override public void onResponse(@NonNull Call<DBModelVerifica> call,@NonNull Response<DBModelVerifica> response) {
                                                    DBModelVerifica dbModelVerifica = response.body();
                                                    if(dbModelVerifica != null){
                                                        List<DBModelVerificaResults> verificaResults = dbModelVerifica.getResults();
                                                        if (verificaResults.get(0).getCodVerifica() == 0){
                                                            CuorePreferiti.setImageResource(R.drawable.ic_like);
                                                            stato = false;
                                                            Toast.makeText(MovieDetailActivity.this , "Film rimosso dalla lista dei preferiti", Toast.LENGTH_SHORT).show();
                                                        }else{
                                                            CuorePreferiti.setImageResource(R.drawable.ic_likeactive);
                                                            stato = true;
                                                        }
                                                    }else {
                                                        Toast.makeText(MovieDetailActivity.this, "Verifica fallita",Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                                @Override public void onFailure(@NonNull Call<DBModelVerifica> call,@NonNull Throwable t) {
                                                    Toast.makeText(MovieDetailActivity.this, "Ops qualcosa è andato storto",Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                        }else{
                                            Toast.makeText(MovieDetailActivity.this, "Rimozione dalla lista dei film preferiti fallita",Toast.LENGTH_SHORT).show();
                                        }
                                    }else{
                                        Toast.makeText(MovieDetailActivity.this, "Impossibile rimuovere dalla lista dei film preferiti",Toast.LENGTH_SHORT).show();
                                    }
                                }
                                @Override public void onFailure(@NonNull Call<DBModelResponseToInsert> call,@NonNull Throwable t) {
                                    Toast.makeText(MovieDetailActivity.this, "Ops qualcosa è andato storto",Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }
                });
                OcchialiDaVedere.setOnClickListener(new View.OnClickListener() {
                    @Override public void onClick(View v) {
                        if(!stato_V) {
                            tipoLista = "Da Vedere";
                            Visibilità = "Solo Amici";
                            Descrizione = "null";
                            Call<DBModelResponseToInsert> aggiungiCall = retrofitServiceDBInterno.AggiungiFilmAlDatabase(String.valueOf(id), tipoLista, UserName, stringTitolo.toString(), stringPoster.toString(), Descrizione, Visibilità);
                            aggiungiCall.enqueue(new Callback<DBModelResponseToInsert>() {
                                @Override public void onResponse(@NonNull Call<DBModelResponseToInsert> call,@NonNull Response<DBModelResponseToInsert> response) {
                                    DBModelResponseToInsert dbModelResponseToInsert = response.body();
                                    if(dbModelResponseToInsert != null){
                                        if(dbModelResponseToInsert.getStato().equals("Successfull")){
                                            if(tipoLista.equals("Preferiti") || tipoLista.equals("Da Vedere")){
                                                if(tipoLista.equals("Preferiti")){
                                                    Call<DBModelVerifica> verificaPrefCall = retrofitServiceDBInterno.VerificaSePresenteNeiPreferiti(String.valueOf(id), UserName);
                                                    verificaPrefCall.enqueue(new Callback<DBModelVerifica>() {
                                                        @Override public void onResponse(@NonNull Call<DBModelVerifica> call,@NonNull Response<DBModelVerifica> response) {
                                                            DBModelVerifica dbModelVerifica = response.body();
                                                            if(dbModelVerifica != null){
                                                                List<DBModelVerificaResults> verificaResults = dbModelVerifica.getResults();
                                                                if (verificaResults.get(0).getCodVerifica() == 0){
                                                                    CuorePreferiti.setImageResource(R.drawable.ic_like);
                                                                    stato = false;
                                                                }else{
                                                                    CuorePreferiti.setImageResource(R.drawable.ic_likeactive);
                                                                    stato = true;
                                                                    Toast.makeText(MovieDetailActivity.this, "Film aggiunto nella lista " + tipoLista + ".", Toast.LENGTH_SHORT).show();
                                                                }
                                                            }else {
                                                                Toast.makeText(MovieDetailActivity.this, "Verifica fallita",Toast.LENGTH_SHORT).show();
                                                            }
                                                        }
                                                        @Override public void onFailure(@NonNull Call<DBModelVerifica> call,@NonNull Throwable t) {
                                                            Toast.makeText(MovieDetailActivity.this, "Ops qualcosa è andato storto",Toast.LENGTH_SHORT).show();
                                                        }
                                                    });
                                                } else{
                                                    Call<DBModelVerifica> verificaDaVedCall = retrofitServiceDBInterno.VerificaSePresenteNeiDaVedere(String.valueOf(id), UserName);
                                                    verificaDaVedCall.enqueue(new Callback<DBModelVerifica>() {
                                                        @Override public void onResponse(@NonNull Call<DBModelVerifica> call,@NonNull Response<DBModelVerifica> response) {
                                                            DBModelVerifica dbModelVerifica = response.body();
                                                            if(dbModelVerifica != null){
                                                                List<DBModelVerificaResults> verificaResults = dbModelVerifica.getResults();
                                                                if (verificaResults.get(0).getCodVerifica() == 0){
                                                                    OcchialiDaVedere.setImageResource(R.drawable.ic__d_glasses);
                                                                    stato_V = false;
                                                                }else{
                                                                    OcchialiDaVedere.setImageResource(R.drawable.ic__d_glasses_active);
                                                                    stato_V = true;
                                                                    Toast.makeText(MovieDetailActivity.this, "Film aggiunto nella lista " + tipoLista + ".", Toast.LENGTH_SHORT).show();
                                                                }
                                                            }else{
                                                                Toast.makeText(MovieDetailActivity.this, "Verifica fallita",Toast.LENGTH_SHORT).show();
                                                            }
                                                        }
                                                        @Override public void onFailure(@NonNull Call<DBModelVerifica> call,@NonNull Throwable t) {
                                                            Toast.makeText(MovieDetailActivity.this, "Ops qualcosa è andato storto",Toast.LENGTH_SHORT).show();
                                                        }
                                                    });
                                                }
                                            }else {
                                                aggiungiA.setSelectedIndex(0);
                                                Toast.makeText(MovieDetailActivity.this, "Film aggiunto nella lista " + tipoLista + ".", Toast.LENGTH_SHORT).show();
                                            }
                                        }else{
                                            Toast.makeText(MovieDetailActivity.this, "Aggiunta film fallita",Toast.LENGTH_SHORT).show();
                                        }
                                    }else{
                                        Toast.makeText(MovieDetailActivity.this, "Impossibile aggiungere il film",Toast.LENGTH_SHORT).show();
                                    }
                                }
                                @Override public void onFailure(@NonNull Call<DBModelResponseToInsert> call,@NonNull Throwable t) {
                                    Toast.makeText(MovieDetailActivity.this, "Ops qualcosa è andato storto",Toast.LENGTH_SHORT).show();
                                }
                            });
                        }else {
                            Call<DBModelResponseToInsert> rimdavedCall = retrofitServiceDBInterno.RimuoviFilmDaVedere(String.valueOf(id),UserName);
                            rimdavedCall.enqueue(new Callback<DBModelResponseToInsert>() {
                                @Override public void onResponse(@NonNull Call<DBModelResponseToInsert> call,@NonNull Response<DBModelResponseToInsert> response) {
                                    DBModelResponseToInsert dbModelResponseToInsert = response.body();
                                    if(dbModelResponseToInsert != null){
                                        if (dbModelResponseToInsert.getStato().equals("Successfull")){
                                            Call<DBModelVerifica> verificaDaVedCall = retrofitServiceDBInterno.VerificaSePresenteNeiDaVedere(String.valueOf(id), UserName);
                                            verificaDaVedCall.enqueue(new Callback<DBModelVerifica>() {
                                                @Override public void onResponse(@NonNull Call<DBModelVerifica> call,@NonNull Response<DBModelVerifica> response) {
                                                    DBModelVerifica dbModelVerifica = response.body();
                                                    if(dbModelVerifica != null){
                                                        List<DBModelVerificaResults> verificaResults = dbModelVerifica.getResults();
                                                        if (verificaResults.get(0).getCodVerifica() == 0){
                                                            OcchialiDaVedere.setImageResource(R.drawable.ic__d_glasses);
                                                            stato_V = false;
                                                            Toast.makeText(MovieDetailActivity.this , "Film rimosso dalla lista dei film da vedere", Toast.LENGTH_SHORT).show();
                                                        }else{
                                                            OcchialiDaVedere.setImageResource(R.drawable.ic__d_glasses_active);
                                                            stato_V = true;
                                                        }
                                                    }else{
                                                        Toast.makeText(MovieDetailActivity.this, "Verifica fallita",Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                                @Override public void onFailure(@NonNull Call<DBModelVerifica> call,@NonNull Throwable t) {
                                                    Toast.makeText(MovieDetailActivity.this, "Ops qualcosa è andato storto",Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                        }else{
                                            Toast.makeText(MovieDetailActivity.this, "Rimozione dalla lista dei film da vedere fallita",Toast.LENGTH_SHORT).show();
                                        }
                                    }else{
                                        Toast.makeText(MovieDetailActivity.this, "Impossibile rimuovere dalla lista dei film da vedere",Toast.LENGTH_SHORT).show();
                                    }
                                }
                                @Override public void onFailure(@NonNull Call<DBModelResponseToInsert> call,@NonNull Throwable t) {
                                    Toast.makeText(MovieDetailActivity.this, "Ops qualcosa è andato storto",Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }
                });
                /*AGGIUNTA NUOVA*/
                previous.setOnClickListener(new View.OnClickListener() {
                    @Override public void onClick(View v) {
                        onBackPressed();
                    }
                });
                aggiungiA.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if (position == 0){
                        aggiungiA.setSelectedIndex(0);
                    }else if(position == 1){
                            dialogBilder = new AlertDialog.Builder(MovieDetailActivity.this);
                            final View PopUpView = getLayoutInflater().inflate(R.layout.crea_liste_pop_up, null);
                            Conferma = (AppCompatButton) PopUpView.findViewById(R.id.conferma_button);
                            Annulla = (AppCompatButton) PopUpView.findViewById(R.id.annulla_button);
                            InserisciTitolo = PopUpView.findViewById(R.id.inserisci_nome_lista);
                            InserisciDescrizione = PopUpView.findViewById(R.id.descrizione_lista);
                            visibility = (RadioGroup) PopUpView.findViewById(R.id.visibilità_lista);
                            dialogBilder.setView(PopUpView);
                            CreaLista = dialogBilder.create();
                            CreaLista.show();
                            Conferma.setOnClickListener(new View.OnClickListener() {
                                @SuppressLint("NonConstantResourceId")
                                @Override public void onClick(View v) {
                                    if(InserisciTitolo.length() > 0 ){
                                        int camposelezionato = visibility.getCheckedRadioButtonId();
                                        if (camposelezionato == -1) {
                                            Toast.makeText(MovieDetailActivity.this, "Seleziona un campo di visibilità", Toast.LENGTH_SHORT).show();
                                        }else{
                                            switch (camposelezionato){
                                                case R.id.solo_amici:
                                                    tipoLista = InserisciTitolo.getText().toString();
                                                    if(InserisciDescrizione.length() > 0) {
                                                        Descrizione = InserisciDescrizione.getText().toString();
                                                    }else{
                                                        Descrizione = "null";
                                                    }
                                                    Visibilità = "Solo amici";
                                                    Call<DBModelVerifica> verificaResultsCall = retrofitServiceDBInterno.VerificaSePresente(String.valueOf(id_film), UserName, tipoLista);
                                                    verificaResultsCall.enqueue(new Callback<DBModelVerifica>() {
                                                        @Override public void onResponse(@NonNull Call<DBModelVerifica> call,@NonNull Response<DBModelVerifica> response) {
                                                            DBModelVerifica dbModelVerifica = response.body();
                                                            if(dbModelVerifica != null){
                                                                List<DBModelVerificaResults> verificaResults = dbModelVerifica.getResults();
                                                                if(verificaResults.get(0).getCodVerifica() == 0){
                                                                    Call<DBModelVerifica> modelVerificaCall = retrofitServiceDBInterno.VerificaSePresente(String.valueOf(id_film), UserName, tipoLista);
                                                                    modelVerificaCall.enqueue(new Callback<DBModelVerifica>() {
                                                                        @Override public void onResponse(@NonNull Call<DBModelVerifica> call,@NonNull Response<DBModelVerifica> response) {
                                                                            DBModelVerifica dbModelVerifica = response.body();
                                                                            if(dbModelVerifica != null){
                                                                                List<DBModelVerificaResults> verificaResults = dbModelVerifica.getResults();
                                                                                if(verificaResults.get(0).getCodVerifica() == 0){
                                                                                    Call<DBModelResponseToInsert> aggiungiCall = retrofitServiceDBInterno.AggiungiFilmAlDatabase(String.valueOf(id), tipoLista, UserName, stringTitolo.toString(), stringPoster.toString(), Descrizione, Visibilità);
                                                                                    aggiungiCall.enqueue(new Callback<DBModelResponseToInsert>() {
                                                                                        @Override public void onResponse(@NonNull Call<DBModelResponseToInsert> call,@NonNull Response<DBModelResponseToInsert> response) {
                                                                                            DBModelResponseToInsert dbModelResponseToInsert = response.body();
                                                                                            if(dbModelResponseToInsert != null){
                                                                                                if(dbModelResponseToInsert.getStato().equals("Successfull")){
                                                                                                    if(tipoLista.equals("Preferiti") || tipoLista.equals("Da Vedere")){
                                                                                                        if(tipoLista.equals("Preferiti")){
                                                                                                            Call<DBModelVerifica> verificaPrefCall = retrofitServiceDBInterno.VerificaSePresenteNeiPreferiti(String.valueOf(id), UserName);
                                                                                                            verificaPrefCall.enqueue(new Callback<DBModelVerifica>() {
                                                                                                                @Override public void onResponse(@NonNull Call<DBModelVerifica> call,@NonNull Response<DBModelVerifica> response) {
                                                                                                                    DBModelVerifica dbModelVerifica = response.body();
                                                                                                                    if(dbModelVerifica != null){
                                                                                                                        List<DBModelVerificaResults> verificaResults = dbModelVerifica.getResults();
                                                                                                                        if (verificaResults.get(0).getCodVerifica() == 0){
                                                                                                                            CuorePreferiti.setImageResource(R.drawable.ic_like);
                                                                                                                            stato = false;
                                                                                                                        }else{
                                                                                                                            CuorePreferiti.setImageResource(R.drawable.ic_likeactive);
                                                                                                                            stato = true;
                                                                                                                            Toast.makeText(MovieDetailActivity.this, "Film aggiunto nella lista " + tipoLista + ".", Toast.LENGTH_SHORT).show();
                                                                                                                        }
                                                                                                                    }else {
                                                                                                                        Toast.makeText(MovieDetailActivity.this, "Verifica fallita",Toast.LENGTH_SHORT).show();
                                                                                                                    }
                                                                                                                }
                                                                                                                @Override public void onFailure(@NonNull Call<DBModelVerifica> call,@NonNull Throwable t) {
                                                                                                                    Toast.makeText(MovieDetailActivity.this, "Ops qualcosa è andato storto",Toast.LENGTH_SHORT).show();
                                                                                                                }
                                                                                                            });
                                                                                                        } else{
                                                                                                            Call<DBModelVerifica> verificaDaVedCall = retrofitServiceDBInterno.VerificaSePresenteNeiDaVedere(String.valueOf(id), UserName);
                                                                                                            verificaDaVedCall.enqueue(new Callback<DBModelVerifica>() {
                                                                                                                @Override public void onResponse(@NonNull Call<DBModelVerifica> call,@NonNull Response<DBModelVerifica> response) {
                                                                                                                    DBModelVerifica dbModelVerifica = response.body();
                                                                                                                    if(dbModelVerifica != null){
                                                                                                                        List<DBModelVerificaResults> verificaResults = dbModelVerifica.getResults();
                                                                                                                        if (verificaResults.get(0).getCodVerifica() == 0){
                                                                                                                            OcchialiDaVedere.setImageResource(R.drawable.ic__d_glasses);
                                                                                                                            stato_V = false;
                                                                                                                        }else{
                                                                                                                            OcchialiDaVedere.setImageResource(R.drawable.ic__d_glasses_active);
                                                                                                                            stato_V = true;
                                                                                                                            Toast.makeText(MovieDetailActivity.this, "Film aggiunto nella lista " + tipoLista + ".", Toast.LENGTH_SHORT).show();
                                                                                                                        }
                                                                                                                    }else{
                                                                                                                        Toast.makeText(MovieDetailActivity.this, "Verifica fallita",Toast.LENGTH_SHORT).show();
                                                                                                                    }
                                                                                                                }
                                                                                                                @Override public void onFailure(@NonNull Call<DBModelVerifica> call,@NonNull Throwable t) {
                                                                                                                    Toast.makeText(MovieDetailActivity.this, "Ops qualcosa è andato storto",Toast.LENGTH_SHORT).show();
                                                                                                                }
                                                                                                            });
                                                                                                        }
                                                                                                    }else {
                                                                                                        aggiungiA.setSelectedIndex(0);
                                                                                                        listefilm.add(tipoLista);
                                                                                                        aggiungiA.attachDataSource(listefilm);
                                                                                                        Toast.makeText(MovieDetailActivity.this, "Film aggiunto nella lista " + tipoLista + ".", Toast.LENGTH_SHORT).show();
                                                                                                    }
                                                                                                }else{
                                                                                                    aggiungiA.setSelectedIndex(0);
                                                                                                    Toast.makeText(MovieDetailActivity.this, "Aggiunta film fallita",Toast.LENGTH_SHORT).show();
                                                                                                }
                                                                                            }else{
                                                                                                aggiungiA.setSelectedIndex(0);
                                                                                                Toast.makeText(MovieDetailActivity.this, "Impossibile aggiungere il film",Toast.LENGTH_SHORT).show();
                                                                                            }
                                                                                        }
                                                                                        @Override public void onFailure(@NonNull Call<DBModelResponseToInsert> call,@NonNull Throwable t) {
                                                                                            aggiungiA.setSelectedIndex(0);
                                                                                            Toast.makeText(MovieDetailActivity.this, "Ops qualcosa è andato storto",Toast.LENGTH_SHORT).show();
                                                                                        }
                                                                                    });
                                                                                }else {
                                                                                    aggiungiA.setSelectedIndex(0);
                                                                                    Toast.makeText(MovieDetailActivity.this, "Film presente all'interno della lista " + tipoLista + ".", Toast.LENGTH_SHORT).show();
                                                                                }
                                                                            }else{
                                                                                aggiungiA.setSelectedIndex(0);
                                                                                Toast.makeText(MovieDetailActivity.this, "Impossibile verificare se il film è presente",Toast.LENGTH_SHORT).show();
                                                                            }
                                                                        }
                                                                        @Override public void onFailure(@NonNull Call<DBModelVerifica> call,@NonNull Throwable t) {
                                                                            aggiungiA.setSelectedIndex(0);
                                                                            Toast.makeText(MovieDetailActivity.this, "Ops qualcosa è andato storto",Toast.LENGTH_SHORT).show();
                                                                        }
                                                                    });
                                                                }else {
                                                                    aggiungiA.setSelectedIndex(0);
                                                                    Toast.makeText(MovieDetailActivity.this, "Lista già creata con il nome di " + tipoLista + ".", Toast.LENGTH_SHORT).show();
                                                                }
                                                            }else{
                                                                aggiungiA.setSelectedIndex(0);
                                                                Toast.makeText(MovieDetailActivity.this, "Impossibile verificare se la lista è presente",Toast.LENGTH_SHORT).show();
                                                            }
                                                        }
                                                        @Override public void onFailure(@NonNull Call<DBModelVerifica> call,@NonNull Throwable t) {
                                                            aggiungiA.setSelectedIndex(0);
                                                            Toast.makeText(MovieDetailActivity.this, "Ops qualcosa è andato storto",Toast.LENGTH_SHORT).show();
                                                        }
                                                    });
                                                    CreaLista.dismiss();
                                                    break;
                                                case R.id.tutti:
                                                    tipoLista = InserisciTitolo.getText().toString();
                                                    if(InserisciDescrizione.length() > 0) {
                                                        Descrizione = InserisciDescrizione.getText().toString();
                                                    }else{
                                                        Descrizione = "null";
                                                    }
                                                    Visibilità = "Tutti";
                                                    Call<DBModelVerifica> verificaResultsCall2 = retrofitServiceDBInterno.VerificaSePresente(String.valueOf(id_film), UserName, tipoLista);
                                                    verificaResultsCall2.enqueue(new Callback<DBModelVerifica>() {
                                                        @Override public void onResponse(@NonNull Call<DBModelVerifica> call,@NonNull Response<DBModelVerifica> response) {
                                                            DBModelVerifica dbModelVerifica = response.body();
                                                            if(dbModelVerifica != null){
                                                                List<DBModelVerificaResults> verificaResults = dbModelVerifica.getResults();
                                                                if(verificaResults.get(0).getCodVerifica() == 0){
                                                                    Call<DBModelVerifica> modelVerificaCall = retrofitServiceDBInterno.VerificaSePresente(String.valueOf(id_film), UserName, tipoLista);
                                                                    modelVerificaCall.enqueue(new Callback<DBModelVerifica>() {
                                                                        @Override public void onResponse(@NonNull Call<DBModelVerifica> call,@NonNull Response<DBModelVerifica> response) {
                                                                            DBModelVerifica dbModelVerifica = response.body();
                                                                            if(dbModelVerifica != null){
                                                                                List<DBModelVerificaResults> verificaResults = dbModelVerifica.getResults();
                                                                                if(verificaResults.get(0).getCodVerifica() == 0){
                                                                                    Call<DBModelResponseToInsert> aggiungiCall = retrofitServiceDBInterno.AggiungiFilmAlDatabase(String.valueOf(id_film), tipoLista, UserName, stringTitolo.toString(), stringPoster.toString(), Descrizione, Visibilità);
                                                                                    aggiungiCall.enqueue(new Callback<DBModelResponseToInsert>() {
                                                                                        @Override public void onResponse(@NonNull Call<DBModelResponseToInsert> call,@NonNull Response<DBModelResponseToInsert> response) {
                                                                                            DBModelResponseToInsert dbModelResponseToInsert = response.body();
                                                                                            if(dbModelResponseToInsert != null){
                                                                                                if(dbModelResponseToInsert.getStato().equals("Successfull")){
                                                                                                    if(tipoLista.equals("Preferiti") || tipoLista.equals("Da Vedere")){
                                                                                                        if(tipoLista.equals("Preferiti")){
                                                                                                            Call<DBModelVerifica> verificaPrefCall = retrofitServiceDBInterno.VerificaSePresenteNeiPreferiti(String.valueOf(id), UserName);
                                                                                                            verificaPrefCall.enqueue(new Callback<DBModelVerifica>() {
                                                                                                                @Override public void onResponse(@NonNull Call<DBModelVerifica> call,@NonNull Response<DBModelVerifica> response) {
                                                                                                                    DBModelVerifica dbModelVerifica = response.body();
                                                                                                                    if(dbModelVerifica != null){
                                                                                                                        List<DBModelVerificaResults> verificaResults = dbModelVerifica.getResults();
                                                                                                                        if (verificaResults.get(0).getCodVerifica() == 0){
                                                                                                                            CuorePreferiti.setImageResource(R.drawable.ic_like);
                                                                                                                            stato = false;
                                                                                                                        }else{
                                                                                                                            CuorePreferiti.setImageResource(R.drawable.ic_likeactive);
                                                                                                                            stato = true;
                                                                                                                            Toast.makeText(MovieDetailActivity.this, "Film aggiunto nella lista " + tipoLista + ".", Toast.LENGTH_SHORT).show();
                                                                                                                        }
                                                                                                                    }else {
                                                                                                                        Toast.makeText(MovieDetailActivity.this, "Verifica fallita",Toast.LENGTH_SHORT).show();
                                                                                                                    }
                                                                                                                }
                                                                                                                @Override public void onFailure(@NonNull Call<DBModelVerifica> call,@NonNull Throwable t) {
                                                                                                                    Toast.makeText(MovieDetailActivity.this, "Ops qualcosa è andato storto",Toast.LENGTH_SHORT).show();
                                                                                                                }
                                                                                                            });
                                                                                                        } else{
                                                                                                            Call<DBModelVerifica> verificaDaVedCall = retrofitServiceDBInterno.VerificaSePresenteNeiDaVedere(String.valueOf(id_film), UserName);
                                                                                                            verificaDaVedCall.enqueue(new Callback<DBModelVerifica>() {
                                                                                                                @Override public void onResponse(@NonNull Call<DBModelVerifica> call,@NonNull Response<DBModelVerifica> response) {
                                                                                                                    DBModelVerifica dbModelVerifica = response.body();
                                                                                                                    if(dbModelVerifica != null){
                                                                                                                        List<DBModelVerificaResults> verificaResults = dbModelVerifica.getResults();
                                                                                                                        if (verificaResults.get(0).getCodVerifica() == 0){
                                                                                                                            OcchialiDaVedere.setImageResource(R.drawable.ic__d_glasses);
                                                                                                                            stato_V = false;
                                                                                                                        }else{
                                                                                                                            OcchialiDaVedere.setImageResource(R.drawable.ic__d_glasses_active);
                                                                                                                            stato_V = true;
                                                                                                                            Toast.makeText(MovieDetailActivity.this, "Film aggiunto nella lista " + tipoLista + ".", Toast.LENGTH_SHORT).show();
                                                                                                                        }
                                                                                                                    }else{
                                                                                                                        Toast.makeText(MovieDetailActivity.this, "Verifica fallita",Toast.LENGTH_SHORT).show();
                                                                                                                    }
                                                                                                                }
                                                                                                                @Override public void onFailure(@NonNull Call<DBModelVerifica> call,@NonNull Throwable t) {
                                                                                                                    Toast.makeText(MovieDetailActivity.this, "Ops qualcosa è andato storto",Toast.LENGTH_SHORT).show();
                                                                                                                }
                                                                                                            });
                                                                                                        }
                                                                                                    }else {
                                                                                                        aggiungiA.setSelectedIndex(0);
                                                                                                        listefilm.add(tipoLista);
                                                                                                        aggiungiA.attachDataSource(listefilm);
                                                                                                        Toast.makeText(MovieDetailActivity.this, "Film aggiunto nella lista " + tipoLista + ".", Toast.LENGTH_SHORT).show();
                                                                                                    }
                                                                                                }else{
                                                                                                    aggiungiA.setSelectedIndex(0);
                                                                                                    Toast.makeText(MovieDetailActivity.this, "Aggiunta film fallita",Toast.LENGTH_SHORT).show();
                                                                                                }
                                                                                            }else{
                                                                                                aggiungiA.setSelectedIndex(0);
                                                                                                Toast.makeText(MovieDetailActivity.this, "Impossibile aggiungere il film",Toast.LENGTH_SHORT).show();
                                                                                            }
                                                                                        }
                                                                                        @Override public void onFailure(@NonNull Call<DBModelResponseToInsert> call,@NonNull Throwable t) {
                                                                                            aggiungiA.setSelectedIndex(0);
                                                                                            Toast.makeText(MovieDetailActivity.this, "Ops qualcosa è andato storto",Toast.LENGTH_SHORT).show();
                                                                                        }
                                                                                    });
                                                                                }else {
                                                                                    aggiungiA.setSelectedIndex(0);
                                                                                    Toast.makeText(MovieDetailActivity.this, "Film presente all'interno della lista " + tipoLista + ".", Toast.LENGTH_SHORT).show();
                                                                                }
                                                                            }else{
                                                                                aggiungiA.setSelectedIndex(0);
                                                                                Toast.makeText(MovieDetailActivity.this, "Impossibile verificare se il film è presente",Toast.LENGTH_SHORT).show();
                                                                            }
                                                                        }
                                                                        @Override public void onFailure(@NonNull Call<DBModelVerifica> call,@NonNull Throwable t) {
                                                                            aggiungiA.setSelectedIndex(0);
                                                                            Toast.makeText(MovieDetailActivity.this, "Ops qualcosa è andato storto",Toast.LENGTH_SHORT).show();
                                                                        }
                                                                    });
                                                                }else {
                                                                    aggiungiA.setSelectedIndex(0);
                                                                    Toast.makeText(MovieDetailActivity.this, "Lista già creata con il nome di " + tipoLista + ".", Toast.LENGTH_SHORT).show();
                                                                }
                                                            }else{
                                                                aggiungiA.setSelectedIndex(0);
                                                                Toast.makeText(MovieDetailActivity.this, "Impossibile verificare se la lista è presente",Toast.LENGTH_SHORT).show();
                                                            }
                                                        }
                                                        @Override public void onFailure(@NonNull Call<DBModelVerifica> call,@NonNull Throwable t) {
                                                            aggiungiA.setSelectedIndex(0);
                                                            Toast.makeText(MovieDetailActivity.this, "Ops qualcosa è andato storto",Toast.LENGTH_SHORT).show();
                                                        }
                                                    });
                                                    aggiungiA.setSelectedIndex(0);
                                                    CreaLista.dismiss();
                                                    break;
                                            }
                                        }
                                    }else{
                                            Toast.makeText(MovieDetailActivity.this, "Inserisci titolo", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                            Annulla.setOnClickListener(new View.OnClickListener() {
                                @Override public void onClick(View v) {
                                    aggiungiA.setSelectedIndex(0);
                                    CreaLista.dismiss();
                                }
                            });
                        }else{
                            int numero = aggiungiA.getSelectedIndex();
                            tipoLista = String.valueOf(listefilm.get(numero));
                            Call<DBModelAttributiLista> attributiListaCall = retrofitServiceDBInterno.getAttributiLista(UserName, tipoLista);
                            attributiListaCall.enqueue(new Callback<DBModelAttributiLista>() {
                                @Override public void onResponse(@NonNull Call<DBModelAttributiLista> call,@NonNull Response<DBModelAttributiLista> response) {
                                    DBModelAttributiLista dbModelAttributiLista = response.body();
                                    if(dbModelAttributiLista != null){
                                        List<DBModelAttributiListaResults> listaResults = dbModelAttributiLista.getResults();
                                        if(!(listaResults.isEmpty())){
                                            Descrizione = listaResults.get(0).getDescrizione();
                                            Visibilità = listaResults.get(0).getVisibilita();
                                            Call<DBModelVerifica> modelVerificaCall = retrofitServiceDBInterno.VerificaSePresente(String.valueOf(id_film), UserName, tipoLista);
                                            modelVerificaCall.enqueue(new Callback<DBModelVerifica>() {
                                                @Override public void onResponse(@NonNull Call<DBModelVerifica> call,@NonNull Response<DBModelVerifica> response) {
                                                    DBModelVerifica dbModelVerifica = response.body();
                                                    if (dbModelVerifica != null) {
                                                        List<DBModelVerificaResults> verificaResults = dbModelVerifica.getResults();
                                                        if (verificaResults.get(0).getCodVerifica() == 0) {
                                                            Call<DBModelResponseToInsert> aggiungiCall = retrofitServiceDBInterno.AggiungiFilmAlDatabase(String.valueOf(id_film), tipoLista, UserName, stringTitolo.toString(), stringPoster.toString(), Descrizione, Visibilità);
                                                            aggiungiCall.enqueue(new Callback<DBModelResponseToInsert>() {
                                                                @Override public void onResponse(@NonNull Call<DBModelResponseToInsert> call,@NonNull Response<DBModelResponseToInsert> response) {
                                                                    DBModelResponseToInsert dbModelResponseToInsert = response.body();
                                                                    if(dbModelResponseToInsert != null){
                                                                        if(dbModelResponseToInsert.getStato().equals("Successfull")){
                                                                            if(tipoLista.equals("Preferiti") || tipoLista.equals("Da Vedere")){
                                                                                if(tipoLista.equals("Preferiti")){
                                                                                    Call<DBModelVerifica> verificaPrefCall = retrofitServiceDBInterno.VerificaSePresenteNeiPreferiti(String.valueOf(id_film), UserName);
                                                                                    verificaPrefCall.enqueue(new Callback<DBModelVerifica>() {
                                                                                        @Override public void onResponse(@NonNull Call<DBModelVerifica> call,@NonNull Response<DBModelVerifica> response) {
                                                                                            DBModelVerifica dbModelVerifica = response.body();
                                                                                            if(dbModelVerifica != null){
                                                                                                List<DBModelVerificaResults> verificaResults = dbModelVerifica.getResults();
                                                                                                if (verificaResults.get(0).getCodVerifica() == 0){
                                                                                                    CuorePreferiti.setImageResource(R.drawable.ic_like);
                                                                                                    stato = false;
                                                                                                }else{
                                                                                                    CuorePreferiti.setImageResource(R.drawable.ic_likeactive);
                                                                                                    stato = true;
                                                                                                    Toast.makeText(MovieDetailActivity.this, "Film aggiunto nella lista " + tipoLista + ".", Toast.LENGTH_SHORT).show();
                                                                                                }
                                                                                            }else {
                                                                                                Toast.makeText(MovieDetailActivity.this, "Verifica fallita",Toast.LENGTH_SHORT).show();
                                                                                            }
                                                                                        }
                                                                                        @Override public void onFailure(@NonNull Call<DBModelVerifica> call,@NonNull Throwable t) {
                                                                                            Toast.makeText(MovieDetailActivity.this, "Ops qualcosa è andato storto",Toast.LENGTH_SHORT).show();
                                                                                        }
                                                                                    });
                                                                                } else{
                                                                                    Call<DBModelVerifica> verificaDaVedCall = retrofitServiceDBInterno.VerificaSePresenteNeiDaVedere(String.valueOf(id), UserName);
                                                                                    verificaDaVedCall.enqueue(new Callback<DBModelVerifica>() {
                                                                                        @Override public void onResponse(@NonNull Call<DBModelVerifica> call,@NonNull Response<DBModelVerifica> response) {
                                                                                            DBModelVerifica dbModelVerifica = response.body();
                                                                                            if(dbModelVerifica != null){
                                                                                                List<DBModelVerificaResults> verificaResults = dbModelVerifica.getResults();
                                                                                                if (verificaResults.get(0).getCodVerifica() == 0){
                                                                                                    OcchialiDaVedere.setImageResource(R.drawable.ic__d_glasses);
                                                                                                    stato_V = false;
                                                                                                }else{
                                                                                                    OcchialiDaVedere.setImageResource(R.drawable.ic__d_glasses_active);
                                                                                                    stato_V = true;
                                                                                                    Toast.makeText(MovieDetailActivity.this, "Film aggiunto nella lista " + tipoLista + ".", Toast.LENGTH_SHORT).show();
                                                                                                }
                                                                                            }else{
                                                                                                Toast.makeText(MovieDetailActivity.this, "Verifica fallita",Toast.LENGTH_SHORT).show();
                                                                                            }
                                                                                        }
                                                                                        @Override public void onFailure(@NonNull Call<DBModelVerifica> call,@NonNull Throwable t) {
                                                                                            Toast.makeText(MovieDetailActivity.this, "Ops qualcosa è andato storto",Toast.LENGTH_SHORT).show();
                                                                                        }
                                                                                    });
                                                                                }
                                                                            }else {
                                                                                aggiungiA.setSelectedIndex(0);
                                                                                Toast.makeText(MovieDetailActivity.this, "Film aggiunto nella lista " + tipoLista + ".", Toast.LENGTH_SHORT).show();
                                                                            }
                                                                        }else{
                                                                            Toast.makeText(MovieDetailActivity.this, "Aggiunta film fallita",Toast.LENGTH_SHORT).show();
                                                                        }
                                                                    }else{
                                                                        Toast.makeText(MovieDetailActivity.this, "Impossibile aggiungere il film",Toast.LENGTH_SHORT).show();
                                                                    }
                                                                }
                                                                @Override public void onFailure(@NonNull Call<DBModelResponseToInsert> call,@NonNull Throwable t) {
                                                                    Toast.makeText(MovieDetailActivity.this, "Ops qualcosa è andato storto",Toast.LENGTH_SHORT).show();
                                                                }
                                                            });
                                                        }else{
                                                            aggiungiA.setSelectedIndex(0);
                                                            Toast.makeText(MovieDetailActivity.this, "Film presente all'interno della lista " + tipoLista + ".", Toast.LENGTH_SHORT).show();
                                                        }
                                                    }else {
                                                        aggiungiA.setSelectedIndex(0);
                                                        Toast.makeText(MovieDetailActivity.this, "Verifica fallita",Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                                @Override public void onFailure(@NonNull Call<DBModelVerifica> call,@NonNull Throwable t) {
                                                    Toast.makeText(MovieDetailActivity.this, "Ops qualcosa è andato storto",Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                        }else{
                                            aggiungiA.setSelectedIndex(0);
                                            Toast.makeText(MovieDetailActivity.this, "Recupero attributi fallito",Toast.LENGTH_SHORT).show();
                                        }
                                    }else{
                                        aggiungiA.setSelectedIndex(0);
                                        Toast.makeText(MovieDetailActivity.this, "Impossibile recuperare attributi lista",Toast.LENGTH_SHORT).show();
                                    }
                                }
                                @Override public void onFailure(@NonNull Call<DBModelAttributiLista> call,@NonNull Throwable t) {
                                    Toast.makeText(MovieDetailActivity.this, "Ops qualcosa è andato storto",Toast.LENGTH_SHORT).show();
                                }
                            });
                        }

                    }
                    @Override public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
                Recensioni.setOnClickListener(new View.OnClickListener() {
                    @Override public void onClick(View v) {
                        dialogBilder2 = new AlertDialog.Builder(MovieDetailActivity.this);
                        final View PopUpView = getLayoutInflater().inflate(R.layout.popup_allertaspoiler, null);
                        Ok = PopUpView.findViewById(R.id.ok_button);
                        dialogBilder2.setView(PopUpView);
                        Spoiler = dialogBilder2.create();
                        Spoiler.show();
                        Ok.setOnClickListener(new View.OnClickListener() {
                            @Override public void onClick(View v) {
                                String Titolo_Mod = stringTitolo.toString().replaceAll("'", "/");
                                Call<DBModelDettagliCinemates> cinematesCall = retrofitServiceDBInterno.getDettagliCinemates(Titolo_Mod);
                                cinematesCall.enqueue(new Callback<DBModelDettagliCinemates>() {
                                    @Override public void onResponse(@NonNull Call<DBModelDettagliCinemates> call,@NonNull Response<DBModelDettagliCinemates> response) {
                                        DBModelDettagliCinemates dbModelDettagliCinemates = response.body();
                                        if(dbModelDettagliCinemates != null){
                                            List<DBModelDettagliCinematesResponce> cinematesResponceList = dbModelDettagliCinemates.getResults();
                                            if(!(cinematesResponceList.isEmpty())){
                                                if(cinematesResponceList.get(0).getNumeroRecensioni() == null && cinematesResponceList.get(0).getValutazione() == null){
                                                    Numero_Recensioni = 0;
                                                    Valutazione_Media = 0.0;
                                                }else{
                                                    if(cinematesResponceList.get(0).getNumeroRecensioni() == null){
                                                        Numero_Recensioni = 0;
                                                    }else if(cinematesResponceList.get(0).getValutazione() == null){
                                                        Valutazione_Media = 0.0;
                                                    }else {
                                                        Numero_Recensioni = Integer.valueOf(cinematesResponceList.get(0).getNumeroRecensioni());
                                                        Valutazione_Media = Double.valueOf(cinematesResponceList.get(0).getValutazione());
                                                    }
                                                }
                                                Intent intent1 = new Intent(MovieDetailActivity.this, RecensioniActivity.class);
                                                intent1.putExtra("Titolo_Film", stringTitolo.toString());
                                                intent1.putExtra("Immagine_Poster",  stringPoster.toString());
                                                intent1.putExtra("Nome_Utente",UserName);
                                                intent1.putExtra("Numero_Recensioni", Numero_Recensioni);
                                                intent1.putExtra("Valutazione", Valutazione_Media);
                                                startActivity(intent1);
                                            }else{
                                                Toast.makeText(MovieDetailActivity.this, "Recupero informazioni dal database fallito",Toast.LENGTH_SHORT).show();
                                            }
                                        }else{
                                            Toast.makeText(MovieDetailActivity.this, "Impossibile recuperare informazioni dal database",Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                    @Override public void onFailure(@NonNull Call<DBModelDettagliCinemates> call,@NonNull Throwable t) {
                                        Toast.makeText(MovieDetailActivity.this, "Ops qualcosa è andato storto",Toast.LENGTH_SHORT).show();
                                    }
                                });
                                Spoiler.dismiss();
                            }
                        });
                    }
                });
            }
        }
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
                filmtrama.setText("Trama non presente");
            }
        }else{
            filmtrama.setText("Trama non presente");
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
                rates.setText("SV");
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