package com.example.cinematesmobile.Recensioni;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.cinematesmobile.ModelDBInterno.DBModelFotoProfiloResponce;
import com.example.cinematesmobile.ModelDBInterno.DBModelRecensioniResponce;
import com.example.cinematesmobile.ModelDBInterno.DBModelVerifica;
import com.example.cinematesmobile.ModelDBInterno.DBModelVerificaResults;
import com.example.cinematesmobile.R;
import com.example.cinematesmobile.Recensioni.Adapter.RecensioniAdapter;
import com.example.cinematesmobile.Recensioni.Model.DBModelFotoProfilo;
import com.example.cinematesmobile.Recensioni.Model.DBModelRecensioni;
import com.example.cinematesmobile.RetrofitClient.RetrofitClientDBInterno;
import com.example.cinematesmobile.RetrofitService.RetrofitServiceDBInterno;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RecensioniActivity extends AppCompatActivity {

    private Integer Id_Film, N_Rece;
    private Double Val;
    private String Titolo_film;
    private String Poster;
    private AppCompatTextView TitoloFilmPerRecensioni, Numero_Recensioni, Voto_Medio;
    private AppCompatButton ScriviRecensione;
    private AppCompatImageView PosterFilmRece;
    private AppCompatImageButton Previously;
    private RecyclerView RecensioniScritte;
    private String Utente;
    private List<DBModelRecensioni> recensioniList = new ArrayList<>();
    private RecensioniAdapter recensioniAdapter;
    private RetrofitServiceDBInterno retrofitServiceDBInterno;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recensioni);
        Id_Film = getIntent().getExtras().getInt("Id_Film");
        N_Rece = getIntent().getExtras().getInt("Numero_Recensioni");
        Val = getIntent().getExtras().getDouble("Valutazione");
        Titolo_film = getIntent().getExtras().getString("Titolo_Film");
        Poster= getIntent().getExtras().getString("Immagine_Poster");
        Utente= getIntent().getExtras().getString("Nome_Utente");
        Previously = findViewById(R.id.previously);
        TitoloFilmPerRecensioni = findViewById(R.id.Titolo_film_per_recensioni);
        RecensioniScritte = findViewById(R.id.lista_recensioni);
        Numero_Recensioni = findViewById(R.id.Numero_Rece);
        Voto_Medio = findViewById(R.id.Voto_Medio);
        ScriviRecensione = findViewById(R.id.button_scrivi_recensione);
        PosterFilmRece = findViewById(R.id.Poster_Film_Rece);
        TitoloFilmPerRecensioni.setText(Titolo_film);
        Numero_Recensioni.setText(String.valueOf(N_Rece));
        Voto_Medio.setText(String.valueOf(Val));
        retrofitServiceDBInterno = RetrofitClientDBInterno.getClient().create(RetrofitServiceDBInterno.class);
        RecensioniScritte.setLayoutManager(new LinearLayoutManager(RecensioniActivity.this, LinearLayoutManager.HORIZONTAL, false));
        String Titolo_Mod = Titolo_film.replaceAll("'", "/");
        Call<DBModelRecensioniResponce> recensioniResponceCall = retrofitServiceDBInterno.PrendiRecensioni(Titolo_Mod);
        recensioniResponceCall.enqueue(new Callback<DBModelRecensioniResponce>() {
            @Override public void onResponse(Call<DBModelRecensioniResponce> call, Response<DBModelRecensioniResponce> response) {
                DBModelRecensioniResponce dbModelRecensioniResponce = response.body();
                if(dbModelRecensioniResponce != null){
                    recensioniList = dbModelRecensioniResponce.getResults();
                    if(!(recensioniList.isEmpty())){
                        RecensioniScritte.setLayoutManager(new LinearLayoutManager(RecensioniActivity.this, LinearLayoutManager.VERTICAL, false));
                        recensioniAdapter = new RecensioniAdapter(RecensioniActivity.this, recensioniList, Utente);
                        RecensioniScritte.setAdapter(recensioniAdapter);
                    }else{
                        Toast.makeText(RecensioniActivity.this, "Nessun utente ha recensito questo film.",Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(RecensioniActivity.this, "Impossibile caricare le recensioni.",Toast.LENGTH_SHORT).show();
                }
            }
            @Override public void onFailure(Call<DBModelRecensioniResponce> call, Throwable t) {
                Toast.makeText(RecensioniActivity.this, "Ops qualcosa è andato storto.",Toast.LENGTH_SHORT).show();
            }
        });
        Glide.with(RecensioniActivity.this).load(Poster).into(PosterFilmRece);
        ScriviRecensione.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                Call<DBModelFotoProfiloResponce> fotoProfiloResponceCall = retrofitServiceDBInterno.PrendiFotoProfilo(Utente);
                fotoProfiloResponceCall.enqueue(new Callback<DBModelFotoProfiloResponce>() {
                    @Override public void onResponse(Call<DBModelFotoProfiloResponce> call, Response<DBModelFotoProfiloResponce> response) {
                        DBModelFotoProfiloResponce fotoProfiloResponce = response.body();
                        if(fotoProfiloResponce != null){
                            List<DBModelFotoProfilo> fotoProfilos = fotoProfiloResponce.getResults();
                            if(!(fotoProfilos.isEmpty())){
                                String Titolo_Mod = Titolo_film.replaceAll("'", "/");
                                Call<DBModelVerifica> verificaCall = retrofitServiceDBInterno.VerificaPresenzaRecensione(Titolo_Mod, Utente);
                                verificaCall.enqueue(new Callback<DBModelVerifica>() {
                                    @Override public void onResponse(Call<DBModelVerifica> call, Response<DBModelVerifica> response) {
                                        DBModelVerifica dbModelVerifica = response.body();
                                        if(dbModelVerifica != null) {
                                            List<DBModelVerificaResults> modelVerificaResults = dbModelVerifica.getResults();
                                            if (modelVerificaResults.get(0).getCodVerifica() == 0) {
                                                Intent intent2 = new Intent(RecensioniActivity.this, ScriviRecensioneActivity.class);
                                                intent2.putExtra("Nome_Utente", Utente);
                                                intent2.putExtra("Titolo_Film", Titolo_Mod);
                                                intent2.putExtra("Foto_Profilo", fotoProfilos.get(0).getFotoProfilo());
                                                startActivity(intent2);
                                            }else{
                                                Toast.makeText(RecensioniActivity.this , "Ha già inserito la recensione per questo film.", Toast.LENGTH_SHORT).show();
                                            }
                                        }else{
                                            Toast.makeText(RecensioniActivity.this , "Impossibile verificare presenza recensione.", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                    @Override public void onFailure(Call<DBModelVerifica> call, Throwable t) {
                                        Toast.makeText(RecensioniActivity.this , "Ops qualcosa è andato storto.", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }else{
                                Toast.makeText(RecensioniActivity.this , "Impossibile caricare foto profilo.", Toast.LENGTH_SHORT).show();
                            }
                        }else{
                            Toast.makeText(RecensioniActivity.this , "Impossibile caricare foto profilo.", Toast.LENGTH_SHORT).show();
                        }
                    }
                    @Override public void onFailure(Call<DBModelFotoProfiloResponce> call, Throwable t) {
                        Toast.makeText(RecensioniActivity.this , "Ops qualcosa è andato storto.", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        Previously.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    @Override protected void onResume() {
        super.onResume();
        recensioniList.clear();
        String Titolo_Mod = Titolo_film.replaceAll("'", "/");
        Call<DBModelRecensioniResponce> recensioniResponceCall = retrofitServiceDBInterno.PrendiRecensioni(Titolo_Mod);
        recensioniResponceCall.enqueue(new Callback<DBModelRecensioniResponce>() {
            @Override public void onResponse(Call<DBModelRecensioniResponce> call, Response<DBModelRecensioniResponce> response) {
                DBModelRecensioniResponce dbModelRecensioniResponce = response.body();
                if(dbModelRecensioniResponce != null){
                    recensioniList = dbModelRecensioniResponce.getResults();
                    if(!(recensioniList.isEmpty())){
                        RecensioniScritte.setLayoutManager(new LinearLayoutManager(RecensioniActivity.this, LinearLayoutManager.VERTICAL, false));
                        recensioniAdapter = new RecensioniAdapter(RecensioniActivity.this, recensioniList, Utente);
                        RecensioniScritte.setAdapter(recensioniAdapter);
                    }else{
                        Toast.makeText(RecensioniActivity.this, "Nessun utente ha recensito questo film.",Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(RecensioniActivity.this, "Impossibile caricare le recensioni.",Toast.LENGTH_SHORT).show();
                }
            }
            @Override public void onFailure(Call<DBModelRecensioniResponce> call, Throwable t) {
                Toast.makeText(RecensioniActivity.this, "Ops qualcosa è andato storto.",Toast.LENGTH_SHORT).show();
            }
        });
    }
}