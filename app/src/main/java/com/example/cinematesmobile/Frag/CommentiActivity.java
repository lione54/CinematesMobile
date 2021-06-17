package com.example.cinematesmobile.Frag;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cinematesmobile.Frag.Adapter.CommentiAdapter;
import com.example.cinematesmobile.Frag.Adapter.MovieListAltroUtenteAdapter;
import com.example.cinematesmobile.Frag.Model.DBModelCommenti;
import com.example.cinematesmobile.Frag.Model.DBModelDataFilms;
import com.example.cinematesmobile.Frag.Model.DBModelDataListeFilm;
import com.example.cinematesmobile.ModelDBInterno.DBModelCommentiResponce;
import com.example.cinematesmobile.ModelDBInterno.DBModelDataListeFilmResponce;
import com.example.cinematesmobile.ModelDBInterno.DBModelFilmsResponce;
import com.example.cinematesmobile.ModelDBInterno.DBModelListeFilmResponse;
import com.example.cinematesmobile.ModelDBInterno.DBModelRecensioniResponce;
import com.example.cinematesmobile.ModelDBInterno.DBModelResponseToInsert;
import com.example.cinematesmobile.ModelDBInterno.DBModelVerifica;
import com.example.cinematesmobile.ModelDBInterno.DBModelVerificaResults;
import com.example.cinematesmobile.R;
import com.example.cinematesmobile.Recensioni.Adapter.RecensioniAdapter;
import com.example.cinematesmobile.Recensioni.Adapter.RecensioniAdapterPerCommenti;
import com.example.cinematesmobile.Recensioni.Model.DBModelRecensioni;
import com.example.cinematesmobile.RetrofitClient.RetrofitClientDBInterno;
import com.example.cinematesmobile.RetrofitService.RetrofitServiceDBInterno;
import com.google.android.material.textfield.TextInputEditText;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CommentiActivity extends AppCompatActivity {

    private String UserProprietario, Titololista, AltroUser, TextDescrizione, TipoCorrente, TitoloFilm, Dichiè;
    private AppCompatImageButton Indietro;
    private AppCompatTextView NomeLista, Descrizione, ScrittaIni;
    private LinearLayoutCompat Desc;
    private RecyclerView Film, Commenti;
    private MovieListAltroUtenteAdapter movieListAltroUtenteAdapter;
    private RetrofitServiceDBInterno retrofitServiceDBInterno;
    private CommentiAdapter commentiAdapter;
    private AppCompatButton InviaCommento;
    private TextInputEditText ScriviCommento;
    private List<DBModelRecensioni> recensioniList = new ArrayList<>();
    private RecensioniAdapterPerCommenti recensioniAdapter;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_commenti);
        retrofitServiceDBInterno = RetrofitClientDBInterno.getClient().create(RetrofitServiceDBInterno.class);
        UserProprietario = getIntent().getExtras().getString("UsernameProprietario");
        AltroUser = getIntent().getExtras().getString("UsernameAltroUtente");
        TipoCorrente = getIntent().getExtras().getString("TipoCorrente");
        if(TipoCorrente.equals("Lista")) {
            Titololista = getIntent().getExtras().getString("TitoloLista");
            if(getIntent().getExtras().getString("Descrizione") != null) {
                TextDescrizione = getIntent().getExtras().getString("Descrizione");
            }else {
                if(getIntent().getExtras().getString("ClickNotifica") != null){
                    if(getIntent().getExtras().getString("ClickNotifica").equals("Yes")){
                        Call<DBModelDataListeFilmResponce> listeFilmResponseCall = retrofitServiceDBInterno.PrendiProprieListe(UserProprietario);
                        listeFilmResponseCall.enqueue(new Callback<DBModelDataListeFilmResponce>() {
                            @Override public void onResponse(@NotNull Call<DBModelDataListeFilmResponce> call, @NotNull Response<DBModelDataListeFilmResponce> response) {
                                DBModelDataListeFilmResponce dbModelDataListeFilmResponce = response.body();
                                if(dbModelDataListeFilmResponce != null){
                                    List<DBModelDataListeFilm> listeFilms = dbModelDataListeFilmResponce.getListeFilms();
                                    if(!(listeFilms.isEmpty())){
                                        for(int o = 0; o < listeFilms.size(); o++){
                                            if(listeFilms.get(o).getTitoloLista().equals(Titololista)){
                                                TextDescrizione = listeFilms.get(o).getDescrizioneLista();
                                            }
                                        }
                                        Call<DBModelResponseToInsert> rimuoviNotifiche = retrofitServiceDBInterno.SegnaComeLetto(UserProprietario, "Commento", "Lista", Titololista);
                                        rimuoviNotifiche.enqueue(new Callback<DBModelResponseToInsert>() {
                                            @Override public void onResponse(@NotNull Call<DBModelResponseToInsert> call,@NotNull Response<DBModelResponseToInsert> response) {
                                                DBModelResponseToInsert dbModelResponseToInsert = response.body();
                                                if(dbModelResponseToInsert != null){
                                                    if (dbModelResponseToInsert.getStato().equals("Successfull")){

                                                    }
                                                }
                                            }
                                            @Override public void onFailure(@NotNull Call<DBModelResponseToInsert> call,@NotNull Throwable t) {

                                            }
                                        });
                                    }
                                }
                            }
                            @Override public void onFailure(@NotNull Call<DBModelDataListeFilmResponce> call,@NotNull Throwable t) {
                            }
                        });
                    }
                }
            }
            Dichiè = getIntent().getExtras().getString("DiChiè");
        }else{
            TitoloFilm = getIntent().getExtras().getString("TitoloFilm");
        }
        Indietro = findViewById(R.id.previously);
        NomeLista = findViewById(R.id.nome_lista);
        Descrizione = findViewById(R.id.descrizione_lista_altro_user);
        Commenti = findViewById(R.id.commentiUtenti);
        Film = findViewById(R.id.film_altro_utente);
        InviaCommento = findViewById(R.id.invia_commento);
        ScriviCommento = findViewById(R.id.commenta);
        ScrittaIni = findViewById(R.id.TipoRelativoCommento);
        Desc = findViewById(R.id.descrizione_lista_altro_user_layout);
        Indietro.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                onBackPressed();
            }
        });
        if(TipoCorrente.equals("Lista")) {
            NomeLista.setText(Titololista);
            Descrizione.setText(TextDescrizione);
            if(Dichiè.equals("Mia")){
                AltroUser = UserProprietario;
            }
            Call<DBModelFilmsResponce> filmsResponceCall = retrofitServiceDBInterno.PrendiFilmDaDB(AltroUser, Titololista);
            filmsResponceCall.enqueue(new Callback<DBModelFilmsResponce>() {
                @Override public void onResponse(@NonNull Call<DBModelFilmsResponce> call, @NonNull retrofit2.Response<DBModelFilmsResponce> response) {
                    DBModelFilmsResponce dbModelFilmsResponce = response.body();
                    if (dbModelFilmsResponce != null) {
                        List<DBModelDataFilms> preferiti = dbModelFilmsResponce.getResults();
                        if (!(preferiti.isEmpty())) {
                            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(Film.getContext(), RecyclerView.HORIZONTAL, false);
                            Film.setLayoutManager(linearLayoutManager);
                            Film.setHasFixedSize(false);
                            movieListAltroUtenteAdapter = new MovieListAltroUtenteAdapter(CommentiActivity.this, preferiti, AltroUser, UserProprietario);
                            Film.setAdapter(movieListAltroUtenteAdapter);
                            movieListAltroUtenteAdapter.notifyDataSetChanged();
                        } else {
                            Toast.makeText(CommentiActivity.this, "Nessun film da mostrare.", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(CommentiActivity.this, "Impossibile recuperare i film.", Toast.LENGTH_SHORT).show();
                    }
                }
                @Override public void onFailure(@NonNull Call<DBModelFilmsResponce> call, @NonNull Throwable t) {
                    Toast.makeText(CommentiActivity.this, "Ops qualcosa è andato storto.", Toast.LENGTH_SHORT).show();
                }
            });
            Call<DBModelVerifica> verificaCommentiCall = retrofitServiceDBInterno.VerificaSeCiSonoCommenti(AltroUser, TipoCorrente, Titololista);
            verificaCommentiCall.enqueue(new Callback<DBModelVerifica>() {
                @Override public void onResponse(@NonNull Call<DBModelVerifica> call, @NonNull Response<DBModelVerifica> response) {
                    DBModelVerifica dbModelVerifica = response.body();
                    if (dbModelVerifica != null) {
                        List<DBModelVerificaResults> results = dbModelVerifica.getResults();
                        if (results.get(0).getCodVerifica() == 1) {
                            Call<DBModelCommentiResponce> commentiCall = retrofitServiceDBInterno.PrendiCommenti(AltroUser, TipoCorrente, Titololista);
                            commentiCall.enqueue(new Callback<DBModelCommentiResponce>() {
                                @Override public void onResponse(@NonNull Call<DBModelCommentiResponce> call, @NonNull Response<DBModelCommentiResponce> response) {
                                    DBModelCommentiResponce dbModelCommentiResponce = response.body();
                                    if (dbModelCommentiResponce != null) {
                                        List<DBModelCommenti> commentiList = dbModelCommentiResponce.getResults();
                                        if (!(commentiList.isEmpty())) {
                                            Commenti.setLayoutManager(new LinearLayoutManager(CommentiActivity.this, LinearLayoutManager.VERTICAL, false));
                                            commentiAdapter = new CommentiAdapter(CommentiActivity.this, commentiList);
                                            Commenti.setAdapter(commentiAdapter);
                                        } else {
                                            Toast.makeText(CommentiActivity.this, "Non sono presenti commenti per questo post.", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }
                                @Override public void onFailure(@NonNull Call<DBModelCommentiResponce> call, @NonNull Throwable t) {
                                    Toast.makeText(CommentiActivity.this, "Ops qualcosa è andato storto.", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }
                }
                @Override public void onFailure(@NonNull Call<DBModelVerifica> call, @NonNull Throwable t) {
                    Toast.makeText(CommentiActivity.this, "Ops qualcosa è andato storto.", Toast.LENGTH_SHORT).show();
                }
            });
            ScriviCommento.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                    if (ScriviCommento.length() > 0) {
                        Call<DBModelResponseToInsert> scriviCommentoCall = retrofitServiceDBInterno.ScriviCommento(ScriviCommento.getText().toString(), UserProprietario, TipoCorrente, Titololista, AltroUser);
                        scriviCommentoCall.enqueue(new Callback<DBModelResponseToInsert>() {
                            @Override public void onResponse(@NonNull Call<DBModelResponseToInsert> call, @NonNull Response<DBModelResponseToInsert> response) {
                                DBModelResponseToInsert dbModelResponseToInsert = response.body();
                                if (dbModelResponseToInsert != null) {
                                    if (dbModelResponseToInsert.getStato().equals("Successfull")) {
                                        if(!(UserProprietario.equals(AltroUser))){
                                            Call<DBModelResponseToInsert> mandanotificaCall = retrofitServiceDBInterno.NotificaInserimentoCommento(AltroUser,  "Commento", TipoCorrente, Titololista, UserProprietario);
                                            mandanotificaCall.enqueue(new Callback<DBModelResponseToInsert>() {
                                                @Override public void onResponse(@NonNull Call<DBModelResponseToInsert> call,@NonNull Response<DBModelResponseToInsert> response) {
                                                    DBModelResponseToInsert dbModelResponseToInsert = response.body();
                                                    if (dbModelResponseToInsert != null) {
                                                        if (dbModelResponseToInsert.getStato().equals("Successfull")) {
                                                            ScriviCommento.setText("");
                                                            recreate();
                                                        }
                                                    }
                                                }
                                                @Override public void onFailure(@NonNull Call<DBModelResponseToInsert> call,@NonNull Throwable t) {
                                                    Toast.makeText(CommentiActivity.this, "Ops qualcosa è andato storto.", Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                        }
                                    }
                                }
                            }
                            @Override public void onFailure(@NonNull Call<DBModelResponseToInsert> call, @NonNull Throwable t) {
                                Toast.makeText(CommentiActivity.this, "Ops qualcosa è andato storto.", Toast.LENGTH_SHORT).show();
                            }
                        });
                        return true;
                    } else {
                        Toast.makeText(CommentiActivity.this, "Scrivi qualcosa.", Toast.LENGTH_SHORT).show();
                        return false;
                    }
                }
            });
            InviaCommento.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    if (ScriviCommento.length() > 0) {
                        Call<DBModelResponseToInsert> scriviCommentoCall = retrofitServiceDBInterno.ScriviCommento(ScriviCommento.getText().toString(), UserProprietario, TipoCorrente, Titololista, AltroUser);
                        scriviCommentoCall.enqueue(new Callback<DBModelResponseToInsert>() {
                            @Override public void onResponse(@NonNull Call<DBModelResponseToInsert> call, @NonNull Response<DBModelResponseToInsert> response) {
                                DBModelResponseToInsert dbModelResponseToInsert = response.body();
                                if (dbModelResponseToInsert != null) {
                                    if (dbModelResponseToInsert.getStato().equals("Successfull")) {
                                        if(!(UserProprietario.equals(AltroUser))){
                                            Call<DBModelResponseToInsert> mandanotificaCall = retrofitServiceDBInterno.NotificaInserimentoCommento(AltroUser,  "Commento", TipoCorrente, Titololista, UserProprietario);
                                            mandanotificaCall.enqueue(new Callback<DBModelResponseToInsert>() {
                                                @Override public void onResponse(@NonNull Call<DBModelResponseToInsert> call,@NonNull Response<DBModelResponseToInsert> response) {
                                                    DBModelResponseToInsert dbModelResponseToInsert = response.body();
                                                    if (dbModelResponseToInsert != null) {
                                                        if (dbModelResponseToInsert.getStato().equals("Successfull")) {
                                                            ScriviCommento.setText("");
                                                            recreate();
                                                        }
                                                    }
                                                }
                                                @Override public void onFailure(@NonNull Call<DBModelResponseToInsert> call,@NonNull Throwable t) {
                                                    Toast.makeText(CommentiActivity.this, "Ops qualcosa è andato storto.", Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                        }
                                    }
                                }
                            }
                            @Override public void onFailure(@NonNull Call<DBModelResponseToInsert> call, @NonNull Throwable t) {
                                Toast.makeText(CommentiActivity.this, "Ops qualcosa è andato storto.", Toast.LENGTH_SHORT).show();
                            }
                        });
                    } else {
                        Toast.makeText(CommentiActivity.this, "Scrivi qualcosa.", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }else{
            ScrittaIni.setText("Nome film:");
            NomeLista.setText(TitoloFilm);
            Desc.setVisibility(View.GONE);
            if(getIntent().getExtras().getString("ClickNotifica") != null) {
                if (getIntent().getExtras().getString("ClickNotifica").equals("Yes")) {
                    Call<DBModelResponseToInsert> rimuoviNotifiche = retrofitServiceDBInterno.SegnaComeLetto(UserProprietario, "Commento", "Recensioni", TitoloFilm);
                    rimuoviNotifiche.enqueue(new Callback<DBModelResponseToInsert>() {
                        @Override public void onResponse(@NotNull Call<DBModelResponseToInsert> call,@NotNull Response<DBModelResponseToInsert> response) {
                            DBModelResponseToInsert dbModelResponseToInsert = response.body();
                            if(dbModelResponseToInsert != null){
                                if (dbModelResponseToInsert.getStato().equals("Successfull")){

                                }
                            }
                        }
                        @Override public void onFailure(@NotNull Call<DBModelResponseToInsert> call,@NotNull Throwable t) {

                        }
                    });
                }
            }
            Call<DBModelRecensioniResponce> recensioniResponceCall = retrofitServiceDBInterno.PrendiRecensioni(TitoloFilm, "Commento", AltroUser);
            recensioniResponceCall.enqueue(new Callback<DBModelRecensioniResponce>() {
                @Override public void onResponse(@NonNull Call<DBModelRecensioniResponce> call,@NonNull Response<DBModelRecensioniResponce> response) {
                    DBModelRecensioniResponce dbModelRecensioniResponce = response.body();
                    if(dbModelRecensioniResponce != null){
                        recensioniList = dbModelRecensioniResponce.getResults();
                        if(!(recensioniList.isEmpty())){
                            Film.setLayoutManager(new LinearLayoutManager(CommentiActivity.this, LinearLayoutManager.VERTICAL, false));
                            recensioniAdapter = new RecensioniAdapterPerCommenti(CommentiActivity.this, recensioniList, UserProprietario);
                            Film.setAdapter(recensioniAdapter);
                        }else{
                            Toast.makeText(CommentiActivity.this, "Nessun utente ha recensito questo film",Toast.LENGTH_SHORT).show();
                        }
                    }else {
                        Toast.makeText(CommentiActivity.this, "Impossibile caricare le recensioni",Toast.LENGTH_SHORT).show();
                    }
                }
                @Override public void onFailure(@NonNull Call<DBModelRecensioniResponce> call,@NonNull Throwable t) {
                    Toast.makeText(CommentiActivity.this, "Ops qualcosa è andato storto",Toast.LENGTH_SHORT).show();
                }
            });
            Call<DBModelVerifica> verificaCommentiCall = retrofitServiceDBInterno.VerificaSeCiSonoCommenti(AltroUser, TipoCorrente, TitoloFilm);
            verificaCommentiCall.enqueue(new Callback<DBModelVerifica>() {
                @Override public void onResponse(@NonNull Call<DBModelVerifica> call, @NonNull Response<DBModelVerifica> response) {
                    DBModelVerifica dbModelVerifica = response.body();
                    if (dbModelVerifica != null) {
                        List<DBModelVerificaResults> results = dbModelVerifica.getResults();
                        if (results.get(0).getCodVerifica() == 1) {
                            Call<DBModelCommentiResponce> commentiCall = retrofitServiceDBInterno.PrendiCommenti(AltroUser, TipoCorrente, TitoloFilm);
                            commentiCall.enqueue(new Callback<DBModelCommentiResponce>() {
                                @Override public void onResponse(@NonNull Call<DBModelCommentiResponce> call, @NonNull Response<DBModelCommentiResponce> response) {
                                    DBModelCommentiResponce dbModelCommentiResponce = response.body();
                                    if (dbModelCommentiResponce != null) {
                                        List<DBModelCommenti> commentiList = dbModelCommentiResponce.getResults();
                                        if (!(commentiList.isEmpty())) {
                                            Commenti.setLayoutManager(new LinearLayoutManager(CommentiActivity.this, LinearLayoutManager.VERTICAL, false));
                                            commentiAdapter = new CommentiAdapter(CommentiActivity.this, commentiList);
                                            Commenti.setAdapter(commentiAdapter);
                                        } else {
                                            Toast.makeText(CommentiActivity.this, "Non sono presenti commenti per questo post.", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }
                                @Override public void onFailure(@NonNull Call<DBModelCommentiResponce> call, @NonNull Throwable t) {
                                    Toast.makeText(CommentiActivity.this, "Ops qualcosa è andato storto.", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }
                }
                @Override public void onFailure(@NonNull Call<DBModelVerifica> call, @NonNull Throwable t) {
                    Toast.makeText(CommentiActivity.this, "Ops qualcosa è andato storto.", Toast.LENGTH_SHORT).show();
                }
            });
            ScriviCommento.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                    if (ScriviCommento.length() > 0) {
                        Call<DBModelResponseToInsert> scriviCommentoCall = retrofitServiceDBInterno.ScriviCommento(ScriviCommento.getText().toString(), UserProprietario, TipoCorrente, TitoloFilm, AltroUser);
                        scriviCommentoCall.enqueue(new Callback<DBModelResponseToInsert>() {
                            @Override public void onResponse(@NonNull Call<DBModelResponseToInsert> call, @NonNull Response<DBModelResponseToInsert> response) {
                                DBModelResponseToInsert dbModelResponseToInsert = response.body();
                                if (dbModelResponseToInsert != null) {
                                    if (dbModelResponseToInsert.getStato().equals("Successfull")) {
                                        if(!(UserProprietario.equals(AltroUser))){
                                            Call<DBModelResponseToInsert> mandanotificaCall = retrofitServiceDBInterno.NotificaInserimentoCommento(AltroUser,  "Commento", TipoCorrente, TitoloFilm, UserProprietario);
                                            mandanotificaCall.enqueue(new Callback<DBModelResponseToInsert>() {
                                                @Override public void onResponse(@NonNull Call<DBModelResponseToInsert> call,@NonNull Response<DBModelResponseToInsert> response) {
                                                    DBModelResponseToInsert dbModelResponseToInsert = response.body();
                                                    if (dbModelResponseToInsert != null) {
                                                        if (dbModelResponseToInsert.getStato().equals("Successfull")) {
                                                            ScriviCommento.getText().clear();
                                                            recreate();
                                                        }
                                                    }
                                                }
                                                @Override public void onFailure(@NonNull Call<DBModelResponseToInsert> call,@NonNull Throwable t) {
                                                    Toast.makeText(CommentiActivity.this, "Ops qualcosa è andato storto.", Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                        }
                                    }
                                }
                            }
                            @Override public void onFailure(@NonNull Call<DBModelResponseToInsert> call, @NonNull Throwable t) {
                                Toast.makeText(CommentiActivity.this, "Ops qualcosa è andato storto.", Toast.LENGTH_SHORT).show();
                            }
                        });
                        return true;
                    } else {
                        Toast.makeText(CommentiActivity.this, "Scrivi qualcosa.", Toast.LENGTH_SHORT).show();
                        return false;
                    }
                }
            });
            InviaCommento.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    if (ScriviCommento.length() > 0) {
                        Call<DBModelResponseToInsert> scriviCommentoCall = retrofitServiceDBInterno.ScriviCommento(ScriviCommento.getText().toString(), UserProprietario, TipoCorrente, TitoloFilm, AltroUser);
                        scriviCommentoCall.enqueue(new Callback<DBModelResponseToInsert>() {
                            @Override public void onResponse(@NonNull Call<DBModelResponseToInsert> call, @NonNull Response<DBModelResponseToInsert> response) {
                                DBModelResponseToInsert dbModelResponseToInsert = response.body();
                                if (dbModelResponseToInsert != null) {
                                    if (dbModelResponseToInsert.getStato().equals("Successfull")) {
                                        if(!(UserProprietario.equals(AltroUser))){
                                            Call<DBModelResponseToInsert> mandanotificaCall = retrofitServiceDBInterno.NotificaInserimentoCommento(AltroUser,  "Commento", TipoCorrente, TitoloFilm, UserProprietario);
                                            mandanotificaCall.enqueue(new Callback<DBModelResponseToInsert>() {
                                                @Override public void onResponse(@NonNull Call<DBModelResponseToInsert> call,@NonNull Response<DBModelResponseToInsert> response) {
                                                    DBModelResponseToInsert dbModelResponseToInsert = response.body();
                                                    if (dbModelResponseToInsert != null) {
                                                        if (dbModelResponseToInsert.getStato().equals("Successfull")) {
                                                            ScriviCommento.getText().clear();
                                                            recreate();
                                                        }
                                                    }
                                                }
                                                @Override public void onFailure(@NonNull Call<DBModelResponseToInsert> call,@NonNull Throwable t) {
                                                    Toast.makeText(CommentiActivity.this, "Ops qualcosa è andato storto.", Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                        }

                                    }
                                }
                            }
                            @Override public void onFailure(@NonNull Call<DBModelResponseToInsert> call, @NonNull Throwable t) {
                                Toast.makeText(CommentiActivity.this, "Ops qualcosa è andato storto.", Toast.LENGTH_SHORT).show();
                            }
                        });
                    } else {
                        Toast.makeText(CommentiActivity.this, "Scrivi qualcosa.", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
}