package com.example.cinematesmobile.Search;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.example.cinematesmobile.Frag.Adapter.AltroUtenteAmicoAdapter;
import com.example.cinematesmobile.Frag.FilmInComuneActivity;
import com.example.cinematesmobile.Frag.ListeAmiciActivity;
import com.example.cinematesmobile.Frag.Model.DBModelDataListeFilm;
import com.example.cinematesmobile.Frag.Model.DBModelProfiloAltroUtente;
import com.example.cinematesmobile.Frag.ProprieRecensioniActivity;
import com.example.cinematesmobile.ModelDBInterno.DBModelDataListeFilmResponce;
import com.example.cinematesmobile.ModelDBInterno.DBModelProfiloAltroUtenteResponce;
import com.example.cinematesmobile.ModelDBInterno.DBModelVerifica;
import com.example.cinematesmobile.ModelDBInterno.DBModelVerificaResults;
import com.example.cinematesmobile.R;
import com.example.cinematesmobile.RetrofitClient.RetrofitClientDBInterno;
import com.example.cinematesmobile.RetrofitService.RetrofitServiceDBInterno;
import com.flaviofaria.kenburnsview.KenBurnsView;
import java.util.List;
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActivityProfiloAltroUtente extends AppCompatActivity {

    private String UsernameAltroUtente, UsernameProprietario;
    private RetrofitServiceDBInterno retrofitServiceDBInterno;
    private KenBurnsView ImmagineCopertina;
    private LinearLayout LayoutNomeAltroUtente, LayoutCognomeAltroUtente, LayoutEmailAltroUtente, LayoutNascitaAltroUtente, LayoutSessoAltroUtente, VaiRecensioniAltroUtente, VediAmici, VaiFilmInComune;
    public CircleImageView ImmagineProfilo;
    private RecyclerView ListeVisibili;
    private AppCompatImageButton Previously;
    private RecyclerView.LayoutManager layoutManager;
    private AltroUtenteAmicoAdapter altroUtenteAmicoAdapter;
    public AppCompatTextView UsernameProfilo, NumeroRecensioniScritte, NumeroAmici, Amici, FilmInComune;
    public AppCompatTextView NomeUser, CognomeUser, EmailUser, DescrizioneUser, DataNascitaUser, SessoUser;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profilo_altro_utente);
        UsernameAltroUtente = getIntent().getExtras().getString("UsernameAltroUtente");
        UsernameProprietario = getIntent().getExtras().getString("UsernameProprietario");
        retrofitServiceDBInterno = RetrofitClientDBInterno.getClient().create(RetrofitServiceDBInterno.class);
        ImmagineProfilo = findViewById(R.id.immagine_profilo);
        UsernameProfilo = findViewById(R.id.Username_Profilo);
        LayoutNomeAltroUtente = findViewById(R.id.layout_nome_altro_utente);
        LayoutCognomeAltroUtente = findViewById(R.id.layout_cognome_altro_utente);
        LayoutEmailAltroUtente = findViewById(R.id.layout_email_altro_utente);
        LayoutNascitaAltroUtente = findViewById(R.id.layout_nascita_altro_utente);
        LayoutSessoAltroUtente = findViewById(R.id.layout_sesso_altro_utente);
        NumeroRecensioniScritte = findViewById(R.id.Numero_recensioni_scritte);
        VaiRecensioniAltroUtente = findViewById(R.id.vai_recensioni_altro_utente);
        VediAmici = findViewById(R.id.VediAmici);
        VaiFilmInComune = findViewById(R.id.Vai_film_in_comune);
        ImmagineCopertina = findViewById(R.id.foto_copertina);
        Previously = findViewById(R.id.previously);
        FilmInComune = findViewById(R.id.Film_in_comune);
        Amici = findViewById(R.id.amici_inc);
        NumeroAmici = findViewById(R.id.Numero_amici);
        NomeUser = findViewById(R.id.Nome_user);
        CognomeUser = findViewById(R.id.Cognome_user);
        EmailUser = findViewById(R.id.Email_User);
        DescrizioneUser = findViewById(R.id.Descrizione_User);
        DataNascitaUser = findViewById(R.id.Data_nascita_user);
        SessoUser = findViewById(R.id.Sesso_user);
        ListeVisibili = findViewById(R.id.liste_visibili);
        Previously.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                onBackPressed();
            }
        });
        Call<DBModelVerifica> VerificaAmiciziaCall = retrofitServiceDBInterno.VerificaSeAmico(UsernameProprietario,UsernameAltroUtente);
        VerificaAmiciziaCall.enqueue(new Callback<DBModelVerifica>() {
            @Override public void onResponse(@NonNull Call<DBModelVerifica> call,@NonNull Response<DBModelVerifica> response) {
                DBModelVerifica dbModelVerifica = response.body();
                if(dbModelVerifica != null) {
                    List<DBModelVerificaResults> modelVerificaResults = dbModelVerifica.getResults();
                    if (modelVerificaResults.get(0).getCodVerifica() == 1) {
                            Call<DBModelProfiloAltroUtenteResponce> amicoCall = retrofitServiceDBInterno.PrendiAmico(UsernameAltroUtente,UsernameProprietario);
                            amicoCall.enqueue(new Callback<DBModelProfiloAltroUtenteResponce>() {
                                @Override public void onResponse(@NonNull Call<DBModelProfiloAltroUtenteResponce> call,@NonNull Response<DBModelProfiloAltroUtenteResponce> response) {
                                 DBModelProfiloAltroUtenteResponce dbModelProfiloAltroUtenteResponce = response.body();
                                 if(dbModelProfiloAltroUtenteResponce != null){
                                     List<DBModelProfiloAltroUtente> altroUtenteList = dbModelProfiloAltroUtenteResponce.getResults();
                                     if(!(altroUtenteList.isEmpty())){
                                         DettagliAmico(altroUtenteList);
                                     }else{
                                         Toast.makeText(ActivityProfiloAltroUtente.this, "Nessun dettaglio trovato" , Toast.LENGTH_SHORT).show();
                                     }
                                 } else{
                                     Toast.makeText(ActivityProfiloAltroUtente.this, "Impossibile trovare i dettagli" , Toast.LENGTH_SHORT).show();
                                 }
                                }
                                @Override public void onFailure(@NonNull Call<DBModelProfiloAltroUtenteResponce> call,@NonNull Throwable t) {
                                    Toast.makeText(ActivityProfiloAltroUtente.this, "Ops qualcosa è andato storto" , Toast.LENGTH_SHORT).show();
                                }
                            });
                    }else{
                        Call<DBModelProfiloAltroUtenteResponce> utenteCall = retrofitServiceDBInterno.PrendiUser(UsernameAltroUtente);
                        utenteCall.enqueue(new Callback<DBModelProfiloAltroUtenteResponce>() {
                            @Override public void onResponse(@NonNull Call<DBModelProfiloAltroUtenteResponce> call,@NonNull Response<DBModelProfiloAltroUtenteResponce> response) {
                                DBModelProfiloAltroUtenteResponce dbModelProfiloAltroUtenteResponce = response.body();
                                if(dbModelProfiloAltroUtenteResponce != null){
                                    List<DBModelProfiloAltroUtente> altroUtenteList = dbModelProfiloAltroUtenteResponce.getResults();
                                    if(!(altroUtenteList.isEmpty())){
                                        DettagliUser(altroUtenteList);
                                    }else{
                                        Toast.makeText(ActivityProfiloAltroUtente.this, "Nessun dettaglio trovato" , Toast.LENGTH_SHORT).show();
                                    }
                                } else{
                                    Toast.makeText(ActivityProfiloAltroUtente.this, "Impossibile trovare i dettagli" , Toast.LENGTH_SHORT).show();
                                }
                            }
                            @Override public void onFailure(@NonNull Call<DBModelProfiloAltroUtenteResponce> call,@NonNull Throwable t) {
                                Toast.makeText(ActivityProfiloAltroUtente.this, "Ops qualcosa è andato storto" , Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }else{
                    Toast.makeText(ActivityProfiloAltroUtente.this, "Impossibile verificare amicizia." , Toast.LENGTH_SHORT).show();
                }
            }
            @Override public void onFailure(@NonNull Call<DBModelVerifica> call,@NonNull Throwable t) {
                Toast.makeText(ActivityProfiloAltroUtente.this, "Ops qualcosa è andato storto." , Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void DettagliUser(List<DBModelProfiloAltroUtente> altroUtenteList) {
        if(altroUtenteList.get(0).getFoto_Profilo() != null){
            Glide.with(ActivityProfiloAltroUtente.this).load(altroUtenteList.get(0).getFoto_Profilo()).into(ImmagineProfilo);
        }else{
            ImmagineProfilo.setImageResource(R.drawable.ic_baseline_person_24_orange);
        }
        if(altroUtenteList.get(0).getFoto_Copertina() != null){
            Glide.with(ActivityProfiloAltroUtente.this).load(altroUtenteList.get(0).getFoto_Copertina()).into(ImmagineCopertina);
        }
        UsernameProfilo.setText(altroUtenteList.get(0).getUserName());
        NumeroRecensioniScritte.setText(altroUtenteList.get(0).getRecensioni_Scritte());
        Amici.setText("Amici in comune");
        NumeroAmici.setText(altroUtenteList.get(0).getTotale_Amici());
        FilmInComune.setText(String.valueOf(0));
        if(altroUtenteList.get(0).getDescrizione_Profilo() != null){
            DescrizioneUser.setText(altroUtenteList.get(0).getDescrizione_Profilo());
        }else{
            DescrizioneUser.setText("Descrizione non inserita dall'user");
        }
        ImmagineProfilo.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                Intent visualizzaImmagineintent = new Intent(ActivityProfiloAltroUtente.this, VisualizzaImmaginiActivity.class);
                visualizzaImmagineintent.putExtra("image_url", altroUtenteList.get(0).getFoto_Profilo());
                startActivity(visualizzaImmagineintent);
            }
        });
        ImmagineCopertina.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                Intent visualizzaImmagineintent = new Intent(ActivityProfiloAltroUtente.this, VisualizzaImmaginiActivity.class);
                visualizzaImmagineintent.putExtra("image_url", altroUtenteList.get(0).getFoto_Copertina());
                startActivity(visualizzaImmagineintent);
            }
        });
        Call<DBModelDataListeFilmResponce> filmCall = retrofitServiceDBInterno.PrendiFilmUser(UsernameAltroUtente);
        filmCall.enqueue(new Callback<DBModelDataListeFilmResponce>() {
            @Override public void onResponse(@NonNull Call<DBModelDataListeFilmResponce> call,@NonNull Response<DBModelDataListeFilmResponce> response) {
                DBModelDataListeFilmResponce dbModelListeFilmResponse = response.body();
                if(dbModelListeFilmResponse != null){
                   List<DBModelDataListeFilm> listeFilms = dbModelListeFilmResponse.getListeFilms();
                    if(!(listeFilms.isEmpty())){
                        layoutManager = new LinearLayoutManager(ActivityProfiloAltroUtente.this);
                        ListeVisibili.setLayoutManager(layoutManager);
                        ListeVisibili.setHasFixedSize(false);
                        altroUtenteAmicoAdapter = new AltroUtenteAmicoAdapter(ActivityProfiloAltroUtente.this, listeFilms, UsernameAltroUtente, UsernameProprietario);
                        ListeVisibili.setAdapter(altroUtenteAmicoAdapter);
                        altroUtenteAmicoAdapter.notifyDataSetChanged();
                    }else{
                        Toast.makeText(ActivityProfiloAltroUtente.this, "Nessuna lista da mostrare agli estranei.",Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(ActivityProfiloAltroUtente.this, "Impossibile prendere liste film.",Toast.LENGTH_SHORT).show();
                }
            }
            @Override public void onFailure(@NonNull Call<DBModelDataListeFilmResponce> call,@NonNull Throwable t) {
                Toast.makeText(ActivityProfiloAltroUtente.this, "Ops qualcosa è andato storto.",Toast.LENGTH_SHORT).show();
            }
        });
        VaiRecensioniAltroUtente.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                Intent intent2 = new Intent(ActivityProfiloAltroUtente.this, ProprieRecensioniActivity.class);
                intent2.putExtra("Nome_Utente", UsernameAltroUtente);
                startActivity(intent2);
            }
        });
        VediAmici.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                Intent intent2 = new Intent(ActivityProfiloAltroUtente.this, VisualizzaAmiciInComuneActivity.class);
                intent2.putExtra("Nome_Utente", UsernameAltroUtente);
                intent2.putExtra("Nome_Proprietario", UsernameProprietario);
                startActivity(intent2);
            }
        });
        VaiFilmInComune.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                Toast.makeText(ActivityProfiloAltroUtente.this, "Solo gli amici possono vedere i film in comune.",Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void DettagliAmico(List<DBModelProfiloAltroUtente> altroUtenteList) {
        if(altroUtenteList.get(0).getFoto_Profilo() != null){
            Glide.with(ActivityProfiloAltroUtente.this).load(altroUtenteList.get(0).getFoto_Profilo()).into(ImmagineProfilo);
        }else{
            ImmagineProfilo.setImageResource(R.drawable.ic_baseline_person_24_orange);
        }
        if(altroUtenteList.get(0).getFoto_Copertina() != null){
            Glide.with(ActivityProfiloAltroUtente.this).load(altroUtenteList.get(0).getFoto_Copertina()).into(ImmagineCopertina);
        }
        UsernameProfilo.setText(altroUtenteList.get(0).getUserName());
        NumeroRecensioniScritte.setText(altroUtenteList.get(0).getRecensioni_Scritte());
        NumeroAmici.setText(altroUtenteList.get(0).getTotale_Amici());
        FilmInComune.setText(altroUtenteList.get(0).getFilm_In_Comune());
        if(altroUtenteList.get(0).getNome() != null) {
            NomeUser.setText(altroUtenteList.get(0).getNome());
            LayoutNomeAltroUtente.setVisibility(View.VISIBLE);
        }
        if(altroUtenteList.get(0).getCognome() != null) {
            CognomeUser.setText(altroUtenteList.get(0).getCognome());
            LayoutCognomeAltroUtente.setVisibility(View.VISIBLE);
        }
        if(altroUtenteList.get(0).getEmail() != null) {
            EmailUser.setText(altroUtenteList.get(0).getEmail());
            LayoutEmailAltroUtente.setVisibility(View.VISIBLE);
        }
        if(altroUtenteList.get(0).getDescrizione_Profilo() != null){
            DescrizioneUser.setText(altroUtenteList.get(0).getDescrizione_Profilo());
        }else{
            DescrizioneUser.setText("Descrizione non inserita dall'user");
        }
        if(altroUtenteList.get(0).getData_Nascita() != null) {
            DataNascitaUser.setText(altroUtenteList.get(0).getData_Nascita());
            LayoutNascitaAltroUtente.setVisibility(View.VISIBLE);
        }
        if(altroUtenteList.get(0).getSesso() != null) {
            SessoUser.setText(altroUtenteList.get(0).getSesso());
            LayoutSessoAltroUtente.setVisibility(View.VISIBLE);
        }
        ImmagineProfilo.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                Intent visualizzaImmagineintent = new Intent(ActivityProfiloAltroUtente.this, VisualizzaImmaginiActivity.class);
                visualizzaImmagineintent.putExtra("image_url", altroUtenteList.get(0).getFoto_Profilo());
                startActivity(visualizzaImmagineintent);
            }
        });
        ImmagineCopertina.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                Intent visualizzaImmagineintent = new Intent(ActivityProfiloAltroUtente.this, VisualizzaImmaginiActivity.class);
                visualizzaImmagineintent.putExtra("image_url", altroUtenteList.get(0).getFoto_Copertina());
                startActivity(visualizzaImmagineintent);
            }
        });
        Call<DBModelDataListeFilmResponce> filmAmiciCall = retrofitServiceDBInterno.PrendiFilmAmico(UsernameAltroUtente,UsernameProprietario);
        filmAmiciCall.enqueue(new Callback<DBModelDataListeFilmResponce>() {
            @Override public void onResponse(@NonNull Call<DBModelDataListeFilmResponce> call,@NonNull Response<DBModelDataListeFilmResponce> response) {
                DBModelDataListeFilmResponce dbModelListeFilmResponse = response.body();
                if(dbModelListeFilmResponse != null){
                    List<DBModelDataListeFilm> listeFilms = dbModelListeFilmResponse.getListeFilms();
                    if(!(listeFilms.isEmpty())){
                        layoutManager = new LinearLayoutManager(ActivityProfiloAltroUtente.this);
                        ListeVisibili.setLayoutManager(layoutManager);
                        ListeVisibili.setHasFixedSize(false);
                        altroUtenteAmicoAdapter = new AltroUtenteAmicoAdapter(ActivityProfiloAltroUtente.this, listeFilms, UsernameAltroUtente, UsernameProprietario);
                        ListeVisibili.setAdapter(altroUtenteAmicoAdapter);
                        altroUtenteAmicoAdapter.notifyDataSetChanged();
                    }else{
                        Toast.makeText(ActivityProfiloAltroUtente.this, "Nessuna lista da mostrare agli estranei.",Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(ActivityProfiloAltroUtente.this, "Impossibile prendere liste film.",Toast.LENGTH_SHORT).show();
                }
            }
            @Override public void onFailure(@NonNull Call<DBModelDataListeFilmResponce> call,@NonNull Throwable t) {

            }
        });
        VaiRecensioniAltroUtente.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                Intent intent2 = new Intent(ActivityProfiloAltroUtente.this, ProprieRecensioniActivity.class);
                intent2.putExtra("Nome_Utente", UsernameAltroUtente);
                intent2.putExtra("Nome_Proprietario", UsernameProprietario);
                startActivity(intent2);
            }
        });
        VediAmici.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                Intent intent2 = new Intent(ActivityProfiloAltroUtente.this, ListeAmiciActivity.class);
                intent2.putExtra("Nome_Utente", UsernameAltroUtente);
                intent2.putExtra("Nome_Proprietario", UsernameProprietario);
                startActivity(intent2);
            }
        });
        VaiFilmInComune.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                Intent intent2 = new Intent(ActivityProfiloAltroUtente.this, FilmInComuneActivity.class);
                intent2.putExtra("Nome_Utente", UsernameAltroUtente);
                intent2.putExtra("Nome_Proprietario", UsernameProprietario);
                startActivity(intent2);
            }
        });

    }
}