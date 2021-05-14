package com.example.cinematesmobile.Frag;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.cinematesmobile.Frag.Adapter.CommentiAdapter;
import com.example.cinematesmobile.Frag.Adapter.MovieListAltroUtenteAdapter;
import com.example.cinematesmobile.Frag.Model.DBModelCommenti;
import com.example.cinematesmobile.Frag.Model.DBModelDataFilms;
import com.example.cinematesmobile.ModelDBInterno.DBModelCommentiResponce;
import com.example.cinematesmobile.ModelDBInterno.DBModelFilmsResponce;
import com.example.cinematesmobile.ModelDBInterno.DBModelResponseToInsert;
import com.example.cinematesmobile.ModelDBInterno.DBModelVerifica;
import com.example.cinematesmobile.ModelDBInterno.DBModelVerificaResults;
import com.example.cinematesmobile.R;
import com.example.cinematesmobile.RetrofitClient.RetrofitClientDBInterno;
import com.example.cinematesmobile.RetrofitService.RetrofitServiceDBInterno;
import com.google.android.material.textfield.TextInputEditText;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CommentiActivity extends AppCompatActivity {

    private String UserProprietario, Titololista, AltroUser, TextDescrizione, TipoCorrente;
    private AppCompatImageButton Indietro;
    private AppCompatTextView NomeLista, Descrizione;
    private RecyclerView Film, Commenti;
    private MovieListAltroUtenteAdapter movieListAltroUtenteAdapter;
    private RetrofitServiceDBInterno retrofitServiceDBInterno;
    private CommentiAdapter commentiAdapter;
    private AppCompatButton InviaCommento;
    private TextInputEditText ScriviCommento;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_commenti);
        UserProprietario = getIntent().getExtras().getString("UsernameProprietario");
        AltroUser = getIntent().getExtras().getString("UsernameAltroUtente");
        TipoCorrente = getIntent().getExtras().getString("TipoCorrente");
        TextDescrizione = getIntent().getExtras().getString("Descrizione");
        if(TipoCorrente.equals("Lista")) {
            Titololista = getIntent().getExtras().getString("TitoloLista");
        }
        Indietro = findViewById(R.id.previously);
        NomeLista = findViewById(R.id.nome_lista);
        Descrizione = findViewById(R.id.descrizione_lista_altro_user);
        Commenti = findViewById(R.id.commentiUtenti);
        Film = findViewById(R.id.film_altro_utente);
        InviaCommento = findViewById(R.id.invia_commento);
        ScriviCommento = findViewById(R.id.commenta);
        retrofitServiceDBInterno = RetrofitClientDBInterno.getClient().create(RetrofitServiceDBInterno.class);
        NomeLista.setText(Titololista);
        Descrizione.setText(TextDescrizione);
        Indietro.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                onBackPressed();
            }
        });
        Call<DBModelFilmsResponce> filmsResponceCall = retrofitServiceDBInterno.PrendiFilmDaDB(AltroUser, Titololista);
        filmsResponceCall.enqueue(new Callback<DBModelFilmsResponce>() {
            @Override public void onResponse(@NonNull Call<DBModelFilmsResponce> call, @NonNull retrofit2.Response<DBModelFilmsResponce> response) {
                DBModelFilmsResponce dbModelFilmsResponce  = response.body();
                if(dbModelFilmsResponce != null){
                    List<DBModelDataFilms> preferiti = dbModelFilmsResponce.getResults();
                    if(!(preferiti.isEmpty())){
                        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(Film.getContext(), RecyclerView.HORIZONTAL, false);
                        Film.setLayoutManager(linearLayoutManager);
                        Film.setHasFixedSize(false);
                        movieListAltroUtenteAdapter = new MovieListAltroUtenteAdapter(CommentiActivity.this, preferiti, AltroUser, UserProprietario);
                        Film.setAdapter(movieListAltroUtenteAdapter);
                        movieListAltroUtenteAdapter.notifyDataSetChanged();
                    }else{
                        Toast.makeText(CommentiActivity.this,"Nessun film da mostrare.",Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(CommentiActivity.this,"Impossibile recuperare i film.",Toast.LENGTH_SHORT).show();
                }
            }
            @Override public void onFailure(@NonNull Call<DBModelFilmsResponce> call,@NonNull Throwable t) {
                Toast.makeText(CommentiActivity.this,"Ops qualcosa è andato storto.",Toast.LENGTH_SHORT).show();
            }
        });
        Call<DBModelVerifica> verificaCommentiCall = retrofitServiceDBInterno.VerificaSeCiSonoCommenti(AltroUser, TipoCorrente, Titololista);
        verificaCommentiCall.enqueue(new Callback<DBModelVerifica>() {
            @Override public void onResponse(@NonNull Call<DBModelVerifica> call,@NonNull Response<DBModelVerifica> response) {
                DBModelVerifica dbModelVerifica = response.body();
                if(dbModelVerifica != null){
                    List<DBModelVerificaResults> results = dbModelVerifica.getResults();
                    if(results.get(0).getCodVerifica() == 1){
                            Call<DBModelCommentiResponce> commentiCall = retrofitServiceDBInterno.PrendiCommenti(AltroUser, TipoCorrente, Titololista);
                            commentiCall.enqueue(new Callback<DBModelCommentiResponce>() {
                                @Override public void onResponse(@NonNull Call<DBModelCommentiResponce> call,@NonNull Response<DBModelCommentiResponce> response) {
                                    DBModelCommentiResponce dbModelCommentiResponce = response.body();
                                    if(dbModelCommentiResponce != null){
                                        List<DBModelCommenti> commentiList = dbModelCommentiResponce.getResults();
                                        if(!(commentiList.isEmpty())){
                                            Commenti.setLayoutManager(new LinearLayoutManager(CommentiActivity.this, LinearLayoutManager.VERTICAL, false));
                                            commentiAdapter = new CommentiAdapter(CommentiActivity.this, commentiList);
                                            Commenti.setAdapter(commentiAdapter);
                                        }else{
                                            Toast.makeText(CommentiActivity.this,"Non sono presenti commenti per questo post.",Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }
                                @Override public void onFailure(@NonNull Call<DBModelCommentiResponce> call,@NonNull Throwable t) {
                                    Toast.makeText(CommentiActivity.this,"Ops qualcosa è andato storto.",Toast.LENGTH_SHORT).show();
                                }
                            });
                    }
                }
            }
            @Override public void onFailure(@NonNull Call<DBModelVerifica> call,@NonNull Throwable t) {
                Toast.makeText(CommentiActivity.this,"Ops qualcosa è andato storto.",Toast.LENGTH_SHORT).show();
            }
        });
        InviaCommento.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                if(ScriviCommento.length() > 0){
                    Call<DBModelResponseToInsert> scriviCommentoCall = retrofitServiceDBInterno.ScriviCommento(ScriviCommento.getText().toString(), UserProprietario, TipoCorrente,Titololista, AltroUser);
                    scriviCommentoCall.enqueue(new Callback<DBModelResponseToInsert>() {
                        @Override public void onResponse(@NonNull Call<DBModelResponseToInsert> call,@NonNull Response<DBModelResponseToInsert> response) {
                            DBModelResponseToInsert dbModelResponseToInsert = response.body();
                            if(dbModelResponseToInsert != null){
                                if(dbModelResponseToInsert.getStato().equals("Successfull")){
                                    recreate();
                                }
                            }
                        }
                        @Override public void onFailure(@NonNull Call<DBModelResponseToInsert> call,@NonNull Throwable t) {
                            Toast.makeText(CommentiActivity.this,"Ops qualcosa è andato storto.",Toast.LENGTH_SHORT).show();
                        }
                    });
                }else{
                    Toast.makeText(CommentiActivity.this,"Scrivi qualcosa.",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}