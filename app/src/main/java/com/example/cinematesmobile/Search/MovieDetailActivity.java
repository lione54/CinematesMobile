package com.example.cinematesmobile.Search;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.appcompat.widget.AppCompatRadioButton;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.cinematesmobile.BuildConfig;
import com.example.cinematesmobile.R;
import com.example.cinematesmobile.Recensioni.RecensioniActivity;
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
import com.google.android.material.textfield.TextInputEditText;
import com.squareup.picasso.Picasso;

import org.angmarch.views.NiceSpinner;
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

    private AppCompatButton Recensioni;
    private AlertDialog.Builder dialogBilder, dialogBilder2;
    private AlertDialog CreaLista, Spoiler;
    private AppCompatButton Conferma,Annulla, Ok;
    private TextInputEditText InserisciTitolo, InserisciDescrizione;
    private RadioGroup visibility;
    private Integer id_film;
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
    private String tipoLista = null;
    private MaterialFavoriteButton CuorePreferiti, OcchialiDaVedere;
    private RetrofitService retrofitService;
    private RetrofitService retrofitService2;
    private NiceSpinner aggiungiA;
    private String preferiti = "Aggiungi A...";
    private String crealista = "Nuova Lista";
    private String Descrizione = "null";
    private String Visibilità = "null";
    private AppCompatTextView rates;
    private boolean stato;
    private boolean stato_V;
    private String UserName = "lione54";
    private boolean firstuse = true;
    private LinearLayoutCompat rates_layout;
    private AppCompatImageButton previous;
    final ArrayList<String> listefilm = new ArrayList<>();
    private Integer Numero_Recensioni;
    private double Valutazione_Media;
    public static final String JSON_ARRAY = "dbdata";
    private static final String INSURL = "http://192.168.1.9/cinematesdb/AggiungiFilmAlDatabase.php";
    private static final String VERURL = "http://192.168.1.9/cinematesdb/VerificaSePresente.php";
    private static final String PREFURL = "http://192.168.1.9/cinematesdb/VerificaSePresenteNeiPreferiti.php";
    private static final String VEDURL = "http://192.168.1.9/cinematesdb/VerificaSePresenteNeiDaVedere.php";
    private static final String RIMURL = "http://192.168.1.9/cinematesdb/RimuoviDaiPreferiti.php";
    private static final String RIMVURL = "http://192.168.1.9/cinematesdb/RimuoviDaVedere.php";
    private static final String LISURL = "http://192.168.1.9/cinematesdb/TrovaListe.php";
    private static final String VISURL = "http://192.168.1.9/cinematesdb/PrendiAttributiLista.php";
    private static final String RECURL = "http://192.168.1.9/cinematesdb/PrendiDettagliCinemates.php";

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
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
        if(firstuse == true) {
            firstuse = false;
            listefilm.add(preferiti);
            listefilm.add(crealista);
            ListePresenti(UserName);
        }else{
            ListePresenti(UserName);
        }
        String lingua = "it-IT";
        Intent intent = getIntent();
        StringBuilder stringTitolo = new StringBuilder();
        StringBuilder stringPoster = new StringBuilder();
        retrofitService = RetrofitClient.getClient().create(RetrofitService.class);
        retrofitService2 = RetrofitClient.getClient().create(RetrofitService.class);
        if ( intent != null && intent.getExtras()!= null ){
            if(intent.getExtras().getString("id")!=null) {
                int id = Integer.parseInt(intent.getExtras().getString("id"));
                verificaSePresenteNeiPreferiti(id, UserName);
                verificaSePresenteNeiDaVedere(id, UserName);
                Call<MovieDetail> movieDetailCall = retrofitService2.getMovieDetail(id, BuildConfig.THE_MOVIE_DB_APY_KEY,lingua);
                movieDetailCall.enqueue(new Callback<MovieDetail>() {
                    @Override public void onResponse(@NonNull Call<MovieDetail> call,@NonNull Response<MovieDetail> response) {
                        MovieDetail movieDetailResponse = response.body();
                        if(movieDetailResponse != null){
                            prepareMovieDetails(movieDetailResponse);
                            id_film = movieDetailResponse.getId();
                            stringPoster.append(movieDetailResponse.getPoster_path());
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
                CuorePreferiti.setOnClickListener(new View.OnClickListener() {
                    @Override public void onClick(View v) {
                        if(!stato) {
                            tipoLista = "Preferiti";
                            Visibilità = "Solo Amici";
                            InserisciNelleListe(id, stringPoster.toString(), stringTitolo.toString(), tipoLista, UserName);
                        }else {
                            RimuoviDaiPreferiti(id, UserName);
                        }
                    }
                });
                OcchialiDaVedere.setOnClickListener(new View.OnClickListener() {
                    @Override public void onClick(View v) {
                        if(!stato_V) {
                            tipoLista = "Da Vedere";
                            Visibilità = "Solo Amici";
                            InserisciNelleListe(id, stringPoster.toString(), stringTitolo.toString(), tipoLista, UserName);
                        }else {
                            RimuoviDaiDaVedere(id, UserName);
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
                                @Override public void onClick(View v) {
                                    if(InserisciTitolo.length() > 0 ){
                                        int camposelezionato = visibility.getCheckedRadioButtonId();
                                        if (camposelezionato == -1) {
                                            Toast.makeText(MovieDetailActivity.this, "Seleziona Un Campo Di Visibilità.", Toast.LENGTH_SHORT).show();
                                        }else{
                                            switch (camposelezionato){
                                                case R.id.solo_amici:
                                                    tipoLista = InserisciTitolo.getText().toString();
                                                    if(InserisciDescrizione.length() > 0) {
                                                        Descrizione = InserisciDescrizione.getText().toString();
                                                    }else{
                                                        Descrizione = "null";
                                                    }
                                                    Visibilità = "Solo Amici";
                                                    listefilm.add(tipoLista);
                                                    aggiungiA.attachDataSource(listefilm);
                                                    verificaNomeLista(id_film, UserName, tipoLista, stringPoster.toString(), stringTitolo.toString());
                                                    CreaLista.dismiss();
                                                    aggiungiA.setSelectedIndex(0);
                                                    break;
                                                case R.id.tutti:
                                                    tipoLista = InserisciTitolo.getText().toString();
                                                    if(InserisciDescrizione.length() > 0) {
                                                        Descrizione = InserisciDescrizione.getText().toString();
                                                    }else{
                                                        Descrizione = "null";
                                                    }
                                                    Visibilità = "Tutti";
                                                    listefilm.add(tipoLista);
                                                    aggiungiA.attachDataSource(listefilm);
                                                    verificaNomeLista(id_film, UserName, tipoLista, stringPoster.toString(), stringTitolo.toString());
                                                    CreaLista.dismiss();
                                                    aggiungiA.setSelectedIndex(0);
                                                    break;
                                            }
                                        }
                                    }else{
                                            Toast.makeText(MovieDetailActivity.this, "Inserisci Titolo", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                            Annulla.setOnClickListener(new View.OnClickListener() {
                                @Override public void onClick(View v) {
                                    CreaLista.dismiss();
                                    aggiungiA.setSelectedIndex(0);
                                }
                            });
                        }else{
                            int numero = aggiungiA.getSelectedIndex();
                            tipoLista = String.valueOf(listefilm.get(numero));
                            PrendiAttributiLista(id_film, UserName, tipoLista, stringPoster.toString(), stringTitolo.toString());
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
                                PrendiDettagliFilm(id_film, stringTitolo.toString(),  stringPoster.toString(), UserName);
                                Spoiler.dismiss();
                            }
                        });
                    }
                });
            }
        }
    }

    private void RimuoviDaiDaVedere(int id, String userName) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, RIMVURL, new com.android.volley.Response.Listener<String>() {
            @Override public void onResponse(String response){
                Toast.makeText(MovieDetailActivity.this , "Film Rimosso Dalla Lista Dei Film Da Vedere.", Toast.LENGTH_LONG).show();
                verificaSePresenteNeiDaVedere(id, userName);
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override public void onErrorResponse(VolleyError error) {
                Toast.makeText(MovieDetailActivity.this , error.toString(), Toast.LENGTH_LONG).show();
            }
        }) {
            @NotNull @Override protected Map<String, String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("Id_Film_Inserito", String.valueOf(id));
                params.put("User_Proprietario", userName);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void verificaSePresenteNeiDaVedere(int id, String userName) {
        final int[] validiti = new int[1];
        StringRequest stringRequest = new StringRequest(Request.Method.POST, VEDURL, new com.android.volley.Response.Listener<String>() {
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
                        OcchialiDaVedere.setImageResource(R.drawable.ic__d_glasses);
                        stato_V = false;
                    }else{
                        OcchialiDaVedere.setImageResource(R.drawable.ic__d_glasses_active);
                        stato_V = true;
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
                params.put("User_Proprietario", userName);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void PrendiDettagliFilm(Integer id_film, String Titolo, String Poster, String userName) {
        String Titolo_Mod = Titolo.replaceAll("'", "/");
        StringRequest stringRequest = new StringRequest(Request.Method.POST, RECURL, new com.android.volley.Response.Listener<String>() {
            @Override public void onResponse(String response){
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray array = jsonObject.getJSONArray(JSON_ARRAY);
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject object = array.getJSONObject(i);
                        String str_n_rece = object.getString("Numero_Recensioni");
                        String str_valu = object.getString("Valutazione");
                        if(str_valu.equals("null")){
                            Numero_Recensioni = 0;
                            Valutazione_Media = 0.0;
                        }else {
                            Numero_Recensioni = Integer.valueOf(str_n_rece);
                            Valutazione_Media = Double.valueOf(str_valu);
                        }
                    }
                    Intent intent1 = new Intent(MovieDetailActivity.this, RecensioniActivity.class);
                    intent1.putExtra("Id_Film", id_film);
                    intent1.putExtra("Titolo_Film", Titolo);
                    intent1.putExtra("Immagine_Poster", Poster);
                    intent1.putExtra("Nome_Utente", userName);
                    intent1.putExtra("Numero_Recensioni", Numero_Recensioni);
                    intent1.putExtra("Valutazione", Valutazione_Media);
                    startActivity(intent1);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override public void onErrorResponse(VolleyError error) {
                Toast.makeText(MovieDetailActivity.this , error.toString(), Toast.LENGTH_LONG).show();
            }
        }) {
            @NotNull @Override protected Map<String, String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("Titolo_Film_Recensito", Titolo_Mod);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void PrendiAttributiLista(Integer id_film, String userName, String tipoLista,String Poster, String Titolo) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, VISURL, new com.android.volley.Response.Listener<String>() {
            @Override public void onResponse(String response) {
                try{
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray array = jsonObject.getJSONArray(JSON_ARRAY);
                    for(int i = 0; i < array.length(); i++) {
                        JSONObject object = array.getJSONObject(i);
                        Descrizione = object.getString("Descrizione");
                        Visibilità = object.getString("Visibilita");
                    }
                    verificaSePresente(id_film, userName, tipoLista, Poster, Titolo);
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
                params.put("User_Proprietario", userName);
                params.put("Tipo_Lista",tipoLista);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void verificaNomeLista(Integer id_film, String utente, String tipoLista, String Poster, String Titolo) {
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
                        verificaSePresente(id_film, utente, tipoLista, Poster, Titolo);
                    }else{
                        Toast.makeText(MovieDetailActivity.this , "Lista Già Creata Con Il Nome Di " + tipoLista, Toast.LENGTH_LONG).show();
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
                params.put("Id_Film_Inserito", String.valueOf(id_film));
                params.put("User_Proprietario", utente);
                params.put("Tipo_Lista",tipoLista);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void ListePresenti(String utente) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, LISURL, new com.android.volley.Response.Listener<String>() {
            @Override public void onResponse(String response){
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray array = jsonObject.getJSONArray(JSON_ARRAY);
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject object = array.getJSONObject(i);
                        String respo = object.getString("Tipo_Lista");
                        listefilm.add(respo);
                    }
                    aggiungiA.attachDataSource(listefilm);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override public void onErrorResponse(VolleyError error) {
                Toast.makeText(MovieDetailActivity.this , error.toString(), Toast.LENGTH_LONG).show();
            }
        }) {
            @NotNull @Override protected Map<String, String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("User_Proprietario", utente);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void RimuoviDaiPreferiti(int id, String utente) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, RIMURL, new com.android.volley.Response.Listener<String>() {
            @Override public void onResponse(String response){
                Toast.makeText(MovieDetailActivity.this , "Film Rimosso Dalla Lista Dei Preferiti.", Toast.LENGTH_LONG).show();
                verificaSePresenteNeiPreferiti(id, utente);
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override public void onErrorResponse(VolleyError error) {
                Toast.makeText(MovieDetailActivity.this , error.toString(), Toast.LENGTH_LONG).show();
            }
        }) {
            @NotNull @Override protected Map<String, String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("Id_Film_Inserito", String.valueOf(id));
                params.put("User_Proprietario", utente);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void verificaSePresente(int id, String utente, String tipoLista, String Poster, String Titolo) {
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
                        InserisciNelleListe(id, Poster, Titolo, tipoLista, utente);
                    }else{
                        Toast.makeText(MovieDetailActivity.this , "Film Già Presente Nella Lista " + tipoLista, Toast.LENGTH_LONG).show();
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
                params.put("User_Proprietario", utente);
                params.put("Tipo_Lista", tipoLista);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void verificaSePresenteNeiPreferiti(int id, String utente) {
        final int[] validiti = new int[1];
        StringRequest stringRequest = new StringRequest(Request.Method.POST, PREFURL, new com.android.volley.Response.Listener<String>() {
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
                params.put("User_Proprietario", utente);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    protected void InserisciNelleListe(int id, String poster, String titolo, String tipoLista, String utente) {
        String titoloMod = titolo.replaceAll("'", "/");
        StringRequest stringRequest = new StringRequest(Request.Method.POST, INSURL, new com.android.volley.Response.Listener<String>() {
            @Override public void onResponse(String response){
                    if(tipoLista.equals("Preferiti") || tipoLista.equals("Da Vedere")){
                        if(tipoLista.equals("Preferiti")){
                            Toast.makeText(MovieDetailActivity.this, "Film Aggiunto Nella Lista " + tipoLista, Toast.LENGTH_LONG).show();
                            verificaSePresenteNeiPreferiti(id_film, UserName);
                        } else{
                            Toast.makeText(MovieDetailActivity.this, "Film Aggiunto Nella Lista " + tipoLista, Toast.LENGTH_LONG).show();
                            verificaSePresenteNeiDaVedere(id_film, UserName);
                        }
                    }else {
                        aggiungiA.setSelectedIndex(0);
                        Toast.makeText(MovieDetailActivity.this, "Film Aggiunto Nella Lista " + tipoLista, Toast.LENGTH_LONG).show();
                    }
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
                params.put("Descrizione", Descrizione);
                params.put("Visibilita", Visibilità);
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
                filmtrama.setText("Trama Non Presente");
            }
        }else{
            filmtrama.setText("Trama Non Presente");
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