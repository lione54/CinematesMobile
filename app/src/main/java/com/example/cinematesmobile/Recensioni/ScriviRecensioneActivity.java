package com.example.cinematesmobile.Recensioni;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;

import android.os.Bundle;
import android.view.View;
import android.widget.RatingBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.cinematesmobile.R;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class ScriviRecensioneActivity extends AppCompatActivity {

    private String NomeUtente, UrlImmagine;
    private AppCompatTextView Nome;
    private Integer Id_Film;
    private AppCompatEditText CorpoRecensione;
    private AppCompatButton Invia;
    private AppCompatButton Annulla;
    private RatingBar Voto;
    private AlertDialog.Builder dialogBilder;
    private AlertDialog MettiZero;
    private AppCompatButton Si, No;
    private AppCompatImageButton Previously;
    private CircleImageView ImmagineRece;
    private static final String INSURL = "http://192.168.1.9/cinematesdb/AggiungiRecensioneAlDatabase.php";

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrivi_recensione);
        NomeUtente= getIntent().getExtras().getString("Nome_Utente");
        Id_Film = getIntent().getExtras().getInt("Id_Film");
        UrlImmagine = getIntent().getExtras().getString("Foto_Profilo");;
        ImmagineRece = findViewById(R.id.immagineRece);
        Nome = findViewById(R.id.nome_utente);
        CorpoRecensione = findViewById(R.id.corp_rece);
        Previously = findViewById(R.id.previously);
        Invia = findViewById(R.id.invia_button);
        Annulla = findViewById(R.id.annu_button);
        Voto = findViewById(R.id.Votofilm);
        if(UrlImmagine.equals("null")){
            ImmagineRece.setImageResource(R.drawable.ic_baseline_person_24);
        }else{
            Glide.with(this).load(UrlImmagine).into(ImmagineRece);
        }
        Nome.setText(NomeUtente);
        Invia.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                if(CorpoRecensione.length() > 0 && CorpoRecensione.length() < 600){
                    float Punteggio = Voto.getRating();
                    String Recensione = CorpoRecensione.getText().toString();
                    if(Punteggio == 0){
                        dialogBilder = new AlertDialog.Builder(ScriviRecensioneActivity.this);
                        final View PopUpView = getLayoutInflater().inflate(R.layout.disclaimerzero, null);
                        Si = (AppCompatButton) PopUpView.findViewById(R.id.conf_button);
                        No = (AppCompatButton) PopUpView.findViewById(R.id.anal_button);
                        dialogBilder.setView(PopUpView);
                        MettiZero = dialogBilder.create();
                        MettiZero.show();
                        Si.setOnClickListener(new View.OnClickListener() {
                            @Override public void onClick(View v) {
                                Inserisci_Recensione(Id_Film, Recensione, Punteggio, NomeUtente);
                                MettiZero.dismiss();
                                onBackPressed();
                            }
                        });
                        No.setOnClickListener(new View.OnClickListener() {
                            @Override public void onClick(View v) {
                                MettiZero.dismiss();
                            }
                        });
                    }else{
                        Inserisci_Recensione(Id_Film, Recensione, Punteggio, NomeUtente);
                        onBackPressed();
                    }
                }else{
                    if(CorpoRecensione.length() <= 0){
                        Toast.makeText(ScriviRecensioneActivity.this, "Scrivi Qualcosa", Toast.LENGTH_SHORT).show();
                    }else if(CorpoRecensione.length() >= 600){
                        Toast.makeText(ScriviRecensioneActivity.this, "Superato La Lunghezza Massima Della Recensione", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        Annulla.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                Toast.makeText(ScriviRecensioneActivity.this, "Inserimento Recensione Annullato", Toast.LENGTH_SHORT).show();
                onBackPressed();
            }
        });
        Previously.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                Toast.makeText(ScriviRecensioneActivity.this, "Inserimento Recensione Annullato", Toast.LENGTH_SHORT).show();
                onBackPressed();
            }
        });
    }

    private void Inserisci_Recensione(Integer id_film, String recensione, float punteggio, String nomeUtente) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, INSURL, new com.android.volley.Response.Listener<String>() {
            @Override public void onResponse(String response){
                Toast.makeText(ScriviRecensioneActivity.this , "Recensione Aggiunta" , Toast.LENGTH_SHORT).show();
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override public void onErrorResponse(VolleyError error) {
                Toast.makeText(ScriviRecensioneActivity.this , error.toString(), Toast.LENGTH_LONG).show();
            }
        }) {
            @NotNull @Override protected Map<String, String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("Corpo_Recensione", recensione);
                params.put("Valutazione",String.valueOf(punteggio));
                params.put("Id_Film_Recensito", String.valueOf(id_film));
                params.put("User_Recensore", nomeUtente);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
}