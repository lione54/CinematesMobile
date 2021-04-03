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

    private Integer Id_Film;
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
    private static final String RECURL = "http://192.168.1.9/cinematesdb/PrendiRecensioniDaDB.php";

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recensioni);
        Id_Film = getIntent().getExtras().getInt("Id_Film");
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
        recensioniList = new ArrayList<>();
        RecensioniScritte.setLayoutManager(new LinearLayoutManager(RecensioniActivity.this, LinearLayoutManager.HORIZONTAL, false));
        PrendiRecensioni(Id_Film, Utente);
        firstuse = false;
        Glide.with(RecensioniActivity.this).load(Poster).into(PosterFilmRece);
        ScriviRecensione.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                Intent intent2 = new Intent(RecensioniActivity.this, ScriviRecensioneActivity.class);
                intent2.putExtra("Nome_Utente", Utente);
                intent2.putExtra("Id_Film", Id_Film);
                startActivity(intent2);
            }
        });
        Previously.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        firstuse = false;
        PrendiRecensioni(Id_Film, Utente);
    }

    private void PrendiRecensioni(Integer id_film, String utente) {
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
                            Integer Id_Recensione = Integer.valueOf(str_id);
                            Float Valutazione = Float.valueOf(str_Val);
                            DBModelRecensioni dbModelRecensioni = new DBModelRecensioni(Id_Recensione, Valutazione, User, Data, Corpo_Rece, Foto);
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
                            Integer Id_Recensione = Integer.valueOf(str_id);
                            Float Valutazione = Float.valueOf(str_Val);
                            DBModelRecensioni dbModelRecensioni = new DBModelRecensioni(Id_Recensione, Valutazione, User, Data, Corpo_Rece, Foto);
                            recensioniList.add(dbModelRecensioni);
                        }
                    }
                    if(recensioniList.isEmpty()){
                        RecensioniScritte.setLayoutManager(new LinearLayoutManager(RecensioniActivity.this, LinearLayoutManager.VERTICAL, false));
                        recensioniAdapter = new RecensioniAdapter(RecensioniActivity.this, recensioniList);
                        RecensioniScritte.setAdapter(recensioniAdapter);
                        Toast.makeText(RecensioniActivity.this, "Nessun Utente Ha Recensito Questo Film.",Toast.LENGTH_SHORT).show();
                    }else{
                        RecensioniScritte.setLayoutManager(new LinearLayoutManager(RecensioniActivity.this, LinearLayoutManager.VERTICAL, false));
                        recensioniAdapter = new RecensioniAdapter(RecensioniActivity.this, recensioniList);
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
                params.put("Id_Film_Inserito", String.valueOf(id_film));
                params.put("User_Proprietario", utente);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(RecensioniActivity.this);
        requestQueue.add(stringRequest);
    }

}