package com.example.cinematesmobile.Search;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.cinematesmobile.Frag.Adapter.AltroUtenteAmicoAdapter;
import com.example.cinematesmobile.Frag.Adapter.MovieListPrefAdapter;
import com.example.cinematesmobile.Frag.Adapter.NotificationAdapter;
import com.example.cinematesmobile.Frag.Model.DBModelDataListeFilm;
import com.example.cinematesmobile.Frag.Model.DBNotificheModelRichiesteAmicizia;
import com.example.cinematesmobile.Frag.Model.DBNotificheModelSegnalazioni;
import com.example.cinematesmobile.R;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class ActivityProfiloAltroUtente extends AppCompatActivity {

    private String UsernameAltroUtente, UsernameProprietario;
    private static final String AMURL = "http://192.168.178.48/cinematesdb/PrendiAltroUserDaDBAmico.php";
    private static final String USURL = "http://192.168.178.48/cinematesdb/PrendiAltroUserDaDB.php";
    private static final String VERURL = "http://192.168.178.48/cinematesdb/VerificaSeAmico.php";
    private String Username, Nome, Cognome, Email, Foto_Profilo, Descrizione, DataNascita, Sesso;
    private LinearLayout LayoutNomeAltroUtente, LayoutCognomeAltroUtente, LayoutEmailAltroUtente, LayoutNascitaAltroUtente, LayoutSessoAltroUtente;
    private Integer  Recensioni_Scritte, TotaleAmici, Film;
    public CircleImageView ImmagineProfilo;
    private RecyclerView ListeVisibili;
    private AppCompatImageButton Previously;
    private RecyclerView.LayoutManager layoutManager;
    private AltroUtenteAmicoAdapter altroUtenteAmicoAdapter;
    public AppCompatTextView UsernameProfilo, NumeroRecensioniScritte, NumeroAmici, Amici, FilmInComune;
    public AppCompatTextView NomeUser, CognomeUser, EmailUser, DescrizioneUser, DataNascitaUser, SessoUser;
    private List<DBModelDataListeFilm> listeFilmList = new ArrayList<>();
    public static final String JSON_ARRAY = "dbdata";

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profilo_altro_utente);
        UsernameAltroUtente = getIntent().getExtras().getString("UsernameAltroUtente");
        UsernameProprietario = getIntent().getExtras().getString("UsernameProprietario");
        ImmagineProfilo = findViewById(R.id.immagine_profilo);
        UsernameProfilo = findViewById(R.id.Username_Profilo);
        LayoutNomeAltroUtente = findViewById(R.id.layout_nome_altro_utente);
        LayoutCognomeAltroUtente = findViewById(R.id.layout_cognome_altro_utente);
        LayoutEmailAltroUtente = findViewById(R.id.layout_email_altro_utente);
        LayoutNascitaAltroUtente = findViewById(R.id.layout_nascita_altro_utente);
        LayoutSessoAltroUtente = findViewById(R.id.layout_sesso_altro_utente);
        NumeroRecensioniScritte = findViewById(R.id.Numero_recensioni_scritte);
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
        VerificaAmicizia(UsernameProprietario,UsernameAltroUtente);
    }

    private void VerificaAmicizia(String usernameProprietario, String username) {
        final int[] validiti = new int[1];
        StringRequest stringRequest = new StringRequest(Request.Method.POST, VERURL, new com.android.volley.Response.Listener<String>() {
            @Override public void onResponse(String response) {
                try{
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray array = jsonObject.getJSONArray(JSON_ARRAY);
                    for(int i = 0; i < array.length(); i++) {
                        JSONObject object = array.getJSONObject(i);
                        String respo = object.getString("Amicizia");
                        validiti[0] = Integer.parseInt(respo);
                    }
                    if(validiti[0] == 1) {
                        PrendiInfoAltroUtenteAmico(UsernameAltroUtente);
                    }else{
                        PrendiInfoAltroUtente(UsernameAltroUtente);
                    }
                }catch (Exception e){
                    Toast.makeText(ActivityProfiloAltroUtente.this, "" + e, Toast.LENGTH_LONG).show();
                }
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override public void onErrorResponse(VolleyError error) {
                Toast.makeText(ActivityProfiloAltroUtente.this, error.toString(), Toast.LENGTH_LONG).show();
            }
        })
        {
            @NotNull @Override protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("User_Proprietario", usernameProprietario);
                params.put("UsernameAltroUtente",username);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(ActivityProfiloAltroUtente.this);
        requestQueue.add(stringRequest);
    }

    private void PrendiInfoAltroUtente(String usernameAltroUtente) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, USURL, new com.android.volley.Response.Listener<String>() {
            @Override public void onResponse(String response){
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray array = jsonObject.getJSONArray(JSON_ARRAY);
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject object = array.getJSONObject(i);
                        String Divisore = object.getString("Divisore");
                        if(Divisore.equals("InfoUtente")) {
                            Username = object.getString("UserName");
                            String str_rece = object.getString("Recensioni_Scritte");
                            Foto_Profilo = object.getString("Foto_Profilo");
                            Descrizione = object.getString("Descrizione_Profilo");
                            String str_amici = object.getString("Amici_In_Comune");
                            Recensioni_Scritte = Integer.valueOf(str_rece);
                            TotaleAmici = Integer.valueOf(str_amici);
                        }else {
                            String TipoLista = object.getString("Tipo_Lista");
                            String Descrizione = object.getString("Descrizione");
                            DBModelDataListeFilm dbModelDataListeFilm = new DBModelDataListeFilm(TipoLista, Descrizione);
                            listeFilmList.add(dbModelDataListeFilm);
                        }
                    }
                    if(!(Foto_Profilo.equals("null"))){
                        String Foto = "http://192.168.1.9/cinematesdb/"+ Foto_Profilo;
                        Glide.with(ActivityProfiloAltroUtente.this).load(Foto).into(ImmagineProfilo);
                    }else{
                        ImmagineProfilo.setImageResource(R.drawable.ic_baseline_person_24_cineblack);
                    }
                    UsernameProfilo.setText(Username);
                    NumeroRecensioniScritte.setText(String.valueOf(Recensioni_Scritte));
                    Amici.setText("Amici in comune");
                    NumeroAmici.setText(String.valueOf(TotaleAmici));
                    if(!(Descrizione.equals("null"))){
                        DescrizioneUser.setText(Descrizione);
                    }else{
                        DescrizioneUser.setText("Descrizione non inserita dall'user");
                    }
                    if(!(listeFilmList.isEmpty())){
                        layoutManager = new LinearLayoutManager(ActivityProfiloAltroUtente.this);
                        ListeVisibili.setLayoutManager(layoutManager);
                        ListeVisibili.setHasFixedSize(false);
                        altroUtenteAmicoAdapter = new AltroUtenteAmicoAdapter(ActivityProfiloAltroUtente.this, listeFilmList, usernameAltroUtente);
                        ListeVisibili.setAdapter(altroUtenteAmicoAdapter);
                        altroUtenteAmicoAdapter.notifyDataSetChanged();
                    }else{
                        Toast.makeText(ActivityProfiloAltroUtente.this, "Nessuna lista da mostrare agli estranei.",Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override public void onErrorResponse(VolleyError error) {
                Toast.makeText(ActivityProfiloAltroUtente.this , error.toString(), Toast.LENGTH_LONG).show();
            }
        }) {
            @NotNull
            @Override protected Map<String, String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("AltroUser", usernameAltroUtente);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void PrendiInfoAltroUtenteAmico(String username) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, AMURL, new com.android.volley.Response.Listener<String>() {
            @Override public void onResponse(String response){
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray array = jsonObject.getJSONArray(JSON_ARRAY);
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject object = array.getJSONObject(i);
                        String Divisore = object.getString("Divisore");
                        if(Divisore.equals("InfoUtente")) {
                            Username = object.getString("UserName");
                            Nome = object.getString("Nome");
                            Cognome = object.getString("Cognome");
                            Email = object.getString("Email");
                            String str_rece = object.getString("Recensioni_Scritte");
                            Foto_Profilo = object.getString("Foto_Profilo");
                            Descrizione = object.getString("Descrizione_Profilo");
                            DataNascita = object.getString("Data_Nascita");
                            Sesso = object.getString("Sesso");
                            String str_amici = object.getString("Totale_Amici");
                            String str_film = object.getString("Film_In_Comune");
                            Recensioni_Scritte = Integer.valueOf(str_rece);
                            TotaleAmici = Integer.valueOf(str_amici);
                            Film = Integer.valueOf(str_film);
                        }else {
                            String TipoLista = object.getString("Tipo_Lista");
                            String Descrizione = object.getString("Descrizione");
                            DBModelDataListeFilm dbModelDataListeFilm = new DBModelDataListeFilm(TipoLista, Descrizione);
                            listeFilmList.add(dbModelDataListeFilm);
                        }
                    }
                    if(!(Foto_Profilo.equals("null"))){
                        String Foto = "http://192.168.178.48/cinematesdb/"+ Foto_Profilo;
                        Glide.with(ActivityProfiloAltroUtente.this).load(Foto).into(ImmagineProfilo);
                    }else{
                        ImmagineProfilo.setImageResource(R.drawable.ic_baseline_person_24_cineblack);
                    }
                    UsernameProfilo.setText(Username);
                    NumeroRecensioniScritte.setText(String.valueOf(Recensioni_Scritte));
                    NumeroAmici.setText(String.valueOf(TotaleAmici));
                    FilmInComune.setText(String.valueOf(Film));
                    if(Nome != null) {
                        NomeUser.setText(Nome);
                        LayoutNomeAltroUtente.setVisibility(View.VISIBLE);
                    }
                    if(Cognome != null) {
                        CognomeUser.setText(Cognome);
                        LayoutCognomeAltroUtente.setVisibility(View.VISIBLE);
                    }
                    if(Email != null) {
                        EmailUser.setText(Email);
                        LayoutEmailAltroUtente.setVisibility(View.VISIBLE);
                    }
                    if(!(Descrizione.equals("null"))){
                        DescrizioneUser.setText(Descrizione);
                    }else{
                        DescrizioneUser.setText("Descrizione non inserita dall'user");
                    }
                    if(DataNascita != null) {
                        DataNascitaUser.setText(DataNascita);
                        LayoutNascitaAltroUtente.setVisibility(View.VISIBLE);
                    }
                    if(Sesso != null) {
                        SessoUser.setText(Sesso);
                        LayoutSessoAltroUtente.setVisibility(View.VISIBLE);
                    }
                    if(!(listeFilmList.isEmpty())){
                        layoutManager = new LinearLayoutManager(ActivityProfiloAltroUtente.this);
                        ListeVisibili.setLayoutManager(layoutManager);
                        ListeVisibili.setHasFixedSize(false);
                        altroUtenteAmicoAdapter = new AltroUtenteAmicoAdapter(ActivityProfiloAltroUtente.this, listeFilmList, username);
                        ListeVisibili.setAdapter(altroUtenteAmicoAdapter);
                        altroUtenteAmicoAdapter.notifyDataSetChanged();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override public void onErrorResponse(VolleyError error) {
                Toast.makeText(ActivityProfiloAltroUtente.this , error.toString(), Toast.LENGTH_LONG).show();
            }
        }) {
            @NotNull
            @Override protected Map<String, String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("AltroUser", username);
                params.put("User_Proprietario", UsernameProprietario);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
}