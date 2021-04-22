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
import com.example.cinematesmobile.R;
import com.example.cinematesmobile.Recensioni.Adapter.RecensioniAdapter;
import com.example.cinematesmobile.Recensioni.Model.DBModelRecensioni;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    private boolean firstuse = true;
    private List<DBModelRecensioni> recensioniList;
    private RecensioniAdapter recensioniAdapter;
    public static final String JSON_ARRAY = "dbdata";
    private static final String RECURL = "http://192.168.178.48/cinematesdb/PrendiRecensioniDaDB.php";
    private static final String FOTURL = "http://192.168.178.48/cinematesdb/PrendiFotoUser.php";
    private static final String VERURL = "http://192.168.178.48/cinematesdb/VerificaSeRecensionePresente.php";

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
        recensioniList = new ArrayList<>();
        RecensioniScritte.setLayoutManager(new LinearLayoutManager(RecensioniActivity.this, LinearLayoutManager.HORIZONTAL, false));
        PrendiRecensioni(Titolo_film, Utente);
        firstuse = false;
        Glide.with(RecensioniActivity.this).load(Poster).into(PosterFilmRece);
        ScriviRecensione.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                PrendiFotoProfiloUtente(Utente,Titolo_film);
            }
        });
        Previously.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void PrendiFotoProfiloUtente(String utente, String titolo_film) {
        final String[] Foto = new String[1];
        StringRequest stringRequest = new StringRequest(Request.Method.POST, FOTURL, new com.android.volley.Response.Listener<String>() {
            @Override public void onResponse(String response) {
                try{
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray array = jsonObject.getJSONArray(JSON_ARRAY);
                    for(int i = 0; i < array.length(); i++) {
                        JSONObject object = array.getJSONObject(i);
                        Foto[0] = object.getString("Foto_Profilo");
                    }
                    String str = Foto[0];
                    String Foto_Mod = "http://192.168.178.48/cinematesdb/"+ str;
                    verificaSePresente(titolo_film, Foto_Mod, utente);
                }catch (Exception e){
                    Toast.makeText(RecensioniActivity.this, "" + e, Toast.LENGTH_LONG).show();
                }
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override public void onErrorResponse(VolleyError error) {
                Toast.makeText(RecensioniActivity.this, error.toString(), Toast.LENGTH_LONG).show();
            }
        })
        {
            @NotNull @Override protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("User", utente);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    @Override protected void onResume() {
        super.onResume();
        firstuse = false;
        PrendiRecensioni(Titolo_film, Utente);
    }

    private void verificaSePresente(String titolo_film, String Foto, String nomeUtente) {
        final int[] validiti = new int[1];
        String titoloMod = titolo_film.replaceAll("'", "/");
        StringRequest stringRequest = new StringRequest(Request.Method.POST, VERURL, new com.android.volley.Response.Listener<String>() {
            @Override public void onResponse(String response) {
                try{
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray array = jsonObject.getJSONArray(JSON_ARRAY);
                    for(int i = 0; i < array.length(); i++) {
                        JSONObject object = array.getJSONObject(i);
                        String respo = object.getString("Id_Film_Recensito");
                        validiti[0] = Integer.parseInt(respo);
                    }
                    if(validiti[0] == 0) {
                        Intent intent2 = new Intent(RecensioniActivity.this, ScriviRecensioneActivity.class);
                        intent2.putExtra("Nome_Utente", nomeUtente);
                        intent2.putExtra("Titolo_Film", titolo_film);
                        intent2.putExtra("Foto_Profilo", Foto);
                        startActivity(intent2);
                    }else{
                        Toast.makeText(RecensioniActivity.this , "Ha già inserito la recensione per questo film", Toast.LENGTH_LONG).show();
                    }
                }catch (Exception e){
                    Toast.makeText(RecensioniActivity.this, "" + e, Toast.LENGTH_LONG).show();
                }
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override public void onErrorResponse(VolleyError error) {
                Toast.makeText(RecensioniActivity.this, error.toString(), Toast.LENGTH_LONG).show();
            }
        })
        {
            @NotNull @Override protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("Titolo_Film_Recensito", titoloMod);
                params.put("User_Recensore", nomeUtente);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void PrendiRecensioni(String titolo_film, String utente) {
        String Titolo_Mod = titolo_film.replaceAll("'", "/");
        StringRequest stringRequest = new StringRequest(Request.Method.POST, RECURL, new com.android.volley.Response.Listener<String>() {
            @Override public void onResponse(String response){
                try {
                    if(firstuse == true){
                        JSONObject jsonObject = new JSONObject(response);
                        JSONArray array = jsonObject.getJSONArray(JSON_ARRAY);
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject object = array.getJSONObject(i);
                            String str_id = object.getString("Id_Recensione");
                            String User = object.getString("User_Recensore");
                            String Corpo_Rece = object.getString("Testo_Recensione");
                            String str_Val = object.getString("Valutazione");
                            String Data = object.getString("Data_Pubblicazione_Recensione");
                            String Foto = object.getString("Foto_Profilo");
                            String Foto_Mod = "http://192.168.1.9/cinematesdb/"+ Foto;
                            String Titolo = object.getString("Titolo_Film_Recensito");
                            String titoloMod = Titolo.replaceAll("/", "'");
                            Integer Id_Recensione = Integer.valueOf(str_id);
                            Float Valutazione = Float.valueOf(str_Val);
                            DBModelRecensioni dbModelRecensioni = new DBModelRecensioni(Id_Recensione, Valutazione, User, Data, Corpo_Rece, titoloMod,Foto_Mod);
                            recensioniList.add(dbModelRecensioni);
                            firstuse = false;
                        }
                    }else{
                        recensioniList.clear();
                        JSONObject jsonObject = new JSONObject(response);
                        JSONArray array = jsonObject.getJSONArray(JSON_ARRAY);
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject object = array.getJSONObject(i);
                            String str_id = object.getString("Id_Recensione");
                            String User = object.getString("User_Recensore");
                            String Corpo_Rece = object.getString("Testo_Recensione");
                            String str_Val = object.getString("Valutazione");
                            String Data = object.getString("Data_Pubblicazione_Recensione");
                            String Foto = object.getString("Foto_Profilo");
                            String Foto_Mod = "http://192.168.1.9/cinematesdb/"+ Foto;
                            String Titolo = object.getString("Titolo_Film_Recensito");
                            String titoloMod = Titolo.replaceAll("/", "'");
                            Integer Id_Recensione = Integer.valueOf(str_id);
                            Float Valutazione = Float.valueOf(str_Val);
                            DBModelRecensioni dbModelRecensioni = new DBModelRecensioni(Id_Recensione, Valutazione, User, Data, Corpo_Rece, titoloMod,Foto_Mod);
                            recensioniList.add(dbModelRecensioni);
                        }
                    }
                    if(recensioniList.isEmpty()){
                        RecensioniScritte.setLayoutManager(new LinearLayoutManager(RecensioniActivity.this, LinearLayoutManager.VERTICAL, false));
                        recensioniAdapter = new RecensioniAdapter(RecensioniActivity.this, recensioniList, utente);
                        RecensioniScritte.setAdapter(recensioniAdapter);
                        Toast.makeText(RecensioniActivity.this, "Nessun utente ha recensito questo film",Toast.LENGTH_SHORT).show();
                    }else{
                        RecensioniScritte.setLayoutManager(new LinearLayoutManager(RecensioniActivity.this, LinearLayoutManager.VERTICAL, false));
                        recensioniAdapter = new RecensioniAdapter(RecensioniActivity.this, recensioniList, utente);
                        RecensioniScritte.setAdapter(recensioniAdapter);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override public void onErrorResponse(VolleyError error) {
                Toast.makeText(RecensioniActivity.this , error.toString(), Toast.LENGTH_LONG).show();
            }
        }) {
            @NotNull @Override protected Map<String, String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("Titolo_Film_Recensito", Titolo_Mod);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(RecensioniActivity.this);
        requestQueue.add(stringRequest);
    }

}